package model;

import java.io.*;
import java.util.*;

/**
 * Class which represent the index  - saves the dictionary and starts all the process
 */
public class Index {
    public HashMap<String, Term> getDictionary() {
        return dictionary;
    }
    private HashMap<String,Term> dictionary;
    private HashMap<String,Doc> documents;
    private boolean stem;
    private Parser parser;
    private String corpusPath;
    private ReadFile readFile;
    private Stemmer stemmer;
    private Posting posting;
    private String postingPath;
    private HashMap<String,HashMap<String,Integer>> termFreqInDoc;
    private HashMap<String,List<String>> fiveMostRelevantEntitys;
    private List<Term> dictionaryListSorted;
    public String getPostingPath() {
        return postingPath;
    }

    /**
     * Constructor to initialize the index
     * @param stem
     * @param corpusPath
     * @param postingPath
     * @throws IOException
     */
    public Index(boolean stem, String corpusPath, String postingPath) throws IOException {
        this.stem=stem;
        dictionary=new HashMap<>();
        this.corpusPath=corpusPath;
        this.postingPath=postingPath;
        readFile=new ReadFile(this.corpusPath);
        parser = new Parser(readFile.getStopWords());
        posting = new Posting(postingPath);
        documents = new HashMap<>();
        termFreqInDoc = new HashMap<>();
        fiveMostRelevantEntitys=new HashMap<>();
    }

    /**
     * Method to clear the ram when clicking on "reset" button
     */
    public void clearIndex(){
        this.dictionary.clear();
        this.documents.clear();
        this.termFreqInDoc.clear();
    }

