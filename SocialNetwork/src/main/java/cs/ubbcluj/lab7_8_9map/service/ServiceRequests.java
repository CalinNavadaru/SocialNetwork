package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.*;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.validators.CerereValidator;
import cs.ubbcluj.lab7_8_9map.events.CerereEvent;
import cs.ubbcluj.lab7_8_9map.events.CerereTaskEvent;
import cs.ubbcluj.lab7_8_9map.events.Event;
import cs.ubbcluj.lab7_8_9map.exceptions.UserNotFoundException;
import cs.ubbcluj.lab7_8_9map.observer.Observable;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ServiceRequests implements Service<Tuple<Long, Long>, DTOCererePrietenie>, Observable<Event> {

    private final Repository<Long, Utilizator> repository;

    private final Repository<Tuple<Long, Long>, Prietenie> prietenieRepository;

    List<Observer<Event>> observerList = new ArrayList<>();

    private final Repository<Tuple<Long, Long>, CererePrietenie> cererePrietenieRepository;

    public ServiceRequests(Repository<Long, Utilizator> repository, Repository<Tuple<Long, Long>, Prietenie> prietenieRepository1, Repository<Tuple<Long, Long>, CererePrietenie> prietenieRepository) {
        this.repository = repository;
        this.prietenieRepository = prietenieRepository1;
        this.cererePrietenieRepository = prietenieRepository;
    }

    @Override
    public DTOCererePrietenie addEntity(DTOCererePrietenie entity) {
        var id1 = entity.getId().getLeft();
        var id2 = entity.getId().getRight();
        var v = new CerereValidator(prietenieRepository, repository);
        v.validate(entity);

        var p = new CererePrietenie(id1, id2, FriendshipStatus.PENDING);

        var r = cererePrietenieRepository.save(p);
        notifyObservers(new CerereTaskEvent(CerereEvent.ADD, entity));
        if (r.isPresent()) {
            return entity;
        }
        return null;
    }

    @Override
    public DTOCererePrietenie findOneEntity(Tuple<Long, Long> longLongTuple) {
        return null;
    }

    @Override
    public Iterable<DTOCererePrietenie> getAllEntities() {
        var result = this.cererePrietenieRepository.findAll();
        List<DTOCererePrietenie> iterable = new ArrayList<>();
        for (var r: result) {
            iterable.add(new DTOCererePrietenie(r.getStatus(), r.getId()));
        }
        return iterable;
    }

    @Override
    public DTOCererePrietenie deleteEntity(Tuple<Long, Long> id) {
        var entity = new DTOCererePrietenie(FriendshipStatus.PENDING, id);
        var v = new CerereValidator(prietenieRepository, repository);
        v.validate(entity);
        CererePrietenie result = null;
        try {
            var r = this.cererePrietenieRepository.delete(id);
            if (r.isPresent())
                result = r.get();
        } catch (UserNotFoundException e) {
            return null;
        }
        if (result != null) {
            notifyObservers(new CerereTaskEvent(CerereEvent.DELETE, entity));
            return entity;
        }
        return null;
    }

    @Override
    public DTOCererePrietenie updateEntity(DTOCererePrietenie entity) {
        return null;
    }

    @Override
    public Iterable<DTOCererePrietenie> getAllFriends(Long Id) {
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
