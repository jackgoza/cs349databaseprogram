
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private static accountManager accountHub;

    public static void main(String args[]) {

	try {
	    accountManager accountHub = new accountManager();

	    accountHub.getTable();

//	selectTableSource(); Does not work.
	    JFrame frame = new AccountTransactionLayout(accountHub);
	    frame.pack();
	    frame.setVisible(true);
	}
	catch (SQLException s) {
	    System.err.println(s);
	}
    }

    private static void selectTableSource() {

	JFrame tableChoice = new JFrame("Choose table source");
	tableChoice.setLayout(new FlowLayout());
	JTextArea info = new JTextArea("Would you like to"
		+ "use the existing table or create a new one?");

	JButton newButton = new JButton("Create new table");
	JButton oldButton = new JButton("Use existing table");

	tableChoice.add(info);
	tableChoice.add(newButton);
	tableChoice.add(oldButton);

	newButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		tableChoice.dispose();

		try {
		    accountHub.createTables();
		}
		catch (SQLException s) {
		    System.err.println(s);
		}
	    }
	});

	oldButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		tableChoice.dispose();

		try {
		    accountHub.getTable();
		}
		catch (SQLException s) {
		    System.err.println(s);
		}
	    }
	});

	tableChoice.pack();
	tableChoice.setVisible(true);
    }

}
