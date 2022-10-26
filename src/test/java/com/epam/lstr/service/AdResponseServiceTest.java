package com.epam.lstr.service;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdResponseException;
import com.epam.lstr.repository.AdResponseRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdResponseServiceTest {

    private static AdResponse adResponse;
    private static Advertisement advertisement;
    private static BaseUser userAdOwner;

    private static BaseUser userNotAdOwner;
    private static AdResponse adResponseNotUserOwner;

    @Mock
    AdResponseRepo adResponseRepo;

    @InjectMocks
    AdResponseService adResponseService;

    @Spy
    Set<AdResponse> adResponses = new HashSet<>();

    @BeforeAll
    public static void before() {
        adResponse = new AdResponse();
        adResponseNotUserOwner = new AdResponse();
        userAdOwner = new BaseUser();
        userNotAdOwner = new BaseUser();
        advertisement = new Advertisement();

        adResponse.setAdvertisement(advertisement);
        adResponse.setUser(userAdOwner);
        adResponse.setId(1L);
        adResponse.setMyPrice(10_000);
        adResponseNotUserOwner.setAdvertisement(advertisement);
        adResponseNotUserOwner.setUser(userNotAdOwner);
        adResponseNotUserOwner.setId(2L);
        adResponseNotUserOwner.setMyPrice(20_000);

        advertisement.setActive(true);
        advertisement.setId(1L);
        advertisement.setUser(userAdOwner);

        userAdOwner.setActive(true);
        userAdOwner.setId(1L);
        userAdOwner.setUsername("user");
        userNotAdOwner.setActive(true);
        userNotAdOwner.setId(2L);
        userNotAdOwner.setUsername("user2");
    }

    @Test
    void getFindAllByUserTest() {
        adResponses.add(adResponse);
        when(adResponseRepo.findByUser(userAdOwner)).thenReturn(adResponses);
        assertEquals(adResponses, adResponseService.findAllByUser(userAdOwner));
    }

    @Test
    void getAdResponseByIdSuccessfulTest() {
        when(adResponseRepo.findById(1L)).thenReturn(Optional.ofNullable(adResponse));
        assertEquals(adResponse, adResponseService.getById(1L));
        Mockito.verify(adResponseRepo).findById(anyLong());
    }

    @Test
    void getAdResponseByIdUnsuccessfulTest() {
        when(adResponseRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdResponseException.class, () -> adResponseService.getById(1L));
        Mockito.verify(adResponseRepo).findById(1L);
    }


    @Test
    void saveAdResponseTest() {
        adResponseService.save(adResponse);
        verify(adResponseRepo).save(adResponse);
    }

    @Test
    void userAdOwnerAddAdResponseUnsuccessfulTest() {
        assertThrows(AdResponseException.class, ()->adResponseService.add(adResponse), "AdResponse and Advertisement cannot be from the same user");
    }

    @Test
    void userNotOwnerAddAdResponseSuccessfulTest() {
        assertDoesNotThrow(()->adResponseService.add(adResponseNotUserOwner));
        when(adResponseRepo.findByUserAndAdvertisement(userNotAdOwner, advertisement))
                .thenReturn(adResponseNotUserOwner);
        assertDoesNotThrow(()->adResponseService.add(adResponseNotUserOwner));
    }

    @Test
    void AddAdResponseToCloseAdvertisementUnSuccessfulTest() {
        advertisement.setActive(false);
        assertThrows(AdResponseException.class, ()->adResponseService.add(adResponseNotUserOwner), "AdResponse cannot be added to a closed ad");
        advertisement.setActive(true);
    }

    @Test
    void saveResponseUnSuccessfulTest() {
        assertThrows(AdResponseException.class, ()->adResponseService.saveResponse(20_000, advertisement, userAdOwner));
    }

    @Test
    void saveResponseSuccessfulTest() {
        assertDoesNotThrow(()->adResponseService.saveResponse(20_000, advertisement, userNotAdOwner));
    }
}
