package com.index325.dscatalog.services;

import com.index325.dscatalog.dto.CategoryDTO;
import com.index325.dscatalog.entities.Category;
import com.index325.dscatalog.repositories.CategoryRepository;
import com.index325.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
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
}
