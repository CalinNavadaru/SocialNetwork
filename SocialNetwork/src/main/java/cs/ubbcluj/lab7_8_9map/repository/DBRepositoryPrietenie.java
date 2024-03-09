package cs.ubbcluj.lab7_8_9map.repository;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Prietenie;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBRepositoryPrietenie implements PagingRepository<Tuple<Long, Long>, Prietenie> {

    private final String url;
    private final String user;
    private final String password;

    public DBRepositoryPrietenie(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<Long, Long> id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT f.user1id AS i1, " +
                     "f.user2id AS i2, f.dataprietenie, f.status FROM friendships f" +
                     " WHERE  f.user1id = ? AND f.user2id = ?")) {

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
                var date = r.getTimestamp(3);
                var status = getStatus(r.getLong(4));
                var p1 = new Prietenie(id1, id2, date.toLocalDateTime(), status);
                return Optional.of(p1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        List<Prietenie> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT f.user1id AS i1, " +
                     "f.user2id AS i2, f.dataprietenie, f.status FROM friendships f" +
                     " WHERE f.status = 1 ")) {

            var r = statement.executeQuery();
            while (r.next()) {
                Long id1 = r.getLong(1);
                Long id2 = r.getLong(2);
                var date = r.getTimestamp(3);
                var status = getStatus(r.getLong(4));
                var p1 = new Prietenie(id1, id2, date.toLocalDateTime(), status);
                result.add(p1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships " +
                     "VALUES (?, ?, ?, ?)")) {
            var idst = entity.getId().getLeft();
            var iddr = entity.getId().getRight();
            if (idst > iddr) {
                var aux = idst;
                idst = iddr;
                iddr = aux;
            }
            statement.setLong(1, idst);
            statement.setLong(2, iddr);
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            statement.setLong(4, entity.getStatus().ordinal());
            var r = statement.executeUpdate();
            return r != 0? Optional.empty(): Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Prietenie> delete(Tuple<Long, Long> id) {
        Prietenie prietenie = null;
        var optionalPrietenie = this.findOne(id);
        if (optionalPrietenie.isPresent())
            prietenie = optionalPrietenie.get();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships " +
                     " WHERE user1id = ? AND user2id = ?")) {
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
                assert prietenie != null;
                return Optional.of(prietenie);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }

    @Override
    public List<Prietenie> getFriends(Tuple<Long, Long> longLongTuple) {
        return null;
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
    public Page<Prietenie> findAll(Pageable pageable) {
        List<Prietenie> prietenieList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships " +
                     " WHERE status = 1 " +
                     "LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) as nr FROM users")) {
            statement.setLong(1, pageable.getPageSize());
            statement.setLong(2, (long) pageable.getPageSize() * pageable.getPageNumber());
            ResultSet pageSet = statement.executeQuery();
            ResultSet countSet = countStatement.executeQuery();

            while(pageSet.next()) {
                Long id1 = pageSet.getLong(1);
                Long id2 = pageSet.getLong(2);
                var date = pageSet.getTimestamp(3);
                var status = getStatus(pageSet.getLong(4));
                var p1 = new Prietenie(id1, id2, date.toLocalDateTime(), status);
                prietenieList.add(p1);
            }
            int totalCount = countSet.next()? countSet.getInt("nr"): 0;
            return new Page<>(prietenieList, totalCount);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
