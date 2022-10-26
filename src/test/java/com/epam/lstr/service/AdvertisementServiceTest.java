package com.epam.lstr.service;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.repository.AdvertisementRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvertisementServiceTest {

    private static Advertisement advertisement;
    private static BaseUser userAdvertisementOwner;

    @Mock
    AdvertisementRepo advertisementRepo;

    @InjectMocks
    AdvertisementService advertisementService;

    @Spy
    List<Advertisement> advertisements = new ArrayList<>();

    @BeforeAll
    public static void before() {
        advertisement = new Advertisement();
        userAdvertisementOwner = new BaseUser();
        advertisement.setActive(true);
        advertisement.setUser(userAdvertisementOwner);
        userAdvertisementOwner.setActive(true);
        userAdvertisementOwner.setId(1L);
        userAdvertisementOwner.setUsername("user");
    }

    @Test
    void getAdvertisementByIdSuccessfulTest() {
        when(advertisementRepo.findById(1L)).thenReturn(Optional.ofNullable(advertisement));
        assertEquals(advertisement, advertisementService.findById(1L));
        Mockito.verify(advertisementRepo).findById(anyLong());
    }

    @Test
    void getAdvertisementByIdUnsuccessfulTest() {
        when(advertisementRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdvertisementNotExistException.class, () -> advertisementService.findById(1L));
        Mockito.verify(advertisementRepo).findById(1L);
    }

    @Test
    void getFindAllActiveTest() {
        advertisements.add(advertisement);
        when(advertisementRepo.findByActiveTrue()).thenReturn(advertisements);
        assertEquals(advertisements, advertisementService.findAllActive());
    }

    @Test
    void getFindByUserTest() {
        advertisements.add(advertisement);
        when(advertisementRepo.findByUser(userAdvertisementOwner)).thenReturn(advertisements);
        assertEquals(advertisements, advertisementService.findByUser(userAdvertisementOwner));
    }

    @Test
    void saveSuccessfulTest() {
        advertisementService.save(advertisement);
        verify(advertisementRepo).save(advertisement);
    }

    @Test
    void saveNullUnSuccessfulTest() {
        assertThrows(AdvertisementNotExistException.class, () -> advertisementService.save(null));
    }

    @Test
    void addSuccessfulTest() {
        assertDoesNotThrow(()->advertisementService.add(advertisement, userAdvertisementOwner));
        verify(advertisementRepo).save(advertisement);
    }

    @Test
    void addNullAdvertisementUnSuccessfulTest() {
        assertThrows(AdvertisementNotExistException.class, () -> advertisementService.add(null, userAdvertisementOwner));
        verifyNoInteractions(advertisementRepo);
    }

    @Test
    void closeAdvertisementByNotOwnerUnSuccessfulTest() {
        AdResponse response = createResponse();
        advertisement.addAdResponse(response);
        assertFalse(advertisementService.closeAdvertisement(response, new BaseUser()));
        verifyNoInteractions(advertisementRepo);
    }

    @Test
    void closeAdvertisementByOwnerSuccessfulTest() {
        AdResponse response = createResponse();
        advertisement.addAdResponse(response);
        assertTrue(advertisementService.closeAdvertisement(response, userAdvertisementOwner));
        verify(advertisementRepo).save(advertisement);
    }

    private AdResponse createResponse() {
        final BaseUser userResponseOwner = new BaseUser();
        userResponseOwner.setActive(true);
        userResponseOwner.setUsername("Owner");
        final AdResponse response = new AdResponse();
        response.setAdvertisement(advertisement);
        response.setId(1L);
        response.setMyPrice(10);
        response.setUser(userResponseOwner);
        return response;
    }
}