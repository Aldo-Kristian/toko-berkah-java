package com.example.sembakoapp.ui.categories;

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
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.databinding.FragmentCategoriesBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import java.util.Collections;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoryAdapter.Listener {

  private FragmentCategoriesBinding binding;
  private CategoriesViewModel viewModel;
  private CategoryAdapter adapter;
  private List<Category> latest = Collections.emptyList();

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentCategoriesBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

    adapter = new CategoryAdapter(this);
    binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.rvCategories.setAdapter(adapter);

    binding.fabAdd.setOnClickListener(v ->
        CategoryFormBottomSheet.newInstance(null)
            .show(getChildFragmentManager(), CategoryFormBottomSheet.TAG));

    viewModel
        .getCategories()
        .observe(
            getViewLifecycleOwner(),
            categories -> {
              if (categories == null) return;
              latest = categories;
              adapter.submit(categories, viewModel.getProductCounts().getValue());
              binding.emptyState.setVisibility(categories.isEmpty() ? View.VISIBLE : View.GONE);
            });

    viewModel
        .getProductCounts()
        .observe(getViewLifecycleOwner(), counts -> adapter.submit(latest, counts));

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
  public void onEdit(Category category) {
    CategoryFormBottomSheet.newInstance(category)
        .show(getChildFragmentManager(), CategoryFormBottomSheet.TAG);
  }

  @Override
  public void onDelete(Category category) {
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.dialog_delete_category_title)
        .setMessage(getString(R.string.dialog_delete_category_message, category.name))
        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(R.string.action_delete, (dialog, which) -> viewModel.delete(category))
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
