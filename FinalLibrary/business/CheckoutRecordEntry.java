package business;

import java.util.Date;


public class CheckoutRecordEntry {
	
	private Date checkoutDate;
	private Date dueDate;
	private BookCopy bkCopy;
	
	public Date getCheckoutDate() {
		return checkoutDate;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	public BookCopy getBkCopy() {
		return bkCopy;
	}
	
	public void createEntry(BookCopy bcopy, Date chkdate, Date dueDate){
		this.bkCopy = bcopy;
		this.checkoutDate = chkdate;
		this.dueDate = dueDate;
	}

}
