package org.example.fourtreesproject.bid.repository;

import org.example.fourtreesproject.bid.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b WHERE b.groupBuy.idx = :gpbuyIdx AND (b.bidStatus = '등록' OR b.bidStatus = '수정') ORDER BY b.bidPrice ASC")
    List<Bid> findAllByGpbuyIdx(Long gpbuyIdx);

    @Query("SELECT b FROM Bid b " +
            "JOIN FETCH b.product p " +
            "JOIN FETCH p.company c " +
            "JOIN FETCH c.user u " +
            "JOIN FETCH b.groupBuy g " +
            "JOIN FETCH p.productImgList pi " +

            "WHERE u.idx = :userIdx AND b.bidSelect = :bidSelect")
    List<Bid> findAllByUserIdxAndBidSelect(Long userIdx, Boolean bidSelect);

    Optional<Bid> findByGroupBuyIdxAndBidSelectIsTrue(Long groupBuyIdx);

}
