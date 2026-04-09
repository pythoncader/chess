package client.websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notify(ServerMessage notification);
    void notifyError(ErrorMessage notification);
    void notifyLoadGame(LoadGameMessage notification);
}
