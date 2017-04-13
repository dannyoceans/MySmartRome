package activity.chat;

/**
 * Created by francesconi on 15/03/2017.
 */


public class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }
}