package unit_tests;

import java.sql.SQLException;
import java.time.LocalDateTime;

import data_access_atm.ATMDA;
import data_access_atm.ATMSessionDA;
import data_access_atm.AccountDA;
import data_access_atm.AccountOpeningDA;
import data_access_atm.BankBranchDA;
import data_access_atm.CardActivationDA;
import data_access_atm.ClientDA;
import data_access_atm.DebitCardDA;
import data_access_atm.TransactionDA;
import database.Database;
import entity_atm.ATMSession;
import entity_atm.Client;

public class DatabaseUnitTest {

	public static void main(String[] args) throws SQLException {
		//Initialize Database
		Database testDB = new Database();
		
		//Testing BankBranch Data Access/Entity
		BankBranchDA bankBranchTestDA = new BankBranchDA(testDB);
		System.out.println("Creating Bank Branch... ");
		int bankBranch = bankBranchTestDA.insertBankBranch("3801 W Temple Ave, Pomona, CA 91768");
		System.out.println("Bank Branch Info: " + bankBranchTestDA.getBankBranchInfo(bankBranch));
		
		//Testing ATM Data Access/Entity
		ATMDA atmTestDA = new ATMDA(testDB);
		System.out.println("Creating ATM... ");
		int atm_one = atmTestDA.insertATM("3801 W Temple Ave, Pomona, CA 91768 - Bldg. 9 West Entrance", bankBranch);
		System.out.println("ATM Info: " + atmTestDA.getATMInfo(atm_one));
		
		//Testing Client Data Access/Entity	
		ClientDA clientTestDA = new ClientDA(testDB);
		System.out.println("Creating Client...");
		LocalDateTime client_one_DOB = LocalDateTime.of(1999, 10, 12, 12, 30);
		int client_one = clientTestDA.insertClient("Kenny Lee", "123 Hey There Eh", "909-696-6969", client_one_DOB, bankBranch);
		Client client_info = clientTestDA.getClientInfo(client_one);
		System.out.println("Client Info: " + client_info);
		
		//Testing Account Data Access/Entity
		AccountDA accountTestDA = new AccountDA(testDB);
		System.out.println("Creating Checking... ");
		int checking_acc = accountTestDA.insertCheckingAcc("Kenny's Checking SPENDY BOI");
		System.out.println("Checking Info: " + accountTestDA.getAccountInfo(checking_acc));
		System.out.println("Creating Savings... ");
		int savings_acc = accountTestDA.insertSavingsAcc("Kenny's College Fund");
		System.out.println("Savings Info: " + accountTestDA.getAccountInfo(savings_acc));
		
		//Testing AccountOpening Data Access/Entity
		AccountOpeningDA savingsAccOpeningTestDA = new AccountOpeningDA(testDB);
		System.out.println("Opening Savings Account...");
		savingsAccOpeningTestDA.insertAccountOpening(client_one, savings_acc);
		System.out.println("Account Opening Details: " + savingsAccOpeningTestDA.getAccountOpeningInfo(client_one, savings_acc));
		
		AccountOpeningDA checkingAccOpeningTestDA = new AccountOpeningDA(testDB);
		System.out.println("Opening Checking Account...");
		checkingAccOpeningTestDA.insertAccountOpening(client_one, checking_acc);
		System.out.println("Account Opening Details: " + checkingAccOpeningTestDA.getAccountOpeningInfo(client_one, checking_acc));
		
		//Testing Debit Card Data Access/Entity
		DebitCardDA debitCardTestDA = new DebitCardDA(testDB);
		System.out.println("Creating Debit Card: ");
		LocalDateTime card_exp = LocalDateTime.now().plusYears(4L);
		long debit_card_one = debitCardTestDA.insertDebitCard(client_info.getCustomerName() ,card_exp, 1234, client_one, bankBranch);
		System.out.println("Debit Card Info: " + debitCardTestDA.getDebitCardInfo(debit_card_one));
		
		//Test for duplicate Debit Card Number
		//System.out.println("Creating Duplicate Debit Card: ");
		//long duplicate_debit_card = debitCardTestDA.insertDebitCard(client_info.getCustomerName() ,card_exp, 1234, client_one, bankBranch);
		//System.out.println("Debit Card Info: " + debitCardTestDA.getDebitCardInfo(duplicate_debit_card));
		
		//Testing CardActivation Data Access/Entity
		System.out.println("Activating Debit Card...");
		CardActivationDA debitCardActivationTestDA = new CardActivationDA(testDB);
		debitCardActivationTestDA.insertCardActivation(debit_card_one, checking_acc);
		System.out.println("Card Activation Details:  " + debitCardActivationTestDA.getCardActivationInfo(debit_card_one, checking_acc));
		
		//Testing ATMSession Data Access/Entity
		System.out.println("Starting an ATM Session...");
		ATMSessionDA atmSessionTestDA = new ATMSessionDA(testDB);
		int atm_session_one = atmSessionTestDA.insertATMSession(atm_one, debit_card_one);
		ATMSession atmSession = atmSessionTestDA.getATMSessionInfo(atm_session_one);
		System.out.println("Session Info (Active): " + atmSession);
		
		//Tests Trigger to keep Session Active Status Synced
		System.out.println("******************************************");
		System.out.println("ATM Session Active Status: " + atmTestDA.getATMInfo(atmSession.getMachineID()).isSessionActive());
		System.out.println("******************************************");
		
		//Testing ATM Session Termination
		try {
			Thread.sleep(2000);//Terminate session after 2 seconds for testing purposes
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		atmSessionTestDA.terminateSession(atm_session_one);
		System.out.println("Session Info (Terminated): " + atmSessionTestDA.getATMSessionInfo(atm_session_one));
		
		//Tests Trigger to keep Session Active Status Synced
		System.out.println("******************************************");
		System.out.println("ATM Session Active Status: " + atmTestDA.getATMInfo(atmSession.getMachineID()).isSessionActive());
		System.out.println("******************************************");
		
		//Testing Transaction Data Access/Entity
		TransactionDA transactionTestDA = new TransactionDA(testDB);
		
		//Testing Deposit Functionality
		System.out.println("Processing Deposit Transaction...");
		int deposit_transaction_number = transactionTestDA.insertDepositTransaction(2000, atm_session_one, savings_acc);
		System.out.println("Deposit Transaction Details: " + transactionTestDA.getTransactionInfo(deposit_transaction_number));
		//Testing Withdrawal Functionality
		System.out.println("Processing Withdrawal Transaction...");
		int withdrawal_transaction_number = transactionTestDA.insertWithdrawalTransaction(500, atm_session_one, savings_acc);
		System.out.println("Withdrawal Transaction Details: " + transactionTestDA.getTransactionInfo(withdrawal_transaction_number));
		//Testing Transfer Functionality
		System.out.println("Processing Transfer Transaction...");
		int transfer_transaction_number = transactionTestDA.insertTransferTransaction(365, checking_acc, atm_session_one, savings_acc);
		System.out.println("Transfer Transaction Details: " + transactionTestDA.getTransactionInfo(transfer_transaction_number));
		
	}//end main
}//end DatabaseTest
