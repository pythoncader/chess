package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConnectionManager {
    public final HashMap<Integer, Set<Session>> connections = new HashMap<>();


    public void add(int gameID, Session session) {
        if (connections.get(gameID) == null){
            Set<Session> sessions = new HashSet<>();
            sessions.add(session);
            connections.put(gameID, sessions);
        } else {
            connections.get(gameID).add(session);
        }
    }

    public void remove(int gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void sendLoadGameMessage(Session session, LoadGameMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
    public void broadcastLoadGameMessage(int gameID, Session excludeSession, LoadGameMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        sendMessages(gameID, excludeSession, msg);
    }

    private void sendMessages(int gameID, Session excludeSession, String msg) throws IOException {
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        sendMessages(gameID, excludeSession, msg);
    }
}
