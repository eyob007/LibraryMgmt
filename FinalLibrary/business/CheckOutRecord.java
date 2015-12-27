package business;

import java.util.ArrayList;

public class CheckOutRecord {
	
	private ArrayList<CheckoutRecordEntry> chkoutRecEnt;

	public CheckOutRecord(ArrayList<CheckoutRecordEntry> chkoutRecEnt) {
		this.chkoutRecEnt = chkoutRecEnt;
	}

	public ArrayList<CheckoutRecordEntry> getChkoutRecEntry() {
		return chkoutRecEnt;
	}
	
	public void addEntry(CheckoutRecordEntry checkoutEntry){
		
		this.chkoutRecEnt.add(checkoutEntry);
	}

}