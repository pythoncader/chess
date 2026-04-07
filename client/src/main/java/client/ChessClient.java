package client;

import chess.ChessBoard;

import java.util.*;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListofGames;
import ui.*;
import websocket.messages.ServerMessage;


public class ChessClient implements ServerMessageHandler {
    private final int port;
    private boolean loggedIn = false;
    private String authToken = "";
    private final ServerFacade server;
    private WebSocketFacade ws = null;
    private HashMap<Integer, GameData> latestGames = new HashMap<>();
    private int currentGameNum = -1;
    private String currentUserColor = "";
    private HashMap<Integer, GameData> gamesOver = new HashMap<>();

    public ChessClient(int port) throws ResponseException {
        this.port = port;
        server = new ServerFacade(port);
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
            System.out.println("Quitting...");
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
            System.out.println("Logging out...");
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
            listGames(true);

        } else if (input.contains("5")){
            // join a game
            System.out.println("Which game do you want to join? (enter the game number)");
            listGames(true);
            try {
                this.currentGameNum = Integer.parseInt(getInput());
                ChessGame desiredGame = latestGames.get(this.currentGameNum).game();
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
                    server.addToGame(latestGames.get(this.currentGameNum).gameID(), colorChoice, this.authToken);
                    ws = new WebSocketFacade(port, this);
                    this.currentUserColor = colorChoice;
                    ws.connectSocket(this.authToken, latestGames.get(this.currentGameNum).gameID(), this.currentUserColor);

                    ChessBoard board = desiredGame.getBoard();

                    System.out.println("\n\n          "+latestGames.get(this.currentGameNum).gameName());
                    DrawChessBoard myDrawer = new DrawChessBoard();
                    myDrawer.drawBoard(colorChoice, board, null, null);
                    gameMenu();
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
            listGames(true);
            try {
                this.currentGameNum = Integer.parseInt(getInput());
                drawGame("WHITE");
            } catch (Exception ex){
                System.out.println("Invalid game number");
            }
        } else {
            System.out.println("Invalid menu option");
        }
        return "";
    }

    public void drawGame(String userColor) {
        ChessGame desiredGame = latestGames.get(this.currentGameNum).game();
        ChessBoard board = desiredGame.getBoard();
        System.out.println("\n\n          " + latestGames.get(this.currentGameNum).gameName());
        DrawChessBoard myDrawer = new DrawChessBoard();
        myDrawer.drawBoard(userColor, board, null, null);
    }

    private void listGames(boolean print) {
        try {
            if (this.latestGames != null) {
                this.latestGames.clear();
            }
            ListofGames gameList = server.listGames(this.authToken);
//                System.out.println(gameList.games());
            int gameNum = 0;
            if (gameList.games().isEmpty()){
                if (print) {
                    System.out.println("There are no current games");
                }
            }
            for (GameData game : gameList.games()){
                gameNum++;
                if (print) {
                    System.out.println("Game " + gameNum + ": \"" + game.gameName() + "\" - white player: "
                            + game.whiteUsername() + ", black player: " + game.blackUsername() + ", nextTurn: " + game.game().getTeamTurn());
                }
                this.latestGames.put(gameNum, game);
            }
        } catch (Exception ex){
            System.out.println("Could not list the games ");
        }
    }

    private String gameMenu() {
        System.out.println();
        System.out.println("   1 - Help");
        System.out.println("   2 - Redraw Chess Board");
        System.out.println("   3 - Leave");
        System.out.println("   4 - Make Move");
        System.out.println("   5 - Resign");
        System.out.println("   6 - Highlight Legal Moves");
        return parseGameMenu(getInput());
    }

