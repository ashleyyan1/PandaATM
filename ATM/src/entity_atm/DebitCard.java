package entity_atm;

import java.time.LocalDateTime;

import database.Database;

public class DebitCard {

	private long cardNumber;
	private String cardHolderName;
	private LocalDateTime cardExpDate;
	private int pinNumber;
	private int customerID;
	private int branchNumber;
	
	public DebitCard(long cN, String cHN, LocalDateTime cED, int pN,
						int cID, int bN) {
		this.cardNumber = cN;
		this.cardHolderName = cHN;
		this.cardExpDate = cED;
		this.pinNumber = pN;
		this.customerID = cID;
		this.branchNumber = bN;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public LocalDateTime getCardExpDate() {
		return cardExpDate;
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public int getCustomerID() {
		return customerID;
	}

	public int getBranchNumber() {
		return branchNumber;
	}
	
	@Override
	public String toString() {
		String str = "Card Number: " + this.cardNumber
				   + "\nCustomer ID: " + this.customerID
				   + "\nCard Exp. Date: " + this.cardExpDate.format(Database.getTimeFormat())
				   + "\nCard Pin Number: " + this.pinNumber + "\n";
		return str;
	}
}//end DebitCard
