package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int errorCode;
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
    public DataAccessException(String message, int errorCode){
        super(message);
        this.errorCode = errorCode;
    }
    public int getErrorCode(){
        return errorCode;
    }
}