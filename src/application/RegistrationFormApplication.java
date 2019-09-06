package application;



import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class RegistrationFormApplication extends Application {
	TextArea recievedHere; 
	TextArea sentHere; 
	@Override
	    public void start(Stage stage) 
	    {
	        // Create the TextArea
		recievedHere = new TextArea();
		recievedHere.setMaxWidth(350);
		recievedHere.setMaxHeight(150);
		
		sentHere = new TextArea();
		sentHere.setMaxWidth(350);
		sentHere.setMaxHeight(150);
	 
	        // Create the Label
	        Label personLbl = new Label("Friends ");
	         
	        // Create the ListView
	        ListView<Person> persons = new ListView<>();
	        // Set the size of the ListView
	        persons.setPrefSize(250, 100);
	        // Add the Persons to the ListView
	        persons.getItems().addAll(createPersonList());
	     
	        // Add a custom cell factory to display formatted names of persons
	        persons.setCellFactory(new PersonCellFactory());
	         
	        // Update the message Label when the selected item changes
	        persons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>()
	        {
	            public void changed(ObservableValue<? extends Person> ov,
	                    final Person oldvalue, final Person newvalue) 
	            { 
	            	
	                System.out.println("hi");
	                displayMessage("message");
	            }
	        });
	         
	        // Create the Selection HBox 
	        HBox selection = new HBox();
	        // Set Spacing to 20 pixels
	        selection.setSpacing(20);
	        // Add the Label and Persons to the HBox
	        selection.getChildren().addAll(personLbl, persons);     
	 
	        // Create the VBox
	        VBox root = new VBox();
	        // Set Spacing to 10 pixels
	        root.setSpacing(10);
	        // Add the HBox and the TextArea to the VBox
	        root.getChildren().addAll(selection, recievedHere,sentHere);
	         
	        // Set the Style-properties of the VBox
	        root.setStyle("-fx-padding: 10;" +
	            "-fx-border-style: solid inside;" +
	            "-fx-border-width: 2;" +
	            "-fx-border-insets: 5;" +
	            "-fx-border-radius: 5;" +
	            "-fx-border-color: blue;");
	         
	        // Create the Scene
	        Scene scene = new Scene(root);
	        // Add the Scene to the Stage
	        stage.setScene(scene);
	        // Set the Title
	        stage.setTitle("A ListView Example with a Cell Factory");
	        // Display the Stage
	        stage.show();   
	     
	    }
	    
	    public static void main(String[] args) {
	        launch(args);
	    }
	    
	    private GridPane createRegistrationFormPane() {
	        // Instantiate a new Grid Pane
	        GridPane gridPane = new GridPane();

	        // Position the pane at the center of the screen, both vertically and horizontally
	        gridPane.setAlignment(Pos.CENTER);

	        // Set a padding of 20px on each side
	        gridPane.setPadding(new javafx.geometry.Insets(40, 40, 40, 40));

	        // Set the horizontal gap between columns
	        gridPane.setHgap(10);

	        // Set the vertical gap between rows
	        gridPane.setVgap(10);

	        // Add Column Constraints

	        // columnOneConstraints will be applied to all the nodes placed in column one.
	        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
	        columnOneConstraints.setHalignment(HPos.RIGHT);

	        // columnTwoConstraints will be applied to all the nodes placed in column two.
	        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
	        columnTwoConstrains.setHgrow(Priority.ALWAYS);

	        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
	        
	        return gridPane;
	    }
	    
	    private void addUIControls(GridPane gridPane) {
	        // Add Header
	        Label headerLabel = new Label("Registration Form");
	        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	        gridPane.add(headerLabel, 0,0,2,1);
	        GridPane.setHalignment(headerLabel, HPos.CENTER);
	        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
            
	        // Add Name Label
	        Label nameLabel = new Label("Full Name : ");
	        gridPane.add(nameLabel, 0,1);

	        // Add Name Text Field
	        TextField nameField = new TextField();
	        nameField.setPrefHeight(40);
	        gridPane.add(nameField, 1,1);


	        // Add Email Label
	        Label emailLabel = new Label("Email ID : ");
	        gridPane.add(emailLabel, 0, 2);

	        // Add Email Text Field
	        TextField emailField = new TextField();
	        emailField.setPrefHeight(40);
	        gridPane.add(emailField, 1, 2);

	        // Add Password Label
	        Label passwordLabel = new Label("Password : ");
	        gridPane.add(passwordLabel, 0, 3);

	        // Add Password Field
	        PasswordField passwordField = new PasswordField();
	        passwordField.setPrefHeight(40);
	        gridPane.add(passwordField, 1, 3);

	        // Add Submit Button
	        Button submitButton = new Button("Submit");
	        submitButton.setPrefHeight(40);
	        submitButton.setDefaultButton(true);
	        submitButton.setPrefWidth(100);
	        gridPane.add(submitButton, 0, 4, 2, 1);
	        GridPane.setHalignment(submitButton, HPos.CENTER);
	        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
	        
	        submitButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                if(nameField.getText().isEmpty()) {
	                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), 
	                    "Form Error!", "Please enter your name");
	                    return;
	                }
	                if(emailField.getText().isEmpty()) {
	                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), 
	                    "Form Error!", "Please enter your email id");
	                    return;
	                }
	                if(passwordField.getText().isEmpty()) {
	                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), 
	                    "Form Error!", "Please enter a password");
	                    return;
	                }

	                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), 
	                "Registration Successful!", "Welcome " + nameField.getText());
	            }
	        });
	    }
	    public class Person 
	    {
	        // Declaring the attributes
	        private String firstName;
	        private String lastName;
	         
	        public Person(String firstName, String lastName) 
	        {
	            this.firstName = firstName;
	            this.lastName = lastName;
	        }
	         
	        public String getFirstName() 
	        {
	            return firstName;
	        }
	         
	        public void setFirstName(String firstName) 
	        {
	            this.firstName = firstName;
	        }
	         
	        public String getLastName() 
	        {
	            return lastName;
	        }
	         
	        public void setLastName(String lastName) 
	        {
	            this.lastName = lastName;
	        }
	     
	        @Override
	        public String toString() 
	        {
	            return firstName + " " + lastName;
	        }
	    }

	    
	    public class PersonCell  extends ListCell<Person>
	    {
	        @Override
	        public void updateItem(Person item, boolean empty) 
	        {
	            super.updateItem(item, empty);
	     
	            int index = this.getIndex();
	            String name = null;
	     
	            // Format name
	            if (item == null || empty) 
	            {
	            } 
	            else
	            {
	                name = (index + 1) + ". " +
	                item.getLastName() + ", " +
	                item.getFirstName();
	            }
	             
	            this.setText(name);
	            setGraphic(null);
	        }
	    }

	    public class PersonCellFactory implements Callback<ListView<Person>, ListCell<Person>>
	    {
	        @Override
	        public ListCell<Person> call(ListView<Person> listview) 
	        {
	            return new PersonCell();
	        }
	    }
	    
	    private ArrayList<Person> createPersonList()
	    {
	        ArrayList<Person> persons = new ArrayList<Person>();
	         
	        persons.add(new Person("Donna", "Duncan"));
	        persons.add(new Person("Layne", "Estes"));
	        persons.add(new Person("John", "Jacobs"));
	        persons.add(new Person("Mason", "Boyd"));
	        persons.add(new Person("Harry", "Eastwood"));
	 
	        return persons;
	    }
	     
	    // Method to display the Person, which has been changed
	    public void displayMessage(String s) 
	    {
	      
	         
	        recievedHere.appendText(s+"\n");
	    }
	    
	    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.initOwner(owner);
	        alert.show();
	    }
	    
}
