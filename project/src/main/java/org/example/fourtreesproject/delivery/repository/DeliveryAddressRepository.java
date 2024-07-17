package org.example.fourtreesproject.delivery.repository;

import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
}
