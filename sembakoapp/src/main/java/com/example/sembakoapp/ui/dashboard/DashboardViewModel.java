package com.example.sembakoapp.ui.dashboard;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sembakoapp.data.SembakoException;
import com.example.sembakoapp.data.SembakoRepository;
import com.example.sembakoapp.data.ServiceLocator;
import com.example.sembakoapp.data.model.DashboardSummary;
import com.example.sembakoapp.data.model.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {

  private final SembakoRepository repository = ServiceLocator.repository();
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  private final MutableLiveData<DashboardSummary> summary =
      new MutableLiveData<>(new DashboardSummary());
  private final MutableLiveData<List<Product>> lowStock =
      new MutableLiveData<>(Collections.emptyList());
  private final MutableLiveData<String> message = new MutableLiveData<>();

  public DashboardViewModel() {
    refresh();
  }

  public LiveData<DashboardSummary> getSummary() {
    return summary;
  }

  public LiveData<List<Product>> getLowStock() {
    return lowStock;
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
        final DashboardSummary loadedSummary = repository.getSummary();
        final List<Product> low = new ArrayList<>();
        for (Product p : repository.getProducts()) if (p.isLowStock()) low.add(p);
        Collections.sort(low, (a, b) -> Integer.compare(a.stock, b.stock));

        mainHandler.post(() -> {
          summary.setValue(loadedSummary);
          lowStock.setValue(low);
        });
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
