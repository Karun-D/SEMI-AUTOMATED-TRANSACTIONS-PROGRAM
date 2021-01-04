import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class TransactionUtilities {

    public static void classifyVendors(ArrayList<Transaction> transaction_list, HashMap<String,ArrayList<Transaction>> income_transaction_vendors, HashMap<String,ArrayList<Transaction>> expense_transaction_vendors) {
        // Iterate transaction list to find the vendor of each transaction
        for (Transaction outter_transaction : transaction_list){
            String vendor_key = outter_transaction.getVendor();                         // Store vendor of current Transaction
            String transaction_type = outter_transaction.getType();                     // Store type of current Transaction (Income or Expense)
            ArrayList <Transaction> transactions = new ArrayList<Transaction>();        // Temp ArrayList used to map all transactions to their corresponding vendor

            // If the HashMap already contains the current vendor, move to the next transaction
            if ((income_transaction_vendors.containsKey(vendor_key)) || (expense_transaction_vendors.containsKey(vendor_key))){
                continue;
            } else {

                // Iterate the transaction list to find all transactions with the current vendor value
                for (Transaction inner_transaction : transaction_list){
                    // Add all transactions that match the current vendor to the ArrayList
                    if (inner_transaction.getVendor().equals(vendor_key)){
                        transactions.add(inner_transaction);
                    }
                }

                // Map list of transactions to their corresponding vendor based on transaction type 
                if (transaction_type.equals("Income")){
                    income_transaction_vendors.put(vendor_key, transactions);
                } else {
                    expense_transaction_vendors.put(vendor_key, transactions);
                }
            }
        }

    }

    public static void initializeTransactionList(ArrayList<Transaction> transaction_list, String filename) {
        String line;                                            // Used to store CSV file line
        String[] line_tokens;                                   // Used to split file line into an array of tokens
        List<List<String>> record_list = new ArrayList<>();     // Used to store file as a list of arrays
        List<String> record;                                    // Used to access a single array of tokens

        Transaction csv_transaction;                            // Used to store CSV file data as an object
        String transaction_date;                                // Used to initialize Transaction object
        String transaction_vendor;                              // Used to initialize Transaction object

        try {
            // Use BufferedReader object to read CSV file
            BufferedReader file = new BufferedReader(new FileReader(filename));
            
            // Iterate file, storing each line as a tokenized array in a record_list
            while ((line = file.readLine()) != null) {
                line_tokens = line.split(",");
                record_list.add(Arrays.asList(line_tokens));
            }

            // Iterate record_list 
            for (int i = 1; i < record_list.size(); i++){
                record = record_list.get(i);                                                    // Store current lineArray in a record
                transaction_date = record.get(0);
                transaction_vendor = record.get(1);
                csv_transaction = new Transaction (transaction_date, transaction_vendor);       // Use array information to init new Transaction object

                // If the first character in the vendor is a space (improper csv format), eliminate character
                if (transaction_vendor.charAt(0) == ' ') {
                    csv_transaction.setVendor(record.get(1).substring(1));
                }

                // Exclude Transfers between Chequing account and Savings account (No new balances entering or leaving account)
                if ((transaction_vendor.equals(" TRANSFER OUT")) || (record.get(1).equals(" TRANSFER IN"))) {
                    continue;
                }

                if ((record.get(2)).equals("")){
                    // If Funds Out balance is empty, Funds In balance is non-empty, label transaction type as Income
                    csv_transaction.setAmount(Double.parseDouble(record.get(3)));
                    csv_transaction.setType("Income");
                } else {
                    // If Funds In balance is empty, Funds Out balance is non-empty, label transaction type as Expense
                    csv_transaction.setAmount(Double.parseDouble(record.get(2)));
                    csv_transaction.setType("Expense");
                }

                // Append transaction to transaction list
                transaction_list.add(csv_transaction);
            }

            file.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void categorizeIncomeTransactions(HashMap<String,ArrayList<Transaction>> income_transaction_vendors, HashMap<String,String[]> recognized_income_vendors) {
        // Iterate list of vendors that are classified as an income source
        for (String vendor : income_transaction_vendors.keySet()){
            String transaction_category;
            String row_value = "";

            // If the vendor is recognized (previously used vendor), retrieve the transaction's category and column value 
            if (recognized_income_vendors.containsKey(vendor)) {
                transaction_category = recognized_income_vendors.get(vendor)[0];
                row_value = recognized_income_vendors.get(vendor)[1];

                // Update the values of all transactions with the current vendor
                for (Transaction transaction : income_transaction_vendors.get(vendor)){ 
                    transaction.setCategory(transaction_category);
                    transaction.setRowValue(row_value);
                }
            } 
        }
    }

    public static void categorizeExpenseTransactions(HashMap<String,ArrayList<Transaction>> expense_transaction_vendors, HashMap<String,String[]> recognized_expense_vendors) {
        // Iterate list of vendors that are classified as an expense source
        for (String vendor : expense_transaction_vendors.keySet()){
            String transaction_category;
            String row_value = "";

            // If the vendor is recognized (previously used vendor), retrieve the transaction's category and column value 
            if (recognized_expense_vendors.containsKey(vendor)) {
                transaction_category = recognized_expense_vendors.get(vendor)[0];
                row_value = recognized_expense_vendors.get(vendor)[1];

                // Update the values of all transactions with the current vendor
                for (Transaction transaction : expense_transaction_vendors.get(vendor)){ 
                    transaction.setCategory(transaction_category);
                    transaction.setRowValue(row_value);
                }
            } 
        }
    }

    public static void computeNonRecognizedIncomeVendors(ArrayList<String> non_recognized_income_vendors, HashMap<String,ArrayList<Transaction>> income_transaction_vendors, HashMap<String,String[]> recognized_income_vendors){
        // Iterate list of vendors that are classified as an income source
        for (String vendor : income_transaction_vendors.keySet()){
            // If the vendor is recognized (previously used vendor), retrieve the transaction's category and column value 
            if (recognized_income_vendors.containsKey(vendor)) {
                continue;
            } else {
                non_recognized_income_vendors.add(vendor);
            }
        }
    }

    public static void computeNonRecognizedExpenseVendors(ArrayList<String> non_recognized_expense_vendors, HashMap<String,ArrayList<Transaction>> expense_transaction_vendors, HashMap<String,String[]> recognized_expense_vendors){
        // Iterate list of vendors that are classified as an income source
        for (String vendor : expense_transaction_vendors.keySet()){
            // If the vendor is recognized (previously used vendor), retrieve the transaction's category and column value 
            if (recognized_expense_vendors.containsKey(vendor)) {
                continue;
            } else {
                non_recognized_expense_vendors.add(vendor);
            }
        }
    }

    public static void finalizeCellLocation(ArrayList<Transaction> transaction_list) {
        char column_value = 'B';                                                // Base column value for January

        // Iterate through each month in the year (column value)
        for (int month = 1; month <= 12; month++) {
            // Iterate transaction list, and find all transactions with the current month
            for (Transaction transaction : transaction_list) {
                // Find the month of the current transaction using the date value
                int transaction_month = Integer.parseInt(transaction.getDate().substring(0, 2));
                
                // Label current transaction with the correct column value
                if (transaction_month == month){
                    transaction.setColumnValue(column_value);
                }
            }

            // Incremet column_value for the next month
            column_value++;
        }
    }

    public static void updateSpreadsheet(ArrayList<Transaction> transaction_list) {
        // Iterate all transactions and update their corresponding cell in spreadsheet
        for (Transaction transaction: transaction_list) {
            // Store the current value of the computed cell
            double previous_amount = Double.parseDouble(SheetsAPI.readCell(transaction.computeLocation())); 
            // Add the current value of the cell with the appended value to produce the new value of the computed cell
            SheetsAPI.insertCell(transaction.computeLocation(), String.valueOf(transaction.getAmount() + previous_amount));
        }
    }

    public static void saveRecognizedVendors(HashMap<String,String[]> recognized_income_vendors, HashMap<String,String[]> recognized_expense_vendors) {
        try {
            ObjectOutputStream write_serialized_file = new ObjectOutputStream(new FileOutputStream("object.ser"));

            // Serialize objects
            write_serialized_file.writeObject(recognized_income_vendors);
            write_serialized_file.writeObject(recognized_expense_vendors);
            write_serialized_file.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}