package business;

import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void checkoutBook(String memberId, String isbn) throws LibrarySystemException;
	public boolean addBookCopy(String isbn) throws LibrarySystemException;
	public Book searchBook(String isbn);
	public Auth login(String id, String password) throws LoginException;
}
