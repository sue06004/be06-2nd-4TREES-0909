package org.example.fourtreesproject.groupbuy.service;


import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.model.response.RegisteredBidListResponse;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final GroupBuyRepository gpbuyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BidRepository bidRepository;

    public boolean save(GroupBuyCreateRequest request) {
        User user = userRepository.findById(request.getUserIdx()).get();
        Category category = categoryRepository.findByCategoryName(request.getCategory());
        GroupBuy groupbuy = GroupBuy.builder()
                .user(user)
                .category(category)
                .gpbuyTitle(request.getTitle())
                .gpbuyQuantity(request.getGpbuyQuantity())
                .gpbuyContent(request.getContent())
                .gpbuyRegedAt(LocalDateTime.now())
                .gpbuyBidEndedAt(LocalDateTime.now().plusDays(2))
                .build();

        GroupBuy res = gpbuyRepository.save(groupbuy);
        if (res == null){
            return false;
        }

        return true;

    }

    public List<RegisteredBidListResponse> findBidList(Long gpbuyIdx) {
        List<Bid> bidList = bidRepository.findAllByGpbuyIdx(gpbuyIdx);
        System.out.println(bidList.get(0).getProduct().getProductName());
        List<RegisteredBidListResponse> responseList = new ArrayList<>();
        for (Bid bid: bidList){
            String productThumbnailImg = "";
            for (ProductImg img : bid.getProduct().getProductImgList()){
                if (img.getProductImgSequence()==0){
                    productThumbnailImg = img.getProductImgUrl();
                }
            }
            RegisteredBidListResponse response = RegisteredBidListResponse.builder()
                    .bidIdx(bid.getIdx())
                    .productIdx(bid.getProduct().getIdx())
                    .productName(bid.getProduct().getProductName())
                    .productThumbnailImg(productThumbnailImg)
                    .bidPrice(bid.getBidPrice())
                    .companyName(bid.getProduct().getCompany().getCompanyName())
                    .build();
            responseList.add(response);
        }
        return responseList;




    }
}
