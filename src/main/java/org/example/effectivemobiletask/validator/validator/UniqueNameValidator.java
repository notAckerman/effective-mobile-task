package org.example.effectivemobiletask.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.repository.UserRepository;
import org.example.effectivemobiletask.validator.annotation.UniqueName;

@RequiredArgsConstructor
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return !userRepository.existsByName(name);
    }
}
