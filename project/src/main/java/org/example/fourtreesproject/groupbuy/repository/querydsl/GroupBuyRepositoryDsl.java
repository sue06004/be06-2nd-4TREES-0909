package org.example.fourtreesproject.groupbuy.repository.querydsl;


import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupBuyRepositoryDsl {
    Slice<GroupBuy> searchWaitList(Pageable pageable, Long categoryIdx, String gpbuyTitle);
    Slice<GroupBuy> searchList(Pageable pageable, GroupBuySearchRequest request);

    Slice<GroupBuy> findSliceByGpbuyStatus(Pageable pageable, String gpbuyStatus);
}
