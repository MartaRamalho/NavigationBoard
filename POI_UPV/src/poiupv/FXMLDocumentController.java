/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;



/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

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
    private String color = "#ff0000";
    private boolean canMove = false;
    private ImageView protactor = new ImageView("/resources/transportador.png");
    private double startTransX = 0;
    private double startTransY = 0;
    private double baseX = 0;
    private double baseY = 0;
    private boolean handledLastText = true;
    private TextField field;
    private Circle ourCircle = new Circle(1);
    private double centerX = 0;    
    private double centerY = 0;
    private Line axisX;
    private Line axisY;
    private VBox mainMenu;


    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Label posicion;
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
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    

    @FXML
    void zoomIn(ActionEvent event) {  
        scale += 0.1;
        zoom(scale);
        setTool("Zoom in", "Alt +");
   }

    @FXML
    void zoomOut(ActionEvent event) {
        scale -= 0.1;
        if(scale <= 0.1) scale = 0.2;
        zoom(scale);
        setTool("Zoom out", "Alt -");
    }
    
    @FXML
    void handleClean(ActionEvent event) {
        Object[] array = zoomGroup.getChildren().toArray();
        System.out.println("HOLA HE LLEGADO");
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
            setMenu(FXMLLoader.load(getClass().getResource("MainMenu.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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
                double despX = event.getSceneX() - startTransX;
                double despY = event.getSceneY() - startTransY;
                protactor.setTranslateX(baseX + despX);
                protactor.setTranslateY(baseY + despY);
                
            }
        });


        

    }

    
    //=========================================================================
    //
    //Funciones de los elementos de JavaFXML
    //
    //=========================================================================
    
    
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }
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
    }

    @FXML
    private void handleMouseClicked(MouseEvent event) {
        if(toolSelected.equals("_Point")) handleDrawPoint(event);
        else if(toolSelected.equals("_Delete")) handleDelete(event);
        
    }
    
    @FXML
    private void handleMouseDragged(MouseEvent event) {
        if(toolSelected.equals("_Line")) handleDrawLine(event);
        else if(toolSelected.equals("_Arc")) handleDrawCircle(event);
    }
    
    private void handleMouseMoved(MouseEvent event) {
        muestraPosicion(event);
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
        Circle point = new Circle(8);
        point.setStroke(Paint.valueOf(color));
        point.setFill(Paint.valueOf(color));
        zoomGroup.getChildren().add(point);
        point.setLayoutX(event.getX());
        point.setLayoutY(event.getY());
        event.consume();
    }
    
    private void handleIniDrawLine(MouseEvent event) {
        ourLine = new Line(event.getX(), event.getY(),event.getX(),event.getY());
        ourLine.setStroke(Paint.valueOf(color));
        ourLine.setStrokeWidth(3);
        zoomGroup.getChildren().add(ourLine);
    }
    
    private void handleDrawLine(MouseEvent event) {
        ourLine.setEndX(event.getX());
        ourLine.setEndY(event.getY());
        event.consume();
    }
    
    
    private void handleIniDrawCircle(MouseEvent event) {
        ourCircle = new Circle(1);
        ourCircle.setStroke(Paint.valueOf(color));
        ourCircle.setStrokeWidth(3);
        ourCircle.setFill(Color.TRANSPARENT);
        zoomGroup.getChildren().add(ourCircle);
        centerX = event.getX();
        centerY = event.getY();
        ourCircle.setCenterX(centerX);
        ourCircle.setCenterY(centerY);

    }
    
    private void handleDrawCircle(MouseEvent event) {
        double distanceX = centerX - event.getX();
        double distanceY = centerY - event.getY();
        ourCircle.setRadius(Math.sqrt(distanceX * distanceX  + distanceY * distanceY));
        event.consume();
    }
    
    private void handleIniProtactor(MouseEvent event) {
        
       protactor.setX(event.getX());
       protactor.setY(event.getY());
       protactor.setVisible(true);
       event.consume();
    }
    
    private void handleDrawAxis(MouseEvent event) {
        if(axisX != null) zoomGroup.getChildren().remove(axisX);
        if(axisY != null) zoomGroup.getChildren().remove(axisY);
        axisX = new Line(0, event.getY(), 8960 , event.getY());
        axisY = new Line(event.getX(), 0, event.getX(), 5750);
        axisX.setStroke(Paint.valueOf(color));
        axisX.setStrokeWidth(3);
        axisY.setStroke(Paint.valueOf(color));
        axisY.setStrokeWidth(3);
        zoomGroup.getChildren().add(axisX);
        zoomGroup.getChildren().add(axisY);
    }
    
    private void handleText(MouseEvent event) {
        if(handledLastText) {
        handledLastText = false;
        field = new TextField();
        zoomGroup.getChildren().add(field);
        field.setLayoutX(event.getX());
        field.setLayoutY(event.getY());
        
        field.setOnAction(ev -> {
            Text label = new Text(field.getText());
            label.setX(field.getLayoutX());
            label.setY(field.getLayoutY());
            label.setStyle("-fx-font-family:Gafatar; -fx-font-size:30;");
            zoomGroup.getChildren().add(label);
            zoomGroup.getChildren().remove(field);
            ev.consume();
            handledLastText = true;
        });
        
        }
    }
    
    private void handleDelete(MouseEvent event) {
        Object[] array = zoomGroup.getChildren().toArray();
        double alpha = 1;
        for(int i = 0; i < array.length; i++) {
            if(array[i] instanceof Circle) {
                Circle circle = (Circle) array[i];
                double distanceX = circle.getCenterX() - event.getX();
                double distanceY = circle.getCenterY() - event.getY();
                double radius = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if(radius > circle.getRadius() - alpha && radius < circle.getRadius() + alpha) {
                    zoomGroup.getChildren().remove(circle);
                    break;
                } 
            } else if(array[i] instanceof Line) {
                Line line = (Line) array[i];
                double lineDistance = distance(line.getStartX(), line.getEndX(), line.getStartY(), line.getEndY());
                double distanceIniCursor = distance(line.getStartX(),event.getX(),line.getStartY(), event.getY());
                double distanceCursorEnd = distance(event.getX(),line.getEndX(), event.getY(), line.getEndY());

                if(lineDistance + alpha > distanceIniCursor + distanceCursorEnd) {
                    zoomGroup.getChildren().remove(line);
                    break;
                }
            } else if(array[i] instanceof Label) {
                Label label = (Label) array[i];            
            }
        }
    }

    
    private void desactivateTools() {
        protactor.setVisible(false);
        startTransX = 0;
        startTransY = 0;
        baseX = 0;
        baseY = 0;
    }
    
    private double distance(double pointX1, double pointX2, double pointY1, double pointY2) {
        return Math.sqrt((pointX1 - pointX2) * (pointX1 - pointX2) + (pointY1 - pointY2) * (pointY1 - pointY2));
    }

    @FXML
    private void sideMenuControl(ActionEvent event) {
        double target = 0;
        //if menu is closed
        if(splitPaneMenu.getDividers().get(0).positionProperty().isEqualTo(0, 0.01).get()){
            target =  0.3;
            menuButton.disableProperty().setValue(true);
            menuBox.maxWidthProperty().setValue(300);
            KeyValue newDirection = new KeyValue(splitPaneMenu.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), newDirection));
            timeline.play();
            timeline.setOnFinished(eventMenu -> {
                menu.setManaged(true);
                menuBox.minWidthProperty().setValue(300);
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
}   
