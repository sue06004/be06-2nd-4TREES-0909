package org.example.fourtreesproject.gpbuy.repository;

import org.example.fourtreesproject.gpbuy.model.entity.Groupbuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpbuyRepository extends JpaRepository<Groupbuy,Long> {
}
