package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Prietenie;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.domain.validators.PrietenieValidator;
import cs.ubbcluj.lab7_8_9map.events.*;
import cs.ubbcluj.lab7_8_9map.exceptions.UserNotFoundException;
import cs.ubbcluj.lab7_8_9map.observer.Observable;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.repository.Page;
import cs.ubbcluj.lab7_8_9map.repository.Pageable;
import cs.ubbcluj.lab7_8_9map.repository.PagingRepository;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServicePrietenie implements Service<Tuple<Long, Long>, DTOPrietenie>, Observable<Event> {

    private final PagingRepository<Long, Utilizator> repository;

    List<Observer<Event>> observerList = new ArrayList<>();

    private final PagingRepository<Tuple<Long, Long>, Prietenie> prietenieRepository;

    public ServicePrietenie(PagingRepository<Long, Utilizator> repository, PagingRepository<Tuple<Long, Long>, Prietenie> prietenieRepository) {
        this.repository = repository;
        this.prietenieRepository = prietenieRepository;
    }

    @Override
    public DTOPrietenie addEntity(DTOPrietenie entity) {
        var id1 = entity.getId().getLeft();
        var id2 = entity.getId().getRight();
        var v = new PrietenieValidator(prietenieRepository, repository);
        v.validate(entity);

        var p = new Prietenie(id1, id2, entity.getDate(), entity.getStatus());

        var r = prietenieRepository.save(p);
        notifyObservers(new PrietenieTaskEvent(PrietenieEvent.ADD, entity));
        if (r.isPresent()) {
            return entity;
        }
        return null;
    }

    @Override
    public DTOPrietenie findOneEntity(Tuple<Long, Long> longLongTuple) {
        return null;
    }

    @Override
    public Iterable<DTOPrietenie> getAllEntities() {
        var result = this.prietenieRepository.findAll();
        List<DTOPrietenie> iterable = new ArrayList<>();
        for (var r: result) {
            iterable.add(new DTOPrietenie(r.getDate(), r.getStatus(), r.getId()));
        }
        return iterable;
    }

    @Override
    public DTOPrietenie deleteEntity(Tuple<Long, Long> id) {
        var entity = new DTOPrietenie(LocalDateTime.now(), FriendshipStatus.ACCEPTED, id);
        var v = new PrietenieValidator(prietenieRepository, repository);
        v.validate(entity);
        Prietenie result = null;
        try {
            var r = this.prietenieRepository.delete(id);
            if (r.isPresent())
                result = r.get();
        } catch (UserNotFoundException e) {
            return null;
        }
        if (result != null) {
            notifyObservers(new PrietenieTaskEvent(PrietenieEvent.DELETE, entity));
            return entity;
        }
        return null;
    }

    public Page<DTOPrietenie> findAllPaginat(Pageable pageable) {
        var result = this.prietenieRepository.findAll(pageable);
        var iterableDTOPrietenie = StreamSupport.stream(result.getElementsOnPage().spliterator(), false)
                .map(res -> new DTOPrietenie(res.getDate(), res.getStatus(), res.getId())).toList();
        return new Page<>(iterableDTOPrietenie, result.getTotalElementCount());
    }

    @Override
    public DTOPrietenie updateEntity(DTOPrietenie entity) {
        return null;
    }

    @Override
    public Iterable<DTOPrietenie> getAllFriends(Long Id) {
        return null;
    }

    @Override
    public void addObserver(Observer<Event> e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observerList.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observerList.stream().forEach(x -> x.update(t));
    }
}