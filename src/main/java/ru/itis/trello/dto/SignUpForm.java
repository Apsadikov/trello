package ru.itis.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.trello.util.validation.Name;
import ru.itis.trello.util.validation.Password;
import ru.itis.trello.util.validation.ValidationSteps;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@GroupSequence({ValidationSteps.First.class, ValidationSteps.Second.class, SignUpForm.class})
public class SignUpForm {
    @NotEmpty(groups = ValidationSteps.First.class, message = "{error.email.empty}")
    @Email(groups = ValidationSteps.Second.class, message = "{error.email.email}")
    private String email;

    @NotEmpty(groups = ValidationSteps.First.class, message = "{error.password.empty}")
    @Password(groups = ValidationSteps.Second.class)
    private String password;

    @NotEmpty(groups = ValidationSteps.First.class, message = "{error.name.empty}")
    @Name(groups = ValidationSteps.Second.class)
    private String name;
}
