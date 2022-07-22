package com.index325.dscatalog.repositories;

import com.index325.dscatalog.entities.Category;
import com.index325.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
