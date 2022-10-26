package com.epam.lstr.repository;

import com.epam.lstr.domain.Advertisement;
import com.epam.lstr.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepo extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByActiveTrue();
    List<Advertisement> findByUser(BaseUser user);
}
