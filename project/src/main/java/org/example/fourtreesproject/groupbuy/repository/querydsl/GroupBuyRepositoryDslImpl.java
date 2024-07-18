package org.example.fourtreesproject.groupbuy.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.fourtreesproject.bid.model.entity.QBid;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.entity.QCategory;
import org.example.fourtreesproject.groupbuy.model.entity.QGroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupBuyRepositoryDslImpl implements GroupBuyRepositoryDsl {
    private final JPAQueryFactory queryFactory;
    private final QGroupBuy groupBuy;
    private final QBid bid;
    private final QCategory category;

    public GroupBuyRepositoryDslImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.groupBuy = QGroupBuy.groupBuy;
        this.bid = QBid.bid;
        this.category = QCategory.category;
    }

    @Override
    public Slice<GroupBuy> searchWaitList(Pageable pageable, Long categoryIdx, String gpbuyTitle) {
        List<GroupBuy> result = queryFactory
                .selectFrom(groupBuy)
                .where(groupBuy.gpbuyStatus.eq("대기"), categoryIdxEq(categoryIdx), gpbuyTitleContains(gpbuyTitle))
                .offset(pageable.getOffset())
                .orderBy(groupBuy.gpbuyRegedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(result.size() - 1);
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public Slice<GroupBuy> searchList(Pageable pageable, GroupBuySearchRequest request) {
        List<GroupBuy> result = queryFactory
                .selectFrom(groupBuy)
                .join(groupBuy.bidList,bid)
                .join(groupBuy.category,category)
                .where(groupBuy.gpbuyStatus.eq("진행"),
                        minMaxBetween(request.getMinPrice(), request.getMaxPrice()),
                        categoryNameEq(request.getCategory()))
                .offset(pageable.getOffset())
                .orderBy(groupBuy.gpbuyRemainQuantity.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(result.size() - 1);
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression categoryIdxEq(Long categoryIdx) {
        return categoryIdx != null ? groupBuy.category.idx.eq(categoryIdx) : null;
    }

    private BooleanExpression minMaxBetween(Integer min, Integer max) {
        if (min == null && max != null){
            return bid.bidPrice.between(0, max);
        }
        if (min != null && max == null) {
            return bid.bidPrice.between(min,Integer.MAX_VALUE );
        }
        if (min != null && max != null){
            return bid.bidPrice.between(min, max);
        }
        return null;
    }

    private BooleanExpression categoryNameEq(String category) {
        return category != null ? groupBuy.category.categoryName.eq(category) : null;
    }

    private BooleanExpression gpbuyTitleContains(String gpbuyTitle) {
        if (gpbuyTitle == null || gpbuyTitle.isBlank()) {
            return null;
        }
        return groupBuy.gpbuyTitle.contains(gpbuyTitle);
    }
}
