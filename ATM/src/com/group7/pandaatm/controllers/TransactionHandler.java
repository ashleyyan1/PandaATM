package com.group7.pandaatm.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data_access_atm.ATMDA;
import com.group7.pandaatm.data_access_atm.AccountDA;
import com.group7.pandaatm.data_access_atm.TransactionDA;
import com.group7.pandaatm.database.Database;
import com.group7.pandaatm.entity_atm.ATM;
import com.group7.pandaatm.entity_atm.ATMSession;
import com.group7.pandaatm.entity_atm.Account;
import com.group7.pandaatm.entity_atm.DebitCard;

public class TransactionHandler {

	private Database db;
	private TransactionDA transactionData;
	private AccountDA accountData;
	private ATMDA atmData;
	private ATMSession atmSession;
	private SessionTimer sessionTimer;
	private ObjectInputStream dataInput;
	private ObjectOutputStream dataOutput;
	private ArrayList<Account> availableAccounts;
	private DebitCard debitCard;
	private ATM atm;
	
	public TransactionHandler(Database database, AccountDA aDA, ATMDA atmData, ATMSession atmS,
								SessionTimer sT, ObjectInputStream dI, ObjectOutputStream dO,
								DebitCard dC, ATM atmFromSH) throws SQLException {
		this.db = database;
		this.transactionData = new TransactionDA(db);
		this.accountData = aDA;
		this.atmData = atmData;
		this.atmSession = atmS;
		this.sessionTimer = sT;
		this.dataInput = dI;
		this.dataOutput = dO;
		this.debitCard = dC;
		this.atm = atmFromSH;
		this.availableAccounts = accountData.getAvailableAccounts(debitCard);
	}//end Constructor
	
