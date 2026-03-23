package client;

import chess.ChessBoard;

import java.util.Objects;
import java.util.Scanner;

import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;
import ui.*;

public class ChessClient {
    private boolean loggedIn = false;
    private String authToken = "";
    private final ServerFacade server;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run() throws ResponseException {
        System.out.println("Welcome to off-brand Chess.com\n");
        String message = "";
        while (!Objects.equals(message, "quit")){
            if (!this.loggedIn) {
                message = mainMenu();
            } else {
                message = loggedInMenu();
            }
        }
    }

    private String mainMenu() throws ResponseException {
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("   1 - Help");
        System.out.println("   2 - Quit");
        System.out.println("   3 - Login");
        System.out.println("   4 - Register");
        return parseMainMenu(getInput());
    }

    private String parseMainMenu(String input) throws ResponseException {
        if (input.contains("1")){
            // print out some helpful material
            System.out.println("Enter the corresponding menu number to " +
                    "\n    quit the application, " +
                    "\n    login to an existing account, " +
                    "\n    or register a new user");
            return "";
        } else if (input.contains("2")){
            System.out.println("We're quitting...");
            // quit the system
            return "quit";
        } else if (input.contains("3")){
            System.out.println("Username: ");
            String username = getInput();
            System.out.println("Password: ");
            String password = getInput();
            // check the credentials
            try {
                AuthData authData = server.login(username, password);
                this.authToken = authData.authToken();
                System.out.println("Logged in successfully!");
                this.loggedIn = true;
            } catch (Exception ex) {
                System.out.println("Could not log in the user with that username and password");
            }
        } else if (input.contains("4")){
            System.out.println("Email: ");
            String email = getInput();
            System.out.println("Username: ");
            String username = getInput();
            System.out.println("Password: ");
            String password = getInput();

            // run the register endpoint
            try {
                AuthData authData = server.register(username, password, email);
                this.authToken = authData.authToken();
                System.out.println("Registered successfully!");
                this.loggedIn = true;
            } catch (Exception ex) {
                System.out.println("Could not register the user with that username and password");
            }
        }
        return "";
    }

    private String getInput(){
        Scanner myScanner = new Scanner(System.in);
        return myScanner.nextLine();
    }

    private String loggedInMenu(){
        System.out.println();
        System.out.println("   1 - Help");
        System.out.println("   2 - Logout");
        System.out.println("   3 - Create Game");
        System.out.println("   4 - List Games");
        System.out.println("   5 - Play Game");
        System.out.println("   6 - Observe Game");
        return parseLoggedInMenu(getInput());
    }

    private String parseLoggedInMenu(String input){
        if (input.contains("1")){
            System.out.println("Enter the corresponding menu number to " +
                    "\n    log out of the application, " +
                    "\n    create a new chess game, " +
                    "\n    view existing chess games," +
                    "\n    join an existing chess game," +
                    "\n    or observe an existing chess game");
            // print out some helpful material
            return "";
        } else if (input.contains("2")){
            System.out.println("We're logging out...");
            // log out the user
            try {
                server.logout(this.authToken);
                this.authToken = "";
                this.loggedIn = false;
            } catch (Exception ex){
                System.out.println("The user could not be logged out of the system");
            }
            return "logged out";
        } else if (input.contains("3")){
            // create a game
            System.out.println("Please enter a name for your game:");
            String gameName = getInput();
            try {
                server.createGame(gameName, this.authToken);
            } catch (Exception ex){
                System.out.println("Invalid game name");
            }
        } else if (input.contains("4")){
            // list games
        } else if (input.contains("5")){
            // play game
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            DrawChessBoard myDrawer = new DrawChessBoard();
            myDrawer.drawBoard("BLACK", board);
        } else if (input.contains("6")){
            // observe game
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            DrawChessBoard myDrawer = new DrawChessBoard();
            myDrawer.drawBoard("WHITE", board);
        }
        return "";
    }

}
