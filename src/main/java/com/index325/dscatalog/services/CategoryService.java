package com.index325.dscatalog.services;

import com.index325.dscatalog.dto.CategoryDTO;
import com.index325.dscatalog.entities.Category;
import com.index325.dscatalog.repositories.CategoryRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);

        return categories.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

//        if (!optionalCategory.isPresent()){
//
//        }

        Category entity = optionalCategory.orElseThrow(() -> new EntityNotFoundException("Such category was not found"));

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();

        entity.setName(dto.getName());

        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());

            entity = categoryRepository.save(entity);

            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Such category was not found");
        }

    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("This category that you`re trying to delete was not found");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation check your request and try again");
        }
    }
}
