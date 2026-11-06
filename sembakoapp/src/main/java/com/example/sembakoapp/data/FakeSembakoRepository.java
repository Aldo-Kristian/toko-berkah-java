package com.example.sembakoapp.data;

import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.DashboardSummary;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/** In-memory data so the UI can be built before the API exists. */
public class FakeSembakoRepository implements SembakoRepository {

  private final List<Category> categories = new ArrayList<>();
  private final List<Supplier> suppliers = new ArrayList<>();
  private final List<Product> products = new ArrayList<>();

  private final AtomicLong categorySeq = new AtomicLong(1);
  private final AtomicLong supplierSeq = new AtomicLong(1);
  private final AtomicLong productSeq = new AtomicLong(1);

  public FakeSembakoRepository() {
    seed();
  }

  private void seed() {
    Category beras =
        addCategory(
            new Category(0, "Beras & Biji-bijian", "Beras, kacang-kacangan, dan biji-bijian"));
    Category minyak = addCategory(new Category(0, "Minyak & Gula", "Minyak goreng dan gula pasir"));
    Category mie = addCategory(new Category(0, "Mie & Pasta", "Mie instan dan pasta"));
    Category minuman = addCategory(new Category(0, "Minuman", "Minuman kemasan dan serbuk"));
    Category bumbu = addCategory(new Category(0, "Bumbu Dapur", "Bumbu dan rempah"));
    addCategory(new Category(0, "Perawatan & Kebersihan", "Sabun dan kebutuhan rumah"));

    Supplier pangan =
        addSupplier(
            new Supplier(
                0,
                "PT Sumber Pangan Sejahtera",
                "021-5550101",
                "Jl. Raya Cikarang No. 12, Bekasi"));
    Supplier makmur =
        addSupplier(
            new Supplier(
                0, "CV Makmur Abadi", "021-5550202", "Jl. Industri Raya No. 8, Tangerang"));
    Supplier berkah =
        addSupplier(
            new Supplier(0, "UD Berkah Jaya", "0812-3456-7890", "Jl. Pasar Baru No. 45, Jakarta"));
    addSupplier(
        new Supplier(0, "PT Sentosa Niaga", "021-5550404", "Jl. Gatot Subroto No. 99, Jakarta"));

    addProduct(new Product(0, "Beras Premium 5 kg", beras.id, pangan.id, 78000, 4, 10, "karung"));
    addProduct(new Product(0, "Beras Medium 5 kg", beras.id, pangan.id, 65000, 24, 10, "karung"));
    addProduct(new Product(0, "Minyak Goreng 1 L", minyak.id, makmur.id, 18000, 6, 12, "botol"));
    addProduct(new Product(0, "Gula Pasir 1 kg", minyak.id, makmur.id, 15000, 30, 15, "kg"));
    addProduct(new Product(0, "Mie Instan Goreng", mie.id, berkah.id, 3500, 120, 40, "bungkus"));
    addProduct(new Product(0, "Mie Instan Kuah", mie.id, berkah.id, 3300, 15, 40, "bungkus"));
    addProduct(new Product(0, "Kopi Bubuk 250 g", minuman.id, berkah.id, 22000, 8, 6, "bungkus"));
    addProduct(new Product(0, "Teh Celup 25 Kantong", minuman.id, berkah.id, 9500, 3, 10, "kotak"));
    addProduct(new Product(0, "Kecap Manis 600 ml", bumbu.id, makmur.id, 21000, 18, 8, "botol"));
    addProduct(new Product(0, "Garam Halus 500 g", bumbu.id, berkah.id, 4000, 40, 20, "bungkus"));
    addProduct(new Product(0, "Sabun Mandi Batang", 6, berkah.id, 4500, 60, 20, "pcs"));
    addProduct(new Product(0, "Sabun Cuci Piring 800 ml", 6, berkah.id, 12500, 5, 10, "botol"));
  }

  @Override
  public List<Category> getCategories() {
    List<Category> copy = new ArrayList<>(categories);
    copy.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    return copy;
  }

  @Override
  public Category addCategory(Category category) {
    category.id = categorySeq.getAndIncrement();
    categories.add(category);
    return category;
  }

  @Override
  public void updateCategory(Category category) {
    for (int i = 0; i < categories.size(); i++) {
      if (categories.get(i).id == category.id) {
        categories.set(i, category);
        return;
      }
    }
  }

  @Override
  public void deleteCategory(long id) {
    categories.removeIf(c -> c.id == id);
  }

  @Override
  public List<Supplier> getSuppliers() {
    List<Supplier> copy = new ArrayList<>(suppliers);
    copy.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    return copy;
  }

  @Override
  public Supplier addSupplier(Supplier supplier) {
    supplier.id = supplierSeq.getAndIncrement();
    suppliers.add(supplier);
    return supplier;
  }

  @Override
  public void updateSupplier(Supplier supplier) {
    for (int i = 0; i < suppliers.size(); i++) {
      if (suppliers.get(i).id == supplier.id) {
        suppliers.set(i, supplier);
        return;
      }
    }
  }

  @Override
  public void deleteSupplier(long id) {
    suppliers.removeIf(s -> s.id == id);
  }

  @Override
  public List<Product> getProducts() {
    List<Product> copy = new ArrayList<>(products);
    for (Product p : copy) {
      p.categoryName = categoryName(p.categoryId);
      p.supplierName = supplierName(p.supplierId);
    }
    copy.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
    return copy;
  }

  @Override
  public Product addProduct(Product product) {
    product.id = productSeq.getAndIncrement();
    products.add(product);
    return product;
  }

  @Override
  public void updateProduct(Product product) {
    for (int i = 0; i < products.size(); i++) {
      if (products.get(i).id == product.id) {
        products.set(i, product);
        return;
      }
    }
  }

  @Override
  public void deleteProduct(long id) {
    products.removeIf(p -> p.id == id);
  }

  @Override
  public DashboardSummary getSummary() {
    DashboardSummary summary = new DashboardSummary();
    summary.totalProducts = products.size();
    summary.totalCategories = categories.size();
    summary.totalSuppliers = suppliers.size();
    for (Product p : products) if (p.isLowStock()) summary.lowStockCount++;
    return summary;
  }

  private String categoryName(long id) {
    for (Category c : categories) if (c.id == id) return c.name;
    return null;
  }

  private String supplierName(long id) {
    for (Supplier s : suppliers) if (s.id == id) return s.name;
    return null;
  }
}
