package ru.zaikin.manager.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zaikin.manager.controller.payload.NewProductPayload;
import ru.zaikin.manager.controller.payload.UpdateProductPayload;
import ru.zaikin.manager.entity.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestProductsRestClient implements ProductsRestClient {
    private final RestClient restClient;
    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REF = new ParameterizedTypeReference<List<Product>>() {};

    @Override
    public List<Product> findAllProducts() {
        return this.restClient
                .get()
                .uri("catalogue-api/products")
                .retrieve()
                .body(PRODUCTS_TYPE_REF);
    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return this.restClient
                    .post()
                    .uri("catalogue-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(title, details))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest e) {
           ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
           throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String details) {
        try {
            this.restClient
                    .patch()
                    .uri("catalogue-api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(title, details))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            Optional.ofNullable(this.restClient.delete()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity());
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException(e);
        }
    }
}
