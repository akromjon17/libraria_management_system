package adminapp.view;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import adminapp.resources.DataBaseController;
import adminapp.Main;

import java.net.URL;
import java.util.ResourceBundle;


public class ChangePasswordController implements Initializable
{
    public AnchorPane root ;
    public HBox hbox1, hbox2, hbox3 ;
    public VBox vbox ;
    public Label header, instruction1, instruction2, instruction3, warningLbl ;
    public PasswordField currentPswrdField, newPswrdField1, newPswrdField2 ;
    public Button changeBtn ;


    private final MessageBox messageBox = new MessageBox() ;

    private DataBaseController dataBaseController ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            this.dataBaseController = new DataBaseController();
            warningLbl.setText("");

            currentPswrdField.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    changePassword();
                else
                    warningLbl.setText("");
            });

            newPswrdField1.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    changePassword();
                else
                    warningLbl.setText("");
            });

            newPswrdField2.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    changePassword();
                else
                    warningLbl.setText("");
            });

            header.setId("main_header_style");
            warningLbl.setId("warning_style");
            instruction1.setId("instruction_style");
            instruction2.setId("instruction_style");
            instruction3.setId("instruction_style");
            root.setStyle("-fx-background-color: maroon");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
        }

    }

    /**
     * This method first checks if all the fields are filled. Then, it will encrypt the text in 'currentPswrdField' using
     * the salt stored in 'Main.userAccount' and compare the result with the password stored in 'Main.userAccount'.
     * If the passwords are equal, then it'll verify the 2 new passwords.
     * After verification, the details for this account to be updated is called using the 'updateAccount' method in
     * the DataBaseController.
     * The operation is then checked if it's successful or not and a message is displayed.
     */
    public void changePassword()
    {
        String oldPassword, newPassword, oldSalt, newSalt ;

        if(currentPswrdField.getText().isBlank() || newPswrdField1.getText().isBlank() || newPswrdField2.getText().isBlank())
        {
            warningLbl.setText("All the fields must be filled.") ;
        }
        else
        {
            if(Main.userAccount.getPassword().equals(currentPswrdField.getText()))
            //if(Main.userAccount.getPassword().equals(currentPswrdField.getText()))
            {
                if(newPswrdField1.getText().equals(newPswrdField2.getText()))
                {
                    // keep a record of the old password and salt before making changes
                    oldPassword = Main.userAccount.getPassword() ;


                    newPassword = newPswrdField1.getText() ;


                    Main.userAccount.setPassword(newPassword) ;


                    if(dataBaseController.updateAcct(Main.userAccount))
                    {
                        messageBox.show("Password changed!", "SUCCESS") ;

                        currentPswrdField.setText("") ;
                        newPswrdField1.setText("") ;
                        newPswrdField2.setText("") ;

                        Stage stage = (Stage) changeBtn.getScene().getWindow() ;
                        stage.close() ;
                    }
                    else
                    {
                        // as something went wrong, reset the password and salt to the old ones
                        Main.userAccount.setPassword(oldPassword) ;


                        messageBox.show("Failed to change your password. Try again later.", "Failed") ;
                    }
                }
                else
                {
                    warningLbl.setText("The new passwords do not match.");
                }
            }
            else
            {
                warningLbl.setText("Current passwords do not match.");
            }
        }
    }
}
