package memberapp.resources;

import javafx.collections.* ;

import java.io.File ;
import java.sql.* ;
import java.time.LocalDate ;
import java.util.Scanner;

import memberapp.model.* ;


public class DataBaseController
{
    private final ObservableList<Book> bookList = FXCollections.observableArrayList() ;
    private final ObservableList<BookRecord> bookRecordList = FXCollections.observableArrayList() ;

    private final Connection connection ;
    private Statement statement ;


    public DataBaseController() throws Exception
    {
        String dbName = "dbLibrary_2" ;
        String url = "jdbc:mysql://localhost:3306/" ;


        Class.forName("com.mysql.cj.jdbc.Driver") ;
        connection = DriverManager.getConnection(url.concat(dbName), "root", "") ;
    }




    public Account signInToAcct(String accountType, String username, String password)
    {
        Account acct = null ;
        String query = "", typeOfID = "" ;

        if(username.equals("NULL") || password.equals("NULL"))  return null ;

        if(accountType.equalsIgnoreCase("admin"))
        {
            query = "SELECT * FROM tbl_admins WHERE Username = '" + username + "' ;" ;
            typeOfID = "AdminID" ;
        }
        else if(accountType.equalsIgnoreCase("librarian"))
        {
            query = "SELECT * FROM tbl_librarians WHERE Username = '" + username + "' ;" ;
            typeOfID = "LibrarianID" ;
        }
        else if(accountType.equalsIgnoreCase("member"))
        {
            query = "SELECT * FROM tbl_members WHERE Username = '" + username + "' ;" ;
            typeOfID = "MemberID" ;
        }

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;

            if(resultSet.next())
            {
                String storedPswrd = resultSet.getString("Password") ;
                String storedUsername = resultSet.getString("Username") ;

                if(storedPswrd.equals(password) && storedUsername.equals(username))
                {
                    String acctID = resultSet.getString(typeOfID) ;
                    acct = this.getAccount(acctID, accountType) ;
                }
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.loginToAcct() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return acct ;
    }

    public Boolean addAccount(Account account)
    {
        String query = "";

        if(account.getAcctType().equalsIgnoreCase("member"))
            query = "INSERT INTO tbl_members SET MemberID = \"" + account.getId() + "\", Status = 'ACTIVE', FirstName = \"" + account.getFirstName() + "\", LastName = \"" + account.getLastName() + "\", Username = \"" + account.getUsername() + "\", Password = \"" + account.getPassword() + "\", DateOfBirth = \"" + account.getDateOfBirth() + "\", Email = \"" + account.getEmail() + "\", Phone = \"" + account.getPhone() + "\", Address = \"" + account.getAddress() + "\", DateCreated = '" + account.getDateCreated() + "';" ;
        else if(account.getAcctType().equalsIgnoreCase("librarian"))
            query = "INSERT INTO tbl_librarians SET LibrarianID  = '" + account.getId() + "', FirstName = \"" + account.getFirstName() + "\", LastName = \"" + account.getLastName() + "\", Username = \"" + account.getUsername() + "\", Password = \"" + account.getPassword() + "\", DateOfBirth = '" + account.getDateOfBirth() + "', Email = '" + account.getEmail() + "', Phone = '" + account.getPhone() + "', NIC = '" + account.getNicNumber() + "', Address = '" + account.getAddress() + "', DateCreated = '" + account.getDateCreated() + "';" ;


        try
        {
            statement = connection.createStatement() ;
            statement.execute(query) ;
            statement.close() ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.addAccount() -> \n" + e) ;
        }

        return false ;
    }


    public Boolean addBook(Book book)
    {
        String query = "INSERT INTO tbl_books SET BookID = \"" + book.getBookID() + "\", Title = \"" + book.getTitle() + "\", Author = \"" + book.getAuthor() + "\", ISBN = \"" + book.getIsbn() + "\", Publisher = \"" + book.getPublisher() + "\", Category = \"" + book.getPublisher() + "\", TotalCopies = " + book.getTotalCopies() + ", CopiesAvailable = " + book.getCopiesAvailable() + " ;" ;

        try
        {
            statement = connection.createStatement() ;
            statement.execute(query) ;
            statement.close() ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.addBook() -> \n" + e) ;
        }

        return false ;
    }

    public Boolean updateAcct(Account account)
    {
        String query = "" ;

        if(account.getAcctType().equalsIgnoreCase("member"))
            query = "UPDATE tbl_members SET FirstName = \"" + account.getFirstName() + "\", LastName = \"" + account.getLastName() + "\", Username = \"" + account.getUsername() + "\", Password = \"" + account.getPassword() + "\",DateOfBirth = \"" + account.getDateOfBirth() + "\", Email = \"" + account.getEmail() + "\", Phone = \"" + account.getPhone() + "\", Address = \"" + account.getAddress() + "\" WHERE MemberID = \"" + account.getId() + "\";" ;
        else if(account.getAcctType().equalsIgnoreCase("librarian"))
            query = "UPDATE tbl_librarians SET FirstName = \"" + account.getFirstName() + "\", LastName = \"" + account.getLastName() + "\", Username = \"" + account.getUsername() + "\", Password = \"" + account.getPassword() + "\", Email = \"" + account.getEmail() + "\", Phone = \"" + account.getPhone() + "\", Address = \"" + account.getAddress() + "\", NIC = \"" + account.getNicNumber() + "\" WHERE LibrarianID = \"" + account.getId() + "\";" ;
        else if(account.getAcctType().equalsIgnoreCase("admin"))
            query = "UPDATE tbl_admins SET FirstName = \"" + account.getFirstName() + "\", LastName = \"" + account.getLastName() + "\", Username = \"" + account.getUsername() + "\", Password = \"" + account.getPassword() + "\", Email = \"" + account.getEmail() + "\", Phone = \"" + account.getPhone() + "\", Address = \"" + account.getAddress() + "\", NIC = \"" + account.getNicNumber() + "\" WHERE AdminID = \"" + account.getId() + "\";";


        try
        {
            statement = connection.createStatement() ;
            statement.execute(query) ;
            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.updateAcct() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return false ;
    }


    public Boolean updateBook(String prevID, Book book)
    {
        String query = "UPDATE tbl_books SET BookID = \"" + book.getBookID() + "\"" + ", Title = \"" + book.getTitle() + "\", Author = \"" + book.getAuthor() + "\", ISBN = \"" + book.getIsbn() + "\", Publisher = \"" + book.getPublisher() + "\", Category = \"" + book.getPublisher() + "\", TotalCopies = " + book.getTotalCopies() + ", CopiesAvailable = " + book.getCopiesAvailable() + " WHERE BookID = \"" + book.getBookID() + "\" WHERE BookID = '" + prevID + "' ;" ;

        try
        {
            statement = connection.createStatement() ;
            return statement.execute(query) ;
        }
        catch (Exception e) {
            System.out.println("\nExceptions in DataBaseController.updateBook() -> \n" + e) ;
        }

        return false ;
    }


    public Boolean reserveBook(BookRecord bookRecord)
    {
        int copiesCount = Integer.parseInt(bookRecord.getBook().getCopiesAvailable()) - 1 ;
        String ref_num = this.generateRefNum() ;
        System.out.println(bookRecord.getDateOfCancellation());
        String query = "INSERT INTO tbl_bookrecords SET RefNum = \"" + ref_num + "\", AccountID = \"" + bookRecord.getAccountID() + "\", AccountType = \"" + bookRecord.getAccountType() + "\", BookID = \"" + bookRecord.getBook().getBookID() + "\", ReservedDate = '" + bookRecord.getDateOfReservation() + "';" ;
        String query2 = "UPDATE tbl_books SET CopiesAvailable = '" + copiesCount + "' WHERE BookID = '" + bookRecord.getBook().getBookID() + "' ;" ;

        try
        {
            statement = connection.createStatement() ;
            statement.execute(query) ;
            statement.execute(query2) ;

            bookRecord.getBook().setCopiesAvailable(Integer.toString(copiesCount)) ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nException in DatabaseController.reserveBook() -> \n" + e) ;
        }

        return false ;
    }

    public Boolean issueBook(BookRecord bookRecord)
    {
        String date = LocalDate.now().toString() ;
        String dueDate = LocalDate.now().plusMonths(1).toString() ;
        String query1 = "UPDATE tbl_bookrecords SET IssuedDate = '" + date + "', DueDate = '" + dueDate + "' WHERE RefNum = '" + bookRecord.getRefNum() + "' ;" ;

        try
        {
            statement = connection.createStatement() ;

            statement.execute(query1) ;

            bookRecord.setDateOfIssue(date) ;
            bookRecord.setDateDue(dueDate) ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nException in DatabaseController.issueBook() -> \n" + e) ;
        }

        return false ;
    }

    public Boolean sendRequestToReturn(BookRecord bookRecord)
    {
        String date = LocalDate.now().toString() ;
        String query = "UPDATE tbl_bookrecords SET ReturnSentOnDate = '" + date + "' WHERE RefNum = '" + bookRecord.getRefNum() + "' ;" ;

        try
        {
            statement = connection.createStatement() ;

            statement.execute(query) ;

            bookRecord.setDateOfRequestToReturn(date) ;

            return  true ;
        }
        catch (Exception e)
        {
            System.out.println("\nException in DatabaseController.sendRequestToReturn() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return false ;
    }

    public Boolean returnBook(BookRecord bookRecord, Account account)
    {
        int copiesCount = Integer.parseInt(bookRecord.getBook().getCopiesAvailable()) + 1 ;

        String date = LocalDate.now().toString() ;
        String query1 = "UPDATE tbl_bookrecords SET ReturnedDate = '" + date + "' WHERE RefNum = '" + bookRecord.getRefNum() + "' ;" ;
        String query2 = "UPDATE tbl_books SET CopiesAvailable = '" + copiesCount + "' WHERE BookID = '" + bookRecord.getBookID() + "'; " ;


        try
        {
            statement = connection.createStatement() ;

            statement.execute(query1) ;
            statement.execute(query2) ;

            bookRecord.setDateOfReturn(date) ;
            bookRecord.getBook().setCopiesAvailable(Integer.toString(copiesCount)) ;

            account.setNumOfBooksBorrowed(Integer.toString(Integer.parseInt(account.getNumOfBooksBorrowed()) - 1)) ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.returnBook() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return false ;
    }
    public Boolean cancelReservation(BookRecord bookRecord)
    {
        String query = "UPDATE tbl_bookrecords SET CancellationDate = \"" + LocalDate.now() + "\";" ;

        try
        {
            statement = connection.createStatement() ;

            statement.execute(query) ;

            bookRecord.setDateOfCancellation(LocalDate.now().toString()) ;

            return true ;
        }
        catch (Exception e)
        {
            System.out.println("\nException in DataBaseController.cancelReservation() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return false ;
    }

    public Boolean checkUsername(String accountType, String id, String usernameToCompare)
    {
        boolean unique = true ;
        String query = "" ;

        if(accountType.equalsIgnoreCase("member"))
            query = "SELECT AdminID, Username FROM tbl_admins WHERE Username = \"" + usernameToCompare + "\" AND AdminID != \"" + id + "\" ;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;

            if(resultSet.next()) unique = false ;

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.checkUsername() -> \n" + e) ;
            e.printStackTrace() ;
        }
        return unique ;
    }

    public Boolean checkBookID(String bookID)
    {
        boolean found = false ;
        String query = "SELECT * FROM tbl_books WHERE BookID = '" + bookID + "';" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;

            if(resultSet.next())    found = true ;

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e) {
            System.out.println("\nExceptions in DataBaseController.checkBookID() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return found ;
    }

    private Boolean checkForRefNum(String refNumToCheck)
    {
        boolean found = false ;
        String query = "SELECT * FROM tbl_bookrecords WHERE RefNum = '" + refNumToCheck + "' ;" ;
        try
        {
            statement = connection.createStatement() ;
            ResultSet resultSet = statement.executeQuery(query) ;

            if(resultSet.next())    found = true ;

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.checkForRefNum() -> \n" + e) ;
        }

        return found ;
    }

    public Boolean checkIfReserved(String personID, Book book)
    {
        boolean found = false ;
        String query = "SELECT * FROM tbl_bookrecords WHERE BookID = '" + book.getBookID() + "' AND AccountID = '" + personID + "' AND ReservedDate IS NOT NULL;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            found = resultSet.next() ;

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.checkIfReserved() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return found ;
    }

    public Boolean checkIfIssued(String personID, Book book)
    {
        boolean found = false ;
        String query = "SELECT * FROM tbl_bookrecords WHERE BookID = '" + book.getBookID() + "' AND AccountID = '" + personID + "' AND IssuedDate IS NOT NULL;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            found = resultSet.next() ;

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.checkIfIssued() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return found ;
    }

    public Account getAccount(String id, String type)
    {
        String query = "" ;
        Account acct = null ;

        type = type.toUpperCase() ;

        if(type.equalsIgnoreCase("member"))
            query = "SELECT * FROM tbl_members WHERE MemberID = \"" + id + "\" ;" ;
        else if(type.equalsIgnoreCase("librarian"))
            query = "SELECT * FROM tbl_librarians WHERE LibrarianID = \"" + id + "\" ;" ;
        else if(type.equalsIgnoreCase("admin"))
            query = "SELECT * FROM tbl_admins WHERE AdminID = \"" + id + "\" ;" ;

        try
        {
            statement = connection.createStatement() ;

            if(!query.equals(""))
            {
                ResultSet resultSet = statement.executeQuery(query);

                if(resultSet.next())
                {
                    acct = new Account(type, id, resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("DateCreated"));
                    acct.setEmail(resultSet.getString("Email"));
                    acct.setPhone(resultSet.getString("Phone"));
                    acct.setDateOfBirth(resultSet.getString("DateOfBirth")) ;
                    acct.setAddress(resultSet.getString("Address"));
                    acct.setDateCreated(resultSet.getString("DateCreated"));
                    acct.setAcctType(type) ;

                    if (type.equalsIgnoreCase("member"))
                    {
                        acct.setDateOfBirth(resultSet.getString("DateOfBirth"));
                        acct.setNumOfBooksBorrowed(getOnlyIssuedBookCount(id));
                    }
                    else
                    {
                        acct.setNicNumber(resultSet.getString("NIC"));
                    }
                }

                resultSet.close() ;
                statement.close() ;
            }

        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.getAccount() -> \n" + e + "\n" + e.getMessage()) ;
            e.printStackTrace() ;
        }

        return acct ;
    }

    public String getFromAccount(String accountID, String accountType, String fieldSought)
    {
        String data = null ;
        String query = "" ;

        if(accountType.equalsIgnoreCase("member"))
            query = "SELECT " + fieldSought + " FROM tbl_members WHERE MemberID = '" + accountID + "';" ;
        else if(accountType.equalsIgnoreCase("librarian"))
            query = "SELECT " + fieldSought + " FROM tbl_librarians WHERE LibrarianID = '" + accountID + "';" ;


        try
        {
            Statement statement = connection.createStatement() ;
            ResultSet resultSet = statement.executeQuery(query) ;

            if(resultSet.next())    data = resultSet.getString(fieldSought) ;

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.getFromAccount() -> " + e.getMessage()) ;
            e.printStackTrace() ;
        }

        return data ;
    }
    public ObservableList<Account> getFromAllAccounts(String accountType, String fieldSought, String val)
    {
        ObservableList<Account> list = FXCollections.observableArrayList() ;
        String query = "" ;
        Account acct = null ;

        if(accountType.equalsIgnoreCase("member"))
            query = "SELECT * FROM tbl_members WHERE " + fieldSought + " LIKE \"" + val + "%\" ;" ;
        else if(accountType.equalsIgnoreCase("librarian"))
            query = "SELECT * FROM tbl_librarians WHERE " + fieldSought + " LIKE \"" + val + "%\" ;" ;

        try
        {
            Statement statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;

            while(resultSet.next())
            {
                if(accountType.equalsIgnoreCase("member"))
                {
                    acct = new Account("MEMBER", resultSet.getString("MemberID"), resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("DateCreated"));
                    acct.setNumOfBooksBorrowed(getOnlyIssuedBookCount(resultSet.getString("MemberID"))) ;
                    acct.setDateOfBirth(resultSet.getString("DateOfBirth")) ;
                }
                else if(accountType.equalsIgnoreCase("librarian"))
                {
                    acct = new Account("LIBRARIAN", resultSet.getString("LibrarianID"), resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("DateCreated"));
                    acct.setNicNumber(resultSet.getString("NIC")) ;
                    acct.setDateOfBirth(resultSet.getString("DateOfBirth")) ;
                }

                acct.setEmail(resultSet.getString("Email"));
                acct.setPhone(resultSet.getString("Phone"));
                acct.setAddress(resultSet.getString("Address"));

                list.add(acct) ;
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
        }

        return list ;
    }

    public ObservableList<Account> getAllMemberAccounts()
    {
        ObservableList<Account> list = FXCollections.observableArrayList() ;
        String query ;
        Account account ;

        query = "SELECT * FROM tbl_members;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                account = new Account("MEMBER", resultSet.getString("MemberID"), resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("DateCreated"));
                account.setEmail(resultSet.getString("Email"));
                account.setPhone(resultSet.getString("Phone"));
                account.setAddress(resultSet.getString("Address"));
                account.setDateOfBirth(resultSet.getString("DateOfBirth")) ;
                account.setNumOfBooksBorrowed(getOnlyIssuedBookCount(resultSet.getString("MemberID"))) ;

                list.add(account) ;
            }

            statement.close() ;
            resultSet.close() ;
        }
        catch (Exception e)
        {
            System.out.println("Exceptions in DataBaseController.getAllMemberAccounts() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return  list ;
    }

    public ObservableList<Account> getAllLibrarianAccounts()
    {
        ObservableList<Account> list = FXCollections.observableArrayList() ;
        String query ;
        Account account ;

        query = "SELECT * FROM tbl_librarians;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                account = new Account("LIBRARIAN", resultSet.getString("LibrarianID"), resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("DateCreated"));
                account.setEmail(resultSet.getString("Email"));
                account.setPhone(resultSet.getString("Phone"));
                account.setAddress(resultSet.getString("Address")) ;
                account.setDateOfBirth(resultSet.getString("DateOfBirth")) ;
                account.setNicNumber(resultSet.getString("NIC")) ;

                list.add(account) ;
            }

            statement.close() ;
            resultSet.close() ;
        }
        catch (Exception e)
        {
            System.out.println("Exceptions in DataBaseController.getAllLibrarianAccounts() -> \n" + e) ;
            e.printStackTrace() ;
        }

        return  list ;
    }

    public ObservableList<Book> getAllBooks()
    {
        String query, bookID, title, author, isbn, publisher, category, copiesAvailable, totalCopies ;
        Book book ;

        bookList.clear() ;
        query = "SELECT * FROM tbl_books;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                bookID = resultSet.getString("BookID") ;
                title = resultSet.getString("Title") ;
                author = resultSet.getString("Author") ;
                isbn = resultSet.getString("ISBN") ;
                publisher = resultSet.getString("Publisher") ;
                category = resultSet.getString("Category") ;
                totalCopies = resultSet.getString("TotalCopies") ;
                copiesAvailable = resultSet.getString("CopiesAvailable") ;

                book = new Book(bookID, title, author, isbn, publisher, category, totalCopies, copiesAvailable) ;

                bookList.add(book) ;
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nException at DataBaseController.getAllBooks() -> \n" + e + " -> \n" + e.getLocalizedMessage()) ;
            e.printStackTrace() ;
        }

        return bookList ;
    }

    public Book getBookFromAllBooks(String fieldName, String val)
    {
        Book book = null ;
        String query, bookID, title, author, isbn, publisher, category, copiesAvailable, totalCopies ;

        query = "SELECT * FROM tbl_Books WHERE " + fieldName + " = \"" + val + "\";" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                bookID = resultSet.getString("BookID") ;
                title = resultSet.getString("Title") ;
                author = resultSet.getString("Author") ;
                isbn = resultSet.getString("ISBN") ;
                publisher = resultSet.getString("Publisher") ;
                category = resultSet.getString("Category") ;
                totalCopies = resultSet.getString("TotalCopies") ;
                copiesAvailable = resultSet.getString("CopiesAvailable") ;

                book = new Book(bookID, title, author, isbn, publisher, category, totalCopies, copiesAvailable) ;
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.getBook() -> " + e) ;
            e.printStackTrace();
        }

        return book ;
    }
    public ObservableList<Book> getFromBooks(String fieldName, String val)
    {
        Book book ;
        String query, bookID, title, author, isbn, publisher, category, copiesAvailable, totalCopies ;

        bookList.clear() ;

        query = "SELECT * FROM tbl_books WHERE " + fieldName + " LIKE \"%" + val + "%\" ;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                bookID = resultSet.getString("BookID") ;
                title = resultSet.getString("Title") ;
                author = resultSet.getString("Author") ;
                isbn = resultSet.getString("ISBN") ;
                publisher = resultSet.getString("Publisher") ;
                category = resultSet.getString("Category") ;
                totalCopies = resultSet.getString("TotalCopies") ;
                copiesAvailable = resultSet.getString("CopiesAvailable") ;

                book = new Book(bookID, title, author, isbn, publisher, category, totalCopies, copiesAvailable) ;

                bookList.add(book) ;
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.getFromBooks() -> \n" + e) ;
            e.printStackTrace();
        }
        return bookList ;
    }

    public ObservableList<BookRecord> getFromBookRecords(String fieldName, String val)
    {
        Book book ;
        String query1, query2, refNum, bookID, acctID, acctType, reservedDate, cancellationDate, issuedDate, dueDate, returnSentOnDate, returnedDate ;
        ObservableList<BookRecord> list = FXCollections.observableArrayList() ;

        query1 = "SELECT * FROM tbl_bookrecords WHERE " + fieldName + " LIKE \"%" + val + "%\" ;" ;
        query2 = "SELECT * FROM tbl_bookrecords ;" ;

        try
        {
            ResultSet resultSet = null ;
            statement = connection.createStatement() ;

            if(fieldName.equalsIgnoreCase("title") || fieldName.equalsIgnoreCase("author"))
                resultSet = statement.executeQuery(query2) ;
            else
                resultSet = statement.executeQuery(query1) ;

            while(resultSet.next())
            {
                refNum = resultSet.getString("RefNum") ;
                bookID = resultSet.getString("BookID") ;
                acctID = resultSet.getString("AccountID") ;
                acctType = resultSet.getString("AccountType") ;
                reservedDate = resultSet.getString("ReservedDate") ;
                cancellationDate = resultSet.getString("CancellationDate") ;
                issuedDate = resultSet.getString("IssuedDate") ;
                dueDate = resultSet.getString("DueDate"); ;
                returnSentOnDate = resultSet.getString("ReturnSentOnDate") ;
                returnedDate = resultSet.getString("ReturnedDate") ;

                book = getBookFromAllBooks("BookID", bookID) ;
                BookRecord bookRecord = new BookRecord( book, refNum, acctID, acctType, reservedDate, cancellationDate, issuedDate, returnSentOnDate, returnedDate, dueDate) ;


                if(fieldName.equalsIgnoreCase("title"))
                {
                    if(book.getTitle().contains(val.toUpperCase()))
                        list.add(bookRecord);
                }
                else if(fieldName.equalsIgnoreCase("author"))
                {
                    if (book.getAuthor().contains(val.toUpperCase()))
                        list.add(bookRecord);
                }
                else
                {
                    list.add(bookRecord);
                }
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch (Exception e)
        {
            System.out.println("\nExceptions in DataBaseController.getFromBookRecords() -> \n" + e) ;
            e.printStackTrace();
        }
        return list ;
    }

    public String getOnlyIssuedBookCount(String id)
    {
        String numCount = null ;
        String query ;

        query = "SELECT COUNT(BookID) AS BookCount FROM tbl_bookrecords WHERE (AccountID = \"" + id + "\" AND ReturnedDate IS NULL);" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            if(resultSet.next())
                numCount = resultSet.getString("BookCount");

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nException in DataBaseController.getOnlyIssuedBookCount() ->\n" + e) ;
            e.printStackTrace();
        }

        return numCount ;
    }

    public ObservableList<BookRecord> getAllBookRecords()
    {
        String query, refNum, bookID, accountID, accountType, reservedDate, cancellationDate, issuedDate,  dueDate, returnSentOnDate, returnedOnDate ;
        Book book ;
        BookRecord bookRecord ;
        ObservableList<BookRecord> tempList = FXCollections.observableArrayList() ;

        query = "SELECT * FROM tbl_bookrecords ORDER BY RefNum ;" ;

        try
        {
            statement = connection.createStatement() ;

            ResultSet resultSet = statement.executeQuery(query) ;
            while(resultSet.next())
            {
                refNum = resultSet.getString("RefNum") ;
                bookID = resultSet.getString("BookID") ;
                accountID = resultSet.getString("AccountID") ;
                accountType = resultSet.getString("AccountType") ;
                reservedDate = resultSet.getString("ReservedDate") ;
                cancellationDate = resultSet.getString("CancellationDate") ;
                issuedDate = resultSet.getString("IssuedDate") ;
                dueDate = resultSet.getString("DueDate") ;
                returnSentOnDate = resultSet.getString("ReturnSentOnDate") ;
                returnedOnDate = resultSet.getString("ReturnedDate") ;

                book = this.getBookFromAllBooks("BookID", bookID) ;     // get a book object with the book id specified in the parameters
                bookRecord = new BookRecord(book, refNum, accountID, accountType, reservedDate, cancellationDate, issuedDate, dueDate, returnSentOnDate, returnedOnDate) ;

                tempList.add(bookRecord) ;
            }

            resultSet.close() ;
            statement.close() ;
        }
        catch(Exception e)
        {
            System.out.println("\nException in DataBaseController.getAllBookRecords() ->\n" + e) ;
            e.printStackTrace() ;
        }

        return tempList ;
    }

    public ObservableList<BookRecord> getAllReservedBooks()
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getDateOfReservation() != null && bookRecord.getDateOfIssue() == null && bookRecord.getDateOfCancellation() == null)
                bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    public ObservableList<BookRecord> getAllIssuedBooks()
    {
        ObservableList<BookRecord> tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getDateOfIssue() != null && bookRecord.getDateOfRequestToReturn() == null)     bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    public ObservableList<BookRecord> getAllBooksRequestedToReturn()
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getDateOfRequestToReturn() != null && bookRecord.getDateOfReturn() == null)   bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    public ObservableList<BookRecord> getAllReturnedBooks()
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getDateOfReturn() != null)   bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }
    public ObservableList<BookRecord> getReservedBooks(String id)
    {
        ObservableList<BookRecord> tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord: tempList)
        {
            if(bookRecord.getAccountID().equals(id) && bookRecord.getDateOfIssue() == null)
                bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }


