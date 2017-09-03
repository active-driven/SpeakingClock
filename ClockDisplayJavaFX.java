package speakingClock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Toggle buttons in a toggle group.
 */

public class ClockDisplayJavaFX extends Application {

    private final static double TOGGLEBUTTON_WIDTH = 150;
    private final static double TOGGLEBUTTON_HEIGHT = 100;
    
    public Parent createContent() {

        // create label to show result of selected toggle button
        final Label displayFirst = new Label();
        displayFirst.setAlignment(Pos.CENTER);
        displayFirst.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        displayFirst.setTextFill(Color.web("#0096d6"));
        
        final Label displaySecond = new Label();
        displaySecond.setAlignment(Pos.CENTER);
        displaySecond.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        displaySecond.setTextFill(Color.web("#0096d6"));

        final Label displayThird = new Label();
        displayThird.setAlignment(Pos.CENTER);
        displayThird.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        displayThird.setTextFill(Color.WHITE);
               
        // create 3 toggle buttons and a toogle group for them
        final ToggleButton tb1 = new ToggleButton("Wyświetl godzinę");
        tb1.setMinSize(TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);
        tb1.setStyle("-fx-border-color: black;"); 
        tb1.setStyle("-fx-base: #7FFF00;");
 
        final ToggleButton tb2 = new ToggleButton("Powiedz godzinę");
        tb2.setMinSize(TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);
        tb2.setStyle("-fx-border-color: black;"); 
        tb2.setStyle("-fx-base: #7FFF00;");
        
        final ToggleButton tb3 = new ToggleButton("Wyświetl i powiedz godzinę");
        tb3.setMinSize(TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);  
        tb3.setStyle("-fx-border-color: black;"); 
        tb3.setStyle("-fx-base: #7FFF00;");
        
        ToggleGroup group = new ToggleGroup();
        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
        tb3.setToggleGroup(group);
        
        
        
        displayFirst.setText("");
        displaySecond.setText("kliknij na co masz ochotę");
                                            
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) -> {

            if (selectedToggle == tb1) {  
                Clock clock = new Clock();        
            	clock.convert();
        		displayFirst.setText("Jest godzina " + clock.getHOURS() + " ");
        		displaySecond.setText(clock.getExtractedTensOFMinutes() == 0 ? clock.getMINUTES() + "." : clock.getTENS_OF_MINUTES()+ " " + clock.getMINUTES() + ".");
        		// to avoid printing milliseconds as in 20:20:49.075
        		displayThird.setText("(" + clock.getActualTimeConverted().substring(0, 8) + ")");   
        		// null releases the button, you don't have to click twice if you want to do sth more
        		group.selectToggle(null);             
            }
            if (selectedToggle == tb2) { 
            	// Clock instance and Runnable block inside group.selectedToggleProperty()... to display new time every clicking 
                Clock clock = new Clock();        
                Runnable r = () ->  { 
                	try {
                	clock.createPath();
                } catch (InterruptedException e) {
                	e.printStackTrace();
                	System.err.println("Wystąpił błąd podczas wygłaszania godziny lub wyświetlania tekstu.");
                }
               };
            	clock.convert();
            	displayFirst.setText("");
            	displaySecond.setText("nie poproszono o wyświetlenie godziny");
            	displayThird.setText("");
        		
            	Thread th = new Thread(r);
            	th.start(); 
            	
        		group.selectToggle(null);
            }
            
            if (selectedToggle == tb3) {    
                Clock clock = new Clock();        
                Runnable r = () ->  { 
                	try {
                	clock.createPath();
                } catch (InterruptedException e) {
                	e.printStackTrace();
                	System.err.println("Wystąpił błąd podczas wygłaszania godziny lub wyświetlania tekstu.");
                }
               };
            	clock.convert();
        		displayFirst.setText("Jest godzina " + clock.getHOURS() + " ");
        		displaySecond.setText(clock.getExtractedTensOFMinutes() == 0 ? clock.getMINUTES() + "." : clock.getTENS_OF_MINUTES()+ " " + clock.getMINUTES() + ".");
        		displayThird.setText("(" + clock.getActualTimeConverted().substring(0, 8) + ")");

        		Thread th = new Thread(r);
        		th.start(); 
        		
        		group.selectToggle(null);
            }
        });

        // add buttons and label to grid and set their positions
        GridPane.setConstraints(tb1, 0, 1);
        GridPane.setConstraints(tb2, 1, 1);
        GridPane.setConstraints(tb3, 2, 1); 
        GridPane.setConstraints(displayFirst, 0, 2, 3, 1, HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(displaySecond, 0, 3, 3, 1, HPos.CENTER, VPos.BASELINE);
        GridPane.setConstraints(displayThird, 0, 4, 3, 1, HPos.CENTER, VPos.BASELINE);
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(12);
        grid.setStyle("-fx-base: black;");
        grid.getChildren().addAll(tb1, tb2, tb3, displayFirst, displaySecond, displayThird);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {   	
       
       Scene scene = new Scene(createContent(),600,400);
       primaryStage.setTitle("Zegar");
       primaryStage.setScene(scene);
       primaryStage.show();
       primaryStage.setOnCloseRequest(e -> {
         Platform.exit();
         System.exit(0);
       });
 //    or: 
 //    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
 //   	  @Override
 //   	  public void handle(WindowEvent event) {
 //   	      Platform.exit();
 //   	      System.exit(0);
 //   	  }
 //    });
    }
    
    /**
     * Java main for when running without JavaFX launcher
     */

    public static void main(String[] args) {
        launch(args);
    }
}
