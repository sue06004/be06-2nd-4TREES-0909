package org.example.fourtreesproject.bid.repository.querydsl;


import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BidRepositoryDsl {
    Slice<Bid> findAllByUserIdAndBidSelect(Pageable pageable, Long userId, Boolean bidSelect);
}
