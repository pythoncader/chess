package websocket.messages;

public class ErrorMessage extends ServerMessage{
    public String getErrorMessage() {
        return errorMessage;
    }

    private final String errorMessage;
    public ErrorMessage(String message, ServerMessageType type, String errorMessage) {
        super(message, type);
        this.errorMessage = errorMessage;
    }
}
