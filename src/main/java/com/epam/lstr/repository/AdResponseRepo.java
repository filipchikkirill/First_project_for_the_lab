package com.epam.lstr.repository;

import com.epam.lstr.domain.AdResponse;
import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AdResponseRepo extends JpaRepository<AdResponse, Long> {
    AdResponse findByUserAndAdvertisement(BaseUser baseUser, Advertisement advertisement);
    Set<AdResponse> findByUser(BaseUser user);
}
