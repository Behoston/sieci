package tcp.server;

class Message {
    private Integer to;
    private Integer from;
    private String message;

    Message(Integer to, Integer from, String message) {
        this.to = to;
        this.from = from;
        this.message = message + '\n';
    }

    Integer getTo() {
        return to;
    }

    Integer getFrom() {
        return from;
    }

    String getMessage() {
        return message;
    }
}
