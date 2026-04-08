package ui;

import chess.*;

import java.util.Collection;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class DrawChessBoard {

    public static void drawBoard(String userColor, ChessBoard board, Collection<ChessPosition> possibleMoves, ChessPosition piecePosition){
        System.out.print(ERASE_SCREEN);
//        System.out.println("\n");
        if (userColor.equals("WHITE")) {
            whitePerspective(board, possibleMoves, piecePosition);
        } else {
            blackPerspective(board, possibleMoves, piecePosition);
        }
        System.out.print("\u001b[0m"); // clears all formatting after printing the game

    }

    private static void blackPerspective(ChessBoard board, Collection<ChessPosition> possibleMoves, ChessPosition piecePosition) {
        drawHeaderBlack();
        for (int row = 1; row < 9; row=row+2){
            drawRow("BLACK", row, board, "WHITE", possibleMoves, piecePosition);
            drawRow("BLACK", row+1, board, "GREEN", possibleMoves, piecePosition);
        }
        drawHeaderBlack();
    }

    private static void whitePerspective(ChessBoard board, Collection<ChessPosition> possibleMoves, ChessPosition piecePosition) {
        drawHeaderWhite();
        for (int row = 8; row > 0; row=row-2){
            drawRow("WHITE", row, board, "WHITE", possibleMoves, piecePosition);
            drawRow("WHITE", row-1, board, "GREEN", possibleMoves, piecePosition);
        }
        drawHeaderWhite();
    }
    private static void drawHeaderWhite(){
        drawRectangle(" ", "other_gray");
        drawRectangle("a", "other_gray");
        drawRectangle("b", "other_gray");
        drawRectangle("c", "other_gray");
        drawRectangle("d", "other_gray");
        drawRectangle("e", "other_gray");
        drawRectangle("f", "other_gray");
        drawRectangle("g", "other_gray");
        drawRectangle("h", "other_gray");
        drawRectangle(" ", "other_gray");
        System.out.print("\u001b[0m"); // clears formatting
        System.out.println();
    }

    private static void drawHeaderBlack(){
        drawRectangle(" ", "other_gray");
        drawRectangle("h", "other_gray");
        drawRectangle("g", "other_gray");
        drawRectangle("f", "other_gray");
        drawRectangle("e", "other_gray");
        drawRectangle("d", "other_gray");
        drawRectangle("c", "other_gray");
        drawRectangle("b", "other_gray");
        drawRectangle("a", "other_gray");
        drawRectangle(" ", "other_gray");
        System.out.print("\u001b[0m"); // clears formatting
        System.out.println();
    }

    private static void drawRectangle(String piece, String color) {
        String backgroundColor;
        String textColor = SET_TEXT_COLOR_BLACK;
        switch (color) {
            case "green" -> backgroundColor = SET_BG_COLOR_GREEN;
            case "gray" -> backgroundColor = SET_BG_COLOR_GRAY;
            case "highlighted gray" -> backgroundColor = SET_BG_COLOR_LIGHT_GREY;
            case "highlighted green" -> backgroundColor = SET_BG_COLOR_DARK_GREEN;
            case "yellow green", "yellow gray" -> backgroundColor = SET_BG_COLOR_YELLOW;
            default -> {
                backgroundColor = SET_BG_COLOR_DARK_GREY;
                textColor = SET_TEXT_COLOR_GRAY;
            }
        }
        System.out.print(backgroundColor);
        System.out.print(textColor);

        System.out.print(" ".repeat(1));
        System.out.print(textColor);
        System.out.print(piece);
        System.out.print(textColor);
        System.out.print(" ".repeat(1));
    }

    private static String getPieceString(ChessPiece currentPiece, String myPiece) {
        ChessPiece.PieceType pieceType;
        if (currentPiece != null){
            if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                pieceType = currentPiece.getPieceType();
                if (pieceType == ChessPiece.PieceType.PAWN){
                    myPiece = WHITE_PAWN;
                } else if (pieceType == ROOK){
                    myPiece = WHITE_ROOK;
                } else if (pieceType == KNIGHT){
                    myPiece = WHITE_KNIGHT;
                } else if (pieceType == BISHOP){
                    myPiece = WHITE_BISHOP;
                } else if (pieceType == QUEEN){
                    myPiece = WHITE_QUEEN;
                } else if (pieceType == KING) {
                    myPiece = WHITE_KING;
                }
            } else {
                pieceType = currentPiece.getPieceType();
                if (pieceType == ChessPiece.PieceType.PAWN){
                    myPiece = BLACK_PAWN;
                } else if (pieceType == ROOK){
                    myPiece = BLACK_ROOK;
                } else if (pieceType == KNIGHT){
                    myPiece = BLACK_KNIGHT;
                } else if (pieceType == BISHOP){
                    myPiece = BLACK_BISHOP;
                } else if (pieceType == QUEEN){
                    myPiece = BLACK_QUEEN;
                } else if (pieceType == KING) {
                    myPiece = BLACK_KING;
                }
            }
        } else {
            myPiece = " ";
        }
        return myPiece;
    }

    private static void drawRow(String identifier,
            int row,
            ChessBoard chessBoard,
            String startColor,
            Collection<ChessPosition> possibleMoves,
            ChessPosition piecePosition
    )
    {
        drawRectangle(Integer.toString(row), "other_gray");
        String currentColor;
        if (Objects.equals(startColor, "WHITE")){
            currentColor = "gray";
        } else {
            currentColor = "green";
        }
        ChessPiece currentPiece;
        ChessPosition currentPosition;
        String myPiece = " ";
        if (identifier.equals("BLACK")) {
            for (int col = 8; col > 0; col--) {
                currentPosition = new ChessPosition(row, col);
                currentColor = highlightSquare(possibleMoves, currentPosition, piecePosition, currentColor);
                currentPiece = chessBoard.getPiece(currentPosition);

                myPiece = getPieceString(currentPiece, myPiece);

                drawRectangle(myPiece, currentColor);
                currentColor = changeColor(currentColor);
            }
        } else {
            for (int col = 1; col < 9; col++){
                currentPosition = new ChessPosition(row, col);
                currentColor = highlightSquare(possibleMoves, currentPosition, piecePosition, currentColor);
                currentPiece = chessBoard.getPiece(currentPosition);

                myPiece = getPieceString(currentPiece, myPiece);

                drawRectangle(myPiece, currentColor);
                currentColor = changeColor(currentColor);
            }
        }
        drawRectangle(Integer.toString(row), "other_gray");
        System.out.print("\u001b[0m"); // clears formatting
        System.out.println();
    }

    private static String highlightSquare(
            Collection<ChessPosition> possibleMoves,
            ChessPosition currentPosition,
            ChessPosition piecePosition,
            String currentColor
        )
    {
        if (possibleMoves != null) {
            if (possibleMoves.contains(currentPosition)) {
//                System.out.println("hey guys we should be highlighting right now");
                if (currentColor.equals("gray")) {
                    currentColor = "highlighted gray";
                } else if (currentColor.equals("green")) {
                    currentColor = "highlighted green";
                }
            } else if (currentPosition.equals(piecePosition)){
                if (currentColor.equals("gray")) {
                    currentColor = "yellow gray";
                } else if (currentColor.equals("green")) {
                    currentColor = "yellow green";
                }
            }
        }
        return currentColor;
    }

    private static String changeColor(String currentColor) {
        if (currentColor.equals("green") || currentColor.equals("highlighted green") || currentColor.equals("yellow green")){
            currentColor = "gray";
        } else if (currentColor.equals("gray") || currentColor.equals("highlighted gray") || currentColor.equals("yellow gray")){
            currentColor = "green";
        }
        return currentColor;
    }
}