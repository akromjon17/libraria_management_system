package adminapp.view;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import adminapp.model.Book;
import adminapp.resources.DataBaseController;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddNewBookController implements Initializable {
    public Pane root;
    public Label header;
    public TextField titleField, authorField, isbnField, publisherField, categoryField, totalCopiesField, copiesAvailableField;

    private DataBaseController dataBaseController;

    private final MessageBox messageBox = new MessageBox();
    private final ConfirmationBox confirmationBox = new ConfirmationBox();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.dataBaseController = new DataBaseController();
            header.setId("header_style");
            root.setStyle("-fx-background-color: maroon");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void create() {
        String id, title, copiesAvailable;


        if (titleField.getText().isBlank() || authorField.getText().isBlank() || copiesAvailableField.getText().isBlank() || isbnField.getText().isBlank()
                || totalCopiesField.getText().isBlank() || publisherField.getText().isBlank() || categoryField.getText().isBlank()) {
            messageBox.show("All the data must be entered!", "ERROR!");
        } else {
            id = generateID(titleField.getText(), copiesAvailableField.getText());
            title = titleField.getText();
            copiesAvailable = copiesAvailableField.getText();

            Book book = new Book(id, title, authorField.getText(), isbnField.getText(), publisherField.getText(), categoryField.getText(), totalCopiesField.getText(), copiesAvailable);
            book.setTitle(titleField.getText());
            ;
            book.setAuthor(authorField.getText());
            ;
            book.setIsbn(isbnField.getText());
            ;
            book.setCategory(categoryField.getText());
            ;
            book.setCopiesAvailable(copiesAvailableField.getText());
            ;
            book.setBookID(id);
            ;

            if (dataBaseController.addBook(book))
                messageBox.show("The book was successfully created.", "SUCCESS");
            else
                messageBox.show("Failed to create the book. Try again later.", "ERROR");
        }
    }

    private String generateID(String firstName, String lastName) {
        String num;
        String id = firstName.toUpperCase().substring(0, 1).concat(lastName.toUpperCase().substring(0, 1));

        id = id.concat(generateLetter());

        num = Integer.toString(generateRandomNum(111, 222));
        id = id.concat(num);
        return id;
    }

    private String generateLetter() {
        String ltrs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int n = random.nextInt(10);

        return ltrs.substring(n, n + 1);
    }


    private Integer generateRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }
}
