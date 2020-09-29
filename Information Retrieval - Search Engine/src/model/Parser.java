package model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.*;

/**
 * Class which parser a given sting to its basic terms
 */
public class Parser {
    private List<String> terms;
    private int currentWord;
    private String currentDocument;
    private Hashtable<String, String> months;
    private String[] words;
    private HashSet<String> stopWords;


    /**
     * Constructor of parser - initialize the Object
     * @param stopWords
     */
    public Parser(HashSet<String> stopWords) {
        currentWord = 0;
        terms = new ArrayList<>();
        months = new Hashtable<>();
        this.stopWords = stopWords;
        createMonthsHash();
    }

    /**
     * Method to clear ram then clicking on "reset button"
     */
    public void clearParser(){
        terms.clear();
        months.clear();
        stopWords.clear();
        words=new String[0];
    }

    /**
     * HashMap for months of the year - used for comparing string and creating dates
     */
    private void createMonthsHash() {
        months = new Hashtable<>();
        months.put("january", "01");
        months.put("february", "02");
        months.put("march", "03");
        months.put("april", "04");
        months.put("may", "05");
        months.put("june", "06");
        months.put("july", "07");
        months.put("august", "08");
        months.put("september", "09");
        months.put("october", "10");
        months.put("november", "11");
        months.put("december", "12");
    }

    /**
     * Main method for starting parsing on a given document
     * @param document
     * @return List of terms
     */
    public List<String> parse(String document){
        currentWord = 0;
        terms = new ArrayList<String>();
        currentDocument = document;
        document=document.trim();
        words = document.split("\\s+|--");
        mainParser();
        return terms;
    }

    /**
     * Method to start the parsing  - first check if the string is a number and then phrase of simple word
     */
    public void mainParser() {
        while(currentWord<words.length) {
            if(currentWord>=words.length)
                continue;
            if (generateNumber())
                continue;
            else if (generatePhrases())
                continue;
            else {
                generateWord();
            }
        }
    }
    /**
     * Method which checks if the string is a simple word - could be stopword or and other
     * @return
     */
    private boolean generateWord(){
        if(currentWord>=words.length)
            return false;
        String firstWord = words[currentWord].replaceAll("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]*","").toLowerCase();
        int firstWordLength = firstWord.length();
        while(firstWord.length()>0&&firstWord.charAt(0)=='-'){
            firstWord=firstWord.substring(1);
        }
        if(firstWord.length()==0){
            currentWord++;
            return true;
        }
        String nextWord = "";
        if(currentWord<words.length-1)
            nextWord=words[currentWord+1];
        if(months.get(firstWord)!=null&&nextWord.matches("\\d+")){
            if(!addDate(nextWord,firstWord)){
                terms.add(firstWord);
                currentWord++;
            }
            return true;
        }
        if(stopWords.contains(firstWord)){
            currentWord++;
            return false;
        }
        if(!words[currentWord].matches("\\w*[\\[\\(&;'?~`+|!%^*:$,.#\"\\)\\]]*")){
            if(checkIfAllInCapitalLettr(words[currentWord])){
                terms.add(words[currentWord]);
                currentWord++;
                return true;
            }
            if(currentWord<words.length-1&&!nextWord.isEmpty()){
                int counter = 2;
                String entity = "";
                entity.matches("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]+");
                if(!(words[currentWord].contains(",")||words[currentWord].contains(".")||words[currentWord+1].contains(",")||words[currentWord+1].contains("."))) {
                    if (checkFirstCharCapitalLetterTwo(words[currentWord], words[currentWord + 1])) {
                        entity = words[currentWord].replaceAll("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]*", "") + " " + words[currentWord + 1].replaceAll("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]*", "");
                        while (currentWord + counter < words.length - 1 && checkFirstCharCapitalLetterOne(words[currentWord + counter])) {
                            if(words[currentWord+counter].contains(",")||words[currentWord+counter].contains("."))
                                break;
                            entity = entity + " " + words[currentWord + counter].replaceAll("[\\[\\(&;'?~`+|!%^*,.#\"\\)\\]]*", "");
                            counter++;
                        }
                        terms.add(entity);
                        currentWord = currentWord + counter;
                        return true;
                    }
                }
            }
        }
        if(firstWord.contains("/")){
            String[] splittedOrWord = firstWord.split("/");
            if(splittedOrWord.length<2){
                firstWord=firstWord.replaceAll("/","");
                terms.add(firstWord);
                currentWord++;
                return true;
            }
            terms.add(splittedOrWord[0]+" Or " + splittedOrWord[1]);
            currentWord++;
            return true;
        }
        if(firstWord.contains(":")){
           if(!addHour(firstWord)){
               terms.add(firstWord.replaceAll(":",""));
               currentWord++;
           }
           return true;
        }
        else {
            terms.add(firstWord);
            currentWord++;
            return true;
        }

    }

