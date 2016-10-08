
import java.sql.SQLException;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jack
 */
public class lab7 {

    public static void main(String args[]) {
	try {

	    accountManager accountHub = new accountManager();
//	    accountHub.createTables();

//	    accountHub.transferFunds(1, 2, 300);
	    JFrame frame = new AccountTransactionLayout(accountHub);
	    frame.pack();
	    frame.setVisible(true);
//	    accountHub.updateTable();
//	    accountHub.getTable();
//	    accountHub.cleanup();
	}
	catch (SQLException e) {
	    System.err.println(e);
	}
    }
}
