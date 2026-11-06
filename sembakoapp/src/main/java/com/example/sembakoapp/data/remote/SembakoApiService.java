package com.example.sembakoapp.data.remote;

import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.DashboardSummary;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** REST contract that the API service should implement. */
public interface SembakoApiService {

  @GET("api/products")
  Call<List<Product>> getProducts();

  @POST("api/products")
  Call<Product> createProduct(@Body Product product);

  @PUT("api/products/{id}")
  Call<Product> updateProduct(@Path("id") long id, @Body Product product);

  @DELETE("api/products/{id}")
  Call<Void> deleteProduct(@Path("id") long id);

  @GET("api/categories")
  Call<List<Category>> getCategories();

  @POST("api/categories")
  Call<Category> createCategory(@Body Category category);

  @PUT("api/categories/{id}")
  Call<Category> updateCategory(@Path("id") long id, @Body Category category);

  @DELETE("api/categories/{id}")
  Call<Void> deleteCategory(@Path("id") long id);

  @GET("api/suppliers")
  Call<List<Supplier>> getSuppliers();

  @POST("api/suppliers")
  Call<Supplier> createSupplier(@Body Supplier supplier);

  @PUT("api/suppliers/{id}")
  Call<Supplier> updateSupplier(@Path("id") long id, @Body Supplier supplier);

  @DELETE("api/suppliers/{id}")
  Call<Void> deleteSupplier(@Path("id") long id);

  @GET("api/dashboard/summary")
  Call<DashboardSummary> getSummary();
}
