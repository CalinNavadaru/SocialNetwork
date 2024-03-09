package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.Message;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOMessage;
import cs.ubbcluj.lab7_8_9map.events.MessageEvent;
import cs.ubbcluj.lab7_8_9map.events.MessageTaskEvent;
import cs.ubbcluj.lab7_8_9map.observer.Observable;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.repository.DBRepositoryMessage;
import cs.ubbcluj.lab7_8_9map.repository.DBRepositoryUtilizator;

import java.util.ArrayList;
import java.util.List;

public class ServiceMessage implements Observable<MessageTaskEvent> {

    private final DBRepositoryUtilizator repositoryUtilizator;
    private final DBRepositoryMessage repositoryMessage;

    private final List<Observer<MessageTaskEvent>> observerList = new ArrayList<>();

    public ServiceMessage(DBRepositoryUtilizator repositoryUtilizator, DBRepositoryMessage repositoryMessage) {
        this.repositoryUtilizator = repositoryUtilizator;
        this.repositoryMessage = repositoryMessage;
    }

    public Iterable<DTOMessage> getAllMessagesId(Long id) {
        var result = repositoryMessage.findOne(id);
        return result.stream().map(x -> {
            var r = new DTOMessage(x.getId1(), x.getId2(), x.getDateMessage(), x.getMessage(),
                    x.getIdReply());
            r.setId(x.getId());
            return r;
        }).toList();
    }

    public DTOMessage addMessage(DTOMessage message) {
        var msg = new Message(message.getId(), message.getId1(), message.getId2(), message.getDateMessage(),
                message.getMessage(), message.getReply());

        var result = this.repositoryMessage.save(msg);
        if (result.isPresent()) {
            message.setId(result.get().getId());
            notifyObservers(new MessageTaskEvent(MessageEvent.SENT, message));
            return message;
        }
        return null;
    }

    public DTOMessage replyMessage(DTOMessage message, DTOMessage originalMessage) {
        var msg = new Message(null, message.getId1(), message.getId2(), message.getDateMessage(),
                message.getMessage(), message.getReply());
        var r1 = this.repositoryMessage.save(msg);
        if (r1.isPresent()) {
            var result = this.repositoryMessage.update(new Message(originalMessage.getId(), message.getId2(),
                    message.getId1(),originalMessage.getDateMessage() ,originalMessage.getMessage(), r1.get().getId()));
            return message;
        }
        return null;
    }

    @Override
    public void addObserver(Observer<MessageTaskEvent> e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageTaskEvent> e) {
        observerList.remove(e);
    }

    @Override
    public void notifyObservers(MessageTaskEvent t) {
        observerList.stream().forEach(x -> x.update(t));
    }
}
