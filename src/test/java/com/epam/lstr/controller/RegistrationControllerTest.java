package com.epam.lstr.controller;

import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    BindingResult bindingResult;

    @Mock
    Model model;

    @Mock
    UserService userService;

    @InjectMocks
    RegistrationController registrationController;

    @Test
    void getRegistration_htmlExist() {
        String result = registrationController.registration();
        assertEquals("registration", result);
    }

    @Test
    void registrationSuccessfulTest() {
        BaseUser user = new BaseUser();
        user.setPassword("1111");
        user.setPassword2("1111");

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
        when(userService.saveUser(user)).thenReturn(Boolean.TRUE);

        String result = registrationController.addUser(user, bindingResult, model);
        assertEquals("redirect:/login", result);
    }

    @Test
    void registrationUsernameExistUnSuccessfulTest() {
        BaseUser user = new BaseUser();
        user.setPassword("1111");
        user.setPassword2("1111");

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
        when(userService.saveUser(user)).thenReturn(Boolean.FALSE);

        String result = registrationController.addUser(user, bindingResult, model);
        assertEquals("registration", result);
        verify(model).addAttribute("usernameError", "User exists!");
    }

    @Test
    void registrationPasswordsNotEqualsUnSuccessfulTest() {
        BaseUser user = new BaseUser();
        user.setPassword("1111");
        user.setPassword2("2222");

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);

        String result = registrationController.addUser(user, bindingResult, model);
        assertEquals("registration", result);
        verifyNoInteractions(userService);
        verify(model).addAttribute("passwordError", "Passwords are not equals");
    }

    @Test
    void registrationHasErrorsUnSuccessfulTest() {
        BaseUser user = new BaseUser();
        ArrayList<FieldError> fieldErrors = new ArrayList<>();
        FieldError fieldError = new FieldError("user", "username", "cannot be null");
        fieldErrors.add(fieldError);

        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        String result = registrationController.addUser(user, bindingResult, model);
        assertEquals("registration", result);
        verifyNoInteractions(userService);
        verify(model).addAttribute(fieldError.getField() + "Error", fieldError.getDefaultMessage());
    }
}