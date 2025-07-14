package ru.zaikin.manager_app.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zaikin.manager_app.entity.Product;
import ru.zaikin.manager_app.entity.UpdateProductPayload;
import ru.zaikin.manager_app.payload.NewProductPayload;
import ru.zaikin.manager_app.service.ProductService;

@Controller
@RequestMapping("catalogue/products/{productId:\\d+}")
@AllArgsConstructor
public class ProductController {


    private final ProductService productService;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId).orElseThrow();
    }

    @GetMapping
    public String getProduct(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("product", this.productService.findProduct(productId).orElseThrow());
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductDetail(@PathVariable("productId") int productId, Model model) {
        Product product = this.productService.findProduct(productId).orElseThrow();
        model.addAttribute("product", product);
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }


}
