/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.mainMenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class MainMenuController implements Initializable {

    @FXML
    private VBox menu;
    @FXML
    private VBox box;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleLogInPressed(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/logIn/LogIn.fxml")));
    }

    @FXML
    private void handleSignUpPressed(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/signUp/SignUp.fxml")));
    }
    
}
