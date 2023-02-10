package memberapp.view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.* ;
import javafx.scene.* ;
import javafx.scene.control.* ;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.* ;
import javafx.stage.Stage;

import memberapp.resources.DataBaseController;
import memberapp.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SignInPageController implements Initializable
{
    public Button signInBtn;
    public AnchorPane anchorPane, mainAnchorPane, centerAnchorPane ;
    public VBox mainVbox, vbox_1, vbox_2 ;
    public HBox hbox_1, hbox_2 ;
    public Label mainHeader, subHeader, usernameLabel, passwordLabel ;
    public TextField usernameField ;
    public PasswordField passwordField ;

    private DataBaseController dataBaseController ;

    private final MessageBox messageBox = new MessageBox() ;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            this.dataBaseController = new DataBaseController() ;
            anchorPane.setStyle("-fx-background-color: maroon") ;
            mainHeader.setId("header_style") ;
            mainHeader.setStyle("-fx-background-color: #233142");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;

            messageBox.show("Failed to connect to the database server." ,"ERROR") ;

            System.exit(0) ;
        }
    }


    public void buttonCLickedSignIn(ActionEvent e)
    {
        signIn(e);
    }


    public void keyPressedSignIn(KeyEvent e)
    {
        if(e.getCode() == KeyCode.ENTER)
        {
            signIn(e) ;
        }
    }


    private void signIn(Event event)
    {


        if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty())
        {




            Main.userAccount = dataBaseController.signInToAcct("MEMBER", usernameField.getText(), passwordField.getText()) ;

            if(Main.userAccount != null)
                loadHomePage(event) ;
            else
                messageBox.show("Invalid username or password!", "SIGN IN FAIL") ;
        }
        else
        {
            messageBox.show("Please enter your username and password.", "SIGN IN FAIL") ;
        }
    }


    private void loadHomePage(Event event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("HomeScreen_FXML.fxml"));

            Scene scene = new Scene(root) ;
            scene.getStylesheets().add("/memberapp/resources/css/HomePageStyleSheet.css") ;

            Stage stage = new Stage() ;
            stage.setTitle("M E M B E R   A P P L I C A T I O N") ;
            stage.getIcons().add(new Image("/memberapp/resources/images/books.png"));
            stage.setResizable(true) ;
            stage.setScene(scene) ;

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow() ;

            stage.show() ;
            window.close() ;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}