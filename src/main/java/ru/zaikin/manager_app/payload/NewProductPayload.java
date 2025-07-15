package ru.zaikin.manager_app.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull @Size(min = 3, max = 50, message="{catalogue.products.create.errors.title_is_invalid}") String title,
        @NotNull @Size(min = 3, max = 50, message="{catalogue.products.create.errors.details_is_invalid}") String details
) {
}
