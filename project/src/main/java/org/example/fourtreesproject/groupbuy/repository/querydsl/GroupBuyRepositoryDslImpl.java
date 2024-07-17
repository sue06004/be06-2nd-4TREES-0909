package org.example.fourtreesproject.groupbuy.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.entity.QGroupBuy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupBuyRepositoryDslImpl implements GroupBuyRepositoryDsl {
    private final JPAQueryFactory queryFactory;
    private final QGroupBuy groupBuy;

    public GroupBuyRepositoryDslImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.groupBuy = QGroupBuy.groupBuy;
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

    private BooleanExpression categoryIdxEq(Long categoryIdx) {
        return categoryIdx != null ? groupBuy.category.idx.eq(categoryIdx) : null;
    }

    private BooleanExpression gpbuyTitleContains(String gpbuyTitle) {
        if (gpbuyTitle == null || gpbuyTitle.isBlank()) {
            return null;
        }
        return groupBuy.gpbuyTitle.contains(gpbuyTitle);
    }
}
