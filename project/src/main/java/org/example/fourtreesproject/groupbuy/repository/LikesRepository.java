package org.example.fourtreesproject.groupbuy.repository;

import org.example.fourtreesproject.groupbuy.model.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Long> {
}
