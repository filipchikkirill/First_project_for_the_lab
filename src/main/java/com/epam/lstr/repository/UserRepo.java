package com.epam.lstr.repository;

import com.epam.lstr.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<BaseUser, Long> {
    BaseUser findByUsername(String username);
}
