package cs.ubbcluj.lab7_8_9map.repository;

import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBRepositoryUtilizator implements PagingRepository<Long, Utilizator> {

    private final String url;
    private final String user;
    private final String password;

    public DBRepositoryUtilizator(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM socialnetwork.public.users WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Utilizator user = getUser(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        List<Utilizator> result;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM socialnetwork.public.users")) {
            ResultSet resultSet = statement.executeQuery();
            result = new ArrayList<>();

            while (resultSet.next()) {
                Utilizator user = getUser(resultSet);
                result.add(user);
            }
        } catch (SQLException Utilizator) {
            throw new RepositoryException(Utilizator.getMessage());
        }
        return result;
    }

    private Utilizator getUser(ResultSet resultSet) throws SQLException {
        String firsName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Long user_id = resultSet.getLong("id");
        String password = resultSet.getString("pass");
        var user = new Utilizator(firsName, lastName, password);
        user.setId(user_id);
        return user;
    }

    @Override
    public List<Utilizator> getFriends(Long id) {
        List<Utilizator> rez = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u1.first_name, " +
                     "u1.last_name, u1.id, u2.first_name, u2.last_name," +
                     " u2.id, u1.pass, u2.pass FROM friendships " +
                     "INNER JOIN public.users u1 on u1.id = friendships.user1id " +
                     "INNER JOIN public.users u2 on u2.id = friendships.user2id " +
                     "WHERE (u1.id = ? OR u2.id = ?) AND friendships.status = 1")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            var r = statement.executeQuery();
            while (r.next()) {
                String f1 = r.getString(1);
                String l1 = r.getString(2);
                Long id1 = r.getLong(3);
                String f2 = r.getString(4);
                String l2 = r.getString(5);
                Long id2 = r.getLong(6);
                String p1 = r.getString(7);
                String p2 = r.getString(8);
                Utilizator u = null;
                if (!id1.equals(id)) {
                    u = new Utilizator(f1, l1, p1);
                    u.setId(id1);
                } else {
                    u = new Utilizator(f2, l2, p2);
                    u.setId(id2);
                }
                rez.add(u);
            }
            return rez;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     "socialnetwork.public.users(first_name, last_name, pass) " +
                     "VALUES (?, ?, ?)")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getPassword());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        Utilizator entity = null;
        var r = this.findOne(id);
        if (r.isPresent())
            entity = r.get();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE from users WHERE id = ?")) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                assert entity != null;
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE public.users " +
                     "SET first_name = ?, last_name = ?" +
                     " WHERE id = ?")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Page<Utilizator> findAll(Pageable pageable) {
        List<Utilizator> userList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users " +
                    "LIMIT ? OFFSET ?");
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) as nr FROM users")) {
            statement.setLong(1, pageable.getPageSize());
            statement.setLong(2, (long) pageable.getPageSize() * pageable.getPageNumber());
            ResultSet pageSet = statement.executeQuery();
            ResultSet countSet = countStatement.executeQuery();

            while(pageSet.next()) {
                Long id = pageSet.getLong(1);
                String firstName = pageSet.getString(2);
                String secondName = pageSet.getString(3);
                String pass = pageSet.getString(4);
                Utilizator u = new Utilizator(firstName, secondName, pass);
                u.setId(id);
                userList.add(u);
            }
            int totalCount = countSet.next()? countSet.getInt("nr"): 0;
            return new Page<>(userList, totalCount);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
