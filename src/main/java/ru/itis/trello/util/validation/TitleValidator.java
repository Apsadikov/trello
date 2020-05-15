package ru.itis.trello.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TitleValidator implements ConstraintValidator<Title, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.compile("^[A-Za-z]{1,45}$").matcher(value).matches();
    }
}
