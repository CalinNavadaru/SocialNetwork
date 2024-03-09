package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Prietenie;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceStatistici {
    private final Repository<Long, Utilizator> repository;

    private final Repository<Tuple<Long, Long>, Prietenie> prietenieRepository;

    public ServiceStatistici(Repository<Long, Utilizator> repository, Repository<Tuple<Long, Long>, Prietenie> prietenieRepository) {
        this.repository = repository;
        this.prietenieRepository = prietenieRepository;
    }

    public int getNrComunities() {
        return getComunities().size();
    }

    public List<DTOUtilizator> getMostSociableComunity() {
        var r = getComunities();
        int maxim = 0, index_maxim = 0, i = 0;
        for (var c : r) {
            if (c.getRight() > maxim) {
                maxim = c.getRight();
                index_maxim = i;
            }
            i++;
        }
        List<DTOUtilizator> rez = new ArrayList<>();
        for (var u : r.get(index_maxim).getLeft()) {
            rez.add(new DTOUtilizator(u.getFirstName(), u.getLastName(), u.getPassword(), u.getId()));
        }
        return rez;
    }

    private List<Tuple<List<Utilizator>, Integer>> getComunities() {
        var r = this.repository.findAll();
        Set<Utilizator> set = new HashSet<>();
        List<Tuple<List<Utilizator>, Integer>> rez = new ArrayList<>();
        for (var u : r) {
            if (!(set.contains(u))) {
                int m = 1;
                var d = DFS(u, set, m);
                rez.add(new Tuple<>(d, m));
            }
        }
        return rez;
    }

    private List<Utilizator> DFS(Utilizator u, Set<Utilizator> set, int max) {
        List<Utilizator> list = new ArrayList<>();
        list.add(u);
        set.add(u);
//        for (var v : u.getFriends())
        for (var v : this.repository.getFriends(u.getId())) {
            if (!set.contains(v)) {
                max += 1;
                var l = DFS(v, set, max);
                list.addAll(l);
            }
        }

        return list;
    }

    public List<DTOPrietenie> getAllFriendshipsMonth(Long id, int month) {
        List<DTOPrietenie> rez = null;
        var r = this.prietenieRepository.findAll();

        Predicate<Prietenie> prieteniePredicate = x -> x.getDate().getMonthValue() == month &&
                (Objects.equals(x.getId().getLeft(), id) || Objects.equals(x.getId().getRight(), id))
                && x.getStatus() == FriendshipStatus.ACCEPTED;
        rez = StreamSupport.stream(r.spliterator(), false)
                .filter(prieteniePredicate)
                .map(x -> new DTOPrietenie(x.getDate(), x.getStatus(), x.getId()))
                .collect(Collectors.toList());

        return rez;
    }
}
