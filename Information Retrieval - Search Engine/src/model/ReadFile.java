package model;

import java.io.*;
import java.util.*;


/**
 * Class to read the documents.
 */

public class ReadFile {

    private String corpusPath;
    private HashSet<String> stopWords;
    private HashMap<String,String> documents ;
    private List<String> queries;
    private static int currentFile;
    private final int NUMBER_OF_FILES = 16;
    File[] directories;

    /**
     * Constructor to build Read file object
     * @param path
     * @throws IOException
     */
    public ReadFile(String path) throws IOException {
        this.corpusPath = path;
        currentFile=0;
        stopWords = new HashSet<>();
        documents = new HashMap();
        queries = new ArrayList<>();
        readStopWords();
        getDocumentsPath();
    }

    /**
     * method to clear ram when clicking on "reset button"
     */
    public void clearFileReader(){
        stopWords.clear();
        documents.clear();
        directories=new File[0];
    }

    /**
     * Getter for last K documents which was read
     * @return
     */
    public HashMap<String, String> getDocuments() {
        return documents;
    }

    /**
     * Setter for documents
     * @param documents
     */
    public void setDocuments(HashMap<String, String> documents) {
        this.documents = documents;
    }

    /**
     * Getter for stopwords
     * @return hashset of stop words
     */
    public HashSet<String> getStopWords() {
        return stopWords;
    }

    /**
     * Setter for stopwords
     * @param stopWords
     */
    public void setStopWords(HashSet<String> stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * Method to read stop words file and placing it in HashSet
     * @throws IOException
     */
    private void readStopWords() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(corpusPath+"\\stopwords.txt"));
        String word;
        while((word = br.readLine()) != null){
            stopWords.add(word);
        }
    }

    /**
     * Method used to read - parse queries from a file
     * @param path
     * @param semantic
     */
    public void readQueries(String path,boolean semantic){
        File queryFile = new File(path);
        String line = "";
        String queryNumber = "";
        String query = "";
        if(!semantic) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(queryFile));
                while ((line = br.readLine()) != null) {
                    if (line.contains("<num>")) {
                        String[] splittedQueryNum = line.split(" ");
                        if (splittedQueryNum.length > 1 && splittedQueryNum[2] != null)
                            queryNumber = splittedQueryNum[2];
                    }
                    if (line.contains("<title>")) {
                        String[] splittedQuery = line.split(" ");
                        for (int i = 1; i < splittedQuery.length; i++) {
                            query = query + " " + splittedQuery[i];
                        }
                    }
                    if ((line.contains("<desc>"))) {
                        line = br.readLine();
                        while (!line.equals("")) {
                            if (line.contains("<narr>"))
                                break;
                            String[] splittedQuery = line.split(" ");
                            for (int i = 0; i < splittedQuery.length; i++) {
                                query = query + " " + splittedQuery[i].replaceAll("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]*", "");
                            }
                            line = br.readLine();
                        }
                        System.out.println("here");
                        query = query.trim();
                        this.queries.add(queryNumber + "@" + query);
                        query = "";
                        queryNumber = "";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try{
                BufferedReader br = new BufferedReader(new FileReader(queryFile));
                while((line=br.readLine())!=null){
                    if(line.contains("<num>")){
                        String[] splittedQueryNum = line.split(" ");
                        if(splittedQueryNum.length>1&&splittedQueryNum[2]!=null)
                            queryNumber=splittedQueryNum[2];
                    }
                    if(line.contains("<title>")){
                        String[] splittedQuery = line.split(" ");
                        for(int i=1;i<splittedQuery.length;i++){
                            query = query+" "+splittedQuery[i];
                        }
                        query=query.trim();
                        this.queries.add(queryNumber+"@"+query);
                        query="";
                        queryNumber="";
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Getter for queries after parsed
     * @return list of queries
     */
    public List<String> getQueries() {
        return queries;
    }

    /**
     * Method which gets document path and reads and storing its content
     * sets the content to HashMap
     * @param docPath
     */
    private void readDocument(String docPath) {
        String openText = "<TEXT>";
        String closeText = "</TEXT>";
        String docId = "<DOCNO>";
        String newDoc = "</DOC>";
        String documentText= "";
        String line;
        String key="";
        boolean textCheck = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(docPath));

            while ((line = br.readLine()) != null) {
                if (line.contains("</f>") || line.contains("</F>"))
                    continue;
                //if(line.contains("</h")||line.contains("</H")||line.contains("<h")||line.contains("<H")||line.contains("h>")||line.contains("H>"))
                //  line = line.replaceAll("<\\/*(h|H)\\d+>","");

                if (line.contains(docId)) {
                    key = line.replaceAll("<.*?>", "");
                }
                if (line.contains(openText)) {
                    textCheck = true;
                    continue;
                }
                if (line.contains(closeText)) {
                    textCheck = false;
                    documents.put(key, documentText);
                    documentText = "";
                }
                line = line.replaceAll("<.*>","");
                if (textCheck) {
                    documentText = documentText + " " + line;
                }

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which send to readDocument method K documents
     * @return
     */
    public boolean readFewFiles()  {
        documents = new HashMap();
         for (int i = currentFile; i < currentFile + NUMBER_OF_FILES; i++){
            if(i >= directories.length)
                return false;
            readDocument(directories[i].getPath()+"\\"+directories[i].getName());
        }
        currentFile = currentFile + NUMBER_OF_FILES;
        return true;
    }

    /**
     * Method which find the documents paths and storing it in a list.
     * @throws IOException
     */
    private void getDocumentsPath() throws IOException {
        directories = new File(corpusPath).listFiles(new FileFilter(){
            @Override
            public boolean accept(File file){
                return file.isDirectory();
            }
        });
    }
}
