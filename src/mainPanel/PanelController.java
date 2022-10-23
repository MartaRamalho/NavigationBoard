/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPanel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mainPanel.colorWidth.ColorWidthController;
import utils.Poi;



/**
 *
 * @author jsoler
 */
public class PanelController implements Initializable {
    
    // stage que se utilizará para el selector de color
    Stage stylePicker = null;
    
    
    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;
    private double scale = 1;
    private String toolSelected = "";
    private Line ourLine;
    private Color color = Color.BLACK;
    private boolean canMove = false;
    private ImageView protactor = new ImageView("/resources/transportador.png");
    private double startTransX = 0;
    private double startTransY = 0;
    private double baseX = 0;
    private double baseY = 0;
    private boolean handledLastText = true;
    private TextField field;
    private Arc ourArc = new Arc();
    private double centerX = 0;    
    private double centerY = 0;
    private boolean colorPickerEnabled = false;
    double width = 3;
    private Line axisX;
    private Line axisY;
    private VBox mainMenu;
    
    private ObjectProperty<Color> colorProp;
    private DoubleProperty widthProp;
    private Scene ourScene;


    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Label toolLabel;
    @FXML
    private Label shortCutLabel;
    @FXML
    private Button menuButton;
    @FXML
    private SplitPane splitPaneMenu;
    private VBox menu;
    @FXML
    private VBox menuBox;
    @FXML
    private VBox mainPane;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Button zoomInButton;
    @FXML
    private VBox map;
    @FXML
    private ImageView image;
    

    @FXML
    void zoomIn(ActionEvent event) {  
        scale += 0.2;
        zoom(scale);
        setTool("Zoom in", "Alt +");
   }

    @FXML
    void zoomOut(ActionEvent event) {
        scale -= 0.1;
        if(scale <= .2) scale = .2;
        zoom(scale);
        setTool("Zoom out", "Alt -");
    }
    
    @FXML
    void handleClean(ActionEvent event) {
        Object[] array = zoomGroup.getChildren().toArray();
        for(int i = 1; i < array.length; i++) {
            if(!(array[i] instanceof ImageView)) zoomGroup.getChildren().remove((Node) array[i]);
        }
         map_scrollpane.setPannable(true);
         map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
         map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
         toolSelected = "";
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

   



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        zoomGroup.getChildren().add(protactor);
        protactor.setVisible(false);
        protactor.setOpacity(0.6);
        
        try {
            setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/mainMenu/MainMenu.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(PanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        protactor.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startTransX = event.getSceneX();
                startTransY = event.getSceneY();
                baseX = protactor.getTranslateX();
                baseY = protactor.getTranslateY();
                //event.getScene().setCursor(Cursor.CROSSHAIR); //change the cursor
                event.consume();   
            }
            
        });
        
