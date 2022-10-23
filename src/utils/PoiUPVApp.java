package utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DBAccess.NavegacionDAOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lateralPanel.logIn.CurrentUser;
import mainPanel.PanelController;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainPanel/Panel.fxml"));
        Parent root = loader.load();
        PanelController controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Navigation Board");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icons/mini_icon.png")));
        stage.show();
        stage.setOnCloseRequest(event->{
            controller.setColorBack();
            if(CurrentUser.getInstance(null).getUser()!=null){
                boolean isClosed = DialogsGenerator.dialogsGenerator("Exit Confirmation", "You are about to exit the application", 
                        "Are you sure you want to exit? Your session will be saved", 3);
                if(isClosed){
                try {
                    CurrentUser.getInstance(null).endSession();
                    controller.colorClose(); 
                } catch (NavegacionDAOException ex) {
                    Logger.getLogger(PoiUPVApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                }else{
                    controller.setColorTop();
                    event.consume();
                }
            } else {
                controller.colorClose();
            } 
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
