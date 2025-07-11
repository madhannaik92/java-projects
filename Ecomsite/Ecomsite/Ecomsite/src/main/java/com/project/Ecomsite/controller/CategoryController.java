package com.project.Ecomsite.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.Ecomsite.model.Category;
import com.project.Ecomsite.service.CategoryService;

@Controller
@RequestMapping("/admin/categories") // Admin path for category management
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Display all categories (admin view)
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories/list"; // Renders src/main/resources/templates/admin/categories/list.html
    }

    // Show form to add new category
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/create_edit"; // Renders src/main/resources/templates/admin/categories/create_edit.html
    }

    // Save a new category
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("message", "Category saved successfully!");
        return "redirect:/admin/categories";
    }

    // Show form to edit category
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return categoryService.getCategoryById(id).map(category -> {
            model.addAttribute("category", category);
            return "admin/categories/create_edit";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Category not found!");
            return "redirect:/admin/categories";
        });
    }

    // Delete a category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
        return "redirect:/admin/categories";
    }
}