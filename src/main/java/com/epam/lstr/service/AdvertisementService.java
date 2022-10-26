package com.epam.lstr.service;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import com.epam.lstr.exception.AdvertisementNotExistException;
import com.epam.lstr.repository.AdvertisementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdvertisementService {
    @Autowired
    AdvertisementRepo advertisementRepo;

    public void add(Advertisement advertisement, BaseUser user) {
        if (advertisement == null) {
            throw new AdvertisementNotExistException();
        }
        advertisement.setActive(true);
        advertisement.setUser(user);
        save(advertisement);
    }

    public Boolean closeAdvertisement(AdResponse response, BaseUser user) {
        if (!response.getAdvertisement().getUser().equals(user)) {
            return Boolean.FALSE;
        }
        Advertisement advertisement = response.getAdvertisement();
        advertisement.clearAdResponses();
        advertisement.addAdResponse(response);
        advertisement.setActive(false);
        save(advertisement);
        return Boolean.TRUE;
    }

    public void save(Advertisement advertisement) throws AdvertisementNotExistException {
        if(advertisement == null) throw new AdvertisementNotExistException();
        advertisementRepo.save(advertisement);
    }

    public List<Advertisement> findByUser(BaseUser user) {
        return advertisementRepo.findByUser(user);
    }

    public Iterable<Advertisement> findAllActive() {
        return advertisementRepo.findByActiveTrue();
    }

    public Advertisement findById(Long idAdvertisement) throws AdvertisementNotExistException {
        return advertisementRepo.findById(idAdvertisement).orElseThrow(AdvertisementNotExistException::new);
    }
}
