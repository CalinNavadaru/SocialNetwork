package cs.ubbcluj.lab7_8_9map.repository;


import cs.ubbcluj.lab7_8_9map.domain.Entity;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAll(Pageable pageable);
}
