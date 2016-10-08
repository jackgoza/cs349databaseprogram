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

public class accountManager {

    private Connection con;
    private Statement stmt;
    public Object[] accountData;

    // Load class and create a connection to the database
    public accountManager() throws SQLException {
	// The ODBC Data Source Name (DSN) is example
	//String url = "jdbc:odbc:example";
	String url = "jdbc:mysql://kc-sce-appdb01.kc.umkc.edu/jgkp9";
	String userID = "jgkp9";
	String password = "java";

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	}
	catch (java.lang.ClassNotFoundException e) {
	    System.out.println(e);
	    System.exit(0);
	}

	con = DriverManager.getConnection(url, userID, password);
	stmt = con.createStatement();
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

	accountData = new Object[]{savings, checking,
	    credit, child};

    }

    public boolean transferFunds(int fromAcct, int toAcct, int amount) {
	bankAccount toObject, fromObject;
	toObject = (bankAccount) accountData[toAcct];
	fromObject = (bankAccount) accountData[fromAcct];
	if (fromObject.balance > amount) {
	    fromObject.balance -= amount;
	    toObject.balance += amount;
	    accountData[fromAcct] = fromObject;
	    accountData[toAcct] = toObject;
	    updateTable();
	    return true;
	}
	return false;
    }

    public void updateTable() {

    }

    public void getTable() throws SQLException {

	String getCommand = "SELECT * FROM Accounts";
	ResultSet rs = stmt.executeQuery(getCommand);

	displayResultSet(rs);

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
