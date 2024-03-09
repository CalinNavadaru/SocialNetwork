package cs.ubbcluj.lab7_8_9map.domain;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;

    public FriendshipStatus getStatus() {
        return status;
    }

    FriendshipStatus status;

    public Prietenie(Long id1, Long id2, LocalDateTime d, FriendshipStatus status) {
        super();
        this.id = new Tuple<>(id1, id2);
        date = d;
        this.status = status;
//        this.id.setLeft(id1);
//        this.id.setRight(id2);
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
}
