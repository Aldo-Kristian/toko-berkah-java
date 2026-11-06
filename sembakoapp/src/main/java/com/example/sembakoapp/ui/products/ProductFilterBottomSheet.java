package com.example.sembakoapp.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.Supplier;
import com.example.sembakoapp.databinding.SheetProductFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ProductFilterBottomSheet extends BottomSheetDialogFragment {

  public static final String TAG = "ProductFilterBottomSheet";

  private SheetProductFilterBinding binding;
  private ProductsViewModel viewModel;

  private final List<Category> categories = new ArrayList<>();
  private final List<Supplier> suppliers = new ArrayList<>();
  private long selectedCategoryId = 0L;
  private long selectedSupplierId = 0L;

  public static ProductFilterBottomSheet newInstance() {
    return new ProductFilterBottomSheet();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = SheetProductFilterBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(requireParentFragment()).get(ProductsViewModel.class);

    selectedCategoryId = viewModel.getCategoryFilter();
    selectedSupplierId = viewModel.getSupplierFilter();

    if (viewModel.getMinStockFilter() >= 0) {
      binding.etMinStock.setText(String.valueOf(viewModel.getMinStockFilter()));
    }
    if (viewModel.getMaxStockFilter() >= 0) {
      binding.etMaxStock.setText(String.valueOf(viewModel.getMaxStockFilter()));
    }

    viewModel.getCategories().observe(getViewLifecycleOwner(), this::bindCategories);
    viewModel.getSuppliers().observe(getViewLifecycleOwner(), this::bindSuppliers);

    binding.btnApply.setOnClickListener(v -> {
      viewModel.setFilters(
          selectedCategoryId,
          selectedSupplierId,
          valueOf(binding.etMinStock),
          valueOf(binding.etMaxStock));
      dismiss();
    });

    binding.btnReset.setOnClickListener(v -> {
      viewModel.resetFilters();
      dismiss();
    });
  }

  private void bindCategories(List<Category> list) {
    if (list == null) return;
    categories.clear();
    categories.addAll(list);

    List<String> names = new ArrayList<>();
    names.add(getString(R.string.filter_all_categories));
    for (Category c : categories) names.add(c.name);

    binding.actCategory.setAdapter(
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, names));
    binding.actCategory.setOnItemClickListener((parent, v, position, id) ->
        selectedCategoryId = position == 0 ? 0L : categories.get(position - 1).id);

    binding.actCategory.setText(
        selectedCategoryId == 0 ? names.get(0) : nameOfCategory(selectedCategoryId), false);
  }

  private void bindSuppliers(List<Supplier> list) {
    if (list == null) return;
    suppliers.clear();
    suppliers.addAll(list);

    List<String> names = new ArrayList<>();
    names.add(getString(R.string.filter_all_suppliers));
    for (Supplier s : suppliers) names.add(s.name);

    binding.actSupplier.setAdapter(
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, names));
    binding.actSupplier.setOnItemClickListener((parent, v, position, id) ->
        selectedSupplierId = position == 0 ? 0L : suppliers.get(position - 1).id);

    binding.actSupplier.setText(
        selectedSupplierId == 0 ? names.get(0) : nameOfSupplier(selectedSupplierId), false);
  }

  private String nameOfCategory(long id) {
    for (Category c : categories) if (c.id == id) return c.name;
    return getString(R.string.filter_all_categories);
  }

  private String nameOfSupplier(long id) {
    for (Supplier s : suppliers) if (s.id == id) return s.name;
    return getString(R.string.filter_all_suppliers);
  }

  private static int valueOf(TextInputEditText field) {
    String value = field.getText() == null ? "" : field.getText().toString().trim();
    if (value.isEmpty()) return -1;
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
