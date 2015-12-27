package business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import java.util.Iterator;
import java.util.List;














import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController extends Application implements
		ControllerInterface {
	public static Auth currentAuth = null;
	public static Stage stageArea;
	public static List<Author> AuthorList=new ArrayList<Author>();
	public static Scene previousScene;	
	public static Node ParentScreen;

	
	@Override
	public Auth login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if (!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException(
					"Passord does not match password on record");
		}
		currentAuth = map.get(id).getAuthorization();
		return currentAuth;
	}

	/**
	 * This method checks if memberId already exists -- if so, it cannot be
	 * added as a new member, and an exception is thrown. If new, creates a new
	 * LibraryMember based on input data and uses DataAccess to store it.
	 * 
	 */
	public void addNewMember(String memberId, String firstName,
			String lastName, String telNumber, Address addr)
			throws LibrarySystemException {
		try {
			CheckOutRecord c = null;
			DataAccess da = new DataAccessFacade();
			HashMap<String, LibraryMember> map = da.readMemberMap();
			if (map.containsKey(memberId)) {
				throw new LibrarySystemException("Member ID" + memberId
						+ " already added");
			}

			else {
				da.saveNewMember(new LibraryMember(firstName, lastName,
						telNumber, addr, memberId, c));
			}

		}

		catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("This " + memberId + " is already added");
			alert.showAndWait();
		}

	}

	/**
	 * Reads data store for a library member with specified id. Ids begin at
	 * 1001... Returns a LibraryMember if found, null otherwise
	 * 
	 */
	public LibraryMember search(String memberId) {
		DataAccess da = new DataAccessFacade();
		return da.searchMember(memberId);
	}

	/**
	 * Same as creating a new member (because of how data is stored)
	 */
	public void updateMemberInfo(String memberId, String firstName,
			String lastName, String street, String city, String state,
			String zip, String telephone) {
		Alert alert = new Alert(AlertType.INFORMATION);
		try {
			LibraryMember member = search(memberId);
			if (member != null) {
				member.setFirstName(firstName);
				member.setLastName(lastName);
				member.setTelephone(telephone);
				Address a = member.getAddress();
				a.setStreet(street);
				a.setCity(city);
				a.setState(state);
				a.setZip(zip);
				DataAccess da = new DataAccessFacade();
				da.updateMember(member);
				alert.setContentText(memberId + " succesfully updated");
				alert.showAndWait();
			} else {

				alert.setContentText(memberId + " not found");
				alert.showAndWait();
			}
		} catch (Exception e) {

			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

	}

	/**
	 * Looks up Book by isbn from data store. If not found, an exception is
	 * thrown. If no copies are available for checkout, an exception is thrown.
	 * If found and a copy is available, member's checkout record is updated and
	 * copy of this publication is set to "not available"
	 */
	/*
	 * public void checkoutBook(String memberId, String isbn) throws
	 * LibrarySystemException { }
	 */
	@Override
	public Book searchBook(String isbn) {
		DataAccess da = new DataAccessFacade();
		return da.searchBook(isbn);
	}

	/**
	 * Looks up book by isbn to see if it exists, throw exceptioni.
	 * Else add the book to storage
	 */
	public boolean addBook(String isbn, String title, int maxCheckoutLength, List<Author> authors) 
			throws LibrarySystemException {
		Book book = searchBook(isbn);
		if(book != null) 
		throw new LibrarySystemException("This book is alraedy in " + isbn 
				+ "  library collection!");
		else if(book == null)	{
			DataAccessFacade dataaccess=new DataAccessFacade();
			dataaccess.saveNewBook(new Book(isbn, title, maxCheckoutLength, authors));
		}				
				return true;
	}


	public boolean addBookCopy(String isbn) throws LibrarySystemException {
		Book book = searchBook(isbn);
		if (book == null)
			throw new LibrarySystemException("No book with isbn " + isbn
					+ " is in the library collection!");
		book.addCopy();
		return true;
	}

	public static void main(String[] args) {
		Application.launch(SystemController.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stageArea = stage;
		Parent root = FXMLLoader.load(getClass().getResource(
				"..\\View\\Login.fxml"));
		stage.setTitle("Library Management System");
		Scene scene=new Scene(root,700,450);
		scene.getStylesheets().addAll(this.getClass().getResource("..\\Style\\Style.css").toExternalForm());
        stage.setScene(scene);
		stage.show();
	}

	@Override
	public void checkoutBook(String memberId, String isbn)
			throws LibrarySystemException {
		LibraryMember member;
		Book book;
		member = search(memberId);
		book = searchBook(isbn);
		BookCopy[] bookcopies = book.getCopies();
		if (member != null && book != null && bookcopies.length != 0) {

		}
	}

	// for searching library member
	@FXML
	private TextField firstNameTextUpdate;
	@FXML
	private TextField lastNameTextUpdate;
	@FXML
	private TextField streetTextUpdate;
	@FXML
	private TextField cityTextUpdate;
	@FXML
	private TextField stateTextUpdate;
	@FXML
	private TextField zipTextUpdate;
	@FXML
	private TextField telephoneTextUpdate;
	@FXML
	private TextField memberIDUpdateText;

	@FXML
	private TextField searchedMemberID;
	@FXML
	private Button updateButton;

	@SuppressWarnings({ "null", "unused" })
	@FXML
	protected void handleSearchMemberButtonAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);

		LibraryMember member = search(searchedMemberID.getText());

		if (member == null) {
			alert.setContentText(searchedMemberID.getText() + " not found");
			alert.showAndWait();

		} else {
			alert.setContentText(member.getMember_ID() + " is found");
			alert.showAndWait();
			Address add = member.getAddress();
			memberIDUpdateText.setText(member.getMember_ID());
			firstNameTextUpdate.setText(member.getFirstName());
			lastNameTextUpdate.setText(member.getLastName());
			streetTextUpdate.setText(add.getStreet());
			cityTextUpdate.setText(add.getCity());
			stateTextUpdate.setText(add.getState());
			zipTextUpdate.setText(add.getZip());
			telephoneTextUpdate.setText(member.getTelephone());
			updateButton.setVisible(true);

		}
	}

	// for updating information based on member_ID

	@FXML
	protected void handleUpdateButtonAction(ActionEvent event) {
		Alert alertconf = new Alert(AlertType.CONFIRMATION);
		alertconf.setContentText("Do you want to update");
		alertconf.showAndWait();
		if (alertconf.getResult() == ButtonType.OK) {
			updateMemberInfo(memberIDUpdateText.getText(),
					firstNameTextUpdate.getText(),
					lastNameTextUpdate.getText(), streetTextUpdate.getText(),
					cityTextUpdate.getText(), stateTextUpdate.getText(),
					zipTextUpdate.getText(), telephoneTextUpdate.getText());
		}

	}

	// All ActionListenerController

	@FXML
	private TextField firstnameText;
	@FXML
	private TextField lastnameText;
	@FXML
	private TextField streetText;
	@FXML
	private TextField cityText;
	@FXML
	private TextField stateText;
	@FXML
	private TextField zipText;
	@FXML
	private TextField telephoneText;
	@FXML
	private TextField memberIDText;

	@FXML
	protected void handleAddMemberButtonAction(ActionEvent event) {

		SystemController sc = new SystemController();
		try {
			sc.addNewMember(memberIDText.getText(), firstnameText.getText(),
					lastnameText.getText(), telephoneText.getText(),
					new Address(streetText.getText(), cityText.getText(),
							stateText.getText(), zipText.getText()));
		} catch (LibrarySystemException e) {
			e.printStackTrace();
		}

	}

	@FXML
	protected void AddNewMemberHandler(ActionEvent event) throws IOException {
		Parent Current = FXMLLoader.load(getClass().getResource(
				"..\\View\\AddLibraryMember.fxml"));

		SplitPane splitPane1 = new SplitPane();	              
        splitPane1.getItems().addAll(ParentScreen, Current);
        Scene scene = new Scene(splitPane1,700,450);	             
        stageArea.setScene(scene);
	}

	// for SearchBOok and add copy
	// @FXML private TextField ISBNidTextBox;
	@SuppressWarnings("rawtypes")
	@FXML
	private ListView ListViewBook;
	@FXML
	private Button CopyBookButton;
	Book SearchedBook;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	protected void handleISBNSearchButtonAction(ActionEvent event) {

		DataAccessFacade dataAccess = new DataAccessFacade();
		try {
			Book SearchedBook = dataAccess.searchBook(ISBNidTextBox.getText());
			ObservableList<String>  items =FXCollections.observableArrayList ("ISBN:"+SearchedBook.getIsbn()+" Title: "+SearchedBook.getTitle());
			ListViewBook.setItems(items);			
			CopyBookButton.setVisible(true);
		} catch (Exception e) {

		}
	}

	@FXML
	protected void handleCopyToBookAction(ActionEvent event) {
		DataAccessFacade dataAccess = new DataAccessFacade();
		Book SearchedBook = dataAccess.searchBook(ISBNidTextBox.getText());
		SearchedBook.addCopy();
		dataAccess.saveNewBook(SearchedBook);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Copy of Book is successfully added in DB.");
		alert.show();
	}

	@FXML
	private TextField ISBNidTextBox;

	@FXML
	protected void SearchBookAndCopyHandler(ActionEvent event) {

		try {
			Parent Current = FXMLLoader.load(getClass().getResource(
					"..\\View\\SearchBook.fxml"));
			SplitPane splitPane1 = new SplitPane();	              
            splitPane1.getItems().addAll(ParentScreen, Current);
            Scene scene = new Scene(splitPane1,700,450);	             
            stageArea.setScene(scene);
		} catch (Exception e) {

		}
	}

	@FXML
	protected void SearchAndUpdateLibraryMemberHandler(ActionEvent event) {
		try {
			Parent Current = FXMLLoader.load(getClass().getResource(
					"..\\View\\SearchAndUpdateMember.fxml"));

			SplitPane splitPane1 = new SplitPane();	              
            splitPane1.getItems().addAll(ParentScreen, Current);
            Scene scene = new Scene(splitPane1,700,450);	             
            stageArea.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//AddBOok Action listeners: 
	
	  @FXML private TextField FirstNameText;
	    @FXML private TextField LastNameText;
	    @FXML private TextField AuthorCredentialText ;
	    @FXML private TextField AuthorPhoneText ;
	    @FXML private TextField AuthorCityText ;
	    @FXML private TextField AuthorAddressText;
	    @FXML private TextField AthourStateText ;
	    @FXML private TextField AuthorZipText;
	   @FXML private ListView<String> ListViewAuthor;
	   // @FXML private TableView<String> TableViewAuthor;

	    
	    @FXML protected void AddNewBookHandler(ActionEvent event) {
	    	SystemController sc=new SystemController();
	        try{  	
	        	   Node Current = FXMLLoader.load(getClass().getResource("..\\View\\AddNewBook.fxml"));
	        	   SplitPane splitPane1 = new SplitPane();	              
	               splitPane1.getItems().addAll(ParentScreen, Current);
	               Scene scene = new Scene(splitPane1,700,450);	             
	               stageArea.setScene(scene);
	           
	          }
	            catch(Exception e){
	            	 e.printStackTrace();
	            }
	    }
	    @FXML protected void ViewAuthorButtonAction(ActionEvent event) {
	 
	    	  LoadAuthor();	
	    }
		@SuppressWarnings("unchecked")
		private void LoadAuthor() {

			
			ListView<String> list =ListViewAuthor;
			Iterator itr=SystemController.AuthorList.iterator();
			int itemelement=0; 
			ObservableList<String>  items =FXCollections.observableArrayList ();
			while(itemelement<SystemController.AuthorList.size()){
				String item="First Name: "+SystemController.AuthorList.get(itemelement).getFirstName()+
						"Last Name: "+SystemController.AuthorList.get(itemelement).getLastName()+
						"City: "+SystemController.AuthorList.get(itemelement).getAddress().getCity()+ 
						"State: "+SystemController.AuthorList.get(itemelement).getAddress().getState()+
						"Zip: "+SystemController.AuthorList.get(itemelement).getAddress().getZip();
				itemelement++;
				items.add(item);
			}
			list.setItems(items);
			
//					TableView<String> table =TableViewAuthor;
//					ObservableList<String> items =FXCollections.observableArrayList (
//					SystemController.AuthorList.get(0).getFirstName(),
//					SystemController.AuthorList.get(0).getLastName(),
//					SystemController.AuthorList.get(0).getAddress().getCity(), 
//					SystemController.AuthorList.get(0).getAddress().getState(),
//					SystemController.AuthorList.get(0).getAddress().getZip()
//					);
//					table.setItems(items);
		}
	    @FXML protected void AddAuthorButtonAction(ActionEvent event) {
	    	
	    	try{
	    		 // Author author =new Author("Subash", "Niroula", "copy", new Address("100N", "iowa", "Faifield", "52557"),"SST");
	    		
	    	SystemController.AuthorList.add(new Author(FirstNameText.getText(), LastNameText.getText(), AuthorCredentialText.getText(), new Address(AuthorAddressText.getText(), AuthorZipText.getText(), AuthorCityText.getText() , AuthorZipText.getText()),AuthorCredentialText.getText()));
	          
	    		
	     	 //  Parent both = FXMLLoader.load(getClass().getResource("..\\View\\AddNewBook.fxml"));
	 	     //  SystemController.stageArea.setScene(new Scene(both));
	    
	    	  stageArea.setScene(previousScene);	    
	 	      SystemController.stageArea.show();
	 		
	       }
	         catch(Exception e){
	         	 e.printStackTrace();
	         }
	        
	    }
	    @FXML protected void AddAuthorViewButtonAction(ActionEvent event) {
	    	SystemController sc=new SystemController();
	        try{
	           previousScene=stageArea.getScene();	
	     	   Parent Current = FXMLLoader.load(getClass().getResource("..\\View\\AddAuthor.fxml"));
	     	  SplitPane splitPane1 = new SplitPane();	              
              splitPane1.getItems().addAll(ParentScreen, Current);
              Scene scene = new Scene(splitPane1,700,450);	             
              stageArea.setScene(scene);
	           
	          }
	            catch(Exception e){
	            	e.printStackTrace();
	            }
	    }
	     @FXML protected void ExitSystemHandler(ActionEvent event) {
	    	 Parent current = null;
			try {
				  current = FXMLLoader.load(getClass().getResource("..\\View\\Login.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stageArea.setTitle("Library Management System");
			Scene scene=new Scene(current,700,450);
			scene.getStylesheets().addAll(this.getClass().getResource("..\\Style\\Style.css").toExternalForm());
			stageArea.setScene(scene);
			stageArea.show();
	     }
	    
	    @FXML private TextField MaxCharFieldBook ;
	    @FXML private TextField isBNFieldBook;
	    @FXML private TextField titleFieldBook;
	    @FXML protected void AddBookButtonAction(ActionEvent event)  {
	        SystemController sc=new SystemController();
	        try {
	        	
//	        	System.out.println(isBNFieldBook.getText());
//	        	System.out.println(titleFieldBook.getText());
	        	
				sc.addBook(isBNFieldBook.getText(), titleFieldBook.getText(), Integer.parseInt(MaxCharFieldBook.getText()), SystemController.AuthorList);
				SendMessage("Book Is Successfully Uploaded");
			} catch (NumberFormatException | LibrarySystemException e) {
				// TODO Auto-generated catch block
				SendMessage("SomeThing is mistake while adding book");
			}

	    }
		private void SendMessage(String message) {
			Alert alert=new Alert(AlertType.INFORMATION);
			 alert.setContentText(message);
			 alert.show();
		}
		
		
		@FXML	protected void handleCheckoutBookViewLoad(ActionEvent event)  {
			Parent Current = null;
			try {
				Current = FXMLLoader.load(getClass().getResource("..\\View\\Checkout.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SplitPane splitPane1 = new SplitPane();	              
            splitPane1.getItems().addAll(ParentScreen, Current);
            Scene scene = new Scene(splitPane1,700,450);	             
            stageArea.setScene(scene);
		}
		
		@FXML	private TextField memIdText;
		@FXML	private TextField isbnText;
		@FXML 	private Text actiontErrorMessages;
		@FXML private ListView<String> ListViewCheckOutBook;
		LibraryMember searchedlibraryMem;
		Book searchedBookMy;
		
		  
	@FXML	protected void handleCheckoutBookButtonAction(ActionEvent event)  {
				DataAccessFacade dataAccess = new DataAccessFacade();
				try{
					
					StringBuilder sb = new StringBuilder();
					
					if(memIdText.getText().isEmpty() || isbnText.getText().isEmpty()){
						sb.append("Both fields are required\n");
					} else {
						searchedlibraryMem = dataAccess.searchMember(memIdText.getText());
						searchedBookMy = dataAccess.searchBook(isbnText.getText());
					
						if(searchedlibraryMem == null ){
							sb.append("Member not found\n");
						} 
						
						else if(searchedBookMy == null){
							sb.append("Book not found\n");
						} else {
							if(searchedBookMy.isAvailable()){
								BookCopy nextAvailCopy = searchedBookMy.getNextAvailableCopy();
								int maxCheckoutLen = searchedBookMy.getMaxCheckoutLength();
								
								Date today = new Date();
				
								Calendar c = Calendar.getInstance();
								c.setTime(new Date()); // Now use today date.
								c.add(Calendar.DATE, maxCheckoutLen); // Adding 21 days
								Date output = c.getTime();
							
								CheckOutRecord chkRec = searchedlibraryMem.checkout(nextAvailCopy, today, output,searchedlibraryMem.getCheckoutrecord());
								dataAccess.saveNewMember(searchedlibraryMem);
								dataAccess.saveNewBook(searchedBookMy);	
								
								
								DataAccess da = new DataAccessFacade();
								HashMap<String, LibraryMember> map = da.readMemberMap();
								
								if (map.containsKey(searchedlibraryMem.getMember_ID())) {
									
									ListView<String> list =ListViewCheckOutBook;
//									Iterator itr=map.keySet().iterator();
									ObservableList<String>  items =FXCollections.observableArrayList ();
//									while(itr.hasNext()){
		//								LibraryMember libraryMember=map.get(itr.next());
										
										ArrayList<CheckoutRecordEntry> checkoutRecordEntry =chkRec.getChkoutRecEntry();
										for(CheckoutRecordEntry entry : checkoutRecordEntry)
										{
										String item ="Memeber Name: "+searchedlibraryMem.getFirstName()+' '+searchedlibraryMem.getFirstName()+
												"BookISBN: "+entry.getBkCopy().getBook().getIsbn()+
												"Book Title"+entry.getBkCopy().getBook().getTitle()+
												"Checkout Date: "+entry.getCheckoutDate()+ 
												"Due Date: "+entry.getDueDate();
										
										items.add(item);
										}
									
										list.setItems(items);
								}
//								System.out.println(map.values());
								
//								for (Object value : map.values()) {
//									LibraryMember val = (LibraryMember) value;
//									val.getCheckoutrecord();
//								}
														
//								Parent Current = FXMLLoader.load(getClass().getResource("..\\View\\CheckOutTableView.fxml"));
//								SplitPane splitPane1 = new SplitPane();	              
//					            splitPane1.getItems().addAll(ParentScreen, Current);
//					            Scene scene = new Scene(splitPane1,700,450);	             
//					            stageArea.setScene(scene);

							} else {
								sb.append("Book copy is not available");
							}
						}			
					}				
					actiontErrorMessages.setText(sb.toString());
		    	}
		    	catch(Exception e){
		    		e.printStackTrace();
		    	}	
		 }
		
		
}
