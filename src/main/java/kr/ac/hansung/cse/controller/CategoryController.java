package kr.ac.hansung.cse.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.model.CategoryForm;
import kr.ac.hansung.cse.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.exception.CategoryInUseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categoryList";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categoryForm";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "categoryForm";
        }

        try {
            Category savedCategory = categoryService.createCategory(categoryForm.getName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "'" + savedCategory.getName() + "' 카테고리가 성공적으로 등록되었습니다.");
            return "redirect:/categories";
        } catch (DuplicateCategoryException e) {
            bindingResult.addError(new FieldError("categoryForm", "name", e.getMessage()));
            return "categoryForm";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
            String categoryName = category.getName();
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "'" + categoryName + "' 카테고리가 삭제되었습니다.");
        } catch (CategoryInUseException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/categories";
    }
}
