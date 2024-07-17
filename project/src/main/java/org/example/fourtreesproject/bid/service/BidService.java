package org.example.fourtreesproject.bid.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;

    public void register(Long userIdx, BidRegisterRequest bidRegisterRequest) {
        // 공구
        Optional<GroupBuy> resultGroupBuy = groupBuyRepository.findById(bidRegisterRequest.getGpbuyIdx());
        if(resultGroupBuy.isPresent()) {
            GroupBuy groupBuy = resultGroupBuy.get();

            // 등록 상품 조회
            Optional<Product> resultProduct = productRepository.findById(bidRegisterRequest.getProductIdx());
            if(resultProduct.isPresent()) {
                Product product = resultProduct.get();

                // 자기 상품인지 검증
                if(product.getCompany().getUser().getIdx().equals(userIdx)) {
                    // save
                    Bid bid = Bid.builder()
                            .bidPrice(bidRegisterRequest.getBidPrice())
                            .product(product)
                            .groupBuy(groupBuy)
                            .build();
                    bidRepository.save(bid);
                } else {
                    // 본인 상품 아님
                }
            } else {
                // 상품조회실패
            }
        } else {
            // 공구 조회 실패
        }
    }
}
