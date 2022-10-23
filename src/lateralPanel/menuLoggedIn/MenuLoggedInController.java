/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.menuLoggedIn;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lateralPanel.logIn.CurrentUser;
import model.User;
import utils.DialogsGenerator;

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

    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void editProfile(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/editProfile/EditProfile.fxml")));
    }

    @FXML
    private void viewResults(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/seeResults/SeeResultSessions.fxml")));
    }

    @FXML
    private void makeProblem(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/problemsTable/ProblemsTable.fxml")));
    }

    @FXML
    private void logOutFromAccount(ActionEvent event) throws IOException, NavegacionDAOException {
        if(DialogsGenerator.dialogsGenerator("Confirmation Log Out", "Confirm Log Out", 
                "Are you sure you want to log out?", 1)){
            CurrentUser.getInstance(null).endSession();
            setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/mainMenu/MainMenu.fxml")));
        }
    }
    
}