    public ObservableList<BookRecord> getIssuedBooks(String id)
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getAccountID().equals(id) && bookRecord.getDateOfIssue() != null)
                bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    public ObservableList<BookRecord> getReturnedBooks(String id)
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getAccountID().equals(id) && bookRecord.getDateOfReturn() != null)
                bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    public ObservableList<BookRecord> getBooksRequestedToReturn(String id)
    {
        ObservableList<BookRecord> tempList ;

        tempList = this.getAllBookRecords() ;

        bookRecordList.clear() ;

        for(BookRecord bookRecord : tempList)
        {
            if(bookRecord.getAccountID().equals(id) && bookRecord.getDateOfRequestToReturn() != null)
                bookRecordList.add(bookRecord) ;
        }

        return bookRecordList ;
    }

    private String generateRefNum()
    {
        String ref_num ;

        do
        {
            ref_num = generateRandomLtrs(4).concat(generateRandomNumbers(6)) ;
        }
        while(checkForRefNum(ref_num)) ;

        return ref_num ;
    }

    private String generateRandomLtrs(int length)
    {
        String random = "" ;
        int num, counter ;

        counter = 1 ;
        do
        {
            num = (int)(Math.random() * 91) ;

            if(num >= 65 && num <= 90)
            {
                random = random.concat(Character.toString((char)num)) ;
                counter++ ;
            }
        }
        while((num < 65 || num > 90) || counter <= length);

        return random ;
    }
    private String generateRandomNumbers(int length)
    {
        String randomNum = "" ;
        int num, index ;

        for(index = 0 ; index < length ; index++)
        {
            num = (int)(Math.random() * 10) ;
            randomNum = randomNum.concat(Integer.toString(num)) ;
        }

        return randomNum ;
    }
}
