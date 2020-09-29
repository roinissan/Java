package view;

import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;




/**
 * Class used for the gui of the program
 */
public class GUI {

    public javafx.scene.control.TextField tx_corpusPath;
    private String corpusPath;
    public javafx.scene.control.TextField tx_postingPath;
    private String postingPath;
    public javafx.scene.control.CheckBox stem;
    public javafx.scene.control.CheckBox semantic;
    private Index index;
    public javafx.scene.control.TextField tx_query;
    private String trec_eval_path;
    private Searcher searcher;
    public javafx.scene.control.TextField tx_trec_eval;
    @FXML
    public ListView<String> listView;


    /**
     * Constructor
     */
    public GUI(){
    }


    /**
     * Method used to open the file browser for the corpus path
     */
    public void getCorpusBrowser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Corpus Directory:");
        File fileChoosed = directoryChooser.showDialog(null);
        if(fileChoosed!=null){
            corpusPath = fileChoosed.getAbsolutePath();
            tx_corpusPath.setText(corpusPath);
        }
    }

    /**
     * Method used to open the file browser for the posting path
     */
    public void getPostingBrowser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose where to save the Posting Files:");
        File fileChoosed = directoryChooser.showDialog(null);
        if(fileChoosed!=null){
            postingPath = fileChoosed.getAbsolutePath();
            tx_postingPath.setText(postingPath);
        }
    }


    /**
     * method used to remove the results file
     */
    private void resetResults(){
        if(!tx_trec_eval.getText().isEmpty()) {
            File file = new File( tx_trec_eval.getText() + "\\results.txt");
            if (!file.exists())
                return;
            file.delete();
            try {
                file.createNewFile();
            }catch (Exception e){
            }
        }
    }


    /**
     * Method used to run when clicking on run query manuelly
     * run the search procedure
     */
    public void runOneQuery() {
        listView.getItems().clear();
        if (tx_query.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please insert Query in order to start Run.");
            alert.showAndWait();
        }
        else {
            if (index == null || index.getDictionary().size() == 0 || index.getDocuments().size() == 0) {
                try {
                    if (tx_corpusPath.getText().isEmpty() || tx_postingPath.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setContentText("Please insert Corpus and Posting paths.");
                        alert.showAndWait();
                    }
                    else {
                        if (index == null || index.getDictionary().size() == 0 || index.getDocuments().size() == 0) {
                            index = new Index(stem.isSelected(), tx_corpusPath.getText(), tx_postingPath.getText());
                            if (index.getDocuments().size() == 0)
                                index.createDocumentsFromFile();
                            if (index.getDictionary().size() == 0)
                                index.createDictionaryFromFile();
                        }
                        resetResults();
                        boolean semantic = this.semantic.isSelected();
                        this.searcher = new Searcher(semantic, index);
                        String trec_eval_path = "";
                        if(!this.tx_trec_eval.getText().isEmpty())
                            trec_eval_path = this.tx_trec_eval.getText();
                        ObservableList<String> observableList= FXCollections.observableArrayList();
                        List<Doc> retrivedDocs = searcher.startQuerySearch("24", this.tx_query.getText(),trec_eval_path);
                        observableList.add("The engine retrived " + retrivedDocs.size() + " documents for query number 24:");
                        for (Doc document : retrivedDocs) {
                            observableList.add(document.getDocID().trim());
                        }
                        listView.setItems(observableList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * method used for the button of the "Browse" trecevel
     */
    public void getTrecEvalBrowser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose where to save the Result File:");
        File fileChoosed = directoryChooser.showDialog(null);
        if(fileChoosed!=null){
            trec_eval_path = fileChoosed.getAbsolutePath();
            this.tx_trec_eval.setText(trec_eval_path);
        }
    }

    /**
     * Method used to show the entities of a document after running the search
     */
    public void showEntities(){
        if(listView.getItems().size()==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("There are no documents in the table.");
            alert.showAndWait();
        }
        else{
            String docID = listView.getSelectionModel().getSelectedItem();
            if(index!=null){
                if(!index.getFiveMostRelevantEntitys().containsKey(docID))
                    docID=" "+docID+" ";
                if(!index.getFiveMostRelevantEntitys().containsKey(docID)) {
                    docID = docID.trim();
                    docID=docID+" ";
                }
                if(!index.getFiveMostRelevantEntitys().containsKey(docID)){
                    docID = docID.trim();
                    docID=" "+docID;
                }
                if(!index.getFiveMostRelevantEntitys().containsKey(docID))
                    docID=docID.trim();
                if(index.getFiveMostRelevantEntitys().containsKey(docID)){
                    List<String> entities = index.getFiveMostRelevantEntitys().get(docID);
                    if(entities.size()==0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setContentText("There are no entities for this document.");
                        alert.showAndWait();
                    }
                    else {
                        Stage newStage = new Stage();
                        VBox vBox = new VBox();
                        ListView<String> entityListView = new ListView<>();
                        for (String entity : entities) {
                            entityListView.getItems().add(entity);
                        }
                        vBox.getChildren().addAll(entityListView);
                        Scene scene = new Scene(vBox);
                        newStage.setScene(scene);
                        newStage.setMinHeight(400);
                        newStage.setMinWidth(400);
                        newStage.show();
                    }
                }
            }
        }
    }


    /**
     * method used to choose the query file and then run the search method
     * used with read queries from read file
     */
    public void chooseQueriesFile(){
        listView.getItems().clear();
        try {
            if (tx_corpusPath.getText().isEmpty() || tx_postingPath.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setContentText("Please insert Corpus and Posting paths.");
                alert.showAndWait();
            }
            else {
                if (index == null || index.getDictionary().size() == 0 || index.getDocuments().size() == 0) {
                    index = new Index(stem.isSelected(), tx_corpusPath.getText(), tx_postingPath.getText());
                    if (index.getDocuments().size() == 0)
                        index.createDocumentsFromFile();
                    if (index.getDictionary().size() == 0)
                        index.createDictionaryFromFile();
                }
                resetResults();
                boolean semantic = this.semantic.isSelected();
                List<List<Doc>> retrivedDocs = new ArrayList<>();
                this.searcher = new Searcher(semantic, index);
                String trec_eval_path = "";
                if(!this.tx_trec_eval.getText().isEmpty())
                    trec_eval_path = this.tx_trec_eval.getText();
                Stage stage=(Stage)tx_trec_eval.getScene().getWindow();
                FileChooser queryChooser = new FileChooser();
                File file = queryChooser.showOpenDialog(stage);
                if(file==null || !file.getAbsolutePath().contains(".txt")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("You have to choose a queries file.");
                    alert.showAndWait();
                }else {
                    index.getReadFile().readQueries(file.getPath(),this.semantic.isSelected());
                    List<String> queries = index.getReadFile().getQueries();
                    String []query_num = new String[queries.size()];
                    for(int i = 0;i<queries.size();i++){
                        query_num[i]=queries.get(i).split("@")[0];
                    }
                    int counter = 0;
                    for(String query:queries){
                        retrivedDocs.add(searcher.startQuerySearch(query_num[counter],query.split("@")[1], trec_eval_path));
                        counter++;
                    }
                    counter=0;
                    ObservableList<String> observableList= FXCollections.observableArrayList();
                    for(List<Doc> docsList: retrivedDocs) {
                        observableList.add("The engine retrived " + docsList.size() + " documents for query number "+query_num[counter]+":");
                        counter++;
                        for (Doc document : docsList) {
                            observableList.add(document.getDocID());
                        }
                    }
                    listView.setItems(observableList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Main Method which opens the first window
     * @throws IOException
     */
    public void run() throws IOException {
        try {

            if (tx_corpusPath.getText().isEmpty() || tx_postingPath.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setContentText("Please insert Corpus and Posting paths.");
                alert.showAndWait();
            } else {
                postingPath = tx_postingPath.getText();
                corpusPath = tx_corpusPath.getText();

                if (stem.isSelected()) {
                    index = new Index(true, corpusPath, postingPath);
                    index.startIndex();
                } else {
                    index = new Index(false, corpusPath, postingPath);
                    index.startIndex();
                }
            }


        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method for the reset button
     * removes posting files
     * @throws IOException
     */
    public void reset() throws IOException {
        try {
            if(index!=null) {
                index.resetPosting();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Posting Files deleted and memory cleared.");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Cannot reset because there are no files to Delete.");
                alert.showAndWait();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method used to load the dictionary for the index from a file
     * without running the index procedure
     */
    public void loadDictToMemory(){
        try {
            if(tx_corpusPath.getText().isEmpty()||tx_postingPath.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please insert Corpus and Posting path");
                alert.showAndWait();
            }
            if (index == null) {
                index = new Index(stem.isSelected(), tx_corpusPath.getText(), tx_postingPath.getText());
                index.createDictionaryFromFile();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setContentText("Dictionary loaded to memory");
                alert2.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Dictionary already loaded");
                alert.showAndWait();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


    /**
     * Method used to show the terms of the dictionary of the index
     */
    public void showDict(){
        try {
            if (index == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please insert Corpus and Posting path");
                alert.showAndWait();
                index = new Index(stem.isSelected(), tx_corpusPath.getText(), tx_postingPath.getText());
                index.createDictionaryFromFile();
            }
            Stage newStage = new Stage();
            VBox vBox = new VBox();
            List<Term> dict = index.getDictionaryListSorted();
            ListView<String> listView=new ListView<>();
            for (Term term : dict) {
                 listView.getItems().add(term.getTerm() + " " + term.getTf());
            }
            vBox.getChildren().addAll(listView);
            Scene scene = new Scene(vBox);
            newStage.setScene(scene);
            newStage.setMinHeight(400);
            newStage.setMinWidth(400);
            newStage.show();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
