package com.yassinmouallia.gestiondestock.services.impl;

import com.yassinmouallia.gestiondestock.dto.CategoryDto;
import com.yassinmouallia.gestiondestock.exception.EntityNotFoundException;
import com.yassinmouallia.gestiondestock.exception.ErrorCodes;
import com.yassinmouallia.gestiondestock.exception.InvalidEntityException;
import com.yassinmouallia.gestiondestock.exception.InvalidOperationException;
import com.yassinmouallia.gestiondestock.model.Article;
import com.yassinmouallia.gestiondestock.repository.ArticleRepository;
import com.yassinmouallia.gestiondestock.repository.CategoryRepository;
import com.yassinmouallia.gestiondestock.services.CategoryService;
import com.yassinmouallia.gestiondestock.validator.CategoryValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  private CategoryRepository categoryRepository;
  private ArticleRepository articleRepository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
    this.categoryRepository = categoryRepository;
    this.articleRepository = articleRepository;
  }

  @Override
  public CategoryDto save(CategoryDto dto) {
    List<String> errors = CategoryValidator.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntityException("La category n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
    }
    return CategoryDto.fromEntity(
        categoryRepository.save(CategoryDto.toEntity(dto))
    );
  }

  @Override
  public CategoryDto findById(Integer id) {
    if (id == null) {
      log.error("Category ID is null");
      return null;
    }
    return categoryRepository.findById(id)
        .map(CategoryDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
            "Aucune category avec l'ID = " + id + " n' ete trouve dans la BDD",
            ErrorCodes.CATEGORY_NOT_FOUND)
        );
  }

  @Override
  public CategoryDto findByCode(String code) {
    if (!StringUtils.hasLength(code)) {
      log.error("Category CODE is null");
      return null;
    }
    return categoryRepository.findCategoryByCode(code)
        .map(CategoryDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
            "Aucune category avec le CODE = " + code + " n' ete trouve dans la BDD",
            ErrorCodes.CATEGORY_NOT_FOUND)
        );
  }

  @Override
  public List<CategoryDto> findAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Category ID is null");
      return;
    }
    List<Article> articles = articleRepository.findAllByCategoryId(id);
    if (!articles.isEmpty()) {
      throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilise",
          ErrorCodes.CATEGORY_ALREADY_IN_USE);
    }
    categoryRepository.deleteById(id);
  }
}
