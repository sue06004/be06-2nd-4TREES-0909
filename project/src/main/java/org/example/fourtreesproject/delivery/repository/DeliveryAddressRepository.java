package org.example.fourtreesproject.delivery.repository;

import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    Optional<DeliveryAddress> findByUserIdxAndAddressDefaultTrue(Long user_idx);
}
