package org.example.fourtreesproject.orders.repository;

import org.example.fourtreesproject.orders.model.entity.Orders;
import org.example.fourtreesproject.orders.model.response.OrdersListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT new org.example.fourtreesproject.orders.model.response." +
            "OrdersListResponse(gb.idx,gb.gpbuyStatus, p.productName, img.productImgUrl, o.deliveryNumber, b.bidPrice, o.orderStartedAt, o.orderStatus) " +
            "FROM Orders o JOIN o.user u JOIN o.groupBuy gb " +
            "JOIN gb.bidList b JOIN b.product p  JOIN p.productImgList img " +
            "WHERE u.idx = :userIdx AND b.bidSelect = true AND img.productImgSequence = 0")
    Slice<OrdersListResponse> findMyOrders(Pageable pageable, Long userIdx);

    Optional<Orders> findByIdxAndUserIdx(Long ordersIdx, Long userIdx);
}
