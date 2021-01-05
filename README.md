<!-- PROJECT Title -->
# Semi-automated Transactions

<!-- ABOUT THE PROJECT -->
## About The Project

This project was built to automate the process of tracking my financial data. Instead of manually entering the transactions into a Google Sheet, this program does it for you by reading a monthly bank statement via the command line. 

It is the responsibility of the user to categorize each transaction if the program does not recognize the vendor. Recognized vendors are serialized into a file and read back into the application for future uses. This process allows the program to automate future reads. 

# Demo
![](demo.gif)

### Built With

* [Java with Gradle](https://gradle.org/install/)
* [Google Sheets API](https://developers.google.com/sheets/api/quickstart/java)

### Prerequisites

1. [Gradle must be installed](https://gradle.org/install/)
2. User must follow the [Google Sheets API Guide: Step #1](https://developers.google.com/sheets/api/quickstart/java)
3. Download monthly statement as a .csv file

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/Karun-D/semi-automated-transactions.git
   ```
2. Insert credentials.json file into src/main/resources/
3. Insert link of custom spreadsheet src/main/java/SheetsAPI.java into the variable "spreadsheetID" on lines 73 and 106


<!-- USAGE EXAMPLES -->
## Usage

Enter name of the .csv into the following command
  ```sh
   gradle run --console=plain --args='<FILENAME>'  
   ```

<!-- ROADMAP -->
## Future Plans

1. Find a practical way to fully automate the classification process and eliminate GUI (Asccess to an existing database of categorized vendors? Create a dabase via webscraping?)
