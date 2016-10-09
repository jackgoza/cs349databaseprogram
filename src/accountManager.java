/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jack
 */
import java.sql.*;
import java.util.*;

public class accountManager {

    private Connection con;
    private Statement stmt;
    private int numberOfAccounts;
    private ArrayList<bankAccount> accountData;
    public Object[][] displayData;

    // Load class and create a connection to the database
    public accountManager() throws SQLException {
	// The ODBC Data Source Name (DSN) is example
	//String url = "jdbc:odbc:example";
	String url = "jdbc:mysql://kc-sce-appdb01.kc.umkc.edu/jgkp9?allowMultiQueries=true";
	String userID = "jgkp9";
	String password = "java";

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	}
	catch (java.lang.ClassNotFoundException e) {
	    System.out.println(e);
	    System.exit(0);
	}

	accountData = new ArrayList<bankAccount>();
	con = DriverManager.getConnection(url, userID, password);
	stmt = con.createStatement();
	getTable();
//	createTables();
    }

    public void cleanup() throws SQLException {
	// Close connection and statement
	// Connections, statements, and result sets are
	// closed automatically when garbage collected
	// but it is a good idea to free them as soon
	// as possible.
	// Closing a statement closes its current result set.
	// Operations that cause a new result set to be
	// created for a statement automatically close
	// the old result set.
	stmt.close();
	con.close();
    }

    public void createTables() throws SQLException {

	// TODO: See if this is actually a good idea. For this project,
	// it seems pretty valid, but upon expansion this is ludicrous.
	stmt.executeUpdate("DROP TABLE IF EXISTS Accounts");

	String sqlcmd1 = "CREATE TABLE Accounts "
		+ "(AcctID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, "
		+ "AcctName VARCHAR(64),"
		+ "Balance INT NOT NULL)";

	bankAccount savings = new bankAccount(1, "Savings", 500);
	bankAccount checking = new bankAccount(2, "Checking", 270);
	bankAccount credit = new bankAccount(3, "Credit", 1000);
	bankAccount child = new bankAccount(4, "Child", 200);

	stmt.executeUpdate(sqlcmd1);

	stmt.executeUpdate(savings.createSQLInitString());
	stmt.executeUpdate(checking.createSQLInitString());
	stmt.executeUpdate(credit.createSQLInitString());
	stmt.executeUpdate(child.createSQLInitString());

	accountData.add(savings);
	accountData.add(checking);
	accountData.add(credit);
	accountData.add(child);

	numberOfAccounts = accountData.size();

	updateDisplayData();

    }

    public boolean transferFunds(int toAcct, int fromAcct, int amount) {
	try {
	    if (accountData.get(fromAcct).balance > amount) {
		accountData.get(fromAcct).balance -= amount;
		accountData.get(toAcct).balance += amount;
		accountData.get(fromAcct).changeFlag = true;
		accountData.get(toAcct).changeFlag = true;
		updateTable();
		updateDisplayData();
		return true;
	    }
	}
	catch (Exception e) {
	    return false;
	}

	return false;
    }

    public void updateDisplayData() {
	displayData = new Object[numberOfAccounts][];
	bankAccount tempAccount;
	for (int i = 0; i < numberOfAccounts; i++) {
	    displayData[i] = accountData.get(i).provideData();
	}

    }

    public void updateTable() {
	try {

	    for (int i = 0; i < numberOfAccounts; i++) {
		if (accountData.get(i).changeFlag == true) {
		    stmt.addBatch(accountData.get(i).createSQLUpdateString());
		    accountData.get(i).changeFlag = false;
		}
	    }
	    stmt.executeBatch();
	}
	catch (SQLException sqlE) {
	    System.err.println("Unable to update database");
	}
    }

    public void getTable() throws SQLException {

	accountData.clear();

	String getCommand = "SELECT * FROM Accounts";
	ResultSet rs = stmt.executeQuery(getCommand);

	ResultSetMetaData rsmd = rs.getMetaData();

	int numCols = rsmd.getColumnCount();

	boolean more = rs.next();
	int number;
	String name;
	int amount;
	while (more) {
	    number = rs.getInt("AcctID");
	    name = rs.getString("AcctName");
	    amount = rs.getInt("Balance");

	    accountData.add(new bankAccount(number, name, amount));

	    more = rs.next();
	}

	numberOfAccounts = accountData.size();

	updateDisplayData();
    }

    public void displayResultSet(ResultSet rs) {
	int i;

	System.out.println();
	System.out.println();
	System.out.println("Result Set:");
	System.out.println("------------------------");
	System.out.println();

	try {
	    ResultSetMetaData rsmd = rs.getMetaData();

	    int numCols = rsmd.getColumnCount();

	    for (i = 1; i <= numCols; i++) {
		System.out.print(rsmd.getColumnLabel(i));
		System.out.print(" ");
	    }
	    System.out.println();

	    boolean more = rs.next();
	    while (more) {
		for (i = 1; i <= numCols; i++) {
		    // Every field value is fetched as a string
		    // JDBC will convert the values from their
		    // stored type to string type
		    System.out.print(rs.getString(i));
		    System.out.print(" ");
		}
		System.out.println();

		more = rs.next();
	    }
	    System.out.println();
	}
	catch (SQLException ex) {
	    System.out.println("*** SQLException");

	    while (ex != null) {
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("Message: " + ex.getMessage());
		System.out.println("Vendor: " + ex.getErrorCode());
		ex = ex.getNextException();
		System.out.println("");
	    }
	}
    }

}
