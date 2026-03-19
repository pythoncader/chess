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
        System.out.println("\n");
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
        System.out.print(SET_BG_COLOR_BLACK);
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
        System.out.print(SET_BG_COLOR_BLACK);
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
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.println();
//        drawRectangle(WHITE_ROOK, "gray");
//        drawRectangle(WHITE_KNIGHT, "green");
//        drawRectangle(WHITE_BISHOP, "gray");
//        drawRectangle(WHITE_KING, "green");
//        drawRectangle(WHITE_QUEEN, "gray");
//        drawRectangle(WHITE_BISHOP, "green");
//        drawRectangle(WHITE_KNIGHT, "gray");
//        drawRectangle(WHITE_ROOK, "green");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(WHITE_PAWN, "green");
//        drawRectangle(WHITE_PAWN, "gray");
//        drawRectangle(WHITE_PAWN, "green");
//        drawRectangle(WHITE_PAWN, "gray");
//        drawRectangle(WHITE_PAWN, "green");
//        drawRectangle(WHITE_PAWN, "gray");
//        drawRectangle(WHITE_PAWN, "green");
//        drawRectangle(WHITE_PAWN, "gray");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//        drawRectangle(" ", "green");
//        drawRectangle(" ", "gray");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//
//        drawRectangle(BLACK_PAWN, "gray");
//        drawRectangle(BLACK_PAWN, "green");
//        drawRectangle(BLACK_PAWN, "gray");
//        drawRectangle(BLACK_PAWN, "green");
//        drawRectangle(BLACK_PAWN, "gray");
//        drawRectangle(BLACK_PAWN, "green");
//        drawRectangle(BLACK_PAWN, "gray");
//        drawRectangle(BLACK_PAWN, "green");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();
//
//        drawRectangle(BLACK_ROOK, "green");
//        drawRectangle(BLACK_KNIGHT, "gray");
//        drawRectangle(BLACK_BISHOP, "green");
//        drawRectangle(BLACK_KING, "gray");
//        drawRectangle(BLACK_QUEEN, "green");
//        drawRectangle(BLACK_BISHOP, "gray");
//        drawRectangle(BLACK_KNIGHT, "green");
//        drawRectangle(BLACK_ROOK, "gray");
//
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.println();

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