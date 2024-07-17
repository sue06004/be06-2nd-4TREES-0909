package org.example.fourtreesproject.user.repository;


import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
}
