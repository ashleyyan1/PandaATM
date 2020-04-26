package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import data_access_atm.AccountDA;
import data_access_atm.TransactionDA;
import database.Database;
import entity_atm.ATMSession;
import entity_atm.Account;
import entity_atm.DebitCard;
import networking.Message;

public class TransactionHandler {

	private Database db;
	private TransactionDA transactionData;
	private AccountDA accountData;
	private ATMSession atmSession;
	private SessionTimer sessionTimer;
	private ObjectInputStream dataInput;
	private ObjectOutputStream dataOutput;
	private ArrayList<Account> availableAccounts;
	private DebitCard debitCard;
	
	public TransactionHandler(Database database, AccountDA aDA, ATMSession atmS,
								SessionTimer sT, ObjectInputStream dI, ObjectOutputStream dO,
								DebitCard dC) throws SQLException {
		this.db = database;
		this.transactionData = new TransactionDA(db);
		this.accountData = aDA;
		this.atmSession = atmS;
		this.sessionTimer = sT;
		this.dataInput = dI;
		this.dataOutput = dO;
		this.debitCard = dC;
		this.availableAccounts = accountData.getAvailableAccounts(debitCard);
	}//end Constructor
	
	/**
	 * This function performs a withdrawal transaction.
	 * 
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 */
	public boolean initiateWithdrawal() throws IOException {	
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		return false;
	}

	/**
	 * This function performs a deposit transaction.
	 * 
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 */
	public boolean initiateDeposit() throws IOException {
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		return false;
	}

	/**
	 * This function performs a transfer transaction.
	 * 
	 * @return - True or false if transaction was successful
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 */
	public boolean initiateTransfer() throws IOException {
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		return false;
	}
	
	private Message prepareAvailableAccountMsg() {
		
		Message preparedAccountsInfo = new Message(15);
		
		for(int i = 0; i < this.availableAccounts.size(); i++) {
			preparedAccountsInfo.addIntegerM(this.availableAccounts.get(i).getAccountNumber());
			preparedAccountsInfo.addStringM(this.availableAccounts.get(i).getAccountName());
		}
		return preparedAccountsInfo;
	}//end prepareAvailableAccountMsg
}// end TransactionHandler