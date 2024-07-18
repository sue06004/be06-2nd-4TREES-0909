package org.example.fourtreesproject.groupbuy.service;


import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.entity.Likes;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyDetailResponse;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyLikesListResponse;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyListResponse;
import org.example.fourtreesproject.groupbuy.model.response.RegisteredBidListResponse;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.groupbuy.repository.LikesRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final GroupBuyRepository gpbuyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BidRepository bidRepository;
    private final LikesRepository likesRepository;

    public boolean save(Long user_idx, GroupBuyCreateRequest request) {
        User user = userRepository.findById(user_idx).get();
        Category category = categoryRepository.findByCategoryName(request.getCategory());
        GroupBuy groupbuy = GroupBuy.builder()
                .user(user)
                .category(category)
                .gpbuyTitle(request.getTitle())
                .gpbuyQuantity(request.getGpbuyQuantity())
                .gpbuyRemainQuantity(request.getGpbuyQuantity())
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
    //공구 시작 기능
    public boolean start(Long gpbuyIdx) {
        GroupBuy groupBuy = gpbuyRepository.findById(gpbuyIdx).get();
        groupBuy.updateStatus("시작");
        groupBuy.updateStartedAt(LocalDateTime.now());
        GroupBuy updatedGroupBuy = groupBuy;
        updatedGroupBuy = gpbuyRepository.save(updatedGroupBuy);
        if (updatedGroupBuy == null){
            return false;
        }

        return true;
    }

    public List<RegisteredBidListResponse> findBidList(Long gpbuyIdx) {
        List<Bid> bidList = bidRepository.findAllByGpbuyIdx(gpbuyIdx);
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

    public List<GroupBuyListResponse> list(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "gpbuy_remain_quantity"));
        Slice<GroupBuy> result = gpbuyRepository.findSliceByGpbuyStatus(pageable,"진행");
        List<GroupBuy> slicedResult = result.getContent();
        List<GroupBuyListResponse> responseList = new ArrayList<>();
        Product selectedProduct = null;
        ProductImg selectedProductThumbnailImg = null;
        Bid selectedBid = null;


        for (GroupBuy g : slicedResult){
            //선정된 상품 확인 (입찰 목록에서 선정 상태 확인)
            for (Bid b: g.getBidList()){
                if (isSelected(b)){
                    selectedBid = b;
                    selectedProduct = selectedBid.getProduct();
                    selectedProductThumbnailImg = extractThumbnailImg(selectedProduct.getProductImgList());
                    break;
                }
            }

            GroupBuyListResponse response = GroupBuyListResponse.builder()
                    .gpbuyIdx(g.getIdx())
                    .gpbuyQuantity(g.getGpbuyQuantity())
                    .gpbuyRemainQuantity(g.getGpbuyRemainQuantity())
                    .productThumbnailImg(selectedProductThumbnailImg.getProductImgUrl())
                    .productName(selectedProduct.getProductName())
                    .bidPrice(selectedBid.getBidPrice())
                    .companyName(selectedProduct.getCompany().getCompanyName())
                    .build();
            responseList.add(response);
        }

        return responseList;
    }

    //공구 상세조회 기능
    public GroupBuyDetailResponse findByGpbuyIdx(Long gpbuyIdx) {
        GroupBuy groupBuy = gpbuyRepository.findById(gpbuyIdx).get();
        Bid bid = null;
        ProductImg thumbnailImg = null;
        List<String> productImgList = new ArrayList<>();

        //선정된 입찰 정보 가져오기
        for (Bid b: groupBuy.getBidList()){
            if (b.getBidSelect()){
                bid = b;
                break;
            }
        }
        //썸네일과 본문 이미지 분리
        for (ProductImg img : bid.getProduct().getProductImgList()){
            thumbnailImg = extractThumbnailImg(bid.getProduct().getProductImgList());
            if (img.getProductImgSequence() != 0){
                productImgList.add(img.getProductImgUrl());
            }
        }
        //응답 DTO 빌딩
        GroupBuyDetailResponse response = GroupBuyDetailResponse.builder()
                .gpbuyIdx(groupBuy.getIdx())
                .userIdx(groupBuy.getUser().getIdx())
                .productThumbnailImg(thumbnailImg.getProductImgUrl())
                .productImgUrlList(productImgList)
                .productName(bid.getProduct().getProductName())
                .companyName(bid.getProduct().getCompany().getCompanyName())
                .gpbuyRemainQuantity(groupBuy.getGpbuyRemainQuantity())
                .gpbuyQuantity(groupBuy.getGpbuyQuantity())
                .gpbuyStartedAt(groupBuy.getGpbuyStartedAt())
                .gpbuyEndedAt(groupBuy.getGpbuyEndedAt())
                .build();
        return response;
    }


    //-- 유틸, 추출 메소드 --
    public Boolean isSelected(Bid b){
        return b.getBidSelect();
    }

    public ProductImg extractThumbnailImg(List<ProductImg> imgList){
        for (ProductImg img: imgList){
            if (img.getProductImgSequence() == 0){
                return img;
            }
        }
        return null;
    }


    public boolean likesSave(Long gpbuyIdx, Long userIdx) {
        Optional<GroupBuy> groupBuy = gpbuyRepository.findById(gpbuyIdx);
        Optional<User> user = userRepository.findById(userIdx);
        if (groupBuy.isPresent() && user.isPresent()){
            Optional<Likes> likes = likesRepository.findByGpbuyIdxAndUserIdx(gpbuyIdx, userIdx);
            if (likes.isEmpty()){
                Likes newLikes = Likes.builder()
                        .groupBuy(groupBuy.get())
                        .user(user.get())
                        .build();
                likesRepository.save(newLikes);
            }else {
                likesRepository.deleteById(likes.get().getIdx());
            }
            return true;
        } else {
            return false;
        }
    }


    //관심 공구 목록 조회
    public List<GroupBuyLikesListResponse> likesList(Long userIdx) {
        List<Likes> likesList = likesRepository.findAllByIdx(userIdx);
        List<GroupBuyLikesListResponse> responseList = new ArrayList<>();

        for (Likes l : likesList){
            Bid bid = null;
            for (Bid b : l.getGroupBuy().getBidList()) {
                if (isSelected(b)){
                    bid = b;
                    break;
                }
            }
            GroupBuyLikesListResponse response = GroupBuyLikesListResponse.builder()
                    .gpbuyIdx(l.getGroupBuy().getIdx())
                    .gpbuyQuantity(l.getGroupBuy().getGpbuyQuantity())
                    .gpbuyRemainQuantity(l.getGroupBuy().getGpbuyRemainQuantity())
                    .productThumbnailImg(extractThumbnailImg(bid.getProduct().getProductImgList()).getProductImgUrl())
                    .productName(bid.getProduct().getProductName())
                    .bidPrice(bid.getBidPrice())
                    .companyName(bid.getProduct().getCompany().getCompanyName())
                    .gpbuyStartedAt(l.getGroupBuy().getGpbuyStartedAt())
                    .gpbuyEndedAt(l.getGroupBuy().getGpbuyEndedAt())
                    .build();
            responseList.add(response);
        }
        return responseList;
    }

    public List<GroupBuyListResponse> search(GroupBuySearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.ASC, "gpbuy_remain_quantity"));
        Slice<GroupBuy> result = gpbuyRepository.searchList(pageable, request);
        List<GroupBuy> slicedResult = result.getContent();
        List<GroupBuyListResponse> responseList = new ArrayList<>();
        Product selectedProduct = null;
        ProductImg selectedProductThumbnailImg = null;
        Bid selectedBid = null;


        for (GroupBuy g : slicedResult){
            //선정된 상품 확인 (입찰 목록에서 선정 상태 확인)
            for (Bid b: g.getBidList()){
                if (isSelected(b)){
                    selectedBid = b;
                    selectedProduct = selectedBid.getProduct();
                    selectedProductThumbnailImg = extractThumbnailImg(selectedProduct.getProductImgList());
                    break;
                }
            }

            GroupBuyListResponse response = GroupBuyListResponse.builder()
                    .gpbuyIdx(g.getIdx())
                    .gpbuyQuantity(g.getGpbuyQuantity())
                    .gpbuyRemainQuantity(g.getGpbuyRemainQuantity())
                    .productThumbnailImg(selectedProductThumbnailImg.getProductImgUrl())
                    .productName(selectedProduct.getProductName())
                    .bidPrice(selectedBid.getBidPrice())
                    .companyName(selectedProduct.getCompany().getCompanyName())
                    .build();
            responseList.add(response);
        }

        return responseList;
    }
}
