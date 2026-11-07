package com.example.sembakoapi.web;

import com.example.sembakoapi.model.Product;
import com.example.sembakoapi.repository.CategoryRepository;
import com.example.sembakoapi.repository.ProductRepository;
import com.example.sembakoapi.repository.SupplierRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository products;
  private final CategoryRepository categories;
  private final SupplierRepository suppliers;

  public ProductController(ProductRepository products, CategoryRepository categories, SupplierRepository suppliers) {
    this.products = products; this.categories = categories; this.suppliers = suppliers;
  }

  @GetMapping
  public List<Product> list() {
    List<Product> all = products.findAll();
    all.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    all.forEach(this::withJoinedNames);
    return all;
  }

  @PostMapping
  public Product create(@RequestBody Product body) {
    body.id = null;
    if (body.name == null || body.name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama wajib diisi");
    return withJoinedNames(products.save(body));
  }

  @PutMapping("/{id}")
  public Product update(@PathVariable Long id, @RequestBody Product body) {
    if (body.name == null || body.name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama wajib diisi");
    Product existing = products.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.name = body.name; existing.categoryId = body.categoryId; existing.supplierId = body.supplierId;
    existing.price = body.price; existing.stock = body.stock; existing.minStock = body.minStock; existing.unit = body.unit;
    return withJoinedNames(products.save(existing));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    if (!products.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    products.deleteById(id);
  }

  private Product withJoinedNames(Product p) {
    p.categoryName = p.categoryId == null ? null : categories.findById(p.categoryId).map(c -> c.name).orElse(null);
    p.supplierName = p.supplierId == null ? null : suppliers.findById(p.supplierId).map(s -> s.name).orElse(null);
    return p;
  }
}