    /**
     * Main method of index which creates the index using the read file and parsing classes
     * and after that creating the psoting
     */
    public void startIndex(){
        int counter= 0;
        HashMap<String,String> docs=null;
        List<String> terms=null;
        boolean continueIteration = true;
        while(continueIteration){
            if(!readFile.readFewFiles())
                continueIteration = false;
            docs = readFile.getDocuments();
            if(docs.size() == 0)
                break;
            posting.createNewFile("");
            for(String docID : docs.keySet()){
                counter++;
                terms = parser.parse(docs.get(docID));
                if(stem){
                    List<String> stemmedTerms = new ArrayList<>();
                    for(String term:terms){
                        if(term.matches("\\w+")) {
                            stemmer = new Stemmer();
                            stemmer.add(term.toCharArray(), term.length());
                            stemmer.stem();
                            stemmedTerms.add(stemmer.toString());
                        }
                    }
                    documents.put(docID,new Doc(docID,stemmedTerms.size()));
                    getNumOfTermFreq(docID,stemmedTerms);
                    for (String term: stemmedTerms) {
                        saveTerm(term);
                    }
                }else {
                    documents.put(docID, new Doc(docID, terms.size()));
                    getNumOfTermFreq(docID, terms);
                    for (String term : terms) {
                        saveTerm(term);
                    }
                }
            }
            posting.addDocToPosting(posting.sortPostingElements(termFreqInDoc));
            termFreqInDoc = new HashMap<>();
            docs.clear();
        }
        posting.mergePostingFiles();
        posting.splitPostingToAlphaBet();
        dictionary = posting.combineLines(dictionary,stem);
        posting.deleteABCLFiles();
        saveDictionaryToDisk();
        try {
            writeDocumentsToFile();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to read sort the dictionary alphabetcally
     */
    private void sortDict(){
        dictionaryListSorted = new ArrayList<>();
        for (String termString:dictionary.keySet()) {
            dictionaryListSorted.add(dictionary.get(termString));
        }
        Collections.sort(dictionaryListSorted, new TermComprator());
    }

    /**
     * Getter for the sorted Dictionary
     * @return List of terms
     */
    public List<Term> getDictionaryListSorted() {
        return dictionaryListSorted;
    }

    /**
     * Getter for stem
     * @return true or false
     */
    public boolean isStem() {
        return stem;
    }

    /**
     * Getter for hash between frequencies of term in a specific document
     * @return hashmap
     */
    public HashMap<String, HashMap<String, Integer>> getTermFreqInDoc() {
        return termFreqInDoc;
    }

    /**
     * Getter for hash between doc and its entities
     * @return hashmap
     */
    public HashMap<String,List<String>> getFiveMostRelevantEntitys(){
        return fiveMostRelevantEntitys;
    }



    /**
     * Method which saves the final dictionary to disk for easy retrieving
     */
    private void saveDictionaryToDisk(){
        sortDict();
        FileWriter dict= null;
        String fileName;
        if(!stem)
            fileName = "Dictionary";
        else
            fileName = "Dictionary-S";
        posting.createNewFile(fileName);
        try {
            dict = new FileWriter(posting.getPostingPaths().get(posting.getPostingPaths().size() -1), true);
            for (Term term:dictionaryListSorted) {
                dict.write(term.getTerm() + "#" + String.valueOf(term.getTf()) +"#"+ String.valueOf(term.getLine())+ "\n");
            }
            dict.flush();
            dict.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Readfile object getter
     * @return
     */
    public ReadFile getReadFile() {
        return readFile;
    }

    /**
     * Getter of hashmap
     * @return hashmap
     */
    public HashMap<String, Doc> getDocuments() {
        return documents;
    }


    /**
     *method which loads the dictionary from file
     */
    public void createDictionaryFromFile(){
        dictionary = new HashMap<>();
        BufferedReader reader = null;
        String postingLine;
        String []splitedLine;
        String fileName;
        try {
            if(!stem)
                fileName = "\\Dictionary.txt";

            else
                fileName = "\\Dictionary-s.txt";
            reader = new BufferedReader(new FileReader(postingPath+fileName));
            while ((postingLine = reader.readLine()) != null) {
                splitedLine = postingLine.split("#");
                if (splitedLine.length == 0)
                    continue;
                try{
                    int tf = Integer.valueOf(splitedLine[1]);
                    int line = Integer.valueOf(splitedLine[2]);
                    Term newTerm = new Term(splitedLine[0],tf);
                    newTerm.setLine(line);
                    dictionary.put(splitedLine[0].toLowerCase(),newTerm);
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
            sortDict();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method to create document hashmap from file
     */
    public void createDocumentsFromFile(){
        documents = new HashMap<>();
        BufferedReader reader = null;
        String fileName = "";
        if(stem){
            fileName="Documents-S.txt";
        }
        else
            fileName="Documents.txt";
        String documentLine;
        String []splitedLine;
        try {
            reader = new BufferedReader(new FileReader(postingPath+"\\"+fileName));
            while ((documentLine = reader.readLine()) != null) {
                splitedLine = documentLine.split("!");
                if (splitedLine.length == 0)
                    continue;
                try{
                    String docName = splitedLine[0];
                    String numOfWords = splitedLine[1];
                    String maxFreqOfWord = splitedLine[2];
                    Doc newDoc = new Doc(docName,Integer.valueOf(numOfWords));
                    newDoc.setMostFreqWord(Integer.valueOf(maxFreqOfWord));
                    documents.put(docName,newDoc);
                    List<String> entities = new ArrayList<>();
                    if(splitedLine.length>3){
                        for(int i=3;i<splitedLine.length;i++){
                            entities.add(splitedLine[i]);
                        }
                    }
                    this.fiveMostRelevantEntitys.put(docName,entities);
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        }

        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method to write the document hash to file
     */
    private void writeDocumentsToFile(){
        FileWriter doc= null;
        String fileName;
        if(stem)
            fileName="Documents-s";
        else
            fileName="Documents";
        posting.createNewFile(fileName);
        String lineToWrite = "";
        Doc currDoc = null;
        int counter = 1;
        try {
            doc = new FileWriter(posting.getPostingPaths().get(posting.getPostingPaths().size() -1), true);
            for (String docName:this.documents.keySet()) {
                currDoc=documents.get(docName);
                String numOfWords = String.valueOf(currDoc.getNumOfwords());
                String maxFreqWord = String.valueOf(currDoc.getMostFreqWord());
                List<String> entities = this.fiveMostRelevantEntitys.get(docName);
                lineToWrite = docName +"!"+numOfWords+"!"+maxFreqWord ;
                if(entities != null) {
                    for (String entity : entities) {
                        lineToWrite = lineToWrite + "!" + entity;
                    }
                }
                lineToWrite = lineToWrite+"\n";
                doc.write(lineToWrite);
                counter++;
            }
            doc.flush();
            doc.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * finction which updates the number of appearances of term in a doc
     * @param docID
     * @param terms
     */
    private  void getNumOfTermFreq(String docID ,List<String> terms){
        HashMap<String,Integer> termsFreqMap = new HashMap<>();
        boolean containsEntitys = false;
        int freq = 0;
        for (String termFreq:terms) {
            if(checkIfTermIsEntity(termFreq)|| checkIfAllInCapitalLetter(termFreq)){
                if(termsFreqMap.containsKey(termFreq)){
                    freq=termsFreqMap.get(termFreq);
                    freq++;
                    termsFreqMap.put(termFreq,freq);
                    containsEntitys=true;
                }
                else{
                    termsFreqMap.put(termFreq,1);
                    containsEntitys=true;
                }
            }
            else if(termsFreqMap.containsKey(termFreq.toLowerCase())){
                freq = termsFreqMap.get(termFreq.toLowerCase());
                freq++;
                termsFreqMap.put(termFreq.toLowerCase(),freq);
            }else{
                termsFreqMap.put(termFreq.toLowerCase(),1);
            }
        }
        int max = 0;
        for (Map.Entry<String, Integer> entry : termsFreqMap.entrySet()) {
            if (entry.getValue().compareTo(max) > 0) {
                max = entry.getValue();
            }
        }
        this.documents.get(docID).setMostFreqWord(max);
        termFreqInDoc.put(docID,termsFreqMap);
        if(containsEntitys){
            addRelevantEntitys(docID,termsFreqMap);
        }
    }

    /**
     * Method to check if a given term is an entity - used with addRelevantEntitys
     * @param term
     * @return true or false
     */
    private boolean checkIfTermIsEntity(String term){
        String[] checkEntity = term.split(" ");
        if(checkEntity.length>1){
            try {
                for(String word:checkEntity){
                    if (term.isEmpty() || !(word.charAt(0) >= 'A' && word.charAt(0) <= 'Z'))
                        return false;

                }
            }
            catch (Exception e){
                return false;
            }
        }return true;
    }

    /**
     * Method to add entity of a doc
     * @param docName
     * @param termsFreq
     */
    private void addRelevantEntitys(String docName,HashMap<String,Integer> termsFreq){
        List<String> entities = new ArrayList<>();
        for(String word:termsFreq.keySet()){
            String[] entityArr = word.split(" ");
            if (entityArr.length > 1 || checkIfAllInCapitalLetter(word))
                entities.add(word);
        }
        List<String> sortedEntitys = new ArrayList<>();
        String currMaxEntity = "";
        int maxValue = 0;
        for(int i =0;i<entities.size();i++) {
            currMaxEntity = "";
            maxValue = 0;
            for (String term : entities) {
                String[] entityArr = term.split(" ");
                if (entityArr.length > 1 || checkIfAllInCapitalLetter(term)) {
                    int termValue = termsFreq.get(term);
                    if (termValue > maxValue) {
                        maxValue = termValue;
                        currMaxEntity = term;
                    }
                }
            }
            sortedEntitys.add(currMaxEntity);
            entities.remove(currMaxEntity);
        }
        List<String> fiveRelevantEntities = new ArrayList<>();
        if(sortedEntitys.size()>5){
            for(int i =0;i<5;i++){
                fiveRelevantEntities.add(sortedEntitys.get(i));
            }
            this.fiveMostRelevantEntitys.put(docName,fiveRelevantEntities);
        }
        else
            this.fiveMostRelevantEntitys.put(docName,sortedEntitys);
    }

    /**
     * Method to check if a string is all capitalized - used for recognize entities
     * @param term
     * @return true or false
     */
    private boolean checkIfAllInCapitalLetter(String term){
        for(int i=0;i<term.length();i++){
            if(!(term.charAt(i)>='A'&&term.charAt(i)<='Z'))
                return false;
        }
        return true;
    }


    /**
     * Method to clear the posting
     * @throws IOException
     */
    public void resetPosting() throws IOException {
        List<File> postingPaths = this.posting.getPostingPaths();
        for(File file: postingPaths){
            file.delete();
        }
        parser.clearParser();
        posting.clearPosting();
        readFile.clearFileReader();
        this.clearIndex();
    }
    public void clearPostingFiles(){
        posting.clearFiles();
    }


    /**
     * function which create new term for each token
     * @param term
     */
    private  void saveTerm(String term){
        String termLowerCase = term.toLowerCase();
        Term newTerm = null;
        if(dictionary.containsKey(termLowerCase)){
            newTerm = dictionary.get(termLowerCase);
           if(!term.equals(newTerm.getTerm())){
               newTerm.setTerm(termLowerCase);
           }
        }else{
            newTerm = new Term(term);
            dictionary.put(termLowerCase,newTerm);
        }
        newTerm.setTf(newTerm.getTf() + 1);
    }


    /**
     * Comparators
     */
    public class FreqComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String firstTerm = (String) o1;
            String secondTerm = (String) o2;
            int firstTermFreq = Integer.valueOf(firstTerm.split("#")[1]);
            int secondTermFreq = Integer.valueOf(secondTerm.split("#")[1]);
            return firstTermFreq - secondTermFreq;
        }
    }

    public class TermComprator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Term firstTerm = (Term) o1;
            Term secondTerm = (Term) o2;
            return firstTerm.getTerm().compareTo(secondTerm.getTerm());
        }
    }


}
