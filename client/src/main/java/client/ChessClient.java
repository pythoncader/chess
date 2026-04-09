package client;

import chess.*;

import java.util.*;

import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListofGames;
import ui.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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

    public ChessClient(int port) throws ResponseException {
        this.port = port;
        server = new ServerFacade(port);
    }

    public void run() throws ResponseException {
        System.out.println("Welcome to off-brand Chess.com\n");
        String message = "";
        while (!Objects.equals(message, "quit")) {
            if (!this.loggedIn) {
                message = mainMenu();
            } else {
                message = loggedInMenu();
                while (message.equals("gameMenu")) {
                    message = gameMenu();
                }
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
        if (input.length() > 1) {
            System.out.println("Please choose a menu number");
            mainMenu();
        }
        if (input.contains("1")) {
            // print out some helpful material
            System.out.println("Enter the corresponding menu number to " +
                    "\n    quit the application, " +
                    "\n    login to an existing account, " +
                    "\n    or register a new user");
            return "";
        } else if (input.contains("2")) {
            System.out.println("Quitting...");
            // quit the system
            return "quit";
        } else if (input.contains("3")) {
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
        } else if (input.contains("4")) {
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

    private String getInput() {
        Scanner myScanner = new Scanner(System.in);
        return myScanner.nextLine();
    }

    private String loggedInMenu() {
        System.out.println();
        System.out.println("   1 - Help");
        System.out.println("   2 - Logout");
        System.out.println("   3 - Create Game");
        System.out.println("   4 - List Games");
        System.out.println("   5 - Play Game");
        System.out.println("   6 - Observe Game");
        return parseLoggedInMenu(getInput());
    }

    private String parseLoggedInMenu(String input) {
        if (input.length() > 1) {
            System.out.println("Please choose a menu number");
            loggedInMenu();
        }
        if (input.contains("1")) {
            System.out.println("Enter the corresponding menu number to " +
                    "\n    log out of the application, " +
                    "\n    create a new chess game, " +
                    "\n    view existing chess games," +
                    "\n    join an existing chess game," +
                    "\n    or observe an existing chess game");
            // print out some helpful material
            return "";
        } else if (input.contains("2")) {
            System.out.println("Logging out...");
            // log out the user
            try {
                server.logout(this.authToken);
                this.authToken = "";
                this.loggedIn = false;
            } catch (Exception ex) {
                System.out.println("The user could not be logged out of the system");
            }
            return "logged out";
        } else if (input.contains("3")) {
            // create a game
            System.out.println("Please enter a name for your game:");
            String gameName = getInput();
            try {
                server.createGame(gameName, this.authToken);
            } catch (Exception ex) {
                System.out.println("Invalid game name");
            }
        } else if (input.contains("4")) {
            // list the games
            listGames(true);

        } else if (input.contains("5")) {
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
                    ws.connectSocket(this.authToken, latestGames.get(this.currentGameNum).gameID());

//                    drawGame(this.currentUserColor);
                    return "gameMenu";
                } catch (ResponseException ex) {
                    if (Objects.equals(ex.code(), ResponseException.Code.AlreadyTakenError)) {
                        System.out.println("Sorry, someone is already playing as the " + colorChoice.toLowerCase() + " player");
                    } else {
                        System.out.println("Could not add the user to the game");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Invalid game number");
            }
        } else if (input.contains("6")) {
            // observe game
            System.out.println("Which game do you want to observe? (enter the game number)");
            listGames(true);
            this.currentUserColor = "WHITE";
            try {
                this.currentGameNum = Integer.parseInt(getInput());
//                drawGame("WHITE");
            } catch (Exception ex) {
                System.out.println("Invalid game number");
            }
            try {
                ws = new WebSocketFacade(port, this);
                ws.observe(authToken, this.latestGames.get(this.currentGameNum).gameID());
            } catch (Exception ex) {
                System.out.println("There was a problem connecting to the game");
            }
            return "gameMenu";
        } else {
            System.out.println("Invalid menu option");
        }
        return "";
    }

    public void drawGame(String userColor) {
        ChessGame desiredGame = latestGames.get(this.currentGameNum).game();
        ChessBoard board = desiredGame.getBoard();
        System.out.println("\n" + latestGames.get(this.currentGameNum).gameName());
        DrawChessBoard myDrawer = new DrawChessBoard();
        myDrawer.drawBoard(userColor, board, null, null);
        listGames(false);
        String turn = this.latestGames.get(this.currentGameNum).game().getTeamTurn().toString().toLowerCase();
        if (!turn.equals("none")) {
            System.out.printf("It's %s's turn%n", turn);
        } else {
            System.out.println("The game is over");
        }
    }

    private void listGames(boolean print) {
        try {
            if (this.latestGames != null) {
                this.latestGames.clear();
            }
            ListofGames gameList = server.listGames(this.authToken);
//                System.out.println(gameList.games());
            int gameNum = 0;
            if (gameList.games().isEmpty()) {
                if (print) {
                    System.out.println("There are no current games");
                }
            }
            for (GameData game : gameList.games()) {
                gameNum++;
                if (print) {
                    System.out.println("Game " + gameNum + ": \"" + game.gameName() + "\" - white player: "
                            + game.whiteUsername() + ", black player: " + game.blackUsername() + ", next turn: " + game.game().getTeamTurn());
                }
                this.latestGames.put(gameNum, game);
            }
        } catch (Exception ex) {
            System.out.println("Could not list the games ");
        }
    }

    private String gameMenu() {
        printGameMenu();
        return parseGameMenu(getInput());
    }

    private static void printGameMenu() {
        System.out.println();
        System.out.println("   1 - Help");
        System.out.println("   2 - Redraw Chess Board");
        System.out.println("   3 - Leave");
        System.out.println("   4 - Make Move");
        System.out.println("   5 - Resign");
        System.out.println("   6 - Highlight Legal Moves");
    }
    private String parseGameMenu(String input) {
        if (input.length() > 1) {
            System.out.println("Please choose a menu number");
            return "gameMenu";
        }
        if (input.contains("1")) {
            // print out some helpful material
            System.out.println("Enter the corresponding menu number to " +
                    "\n    draw the chessboard again, " +
                    "\n    leave the game, " +
                    "\n    make a chess move in the game," +
                    "\n    resign and forfeit the game," +
                    "\n    or see all legal moves for a piece");
            return "gameMenu";
        } else if (input.contains("2")) {
            // redraw the chess board
            drawGame(this.currentUserColor);
            return "gameMenu";
        } else if (input.contains("3")) {
            // leave the game
            System.out.println("You have left the game");
            try {
                ws.leave(this.authToken, latestGames.get(this.currentGameNum).gameID());
            } catch (ResponseException ex) {
                System.out.println("There was a problem sending a message to the other users");
            }
            return "exit game menu";
        } else if (input.contains("4")) {
            // make a chess move
            System.out.println("Which piece do you want to move? Enter the piece location (e.g. a2):");
            String pieceLocation = getInput();
            while (pieceLocation.isEmpty()) {
                System.out.println("Please enter a valid location on the chessboard");
                pieceLocation = getInput();
            }
            while (pieceLocation.length() > 2
                    | !chessColumns.containsKey(String.valueOf(pieceLocation.charAt(0)))
                    | !Character.isDigit(pieceLocation.charAt(1))) {
                System.out.println("Please enter a valid location on the chessboard");
                pieceLocation = getInput();
            }
            ChessPosition piecePosition;
            try {
                int col = chessColumns.get(String.valueOf(pieceLocation.charAt(0)));
                int row = Integer.parseInt(String.valueOf(pieceLocation.charAt(1)));
//            System.out.println("col: "+col+"\nrow: "+row);
                piecePosition = new ChessPosition(row, col);
                highlightMoves(pieceLocation);
            } catch (Exception ex) {
                System.out.println("Invalid piece location");
                return "gameMenu";
            }
            System.out.println("Where do you want to move to? Enter the board location (e.g. a4):");
            String moveToLocation = getInput();
            while (moveToLocation.isEmpty()) {
                System.out.println("Please enter a valid location on the chessboard");
                moveToLocation = getInput();
            }
            while (moveToLocation.length() > 2
                    | !chessColumns.containsKey(String.valueOf(moveToLocation.charAt(0)))
                    | !Character.isDigit(moveToLocation.charAt(1))) {
                System.out.println("Please enter a valid location on the chessboard");
                moveToLocation = getInput();
            }
            try {
                makeMove(moveToLocation, piecePosition, pieceLocation);
                return "gameMenu";
            } catch (ResponseException ex) { // for errors sending out messages to the other users
                System.out.println(ex.getMessage());
                return "gameMenu";
            } catch (Exception ex) {
                System.out.println("Invalid piece location" + ex.getMessage());
                return "gameMenu";
            }
        } else if (input.contains("5")) {
            // resign the game
            resign();
            return "gameMenu";
        } else if (input.contains("6")) {
            // highlight the legal moves
            highlightMoves();
            return "gameMenu";
        } else {
            System.out.println("Invalid menu option");
            return "gameMenu";
        }
    }
    private void makeMove(String moveToLocation, ChessPosition piecePosition, String pieceLocation) throws ResponseException {
        int col = chessColumns.get(String.valueOf(moveToLocation.charAt(0)));
        int row = Integer.parseInt(String.valueOf(moveToLocation.charAt(1)));

        ChessPosition moveToPosition = new ChessPosition(row, col);
        String promotionPieceString = null;
        String moveString = String.format(" moved the %s at %s to %s",
                this.latestGames.get(this.currentGameNum).game().getBoard().getPiece(piecePosition).toString().toLowerCase(),
                pieceLocation,
                moveToLocation
        );
        if (this.latestGames.get(
                this.currentGameNum).game().getBoard().getPiece(piecePosition).toString().equalsIgnoreCase("pawn")) {
            if (moveToPosition.getRow() == 8 || moveToPosition.getRow() == 1) {
                promotionPieceString = getString(promotionPieceString);
                moveString = moveString + " and promoted it to a " + promotionPieceString;
            }
        }
        ws.makeMove(
                authToken,
                this.latestGames.get(this.currentGameNum).gameID(),
                moveString,
                piecePosition, moveToPosition,
                promotionPieceString);
        listGames(false);
//        drawGame(currentUserColor);
    }
    private String getString(String promotionPieceString) {
        boolean invalid = true;
        while (invalid) {
            System.out.println("What kind of piece would you like to convert your pawn into?");
            try {
                promotionPieceString = getInput();
                ArrayList<String> validPieces = new ArrayList<>(List.of("knight", "bishop", "rook", "queen"));
                if (validPieces.contains(promotionPieceString.toLowerCase())) {
                    invalid = false;
                } else {
                    System.out.println("Please type out the name of the piece:");
                }
            } catch (Exception ex) {
                System.out.println("Please type out the name of the piece:");
            }
        }
        return promotionPieceString;
    }
    private void highlightMoves() {
        System.out.println("Enter the piece location (e.g. c8):");
        String pieceLocation = getInput();
        while (pieceLocation.length() > 2
                | !chessColumns.containsKey(String.valueOf(pieceLocation.charAt(0)))
                | !Character.isDigit(pieceLocation.charAt(1))) {
            System.out.println("Please enter a valid location on the chessboard");
            pieceLocation = getInput();
        }
        try {
            highlightMoves(pieceLocation);
        } catch (Exception ex) {
            System.out.println("Could not highlight the chessboard");
        }
    }

    private void resign() {
        System.out.println("Are you sure you want to resign? (y/n)");
        String choice = getInput();
        if (choice.equals("y")) {
            listGames(false); // update the list of latest games so that it is up to date
            if (latestGames.get(this.currentGameNum).game().getTeamTurn() != ChessGame.TeamColor.NONE) {
                try {
                    ws.resign(this.authToken, latestGames.get(this.currentGameNum).gameID());
                    System.out.println("You have forfeited the game");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    // make this better!
                }
            } else {
                System.out.println("The game is already over, you cannot resign!");
            }
        }
    }
    private void highlightMoves(String pieceLocation) {
        int col = chessColumns.get(String.valueOf(pieceLocation.charAt(0)));
        int row = Integer.parseInt(String.valueOf(pieceLocation.charAt(1)));
//        System.out.println("col: "+col+"\nrow: "+row);
        ChessGame desiredGame = latestGames.get(this.currentGameNum).game();
        ChessBoard board = desiredGame.getBoard();
        ChessPosition piecePosition = new ChessPosition(row, col);
        Collection<ChessPosition> possibleMoves = getValidMoves(desiredGame, piecePosition);
        System.out.println("\n" + latestGames.get(this.currentGameNum).gameName());
        DrawChessBoard myDrawer = new DrawChessBoard();
        myDrawer.drawBoard(this.currentUserColor, board, possibleMoves, piecePosition);
    }
    private static Collection<ChessPosition> getValidMoves(ChessGame desiredGame, ChessPosition piecePosition) {
        Collection<ChessMove> possibleChessMoves = desiredGame.validMoves(piecePosition);
        if (possibleChessMoves != null) {
            Collection<ChessPosition> possibleMoves = new ArrayList<>();
            for (ChessMove move : possibleChessMoves) {
                possibleMoves.add(move.getEndPosition());
            }
            return possibleMoves;
        } else {
            System.out.println("returned null");
            return null;
        }
    }
    private final Map<String, Integer> chessColumns = Map.of("a", 1, "b", 2, "c", 3, "d", 4, "e", 5, "f", 6, "g", 7, "h", 8);
    @Override
    public void notify(ServerMessage notification) {
        listGames(false);
        System.out.println("\n" + notification.getMessage());
    }

    @Override
    public void notifyError(ErrorMessage notification) {
        listGames(false);
        System.out.println("\n" + notification.getErrorMessage());
    }

    @Override
    public void notifyLoadGame(LoadGameMessage notification){
        listGames(false);
        drawGame(this.currentUserColor);
        printGameMenu();
    }
}