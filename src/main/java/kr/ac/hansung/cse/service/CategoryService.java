package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.CategoryInUseException;
import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findByIdWithProducts(id);
    }

    @Transactional // readOnly 오버라이드 → 쓰기 허용
    public Category createCategory(String name) {
        // 중복 검사: 이름이 이미 있으면 예외 발생
        categoryRepository.findByName(name)
            .ifPresent(c -> { throw new DuplicateCategoryException(name); });
        return categoryRepository.save(new Category(name)); 
    }

    @Transactional
    public void deleteCategory(Long id) {
        long count = categoryRepository.countProductsByCategoryId(id);
        if (count > 0) throw new CategoryInUseException((int) count);
        categoryRepository.delete(id); 
    }
}
