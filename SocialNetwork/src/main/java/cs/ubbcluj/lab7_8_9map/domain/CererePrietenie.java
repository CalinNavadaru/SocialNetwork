package cs.ubbcluj.lab7_8_9map.domain;

public class CererePrietenie  extends Entity<Tuple<Long, Long>> {

    FriendshipStatus status;

    public CererePrietenie(Long id1, Long id2, FriendshipStatus status) {
        super();
        this.id = new Tuple<>(id1, id2);
        this.status = status;
    }

    public FriendshipStatus getStatus() {
        return status;
    }
}
