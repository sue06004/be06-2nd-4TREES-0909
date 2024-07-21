package org.example.fourtreesproject.bid.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.model.entity.QBid;
import org.example.fourtreesproject.company.model.entity.QCompany;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.entity.QCategory;
import org.example.fourtreesproject.groupbuy.model.entity.QGroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.example.fourtreesproject.product.model.entity.QProduct;
import org.example.fourtreesproject.product.model.entity.QProductImg;
import org.example.fourtreesproject.user.model.entity.QUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BidRepositoryDslImpl implements BidRepositoryDsl {
    private final JPAQueryFactory queryFactory;
    private final QGroupBuy groupBuy;
    private final QBid bid;
    private final QCompany company;
    private final QProduct product;
    private final QProductImg productImg;
    private final QUser user;

    public BidRepositoryDslImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.groupBuy = QGroupBuy.groupBuy;
        this.bid = QBid.bid;
        this.company = QCompany.company;
        this.product = QProduct.product;
        this.productImg = QProductImg.productImg;
        this.user = QUser.user;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Bid> findAllByUserIdAndBidSelect(Pageable pageable, Long userIdx, Boolean bidSelect) {
        List<Bid> bids = queryFactory.selectFrom(bid)
                .join(bid.product, product).fetchJoin()
                .join(product.company, company).fetchJoin()
                .join(company.user, user).fetchJoin()
                .leftJoin(product.productImgList, productImg).fetchJoin()
                .leftJoin(bid.groupBuy, groupBuy).fetchJoin()
                .where(user.idx.eq(userIdx)
                        .and(bid.bidSelect.eq(bidSelect)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)  // 페이지 크기보다 1 더 많이 가져와서 다음 페이지 여부 확인
                .fetch();

        boolean hasNext = bids.size() > pageable.getPageSize();

        if (hasNext) {
            bids.remove(bids.size() - 1);
        }

        return new SliceImpl<>(bids, pageable, hasNext);
    }
}
