package cs.ubbcluj.lab7_8_9map.domain.validators;

import cs.ubbcluj.lab7_8_9map.domain.Prietenie;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.exceptions.ValidationException;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

public class PrietenieValidator implements Validator<DTOPrietenie> {
    private final Repository<Tuple<Long, Long>, Prietenie> repository;

    private final Repository<Long, Utilizator> utilizatorRepository;

    public PrietenieValidator(Repository<Tuple<Long, Long>, Prietenie> repository, Repository<Long, Utilizator> utilizatorRepository) {
        this.repository = repository;
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public void validate(DTOPrietenie entity) throws ValidationException {
        var id = entity.getId();

        if (utilizatorRepository.findOne(id.getLeft()).isEmpty()) {
            throw new ValidationException("Nu exista primul utilizator!");
        }
        if (utilizatorRepository.findOne(id.getRight()).isEmpty()) {
            throw new ValidationException("Nu exista al doilea utilizator!");
        }
    }
}
