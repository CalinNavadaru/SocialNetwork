package cs.ubbcluj.lab7_8_9map.domain.dto;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;

import java.time.LocalDateTime;

public class DTOPrietenie {

    LocalDateTime date;

    FriendshipStatus status;

    Tuple<Long, Long> id;

    public FriendshipStatus getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Tuple<Long, Long> getId() {
        return id;
    }

    public Long getId1() {
        return id.getLeft();
    }

    public Long getId2() {
        return id.getRight();
    }

    public DTOPrietenie(LocalDateTime date, FriendshipStatus status, Tuple<Long, Long> id) {
        this.date = date;
        this.id = id;
        this.status = status;
    }
}
