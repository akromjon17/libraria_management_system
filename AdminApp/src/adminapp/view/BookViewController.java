package adminapp.view;

import adminapp.model.Book;
import adminapp.resources.DataBaseController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class BookViewController implements Initializable
{
    public AnchorPane root ;
    public Label bookIDLbl, titleLbl, authorLbl, isbnLbl, publisherLbl, categoryLbl, copiesAvailableLbl, totalCopiesLbl ;
    public TextField bookIDField, isbnField, publisherField, categoryField, copiesAvailableField, totalCopiesField ;
    public VBox vbox ;

DataBaseController dataBaseController=new DataBaseController();
MessageBox messageBox=new MessageBox();

    public BookViewController() throws Exception { }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        titleLbl.setText(BooksPageController.selectedBook.getTitle()) ;
        authorLbl.setText("By " + BooksPageController.selectedBook.getAuthor()) ;

        bookIDField.setText(BooksPageController.selectedBook.getBookID()) ;
        isbnField.setText(BooksPageController.selectedBook.getIsbn()) ;
        publisherField.setText(BooksPageController.selectedBook.getPublisher()) ;
        categoryField.setText(BooksPageController.selectedBook.getCategory()) ;
        copiesAvailableField.setText(BooksPageController.selectedBook.getCopiesAvailable()) ;
        totalCopiesField.setText(BooksPageController.selectedBook.getTotalCopies()) ;

        root.setId("root_style") ;
        titleLbl.setId("main_header_style") ;
        authorLbl.setId("header_style") ;
        bookIDLbl.setId("instruction_style") ;
        isbnLbl.setId("instruction_style") ;
        publisherLbl.setId("instruction_style") ;
        copiesAvailableLbl.setId("instruction_style") ;
        totalCopiesLbl.setId("instruction_style") ;
        categoryLbl.setId("instruction_style") ;
    }
    public void delete(){
        Book book = BooksPageController.selectedBook;

        if(dataBaseController.deleteBook(book))
            messageBox.show("The book was successfully deleted.", "SUCCESS") ;
        else
            messageBox.show("Failed to delete the book. Try again later. ", "ERROR") ;

    }
}
