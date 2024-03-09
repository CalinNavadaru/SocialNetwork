package cs.ubbcluj.lab7_8_9map.domain.dto;

import cs.ubbcluj.lab7_8_9map.domain.Entity;
import cs.ubbcluj.lab7_8_9map.domain.Utilizator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DTOMessage extends Entity<Long> {

    private final Long id1;
    private final Long id2;
    private final LocalDateTime dateMessage;

    private final String message;

    private final Long reply;

    private final List<Utilizator> toList;

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

    public Long getReply() {
        return reply;
    }

    public List<Utilizator> getToList() {
        return toList;
    }

    public DTOMessage(Long id1, Long id2, LocalDateTime dateMessage, String message, Long reply) {
        this.id1 = id1;
        this.id2 = id2;
        this.dateMessage = dateMessage;
        this.message = message;
        this.reply = reply;
        this.toList = new ArrayList<>();
    }
}
