package com.example.sembakoapp.ui.dashboard;

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
import com.example.sembakoapp.databinding.FragmentDashboardBinding;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.snackbar.Snackbar;

public class DashboardFragment extends Fragment {

  private FragmentDashboardBinding binding;
  private DashboardViewModel viewModel;
  private LowStockAdapter lowStockAdapter;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentDashboardBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

    lowStockAdapter = new LowStockAdapter();
    binding.rvLowStock.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.rvLowStock.setAdapter(lowStockAdapter);

    binding.swipeRefresh.setOnRefreshListener(() -> viewModel.refresh());

    viewModel
        .getSummary()
        .observe(
            getViewLifecycleOwner(),
            summary -> {
              binding.tvTotalProducts.setText(String.valueOf(summary.totalProducts));
              binding.tvLowStock.setText(String.valueOf(summary.lowStockCount));
              binding.tvTotalCategories.setText(String.valueOf(summary.totalCategories));
              binding.tvTotalSuppliers.setText(String.valueOf(summary.totalSuppliers));

              binding.tvLowStock.setTextColor(
                  MaterialColors.getColor(
                      binding.tvLowStock,
                      summary.lowStockCount > 0
                          ? androidx.appcompat.R.attr.colorError
                          : com.google.android.material.R.attr.colorOnSurface));

              binding.tvLowStockSummary.setText(
                  summary.lowStockCount == 0
                      ? getString(R.string.low_stock_summary_empty)
                      : getString(R.string.low_stock_summary, summary.lowStockCount));

              binding.swipeRefresh.setRefreshing(false);
            });

    viewModel
        .getLowStock()
        .observe(
            getViewLifecycleOwner(),
            products -> {
              lowStockAdapter.submit(products);
              boolean empty = products.isEmpty();
              binding.cardLowStock.setVisibility(empty ? View.GONE : View.VISIBLE);
              binding.lowStockEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            });

    viewModel
        .getMessage()
        .observe(
            getViewLifecycleOwner(),
            text -> {
              if (text == null) return;
              binding.swipeRefresh.setRefreshing(false);
              Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG).show();
              viewModel.clearMessage();
            });
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
