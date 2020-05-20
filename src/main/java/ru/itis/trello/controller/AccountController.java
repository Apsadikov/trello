package ru.itis.trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.trello.dto.SignUpForm;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.service.AccountService;
import ru.itis.trello.util.exception.ConfirmationTokenInvalid;
import ru.itis.trello.util.exception.EmailIsAlreadyUse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class AccountController {
    private AccountService accountService;
    private MessageSource messageSource;

    @Autowired
    public AccountController(AccountService accountService, MessageSource messageSource) {
        this.accountService = accountService;
        this.messageSource = messageSource;
    }

    @GetMapping("/sign-in")
    @PreAuthorize("isAnonymous()")
    public String signIn(Model model) {
        model.addAttribute("isAuthError", "false");
        return "sign-in";
    }

    @PostMapping("/sign-in-error")
    public String signIn(Model model, HttpServletRequest request) {
        AuthenticationException authenticationException = (AuthenticationException)
                request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (authenticationException.getMessage().equals("User account is locked")) {
            model.addAttribute("isAccountLocked", "true");
        } else if (authenticationException.getMessage().equals("Bad credentials")) {
            model.addAttribute("isBadCredentials", "true");
        }
        model.addAttribute("email", request.getParameter("email"));
        return "sign-in";
    }

    @GetMapping("/sign-up")
    @PreAuthorize("isAnonymous()")
    public String signUp(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    @PreAuthorize("isAnonymous()")
    public String signUp(Model model, @Valid SignUpForm signUpForm, BindingResult bindingResult, Locale locale) {
        if (!bindingResult.hasErrors()) {
            try {
                accountService.signUp(UserDto.builder()
                        .email(signUpForm.getEmail())
                        .name(signUpForm.getName())
                        .password(signUpForm.getPassword())
                        .build());
                return "redirect:/sign-in";
            } catch (EmailIsAlreadyUse emailIsAlreadyUse) {
                bindingResult.rejectValue("email", "Exist",
                        messageSource.getMessage("error.email.exist", new String[]{signUpForm.getEmail()}, locale));
            }
        }
        model.addAttribute("signUpForm", signUpForm);
        return "sign-up";
    }

    @GetMapping("/confirm")
    @PreAuthorize("isAnonymous()")
    public String confirm(@RequestParam("token") String token) {
        try {
            accountService.confirmEmail(token);
            return "redirect:/sign-in";
        } catch (ConfirmationTokenInvalid confirmationTokenInvalid) {
            return "redirect:/sign-in";
        }
    }
}
