package com.index325.dscatalog.services;

import com.index325.dscatalog.dto.ProductDTO;
import com.index325.dscatalog.entities.Product;
import com.index325.dscatalog.repositories.ProductRepository;
import com.index325.dscatalog.repositories.ProductRepository;
import com.index325.dscatalog.services.exceptions.DatabaseException;
import com.index325.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> categories = productRepository.findAll(pageRequest);

        return categories.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        Product entity = optionalProduct.orElseThrow(() -> new EntityNotFoundException("Such product was not found"));

        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();

//        entity.setName(dto.getName());

        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
//            entity.setName(dto.getName());

            entity = productRepository.save(entity);

            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Such product was not found");
        }

    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("This product that you`re trying to delete was not found");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation check your request and try again");
        }
    }
}
