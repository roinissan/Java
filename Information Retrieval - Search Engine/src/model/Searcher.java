package model;

import com.medallia.word2vec.Word2VecModel;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class used to search relevant documents of queries - using Rank class
 */
public class Searcher {

    private boolean semantic;
    private Ranker ranker;
    private List<Doc> docList;
    private Index index;
    private HashMap<String,Term> dictionary;
    private HashMap<String,String> postingNames;
    private boolean stem;


    /**
     * Constructor
     * @param semantic
     * @param index
     */
    public Searcher(boolean semantic,Index index){
        this.semantic=semantic;
        this.docList=new ArrayList<>();
        this.index=index;
        this.dictionary=index.getDictionary();
        this.stem=index.isStem();
        initPostingNames();
    }

    /**
     * Method to initiate hashmap for posting file names
     */
    public void initPostingNames(){
        postingNames = new HashMap<>();
        postingNames.put("a","abc");
        postingNames.put("b","abc");
        postingNames.put("c","abc");
        postingNames.put("d","def");
        postingNames.put("e","def");
        postingNames.put("f","def");
        postingNames.put("g","ghi");
        postingNames.put("h","ghi");
        postingNames.put("i","ghi");
        postingNames.put("j","jkl");
        postingNames.put("k","jkl");
        postingNames.put("l","jkl");
        postingNames.put("m","mno");
        postingNames.put("n","mno");
        postingNames.put("o","mno");
        postingNames.put("n","mno");
        postingNames.put("p","pqr");
        postingNames.put("q","pqr");
        postingNames.put("r","pqr");
        postingNames.put("s","stu");
        postingNames.put("t","stu");
        postingNames.put("u","stu");
        postingNames.put("v","vwx");
        postingNames.put("w","vwx");
        postingNames.put("x","vwx");
        postingNames.put("y","yz");
        postingNames.put("z","yz");

    }



    /**
     * Method used to split query to words
     * @param query
     * @return string array of words
     */
    private String[] splitRegularQuery(String query){
        String[] splittedQuery = query.split(" ");
        for(String word:splittedQuery){
            word=word.toLowerCase();
        }
        return splittedQuery;
    }

    /**
     * Method used to split query to words with stem option
     * @param query
     * @return string array of words
     */
    public String[] splittedQueryWithStem(String query){
        String[] splittedQuery = query.split(" ");
        for(String word:splittedQuery){
            word=word.toLowerCase();
        }
        String[] splittedStemmedQuery = new String[splittedQuery.length];
        Stemmer stemmer = new Stemmer();
        int counter = 0;
        for(String term : splittedQuery){
            stemmer = new Stemmer();
            stemmer.add(term.toCharArray(), term.length());
            stemmer.stem();
            splittedStemmedQuery[counter]=stemmer.toString();
            counter++;
        }
        return splittedStemmedQuery;
    }

