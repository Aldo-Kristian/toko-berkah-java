package com.example.sembakoapp.ui.products;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sembakoapp.data.SembakoException;
import com.example.sembakoapp.data.SembakoRepository;
import com.example.sembakoapp.data.ServiceLocator;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsViewModel extends ViewModel {

  private final SembakoRepository repository = ServiceLocator.repository();
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
  private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
  private final MutableLiveData<List<Supplier>> suppliers = new MutableLiveData<>();
  private final MutableLiveData<String> message = new MutableLiveData<>();

  private List<Product> allProducts = new ArrayList<>();

  private String query = "";
  private long categoryFilter = 0L;
  private long supplierFilter = 0L;
  private int minStockFilter = -1;
  private int maxStockFilter = -1;

  public ProductsViewModel() {
    refresh();
  }

  public LiveData<List<Product>> getProducts() {
    return products;
  }

  public LiveData<List<Category>> getCategories() {
    return categories;
  }

  public LiveData<List<Supplier>> getSuppliers() {
    return suppliers;
  }

  public LiveData<String> getMessage() {
    return message;
  }

  public void clearMessage() {
    message.setValue(null);
  }

  public long getCategoryFilter() {
    return categoryFilter;
  }

  public long getSupplierFilter() {
    return supplierFilter;
  }

  public int getMinStockFilter() {
    return minStockFilter;
  }

  public int getMaxStockFilter() {
    return maxStockFilter;
  }

  public boolean isFilterActive() {
    return categoryFilter != 0 || supplierFilter != 0 || minStockFilter >= 0 || maxStockFilter >= 0;
  }

  public void refresh() {
    executor.execute(() -> {
      try {
        final List<Product> loadedProducts = repository.getProducts();
        final List<Category> loadedCategories = repository.getCategories();
        final List<Supplier> loadedSuppliers = repository.getSuppliers();

        mainHandler.post(() -> {
          allProducts = loadedProducts;
          categories.setValue(loadedCategories);
          suppliers.setValue(loadedSuppliers);
          applyFilters();
        });
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void setQuery(String value) {
    query = value == null ? "" : value.trim();
    applyFilters();
  }

  public void setFilters(long categoryId, long supplierId, int minStock, int maxStock) {
    categoryFilter = categoryId;
    supplierFilter = supplierId;
    minStockFilter = minStock;
    maxStockFilter = maxStock;
    applyFilters();
  }

  public void resetFilters() {
    setFilters(0, 0, -1, -1);
  }

  public void save(Product product) {
    executor.execute(() -> {
      try {
        if (product.id == 0) repository.addProduct(product);
        else repository.updateProduct(product);
        mainHandler.post(this::refresh);
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void delete(Product product) {
    executor.execute(() -> {
      try {
        repository.deleteProduct(product.id);
        mainHandler.post(this::refresh);
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  private void applyFilters() {
    List<Product> result = new ArrayList<>();
    for (Product p : allProducts) {
      if (!matchesQuery(p)) continue;
      if (categoryFilter != 0 && p.categoryId != categoryFilter) continue;
      if (supplierFilter != 0 && p.supplierId != supplierFilter) continue;
      if (minStockFilter >= 0 && p.stock < minStockFilter) continue;
      if (maxStockFilter >= 0 && p.stock > maxStockFilter) continue;
      result.add(p);
    }
    products.setValue(result);
  }

  private boolean matchesQuery(Product p) {
    if (query.isEmpty()) return true;
    String q = query.toLowerCase();
    return contains(p.name, q) || contains(p.categoryName, q) || contains(p.supplierName, q);
  }

  private boolean contains(String value, String query) {
    return value != null && value.toLowerCase().contains(query);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    executor.shutdownNow();
  }
}
