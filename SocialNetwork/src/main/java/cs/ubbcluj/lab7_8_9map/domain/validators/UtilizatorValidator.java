package cs.ubbcluj.lab7_8_9map.domain.validators;

import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.exceptions.ValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        var firstName = entity.getFirstName();
        var lastName = entity.getLastName();

        if (!(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) {
            throw new ValidationException("Prenumele trebuie sa inceapa cu litera mare!");
        }

        if (!(lastName.charAt(0) >= 'A' && lastName.charAt(0) <= 'Z')) {
            throw new ValidationException("Numele trebuie sa inceapa cu litera mare!");
        }
    }
}
