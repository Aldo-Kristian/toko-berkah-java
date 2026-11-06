package com.example.sembakoapp.data.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.sembakoapp.data.SembakoException;
import com.example.sembakoapp.data.model.Product;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteSembakoRepositoryTest {

  private MockWebServer server;
  private RemoteSembakoRepository repository;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.start();
    SembakoApiService api =
        new Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SembakoApiService.class);
    repository = new RemoteSembakoRepository(api);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdown();
  }

  @Test
  public void getProductsParsesListAndPath() throws Exception {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(
                "[{\"id\":1,\"name\":\"Beras\",\"categoryId\":2,\"supplierId\":3,"
                    + "\"price\":78000,\"stock\":4,\"minStock\":10,\"unit\":\"karung\","
                    + "\"categoryName\":\"Beras & Biji-bijian\","
                    + "\"supplierName\":\"PT Sumber\"}]"));

    List<Product> products = repository.getProducts();

    assertEquals(1, products.size());
    assertEquals("Beras", products.get(0).name);
    assertEquals(1, products.get(0).id);
    assertEquals("/api/products", server.takeRequest().getPath());
  }

  @Test
  public void deleteProductUsesDelete() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(204));

    repository.deleteProduct(7);

    RecordedRequest request = server.takeRequest();
    assertEquals("DELETE", request.getMethod());
    assertEquals("/api/products/7", request.getPath());
  }

  @Test
  public void httpErrorBecomesSembakoException() {
    server.enqueue(new MockResponse().setResponseCode(500));

    SembakoException error = assertThrows(SembakoException.class, repository::getProducts);
    assertTrue(error.getMessage().contains("500"));
  }

  @Test
  public void malformedJsonBecomesSembakoException() {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("not json"));

    assertThrows(SembakoException.class, repository::getProducts);
  }
}
