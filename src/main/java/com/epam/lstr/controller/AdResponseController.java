package com.epam.lstr.controller;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdResponseException;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.service.AdResponseService;
import com.epam.lstr.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adResponse")
public class AdResponseController {
    @Autowired
    private AdResponseService adResponseService;
    @Autowired
    private AdvertisementService advertisementService;

    @PostMapping
    public String addResponse(Integer myPrice, Long idAdvertisement, @AuthenticationPrincipal BaseUser user) {
        try {
            Advertisement advertisement = advertisementService.findById(idAdvertisement);
            adResponseService.saveResponse(myPrice, advertisement, user);
        } catch (AdvertisementNotExistException | AdResponseException e) {
            return "error";
        }
        return "redirect:/advertisement";
    }
}