    /**
     * Method used to get the line of term from its posting file
     * used with getTermFromPosting
     * @param postingName
     * @param line
     * @return
     */
    private String getLineFromPosting(String postingName,int line){
        File postingFile=null;
        BufferedReader br=null;
        String currLine = "";
        int counter = 0;
        try{
            postingFile=new File(index.getPostingPath()+"\\"+postingName+".txt");
            br=new BufferedReader(new FileReader(postingFile));
            while(counter<line){
                currLine = br.readLine();
                counter++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return currLine;
    }


    /**
     * Method used to find the line of term from posting and parse it to doc-frequencies hashmap
     * @param term
     * @param line
     * @return hashmap of docs-frequencies
     */
    private HashMap<String,Integer> getTermFromPosting(String term, int line){
        String firstChar = ""+term.charAt(0);
        firstChar=firstChar.toLowerCase();
        String postingName = "";
        String[] splittedLineFromPosting=null;
        HashMap<String,Integer> docsAndFreqs = new HashMap<>();
        if(firstChar.charAt(0)>='a' && firstChar.charAt(0)<='z'){
            postingName=postingNames.get(firstChar);
        }
        else{
            postingName="chars";
        }
        if(this.index.isStem()){
            postingName=postingName+"-S";
        }
        String lineFromPosting = getLineFromPosting(postingName,line);
        splittedLineFromPosting=lineFromPosting.split("\\|");
        int frequencyOfDoc = 0;
        String freqOfDocNoSign = "";
        for(int i = 0;i<splittedLineFromPosting.length;i++){
            String[] splittedValueFromPosting = splittedLineFromPosting[i].split(":");
            if(i==0){
                for (int j = 1; j < splittedValueFromPosting.length; j = j + 10) {
                    freqOfDocNoSign = splittedValueFromPosting[j + 1].replaceAll("\\|", "");
                    try {
                        frequencyOfDoc = Integer.valueOf(freqOfDocNoSign);
                    }
                    catch (Exception e){
                        frequencyOfDoc=0;
                    }
                    docsAndFreqs.put(splittedValueFromPosting[j], frequencyOfDoc);
                }
            }
            else {
                for (int j = 0; j < splittedValueFromPosting.length; j = j + 10) {
                    freqOfDocNoSign = splittedValueFromPosting[j + 1].replaceAll("\\|", "");
                    try {
                        frequencyOfDoc = Integer.valueOf(freqOfDocNoSign);
                    }
                    catch (Exception e){
                        frequencyOfDoc=0;
                    }
                    docsAndFreqs.put(splittedValueFromPosting[j], frequencyOfDoc);
                }
            }
        }
        return docsAndFreqs;
    }

    /**
     * Method used to write the results of search to file - used for traceval
     * @param queryNum
     * @param documents
     * @param trec_eval_path
     */
    public void writeToResultsFile(String queryNum,List<Doc> documents,String trec_eval_path){
        String line = "";
        String docName = "";
        String rank = "109";
        String sim = "12.1234";
        String runID = "md";
        try {
            File file = new File(trec_eval_path+"\\results.txt");
            if(!file.exists())
                file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            for(Doc document:documents){
                if(document == null)
                    continue;
                docName=document.getDocID().replaceAll(" ","");
                line  = queryNum+" "+"0"+" "+docName+" "+rank+" "+sim+" "+runID+"\n";
                writer.write(line);
            }
            writer.close();
        }
        catch (IOException e){

        }
    }


    /**
     * Main method to start the search procedure
     * @param queryNum
     * @param query
     * @param trec_eval_path
     * @return
     */
    public List<Doc> startQuerySearch(String queryNum,String query,String trec_eval_path){
        String[] splittedQuery=null;
        if (semantic){
            query = query + getSemanticWords(splitRegularQuery(query));
        }
        if(stem){
            splittedQuery=splittedQueryWithStem(query);
        }
        else{
            splittedQuery= splitRegularQuery(query);
        }
        Term currentTerm=null;
        String currWord="";
        int lineNum = 0;
        List<HashMap<String,Integer>> listToRank = new ArrayList<>();
        for(String string:splittedQuery){
            currWord=string.toLowerCase();
            if(dictionary.containsKey(currWord)){
                currentTerm=dictionary.get(currWord);
                lineNum=currentTerm.getLine();
                listToRank.add(getTermFromPosting(currWord,lineNum));
            }
        }
        ranker=new Ranker(semantic,listToRank,index);
        this.docList=ranker.startRank();
        if(!trec_eval_path.equals(""))
            writeToResultsFile(queryNum,docList,trec_eval_path);
        return docList;
    }


    /**
     * Method used to get semanticly close words to the query words
     * used when semantic option is enabled
     * @param queryWords
     * @return
     */
    public String getSemanticWords(String[] queryWords){
        String semanticWords = "";
        try{
            Word2VecModel model = Word2VecModel.fromTextFile(new File( "semantic model\\word2vec.c.output.model.txt"));
            com.medallia.word2vec.Searcher semanticSearcher = model.forSearch();

            int resultsNum = 10;

            for (String word:queryWords) {
                try {
                    List<com.medallia.word2vec.Searcher.Match> matches = semanticSearcher.getMatches(word, resultsNum);

                    for (com.medallia.word2vec.Searcher.Match match : matches) {
                        semanticWords = semanticWords + " " + match.match();
                    }
                }catch (com.medallia.word2vec.Searcher.UnknownWordException e){
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return semanticWords;

    }
}
