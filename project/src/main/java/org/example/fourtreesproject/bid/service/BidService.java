package org.example.fourtreesproject.bid.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.model.response.BidMyListResponse;
import org.example.fourtreesproject.bid.model.response.GpbuyWaitListResponse;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final UserRepository userRepository;

    // TODO : 예외처리
    public void register(Long userIdx, BidRegisterRequest bidRegisterRequest) {
        // 공구
        Optional<GroupBuy> resultGroupBuy = groupBuyRepository.findById(bidRegisterRequest.getGpbuyIdx());
        if(resultGroupBuy.isPresent()) {
            GroupBuy groupBuy = resultGroupBuy.get();
            System.out.println();

            // 등록 상품 조회
            Optional<Product> resultProduct = productRepository.findById(bidRegisterRequest.getProductIdx());
            if(resultProduct.isPresent()) {
                Product product = resultProduct.get();

                // 자기 상품인지 검증
                if(product.getCompany().getUser().getIdx().equals(userIdx)) {
                    System.out.println(product.getCompany().getUser().getIdx());
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

    // TODO : 예외처리
    public List<BidMyListResponse> myList(Long userIdx, Boolean bidSelect) {
        List<Bid> bidResults = bidRepository.findAllByUserIdxAndBidSelect(userIdx, bidSelect);

        List<BidMyListResponse> bidMyListResponses = new ArrayList<>();
        for(Bid bid : bidResults) {
            BidMyListResponse bidMyListResponse = BidMyListResponse.builder()
                    .productName(bid.getProduct().getProductName())
                    .productImgUrl(bid.getProduct().getProductImgList().stream()
                            .filter(img -> img.getProductImgSequence() == 0)
                            .findFirst()
                            .map(ProductImg::getProductImgUrl)
                            .orElse(null))
                    .bidPrice(bid.getBidPrice())
                    .build();
            bidMyListResponses.add(bidMyListResponse);
        }
        return bidMyListResponses;
    }

    // TODO : 예외처리
    public List<GpbuyWaitListResponse> statusWaitList(Integer page, Integer size, Long categoryIdx, String gpbuyTitle) {
        // 업체 회원이 업체가 입찰할 수 있는 공구 목록을 조회한다. (카테고리, 제목)
        // TODO : 뭘로 정렬할지
        Pageable pageable = PageRequest.of(page, size);

        Slice<GroupBuy> result = groupBuyRepository.searchWaitList(pageable,categoryIdx, gpbuyTitle);

        List<GroupBuy> groupBuyList = result.getContent();
        List<GpbuyWaitListResponse> gpbuyWaitListResponseList = new ArrayList<>();
        for(GroupBuy groupBuy : groupBuyList) {
            GpbuyWaitListResponse gpbuyWaitListResponse = GpbuyWaitListResponse.builder()
                    .gpbuyIdx(groupBuy.getIdx())
                    .gpbuyTitle(groupBuy.getGpbuyTitle())
                    .gpbuyQuantity(groupBuy.getGpbuyQuantity())
                    .build();
            gpbuyWaitListResponseList.add(gpbuyWaitListResponse);
        }
        return gpbuyWaitListResponseList;
    }
}
