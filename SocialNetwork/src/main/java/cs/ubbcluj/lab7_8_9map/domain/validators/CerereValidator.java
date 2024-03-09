package cs.ubbcluj.lab7_8_9map.domain.validators;

import cs.ubbcluj.lab7_8_9map.domain.*;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;
import cs.ubbcluj.lab7_8_9map.exceptions.ValidationException;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

public class CerereValidator implements Validator<DTOCererePrietenie> {
    private final Repository<Tuple<Long, Long>, Prietenie> repository;

    private final Repository<Long, Utilizator> utilizatorRepository;

    public CerereValidator(Repository<Tuple<Long, Long>, Prietenie> repository, Repository<Long, Utilizator> utilizatorRepository) {
        this.repository = repository;
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public void validate(DTOCererePrietenie entity) throws ValidationException {
        var id = entity.getId();

        if (utilizatorRepository.findOne(id.getLeft()).isEmpty()) {
            throw new ValidationException("Nu exista primul utilizator!");
        }
        if (utilizatorRepository.findOne(id.getRight()).isEmpty()) {
            throw new ValidationException("Nu exista al doilea utilizator!");
        }

        var result = repository.findOne(entity.getId());
        if (result.isPresent() && result.get().getStatus() == FriendshipStatus.ACCEPTED)
            throw new ValidationException("Prietenia exista deja!");
    }
}