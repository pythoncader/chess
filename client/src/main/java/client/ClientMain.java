package client;

import chess.*;
import exception.ResponseException;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new ChessClient(port).run();
        } catch (ResponseException ex) {
            System.out.println(ex.code());
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
