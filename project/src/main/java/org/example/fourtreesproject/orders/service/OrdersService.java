package org.example.fourtreesproject.orders.service;

import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.common.annotation.Timer;
import org.example.fourtreesproject.coupon.model.Coupon;
import org.example.fourtreesproject.coupon.model.UserCoupon;
import org.example.fourtreesproject.coupon.repository.UserCouponRepository;
import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.example.fourtreesproject.delivery.model.response.DeliveryAddressResponse;
import org.example.fourtreesproject.exception.custom.InvalidBidException;
import org.example.fourtreesproject.exception.custom.InvalidOrderException;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.orders.model.entity.Orders;
import org.example.fourtreesproject.orders.model.response.OrderPageResponse;
import org.example.fourtreesproject.orders.model.response.OrdersListResponse;
import org.example.fourtreesproject.orders.repository.OrdersRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.exception.custom.InvalidUserException;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.repository.ProductImgRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.model.response.UserCouponResponse;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final UserCouponRepository userCouponRepository;
    private final BidRepository bidRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final ProductImgRepository productImgRepository;

    @Transactional
    public void registerOrder(Long userIdx, String impUid) throws IamportResponseException, IOException, RuntimeException {
        User user = userRepository.findById(userIdx).orElse(null);
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = iamportResponse.getResponse();
        BigDecimal amount = payment.getAmount();
        if (user == null) {
            refund(impUid, amount);
            throw new InvalidUserException(USER_INFO_DETAIL_FAIL);
        }


        String customData = payment.getCustomData();
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(customData, Map.class);

        Long bidIdx = (Double.valueOf(data.get("bidIdx").toString())).longValue();
        Long userCouponIdx = null;
        if (data.get("userCouponIdx") != null) {
            userCouponIdx = (Double.valueOf(data.get("userCouponIdx").toString())).longValue();
        }
        Integer orderQuantity = (Double.valueOf(data.get("orderQuantity").toString())).intValue();
        Integer usePoint = (Double.valueOf(data.get("usePoint").toString())).intValue();
        Integer deadline = (Double.valueOf(data.get("deadline").toString())).intValue();
        String recipientName = data.get("recipientName").toString();
        String recipientAddress = data.get("recipientAddress").toString();
        Integer recipientPostCode = (Double.valueOf(data.get("recipientPostCode").toString())).intValue();
        String recipientPhoneNumber = data.get("recipientPhoneNumber").toString();

        Bid bid = bidRepository.findById(bidIdx).orElse(null);
        if (bid == null) {
            refund(impUid, amount);
            throw new InvalidOrderException(BID_INFO_FAIL);
        }
        GroupBuy groupBuy = bid.getGroupBuy();
        if (groupBuy.getGpbuyRemainQuantity() < orderQuantity) { // 공구 수량 초과
            refund(impUid, amount);
            throw new InvalidOrderException(GROUPBUY_JOIN_FAIL_QUANTITY_OVER);
        }
        String gpbuyStatus = groupBuy.getGpbuyStatus();
        if (gpbuyStatus.equals("대기")) {
            groupBuy.startGroupBuy(deadline);
            groupBuyRepository.save(groupBuy);
            bid.selectBid();
            bidRepository.save(bid);
        } else if (!gpbuyStatus.equals("진행")) {
            refund(impUid, amount);
            throw new InvalidOrderException(GROUPBUY_JOIN_FAIL);
        } else if (LocalDateTime.now().isAfter(groupBuy.getGpbuyEndedAt())) {
            refund(impUid, amount);
            throw new InvalidOrderException(GROUPBUY_RECRUITING_FAIL_DEADLINE);
        }

        UserDetail userDetail = userDetailRepository.findByUserIdx(user.getIdx()).orElseThrow(() -> new InvalidUserException(USER_NOT_BASIC));
        UserCoupon userCoupon = null;
        Integer couponPrice = 0;
        if (userCouponIdx != null) {
            userCoupon = userCouponRepository.findFirstByIdxAndUserIdxAndCouponStatusTrueOrderByIdx(userCouponIdx, userIdx).orElse(null);
            if (userCoupon == null) {
                refund(impUid, amount);
                throw new InvalidOrderException(COUPON_NOT_FOUND);
            }
            Coupon coupon = userCoupon.getCoupon();
            couponPrice = coupon.getCouponPrice();
        }


        if (userDetail.getPoint() < usePoint) {
            refund(impUid, amount);
            throw new InvalidOrderException(USER_POINT_LACK);
        }

        if (bid.getBidPrice() * orderQuantity - usePoint - couponPrice == amount.intValue()) {
            Orders orders = Orders.builder()
                    .orderQuantity(orderQuantity)
                    .recipientName(recipientName)
                    .recipientAddress(recipientAddress)
                    .recipientPostCode(recipientPostCode)
                    .recipientPhoneNumber(recipientPhoneNumber)
                    .impUid(impUid)
                    .usePoint(usePoint)
                    .userCoupon(userCoupon)
                    .groupBuy(groupBuy)
                    .orderStatus("주문")
                    .user(user)
                    .build();
            ordersRepository.save(orders);
            groupBuy.updateRemainQuantity(orderQuantity);
            groupBuyRepository.save(groupBuy);
            if (userCoupon != null) {
                userCoupon.useCoupon();
                userCouponRepository.save(userCoupon);
            }
            userDetail.updatePoint(usePoint);
            userDetailRepository.save(userDetail);
            log.info("결제 성공");
        } else {
            refund(impUid, amount);
            throw new InvalidOrderException(PAYMENT_FAIL);
        }
    }

    public void refund(String impUid, BigDecimal amount) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true, amount);
        iamportClient.cancelPaymentByImpUid(cancelData);
        log.info("결제 취소");
    }

    @Timer
    public List<OrdersListResponse> getOrderInfoList(Integer page, Integer size, Long userIdx) throws RuntimeException {
        Pageable pageable = PageRequest.of(page, size);
        Slice<OrdersListResponse> ordersListResponseSlice = ordersRepository.findMyOrders(pageable, userIdx);
        return ordersListResponseSlice.stream().toList();
    }

    public OrderPageResponse loadOrderPage(Long userIdx, Long gpbuyIdx, Integer quantity) throws RuntimeException {
        User user = userRepository.findById(userIdx).orElseThrow(() -> new InvalidUserException(USER_NOT_BASIC));
        UserDetail userDetail = userDetailRepository.findByUserIdx(user.getIdx()).orElseThrow(() -> new InvalidUserException(USER_NOT_BASIC));
        List<UserCoupon> userCouponList = user.getUserCouponList();
        List<UserCouponResponse> userCouponResponseList = new ArrayList<>();
        for (UserCoupon userCoupon : userCouponList) {
            if (!userCoupon.getCouponStatus()){
                continue;
            }
            Coupon coupon = userCoupon.getCoupon();
            UserCouponResponse userCouponResponse = UserCouponResponse.builder()
                    .couponPrice(coupon.getCouponPrice())
                    .couponContent(coupon.getCouponContent())
                    .couponName(coupon.getCouponName())
                    .userCouponIdx(userCoupon.getIdx())
                    .minOrderPrice(coupon.getMinOrderPrice())
                    .build();
            userCouponResponseList.add(userCouponResponse);
        }
        List<DeliveryAddress> deliveryAddressList = user.getDeliveryAddress();
        List<DeliveryAddressResponse> deliveryAddressResponseList = new ArrayList<>();
        for (DeliveryAddress deliveryAddress : deliveryAddressList) {
            DeliveryAddressResponse deliveryAddressResponse = DeliveryAddressResponse.builder()
                    .addressDefault(deliveryAddress.getAddressDefault())
                    .addressName(deliveryAddress.getAddressName())
                    .addressInfo(deliveryAddress.getAddressInfo())
                    .postCode(deliveryAddress.getPostCode())
                    .build();
            deliveryAddressResponseList.add(deliveryAddressResponse);
        }

        Bid bid = bidRepository.findByGroupBuyIdxAndBidSelectIsTrue(gpbuyIdx).orElseThrow(() -> new InvalidBidException(BID_INFO_FAIL));
        Product product = bid.getProduct();
        ProductImg productThumbnailImg = productImgRepository.findByProductIdxAndProductImgSequence(product.getIdx(), 0).orElse(null);
        return OrderPageResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .point(userDetail.getPoint())
                .deliveryAddressResponseList(deliveryAddressResponseList)
                .userCouponResponseList(userCouponResponseList)
                .bidPrice(bid.getBidPrice())
                .productName(product.getProductName())
                .productThumbnailUrl(productThumbnailImg.getProductImgUrl())
                .quantity(quantity)
                .build();
    }

}