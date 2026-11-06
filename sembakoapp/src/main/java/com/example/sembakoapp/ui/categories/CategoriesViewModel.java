package com.example.sembakoapp.ui.categories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.SembakoException;
import com.example.sembakoapp.data.SembakoRepository;
import com.example.sembakoapp.data.ServiceLocator;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesViewModel extends AndroidViewModel {

  private final SembakoRepository repository = ServiceLocator.repository();
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
  private final MutableLiveData<Map<Long, Integer>> productCounts = new MutableLiveData<>();
  private final MutableLiveData<String> message = new MutableLiveData<>();

  public CategoriesViewModel(@NonNull Application application) {
    super(application);
    refresh();
  }

  public LiveData<List<Category>> getCategories() {
    return categories;
  }

  public LiveData<Map<Long, Integer>> getProductCounts() {
    return productCounts;
  }

  public LiveData<String> getMessage() {
    return message;
  }

  public void clearMessage() {
    message.setValue(null);
  }

  public void refresh() {
    executor.execute(() -> {
      try {
        final List<Category> loadedCategories = repository.getCategories();
        final Map<Long, Integer> counts = new HashMap<>();
        for (Product p : repository.getProducts()) {
          Integer current = counts.get(p.categoryId);
          counts.put(p.categoryId, current == null ? 1 : current + 1);
        }
        mainHandler.post(() -> {
          categories.setValue(loadedCategories);
          productCounts.setValue(counts);
        });
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void save(Category category) {
    executor.execute(() -> {
      try {
        if (category.id == 0) repository.addCategory(category);
        else repository.updateCategory(category);
        mainHandler.post(this::refresh);
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void delete(Category category) {
    executor.execute(() -> {
      try {
        int used = 0;
        for (Product p : repository.getProducts()) {
          if (p.categoryId == category.id) used++;
        }
        if (used > 0) {
          final String text = getApplication().getString(R.string.category_in_use, used);
          mainHandler.post(() -> message.setValue(text));
          return;
        }
        repository.deleteCategory(category.id);
        mainHandler.post(this::refresh);
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    executor.shutdownNow();
  }
}
