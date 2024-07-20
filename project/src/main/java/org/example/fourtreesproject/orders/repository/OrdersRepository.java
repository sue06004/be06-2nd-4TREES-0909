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
public interface OrdersRepository extends JpaRepository<Orders,Long> {

    @Query("SELECT new org.example.fourtreesproject.orders.model.response." +
            "OrdersListResponse(gb.idx,gb.gpbuyStatus, p.productName, o.deliveryNumber) " +
            "FROM Orders o JOIN o.user u JOIN o.groupBuy gb " +
            "JOIN gb.bidList b JOIN b.product p " +
            "WHERE u.idx = :userIdx AND  o.orderStatus = :orderStatus AND b.bidSelect = true")
    Slice<OrdersListResponse> findMyOrders(Pageable pageable,Long userIdx, String orderStatus);

    Optional<Orders> findByIdxAndUserIdx(Long ordersIdx, Long userIdx);
}
