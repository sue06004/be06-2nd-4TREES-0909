package org.example.fourtreesproject.groupbuy.repository.querydsl;


import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupBuyRepositoryDsl {
    Slice<GroupBuy> searchWaitList(Pageable pageable, Long categoryIdx, String gpbuyTitle);
}
