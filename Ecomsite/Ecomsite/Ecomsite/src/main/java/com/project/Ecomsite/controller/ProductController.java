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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.Ecomsite.model.Product;
import com.project.Ecomsite.service.CategoryService;
import com.project.Ecomsite.service.ProductService;

@Controller
@RequestMapping("/admin/products") // Admin path for product management
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Display all products (admin view)
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products/list"; // Renders src/main/resources/templates/admin/products/list.html
    }

    // Show form to add new product
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); // Needed for category dropdown
        return "admin/products/create_edit"; // Renders src/main/resources/templates/admin/products/create_edit.html
    }

    // Save a new product
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("categoryId") Long categoryId,
                              RedirectAttributes redirectAttributes) {
        categoryService.getCategoryById(categoryId).ifPresent(product::setCategory);
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
        return "redirect:/admin/products";
    }

    // Show form to edit product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id).map(product -> {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/products/create_edit";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Product not found!");
            return "redirect:/admin/products";
        });
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/admin/products";
    }
}