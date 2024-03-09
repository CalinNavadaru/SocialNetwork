package cs.ubbcluj.lab7_8_9map.domain.validators;

import cs.ubbcluj.lab7_8_9map.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
