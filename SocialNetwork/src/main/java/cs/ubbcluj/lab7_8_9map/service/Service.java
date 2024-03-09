package cs.ubbcluj.lab7_8_9map.service;

public interface Service<ID, E> {

    E addEntity(E entity);

    E findOneEntity(ID id);

    Iterable<E> getAllEntities();

    E deleteEntity(ID id);

    E updateEntity(E entity);

    Iterable<E> getAllFriends(Long Id);

}
