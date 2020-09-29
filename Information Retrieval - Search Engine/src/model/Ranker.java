package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to rank a list of documents
 */
public class Ranker {

    private boolean semantic;
    private double k1;
    private double b;

    private int corpusSize;
    private List<HashMap<String,Integer>> listOfDocuments;
    private Index index;
    private HashMap<String,Doc> documentsToRank;
    private HashMap<String,Doc> documentsInIndex;
    private HashMap<String,Double> documentAndRank;

    /**
     * Constructon
     * @param semantic
     * @param listOfDocuments
     * @param index
     */
    public Ranker(boolean semantic,List<HashMap<String,Integer>> listOfDocuments,Index index){
        this.semantic=semantic;
        this.listOfDocuments=new ArrayList<>();
        this.listOfDocuments=listOfDocuments;
        this.index=index;
        this.documentsToRank = new HashMap<>();
        this.documentsInIndex = new HashMap<>();
        this.documentsInIndex = index.getDocuments();
        this.documentAndRank=new HashMap<>();
        this.k1=0.04;
        this.b=0.0095;
        this.corpusSize=documentsInIndex.size();
    }

    /**
     * Method used to calculate the rank of a given doc
     * @param tf
     * @param numberOfDocuments
     * @param docLength
     * @param averageDocumentLength
     * @param documentFrequency
     * @return the rank of a document
     */
    public double BM25(double tf, double numberOfDocuments, double docLength, double averageDocumentLength, double documentFrequency){
        double K = k1 * ((1 - b) + ((b * docLength) / averageDocumentLength));
        double weight = ( ((k1 + 1d) * tf) / (K + tf) );
        double idf = weight * Math.log((numberOfDocuments - documentFrequency + 0.5d) / (documentFrequency + 0.5d));
        return idf;
    }

    /**
     * Main method to start the ranking process
     * @return list of 50 doc ordered by rank
     */
    public List<Doc> startRank(){
        int avgDocsLength = this.calculateAvgLengthDocument();
        noDuplicateDocument();
        double freqOfCurrentWord = 0;
        double lengthOfDocument = 0;
        double mostFreqWordInDocument = 0;
        double numberOfDocumentsForTerm = 0;
        double tf = 0;
        double rank = 0;
        for(HashMap<String,Integer> docHash:listOfDocuments){
            for(String docName : docHash.keySet()){
                try{
                    freqOfCurrentWord = docHash.get(docName);
                    lengthOfDocument = this.documentsInIndex.get(docName).getNumOfwords();
                    mostFreqWordInDocument = this.documentsInIndex.get(docName).getMostFreqWord();
                    tf = freqOfCurrentWord/mostFreqWordInDocument;
                    numberOfDocumentsForTerm = docHash.size();
                    rank= BM25(tf,this.corpusSize,lengthOfDocument,avgDocsLength,numberOfDocumentsForTerm);
                    updateDocumentAndRank(docName,rank);
                }
                catch (Exception e){
                    continue;
                }
            }
        }
        List<Doc> documentsSorted = sortDocumentsByRank();
        return documentsSorted;
    }

    /**
     * method to sort by rank a given list of doc
     * @return list of docs
     */
    public List<Doc> sortDocumentsByRank(){
        double currMaxRank = 0;
        List<Doc> documentsSorted = new ArrayList<>();
        String currMaxDocName = "";
        double currentValue = 0;
        HashMap<String,Double> duplicatedHash = this.documentAndRank;
        int counter = 0;
        while(counter<50) {
            currMaxRank=0;
            currMaxDocName = "";
            currentValue = 0;
            for (String docName : duplicatedHash.keySet()) {
                currentValue = duplicatedHash.get(docName);
                if (currentValue >= currMaxRank) {
                    currMaxRank = currentValue;
                    currMaxDocName = docName;
                }
            }
            documentsSorted.add(this.documentsToRank.get(currMaxDocName));
            duplicatedHash.remove(currMaxDocName);
            counter++;
        }
        return documentsSorted;
    }

    /**
     * Method to set rank to document
     * @param docName
     * @param rank
     */
    public void updateDocumentAndRank(String docName,double rank){
        double newRank = 0;
        if(this.documentAndRank.containsKey(docName)){
            newRank=documentAndRank.get(docName)+rank;
            documentAndRank.remove(docName);
            documentAndRank.put(docName,newRank);
        }else{
            documentAndRank.put(docName,rank);
        }
    }

    /**
     * Method to remove duplicate documents
     */
    public void noDuplicateDocument(){
        Doc document = null;
        for(HashMap<String,Integer> docsHash:listOfDocuments){
            for(String docName : docsHash.keySet()){
                if(documentsInIndex.containsKey(docName)){
                    document=documentsInIndex.get(docName);
                    if(!this.documentsToRank.containsKey(document))
                        this.documentsToRank.put(docName,document);
                }
            }
        }
    }

    /**
     * Method to total words num of the documents - used with ranking
     * @return int - number of words
     */

    public int calculateAvgLengthDocument(){
        long N = documentsInIndex.size();
        long sumOfWords = 0;
        for(String docName:documentsInIndex.keySet()){
            sumOfWords=sumOfWords+documentsInIndex.get(docName).getNumOfwords();
        }
        return (int)(sumOfWords/N);
    }


}
