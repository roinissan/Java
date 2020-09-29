package model;

import java.io.*;
import java.util.*;



/**
 * Class which used for creating the posting file
 */
public class Posting {
    private List<File> postingPaths;
    private int counterForPostin;
    private String postingPath;
    private int firstTime;
    private List<String> alphaBet;
    private List<String> alphaBetS;
    private List<String> alphaBetL;
    private final int K1 = 20000;
    private final int K2 = 60000;
    private final int K3 = 10000;
    private int [] postingLastLine;
    private PriorityQueue<String> minHeap;


    /**
     * Constructor using for initializing the object
     * @param postingPath
     */
    public Posting(String postingPath){
        counterForPostin=0;
        this.postingPath=postingPath;
        postingPaths = new ArrayList<>();
        firstTime=0;
        alphaBet = new ArrayList<>(Arrays.asList("abc","def","ghi","jkl","mno","pqr"
                ,"stu","vwx","yz","chars"));
        alphaBetS = new ArrayList<>(Arrays.asList("abc-S","def-S","ghi-S","jkl-S","mno-S","pqr-S"
                ,"stu-S","vwx-S","yz-S","chars-S"));
        alphaBetL = new ArrayList<>(Arrays.asList("abc-L","def-L","ghi-L","jkl-L","mno-L","pqr-L"
                ,"stu-L","vwx-L","yz-L","chars-L"));
        minHeap = new PriorityQueue<>();
    }

    /**
     * Method to get the paths for all the posting files
     * @return
     */
    public List<File> getPostingPaths() {
        return postingPaths;
    }

    /**
     * Method for clearing the ram - used when clicking on "reset" button
     */
    public void clearPosting(){
        this.postingPaths.clear();
        this.alphaBet.clear();
        postingLastLine = new int[0];
    }

    /**
     * Method used to create new file in the computer - given a name
     * @param name
     */
    public void createNewFile(String name){
        createFile(name);
    }

    /**
     * Method used to create new file in the computer - given a name
     * @param name
     */
    public void createFile(String name){
        if(!name.equals("")){
            File file = new File(postingPath + "\\" + name+ ".txt");
            postingPaths.add(file);
        }
        else {
            counterForPostin++;
            File file = new File(postingPath + "\\" + "Posting" + counterForPostin + ".txt");
            postingPaths.add(file);
        }
    }

