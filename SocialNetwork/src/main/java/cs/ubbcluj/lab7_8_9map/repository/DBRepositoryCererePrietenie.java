package cs.ubbcluj.lab7_8_9map.repository;


import cs.ubbcluj.lab7_8_9map.domain.CererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBRepositoryCererePrietenie implements Repository<Tuple<Long, Long>, CererePrietenie> {

    private final String url;
    private final String user;
    private final String password;

    public DBRepositoryCererePrietenie(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<CererePrietenie> findOne(Tuple<Long, Long> id) {

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT f.id1 AS i1, " +
                     "f.id2 AS i2, f.status FROM friendrequests f" +
                     " WHERE  f.id1 = ? AND f.id2 = ?")) {

            var idst = id.getLeft();
            var iddr = id.getRight();
            if (idst > iddr) {
                var aux = idst;
                idst = iddr;
                iddr = aux;
            }
            statement.setLong(1, idst);
            statement.setLong(2, iddr);

            var r = statement.executeQuery();
            if (r.next()) {
                Long id1 = r.getLong(1);
                Long id2 = r.getLong(2);
                var status = getStatus(r.getLong(3));
                var p1 = new CererePrietenie(id1, id2, status);
                return Optional.of(p1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<CererePrietenie> findAll() {
        List<CererePrietenie> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT f.id1 AS i1, " +
                     "f.id2 AS i2, f.status FROM friendrequests f")) {

            var r = statement.executeQuery();
            while (r.next()) {
                Long id1 = r.getLong(1);
                Long id2 = r.getLong(2);
                var status = r.getLong(3);
                var friendshipStatus = this.getStatus(status);
                var p1 = new CererePrietenie(id1, id2, friendshipStatus);
                result.add(p1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    private FriendshipStatus getStatus(long status) {
        return switch ((int) status) {
            case 0 -> FriendshipStatus.PENDING;
            case 1 -> FriendshipStatus.ACCEPTED;
            case 2 -> FriendshipStatus.REJECTED;
            default -> null;
        };
    }

    @Override
    public Optional<CererePrietenie> save(CererePrietenie entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friendrequests " +
                     "VALUES (?, ?, ?)")) {
            var idst = entity.getId().getLeft();
            var iddr = entity.getId().getRight();
            if (idst > iddr) {
                var aux = idst;
                idst = iddr;
                iddr = aux;
            }
            statement.setLong(1, idst);
            statement.setLong(2, iddr);
            statement.setLong(3, entity.getStatus().ordinal());
            var r = statement.executeUpdate();
            return r != 0? Optional.empty(): Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<CererePrietenie> delete(Tuple<Long, Long> id) {
        CererePrietenie cererePrietenie = null;
        var optionalCererePrietenie = this.findOne(id);
        if (optionalCererePrietenie.isPresent())
            cererePrietenie = optionalCererePrietenie.get();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendrequests " +
                     " WHERE id1 = ? AND id2 = ?")) {
            var idst = id.getLeft();
            var iddr = id.getRight();
            if (idst > iddr) {
                var aux = idst;
                idst = iddr;
                iddr = aux;
            }
            statement.setLong(1, idst);
            statement.setLong(2, iddr);
            var affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                assert cererePrietenie != null;
                return Optional.of(cererePrietenie);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<CererePrietenie> update(CererePrietenie entity) {
        return Optional.empty();
    }

    @Override
    public List<CererePrietenie> getFriends(Tuple<Long, Long> longLongTuple) {
        return null;
    }
}

