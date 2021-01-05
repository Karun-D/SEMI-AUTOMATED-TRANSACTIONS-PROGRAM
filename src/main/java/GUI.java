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


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class GUI extends JFrame {
    // Constants for frame/panel sizes
    private static final int WIDTH = 200 * 3;
    private static final int HEIGHT = (int)(300 * 1.5);

    // Constants for text console size
    private static final int LINES = 15;
    private static final int CHAR_PER_LINE = 30;

    // Textfields, textbox, and ComboBoxes for user IO
    private static JTextField incomeField;
    private static JTextField expenseField;
    private static JComboBox incomeCategories;
    private static JComboBox expenseCategories;
    private static JTextArea memoDisplay;

    // Default categories for income and expense transactions
    private static String income_transaction_category = "Direct Deposit";
    private static String expense_transaction_category = "Textbooks";

    // Used to keep track of the number of vendors that are classified by the user using the GUI
    private static int incomeVendorCounter = 0;
    private static int expenseVendorCounter = 0;

    // Used to store each transaction in csv_file as a list of Transaction objects
    private static ArrayList<Transaction> transaction_list = new ArrayList<Transaction>();

    // Used to associate each vendor with its corresponding list of transactions
    private static HashMap<String,ArrayList<Transaction>> income_transaction_vendors = new HashMap<String,ArrayList<Transaction>>();
    private static HashMap<String,ArrayList<Transaction>> expense_transaction_vendors = new HashMap<String,ArrayList<Transaction>>();

    // Used to save vendors and their corresponding transaction category. 
    // Allows the automation of classifying future transactions
    private static HashMap<String,String[]> recognized_income_vendors;
    private static HashMap<String,String[]> recognized_expense_vendors;

    // Used to save vendors and their corresponding transaction category that are still yet to be classified
    private static ArrayList<String> non_recognized_income_vendors = new ArrayList<String>();
    private static ArrayList<String> non_recognized_expense_vendors = new ArrayList<String>();

    /**
     * Used to retrieve the category of an income transaction from the JComboBox
     */
    private class incomeCategoryListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            // Cast actionEvent to type ComboBox
            JComboBox cb = (JComboBox) e.getSource();
            // Read the user's selected choice from the ComboBox
            String transaction_category = (String)cb.getSelectedItem();
            income_transaction_category = transaction_category;
        }   
    }

    /**
     * Used to retrieve the category of an expense transaction from the JComboBox
     */
    private class expenseCategoryListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            // Cast actionEvent to type ComboBox
            JComboBox cb = (JComboBox) e.getSource();
            // Read the user's selected choice from the ComboBox
            String transaction_category = (String)cb.getSelectedItem();
            expense_transaction_category = transaction_category;
        }   
    }

    /**
     * Used to classify unrecognized income vendors
     */
    private class incomeVendorListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String income_categories[] = {"Direct Deposit", "E-Transfers", "Opt-out Services", "Tax Return", "Gov. Benefit"};
            String vendor = incomeField.getText();
            String transaction_category = income_transaction_category;
            int index = 0;
            String row_value = "";
            int numerical_row_value = 15; 

            // Loops until all income vendors are classified by the user
            if (incomeVendorCounter < non_recognized_income_vendors.size()){
                
                // Calculates the position of the transaction category in the array
                for (int i = 0; i < income_categories.length; i++){
                    if (income_categories[i].equals(transaction_category)){
                        index = i;
                    }
                }

                // Computes the row value by adding the index to the default category (Direct Deposit = 15)
                row_value = String.valueOf(numerical_row_value + index);

                System.out.println(transaction_category);
                System.out.println(row_value);

                // Updates the row values and categories of all transactions with the current vendor
                for (Transaction transaction : income_transaction_vendors.get(vendor)){
                    transaction.setCategory(transaction_category);
                    transaction.setRowValue(row_value);
                }

                // Adds vendor to HashMap so the vendor is no longer unclassified
                recognized_income_vendors.put(vendor, new String[]{transaction_category, row_value});

                // Increase vendor count to show that there is one less unrecognized vendor
                incomeVendorCounter++;
                
                // If there are still unclassfied vendors, iterate to the next one
                if (incomeVendorCounter != non_recognized_income_vendors.size()){
                    incomeField.setText(non_recognized_income_vendors.get(incomeVendorCounter));
                } else {
                    incomeField.setText(" ALL INCOME VENDORS ARE RECOGNIZED");
                }
            } else {
                incomeField.setText(" ALL INCOME VENDORS ARE RECOGNIZED");
            }
        }
    } 

    /**
     * Used to classify unrecognized expense vendors
     */
    private class expenseVendorListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String expense_categories[] = {"Textbooks", "Groceries", "Coffee", "Gym", "Spotify/Netflix", "Dining", "Leisure Activities", "Clothes", "E-Transfers", "Other Purchases"};
            // Cast actionEvent to type ComboBox
            String vendor = expenseField.getText();
            String transaction_category = expense_transaction_category;
            // Read the user's selected choice from the ComboBox
            int index = 0;
            String row_value = "";
            int numerical_row_value = 2; 

            // Loops until all expense vendors are classified by the user
            if (expenseVendorCounter < non_recognized_expense_vendors.size()){
                
                // Calculates the position of the transaction category in the array
                for (int i = 0; i < expense_categories.length; i++){
                    if (expense_categories[i].equals(transaction_category)){
                        index = i;
                    }
                }

                // Computes the row value by adding the index to the default category (Textbooks = 2)
                row_value = String.valueOf(numerical_row_value + index);

                System.out.println(transaction_category);
                System.out.println(row_value);

                 // Updates the row values and categories of all transactions with the current vendor
                for (Transaction transaction : expense_transaction_vendors.get(vendor)){
                    transaction.setCategory(transaction_category);
                    transaction.setRowValue(row_value);
                }

                // Adds vendor to HashMap so the vendor is no longer unclassified
                recognized_expense_vendors.put(vendor, new String[]{transaction_category, row_value});

                 // Increase vendor count to show that there is one less unrecognized vendor
                expenseVendorCounter++;
                
                // If there are still unclassfied vendors, iterate to the next one
                if (expenseVendorCounter != non_recognized_expense_vendors.size()){
                    expenseField.setText(non_recognized_expense_vendors.get(expenseVendorCounter));
                } else {
                    expenseField.setText(" ALL EXPENSE VENDORS ARE RECOGNIZED");
                }

            } else {
                expenseField.setText(" ALL EXPENSE VENDORS ARE RECOGNIZED");
            }
        }
    } 

    /**
     * Used to tell user about the process of transaction upload
     */
    private class updateListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            memoDisplay.setText("Updating Spreadsheet cells... ");
            
            // Calculate the corresponding cell's column value of spreadsheet based on the month the transaction had occured
            TransactionUtilities.finalizeCellLocation(transaction_list);

            // Automatically update spreadsheet using Google SheetsAPI
            TransactionUtilities.updateSpreadsheet(transaction_list);

            memoDisplay.setText("Spreadsheet Updated! ");
        }
    } 

    /**
     * Used to quit program
     */
    private class quitListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            // Serialize vendors to automate the classification of future transactions
            TransactionUtilities.saveRecognizedVendors(recognized_income_vendors, recognized_expense_vendors);

            System.exit(0);
        }
    } 

    public GUI(){
        super("Transaction Automater");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create Menu
        JMenu menu = new JMenu("Menu");

        JMenuItem update = new JMenuItem("Update Spreadsheet");
        update.addActionListener(new updateListener());
        menu.add(update);

         // Add MenuItems and Listeners for Add Command
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new quitListener());
        menu.add(quit);

        // Add Menu to MenuBar/Container
        JMenuBar bar = new JMenuBar( );
        bar.add(menu);

        // Add MenuBar/Container to Window
        setJMenuBar(bar);

        // Main Income Panel
        JPanel incomePanel = new JPanel();
        incomePanel.setBackground(Color.BLACK);
        incomePanel.setVisible(true);
        incomePanel.setLayout(new BorderLayout());

        // Income Header Text
        JPanel incomeHeaderPanel = new JPanel();
        incomeHeaderPanel.setBackground(Color.BLACK);
        JLabel incomeHeader = new JLabel("Unrecognized Vendor for Income Transactions");
        incomeHeader.setForeground(Color.CYAN);
        incomeHeaderPanel.add(incomeHeader);
        incomePanel.add(incomeHeaderPanel, BorderLayout.NORTH);

        // Main Income Content Panel
        JPanel incomeContentPanel = new JPanel();
        incomeContentPanel.setLayout(new BorderLayout());
        incomeContentPanel.setBackground(Color.darkGray);

        // Income Content Panel
        JPanel incomeVendorPanel = new JPanel();
        incomeVendorPanel.setLayout(new BorderLayout());
        JLabel incomeVendor = new JLabel("    Vendor:                  ");
        incomeVendorPanel.add(incomeVendor, BorderLayout.WEST);
        incomeField = new JTextField(20);
        incomeVendorPanel.add(incomeField, BorderLayout.CENTER);
        incomeField.setEditable(false);

        // Income Vendors Panel
        String[] categories = {"Direct Deposit", "E-Transfers", "Opt-out Services", "Tax Return", "Gov. Benefit"};
        incomeCategories = new JComboBox<String>(categories);
        incomeCategories.addActionListener(new incomeCategoryListener());
        incomeVendorPanel.add(incomeCategories, BorderLayout.SOUTH);
        incomeContentPanel.add(incomeVendorPanel, BorderLayout.CENTER);

        // Income Button Panel
        JPanel incomeButtonPanel = new JPanel();
        incomeButtonPanel.setLayout(new BorderLayout());
        JButton incomeButton = new JButton("Classify");
        incomeButton.addActionListener(new incomeVendorListener());
        incomeButtonPanel.add(incomeButton, BorderLayout.CENTER);

        // White Space
        JPanel blank = new JPanel();
        blank.setBackground(Color.darkGray);
        blank.setPreferredSize(new Dimension(15,0));
        incomeButtonPanel.add(blank,BorderLayout.WEST);
        blank.setBackground(Color.darkGray);

        // Add panels to main Income Panel
        incomeContentPanel.add(incomeButtonPanel, BorderLayout.EAST);
        incomeButtonPanel.setBackground(Color.DARK_GRAY);
        incomeButton.setFont(new Font("Calibri", Font.BOLD, 26));
        incomeVendorPanel.setBackground(Color.darkGray);
        incomeVendor.setForeground(Color.CYAN);
        incomePanel.add(incomeContentPanel, BorderLayout.CENTER);
        add(incomePanel);

        // Main Income Panel
        JPanel expensePanel = new JPanel();
        expensePanel.setBackground(Color.BLACK);
        expensePanel.setVisible(true);
        expensePanel.setLayout(new BorderLayout());

        // Income Header Text
        JPanel expenseHeaderPanel = new JPanel();
        expenseHeaderPanel.setBackground(Color.BLACK);
        JLabel expenseHeader = new JLabel("Unrecognized Vendor for Expense Transactions");
        expenseHeader.setForeground(Color.CYAN);
        expenseHeaderPanel.add(expenseHeader);
        expensePanel.add(expenseHeaderPanel, BorderLayout.NORTH);

        // Main Income Content Panel
        JPanel expenseContentPanel = new JPanel();
        expenseContentPanel.setLayout(new BorderLayout());
        expenseContentPanel.setBackground(Color.darkGray);

        // Income Content Panel
        JPanel expenseVendorPanel = new JPanel();
        expenseVendorPanel.setLayout(new BorderLayout());
        JLabel expenseVendor = new JLabel("    Vendor:                  ");
        expenseVendorPanel.add(expenseVendor, BorderLayout.WEST);
        expenseField = new JTextField(20);
        expenseField.setEditable(false);
        expenseVendorPanel.add(expenseField, BorderLayout.CENTER);

        // Expense Vendors Panel
        String[] categoriesTwo = {"Textbooks", "Groceries", "Coffee", "Gym", "Spotify/Netflix", "Dining", "Leisure Activities", "Clothes", "E-Transfers", "Other Purchases"};
        expenseCategories = new JComboBox<String>(categoriesTwo);
        expenseCategories.addActionListener(new expenseCategoryListener());
        expenseVendorPanel.add(expenseCategories, BorderLayout.SOUTH);
        expenseContentPanel.add(expenseVendorPanel, BorderLayout.CENTER);

        // Expense Button Panel
        JPanel expenseButtonPanel = new JPanel();
        expenseButtonPanel.setLayout(new BorderLayout());
        JButton expenseButton = new JButton("Classify");
        expenseButton.addActionListener(new expenseVendorListener());
        expenseButtonPanel.add(expenseButton, BorderLayout.CENTER);
        expenseButtonPanel.setBackground(Color.DARK_GRAY);

        // White Space
        JPanel blankTwo= new JPanel();
        blankTwo.setBackground(Color.darkGray);
        blankTwo.setPreferredSize(new Dimension(15,0));
        expenseButtonPanel.add(blankTwo,BorderLayout.WEST);
        blankTwo.setBackground(Color.darkGray);
        expenseContentPanel.add(expenseButtonPanel, BorderLayout.EAST);

        // Main Expense Panel
        expenseButton.setFont(new Font("Calibri", Font.BOLD, 26));
        expenseVendorPanel.setBackground(Color.darkGray);
        expenseVendor.setForeground(Color.CYAN);
        expensePanel.add(expenseContentPanel, BorderLayout.CENTER);
        add(expensePanel);

        // Create South Panel
        JPanel southAddPanel = new JPanel();
        southAddPanel.setLayout(new BorderLayout());

        // Create Header for JTextArea, Add to SouthPanel
        JPanel southHeaderPanel = new JPanel();
        JLabel southHeader = new JLabel("Messages");
        southHeaderPanel.add(southHeader);
        southAddPanel.add(southHeaderPanel, BorderLayout.NORTH);
        southAddPanel.setBackground(Color.darkGray);
        southHeaderPanel.setBackground(Color.BLACK);
        southHeader.setForeground(Color.CYAN);

        // Create JTextArea
        memoDisplay = new JTextArea((int)(LINES/1.5), (int)(CHAR_PER_LINE * 1.5));
        memoDisplay.setBackground(Color.BLACK);
        memoDisplay.setForeground(Color.CYAN);
        memoDisplay.setEditable(false);

        // Create Scroll Bars
        JScrollPane scrolledText = new JScrollPane(memoDisplay);
        scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add JTextArea to South Panel. Change size,Add to SouthPanel
        southAddPanel.add(scrolledText, BorderLayout.CENTER);
        southAddPanel.setPreferredSize(new Dimension(WIDTH, 210));

        add(southAddPanel);

    }

    public static void main (String args[]) {
        // Initialize GUI 
        GUI window = new GUI();                                            
        window.setVisible(true);
       
        // Initialize Recognized Vendors using serialized file
        try {
            ObjectInputStream read_serialized_file = new ObjectInputStream(new FileInputStream("object.ser"));
            recognized_income_vendors = (HashMap<String,String[]>)read_serialized_file.readObject();
            recognized_expense_vendors = (HashMap<String,String[]>)read_serialized_file.readObject();
            read_serialized_file.close();
        } catch (Exception e) {
            recognized_income_vendors = new HashMap<String,String[]>();
            recognized_expense_vendors = new HashMap<String,String[]>();
        }

        // Read CSV file and store file as a list of Transaction objects
        if (args.length == 0) {
            TransactionUtilities.initializeTransactionList(transaction_list, "test_file.csv");
        } else {
            TransactionUtilities.initializeTransactionList(transaction_list, args[0]);
        }

        // Label the vendor type as an income source or an expense source
        TransactionUtilities.classifyVendors(transaction_list, income_transaction_vendors, expense_transaction_vendors);

        // Categorize vendor by transaction category, and update the type of each transaction (for recognized vendors)
        // Calculate the corresponding cell's row value based on transaction category
        TransactionUtilities.categorizeIncomeTransactions(income_transaction_vendors, recognized_income_vendors);
        TransactionUtilities.categorizeExpenseTransactions(expense_transaction_vendors, recognized_expense_vendors);
        TransactionUtilities.computeNonRecognizedIncomeVendors(non_recognized_income_vendors, income_transaction_vendors, recognized_income_vendors);
        TransactionUtilities.computeNonRecognizedExpenseVendors(non_recognized_expense_vendors, expense_transaction_vendors, recognized_expense_vendors);

        // If there are unclassfied income vendors after reading the serialized object, start classification process
        if (non_recognized_income_vendors.size() > 0) {
            incomeField.setText(non_recognized_income_vendors.get(0));
        } else {
            incomeField.setText(" ALL INCOME VENDORS ARE RECOGNIZED");
        }

        // If there are unclassfied income vendors after reading the serialized object, start classification process
        if (non_recognized_expense_vendors.size() > 0) {
            expenseField.setText(non_recognized_expense_vendors.get(0));
        } else {
            expenseField.setText(" ALL EXPENSE VENDORS ARE RECOGNIZED");
        }

        System.out.println("Income = " + non_recognized_income_vendors.size());
        System.out.println("Expense = "+ non_recognized_expense_vendors.size());
    }
}
