package org.example.fourtreesproject.orders.service;

import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.bid.repository.BidRepository;
import org.example.fourtreesproject.coupon.model.UserCoupon;
import org.example.fourtreesproject.coupon.repository.UserCouponRepository;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.orders.exception.custom.InvalidOrderException;
import org.example.fourtreesproject.orders.model.entity.Orders;
import org.example.fourtreesproject.orders.repository.OrdersRepository;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.example.fourtreesproject.user.model.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.example.fourtreesproject.common.BaseResponseStatus.BID_INFO_FAIL;
import static org.example.fourtreesproject.common.BaseResponseStatus.PAYMENT_FAIL;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final UserCouponRepository userCouponRepository;
    private final BidRepository bidRepository;
    private final GroupBuyRepository groupBuyRepository;

    public void registerOrder(CustomUserDetails customUserDetails, String impUid) throws IamportResponseException, IOException, RuntimeException {
        User user = customUserDetails.getUser();
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = iamportResponse.getResponse();

        BigDecimal amount = payment.getAmount();
        String customData = payment.getCustomData();
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(customData, Map.class);

        Long bidIdx = (Double.valueOf(data.get("bidIdx").toString())).longValue();
        Long couponIdx = null;
        if (data.get("couponIdx") != null){
            couponIdx = (Double.valueOf(data.get("couponIdx").toString())).longValue();
        }
        Integer orderQuantity = (Double.valueOf(data.get("orderQuantity").toString())).intValue();
        Integer usePoint = (Double.valueOf(data.get("usePoint").toString())).intValue();
        String recipientName = data.get("recipientName").toString();
        String recipientAddress = data.get("recipientAddress").toString();
        Integer recipientPostCode = (Double.valueOf(data.get("recipientPostCode").toString())).intValue();
        String recipientPhoneNumber = data.get("recipientPhoneNumber").toString();

        Bid bid = bidRepository.findById(bidIdx).orElse(null);
        UserCoupon userCoupon = null;
        if (couponIdx != null){
            userCoupon = userCouponRepository.findFirstByUserIdxAndCouponIdxAndCouponStatusTrueOrderByIdx(user.getIdx(), couponIdx).orElse(null);
        }
        if (bid == null) {
            throw new InvalidOrderException(BID_INFO_FAIL);
        }
        GroupBuy groupBuy = bid.getGroupBuy();
        if (bid.getBidPrice() * orderQuantity == amount.intValue()) {
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
            log.info("결제 성공");
        } else {
            CancelData cancelData = new CancelData(impUid, true, amount);
            iamportClient.cancelPaymentByImpUid(cancelData);
            log.info("결제 취소");
            throw new InvalidOrderException(PAYMENT_FAIL);
        }
    }

}
