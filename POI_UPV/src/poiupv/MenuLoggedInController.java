/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class MenuLoggedInController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private Circle clip;
    @FXML
    private ImageView avatar;
    @FXML
    private Text welcomeText;
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user=CurrentUser.getInstance(null).getUser();
        avatar.setImage(user.getAvatar());
        welcomeText.setText("Welcome "+user.getNickName());
    }    

    @FXML
    private void editProfile(ActionEvent event) {
    }

    @FXML
    private void viewResults(ActionEvent event) {
    }

    @FXML
    private void makeProblem(ActionEvent event) {
    }

    @FXML
    private void logOutFromAccount(ActionEvent event) {
    }
    
}
