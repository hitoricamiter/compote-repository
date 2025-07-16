package ru.zaikin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.zaikin.controller.payload.NewProductPayload;
import ru.zaikin.entity.Product;
import ru.zaikin.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload, BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException e) {
                throw e;
            } else {
                throw new BindException(bindingResult);
            }

        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity.created(uriBuilder
                            .replacePath("/catalogue-api/products/{productId}").build(Map.of("productId", product.getId())))
                    .body(product);

        }
    }
}
