/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class SignUpController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private Circle clip;
    @FXML
    private ImageView avatar;
    private ArrayList<Image> avatarURLs;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private Text somethingWrong;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avatarURLs = new ArrayList<>();
         for(int i=0; i<5; i++){
             String path = "";
             if(i==0) 
                 path = "/resources/newAvatars/default.png";
             else 
                 path = "/resources/newAvatars/avatar"+i+".png";
             Image img = new Image(path);
             avatarURLs.add(img);
         }
        avatar.setImage(avatarURLs.get(0));
    }    
    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("MainMenu.fxml")));
    }


    @FXML
    private void previousImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        if(i-1<0)
            i=avatarURLs.size()-1;
        else
            i--;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    private void nextImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        System.out.println(i);
        if(i+1==avatarURLs.size())
            i=0;
        else
            i++;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    private void handleSignUpPressed(ActionEvent event) throws NavegacionDAOException {
        if(Navegacion.getSingletonNavegacion().exitsNickName(username.getText())){
            DialogsGenerator.dialogsGenerator("Invalid Username", "Username already in use", "This username "
                    + "already exists. Please select a new one.", 4);
        }
        if(!User.checkNickName(username.getText())){
            manageError(username);
        }
        if(!User.checkEmail(email.getText())){
            manageError(email);
        }
        if(!User.checkPassword(password.getText())){
            manageError(password);
        }
        if(Period.between(dateOfBirth.getValue(), java.time.LocalDate.now()).getYears()<12){
            
        }
        else{
            Navegacion.getSingletonNavegacion().registerUser(username.getText(), email.getText(), 
                    password.getText(), avatar.getImage(), (LocalDate) dateOfBirth.getValue());
            email.clear();
            password.clear();
            username.clear();
            dateOfBirth.setValue(null);
            avatar.setImage(avatarURLs.get(0));
            DialogsGenerator.dialogsGenerator("User created", "User Created Successfully", "User Created. Log In to access "
                    + "your account", 2);
        }
    }
    
    private void manageError(TextField textField){
        showErrorMessage(textField);
        textField.requestFocus();
    }
    private void showErrorMessage(TextField textField){
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");
        if(textField.equals(username)){
            somethingWrong.setText("Invalid Username. Must be between 6-15 characters and contain only letters and '-' and '_'");
        }
        if(textField.equals(email)){
            somethingWrong.setText("Invalid Email");
        }
        
    }
    
}