package CreateLevel;

public class InvalidMapException extends Exception {
    /**
     * Sets the message of the exception
     *
     * @param message the message of the exception
     */
    public InvalidMapException(String message){
        super(message);
    }
}
