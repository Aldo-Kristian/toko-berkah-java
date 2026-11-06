package com.example.sembakoapp.ui.suppliers;

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
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuppliersViewModel extends AndroidViewModel {

  private final SembakoRepository repository = ServiceLocator.repository();
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  private final MutableLiveData<List<Supplier>> suppliers = new MutableLiveData<>();
  private final MutableLiveData<String> message = new MutableLiveData<>();

  public SuppliersViewModel(@NonNull Application application) {
    super(application);
    refresh();
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

  public void refresh() {
    executor.execute(() -> {
      try {
        final List<Supplier> loaded = repository.getSuppliers();
        mainHandler.post(() -> suppliers.setValue(loaded));
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void save(Supplier supplier) {
    executor.execute(() -> {
      try {
        if (supplier.id == 0) repository.addSupplier(supplier);
        else repository.updateSupplier(supplier);
        mainHandler.post(this::refresh);
      } catch (SembakoException e) {
        mainHandler.post(() -> message.setValue(e.getMessage()));
      }
    });
  }

  public void delete(Supplier supplier) {
    executor.execute(() -> {
      try {
        int used = 0;
        for (Product p : repository.getProducts()) {
          if (p.supplierId == supplier.id) used++;
        }
        if (used > 0) {
          final String text = getApplication().getString(R.string.supplier_in_use, used);
          mainHandler.post(() -> message.setValue(text));
          return;
        }
        repository.deleteSupplier(supplier.id);
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
