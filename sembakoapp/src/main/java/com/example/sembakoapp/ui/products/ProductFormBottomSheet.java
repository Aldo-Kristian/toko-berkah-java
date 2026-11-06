package com.example.sembakoapp.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.data.model.Supplier;
import com.example.sembakoapp.databinding.SheetProductFormBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class ProductFormBottomSheet extends BottomSheetDialogFragment {

  public static final String TAG = "ProductFormBottomSheet";
  private static final String ARG_PRODUCT = "arg_product";
  private static final String[] UNITS = {
    "pcs", "kg", "gram", "liter", "botol", "bungkus", "karung", "kotak", "pack"
  };

  private SheetProductFormBinding binding;
  private ProductsViewModel viewModel;
  private Product editing;

  private final List<Category> categories = new ArrayList<>();
  private final List<Supplier> suppliers = new ArrayList<>();
  private long selectedCategoryId = 0L;
  private long selectedSupplierId = 0L;

  public static ProductFormBottomSheet newInstance(@Nullable Product product) {
    ProductFormBottomSheet sheet = new ProductFormBottomSheet();
    if (product != null) {
      Bundle args = new Bundle();
      args.putSerializable(ARG_PRODUCT, product);
      sheet.setArguments(args);
    }
    return sheet;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = SheetProductFormBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(requireParentFragment()).get(ProductsViewModel.class);

    if (getArguments() != null) {
      editing = (Product) getArguments().getSerializable(ARG_PRODUCT);
    }

    binding.tvTitle.setText(
        editing == null ? R.string.form_add_product : R.string.form_edit_product);

    binding.actUnit.setAdapter(
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, UNITS));
    binding.actUnit.setText(editing != null && editing.unit != null ? editing.unit : "pcs", false);

    viewModel.getCategories().observe(getViewLifecycleOwner(), this::bindCategories);
    viewModel.getSuppliers().observe(getViewLifecycleOwner(), this::bindSuppliers);

    if (editing != null) {
      binding.etName.setText(editing.name);
      binding.etPrice.setText(String.valueOf(editing.price));
      binding.etStock.setText(String.valueOf(editing.stock));
      binding.etMinStock.setText(String.valueOf(editing.minStock));
      selectedCategoryId = editing.categoryId;
      selectedSupplierId = editing.supplierId;
    }

    binding.btnSave.setOnClickListener(v -> save());
  }

  private void bindCategories(List<Category> list) {
    if (list == null) return;
    categories.clear();
    categories.addAll(list);

    List<String> names = new ArrayList<>();
    for (Category c : categories) names.add(c.name);
    binding.actCategory.setAdapter(
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, names));
    binding.actCategory.setOnItemClickListener(
        (parent, v, position, id) -> selectedCategoryId = categories.get(position).id);

    if (selectedCategoryId != 0) {
      for (Category c : categories) {
        if (c.id == selectedCategoryId) {
          binding.actCategory.setText(c.name, false);
          break;
        }
      }
    }
  }

  private void bindSuppliers(List<Supplier> list) {
    if (list == null) return;
    suppliers.clear();
    suppliers.addAll(list);

    List<String> names = new ArrayList<>();
    for (Supplier s : suppliers) names.add(s.name);
    binding.actSupplier.setAdapter(
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, names));
    binding.actSupplier.setOnItemClickListener(
        (parent, v, position, id) -> selectedSupplierId = suppliers.get(position).id);

    if (selectedSupplierId != 0) {
      for (Supplier s : suppliers) {
        if (s.id == selectedSupplierId) {
          binding.actSupplier.setText(s.name, false);
          break;
        }
      }
    }
  }

  private void save() {
    clearErrors();

    String name = textOf(binding.etName);
    long price = parseDouble(textOf(binding.etPrice));
    int stock = parseInt(textOf(binding.etStock));
    int minStock = parseInt(textOf(binding.etMinStock));
    String unit = textOf(binding.actUnit);

    boolean valid = true;
    if (name.isEmpty()) {
      binding.tilName.setError(getString(R.string.error_required));
      valid = false;
    }
    if (selectedCategoryId == 0) {
      binding.tilCategory.setError(getString(R.string.error_required));
      valid = false;
    }
    if (selectedSupplierId == 0) {
      binding.tilSupplier.setError(getString(R.string.error_required));
      valid = false;
    }
    if (price < 0) {
      binding.tilPrice.setError(getString(R.string.error_invalid_number));
      valid = false;
    }
    if (stock < 0) {
      binding.tilStock.setError(getString(R.string.error_invalid_number));
      valid = false;
    }
    if (minStock < 0) {
      binding.tilMinStock.setError(getString(R.string.error_invalid_number));
      valid = false;
    }
    if (!valid) return;

    Product product = editing != null ? editing : new Product();
    product.name = name;
    product.categoryId = selectedCategoryId;
    product.supplierId = selectedSupplierId;
    product.price = price;
    product.stock = stock;
    product.minStock = minStock;
    product.unit = unit.isEmpty() ? "pcs" : unit;

    viewModel.save(product);
    dismiss();
  }

  private void clearErrors() {
    binding.tilName.setError(null);
    binding.tilCategory.setError(null);
    binding.tilSupplier.setError(null);
    binding.tilPrice.setError(null);
    binding.tilStock.setError(null);
    binding.tilMinStock.setError(null);
  }

  private static String textOf(TextView field) {
    return field.getText() == null ? "" : field.getText().toString().trim();
  }

  private static int parseInt(String value) {
    if (value.isEmpty()) return -1;
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private static long parseDouble(String value) {
    if (value.isEmpty()) return -1;
    try {
      return (long) Double.parseDouble(value);
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
