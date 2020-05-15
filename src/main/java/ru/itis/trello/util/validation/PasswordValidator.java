package ru.itis.trello.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,32})").matcher(s).matches();
    }
}
