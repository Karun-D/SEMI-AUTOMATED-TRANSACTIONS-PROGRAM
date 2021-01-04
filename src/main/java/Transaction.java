public class Transaction {

    // ===== Instance Variables ===== //
    private String date;
    private String vendor;
    private double amount;
    private String transaction_type;
    private String transaction_category;

    private String row_value;
    private char column_value;
    private String cell_location;

    // ===== Constructors ===== //

    public Transaction() {
        date = "";
        vendor = "";
        amount = -1;
        transaction_type = "";
        transaction_category = "";
        row_value = "";
        column_value = ' ';
        cell_location = "";
    }

    public Transaction(String date, String vendor, double amount, String transaction_type, String transaction_category, String row_value, char column_value, String cell_location) {
        this.date = date;
        this.vendor = vendor;
        this.amount = amount;
        this.transaction_type = transaction_type;
        this.transaction_category = transaction_category;
        this.row_value = row_value;
        this.column_value = column_value;
        this.cell_location = cell_location;
    }

    public Transaction(String date, String vendor){
        this(date, vendor, -1, "", "", "", ' ', "");
    }

    /////////////////////////////////
    // ===== Mutator Methods ===== //
    ////////////////////////////////

    /**
     * Description: Mutator method for initializing the transaction's date
     * @param date - Transaction Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Description: Mutator method for initializing the transaction's vendor
     * @param vendor - Transaction Vendor
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * Description: Mutator method for initializing the transaction amount
     * @param amount - Transaction Amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Description: Mutator method for initializing the transaction type
     * @param transaction_type - Transaction Type: Income or Expense
     */
    public void setType(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    /**
     * Description: Mutator method for initializing the transaction category
     * @param transaction_category - Transaction Category (ex. Direct Deposit)
     */
    public void setCategory(String transaction_category) {
        this.transaction_category = transaction_category;
    }


    /**
     * Description: Mutator method for initializing the transaction's row value in the spreadsheet
     * @param row_value - Row value in spreadsheet
     */
    public void setRowValue(String row_value) {
        this.row_value = row_value;
    }

    /**
     * Description: Mutator method for initializing the transaction's column value in the spreadsheet
     * @param column_value - Column value in spreadsheet
     */
    public void setColumnValue(char column_value) {
        this.column_value = column_value;
    }

    //////////////////////////////////
    // ===== Accessor Methods ===== //
    //////////////////////////////////

    /**
     * Description: Accessor method for retrieving the teansaction's date
     */
    public String getDate() {
        return date;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's type
     */
    public String getType() {
        return transaction_type;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's category
     */
    public String getCategory() {
        return transaction_category;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's row value in spreadsheet
     */
    public String getRowValue() {
        return row_value;
    }

    /**
     * Description: Accessor method for retrieving the teansaction's column value in spreadsheet
     */
    public char getColumnValue() {
        return column_value;
    }

    ////////////////////////////////
    // ===== Object Methods ===== //
    ////////////////////////////////

    /**
     * Description: Method to compute the location of the transaction in the spreadsheet using the column and row value 
     * @return
     */
    public String computeLocation(){
        cell_location = column_value + row_value;
        return cell_location;
    }

    /**
     * Description: Method to output the details of the transaction object
     * @return
     */
    public String toString(){
        return (
            vendor + ", " + amount + "\n"
            + date + "\n"
            + transaction_type + ": " + transaction_category + "\n"
            + cell_location
        );
    }
}