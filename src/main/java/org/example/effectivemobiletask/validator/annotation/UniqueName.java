package org.example.effectivemobiletask.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.effectivemobiletask.validator.validator.UniqueNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNameValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueName {
    String message() default "Name already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
