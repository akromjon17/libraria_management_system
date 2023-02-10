package adminapp.view ;

import javafx.stage.* ;
import javafx.scene.* ;
import javafx.scene.layout.* ;
import javafx.scene.control.* ;
import javafx.geometry.* ;

public class ConfirmationBox
{
    private Stage primaryStage;
    private boolean btnYesClicked ;

    public boolean show(String message, String title, String textYes, String textNo)
    {
        btnYesClicked = false ;

        primaryStage = new Stage() ;
        primaryStage.setTitle(title) ;

        Label lbl = new Label() ;
        lbl.setText(message) ;
        lbl.setMaxWidth(Double.MAX_VALUE) ;
        lbl.setPadding(new Insets(5, 15, 5, 15)) ;

        Button yesBtn = new Button() ;
        yesBtn.setText(textYes) ;
        yesBtn.setMinWidth(50) ;
        yesBtn.setPrefHeight(20) ;
        yesBtn.setOnAction(e ->
        {
            btnYesClicked = true ;
            primaryStage.close() ;
        }) ;

        Button noBtn = new Button() ;
        noBtn.setText(textNo) ;
        noBtn.setMinWidth(50) ;
        noBtn.setPrefHeight(20) ;
        noBtn.setOnAction(e ->
        {
            btnYesClicked = false ;
            primaryStage.close() ;
        }) ;

        HBox paneBtn = new HBox(20) ;
        paneBtn.getChildren().addAll(yesBtn, noBtn) ;
        paneBtn.setAlignment(Pos.CENTER) ;

        VBox vbox = new VBox(20) ;
        vbox.getChildren().addAll(lbl, paneBtn) ;
        vbox.setAlignment(Pos.CENTER) ;
        vbox.setPadding(new Insets(10)) ;

        Scene scene = new Scene(vbox) ;

        primaryStage.setScene(scene) ;
        primaryStage.initModality(Modality.APPLICATION_MODAL) ;
        primaryStage.setResizable(false) ;
        primaryStage.showAndWait() ;

        return btnYesClicked ;
    }
}