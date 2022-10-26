package com.epam.lstr.controller;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdResponseException;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.service.AdResponseService;
import com.epam.lstr.service.AdvertisementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdResponseControllerTest {

    @Mock
    AdResponseService adResponseService;

    @Mock
    AdvertisementService advertisementService;

    @InjectMocks
    AdResponseController adResponseController;

    @Test
    void getAdResponse_htmlExists_stringReturned() {
        // GIVEN
        Advertisement advertisement = new Advertisement();
        BaseUser user = new BaseUser();
        // WHEN
        String result = adResponseController.addResponse(1, advertisement.getId(), user);
        // THEN
        assertEquals("redirect:/advertisement", result);
    }

    @Test
    void addResponseNoAdvertisementWithPresentIdUnSuccessfulTest() {
        final Long idAdvertisement = 1L;
        final BaseUser userCurrent = new BaseUser();
        final Integer myPrice = 10;

        when(advertisementService.findById(idAdvertisement)).thenThrow(new AdvertisementNotExistException());

        String result = adResponseController.addResponse(myPrice, idAdvertisement, userCurrent);
        assertEquals("error", result);
        verifyNoInteractions(adResponseService);
    }
}