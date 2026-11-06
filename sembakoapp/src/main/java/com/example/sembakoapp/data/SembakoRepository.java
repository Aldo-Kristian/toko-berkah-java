package com.example.sembakoapp.data;

import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.DashboardSummary;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.util.List;

/**
 * Synchronous data source. Call it from a background thread (the ViewModels do this). Implement
 * this interface with Retrofit later; the UI does not change.
 */
public interface SembakoRepository {

  List<Category> getCategories();

  Category addCategory(Category category);

  void updateCategory(Category category);

  void deleteCategory(long id);

  List<Supplier> getSuppliers();

  Supplier addSupplier(Supplier supplier);

  void updateSupplier(Supplier supplier);

  void deleteSupplier(long id);

  List<Product> getProducts();

  Product addProduct(Product product);

  void updateProduct(Product product);

  void deleteProduct(long id);

  DashboardSummary getSummary();
}
