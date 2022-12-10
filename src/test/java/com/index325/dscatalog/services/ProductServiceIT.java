package com.index325.dscatalog.services;

import com.index325.dscatalog.dto.ProductDTO;
import com.index325.dscatalog.repositories.ProductRepository;
import com.index325.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);

        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10(){

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> productDTOPage = service.findAllPaged(pageRequest);

        Assertions.assertFalse(productDTOPage.isEmpty());
        Assertions.assertEquals(0, productDTOPage.getNumber());
        Assertions.assertEquals(10, productDTOPage.getSize());
        Assertions.assertEquals(countTotalProducts, productDTOPage.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists(){

        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> productDTOPage = service.findAllPaged(pageRequest);

        Assertions.assertTrue(productDTOPage.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnOrderedPageWhenSortByName(){

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> productDTOPage = service.findAllPaged(pageRequest);

        Assertions.assertFalse(productDTOPage.isEmpty());
        Assertions.assertEquals("Macbook Pro", productDTOPage.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", productDTOPage.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", productDTOPage.getContent().get(2).getName());

    }


}