	/**
	 * This function performs a withdrawal transaction.
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 */
	public void initiateWithdrawal() throws IOException, ClassNotFoundException, InterruptedException {	
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		
		Message chosenAccount = (Message)dataInput.readObject();
		if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
		sessionTimer.refreshTimer();
		
		if(chosenAccount.flag() == 16) {//Transaction Account Selected
			
			//Show Available Balance to User Before Prompting for an amount
			Account account = accountData.getAccountInfo(chosenAccount.getIntegerMessages().get(0));
			Message accountBalanceMsg = new Message(14);
			accountBalanceMsg.addDoubleM(account.getAccountBal());
			
			//Check if Account is of type 'Checking' for `minReqBalance` (Balance Warning Handled in Client)
			if(account.getAccountType() == 1) {
				accountBalanceMsg.addDoubleM(account.getMinReqBalance());
			}	
			dataOutput.writeObject(accountBalanceMsg);
			
			Message transactionAmountMsg = (Message) dataInput.readObject();
			if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
			sessionTimer.refreshTimer();
			//Record Amount from User to Deposit
			if(transactionAmountMsg.flag() == 18) {//Transaction Amount
				
				//Pull Amount from transactionAmount message
				double transactionAmount = transactionAmountMsg.getDoubleMessages().get(0);
				//Purpose is to allow one last check of accountBalance before confirming trans
				Message confirmTransaction = (Message) dataInput.readObject();
				if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
				sessionTimer.refreshTimer();
				
				if(confirmTransaction.flag() == 19) {//Transaction Confirmation
					
					try {
						db.lock();
						//Double Check account balance one final time (avoids stale data)
						account = accountData.getAccountInfo(account.getAccountNumber());
						
						if(transactionAmount > account.getAccountBal()) {//Transaction Not Possible
							Message insufficientBalance = new Message(20);
							insufficientBalance.addStringM("Insufficient Balance to complete transaction. "
															+ "Transaction aborted.");
							dataOutput.writeObject(insufficientBalance);
						}
						else {//Enough Funds are available to continue transaction
							
							//Checks to make sure ATM has enough bills to dispense for transaction
							if(atm.getWithdrawalBillsRemaining() >= transactionAmount / 20) {
								transactionData.insertWithdrawalTransaction(transactionAmount, atmSession.getSessionID(), 
																			 account.getAccountNumber(), false);
								Message transactionSuccessful = new Message(12);//Transaction Successful
								transactionSuccessful.addStringM("Transaction Successful. Returning to Main Menu.");
								dataOutput.writeObject(transactionSuccessful);
								
								
								//Checks minBillThreshold (Notifies bank when withdrawal bill box is low)
								if(atm.getWithdrawalBillsRemaining() - transactionAmount / 20 < atm.getMinBillThreshold()) {
									System.out.println("Remaining Bills Available for Withdrawal has "
											   + "fallen below set threshold.\nATM ID: " + atm.getMachineID()
											   + "\nMachine Address: " + atm.getMachineAddress());
								}
							}
							else {//ATM doesn't have enough bills for transaction
								Message insufficientBillCount = new Message(21);
								insufficientBillCount.addStringM("Not enough bills available in ATM. "
																	+ "Transaction aborted.");
								dataOutput.writeObject(insufficientBillCount);
							}			
						}
						db.unlock();
					} catch(Exception e) {
						db.unlock();
						throw e;
					}
				}
				else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
					Message transactionCancelled = new Message(17);
					transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
					dataOutput.writeObject(transactionCancelled);
				}
				else {//Communication Error
					Message unexpectedRequest = new Message(7);
					unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
					dataOutput.writeObject(unexpectedRequest);
				}	
			}
			else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
				Message transactionCancelled = new Message(17);
				transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
				dataOutput.writeObject(transactionCancelled);
			}
			else {//Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}	
			
		}
		else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
			Message transactionCancelled = new Message(17);
			transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
			dataOutput.writeObject(transactionCancelled);
		}
		else {//Communication Error
			Message unexpectedRequest = new Message(7);
			unexpectedRequest.addStringM("Expected Account Selection. Received Unexpected Request Type.");
			dataOutput.writeObject(unexpectedRequest);
		}	
	}//end initiateWithdrawal

	/**
	 * This function performs a deposit transaction.
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 */
	public void initiateDeposit() throws IOException, ClassNotFoundException, InterruptedException {
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		
		Message chosenAccount = (Message)dataInput.readObject();
		if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
		sessionTimer.refreshTimer();
		
		if(chosenAccount.flag() == 16) {//Transaction Account Selected
			
			//Show Available Balance to User Before Prompting for amount (Bill Counts)
			Account account = accountData.getAccountInfo(chosenAccount.getIntegerMessages().get(0));
			Message accountBalanceMsg = new Message(14);
			accountBalanceMsg.addDoubleM(account.getAccountBal());
			
			//Send the number of bills the ATM has room for in its deposit box
			accountBalanceMsg.addIntegerM(atm.getMaxDepositCapacity() - atm.getDepositBillCount());
			dataOutput.writeObject(accountBalanceMsg);
			
			Message transactionAmountMsg = (Message) dataInput.readObject();
			if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
			sessionTimer.refreshTimer();
			//Record Amount from User to Deposit
			if(transactionAmountMsg.flag() == 18) {//Transaction Amount (NUMBER OF BILLS)
				
				/*
				 * Message Field Order for transactionAmount (Bill Count)
				 * 0 = $1   Bills
				 * 1 = $2   Bills
				 * 2 = $5   Bills
				 * 3 = $10  Bills
				 * 4 = $20  Bills
				 * 5 = $50  Bills
				 * 6 = $100 Bills
				 */
				
				//Pull Amount from transactionAmount message
				int onesCount = transactionAmountMsg.getIntegerMessages().get(0);
				int twosCount = transactionAmountMsg.getIntegerMessages().get(1);
				int fivesCount = transactionAmountMsg.getIntegerMessages().get(2);
				int tensCount = transactionAmountMsg.getIntegerMessages().get(3);
				int twentiesCount = transactionAmountMsg.getIntegerMessages().get(4);
				int fiftiesCount = transactionAmountMsg.getIntegerMessages().get(5);
				int hundredsCount = transactionAmountMsg.getIntegerMessages().get(6);
				
				double transactionAmount = onesCount + 2 * twosCount + 5 * fivesCount + 10 * tensCount
									     + 20 * twentiesCount + 50 * fiftiesCount + 100 * hundredsCount;
				int billCount = onesCount + twosCount + fivesCount + tensCount + twentiesCount + fiftiesCount + hundredsCount;
				
				//Confirm with user to proceed with Deposit Transaction ("Cancelling Returns Bills")
				Message confirmTransaction = (Message) dataInput.readObject();
				if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
				sessionTimer.refreshTimer();
				
				if(confirmTransaction.flag() == 19) {//Transaction Confirmation
					
					try {
						db.lock();
						//Checks to make sure ATM has enough space bills in deposit box
						if(atm.getDepositBillCount() + billCount < atm.getMaxDepositCapacity()) {
							transactionData.insertDepositTransaction(transactionAmount, atmSession.getSessionID(), 
																		 account.getAccountNumber(), false);
							
							//Perform an update on ATM to adjust `depositBillCount` to reflect local billCount
							atmData.updateDepositBillCount(billCount, atm.getMachineID());
							
							Message transactionSuccessful = new Message(12);//Transaction Successful
							transactionSuccessful.addStringM("Transaction Successful. Returning to Main Menu.");
							dataOutput.writeObject(transactionSuccessful);		
							
							//Checks maxBillThreshold (Notifies bank when deposit bill box is close to full)
							if(atm.getDepositBillCount() > atm.getMaxBillThreshold()) {
								System.out.println("Remaining Space Available for depositing bills has "
										   + "risen above set threshold.\nATM ID: " + atm.getMachineID()
										   + "\nMachine Address: " + atm.getMachineAddress());
							}
						}
						else {//ATM doesn't have enough bills for transaction
							Message insufficientBillCount = new Message(21);
							insufficientBillCount.addStringM("Not enough bills available in ATM. "
																+ "Transaction aborted.");
							dataOutput.writeObject(insufficientBillCount);
						}
						db.unlock();
					} catch(Exception e) {
						db.unlock();
						throw e;
					}
				}
				else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
					Message transactionCancelled = new Message(17);
					transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
					dataOutput.writeObject(transactionCancelled);
				}
				else {//Communication Error
					Message unexpectedRequest = new Message(7);
					unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
					dataOutput.writeObject(unexpectedRequest);
				}	
			}
			else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
				Message transactionCancelled = new Message(17);
				transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
				dataOutput.writeObject(transactionCancelled);
			}
			else {//Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}	
			
		}
		else if(chosenAccount.flag() == 17) {//Cancel Transaction (Button Pressed)
			Message transactionCancelled = new Message(17);
			transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
			dataOutput.writeObject(transactionCancelled);
		}
		else {//Communication Error
			Message unexpectedRequest = new Message(7);
			unexpectedRequest.addStringM("Expected Account Selection. Received Unexpected Request Type.");
			dataOutput.writeObject(unexpectedRequest);
		}	
	}//end initiateDeposit

	/**
	 * This function performs a transfer transaction.
	 * 
	 * This function should also assign a value to timeDateOfTrans This function
	 * should also assign a value to transactionType
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public void initiateTransfer() throws IOException, InterruptedException, ClassNotFoundException {
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		
		//First Account read in is the Source Account
		Message firstChosenAccountMsg = (Message)dataInput.readObject();
		if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
		sessionTimer.refreshTimer();
		
		//FIRST CHOSEN ACCOUNT IS THE SOURCE ACCOUNT (FUNDS COME OUT OF HERE)
		if(firstChosenAccountMsg.flag() == 16) {//Transaction Account Selected
			
			//Show Available Balance (From Source Account) to User Before Prompting for amount
			Account srcAccount = accountData.getAccountInfo(firstChosenAccountMsg.getIntegerMessages().get(0));
			Message srcAccBalanceMsg = new Message(14);
			srcAccBalanceMsg.addDoubleM(srcAccount.getAccountBal());
			
			//Check if Account is of type 'Checking' for `minReqBalance` (Balance Warning Handled in Client)
			if(srcAccount.getAccountType() == 1) {
				srcAccBalanceMsg.addDoubleM(srcAccount.getMinReqBalance());
			}
			dataOutput.writeObject(srcAccBalanceMsg);
			
			//Return remaining accounts available after source account selected
			Message remainingAccounts = new Message(15);
			for(int i = 0; i < this.availableAccounts.size(); i++) {
				//Account has NOT already been selected
				if(this.availableAccounts.get(i).getAccountNumber() != srcAccount.getAccountNumber()) {
					remainingAccounts.addIntegerM(this.availableAccounts.get(i).getAccountNumber());
					remainingAccounts.addStringM(this.availableAccounts.get(i).getAccountName());
				}
			}//end for
			dataOutput.writeObject(remainingAccounts);
			
			//Second Account read in is the target account
			Message secondChosenAccountMsg = (Message)dataInput.readObject();
			if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
			sessionTimer.refreshTimer();
			
			if(secondChosenAccountMsg.flag() == 16) {
			
				//Show Available Balance (From Target Account) to User
				Account targetAccount = accountData.getAccountInfo(secondChosenAccountMsg.getIntegerMessages().get(0));
				Message targetAccBalanceMsg = new Message(14);
				targetAccBalanceMsg.addDoubleM(srcAccount.getAccountBal());
				dataOutput.writeObject(targetAccBalanceMsg);
				
				Message transactionAmountMsg = (Message) dataInput.readObject();
				if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
				sessionTimer.refreshTimer();
				
				//Record Amount from User to Transfer
				if(transactionAmountMsg.flag() == 18) {//Transaction Amount (Desired Amount to be digitally transferred)
					
					double transactionAmount = transactionAmountMsg.getDoubleMessages().get(0); 
					//Confirm with user to proceed with Transfer Transaction
					Message confirmTransaction = (Message) dataInput.readObject();
					if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
					sessionTimer.refreshTimer();
					
					if(confirmTransaction.flag() == 19) {//Transaction Confirmation (User Selected "Proceed/Yes/etc.")
						
						try {
							db.lock();
							if(accountData.getAccountInfo(srcAccount.getAccountNumber()).getAccountBal() >= transactionAmount) {
								
								transactionData.insertTransferTransaction(transactionAmount, targetAccount.getAccountNumber(), 
																	atmSession.getSessionID(), srcAccount.getAccountNumber(), false);
								Message transactionSuccessful = new Message(12);//Transaction Successful
								transactionSuccessful.addStringM("Transaction Successful. Returning to Main Menu.");
								dataOutput.writeObject(transactionSuccessful);
							}
							else {//Insufficient Funds Available
								Message insufficientBalance = new Message(20);
								insufficientBalance.addStringM("Insufficient Balance to complete transfer. "
																+ "Transaction aborted.");
								dataOutput.writeObject(insufficientBalance);
							}
							db.unlock();
						} catch(Exception e) {
							db.unlock();
							throw e;
						}
					}
					else if(confirmTransaction.flag() == 17) {//Cancel Transaction (Button Pressed)
						Message transactionCancelled = new Message(17);
						transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
						dataOutput.writeObject(transactionCancelled);
					}
					else {//Communication Error
						Message unexpectedRequest = new Message(7);
						unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
						dataOutput.writeObject(unexpectedRequest);
					}							
				}//end if(transactionAmountMsg.flag() == 18)
			}//end if(secondChosenAccount.flag() ==16)
			else if(secondChosenAccountMsg.flag() == 17) {//Cancel Transaction (Button Pressed)
				Message transactionCancelled = new Message(17);
				transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
				dataOutput.writeObject(transactionCancelled);
			}
			else {//Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected Amount Selection. Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}	
		}
		else if(firstChosenAccountMsg.flag() == 17) {//Cancel Transaction (Button Pressed)
			Message transactionCancelled = new Message(17);
			transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
			dataOutput.writeObject(transactionCancelled);
		}
		else {//Communication Error
			Message unexpectedRequest = new Message(7);
			unexpectedRequest.addStringM("Expected Account Selection. Received Unexpected Request Type.");
			dataOutput.writeObject(unexpectedRequest);
		}	
	}//end initiateTransfer
	
	private Message prepareAvailableAccountMsg() {
		
		Message preparedAccountsInfo = new Message(15);
		
		for(int i = 0; i < this.availableAccounts.size(); i++) {
			preparedAccountsInfo.addIntegerM(this.availableAccounts.get(i).getAccountNumber());
			preparedAccountsInfo.addStringM(this.availableAccounts.get(i).getAccountName());
		}
		return preparedAccountsInfo;
	}//end prepareAvailableAccountMsg

	public void initiateBalanceInquiry() throws IOException, InterruptedException, ClassNotFoundException {
		
		//Sends Message to Client which accounts are available for transaction
		Message availableAccountMsg = prepareAvailableAccountMsg();
		dataOutput.writeObject(availableAccountMsg);
		
		//A selected account is read in from the client
		Message chosenAccountMsg = (Message)dataInput.readObject();
		if (!sessionTimer.getSessionThreadActive()) throw new InterruptedException();
		sessionTimer.refreshTimer();
		
		if(chosenAccountMsg.flag() == 16) {//"Transaction" Account Selected
			
			Account account= accountData.getAccountInfo(chosenAccountMsg.getIntegerMessages().get(0));
			Message accountBalance = new Message(14);
			accountBalance.addDoubleM(account.getAccountBal());
			dataOutput.writeObject(accountBalance);
		}
		else if(chosenAccountMsg.flag() == 17) {//Cancel Transaction (Button Pressed)
			Message transactionCancelled = new Message(17);
			transactionCancelled.addStringM("Transaction was cancelled. Returning to menu.");
			dataOutput.writeObject(transactionCancelled);
		}
		else {
			Message unexpectedRequest = new Message(7);
			unexpectedRequest.addStringM("Expected Account Selection. Received Unexpected Request Type.");
			dataOutput.writeObject(unexpectedRequest);
		}	
	}//end initiateBalanceInquiry
}// end TransactionHandler