package client;

import chess.ChessBoard;

import java.util.Objects;
import java.util.Scanner;

import exception.ResponseException;
import server.ServerFacade;
import ui.*;

public class ChessClient {
    private boolean loggedIn = false;
    private final ServerFacade server;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run() throws ResponseException {
        System.out.println("Welcome to off-brand Chess.com\n");
        String message = "";
        while (!Objects.equals(message, "quit")){
            if (!loggedIn) {
                message = mainMenu();
            } else {
                message = loggedInMenu();
            }
        }
    }

    public String mainMenu() throws ResponseException {
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("   1 - Help");
        System.out.println("   2 - Quit");
        System.out.println("   3 - Login");
        System.out.println("   4 - Register");
        System.out.println();
        return parseMainMenu(getInput());
    }

    public String parseMainMenu(String input) throws ResponseException {
        if (input.contains("1")){
            System.out.println("We're helping...");
            // print out some helpful material
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
            loggedIn = true;
        } else if (input.contains("4")){
            System.out.println("Email: ");
            String email = getInput();
            System.out.println("Username: ");
            String username = getInput();
            System.out.println("Password: ");
            String password = getInput();

            // run the register endpoint
            try {
                server.register(username, password, email);
                loggedIn = true;
            } catch (Exception ex) {
                System.out.println("Could not register the user with that username and password");
            }
        }
        return "";
    }

    public String getInput(){
        Scanner myScanner = new Scanner(System.in);
        return myScanner.nextLine();
    }

    public String loggedInMenu(){
        System.out.println();
        System.out.println("   1 - Help");
        System.out.println("   2 - Logout");
        System.out.println("   3 - Create Game");
        System.out.println("   4 - List Games");
        System.out.println("   5 - Play Game");
        System.out.println("   6 - Observe Game");
        System.out.println("\n");
        return parseLoggedInMenu(getInput());
    }

    public String parseLoggedInMenu(String input){
        if (input.contains("1")){
            System.out.println("We're helping...");
            // print out some helpful material
        } else if (input.contains("2")){
            System.out.println("We're logging out...");
            // log out the user
            loggedIn = false;
            return "logged out";
        } else if (input.contains("3")){
            // create a game
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