    /**
     * First phase of the posting , given a list of terms writes its to disk
     * @param allTermsInDoc
     */
    public void addDocToPosting(List<String> allTermsInDoc){
        try {
            FileWriter out = new FileWriter(postingPaths.get(postingPaths.size() - 1).getPath(), true);
            int counter = 0;
            int sizeOfList = allTermsInDoc.size();
            for (String postingTerm : allTermsInDoc) {
                if(counter < sizeOfList -1 )
                    out.write(postingTerm + "\n");
                else
                    out.write(postingTerm );
            }
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to sord the docs of a specific terms
     * @param terms
     * @return List
     */
    public List<String> sortPostingElements(HashMap<String,HashMap<String,Integer>> terms){
        List<String> postingTerms = new ArrayList<>();
        for (String docID: terms.keySet()){
            for(String term:terms.get(docID).keySet()){
                postingTerms.add(term + ":" + docID + ":" + terms.get(docID).get(term) + "|");
            }
        }
        Collections.sort(postingTerms);
        return postingTerms;
    }



    /**
     * Second phase of posting - after writing mang posting files
     * merging the files to one big file sorted
     */
    public void mergePostingFiles(){
        List<List<String>> files = new ArrayList<>();
        postingLastLine = new int[postingPaths.size()];
        int counter = 0;
        for(File f: postingPaths){
            postingLastLine[counter] = K1;
            files.add(readKLinesFromDoc(f.getAbsolutePath(),0));
            counter++;
        }
        KWayMerge(files);
    }

    /**
     * function to read K lines from a given file path- used with merging
     * @param DocPath
     * @param startIndex
     * @return list of posting lines
     */

    private List<String> readKLinesFromDoc(String DocPath,int startIndex) {
        List<String> kLines = new ArrayList<>();
        BufferedReader reader = null;
        String postingLine = "";
        int linesCounter = 0;

        try {
            reader = new BufferedReader(new FileReader(DocPath));
            while ((postingLine = reader.readLine()) != null && kLines.size() < K1) {
                if (linesCounter >= startIndex) {
                    kLines.add(postingLine);
                }
                linesCounter++;
            }
            return kLines;

        } catch (Exception e) {
            System.out.println(e);
            return kLines;
        }
    }

    /**
     * Mehod used with mergePostingFiles - implements the algorithm for merging the files to one file
     * using the multiway merge sort algorithm
     * @param postingFilesKlines
     */
    private void KWayMerge(List<List<String>> postingFilesKlines){
        List<String> termsToSave = new ArrayList<>();
        int numberOfFiles = postingPaths.size();
        int [] lineIndex = new int[numberOfFiles];
        int [] finishPostingArr = new int[numberOfFiles];
        String minWord;
        int fileWithMinWord = 0;
        boolean firstTime = true;
        int finishedPosting  = 0;
        for(List<String> files : postingFilesKlines){
            minHeap.add(files.get(0));
        }
        while(finishedPosting < numberOfFiles){
            minWord = minHeap.poll();
            termsToSave.add(minWord);
            for(int i = 0; fileWithMinWord < lineIndex.length; i++){
                fileWithMinWord = i;
                if(lineIndex[fileWithMinWord] >= postingFilesKlines.get(fileWithMinWord).size()){
                    if(finishPostingArr[fileWithMinWord] == 0) {
                        finishPostingArr[fileWithMinWord] = 1;
                        finishedPosting++;
                    }
                    if( i == lineIndex.length - 1)
                        break;
                    else
                        continue;
                }
                if(postingFilesKlines.get(fileWithMinWord).get(lineIndex[fileWithMinWord]).equals(minWord)){
                    lineIndex[fileWithMinWord]++;
                    if(lineIndex[fileWithMinWord] >= K1) {
                        postingFilesKlines.set(fileWithMinWord,readKLinesFromDoc(postingPaths.get(fileWithMinWord).getAbsolutePath(),
                                postingLastLine[fileWithMinWord]));
                        postingLastLine[fileWithMinWord]+=K1;
                        lineIndex[fileWithMinWord] = 0;
                    }
                    break;
                }
            }

            if (postingFilesKlines.get(fileWithMinWord).size() > lineIndex[fileWithMinWord])
                minHeap.add(postingFilesKlines.get(fileWithMinWord).get(lineIndex[fileWithMinWord]));
            if(termsToSave.size() > K2){
                if(firstTime) {
                    firstTime = false;
                    createNewFile("combinedList");
                }
                addDocToPosting(termsToSave);
                termsToSave = new ArrayList<>();
            }
        }
        if(firstTime) {
            firstTime = false;
            createNewFile("combinedList");
        }
        addDocToPosting(termsToSave);

    }

    /**
     * Third phase of posting -spliting the combined file to alphabet files
     * @param
     * @param
     */
    public void splitPostingToAlphaBet(){
        int updatedFile =0;
        Term term;
        int[] fileCounter = new int[10];
        List<String> kLines = new ArrayList<>();
        BufferedReader reader = null;
        FileWriter abc,def,ghi,jkl,mno,pqr,stu,vwx,yz,chars;
        String postingLine = "";
        String []postingLineSplited;
        int numberOfTerms = 0;
        for(String s:alphaBetL)
            createNewFile(s);
        try {
            abc = new FileWriter(postingPaths.get(postingPaths.size() - 10).getPath(), true);
            def = new FileWriter(postingPaths.get(postingPaths.size() - 9).getPath(), true);
            ghi = new FileWriter(postingPaths.get(postingPaths.size() - 8).getPath(), true);
            jkl = new FileWriter(postingPaths.get(postingPaths.size() - 7).getPath(), true);
            mno = new FileWriter(postingPaths.get(postingPaths.size() - 6).getPath(), true);
            pqr = new FileWriter(postingPaths.get(postingPaths.size() - 5).getPath(), true);
            stu = new FileWriter(postingPaths.get(postingPaths.size() - 4).getPath(), true);
            vwx = new FileWriter(postingPaths.get(postingPaths.size() - 3).getPath(), true);
            yz = new FileWriter(postingPaths.get(postingPaths.size() - 2).getPath(), true);
            chars = new FileWriter(postingPaths.get(postingPaths.size() - 1).getPath(), true);

            reader = new BufferedReader(new FileReader(postingPath+"\\combinedList.txt"));
            while ((postingLine = reader.readLine()) != null) {
                postingLineSplited = postingLine.split(":");
                postingLine = postingLine +"\n";
                if(postingLineSplited[0].length() == 0)
                    chars.write(postingLine);
                else if((postingLineSplited[0].charAt(0) >= 'a' && postingLineSplited[0].charAt(0) <= 'c') ||
                        (postingLineSplited[0].charAt(0) >= 'A' && postingLineSplited[0].charAt(0) <= 'C')){
                    abc.write(postingLine);
                    updatedFile = 0;
                    fileCounter[0]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'd' && postingLineSplited[0].charAt(0) <= 'f') ||
                        (postingLineSplited[0].charAt(0) >= 'D' && postingLineSplited[0].charAt(0) <= 'F')){
                    def.write(postingLine);
                    updatedFile = 1;
                    fileCounter[1]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'g' && postingLineSplited[0].charAt(0) <= 'i') ||
                        (postingLineSplited[0].charAt(0) >= 'G' && postingLineSplited[0].charAt(0) <= 'I')){
                    ghi.write(postingLine);
                    updatedFile = 2;
                    fileCounter[2]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'j' && postingLineSplited[0].charAt(0) <= 'l') ||
                        (postingLineSplited[0].charAt(0) >= 'J' && postingLineSplited[0].charAt(0) <= 'L')){
                    jkl.write(postingLine);
                    updatedFile =3;
                    fileCounter[3]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'm' && postingLineSplited[0].charAt(0) <= 'o') ||
                        (postingLineSplited[0].charAt(0) >= 'M' && postingLineSplited[0].charAt(0) <= 'O')){
                    mno.write(postingLine);
                    updatedFile = 4;
                    fileCounter[4]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'p' && postingLineSplited[0].charAt(0) <= 'r') ||
                        (postingLineSplited[0].charAt(0) >= 'P' && postingLineSplited[0].charAt(0) <= 'R')){
                    pqr.write(postingLine);
                    updatedFile = 5;
                    fileCounter[5]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 's' && postingLineSplited[0].charAt(0) <= 'u') ||
                        (postingLineSplited[0].charAt(0) >= 'S' && postingLineSplited[0].charAt(0) <= 'U')){
                    stu.write(postingLine);
                    updatedFile = 6;
                    fileCounter[6]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'v' && postingLineSplited[0].charAt(0) <= 'x') ||
                        (postingLineSplited[0].charAt(0) >= 'V' && postingLineSplited[0].charAt(0) <= 'X')){
                    vwx.write(postingLine);
                    updatedFile = 7;
                    fileCounter[7]++;
                }
                else if((postingLineSplited[0].charAt(0) >= 'y' && postingLineSplited[0].charAt(0) <= 'z') ||
                        (postingLineSplited[0].charAt(0) >= 'Y' && postingLineSplited[0].charAt(0) <= 'Z')){
                    yz.write(postingLine);
                    updatedFile = 8;
                    fileCounter[8]++;
                }
                else{
                    updatedFile = 9;
                    fileCounter[9]++;
                    chars.write(postingLine);

                }
            }
            reader.close();
            abc.flush();
            def.flush();
            ghi.flush();
            jkl.flush();
            mno.flush();
            pqr.flush();
            stu.flush();
            vwx.flush();
            yz.flush();
            chars.flush();
            abc.close();
            def.close();
            ghi.close();
            jkl.close();
            mno.close();
            pqr.close();
            stu.close();
            vwx.close();
            yz.close();
            chars.close();

            for(File file:postingPaths){
                if(file.getName().matches("Posting\\d+.txt")||file.getName().matches("combinedList.txt")){
                    file.delete();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /**
     * Method used to combine lines with same term
     * @param dictionary
     * @param stem
     * @return returns a new dictionary
     */
    public HashMap<String,Term> combineLines(HashMap<String,Term> dictionary,boolean stem){
        String fileName;
        int sizeOfPosting = postingPaths.size();
        for (int i = 0;i < sizeOfPosting;i++){
            fileName = postingPaths.get(i).getName().split("\\.")[0];
            if(!alphaBetL.contains(fileName))
                continue;
            if(stem)
                createNewFile(fileName.split("-")[0] + "-S" );
            else
                createNewFile(fileName.split("-")[0]);
            sizeOfPosting++;
            dictionary = combineLines(postingPaths.get(i), postingPaths.get(sizeOfPosting - 1),dictionary);
        }
        return dictionary;

    }


    /**
     * Method used to combine lines with same term
     * @param oldFile
     * @param newFile
     * @param dictionary
     * @return returns a new dictionary
     */
    public HashMap<String,Term> combineLines(File oldFile,File newFile,HashMap<String,Term> dictionary){
        BufferedReader reader;
        String postingLine;
        String newPostingLine = "";
        String previousTerm = "";
        String[] currentLine;
        int KPassed = 0;
        int counter = 1;
        Term term;
        List<String> newPostingLines = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(oldFile));
            while ((postingLine = reader.readLine()) != null) {
                currentLine = postingLine.split(":");
                if(currentLine == null || currentLine.length < 2|| currentLine[1] == null || currentLine[2] == null || currentLine [1].length() == 0 || currentLine[2].length() == 0)
                    continue;
                if(previousTerm.equals(currentLine[0])){
                    newPostingLine = newPostingLine  + currentLine[1]+ ":" + currentLine[2];
                }else{
                    counter++;
                    newPostingLines.add(newPostingLine);
                    if(dictionary.containsKey(currentLine[0])){
                        term = dictionary.get(currentLine[0]);
                        if(term.getLine() == -1)
                            term.setLine(counter);
                    }
                    newPostingLine = postingLine;
                }
                previousTerm = currentLine[0];

                if(newPostingLines.size() >= K3) {
                    writeNewPostingFile(newFile,newPostingLines);
                    newPostingLines = new ArrayList<>();
                }

            }
            if(newPostingLines.size() < K3){
                KPassed++;
                writeNewPostingFile(newFile,newPostingLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    /**
     * method to write file
     * @param file
     * @param newPostingLines
     */
    private void writeNewPostingFile(File file,List<String> newPostingLines){
        FileWriter writer = null;
        try {
            writer = new FileWriter(file.getPath(), true);
            for(String line:newPostingLines)
                writer.write(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method used to uncombined ABC posting files
     */
    public void deleteABCLFiles(){
        int sizeOfPosting = postingPaths.size();
        String fileName;
        for (int i = 0;i < sizeOfPosting;i++){
            fileName = postingPaths.get(i).getName().split("\\.")[0];
            if(!alphaBetL.contains(fileName))
                continue;
            postingPaths.get(i).delete();
        }

    }

    /**
     * method used to delete unnecessary files
     */
    public void clearFiles(){
        File postingFolder  = new File(postingPath);
        List<File> filesToDelete = new ArrayList<>();
        for(File f : postingFolder.listFiles()){
            if(alphaBet.contains(f.getName().split("\\.")[0]) ||
                    alphaBetS.contains(f.getName().split("\\.")[0]) ||
                    f.getName().matches("Documents.txt") ||
                    f.getName().matches("Documents-S.txt") ||
                    f.getName().matches("Dictionary-S.txt") ||
                    f.getName().matches("Dictionary.txt"))
                continue;

            f.delete();
        }
    }

}

