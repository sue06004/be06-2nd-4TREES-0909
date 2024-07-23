package org.example.fourtreesproject.user.repository;


import org.example.fourtreesproject.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String Name);

    @Query ("SELECT u FROM User u LEFT JOIN FETCH u.userCouponList uc " +
            "LEFT JOIN FETCH uc.coupon " +
            "WHERE u.idx = :userIdx")
    Optional<User> findUserInfoDetail(Long userIdx);
}
