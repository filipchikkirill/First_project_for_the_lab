package com.epam.lstr.controller;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.repository.AdvertisementRepo;
import com.epam.lstr.service.AdResponseService;
import com.epam.lstr.service.AdvertisementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertisementControllerTest {

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;

    @Mock
    AdvertisementService advertisementService;

    @Mock
    AdResponseService adResponseService;

    @InjectMocks
    AdvertisementController advertisementController;

    @Test
    void getAdvertisement_htmlExists_stringReturned() {
        // GIVEN
        Advertisement advertisement = new Advertisement();
        BaseUser user = new BaseUser();

        // WHEN
        String result = advertisementController.getAdvertisement(model);
        // THEN
        assertEquals("all_advertisement", result);
    }

    @Test
    void getAdvertisementWithIdTest() {
        // GIVEN
        Long id = 1L;
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);

        // WHEN
        when(advertisementService.findById(id)).thenReturn(advertisement);
        // THEN
        String result = advertisementController.getAdvertisement(id, model);
        verify(model).addAttribute("advertisement", advertisement);
        assertEquals("cur_advertisement", result);
    }

    @Test
    void getAdvertisementWithIdUnSuccessfulTest() {
        final Long id = 1L;

        when(advertisementService.findById(id)).thenThrow(new AdvertisementNotExistException());

        final String result = advertisementController.getAdvertisement(id, model);
        verifyNoInteractions(model);
        assertEquals("redirect:/error", result);
    }

    @Test
    void closeAdvertisementSuccessfulTest() {
        final Long adResponseId = 1L;
        final AdResponse response = new AdResponse();
        response.setId(1L);
        final BaseUser user = new BaseUser();

        when(adResponseService.getById(adResponseId)).thenReturn(response);
        when(advertisementService.closeAdvertisement(response, user))
                .thenReturn(Boolean.TRUE);

        String result = advertisementController.closeAdvertisement(user, adResponseId);
        assertEquals("redirect:/advertisement/user", result);
    }

    @Test
    void closeAdvertisementUnSuccessfulTest() {
        final Long adResponseId = 1L;
        final AdResponse response = new AdResponse();
        response.setId(1L);
        final BaseUser user = new BaseUser();

        when(adResponseService.getById(adResponseId)).thenReturn(response);
        when(advertisementService.closeAdvertisement(response, user))
                .thenReturn(Boolean.FALSE);

        String result = advertisementController.closeAdvertisement(user, adResponseId);
        assertEquals("redirect:/error", result);
    }

    @Test
    void getUsersAdvertisementSuccessfulTest() {
        final BaseUser user = new BaseUser();
        final List<Advertisement> advertisements = new ArrayList<>();
        final Set<AdResponse> adResponses = new HashSet<>();

        when(advertisementService.findByUser(user)).thenReturn(advertisements);
        when(adResponseService.findAllByUser(user)).thenReturn(adResponses);

        assertEquals("advertisement", advertisementController.getUsersAdvertisement(user, new Advertisement(), model));
        verify(model).addAttribute("curAdvert", advertisements);
        verify(model).addAttribute("curUserResp", adResponses);
    }

    @Test
    void addAdvertisementSuccessfulTest() {
        final BaseUser user = new BaseUser();
        final Advertisement advertisement = new Advertisement();

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);

        String result = advertisementController.addAdvertisement(user, advertisement, bindingResult, model);
        assertEquals("redirect:/advertisement/user", result);
        verify(advertisementService).add(advertisement, user);
    }

    @Test
    void addAdvertisementUnSuccessfulTest() {
        final BaseUser user = new BaseUser();
        final Advertisement advertisement = new Advertisement();
        final List<Advertisement> advertisements = new ArrayList<>();
        final Set<AdResponse> adResponses = new HashSet<>();

        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(advertisementService.findByUser(user)).thenReturn(advertisements);
        when(adResponseService.findAllByUser(user)).thenReturn(adResponses);

        String result = advertisementController.addAdvertisement(user, advertisement, bindingResult, model);
        assertEquals("advertisement", result);
        verify(model).addAttribute("curAdvert", advertisementService.findByUser(user));
        verify(model).addAttribute("curUserResp", adResponseService.findAllByUser(user));
    }
}
