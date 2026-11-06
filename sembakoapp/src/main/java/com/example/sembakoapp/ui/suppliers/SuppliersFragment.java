package com.example.sembakoapp.ui.suppliers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Supplier;
import com.example.sembakoapp.databinding.FragmentSuppliersBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class SuppliersFragment extends Fragment implements SupplierAdapter.Listener {

  private FragmentSuppliersBinding binding;
  private SuppliersViewModel viewModel;
  private SupplierAdapter adapter;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSuppliersBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(this).get(SuppliersViewModel.class);

    adapter = new SupplierAdapter(this);
    binding.rvSuppliers.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.rvSuppliers.setAdapter(adapter);

    binding.fabAdd.setOnClickListener(v ->
        SupplierFormBottomSheet.newInstance(null)
            .show(getChildFragmentManager(), SupplierFormBottomSheet.TAG));

    viewModel
        .getSuppliers()
        .observe(
            getViewLifecycleOwner(),
            suppliers -> {
              if (suppliers == null) return;
              adapter.submit(suppliers);
              binding.emptyState.setVisibility(suppliers.isEmpty() ? View.VISIBLE : View.GONE);
            });

    viewModel
        .getMessage()
        .observe(
            getViewLifecycleOwner(),
            text -> {
              if (text == null) return;
              Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG).show();
              viewModel.clearMessage();
            });
  }

  @Override
  public void onEdit(Supplier supplier) {
    SupplierFormBottomSheet.newInstance(supplier)
        .show(getChildFragmentManager(), SupplierFormBottomSheet.TAG);
  }

  @Override
  public void onDelete(Supplier supplier) {
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.dialog_delete_supplier_title)
        .setMessage(getString(R.string.dialog_delete_supplier_message, supplier.name))
        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(R.string.action_delete, (dialog, which) -> viewModel.delete(supplier))
        .show();
  }

  @Override
  public void onResume() {
    super.onResume();
    viewModel.refresh();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
