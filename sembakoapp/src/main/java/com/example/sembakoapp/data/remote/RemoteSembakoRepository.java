package com.example.sembakoapp.data.remote;

import com.example.sembakoapp.data.SembakoException;
import com.example.sembakoapp.data.SembakoRepository;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.DashboardSummary;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

public class RemoteSembakoRepository implements SembakoRepository {

  private final SembakoApiService api;

  public RemoteSembakoRepository(SembakoApiService api) {
    this.api = api;
  }

  @Override
  public List<Category> getCategories() {
    List<Category> categories = call(api.getCategories());
    return categories == null ? Collections.emptyList() : categories;
  }

  @Override
  public Category addCategory(Category category) {
    return call(api.createCategory(category));
  }

  @Override
  public void updateCategory(Category category) {
    call(api.updateCategory(category.id, category));
  }

  @Override
  public void deleteCategory(long id) {
    call(api.deleteCategory(id));
  }

  @Override
  public List<Supplier> getSuppliers() {
    List<Supplier> suppliers = call(api.getSuppliers());
    return suppliers == null ? Collections.emptyList() : suppliers;
  }

  @Override
  public Supplier addSupplier(Supplier supplier) {
    return call(api.createSupplier(supplier));
  }

  @Override
  public void updateSupplier(Supplier supplier) {
    call(api.updateSupplier(supplier.id, supplier));
  }

  @Override
  public void deleteSupplier(long id) {
    call(api.deleteSupplier(id));
  }

  @Override
  public List<Product> getProducts() {
    List<Product> products = call(api.getProducts());
    return products == null ? Collections.emptyList() : products;
  }

  @Override
  public Product addProduct(Product product) {
    return call(api.createProduct(product));
  }

  @Override
  public void updateProduct(Product product) {
    call(api.updateProduct(product.id, product));
  }

  @Override
  public void deleteProduct(long id) {
    call(api.deleteProduct(id));
  }

  @Override
  public DashboardSummary getSummary() {
    DashboardSummary summary = call(api.getSummary());
    return summary == null ? new DashboardSummary() : summary;
  }

  private <T> T call(Call<T> call) {
    try {
      Response<T> response = call.execute();
      if (!response.isSuccessful()) throw new HttpException(response);
      return response.body();
    } catch (HttpException e) {
      throw new SembakoException("Server menolak permintaan (" + e.code() + ").", e);
    } catch (IOException e) {
      throw new SembakoException(
          "Tidak dapat terhubung ke server. Periksa koneksi dan alamat API.", e);
    } catch (RuntimeException e) {
      throw new SembakoException("Terjadi kesalahan saat menghubungi server.", e);
    }
  }
}
