package ws.hany.chatfirebaseandroid;

import java.io.Serializable;

public class InstantMessage implements Serializable {

    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstantMessage() {}

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

}
