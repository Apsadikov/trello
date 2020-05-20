package ru.itis.trello.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.trello.aspect.RestAuthorization;
import ru.itis.trello.dto.AddStackForm;
import ru.itis.trello.dto.StackDto;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.StackService;

import javax.validation.Valid;

@RestController
public class StackRestController {
    private StackService stackService;

    @Autowired
    public StackRestController(StackService stackService) {
        this.stackService = stackService;
    }

    @PreAuthorize("isAuthenticated()")
    @RestAuthorization
    @RequestMapping(value = "/api/boards/{board_id}/stacks", method = RequestMethod.POST)
    public ResponseEntity addStack(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @Valid AddStackForm addStackForm) {
        StackDto stackDto = StackDto.builder()
                .title(addStackForm.getTitle())
                .build();
        stackService.addStack(stackDto, userDetails.getUser().getId(), boardId);
        return ResponseEntity.ok(stackDto);
    }
}
