package org.example.fourtreesproject.groupbuy.repository;

import org.example.fourtreesproject.groupbuy.repository.querydsl.GroupBuyRepositoryDsl;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy,Long>, GroupBuyRepositoryDsl {
    List<GroupBuy> findByUserIdxAndGpbuyStatus(Long userIdx, String gpbuyStatus);
}
