package ru.itis.trello.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.service.UserService;

import java.util.List;

@Controller
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public List<UserDto> getUsers(@RequestParam(value = "name") String name) {
        return userService.findUserByName(name);
    }
}
