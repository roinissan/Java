import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.File;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class Assignment4 {


    private Assignment4() {


    }

    public static void executeFunc(Assignment4 ass, String[] args) {
        String funcName = args[0];
        switch (funcName) {
            case "loadNeighborhoodsFromCsv":
                ass.loadNeighborhoodsFromCsv(args[1]);
                break;
            case "dropDB":
                ass.dropDB();
                break;
            case "initDB":
                ass.initDB(args[1]);
                break;
            case "updateEmployeeSalaries":
                ass.updateEmployeeSalaries(Double.parseDouble(args[1]));
                break;
            case "getEmployeeTotalSalary":
                System.out.println(ass.getEmployeeTotalSalary());
                break;
            case "updateAllProjectsBudget":
                ass.updateAllProjectsBudget(Double.parseDouble(args[1]));
                break;
            case "getTotalProjectBudget":
                System.out.println(ass.getTotalProjectBudget());
                break;
            case "calculateIncomeFromParking":
                System.out.println(ass.calculateIncomeFromParking(Integer.parseInt(args[1])));
                break;
            case "getMostProfitableParkingAreas":
                System.out.println(ass.getMostProfitableParkingAreas());
                break;
            case "getNumberOfParkingByArea":
                System.out.println(ass.getNumberOfParkingByArea());
                break;
            case "getNumberOfDistinctCarsByArea":
                System.out.println(ass.getNumberOfDistinctCarsByArea());
                break;
            case "AddEmployee":
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                ass.AddEmployee(Integer.parseInt(args[1]), args[2], args[3], Date.valueOf(args[4]), args[5], Integer.parseInt(args[6]), Integer.parseInt(args[7]), args[8]);
                break;
            default:
                break;
        }
    }



    public static void main(String[] args) {
        File file = new File("");
        String csvFile = args[0];
        String line = "";
        String cvsSplitBy = ",";
        Assignment4 ass = new Assignment4();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);

                executeFunc(ass, row);

            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private static Connection sqlConnection() {
        Connection con = null;
        String connectionUrl = "jdbc:sqlserver://localhost;integratedSecurity=true";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return con;

    }


    private void loadNeighborhoodsFromCsv(String csvPath) {
        Connection conn = null;
        Statement stmt = null;
        String to_add = "";
        try{
            conn = sqlConnection();
            String line = "";
            String cvsSplitBy = ",";
            try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

                while ((line = br.readLine()) != null) {
                    String[] row = line.split(cvsSplitBy);
                    to_add = "insert into Neighborhood values("+row[0]+", '"+row[1]+"')";
                    try{
                        stmt = conn.createStatement();
                        stmt.execute(to_add);
                    }
                    catch (SQLException se){
                        se.printStackTrace();
                    }
                }
                try{
                    conn.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateEmployeeSalaries(double percentage) {
        Connection conn = null;
        try{
            conn = sqlConnection();
            String percents = String.valueOf(percentage);
            Statement stmt = null;
            String updt = "UPDATE ConstructorEmployee " +
                    "SET SalaryPerDay = SalaryPerDay*(1+"+percents+"*0.01) " +
                    "FROM ConstructorEmployee " +
                    "INNER JOIN Employee " +
                    "ON (Employee.EID = ConstructorEmployee.EID) " +
                    "WHERE (datediff(year,Employee.BirthDate,GETDATE())>49);";
            try{
                stmt = conn.createStatement();
                stmt.execute(updt);
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void updateAllProjectsBudget(double percentage) {
        Connection conn = null;
        try{
            conn = sqlConnection();
            String percents = String.valueOf(percentage);
            Statement stmt = null;
            String updt = "UPDATE Project SET Budget = Budget*(1+"+percents+"*0.01)";
            try{
                stmt = conn.createStatement();
                stmt.execute(updt);
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private double getEmployeeTotalSalary() {
        Connection conn = null;
        ResultSet rs =null;
        double salary = 0;
        try{
            conn = sqlConnection();
            String totalSal = "SELECT SUM(SalaryPerDay) as x FROM ConstructorEmployee";
            Statement stmt = null;
            try{
                stmt = conn.createStatement();
                rs = stmt.executeQuery(totalSal);
                while(rs.next()){
                    salary = rs.getDouble("x");
                }
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return salary;

    }


    private int getTotalProjectBudget() {
        Connection conn = null;
        ResultSet rs =null;
        int budget = 0;
        try{
            conn = sqlConnection();
            String totalSal = "SELECT SUM(Budget) as x FROM Budget";
            Statement stmt = null;
            try{
                stmt = conn.createStatement();
                rs = stmt.executeQuery(totalSal);
                while(rs.next()){
                    budget = rs.getInt("x");
                }
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return budget;
    }


    private void dropDB() {
        Statement statment = null;
        String command = null;
        Connection con= null;
        try {
            con = sqlConnection();
            if (con != null) {
                command = "USE master;\n" +
                        "Drop Database \"DB2019_Ass2\";";
                statment = con.createStatement();
                statment.execute(command);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    private List<String> readFile(String file_path){
        int counter = 0;
        String command = "";
        String line = null,first_word;
        String[] line_split;
        List<String> commands_batch = new ArrayList<>();
        List<String> keywords = Arrays.asList("CREATE","SELECT","INSERT","UPDATE","DELETE","CASE","DROP","BACKUP","ALTER","EXEC","USE");
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {

            while ((line = br.readLine()) != null) {
                if(!line.contains("GO"))
                    if(line.length() > 0){
                        line_split = line.split(" ");
                        first_word = line_split[0].toUpperCase();
                        if (keywords.contains(first_word) && counter != 0){
                            commands_batch.add(command);
                            command = line;

                        }else{
                            command = command+ "\n"+ line;
                            counter++;
                        }
                    }
            }
            commands_batch.add(command);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return commands_batch;
    }

    private void initDB(String csvPath) {
        List<String> file_content = readFile(csvPath);

        Statement statment = null;
        Connection con= null;
        try {
            con = sqlConnection();
            if (con != null) {
                for (String command:file_content) {
                    statment = con.createStatement();
                    statment.execute(command);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private int calculateIncomeFromParking(int year) {
        Connection conn = null;
        ResultSet rs =null;
        String s_year = String.valueOf(year);
        int income = 0;
        try{
            conn = sqlConnection();
            String totalCost = "select SUM(Cost) from CarParking where year(StartTime)="+ s_year +" and year(EndTime)="+s_year;
            Statement stmt = null;
            try{
                stmt = conn.createStatement();
                rs = stmt.executeQuery(totalCost);
                while(rs.next()){
                    income = rs.getInt("x");
                }
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return income;
    }

    private ArrayList<Pair<Integer, Integer>> getMostProfitableParkingAreas() {
        ArrayList<Pair<Integer, Integer>> most_prof_areas = new  ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pid_cost_pair = null;
        Statement statment = null;
        String command = null;
        Connection con = null;
        ResultSet command_result_dataset = null;
        try {
            con = sqlConnection();
            if (con != null) {
                command = "select top (5) ParkingAreaID, SUM(cost) as 'Total Cost'\n" +
                        "from CarParking\n" +
                        "group by ParkingAreaID\n" +
                        "order by [Total Cost] desc";
                statment = con.createStatement();
                command_result_dataset =  statment.executeQuery(command);
                while (command_result_dataset.next()){
                    pid_cost_pair = new  Pair<Integer, Integer>(command_result_dataset.getInt("ParkingAreaID"),command_result_dataset.getInt("Total Cost"));
                    most_prof_areas.add(pid_cost_pair);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return most_prof_areas;
    }

    private ArrayList<Pair<Integer, Integer>> getNumberOfParkingByArea() {
        ArrayList<Pair<Integer, Integer>>num_of_parking = new  ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pid_cid_count_pair = null;
        Statement statment = null;
        String command = null;
        Connection con = null;
        ResultSet command_result_dataset = null;
        try {
            con = sqlConnection();
            if (con != null) {
                command = "select ParkingAreaID, count(CID) as NumberOfParkings from CarParking\n" +
                        "group by ParkingAreaID";
                statment = con.createStatement();
                command_result_dataset =  statment.executeQuery(command);
                while (command_result_dataset.next()){
                    pid_cid_count_pair = new  Pair<Integer, Integer>(command_result_dataset.getInt("ParkingAreaID"),command_result_dataset.getInt("NumberOfParkings"));
                    num_of_parking.add(pid_cid_count_pair);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num_of_parking;
    }


    private ArrayList<Pair<Integer, Integer>> getNumberOfDistinctCarsByArea() {
        ArrayList<Pair<Integer, Integer>>num_of_distinct_cars = new  ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pid_cid_count_pair = null;
        Statement statment = null;
        String command = null;
        Connection con = null;
        ResultSet command_result_dataset = null;
        try {
            con = sqlConnection();
            if (con != null) {
                command = "select x.ParkingAreaID,count(x.CID) as NumberOfCars\n" +
                        "from\n" +
                        "(select  ParkingAreaID,CID  from CarParking\n" +
                        "group by ParkingAreaID,CID) x\n" +
                        "group by x.ParkingAreaID";
                statment = con.createStatement();
                command_result_dataset =  statment.executeQuery(command);
                while (command_result_dataset.next()){
                    pid_cid_count_pair = new  Pair<Integer, Integer>(command_result_dataset.getInt("ParkingAreaID"),command_result_dataset.getInt("NumberOfCars"));
                    num_of_distinct_cars.add(pid_cid_count_pair);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num_of_distinct_cars;
    }


    private void AddEmployee(int EID, String LastName, String FirstName, Date BirthDate, String StreetName, int Number, int door, String City) {
        Connection conn = null;
        try{
            conn = sqlConnection();
            Statement stmt = null;
            String s_EID = String.valueOf(EID);
            String s_Number = String.valueOf(Number);
            String s_door = String.valueOf(door);
            String to_add = "insert into Employee values("+s_EID+", '"+LastName+"' , '"+FirstName+"' , '"+BirthDate+"' , '"+StreetName+"' ,"+s_Number+","+s_door+", '"+City+"')";
            try{
                stmt = conn.createStatement();
                stmt.executeQuery(to_add);
            }
            catch (SQLException se){
                se.printStackTrace();
            }finally {
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
