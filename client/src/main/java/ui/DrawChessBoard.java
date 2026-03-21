package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class DrawChessBoard {

    public static void drawBoard(String userColor, ChessBoard board){
        System.out.print(ERASE_SCREEN);
        System.out.println("\n");
        if (userColor.equals("WHITE")) {
            whitePerspective(board);
        } else {
            blackPerspective(board);
        }
        System.out.print("\u001b[0m"); // clears all formatting after printing the game
    }

    private static void blackPerspective(ChessBoard board) {
        drawHeaderBlack();
        for (int row = 1; row < 9; row=row+2){
            drawRow(row, board, "WHITE");
            drawRow(row+1, board, "GREEN");
        }
        drawHeaderBlack();
    }

    private static void whitePerspective(ChessBoard board) {
        drawHeaderWhite();
        for (int row = 8; row > 0; row=row-2){
            drawRow(row, board, "WHITE");
            drawRow(row-1, board, "GREEN");
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
        String textColor;
        if (color.equals("green")){
            backgroundColor = SET_BG_COLOR_GREEN;
            textColor = SET_TEXT_COLOR_BLACK;
        } else if (color.equals("gray")){
            backgroundColor = SET_BG_COLOR_GRAY;
            textColor = SET_TEXT_COLOR_BLACK;
        } else {
            backgroundColor = SET_BG_COLOR_DARK_GREY;
            textColor = SET_TEXT_COLOR_GRAY;
        }
        System.out.print(backgroundColor);
        System.out.print(textColor);

        System.out.print(" ".repeat(1));
        System.out.print(textColor);
        System.out.print(piece);
        System.out.print(textColor);
        System.out.print(" ".repeat(1));
    }

    private static void drawRow(int row, ChessBoard chessBoard, String startColor) {
        drawRectangle(Integer.toString(row), "other_gray");
        String currentColor;
        if (Objects.equals(startColor, "WHITE")){
            currentColor = "gray";
        } else {
            currentColor = "green";
        }
        ChessPiece currentPiece;
        String myPiece = " ";
        ChessPiece.PieceType pieceType;
        for (int col = 1; col < 9; col++){
            currentPiece = chessBoard.getPiece(new ChessPosition(row, col));

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

            drawRectangle(myPiece, currentColor);
            currentColor = changeColor(currentColor);
        }
        drawRectangle(Integer.toString(row), "other_gray");
        System.out.print("\u001b[0m"); // clears formatting
        System.out.println();
    }

    private static String changeColor(String currentColor) {
        if (currentColor.equals("green")){
            currentColor = "gray";
        } else if (currentColor.equals("gray")){
            currentColor = "green";
        }
        return currentColor;
    }
}