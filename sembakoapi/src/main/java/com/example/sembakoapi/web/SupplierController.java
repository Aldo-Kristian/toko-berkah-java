package com.example.sembakoapi.web;

import com.example.sembakoapi.model.Supplier;
import com.example.sembakoapi.repository.ProductRepository;
import com.example.sembakoapi.repository.SupplierRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final SupplierRepository suppliers;
  private final ProductRepository products;

  public SupplierController(SupplierRepository suppliers, ProductRepository products) {
    this.suppliers = suppliers; this.products = products;
  }

  @GetMapping
  public List<Supplier> list() {
    List<Supplier> all = suppliers.findAll();
    all.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    return all;
  }

  @PostMapping
  public Supplier create(@RequestBody Supplier body) {
    body.id = null;
    if (body.name == null || body.name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return suppliers.save(body);
  }

  @PutMapping("/{id}")
  public Supplier update(@PathVariable Long id, @RequestBody Supplier body) {
    Supplier existing = suppliers.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.name = body.name; existing.phone = body.phone; existing.address = body.address;
    return suppliers.save(existing);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    if (!suppliers.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    long used = products.countBySupplierId(id);
    if (used > 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Dipakai oleh produk");
    suppliers.deleteById(id);
  }
}
