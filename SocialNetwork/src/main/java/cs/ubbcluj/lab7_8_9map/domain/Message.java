package cs.ubbcluj.lab7_8_9map.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long> {
    private Long id1, id2;
    private LocalDateTime dateMessage;

    private String message;

    private Long idReply;

    private List<Utilizator> toList;

    public void setIdReply(Long idReply) {
        this.idReply = idReply;
    }

    public void setToList(List<Utilizator> toList) {
        this.toList = toList;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public LocalDateTime getDateMessage() {
        return dateMessage;
    }

    public String getMessage() {
        return message;
    }

    public Long getIdReply() {
        return idReply;
    }

    public List<Utilizator> getToList() {
        return toList;
    }

    public Message(Long idMessage, Long id1, Long id2, LocalDateTime dateMessage, String message, Long reply) {
        super();
        this.setId(idMessage);
        this.id1 = id1;
        this.id2 = id2;
        this.dateMessage = dateMessage;
        this.message = message;
        this.idReply = reply;
        this.toList = new ArrayList<>();
    }
}
