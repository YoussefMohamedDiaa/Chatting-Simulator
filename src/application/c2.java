package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.sun.javafx.tk.Toolkit.Task;

import application.c1.Person;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class c2 extends Application {
	static String lastMessage = "";
	static String name1 = "";
	static int existFlag = 0;
	static Socket clientSocket = null;
	static TextArea recievedHere;
	static TextArea sentHere;
	static Label title;
	static TextArea sentingTo;
	static Stage window;
	static Scene scene1, scene2;
	static Button submitButton = new Button("Submit");
	static Button refresh = new Button("refresh");
	static Button sendButton = new Button("Send");
	static Button logoutButton = new Button("Logout");
	public static ListView<Person> persons;
	public static VBox personsVB = new VBox();
	public static HashSet<String> currentGroup = new HashSet<>();
	public static ArrayList<String> lastGroup = new ArrayList<>(); 
	static TextArea nameField;
	static Image image = new Image("personIcon.png");
	static ImageView iv1 = new ImageView();

	@Override
	public void start(Stage primaryStage) throws Exception {
		iv1.setImage(image);
		iv1.setFitWidth(30);
		iv1.setFitHeight(30);
		window = primaryStage;
		// window.setTitle(name1);

		GridPane gridPane = createRegistrationFormPane();

		addUIControls(gridPane);

		scene1 = new Scene(gridPane, 800, 500);

		recievedHere = new TextArea();
		recievedHere.setMaxWidth(350);
		recievedHere.setMaxHeight(150);

		sentingTo = new TextArea();
		sentingTo.setMaxWidth(230);
		sentingTo.setMaxHeight(30);

		sentHere = new TextArea();
		sentHere.setMaxWidth(350);
		sentHere.setMaxHeight(150);
		sendButton.setMaxHeight(150);
		sendButton.setMaxWidth(150);
		logoutButton.setMaxHeight(150);
		logoutButton.setMaxWidth(150);
		
		title = new Label();
		title.setMinWidth(350);
		title.setMinHeight(50);
		title.setTextFill(Color.web("#ffffff"));
		
		Text tit = TextBuilder.create().text("     Whats App (not original)").build();
		tit.setText("Whats App (not original)");
		tit.setFill(Color.WHITE);
		title.setText(tit.getText());
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 23));
	    title.setStyle("-fx-background-color: green;");
	    
		

		// Create the ListView
		ListView<Person> persons = new ListView<>();
		// Set the size of the ListView
		persons.setPrefSize(250, 100);

		// Add a custom cell factory to display formatted names of persons
		persons.setCellFactory(new PersonCellFactory());

		// Update the message Label when the selected item changes
		// persons.getSelectionModel().selectedItemProperty().addListener(new
		// ChangeListener<Person>()
		// {
		// public void changed(ObservableValue<? extends Person> ov,
		// final Person oldvalue, final Person newvalue)
		// {
		// System.out.println("hi");
		// sentingTo.setText(newvalue.toString());
		//
		// }
		// });

		// Create the Selection HBox
		personsVB.setStyle(" -fx-border-color: black;");
		HBox selection = new HBox();
		// Set Spacing to 20 pixels
		selection.setSpacing(20);
		// Add the Label and Persons to the HBox
		selection.getChildren().addAll( personsVB);

		Label headerLabel = new Label("Sending to: ");
		headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		HBox sendingInfo = new HBox();
		// Set Spacing to 20 pixels
		sendingInfo.setSpacing(20);
		// Add the Label and Persons to the HBox
		sendingInfo.getChildren().addAll(headerLabel, sentingTo);

		// Create the VBox
		VBox root = new VBox();
		// Set Spacing to 10 pixels
		root.setSpacing(10);
		// Add the HBox and the TextArea to the VBox
		recievedHere.setStyle("-fx-control-inner-background: #DFB951;");
		sentHere.setStyle("-fx-control-inner-background: #DFB951;");

		HBox bts = new HBox();
		bts.setSpacing(10);
		
		bts.getChildren().addAll(sendButton,logoutButton);
		
		root.getChildren().addAll(title,selection, recievedHere, sendingInfo, sentHere, bts, refresh);

		// Set the Style-properties of the VBox
		root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: #DFB951;");

		// Create the Scene
		Scene scene2 = new Scene(root);

		submitButton.setStyle("-fx-text-fill: #006464;" + "-fx-background-color: #DFB951;" + "-fx-border-radius: 20;"
				+ "-fx-background-radius: 20;" + "-fx-padding: 5;");

		sendButton.setStyle("-fx-text-fill: #006464; " +

				"-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-padding: 5;");

		logoutButton.setStyle("-fx-text-fill: #006464; " +

				"-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-padding: 5;");
		logoutButton.setOnMouseClicked(e -> {
			try {
				Logout(name1);
			} catch (Exception e1) {

			}
		});
		refresh.setStyle("-fx-text-fill: #006464; " +

				"-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-padding: 5;");

		refresh.setOnAction(e -> {
			personsVB.getChildren().clear();
			persons.getItems().clear();
			try {
				ArrayList<Person> arp = createPersonList();
				persons.getItems().addAll(createPersonList());
				int dotsNumber = persons.getItems().size();
				currentGroup= new HashSet<>();
				for(int i=0;i<dotsNumber;i++) {
					currentGroup.add(arp.get(i).toString());
					Label labname = new Label(arp.get(i).toString());
					labname.setStyle("-fx-font-weight: bold;");
					labname.setMinWidth(300f);
					ImageView currentIv = new ImageView();
				    currentIv.setImage(image);
					currentIv.setFitWidth(30);
					currentIv.setFitHeight(30);
					HBox currentHb = new HBox(currentIv,labname,onlineCircle());
					currentHb.setStyle("-fx-background-color: #ffffff;"+" -fx-border-color: grey;");
					personsVB.getChildren().add(currentHb);	
					
				}
				for(int i=0;i<lastGroup.size();i++) {
					if(!currentGroup.contains(lastGroup.get(i))) {
						Label labname = new Label(lastGroup.get(i));
						labname.setStyle("-fx-font-weight: bold;");
						labname.setMinWidth(300f);
						ImageView currentIv = new ImageView();
					    currentIv.setImage(image);
						currentIv.setFitWidth(30);
						currentIv.setFitHeight(30);
						HBox currentHb = new HBox(currentIv,labname,offlineCircle());
						currentHb.setStyle("-fx-background-color: #fffffff;"+" -fx-border-color: grey;");
						personsVB.getChildren().add(currentHb);	
					}
				}
				
				lastGroup = new ArrayList<>();
				for(String name : currentGroup)
					lastGroup.add(name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		window.setScene(scene1);
		submitButton.setOnAction(e -> {

			if (nameField.getText().isEmpty()) {
				showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!",
						"Please enter your name");
				return;
			}

			try {
				if (!Join(nameField.getText())) {
					showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!",
							"Name taken, please enter another name");
					return;
				}
			} catch (IOException ex) {
			}
			try {
				// System.out.println(s1.allUser);
				s1.increment();
				// System.out.println(s1.allUser);
				getMessage R1 = new getMessage("Thread-z", persons);
				R1.start();

				Login(nameField.getText());
				persons.getItems().clear();
				persons.getItems().addAll(createPersonList());
				
				personsVB.getChildren().clear();
				ArrayList<Person> arp = createPersonList();
				int dotsNumber = persons.getItems().size();
				currentGroup= new HashSet<>();
				for(int i=0;i<dotsNumber;i++) {
					currentGroup.add(arp.get(i).toString());
					Label labname = new Label(arp.get(i).toString());
					labname.setStyle("-fx-font-weight: bold;");
					labname.setMinWidth(300f);
					ImageView currentIv = new ImageView();
				    currentIv.setImage(image);
					currentIv.setFitWidth(30);
					currentIv.setFitHeight(30);
					HBox currentHb = new HBox(currentIv,labname,onlineCircle());
					currentHb.setStyle("-fx-background-color: #ffffff;"+" -fx-border-color: grey;");
					personsVB.getChildren().add(currentHb);	
				}
				
				for(int i=0;i<lastGroup.size();i++) {
					if(!currentGroup.contains(lastGroup.get(i))) {
						Label labname = new Label(lastGroup.get(i));
						labname.setStyle("-fx-font-weight: bold;");
						labname.setMinWidth(300f);
						ImageView currentIv = new ImageView();
					    currentIv.setImage(image);
						currentIv.setFitWidth(30);
						currentIv.setFitHeight(30);
						HBox currentHb = new HBox(currentIv,labname,offlineCircle());
						currentHb.setStyle("-fx-background-color: #ffffff;"+" -fx-border-color: grey;");
						personsVB.getChildren().add(currentHb);	
					}
				}
				
				lastGroup = new ArrayList<>();
				for(String name : currentGroup)
					lastGroup.add(name);

				window.setScene(scene2);

				// getMessage R1 = new getMessage("Thread-z",persons);
				// R1.start();
				// Login(nameField.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		window.setTitle(nameField.getText());
		window.show();

	}
	
	 public static Circle offlineCircle() {
	 	    
	 		Circle node = new Circle();
	 		node.setCenterX(10.0f);
	 		node.setCenterY(10.0f);
	 		node.setRadius(8.0f);
	 		node.setFill(javafx.scene.paint.Color.GREY);
	 		
	 		return node;
	 	 }


	 public static Circle onlineCircle() {
		    
			Circle node = new Circle();
			node.setCenterX(10.0f);
			node.setCenterY(10.0f);
			node.setRadius(8.0f);
			node.setFill(javafx.scene.paint.Color.GREEN);
			
			return node;
		 }
	
	public static void Login(String nameFromField) throws IOException {

		Scanner sc = new Scanner(System.in);
		// System.out.println("PLEASE LOG IN");

		name1 = nameFromField;

		sendButton.setOnAction(e -> {
			try {
				clientSocket = new Socket("localhost", 6005);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// System.out.println("SEND TO :");
			String name = sentingTo.getText();

			String message = sentHere.getText();
			if (message.equals("exist"))
				existFlag = 1;

			try {
				Chat(name1, name, 3, message);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	static void Chat(String Source, String Destination, int TTL, String Message) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes("to " + Destination + " " + Message + " (from " + Source + ")" + TTL);
		clientSocket.close();
	}

	static void Logout(String name) throws IOException {
		clientSocket = new Socket("localhost", 6005);
		DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
		outToServer1.writeBytes("remove" + name + '\n');
		GetMemberListGUI();
		existFlag=1;
		System.exit(0);
	}

	static boolean Join(String name) throws IOException {
		clientSocket = new Socket("localhost", 6005);
		DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
		outToServer1.writeBytes("log " + name + '\n');
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String serverResponse = inFromServer.readLine();
		if (serverResponse != null && serverResponse.equals("Registered")) {
			System.out.println(serverResponse);
			clientSocket.close();
			return true;
		}
		clientSocket.close();
		return false;
	}

	static void GetMemberList() throws UnknownHostException, IOException {
		String op = "GetMemberList";
		DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
		outToServer1.writeBytes("log " + op + '\n');
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String sit = inFromServer.readLine();
		System.out.println(sit);
	}

	static String GetMemberListGUI() throws UnknownHostException, IOException {
		String op = "GetMemberList";
		DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
		outToServer1.writeBytes("log " + op + '\n');
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String sit = inFromServer.readLine();
		return sit;
	}

	public static class getMessage implements Runnable {
		private Thread t;
		private String threadName;
		private ListView<Person> personsth;

		getMessage(String name, ListView<Person> persons) {
			threadName = name;
			personsth = persons;
		}

		public void run() {

			try {

				while (true) {

					if (existFlag == 1)
						return;

					Socket clientSocket = new Socket("localhost", 6005);

					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					outToServer.writeBytes("123" + name1 + '\n');
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));

					String modifiedSentence = inFromServer.readLine();
					if (modifiedSentence != null && !modifiedSentence.equals(lastMessage)) {
						System.out.println(modifiedSentence);
						displayMessage(modifiedSentence);
						lastMessage = modifiedSentence;
					}

					clientSocket.close();
					// System.out.println(s1.allu());
					// if(s1.allu()>personsth.getItems().size()) {
					// personsth.getItems().add(createPersonList().get(createPersonList().size()-1));
					// }

					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				System.out.println("Thread " + threadName + " interrupted.");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Thread " + threadName + " exiting.");
		}

		public void start() {

			if (t == null) {
				t = new Thread(this, threadName);
				t.start();
			}
		}
	}

	private GridPane createRegistrationFormPane() {
		// Instantiate a new Grid Pane
		GridPane gridPane = new GridPane();

		// Position the pane at the center of the screen, both vertically and
		// horizontally
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
		ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
		columnTwoConstrains.setHgrow(Priority.ALWAYS);

		gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

		return gridPane;
	}

	private void addUIControls(GridPane gridPane) {
		// Add Header
		Label headerLabel = new Label("Registration Form");
		headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		gridPane.add(headerLabel, 0, 0, 2, 1);
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

		// Add Name Label
		Label nameLabel = new Label("Full Name : ");
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		gridPane.add(nameLabel, 0, 1);

		// Add Name Text Field
		nameField = new TextArea();
		nameField.setPrefHeight(40);
		nameField.setStyle("-fx-control-inner-background: #DFB951;");

		gridPane.add(nameField, 1, 1);

		submitButton.setPrefHeight(40);
		submitButton.setDefaultButton(true);
		submitButton.setPrefWidth(100);
		gridPane.add(submitButton, 0, 4, 2, 1);
		GridPane.setHalignment(submitButton, HPos.CENTER);
		GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

	}

	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}

	public static class Person {
		// Declaring the attributes
		private String firstName;

		public Person(String firstName) {
			this.firstName = firstName;

		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		@Override
		public String toString() {
			return firstName;
		}
	}

	public class PersonCell extends ListCell<Person> {
		@Override
		public void updateItem(Person item, boolean empty) {
			super.updateItem(item, empty);

			int index = this.getIndex();
			String name = null;

			// Format name
			if (item == null || empty) {
			} else {
				name = (index + 1) + ". " +

						item.getFirstName();
			}

			this.setText(name);
			setGraphic(null);
		}
	}

	public class PersonCellFactory implements Callback<ListView<Person>, ListCell<Person>> {
		@Override
		public ListCell<Person> call(ListView<Person> listview) {
			return new PersonCell();
		}
	}

	public static ArrayList<Person> createPersonList() throws UnknownHostException, IOException {
		ArrayList<Person> persons = new ArrayList<Person>();

		clientSocket = new Socket("localhost", 6005);
		StringTokenizer st = new StringTokenizer(GetMemberListGUI());
		while (st.hasMoreTokens()) {
			String cur = st.nextToken();

			if (cur.charAt(0) == '[' && cur.charAt(1) == ']')
				break;

			if (cur.charAt(0) == '[')
				persons.add(new Person(cur.substring(1, cur.length() - 1)));
			else
				persons.add(new Person(cur.substring(0, cur.length() - 1)));
		}

		return persons;
	}

	// Method to display the Person, which has been changed
	public static void displayMessage(String s) {

		recievedHere.appendText(s + "\n");
	}
}