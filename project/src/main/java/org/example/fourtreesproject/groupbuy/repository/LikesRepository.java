package org.example.fourtreesproject.groupbuy.repository;

import org.example.fourtreesproject.groupbuy.model.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Long> {

    @Query("SELECT l FROM Likes l WHERE l.groupBuy.idx = :gpbuyIdx AND l.user.idx = :userIdx")
    Optional<Likes> findByGpbuyIdxAndUserIdx(Long gpbuyIdx, Long userIdx);
}
