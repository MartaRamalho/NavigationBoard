/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.logIn;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Navegacion;
import static utils.DialogsGenerator.dialogsGenerator;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class LogInController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    private ImageView avatar;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button logInButton;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
         // bind para que el botón de aceptar esté desactivado si los campos no están rellenos
        logInButton.disableProperty().bind(Bindings.or(username.textProperty().isEmpty(), password.textProperty().isEmpty()));
                        
    }    
    

    @FXML
    private void handleLogIn(ActionEvent event) throws IOException, NavegacionDAOException {
        if(!Navegacion.getSingletonNavegacion().exitsNickName(username.getText())){
            dialogsGenerator("Invalid Username", "User not found", "User with this username does not exist.", 4);
        } else {
            if(Navegacion.getSingletonNavegacion().loginUser(username.getText(), password.getText())==null){
                dialogsGenerator("Wrong Password", "Wrong Password", "The introduced password is incorrect.", 4);
            }
            else{
                CurrentUser.getInstance(Navegacion.getSingletonNavegacion().loginUser(username.getText(), password.getText()));
                setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/menuLoggedIn/MenuLoggedIn.fxml")));
                
            }
        }
        
    }

    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/mainMenu/MainMenu.fxml")));
    }
}
