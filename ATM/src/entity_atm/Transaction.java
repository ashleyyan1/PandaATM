package entity_atm;

import java.time.LocalDateTime;

import database.Database;

public class Transaction {

	private int transactionID;
	private LocalDateTime timeDateOfTrans;
	private int transactionType;
	private double amount;
	private int targetAccNumber;
	private int sessionID;
	private int accountNumber; //accountNumber is the Source Account Number
	
	public Transaction(int tID, LocalDateTime tDOT, int tT, double a,
						int tAN, int sID, int aN) {
		this.transactionID = tID;
		this.timeDateOfTrans = tDOT;
		this.transactionType = tT;
		this.amount = a;
		this.targetAccNumber = tAN;
		this.sessionID = sID;
		this.accountNumber = aN;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public LocalDateTime getTimeDateOfTrans() {
		return timeDateOfTrans;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public int getTargetAccNumber() {
		return targetAccNumber;
	}

	public int getSessionID() {
		return sessionID;
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	
	/*
	 * This function performs a withdrawal transaction.
	 * @param amt - The desired amount to withdraw
	 * @param srcAcc - The account to withdraw money from
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans
	 * This function should also assign a value to transactionType
	 */
	public boolean withdraw(int amt, int srcAcc) {
		return false;
	}
	
	/*
	 * This function performs a deposit transaction.
	 * @param amt - The amount of money deposited
	 * @param targAcc - The target account to deposit money into
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans
	 * This function should also assign a value to transactionType
	 */
	public boolean deposit(int amt, int targAcc) {
		return false;
	}
	
	/*
	 * This function performs a transfer transaction.
	 * @param amt - The amount of money to be transferred
	 * @param srcAcc - The source account to withdraw money from
	 * @param targAcc - The target account to despoit money into
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans
	 * This function should also assign a value to transactionType
	 */
	public boolean transfer(int amt, int srcAcc, int targAcc) {
		return false;
	}
	
	@Override
	public String toString() {
		String str = new String();
		str = "Transaction ID: " + this.transactionID
		    + "\nTransaction Date/Time: " + this.timeDateOfTrans.format(Database.getTimeFormat())
		    + "\nAmount: " + this.amount
		    + "\nSession ID: " + this.sessionID
		    + "\nSource Account Number: " + this.accountNumber;
		
		if(this.transactionType == 0) {
			str += "\nTransaction Type: Withdraw\n";
		}
		else if(this.transactionType == 1) {
			str += "\nTransaction Type: Deposit\n";
		}
		else {
			str += "\nTransaction Type: Transfer" 
				+  "\nTarget Account Number: " + this.targetAccNumber + "\n";
		}
		return str;
	}
}//end Transaction
