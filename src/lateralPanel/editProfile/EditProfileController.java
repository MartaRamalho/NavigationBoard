/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.editProfile;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
public class EditProfileController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private Circle clip;
    @FXML
    private ImageView avatar;
    @FXML
    private Text somethingWrong;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker dateOfBirth;
    private ArrayList<Image> avatarURLs;
    @FXML
    private Button saveChanges;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avatar.setImage(CurrentUser.getInstance(null).getUser().getAvatar());
        avatarURLs = new ArrayList<>();
         for(int i=0; i<5; i++){
             String path;
             if(i==0) 
                 path = "/resources/Avatars/default.png";
             else 
                 path = "/resources/Avatars/avatar"+i+".png";
             Image img = new Image(path);
             avatarURLs.add(img);
            if(ImageCompare.isImageEqual(avatar.getImage(), img)){ 
                avatar.setImage(img);
            }
        }
        username.setText(CurrentUser.getInstance(null).getUser().getNickName());
        username.setDisable(true);
        password.setText(CurrentUser.getInstance(null).getUser().getPassword());
        email.setText(CurrentUser.getInstance(null).getUser().getEmail());
        dateOfBirth.setValue(CurrentUser.getInstance(null).getUser().getBirthdate());
        dateOfBirth.editableProperty().setValue(false);
        // propiedad para que se desactiven los días en adelante desde hoy
        dateOfBirth.setDayCellFactory(param -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
        }
        });
                
        // bind para que el botón de aceptar esté desactivado si los campos no están rellenos
        saveChanges.disableProperty().bind(
                Bindings.or(
                        Bindings.or(
                                username.textProperty().isEmpty(), email.textProperty().isEmpty()), 
                        Bindings.or(
                                password.textProperty().isEmpty(), dateOfBirth.valueProperty().isNull())));
        //listeners para asegurar informacion correcta
        email.focusedProperty().addListener((observable, oldVal, newVal)->{
            if(!newVal){ //focus lost.
                checkField(email);
            }
        });
        password.focusedProperty().addListener((observable, oldVal, newVal)->{
            if(!newVal){ //focus lost.
                checkField(password);
            }
        });
    }    
    
    //comprobar campos
    private void checkField(TextField field){
        if (field==email){
            if(!User.checkEmail(email.getText())){
                manageError(email);
            } else{
                manageCorrect(email);
            }
        } else if (field==password){
            if(!User.checkPassword(password.getText())){
                manageError(password);
            } else{
                manageCorrect(password);
            }
        }
    }
    
    private void manageError(TextField textField){
        showErrorMessage(textField);
        textField.requestFocus();
    }
    
    private void manageCorrect(TextField textField){
        hideErrorMessage(textField);
    }
    
    private void hideErrorMessage(TextField textField)
    {
        somethingWrong.setText("");
        textField.styleProperty().setValue("");
    }
    
    private void showErrorMessage(TextField textField){
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");
        if(textField.equals(email)){
            somethingWrong.setText("Invalid Email");
        }
        if(textField.equals(password)){
            somethingWrong.setText("Invalid Password");
        }
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/menuLoggedIn/MenuLoggedIn.fxml")));
    }
    
    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }

    @FXML
    public void previousImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        if(i-1<0)
            i=avatarURLs.size()-1;
        else
            i--;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    public void nextImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        if(i+1==avatarURLs.size())
            i=0;
        else
            i++;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) throws NavegacionDAOException {
        if(Period.between(dateOfBirth.getValue(), java.time.LocalDate.now()).getYears()<12){
            DialogsGenerator.dialogsGenerator("Invalid birthdate", "The user is not old enough", 
                    "The user must be at least 12 years old.", 4);
        } else{
            CurrentUser.getInstance(null).getUser().setAvatar(avatar.getImage());
            CurrentUser.getInstance(null).getUser().setBirthdate(dateOfBirth.getValue());
            CurrentUser.getInstance(null).getUser().setEmail(email.getText());
            CurrentUser.getInstance(null).getUser().setPassword(password.getText());
            DialogsGenerator.dialogsGenerator("Changes Saved", "User Updated", "Your User has been updated correctly.", 2);
        }
    }
    
}
