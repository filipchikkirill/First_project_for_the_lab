package com.epam.lstr.service;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdResponseException;
import com.epam.lstr.repository.AdResponseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class AdResponseService {
    @Autowired
    private AdResponseRepo adResponseRepo;

    public AdResponse save(AdResponse adresponse) {
        return adResponseRepo.save(adresponse);
    }

    public AdResponse add(AdResponse response) throws AdResponseException{
        if (!response.getAdvertisement().isActive()) {
            throw new AdResponseException("AdResponse cannot be added to a closed ad");
        }

        if (response.getAdvertisement().getUser().equals(response.getUser())) {
            throw new AdResponseException("AdResponse and Advertisement cannot be from the same user");
        }

        AdResponse a_response;
        a_response = adResponseRepo.findByUserAndAdvertisement(response.getUser(), response.getAdvertisement());

        if (a_response != null) {
            a_response.setMyPrice(response.getMyPrice());
        } else {
            a_response = response;
        }
        return save(a_response);
    }

    public Set<AdResponse> findAllByUser(BaseUser user) {
        return adResponseRepo.findByUser(user);
    }

    public AdResponse getById(Long id) {
        return adResponseRepo.findById(id)
                .orElseThrow(() -> new AdResponseException("This response(id = "+ id +") is not exist"));
    }

    public void saveResponse (Integer myPrice,
                              Advertisement advertisement,
                              BaseUser user) throws AdResponseException {
        AdResponse response = new AdResponse();
        response.setAdvertisement(advertisement);
        response.setUser(user);
        response.setMyPrice(myPrice);
        add(response);
    }
}
