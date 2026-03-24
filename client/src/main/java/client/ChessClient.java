package client;

import chess.ChessBoard;

import java.util.*;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListofGames;
import server.ServerFacade;
import ui.*;

public class ChessClient {
    private boolean loggedIn = false;
    private String authToken = "";
    private final ServerFacade server;
    private HashMap<Integer, GameData> latestGames = new HashMap<>();

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
        if (input.length() > 1){
            System.out.println("Please choose a menu number");
            return "";
        }
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
        } else {
            System.out.println("Invalid menu option");
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
        if (input.length() > 1){
            System.out.println("Please choose a menu number");
            return "";
        }
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
            listGames();
        } else if (input.contains("5")){
            System.out.println("Which game do you want to join? (enter the game number)");
            listGames();
            try {
                int gameNum = Integer.parseInt(getInput());
                ChessGame desiredGame = latestGames.get(gameNum).game();
                System.out.println("Would you like to join as the white or the black player?");

                String colorChoice = "";
                while (!colorChoice.equals("BLACK") && !colorChoice.equals("WHITE")) {
                    colorChoice = getInput();
                    if (colorChoice.contains("b") || colorChoice.contains("B")) {
                        colorChoice = "BLACK";
                    } else if (colorChoice.contains("w") || colorChoice.contains("W")) {
                        colorChoice = "WHITE";
                    } else {
                        System.out.println("Please enter your choice of \"black\" or \"white\"");
                    }
                }

                try {
                    server.addToGame(latestGames.get(gameNum).gameID(), colorChoice, this.authToken);
                    ChessBoard board = desiredGame.getBoard();
                    // play game
//                ChessBoard board = new ChessBoard();
//                board.resetBoard();
                    System.out.println("\n\n          "+latestGames.get(gameNum).gameName());
                    DrawChessBoard myDrawer = new DrawChessBoard();
                    myDrawer.drawBoard(colorChoice, board);
                } catch (ResponseException ex){
                    if (Objects.equals(ex.code(), ResponseException.Code.AlreadyTakenError)){
                        System.out.println("Sorry, someone is already playing as the "+colorChoice.toLowerCase()+" player");
                    } else {
                        System.out.println("Could not add the user to the game");
                    }
                }
            } catch (Exception ex){
                System.out.println("Invalid game number");
            }

        } else if (input.contains("6")){
            // observe game
            System.out.println("Which game do you want to observe? (enter the game number)");
            listGames();
            try {
                int gameNum = Integer.parseInt(getInput());
                ChessGame desiredGame = latestGames.get(gameNum).game();
                ChessBoard board = desiredGame.getBoard();
                System.out.println("\n\n          " + latestGames.get(gameNum).gameName());
                DrawChessBoard myDrawer = new DrawChessBoard();
                myDrawer.drawBoard("WHITE", board);
            } catch (Exception ex){
                System.out.println("Invalid game number");
            }
        } else {
            System.out.println("Invalid menu option");
        }
        return "";
    }

    private void listGames() {
        try {
            if (this.latestGames != null) {
                this.latestGames.clear();
            }
            ListofGames gameList = server.listGames(this.authToken);
//                System.out.println(gameList.games());
            int gameNum = 0;
            if (gameList.games().isEmpty()){
                System.out.println("There are no current games");
            }
            for (GameData game : gameList.games()){
                gameNum++;
                System.out.println("Game " + gameNum + ": \"" + game.gameName() + "\" - white player: "
                        + game.whiteUsername() + ", black player: "+ game.blackUsername());
                this.latestGames.put(gameNum, game);
            }
        } catch (Exception ex){
            System.out.println("Could not list the games "+ex.getMessage());
        }
    }

}
