package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.domain.validators.UtilizatorValidator;
import cs.ubbcluj.lab7_8_9map.events.*;
import cs.ubbcluj.lab7_8_9map.exceptions.UserNotFoundException;
import cs.ubbcluj.lab7_8_9map.observer.Observable;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.repository.Page;
import cs.ubbcluj.lab7_8_9map.repository.Pageable;
import cs.ubbcluj.lab7_8_9map.repository.PagingRepository;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceUtilizator implements Service<Long, DTOUtilizator>, Observable<Event> {

    private final PagingRepository<Long, Utilizator> repository;

    private final List<Observer<Event>> observers = new ArrayList<>();

    public ServiceUtilizator(PagingRepository<Long, Utilizator> repository) {
        this.repository = repository;
    }

    @Override
    public DTOUtilizator addEntity(DTOUtilizator entity) {
        var firstName = entity.getFirstName();
        var lastName = entity.getLastName();
        var id = entity.getId();
        var utilizator = new Utilizator(firstName, lastName, entity.getPassword());
        var v = new UtilizatorValidator();
        v.validate(utilizator);
        utilizator.setId(id);

        var result = this.repository.save(utilizator);
        if (result.isPresent()) {
            var r = result.get();
            return new DTOUtilizator(r.getFirstName(), r.getLastName(), r.getPassword(),
                    r.getId());
        }
        notifyObservers(new UtilizatorTaskEvent(UtilizatorEvent.ADD, entity));
        return null;
    }

    @Override
    public DTOUtilizator findOneEntity(Long id) {
        var result = this.repository.findOne(id);
        if (result.isPresent()) {
            var r = result.get();
            return new DTOUtilizator(r.getFirstName(), r.getLastName(), r.getPassword(),
                    r.getId());
        }
        return null;
    }

    @Override
    public Iterable<DTOUtilizator> getAllEntities() {
        var result = this.repository.findAll();
        List<DTOUtilizator> iterable = new ArrayList<>();
        result.forEach(r -> iterable.add(new DTOUtilizator(r.getFirstName(), r.getLastName(), r.getPassword(),
                r.getId())));
        return iterable;
    }

    @Override
    public DTOUtilizator deleteEntity(Long id) {
        Utilizator result = null;
        try {
            var r = this.repository.delete(id);
            if (r.isPresent()) result = r.get();
        } catch (UserNotFoundException e) {
            return null;
        }
        if (result != null) {
            var r = new DTOUtilizator(result.getFirstName(), result.getLastName(), result.getPassword(), result.getId());
            notifyObservers(new UtilizatorTaskEvent(UtilizatorEvent.DELETE, r, null));
            notifyObservers(new PrietenieTaskEvent(PrietenieEvent.DELETE, null, null));
            return r;
        }
        return null;
    }

    @Override
    public DTOUtilizator updateEntity(DTOUtilizator entity) {
        String firstName = entity.getFirstName();
        var lastName = entity.getLastName();
        var id = entity.getId();
        var utilizator = new Utilizator(firstName, lastName, entity.getPassword());
        var v = new UtilizatorValidator();
        v.validate(utilizator);
        utilizator.setId(id);

        var result = this.repository.update(utilizator);
        if (result.isPresent()) {
            notifyObservers(new UtilizatorTaskEvent(UtilizatorEvent.UPDATE, entity));
            return entity;
        }
        return null;
    }

    @Override
    public Iterable<DTOUtilizator> getAllFriends(Long Id) {
        return this.repository.getFriends(Id)
                .stream()
                .map(x -> new DTOUtilizator(x.getFirstName(), x.getLastName(), x.getPassword(), x.getId()))
                .toList();
    }

    public Page<DTOUtilizator> findAllPaginat(Pageable pageable) {
        var r = this.repository.findAll(pageable);
        var elements = StreamSupport.stream(r.getElementsOnPage().spliterator(), false).map(u ->
                new DTOUtilizator(u.getFirstName(), u.getLastName(), u.getPassword(), u.getId())).toList();
        return new Page<>(elements, r.getTotalElementCount());
    }

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x -> x.update(t));
    }


}
