package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class LibraryMember extends Person implements Serializable {
	private static final long serialVersionUID = 1L;
	private String member_ID;
	private CheckOutRecord checkoutrecord;

	public LibraryMember(String f, String l, String t, Address a, String m,
			CheckOutRecord c) {
		super(f, l, t, a);
		this.setMember_ID(m);
		this.setCheckoutrecord(c);
	}

	public CheckOutRecord getCheckoutrecord() {
		return checkoutrecord;
	}

	public void setCheckoutrecord(CheckOutRecord checkoutrecord) {
		this.checkoutrecord = checkoutrecord;
	}

	public String getMember_ID() {
		return member_ID;
	}

	public void setMember_ID(String member_ID) {
		this.member_ID = member_ID;
	}
	
	public CheckOutRecord checkout(BookCopy bcopy,Date checkoutDate, Date todayPlusMaxCheckoutLen, CheckOutRecord chkrec){		
		if(!bcopy.isAvailable()){
			return null;
			
		} else {
			
			ArrayList<CheckoutRecordEntry> chkREntryList=new ArrayList<CheckoutRecordEntry> ();
			CheckoutRecordEntry chkREntry = new CheckoutRecordEntry();
			chkREntry.createEntry(bcopy, checkoutDate, todayPlusMaxCheckoutLen);
			chkREntryList.add(chkREntry);
			
			if(chkrec==null){
				//searchedlibraryMem.setCheckoutrecord(new CheckOutRecord(chkREntry));
				chkrec =new CheckOutRecord(chkREntryList);
			}
			else
			  chkrec.addEntry(chkREntry);
			//CheckOutRecord chkrec = new CheckOutRecord(chkREntryList);

			//chkrec.addEntry(chkREntry);
			bcopy.changeAvailability();
			
			return chkrec;		
		} 	
	}



}
