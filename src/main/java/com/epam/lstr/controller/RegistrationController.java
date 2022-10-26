package com.epam.lstr.controller;

import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import org.springframework.validation.FieldError;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid BaseUser user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            for (FieldError err: bindingResult.getFieldErrors()) {
                model.addAttribute(err.getField() + "Error", err.getDefaultMessage());
            }
            return "registration";
        }

        if (!user.getPassword().equals(user.getPassword2())){
            model.addAttribute("passwordError", "Passwords are not equals");
            return "registration";
        }

        if (!userService.saveUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}
