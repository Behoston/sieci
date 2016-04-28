package tcp;

public class Message {
    private Integer to;
    private Integer from;
    private String message;

    public Message(Integer to, Integer from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
