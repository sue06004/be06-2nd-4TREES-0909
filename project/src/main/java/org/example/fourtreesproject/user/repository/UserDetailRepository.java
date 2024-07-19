package org.example.fourtreesproject.user.repository;


import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUserIdx(Long userIdx);

}
