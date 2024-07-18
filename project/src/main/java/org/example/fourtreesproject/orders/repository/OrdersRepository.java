package org.example.fourtreesproject.orders.repository;

import org.example.fourtreesproject.orders.model.entity.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {
    Slice<Orders> findByUserIdxAndOrderStatusOrderByOrderStartedAtDesc(Pageable pageable, Long userIdx, String orderStatus);
}
