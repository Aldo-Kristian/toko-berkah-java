package com.example.sembakoapi.web;

import com.example.sembakoapi.model.Category;
import com.example.sembakoapi.repository.CategoryRepository;
import com.example.sembakoapi.repository.ProductRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryRepository categories;
  private final ProductRepository products;

  public CategoryController(CategoryRepository categories, ProductRepository products) {
    this.categories = categories; this.products = products;
  }

  @GetMapping
  public List<Category> list() {
    List<Category> all = categories.findAll();
    all.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    return all;
  }

  @PostMapping
  public Category create(@RequestBody Category body) {
    body.id = null;
    if (body.name == null || body.name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return categories.save(body);
  }

  @PutMapping("/{id}")
  public Category update(@PathVariable Long id, @RequestBody Category body) {
    Category existing = categories.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.name = body.name; existing.description = body.description;
    return categories.save(existing);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    if (!categories.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    long used = products.countByCategoryId(id);
    if (used > 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Dipakai oleh produk");
    categories.deleteById(id);
  }
}