    /**
     * Method to check it string is all capitalized
     * @param term
     * @return
     */
    public boolean checkIfAllInCapitalLettr(String term){
        for(int i=0;i<term.length();i++){
            if(!(term.charAt(i)>='A'&&term.charAt(i)<='Z'))
                return false;
        }
        return true;
    }

    /**
     * Method to check if two strings starts with capital letter - for identities
     * @param firstWord
     * @param secWord
     * @return
     */
    private boolean checkFirstCharCapitalLetterTwo(String firstWord, String secWord){
        if(!firstWord.isEmpty()&& !secWord.isEmpty()&&firstWord.charAt(0)>='A'&&firstWord.charAt(0)<='Z'&&secWord.charAt(0)>='A'&&secWord.charAt(0)<='Z')
            return true;
        return false;
    }

    /**
     * Method to check if string starts with capital letter
     * @param word
     * @return
     */
    private boolean checkFirstCharCapitalLetterOne(String word){

        if(!word.isEmpty()&&word.charAt(0)>='A'&&word.charAt(0)<='Z')
            return true;
        return false;
    }


    /**
     * Method to add a new phrase - new pattern for hours
     * @param firstWord
     * @return true if succeed, false if there is exception
     */
    private boolean addHour(String firstWord){
        String[] splittedHourWord = firstWord.split(":");
        try{
            int hour = Integer.valueOf(splittedHourWord[0]);
            int minute = Integer.valueOf(splittedHourWord[1]);
            if(hour>11 && minute>=0 && hour<24 &&minute<60){
                terms.add(String.valueOf(Integer.valueOf(splittedHourWord[0]))+ " PM");
                currentWord++;
                return true;
            }
            else if(hour<11 && minute>=0 &&minute<60){
                terms.add(splittedHourWord[0]+ " AM");
                currentWord++;
                return true;
            }
            else if(hour==12&&minute<60){
                terms.add(splittedHourWord[0]+ " PM");
                currentWord++;
                return true;
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method which checks if the string is a phrase
     * @return true if the string is a phrase true otherwise
     */
    private boolean generatePhrases(){
        if(currentWord + 3 < words.length){
            if(words[currentWord].toLowerCase().equals("between") &&
                    words[currentWord + 1].matches("\\d+") &&
                    words[currentWord + 2].toLowerCase().equals("and") &&
                    words[currentWord + 3].matches("\\d+")){
                terms.add(words[currentWord + 1] + "-" + words[currentWord + 3]);
                currentWord+=4;
                return true;
            }
        }
        return false;
    }


    /**
     * Main method which checks if the string is s number of any combination
     * @return true if the sting is a number and added it to the terms
     */
    public boolean generateNumber(){
        String firstWord = words[currentWord].replaceAll("[[?(#)*'+`;\"]]*","");
        String nextWord = "";
        String thirdWord="";
        String fourthWord="";
        int firstWordLenght = firstWord.length();
        if(!firstWord.matches(".*\\d+.*"))
            return false;
        if((firstWord.charAt(firstWordLenght-1)==','||firstWord.charAt(firstWordLenght-1)=='.'||firstWord.charAt(firstWordLenght-1)=='-'||firstWord.charAt(firstWordLenght-1)=='-'||firstWord.charAt(firstWordLenght-1)==']'))
            firstWord=firstWord.substring(0,firstWordLenght-1);
        if(currentWord<words.length-1)
            nextWord=words[currentWord+1].toLowerCase();
        if(currentWord+3<words.length-1) {
            thirdWord = words[currentWord + 2].toLowerCase();
            fourthWord = words[currentWord + 3].toLowerCase();
        }
        if(firstWord.length()==0) {
            currentWord++;
            return true;
        }
        if(nextWord.equals("thousand")){
            if(!addLargeNumbers(firstWord, nextWord)) {
                terms.add(firstWord);
                currentWord++;
            }
            return true;
        }
        if(nextWord.equals("million")){
            if(firstWord.charAt(0)=='$'&&nextWord.equals("million")){
                if(!addPriceWithDollarSign(firstWord,nextWord)) {
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            else if(thirdWord.equals("u.s.")&&fourthWord.equals("dollars")){
                if(!addPriceWithUSandDollar(firstWord,nextWord)){
                    terms.add(firstWord);
                    currentWord++;
                }
            }else{
                if(!addLargeNumbers(firstWord, nextWord)) {
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            return true;
        }
        if(nextWord.equals("billion")){
            if(firstWord.charAt(0)=='$'&&nextWord.equals("billion")){
                if(!addPriceWithDollarSign(firstWord,nextWord)) {
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            else if(thirdWord.equals("u.s.")&&fourthWord.equals("dollars")){
                if(!addPriceWithUSandDollar(firstWord,nextWord)){
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            else{
                if(!addLargeNumbers(firstWord, nextWord)) {
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            return true;
        }
        if(nextWord.equals("dollars")){
            if(firstWord.charAt(firstWord.length()-1)=='m'){
                if(!addPriceWithMSuffix(firstWord)){
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            else if(firstWord.length()>2 && firstWord.charAt(firstWord.length()-2)=='b'&&firstWord.charAt(firstWord.length()-1)=='n'){
                if(!addPriceWithBnSuffix(firstWord)){
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            else{
                if(!addPriceWithDollar(firstWord)){
                    terms.add(firstWord);
                    currentWord++;
                }
            }
            return true;
        }
        if(nextWord.equals("percentage")||nextWord.equals("percent") || firstWord.charAt(firstWord.length()-1)=='%'){
            addPercentNumber(firstWord);
            return true;
        }
        if(firstWord.charAt(0)=='$'&&!nextWord.equals("billion")&&!nextWord.equals("million")){
            if(!addPriceWithDollarSign(firstWord,nextWord)){
                terms.add(firstWord);
                currentWord++;
            }
            return true;
        }
        if(nextWord.contains("/")&&thirdWord.equals("dollars")){
            terms.add(firstWord+ " "+ nextWord + " Dollars");
            currentWord=currentWord+3;
            return true;
        }
        if(nextWord.contains("/")&&!thirdWord.equals("dollars")){
            if(nextWord.matches("\\d+/\\d")) {
                terms.add(firstWord + " " + nextWord);
                currentWord = currentWord + 2;
                return true;
            }
        }
        if(months.get(nextWord)!=null){
            if(!addDate(firstWord,nextWord)){
                terms.add(firstWord);
                currentWord++;
            }
            return true;
        }
        if(firstWord.contains(":")){
            return false;
        }

        if(!addPlainNumber(firstWord)){
            terms.add(firstWord);
            currentWord++;
            return true;
        }
        return true;
    }



    /**
     * Method to add the pattern of a number when there is a $ sign
     * @param word
     * @param nextWord
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPriceWithDollarSign(String word,String nextWord) {
        String priceTerm = "";
        float floatNumber;
        long longNumber;
        int currentCheck = currentWord;
        priceTerm = word.replaceAll("\\$*", "");
        try {
            if (nextWord.equals("million")) {
                priceTerm = priceTerm.replaceAll("\\,*", "");
                priceTerm = priceTerm + " M Dollars";
                currentWord = currentWord+2;
            } else if (nextWord.equals("billion")) {
                priceTerm = priceTerm.replaceAll("\\,*", "");
                priceTerm = priceTerm + "000" + " M Dollars";
                currentWord = currentWord+2;
            }else{
            if (priceTerm.contains(".")) {
                floatNumber = Float.parseFloat(priceTerm.replaceAll("\\,*", ""));
                if (floatNumber >= 1000000) {
                    priceTerm = String.valueOf(floatNumber / 1000000) + " M Dollars";
                } else
                    priceTerm = priceTerm + " Dollars";
            } else {
                longNumber = Long.parseLong(priceTerm.replaceAll("\\,*", ""));
                if (longNumber >= 1000000)
                    priceTerm = String.valueOf(longNumber / 1000000) + " M Dollars";
                else
                    priceTerm = priceTerm + " Dollars";
                }
                currentWord++;
            }
            terms.add(priceTerm);
            return true;
        }
         catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add the pattern of Date
     * @param number
     * @param month
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addDate(String number, String month) {
        Term date;
        String dateText = "";
        try {
            Integer.valueOf(number);
            if (number.length()==1) {
                dateText = months.get(month) + "-0" + number;
            } else if (number.length()==2) {
                dateText = months.get(month) + "-" + number;
            } else if (number.length()==4) {
                dateText = number + "-" + months.get(month);
            }
            terms.add(dateText);
            currentWord=currentWord+2;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Mehtod to add a plain number - all other patterns didnt work
     * @param number
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPlainNumber(String number) {
        String formattedNumber = number.replaceAll(",*", "");
        String numberTerm = "";
        long nativeNum;
        try {
            if (formattedNumber.contains(".")) {
                if (Float.parseFloat(formattedNumber) < 1000)
                    numberTerm = String.valueOf(Float.parseFloat(formattedNumber));
                else if (Float.parseFloat(formattedNumber) < 1000000)
                    numberTerm = String.valueOf(Float.parseFloat(formattedNumber) / 1000) + "K";
            } else {
                nativeNum = Long.parseLong(formattedNumber);
                if (nativeNum < 1000)
                    numberTerm = String.valueOf(nativeNum);
                else if (nativeNum < 1000000)
                    numberTerm = String.valueOf((float) nativeNum / 1000) + "K";
                else if (nativeNum < 1000000000)
                    numberTerm = String.valueOf((float) nativeNum / 1000000) + "M";
                else
                    numberTerm = String.valueOf((float) nativeNum / 1000000000) + "B";

            }
            terms.add(numberTerm);
            currentWord++;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add percent number pattern
     * @param word
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPercentNumber(String word) {
        if (word.contains("%")) {
            terms.add(word);
            currentWord++;
            return true;
        }
        else{
            terms.add(word+"%");
            currentWord=currentWord+2;
            return true;
        }
    }

    /**
     * Method to add a number which has m pattern at the end (million)
     * @param word
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPriceWithMSuffix(String word){
        float floatNumber;
        int intNumber;
        try {
            String priceTerm = word.replaceAll("[,m]", "");
            floatNumber = Float.parseFloat(priceTerm);
            priceTerm = priceTerm + " M Dollars";
            terms.add(priceTerm);
            currentWord = currentWord + 2;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add a number which has bn pattern at the end (billion)
     * @param word
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPriceWithBnSuffix(String word){
        float floatNumber;
        long intNumber;
        String priceTerm=word.replaceAll("[,bn]*","");
        try {
            if (word.contains(".")) {
                floatNumber = Float.parseFloat(priceTerm);
                priceTerm = String.valueOf((float) (floatNumber * 1000));

            } else {
                intNumber = Integer.valueOf(priceTerm);
                priceTerm = String.valueOf((long) (intNumber * 1000));

            }
            priceTerm = priceTerm + " M Dollars";
            terms.add(priceTerm);
            currentWord = currentWord + 2;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add number which the next word is dollar pattern
     * @param number
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPriceWithDollar(String number) {
        number = number.replaceAll("[,]","");
        try {
            if (number.contains(".")) {
                float price = Float.parseFloat(number);
                String priceWithDollar = "";
                if (price < 1000000) {
                    priceWithDollar = number + " Dollars";
                } else {
                    priceWithDollar = String.valueOf(price / 1000000) + " M Dollars";
                }
                terms.add(priceWithDollar);
            } else {
                long price = Long.parseLong(number);
                String priceWithDollar = "";
                if (price < 1000000) {
                    priceWithDollar = number + " Dollars";
                } else {
                    priceWithDollar = String.valueOf(price / 1000000) + " M Dollars";
                }
                terms.add(priceWithDollar);
            }
            currentWord = currentWord + 2;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add number which are bigger than 1000 - K/M/B pattern
     * @param word
     * @param nextWord
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addLargeNumbers(String word, String nextWord) {
        String termName;
        word = word.replaceAll(",","");
        try {
            if (word.contains(".")) {
                float tempoFloat = Float.parseFloat(word);
                termName = String.format("%.3f", tempoFloat);
            } else {
                termName = word;
            }
            if (nextWord.equals("thousand")) {
                termName = termName + "K";
                terms.add(termName);
            } else if (nextWord.equals("million")) {
                termName = termName + "M";
                terms.add(termName);
            } else if (nextWord.equals("billion")) {
                termName = termName + "B";
                terms.add(termName);
            }
            currentWord = currentWord+2;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method to add the us dollar pattern
     * @param word
     * @param nextWord
     * @return true if the pattern is as should be false otherwise
     */
    private boolean addPriceWithUSandDollar(String word,String nextWord){
        float floatNumber;
        int intNumber;
        try {
            String priceTerm = word.replaceAll("[,]*", "");
            if (word.contains(".")) {
                floatNumber = Float.parseFloat(priceTerm);
                priceTerm = String.valueOf(floatNumber);
            } else {
                intNumber = Integer.valueOf(priceTerm);
                priceTerm = String.valueOf(intNumber);
            }
            if (nextWord.equals("million")) {
                priceTerm = priceTerm + " M Dollars";
            } else {
                priceTerm = priceTerm + "000 M Dollars";
            }
            terms.add(priceTerm);
            currentWord=currentWord+4;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


}
