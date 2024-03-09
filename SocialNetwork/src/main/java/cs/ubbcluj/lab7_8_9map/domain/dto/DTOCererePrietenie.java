package cs.ubbcluj.lab7_8_9map.domain.dto;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;

public class DTOCererePrietenie {
    FriendshipStatus status;

    Tuple<Long, Long> id;


    public Tuple<Long, Long> getId() {
        return id;
    }

    public Long getId1() {
        return id.getLeft();
    }

    public Long getId2() {
        return id.getRight();
    }

    public DTOCererePrietenie(FriendshipStatus status, Tuple<Long, Long> id) {
        this.status = status;
        this.id = id;
    }
}