    private String parseGameMenu(String input){
        if (input.length() > 1){
            System.out.println("Please choose a menu number");
            return "";
        }
        if (input.contains("1")){
            // print out some helpful material
            System.out.println("Enter the corresponding menu number to " +
                    "\n    draw the chessboard again, " +
                    "\n    leave the game, " +
                    "\n    make a chess move in the game," +
                    "\n    resign and forfeit the game," +
                    "\n    or see all legal moves for a piece");
            gameMenu();
        } else if (input.contains("2")){
            // redraw the chess board
            drawGame(this.currentUserColor);
            gameMenu();
        } else if (input.contains("3")){
            // leave the game
            System.out.println("You have left the game");
            try {
                ws.leave(this.authToken, latestGames.get(this.currentGameNum).gameID(), currentUserColor);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
                // make this better!
            }
        } else if (input.contains("4")){
            // make a chess move
        } else if (input.contains("5")){
            // resign the game
            System.out.println("Are you sure you want to resign? (y/n)");
            String choice = getInput();
            if (choice.equals("y")){
                listGames(false); // update the list of latest games so that it is up to date
                if (latestGames.get(this.currentGameNum).game().getTeamTurn() != ChessGame.TeamColor.NONE) {
                    System.out.println("You have quit the game");
                    try {
                        ws.resign(this.authToken, latestGames.get(this.currentGameNum).gameID());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        // make this better!
                    }
                } else {
                    System.out.println("The game is already over, you cannot resign!");
                }
            }
            gameMenu();
        } else if (input.contains("6")){
            // highlight the legal moves
            System.out.println("Enter the piece location (e.g. c8):");
            String pieceLocation = getInput();

            while (pieceLocation.length() > 2
                    | !chessColumnsWhite.containsKey(String.valueOf(pieceLocation.charAt(0)))
                    | !Character.isDigit(pieceLocation.charAt(1))) {
                System.out.println("Please enter a valid location on the chessboard");
                pieceLocation = getInput();
            }
            try {
                int col;
                if (this.currentUserColor.equals("WHITE")) {
                    col = chessColumnsWhite.get(String.valueOf(pieceLocation.charAt(0)));
                } else {
                    col = chessColumnsBlack.get(String.valueOf(pieceLocation.charAt(0)));
                }
                int row = Integer.parseInt(String.valueOf(pieceLocation.charAt(1)));
                System.out.println("col: "+col+"\nrow: "+row);
                ChessGame desiredGame = latestGames.get(this.currentGameNum).game();
                ChessBoard board = desiredGame.getBoard();
                ChessPosition piecePosition = new ChessPosition(row, col);

                Collection<ChessPosition> possibleMoves = getValidMoves(desiredGame, piecePosition);

                System.out.println("\n\n          " + latestGames.get(this.currentGameNum).gameName());
                DrawChessBoard myDrawer = new DrawChessBoard();
                myDrawer.drawBoard(this.currentUserColor, board, possibleMoves, piecePosition);
                gameMenu();
            } catch (Exception ex){
                System.out.println("Invalid game number");
            }
        } else {
            System.out.println("Invalid menu option");
        }
        return "";
    }

    private static Collection<ChessPosition> getValidMoves(ChessGame desiredGame, ChessPosition piecePosition) {
        Collection<ChessMove> possibleChessMoves = desiredGame.validMoves(piecePosition);
        if (possibleChessMoves != null){
            Collection<ChessPosition> possibleMoves = new ArrayList<>();
            for (ChessMove move : possibleChessMoves){
                possibleMoves.add(move.getEndPosition());
            }
            System.out.println(possibleMoves);
            return possibleMoves;
        } else {
            System.out.println("returned null");
            return null;
        }
    }

    private final Map<String, Integer> chessColumnsWhite = Map.of("a", 1, "b", 2, "c", 3, "d", 4, "e", 5, "f", 6, "g", 7, "h", 8);
    private final Map<String, Integer> chessColumnsBlack = Map.of("a", 8, "b", 7, "c", 6, "d", 5, "e", 4, "f", 3, "g", 2, "h", 1);

    @Override
    public void notify(ServerMessage notification) {
        if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)){
            drawGame(this.currentUserColor);
        }
        System.out.println(notification.getMessage());
    }
}
