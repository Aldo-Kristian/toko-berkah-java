package com.example.sembakoapp.ui.products;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.databinding.FragmentProductsBinding;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class ProductsFragment extends Fragment implements ProductAdapter.Listener {

  private FragmentProductsBinding binding;
  private ProductsViewModel viewModel;
  private ProductAdapter adapter;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentProductsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

    adapter = new ProductAdapter(this);
    binding.rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.rvProducts.setAdapter(adapter);

    binding.fabAdd.setOnClickListener(v ->
        ProductFormBottomSheet.newInstance(null)
            .show(getChildFragmentManager(), ProductFormBottomSheet.TAG));

    binding.btnFilter.setOnClickListener(v ->
        ProductFilterBottomSheet.newInstance()
            .show(getChildFragmentManager(), ProductFilterBottomSheet.TAG));

    binding.etSearch.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int a, int b, int c) {}

          @Override
          public void onTextChanged(CharSequence s, int a, int b, int c) {
            viewModel.setQuery(s.toString());
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });

    viewModel
        .getProducts()
        .observe(
            getViewLifecycleOwner(),
            products -> {
              if (products == null) return;
              adapter.submit(products);

              boolean empty = products.isEmpty();
              binding.rvProducts.setVisibility(empty ? View.GONE : View.VISIBLE);
              binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);

              if (empty) {
                boolean filtered =
                    viewModel.isFilterActive()
                        || !binding.etSearch.getText().toString().trim().isEmpty();
                binding.tvEmptyTitle.setText(
                    filtered ? R.string.empty_filter_title : R.string.empty_products_title);
                binding.tvEmptySubtitle.setText(
                    filtered ? R.string.empty_filter_subtitle : R.string.empty_products_subtitle);
              }

              updateFilterIcon();
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

  private void updateFilterIcon() {
    int color =
        MaterialColors.getColor(
            binding.btnFilter,
            viewModel.isFilterActive()
                ? androidx.appcompat.R.attr.colorPrimary
                : com.google.android.material.R.attr.colorOnSurfaceVariant);
    binding.btnFilter.setIconTint(ColorStateList.valueOf(color));
  }

  @Override
  public void onEdit(Product product) {
    ProductFormBottomSheet.newInstance(product)
        .show(getChildFragmentManager(), ProductFormBottomSheet.TAG);
  }

  @Override
  public void onDelete(Product product) {
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.dialog_delete_product_title)
        .setMessage(getString(R.string.dialog_delete_product_message, product.name))
        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(R.string.action_delete, (dialog, which) -> viewModel.delete(product))
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
