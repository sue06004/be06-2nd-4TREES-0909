package org.example.fourtreesproject.groupbuy.repository;

import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String category);
}
