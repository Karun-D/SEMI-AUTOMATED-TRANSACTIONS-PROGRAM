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

    public void setDate(String date) {
        this.date = date;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setCategory(String transaction_category) {
        this.transaction_category = transaction_category;
    }

    public void setRowValue(String row_value) {
        this.row_value = row_value;
    }

    public void setColumnValue(char column_value) {
        this.column_value = column_value;
    }

    //////////////////////////////////
    // ===== Accessor Methods ===== //
    //////////////////////////////////

    public String getDate() {
        return date;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return transaction_type;
    }

    public String getCategory() {
        return transaction_category;
    }

    public String getRowValue() {
        return row_value;
    }

    public char getColumnValue() {
        return column_value;
    }

    ////////////////////////////////
    // ===== Object Methods ===== //
    ////////////////////////////////

    public String computeLocation(){
        cell_location = column_value + row_value;
        return cell_location;
    }

    public String toString(){
        return (
            vendor + ", " + amount + "\n"
            + date + "\n"
            + transaction_type + ": " + transaction_category + "\n"
            + cell_location
        );
    }
}