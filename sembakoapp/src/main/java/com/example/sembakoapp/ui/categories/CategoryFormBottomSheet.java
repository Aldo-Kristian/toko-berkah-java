package com.example.sembakoapp.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.databinding.SheetCategoryFormBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryFormBottomSheet extends BottomSheetDialogFragment {

  public static final String TAG = "CategoryFormBottomSheet";
  private static final String ARG_CATEGORY = "arg_category";

  private SheetCategoryFormBinding binding;
  private CategoriesViewModel viewModel;
  private Category editing;

  public static CategoryFormBottomSheet newInstance(@Nullable Category category) {
    CategoryFormBottomSheet sheet = new CategoryFormBottomSheet();
    if (category != null) {
      Bundle args = new Bundle();
      args.putSerializable(ARG_CATEGORY, category);
      sheet.setArguments(args);
    }
    return sheet;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = SheetCategoryFormBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(requireParentFragment()).get(CategoriesViewModel.class);

    if (getArguments() != null) {
      editing = (Category) getArguments().getSerializable(ARG_CATEGORY);
    }

    binding.tvTitle.setText(
        editing == null ? R.string.form_add_category : R.string.form_edit_category);

    if (editing != null) {
      binding.etName.setText(editing.name);
      binding.etDescription.setText(editing.description);
    }

    binding.btnSave.setOnClickListener(v -> save());
  }

  private void save() {
    binding.tilName.setError(null);

    String name =
        binding.etName.getText() == null ? "" : binding.etName.getText().toString().trim();
    String description =
        binding.etDescription.getText() == null
            ? ""
            : binding.etDescription.getText().toString().trim();

    if (name.isEmpty()) {
      binding.tilName.setError(getString(R.string.error_required));
      return;
    }

    Category category = editing != null ? editing : new Category();
    category.name = name;
    category.description = description;

    viewModel.save(category);
    dismiss();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
