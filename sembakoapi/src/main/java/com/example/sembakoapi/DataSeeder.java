package com.example.sembakoapi;

import com.example.sembakoapi.model.Category;
import com.example.sembakoapi.model.Product;
import com.example.sembakoapi.model.Supplier;
import com.example.sembakoapi.repository.CategoryRepository;
import com.example.sembakoapi.repository.ProductRepository;
import com.example.sembakoapi.repository.SupplierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final CategoryRepository categories;
  private final SupplierRepository suppliers;
  private final ProductRepository products;

  public DataSeeder(CategoryRepository categories, SupplierRepository suppliers, ProductRepository products) {
    this.categories = categories; this.suppliers = suppliers; this.products = products;
  }

  @Override
  public void run(String... args) {
    if (categories.count() > 0) return;

    Category beras = category("Beras & Biji-bijian", "Beras, kacang-kacangan, dan biji-bijian");
    Category minyak = category("Minyak & Gula", "Minyak goreng dan gula pasir");
    Category mie = category("Mie & Pasta", "Mie instan dan pasta");
    Category minuman = category("Minuman", "Minuman kemasan dan serbuk");
    Category bumbu = category("Bumbu Dapur", "Bumbu dan rempah");
    category("Perawatan & Kebersihan", "Sabun dan kebutuhan rumah");

    Supplier pangan = supplier("PT Sumber Pangan Sejahtera", "021-5550101", "Jl. Raya Cikarang No. 12, Bekasi");
    Supplier makmur = supplier("CV Makmur Abadi", "021-5550202", "Jl. Industri Raya No. 8, Tangerang");
    Supplier berkah = supplier("UD Berkah Jaya", "0812-3456-7890", "Jl. Pasar Baru No. 45, Jakarta");
    supplier("PT Sentosa Niaga", "021-5550404", "Jl. Gatot Subroto No. 99, Jakarta");

    product("Beras Premium 5 kg", beras.id, pangan.id, 78000, 4, 10, "karung");
    product("Beras Medium 5 kg", beras.id, pangan.id, 65000, 24, 10, "karung");
    product("Minyak Goreng 1 L", minyak.id, makmur.id, 18000, 6, 12, "botol");
    product("Gula Pasir 1 kg", minyak.id, makmur.id, 15000, 30, 15, "kg");
    product("Mie Instan Goreng", mie.id, berkah.id, 3500, 120, 40, "bungkus");
    product("Mie Instan Kuah", mie.id, berkah.id, 3300, 15, 40, "bungkus");
    product("Kopi Bubuk 250 g", minuman.id, berkah.id, 22000, 8, 6, "bungkus");
    product("Teh Celup 25 Kantong", minuman.id, berkah.id, 9500, 3, 10, "kotak");
    product("Kecap Manis 600 ml", bumbu.id, makmur.id, 21000, 18, 8, "botol");
    product("Garam Halus 500 g", bumbu.id, berkah.id, 4000, 40, 20, "bungkus");
    product("Sabun Mandi Batang", 6L, berkah.id, 4500, 60, 20, "pcs");
    product("Sabun Cuci Piring 800 ml", 6L, berkah.id, 12500, 5, 10, "botol");
  }

  private Category category(String name, String description) {
    Category c = new Category(); c.name = name; c.description = description;
    return categories.save(c);
  }

  private Supplier supplier(String name, String phone, String address) {
    Supplier s = new Supplier(); s.name = name; s.phone = phone; s.address = address;
    return suppliers.save(s);
  }

  private void product(String name, long categoryId, long supplierId, long price, int stock, int minStock, String unit) {
    Product p = new Product();
    p.name = name; p.categoryId = categoryId; p.supplierId = supplierId;
    p.price = price; p.stock = stock; p.minStock = minStock; p.unit = unit;
    products.save(p);
  }
}
