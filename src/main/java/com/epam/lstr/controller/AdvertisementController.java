package com.epam.lstr.controller;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.service.AdResponseService;
import com.epam.lstr.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    AdResponseService adResponseService;

    @GetMapping("/advertisement/user")
    public String getUsersAdvertisement(@AuthenticationPrincipal BaseUser user,
                                        Advertisement advertisement,
                                        Model model) {
        model.addAttribute("curAdvert", advertisementService.findByUser(user));
        model.addAttribute("curUserResp", adResponseService.findAllByUser(user));
        return "advertisement";
    }

    @GetMapping("/advertisement")
    public String getAdvertisement(Model model) {
        model.addAttribute("advertisements", advertisementService.findAllActive());
        return "all_advertisement";
    }

    @GetMapping("/advertisement/get/{id}")
    public String getAdvertisement(@PathVariable Long id, Model model) {
        try {
            Advertisement advertisement = advertisementService.findById(id);
            model.addAttribute("advertisement", advertisement);
        } catch (AdvertisementNotExistException ex) {
            return "redirect:/error";
        }
        return "cur_advertisement";
    }


    @PostMapping("/advertisement")
    public String addAdvertisement(@AuthenticationPrincipal BaseUser user,
                                   @Valid Advertisement advertisement,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("curAdvert", advertisementService.findByUser(user));
            model.addAttribute("curUserResp", adResponseService.findAllByUser(user));
            return "advertisement";
        }
        advertisementService.add(advertisement, user);
        return "redirect:/advertisement/user";
    }

    @PostMapping("/advertisement/close")
    public String closeAdvertisement(@AuthenticationPrincipal BaseUser user,
                                     Long adResponseId) {
        AdResponse response = adResponseService.getById(adResponseId);
        if (advertisementService.closeAdvertisement(response, user)) {
            return "redirect:/advertisement/user";
        }
        return "redirect:/error";
    }
}
