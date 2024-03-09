package cs.ubbcluj.lab7_8_9map.repository;

import cs.ubbcluj.lab7_8_9map.domain.Message;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOMessage;
import cs.ubbcluj.lab7_8_9map.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBRepositoryMessage  {
    private final String url;
    private final String user;
    private final String password;

    public DBRepositoryMessage(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    public List<Message> findOne(Long id) {
        List<Message> messageList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM usermessage " +
                     "WHERE id1 = ? OR id2 = ?")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            var result = statement.executeQuery();
            while (result.next()) {
                var idMessage = result.getLong(1);
                var idUser1 = result.getLong(2);
                var idUser2 = result.getLong(3);
                var message = result.getString(4);
                var idReply = result.getLong(5);
                var data = result.getTimestamp(6).toLocalDateTime();
                messageList.add(new Message(idMessage, idUser1, idUser2, data, message, idReply));
            }
            return messageList;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public Iterable<Message> findAll() {
        return null;
    }

    public Optional<Message> save(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO usermessage (id1, id2, msg, reply, datamesaj) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "RETURNING idmessage ")) {
            statement.setLong(1,entity.getId1());
            statement.setLong(2,entity.getId2());
            statement.setString(3, entity.getMessage());
            statement.setLong(4, -1);
            statement.setTimestamp(5, Timestamp.valueOf(entity.getDateMessage()));

            var idMessage = statement.executeQuery();
            if (idMessage.next()) {
                entity.setId((long) idMessage.getLong("idmessage"));
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    public Optional<Message> update(Message entity) {

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE usermessage " +
                     "SET reply = ? " +
                     "WHERE idmessage = ?")) {
            statement.setLong(1, entity.getIdReply());
            statement.setLong(2, entity.getId());

            var affectedRows = statement.executeUpdate();
            return affectedRows != 0? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<Message> getFriends(Long aLong) {
        return null;
    }
}