        protactor.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                protactor.setCursor(Cursor.MOVE);
                double despX = event.getSceneX() - startTransX;
                double despY = event.getSceneY() - startTransY;
                protactor.setTranslateX(baseX + despX);
                protactor.setTranslateY(baseY + despY);  
            }
        });
        
        // Center the image in the scrollPane
        map_scrollpane.setVvalue(0.5);
        map_scrollpane.setHvalue(0.45);
       
        //Obtain scene to be able to change the cursor on no action events
        ourScene = map_scrollpane.getScene();
    }

    
    //=========================================================================
    //
    //Funciones de los elementos de JavaFXML
    //
    //=========================================================================
    
    
    @FXML
    private void selectTool(Event event) {
        String selected = ((Button)event.getSource()).getText();
        
        if(toolSelected.equals(selected)) {
            canMove = true;
            setTool("", "");
            desactivateTools();
            
        }
        else {           
            setTool(getTool(selected), getShortCut(selected));
            canMove = false;
            toolSelected = selected;
        }
        if(field != null) {
            zoomGroup.getChildren().remove(field);
            handledLastText = true;
        }
        if(canMove) {
            map_scrollpane.setPannable(true);
            map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            toolSelected = "";

        } else {
            map_scrollpane.setPannable(false);
            map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        } 
        
        if(toolSelected.equals("") && colorPickerEnabled) setTool("Modify color and width", "");
    }

    @FXML
    private void handleMouseClicked(MouseEvent event) {
        if(toolSelected.equals("_Point")) handleDrawPoint(event);
        else if(toolSelected.equals("_Delete") && !(event.getSource() instanceof Pane)) handleDelete(event);
        
    }
    
    @FXML
    private void handleMouseDragged(MouseEvent event) {
        if(toolSelected.equals("_Line")) handleDrawLine(event);
        else if(toolSelected.equals("_Arc")) handleDrawCircle(event);
    }
    
    @FXML
    private void handleMousePressed(MouseEvent event) {
        if(toolSelected.equals("_Line")) handleIniDrawLine(event);
        else if(toolSelected.equals("Protracto_r")) handleIniProtactor(event);
        else if(toolSelected.equals("_Text")) handleText(event);
        else if(toolSelected.equals("_Arc")) handleIniDrawCircle(event);
        else if(toolSelected.equals("A_xis")) handleDrawAxis(event);
    }
  
    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }
    
    //=========================================================================
    //
    //Funciones internas
    //
    //=========================================================================
    
    // debug menu
    private void setTool(String tool, String toolSC) {
        toolLabel.setText("Tool: " + tool);
        shortCutLabel.setText( "Shotcut: " + toolSC);
    }
    
    private String getTool(String text) {
        if(text.equals("_Point")) return "Draw a point";
        else if(text.equals("_Line")) return "Draw a line";
        else if(text.equals("_Text")) return "Put text";
        else if(text.equals("_Arc")) return "Draw an arc";
        else if(text.equals("Protracto_r")) return "Use protactor";
        else if(text.equals("_Delete")) return "Delete an element";
        else if(text.equals("_Clean")) return "Clean the Navigation Board";
        else if(text.equals("A_xis")) return "Draw axis";
        else return "";
    }
    
    private String getShortCut(String text) {
        if(text.equals("_Point")) return "Alt P";
        else if(text.equals("_Line")) return "Alt L";
        else if(text.equals("_Text")) return "Alt T";
        else if(text.equals("_Arc")) return "Alt A";
        else if(text.equals("Protracto_r")) return "Alt R";
        else if(text.equals("_Delete")) return "Alt D";
        else if(text.equals("_Clean")) return "Alt C";
        else if(text.equals("A_xis")) return "Alt X";
        else return "";
    }
    
    private void handleDrawPoint(MouseEvent event) {
        Circle point = new Circle(8 / scale);
        point.setStroke(color);
        point.setFill(color);
        zoomGroup.getChildren().add(point);
        point.setLayoutX(event.getX());
        point.setLayoutY(event.getY());
        
        point.setOnMouseClicked(ev -> {
            if(toolSelected.equals("") && colorPickerEnabled) {
                point.setFill(colorProp.get());
                point.setStroke(colorProp.get());
            }
            else handleMouseClicked(ev);
        });
        point.setOnMousePressed(ev -> {
            handleMousePressed(ev);
        });
        point.setOnMouseDragged(ev -> {
            handleMouseDragged(ev);
        });
        
        
        event.consume();
    }
    
    private void handleIniDrawLine(MouseEvent event) {
        ourLine = new Line(event.getX(), event.getY(),event.getX(),event.getY());
        ourLine.setStroke(color);
        ourLine.setStrokeWidth(width / scale);
        zoomGroup.getChildren().add(ourLine);
        ourLine.setOnMouseClicked(ev -> {
            if(toolSelected.equals("") && colorPickerEnabled) {
                ourLine.setStroke(colorProp.get());
                ourLine.setStrokeWidth(widthProp.get() / scale);
            
            }
            else handleMouseClicked(ev);
        });
        ourLine.setOnMousePressed(ev -> {
            handleMousePressed(ev);
        });
        ourLine.setOnMouseDragged(ev -> {
                handleMouseDragged(ev);
        });


    }
    
    private void handleDrawLine(MouseEvent event) {
        ourLine.setEndX(event.getX());
        ourLine.setEndY(event.getY());

        event.consume();
    }
    
    
    private void handleIniDrawCircle(MouseEvent event) {
        ourArc = new Arc();
        ourArc.setStroke(color);
        ourArc.setStrokeWidth(width / scale);
        ourArc.setFill(null);
        zoomGroup.getChildren().add(ourArc);
        centerX = event.getX();
        centerY = event.getY();
        ourArc.setCenterX(centerX);
        ourArc.setCenterY(centerY);
        
        ourArc.setStartAngle(0);
        
        ourArc.setOnMouseClicked(ev -> {
            if(toolSelected.equals("") && colorPickerEnabled) {
                ourArc.setStroke(colorProp.get());
                ourArc.setStrokeWidth(widthProp.get());
            }
            else handleMouseClicked(ev);
        });
        ourArc.setOnMousePressed(ev -> {
            handleMousePressed(ev);
        });
        
        ourArc.setOnMouseDragged(ev -> {
            handleMouseDragged(ev);
        });
    }
    
    private void handleDrawCircle(MouseEvent event) {
        double distanceX = centerX - event.getX();
        double distanceY = centerY - event.getY();
        ourArc.setRadiusX(Math.sqrt(distanceX * distanceX  + distanceY * distanceY));
        ourArc.setRadiusY(Math.sqrt(distanceX * distanceX  + distanceY * distanceY));
        
        //Obatin the angle from cursor coordinates
        
        double angle = Math.acos((event.getX() - ourArc.getCenterX()) / ourArc.getRadiusX()) * (180 / Math.PI);
        
        if(event.getY() < ourArc.getCenterY()) ourArc.setLength(angle);
        else ourArc.setLength(360 - angle);
        
        // debug System.out.println(ourArc.getLength());
        event.consume();
    }
    
    private void handleIniProtactor(MouseEvent event) {
       protactor.toFront();
       protactor.setX(event.getX());
       protactor.setY(event.getY());
       protactor.setScaleX(1 / scale);
       protactor.setScaleY(1 / scale);
       protactor.setVisible(true);
       event.consume();
    }
    
    private void handleDrawAxis(MouseEvent event) {
        if(axisX != null) zoomGroup.getChildren().remove(axisX);
        if(axisY != null) zoomGroup.getChildren().remove(axisY);
        axisX = new Line(-1622, event.getY(), 2460 , event.getY());
        axisY = new Line(event.getX(), -1050, event.getX(), 1578);
        axisX.setStroke(color);
        axisX.setStrokeWidth(width / scale);
        axisY.setStroke(color);
        axisY.setStrokeWidth(width / scale);
        zoomGroup.getChildren().add(axisX);
        zoomGroup.getChildren().add(axisY);
        
        axisX.setOnMouseClicked(ev -> {
            if(toolSelected.equals("") && colorPickerEnabled) {
                axisX.setStroke(colorProp.get());
                axisX.setStrokeWidth(widthProp.get() / scale); 
                axisY.setStroke(colorProp.get());
                axisY.setStrokeWidth(widthProp.get() / scale); 
            }
            else if(toolSelected.equals("_Delete")) {
                handleDelete(ev);
                if(axisY != null) zoomGroup.getChildren().remove(axisY);             
            } else {handleMouseClicked(ev);}
        });
        axisX.setOnMousePressed(ev -> {
            handleMousePressed(ev);
        });
        axisX.setOnMouseDragged(ev -> {
             handleMouseDragged(ev);
        });
        
        
   
        axisY.setOnMouseClicked(ev -> {
            if(toolSelected.equals("") && colorPickerEnabled) {
                axisX.setStroke(colorProp.get());
                axisX.setStrokeWidth(widthProp.get() / scale); 
                axisY.setStroke(colorProp.get());
                axisY.setStrokeWidth(widthProp.get() / scale); 
            }
            else if(toolSelected.equals("_Delete")) {
                handleDelete(ev);
                if(axisX != null) zoomGroup.getChildren().remove(axisX);             
            } else {handleMouseClicked(ev);}
        });
        axisY.setOnMousePressed(ev -> {
            handleMousePressed(ev);
        });
        axisY.setOnMouseDragged(ev -> {
            handleMouseDragged(ev);
        });     
    }
    
    private void handleText(MouseEvent event) {
        if(!handledLastText) {
            zoomGroup.getChildren().remove(field);
            handledLastText = true;
        }
        
        handledLastText = false;
        field = new TextField();

        zoomGroup.getChildren().add(field);
         
        field.setScaleX(1.1 / scale);       
        field.setScaleY(1.1 / scale);
        field.setLayoutX(event.getX() - (field.getWidth() - field.getWidth() * field.getScaleX()) / 2);
        field.setLayoutY(event.getY() - (field.getHeight() - field.getHeight() * field.getScaleX()) / 2);
        field.setAlignment(Pos.CENTER_LEFT);
        field.requestFocus();
        
        
        field.setOnAction(ev -> {
            Text label = new Text(field.getText());
            label.setX(field.getLayoutX());
            label.setY(field.getLayoutY());
            label.setStyle("-fx-font-family:Segoe UI Light; -fx-font-size:" + (int)(20 / scale) + ";");
            label.setStroke(color);
            zoomGroup.getChildren().add(label);
            zoomGroup.getChildren().remove(field);  
            label.focusTraversableProperty().set(true);
            label.setOnMouseClicked(e -> {
                if(toolSelected.equals("") && colorPickerEnabled) {
                    label.setStroke(colorProp.get());
                    label.setFill(colorProp.get());
                }
                else handleMouseClicked(e);
            });
            
            label.setOnMousePressed(e -> {
                 handleMousePressed(e);
            });
            label.setOnMouseDragged(e -> {
                handleMouseDragged(e);
            });
                        
            ev.consume();
            handledLastText = true;
        });
    }
    
    private void handleDelete(MouseEvent event) {
        zoomGroup.getChildren().remove(event.getSource());
    }

    
    private void desactivateTools() {
        protactor.setVisible(false);
        startTransX = 0;
        startTransY = 0;
        baseX = 0;
        baseY = 0;
    }


    @FXML
    private void sideMenuControl(ActionEvent event) {
        double target = 0;
        //if menu is closed
        if(splitPaneMenu.getDividers().get(0).positionProperty().isEqualTo(0, 0.01).get()){
            target =  0.35;
            menuButton.disableProperty().setValue(true);
            menuBox.maxWidthProperty().setValue(350);
            KeyValue newDirection = new KeyValue(splitPaneMenu.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), newDirection));
            timeline.play();
            timeline.setOnFinished(eventMenu -> {
                menu.setManaged(true);
                menuBox.minWidthProperty().setValue(350);
                menuButton.disableProperty().setValue(false);
                    });
         //if menu is open
        } else {
            target=0.0;
            menuButton.disableProperty().setValue(true);
            menuBox.minWidthProperty().setValue(0);
            menu.setManaged(false);
            KeyValue newDirection = new KeyValue(splitPaneMenu.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), newDirection));
            timeline.setOnFinished(disabbleDragging -> {
                menuBox.maxWidthProperty().setValue(0);
                menuButton.disableProperty().setValue(false);
                    });
            timeline.play();
        }
        
    }
    
    public void setMenu(VBox box){
        menu=box;
        menuBox.getChildren().clear();
        menuBox.getChildren().add(menu);
    }

    @FXML
    private void handleMouseReleased(MouseEvent event) {
    }


    @FXML
    private void obtainColor(ActionEvent event) throws IOException {
        
        // check de que colorPicker no está abierto ya
        if(!colorPickerEnabled) {
            colorPickerEnabled = true;
            if(toolSelected.equals("") && colorPickerEnabled) setTool("Modify color and width", "");
            stylePicker = new Stage();
            
            // Icono
            stylePicker.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icons/toolbarIcons/color-icon.png")));
            
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/mainPanel/colorWidth/ColorWidth.fxml"));
            
            // título
            stylePicker.setTitle("Colours");             
            stylePicker.setScene(new Scene(fxmlLoader.load()));
            
            // eliminar posibilidad de cambiar tamaño
            stylePicker.setResizable(false);
            
            // always on top
            stylePicker.setAlwaysOnTop(true);
            
            // disable minimise and maximise
            stylePicker.initStyle(StageStyle.UTILITY);
            
            // opacidad cuando deja de estar resaltado
            stylePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!stylePicker.focusedProperty().get()) { stylePicker.setOpacity(0.7); }
                else { stylePicker.setOpacity(1.0); }
            });
            
            // llamada al controlador
            ColorWidthController controller = (ColorWidthController) fxmlLoader.getController();

            // asignación de color y tamaño
            colorProp = controller.getColorProp();
            widthProp = controller.getWidthProp();
            colorProp.addListener((obs, newV, oldV) -> {
                color = colorProp.get();
            });
            controller.setColor(color);
            widthProp.addListener((obs, newV, oldV) -> {
                width = widthProp.get();
            });
            controller.setWidth(width);
            
            // mostrar ventana
            stylePicker.showAndWait();
            // ventana cerrada
            colorPickerEnabled = false;
            // update debug menu
            if(toolSelected.equals("")) setTool("", "");
        } 
    }
    public void colorClose(){
        if(stylePicker != null){
            if(stylePicker.isShowing()){
                stylePicker.close();
            }
        }
    }
    
    public void setColorBack(){
        if(stylePicker != null){
            if(stylePicker.isShowing()){
                stylePicker.setAlwaysOnTop(false);
            }
        }
    }
    public void setColorTop(){
        if(stylePicker != null){
            if(stylePicker.isShowing()){
                stylePicker.setAlwaysOnTop(true);
            }
        }
    }
}   
