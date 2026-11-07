package com.example.sembakoapi.web;

import com.example.sembakoapi.model.DashboardSummary;
import com.example.sembakoapi.model.Product;
import com.example.sembakoapi.repository.CategoryRepository;
import com.example.sembakoapi.repository.ProductRepository;
import com.example.sembakoapi.repository.SupplierRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final ProductRepository products;
  private final CategoryRepository categories;
  private final SupplierRepository suppliers;

  public DashboardController(ProductRepository products, CategoryRepository categories, SupplierRepository suppliers) {
    this.products = products; this.categories = categories; this.suppliers = suppliers;
  }

  @GetMapping("/summary")
  public DashboardSummary summary() {
    DashboardSummary s = new DashboardSummary();
    s.totalProducts = (int) products.count();
    s.totalCategories = (int) categories.count();
    s.totalSuppliers = (int) suppliers.count();
    for (Product p : products.findAll()) {
      if (p.stock <= p.minStock) s.lowStockCount++;
    }
    return s;
  }
}
