package com.project.Ecomsite.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Ecomsite.model.Category;
import com.project.Ecomsite.model.Product;
import com.project.Ecomsite.service.CategoryService;
import com.project.Ecomsite.service.ProductService;

@Controller
public class ViewController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "index"; // Renders src/main/resources/templates/index.html
    }

    @GetMapping("/products")
    public String products(@RequestParam(name = "categoryId", required = false) Long categoryId, Model model) {
        List<Product> products;
        if (categoryId != null) {
            products = productService.getProductsByCategoryId(categoryId);
            categoryService.getCategoryById(categoryId).ifPresent(category ->
                model.addAttribute("currentCategory", category.getName()));
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products"; // Renders src/main/resources/templates/products.html
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        return productService.getProductById(id).map(product -> {
            model.addAttribute("product", product);
            return "product_detail"; // Renders src/main/resources/templates/product_detail.html
        }).orElse("error/404"); // Or redirect to a 404 page
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories"; // Renders src/main/resources/templates/categories.html
    }
}