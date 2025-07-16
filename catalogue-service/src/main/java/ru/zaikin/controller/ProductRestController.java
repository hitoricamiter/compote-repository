package ru.zaikin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zaikin.controller.payload.UpdateProductPayload;
import ru.zaikin.entity.Product;
import ru.zaikin.service.ProductService;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products/{productId:\\d+}")
public class ProductRestController {
    private final ProductService productService;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable int productId) {
        return this.productService.findProduct(productId).orElseThrow(() -> new NoSuchMessageException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@PathVariable int productId, @Valid @RequestBody UpdateProductPayload payload,
                                           BindingResult bindingResult, Locale locale) {
        if (bindingResult.hasErrors()) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    this.messageSource.getMessage("errors.400.title", new Object[0], "errors.400.title", locale));

            problemDetail.setProperty("errors",
                    bindingResult
                            .getAllErrors()
                            .stream()
                            .map(MessageSourceResolvable::getDefaultMessage)
                            .toList());

            return ResponseEntity
                    .badRequest()
                    .body(problemDetail);
        } else {
            this.productService.updateProduct(productId, payload.title(), payload.details());
            return ResponseEntity.noContent().build();
        }

    }
}