package ru.zaikin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zaikin.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}
