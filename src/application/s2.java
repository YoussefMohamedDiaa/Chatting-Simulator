package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;

public class s2 extends Application {
	static ArrayList<String> ar = new ArrayList<>();
	static HashMap<Integer, String> idToMessage = new HashMap<>();
	private static String myName = "Server 2";
	private static String secondServerName = "Server 1";
	private static boolean finalChance;
	private static int idReceiver;
	private static String backTo, errorMessage;
	private static String given;
	private static boolean indicator = true;

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("server 2 is running.");
		ExecutorService pool = Executors.newFixedThreadPool(20);
		int clientNumber = 0;
		try (ServerSocket listener = new ServerSocket(6005)) {
			while (true) {
				Socket ss = listener.accept();
				pool.execute(new Capitalizer(ss, clientNumber++));
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	static boolean JoinResponse() throws UnknownHostException, IOException {
		Socket clientSocket = new Socket("localhost", 6000);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes("log " + "GetMemberList1" + '\n');
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String onlineListServer = inFromServer.readLine();
		clientSocket.close();
		boolean flag = false;
		boolean found = false;
		if (onlineListServer.contains(","))
			flag = true;
		if (flag) {
			if (onlineListServer.contains(given.substring(4) + ","))
				found = true;
		} else if (onlineListServer.substring(1, onlineListServer.length() - 1).equals(given.substring(4)))
			found = true;
		if (!ar.contains(given.substring(4)) && !found) {
			ar.add(given.substring(4));
			idToMessage.put(ar.size() - 1, "");
			return true;
		} else
			return false;
	}

	static void Logout(String name) {
		for (int i = 0; i < ar.size(); i++)
			if (ar.get(i).equals(name)) {
				ar.remove(i);
				break;
			}
	}

	static void Route(String message, String destination) throws IOException {
		if (destination.equals(myName))
			idToMessage.put(idReceiver, message);
		else {
			Socket clientSocket = new Socket("localhost", 6000);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes(message);
			clientSocket.close();
		}
	}

	private static class Capitalizer implements Runnable {
		private Socket socket;
		private int clientNumber;

		public Capitalizer(Socket socket, int clientNumber) throws IOException {
			this.socket = socket;
			this.clientNumber = clientNumber;
		}

		public void run() {

			try {

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				given = in.readLine();
				if (given.startsWith("remove")) {
					String name = given.substring(6);
					System.out.println(name);
					Logout(name);
				} else if (given.contains("GetMemberList1")) {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					String z = ar.toString();
					out.println(z);
				} else if (given.contains("GetMemberList")) {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					Socket clientSocket = new Socket("localhost", 6000);
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					outToServer.writeBytes("log " + "GetMemberList1" + '\n');
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					String onlineListServer = inFromServer.readLine();
					clientSocket.close();
					String z = ar.toString();
					out.println(z + " " + onlineListServer);
				} else if (given.substring(0, 3).equals("log")) {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

					if (JoinResponse())
						out.println("Registered");
					else
						out.println("Name taken please enter another one");

				} else if (given.substring(0, 2).equals("to")) {
					StringTokenizer st = new StringTokenizer(given, " ");
					st.nextToken();
					String reciver = st.nextToken();
					String message = "";
					indicator = true;
					while (st.hasMoreTokens())
						message += st.nextToken() + " ";

					idReceiver = -1;
					for (int i = 0; i < ar.size(); i++) {
						if (reciver.equals(ar.get(i))) {
							idReceiver = i;
							break;
						}
					}
					int TTL = 0;
					int indexTTL = given.indexOf(")");
					if (indexTTL >= 0) {
						TTL = Integer.parseInt(given.substring(indexTTL + 1, given.length()));
						given = given.substring(0, indexTTL + 1);
						given += (TTL - 1);
					}
					int indexTTLMessage = message.indexOf(")");
					finalChance = false;
					int diff = given.length() - (indexTTL + 1);
					diff = indexTTL == -1 ? 2 : 2 + diff;
					if (TTL == 1)
						finalChance = true;
					if (idReceiver != -1 && finalChance) {
						if (!message.equalsIgnoreCase(errorMessage))
							message = message.substring(0,
									indexTTLMessage == -1 ? message.length() - diff : indexTTLMessage + 1);
						Route(message, myName);
					}
					if (idReceiver != -1 && !finalChance) {
						if (!message.equalsIgnoreCase(errorMessage))
							message = message.substring(0,
									indexTTLMessage == -1 ? message.length() - diff : indexTTLMessage + 1);
						Route(message, myName);
					}

					if (finalChance && idReceiver == -1) {
						errorMessage = "no user exists with the current name ";

						backTo = "";
						int star = 0;
						int oldstar = 0;
						for (int i = 0; i < message.length(); i++) {
							if (message.charAt(i) == ' ') {
								oldstar = star;
								star = i;
							}
						}

						backTo = message.substring(oldstar + 1, message.length() - 3);
						if (backTo.charAt(backTo.length() - 1) == ')') {
							backTo = backTo.substring(0, backTo.length() - 1);
						}
						Route("to" + " " + backTo + " " + errorMessage, secondServerName);
					}

					if (idReceiver == -1 && !finalChance)
						Route(given, secondServerName);

				} else {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					int id = -1;
					for (int i = 0; i < ar.size(); i++) {
						if (ar.get(i).equals(given.substring(3))) {
							id = i;
							break;
						}
					}
					if (idToMessage.get(id) != null && indicator)
						out.println(idToMessage.get(id));
				}

			} catch (Exception e) {
				System.out.println("Client closed" + " ");
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}