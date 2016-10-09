/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jack
 */
public class bankAccount {

    protected int idNumber = 0;
    protected String accountName = "";
    protected int balance = 0;
    protected boolean changeFlag = false;

    public bankAccount(int newID, String newName, int newMoney) {
	idNumber = newID;
	accountName = newName;
	balance = newMoney;
    }

    protected String createSQLInitString() {
	String combinedString = "INSERT INTO Accounts VALUES("
		+ Integer.toString(idNumber) + ",'" + accountName
		+ "', " + Integer.toString(balance) + ")";

	return combinedString;

    }

    protected String createSQLUpdateString() {
	String query = "UPDATE Accounts SET Balance = " + balance + " where AcctID = " + idNumber;
	return query;
    }

    protected Object[] provideData() {

	Object[] accountAsObject = {idNumber, accountName, balance};
	return accountAsObject;

    }

}
