package com.epam.lstr.service;

import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.repository.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static BaseUser user;
    private static BaseUser user2;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void before() {
        user = new BaseUser();
        user2 = new BaseUser();
        user.setActive(true);
        user2.setActive(true);
        user.setId(1L);
        user2.setId(2L);
        user.setUsername("user");
        user2.setUsername("user2");
    }

    @Test
    void loadUserByUsername() {
        when(userRepo.findByUsername("user")).thenReturn(user);
        when(userRepo.findByUsername("user2")).thenReturn(user2);
        assertEquals(user, userService.loadUserByUsername("user"));
        assertEquals(user2, userService.loadUserByUsername("user2"));
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("some name"));
    }

    @Test
    void saveUser() {
        when(userRepo.findByUsername("user")).thenReturn(user);
        when(userRepo.findByUsername("user2")).thenReturn(user2);
        assertFalse(userService.saveUser(user));
        assertFalse(userService.saveUser(user2));
    }
}