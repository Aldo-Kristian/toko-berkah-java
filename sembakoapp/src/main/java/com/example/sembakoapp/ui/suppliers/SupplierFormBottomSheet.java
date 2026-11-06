package com.example.sembakoapp.ui.suppliers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Supplier;
import com.example.sembakoapp.databinding.SheetSupplierFormBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SupplierFormBottomSheet extends BottomSheetDialogFragment {

  public static final String TAG = "SupplierFormBottomSheet";
  private static final String ARG_SUPPLIER = "arg_supplier";

  private SheetSupplierFormBinding binding;
  private SuppliersViewModel viewModel;
  private Supplier editing;

  public static SupplierFormBottomSheet newInstance(@Nullable Supplier supplier) {
    SupplierFormBottomSheet sheet = new SupplierFormBottomSheet();
    if (supplier != null) {
      Bundle args = new Bundle();
      args.putSerializable(ARG_SUPPLIER, supplier);
      sheet.setArguments(args);
    }
    return sheet;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = SheetSupplierFormBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(requireParentFragment()).get(SuppliersViewModel.class);

    if (getArguments() != null) {
      editing = (Supplier) getArguments().getSerializable(ARG_SUPPLIER);
    }

    binding.tvTitle.setText(
        editing == null ? R.string.form_add_supplier : R.string.form_edit_supplier);

    if (editing != null) {
      binding.etName.setText(editing.name);
      binding.etPhone.setText(editing.phone);
      binding.etAddress.setText(editing.address);
    }

    binding.btnSave.setOnClickListener(v -> save());
  }

  private void save() {
    binding.tilName.setError(null);

    String name =
        binding.etName.getText() == null ? "" : binding.etName.getText().toString().trim();
    String phone =
        binding.etPhone.getText() == null ? "" : binding.etPhone.getText().toString().trim();
    String address =
        binding.etAddress.getText() == null ? "" : binding.etAddress.getText().toString().trim();

    if (name.isEmpty()) {
      binding.tilName.setError(getString(R.string.error_required));
      return;
    }

    Supplier supplier = editing != null ? editing : new Supplier();
    supplier.name = name;
    supplier.phone = phone;
    supplier.address = address;

    viewModel.save(supplier);
    dismiss();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
