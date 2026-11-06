package com.example.sembakoapp.ui.categories;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Category;
import com.example.sembakoapp.databinding.ItemCategoryBinding;
import com.google.android.material.color.MaterialColors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

  public interface Listener {
    void onEdit(Category category);

    void onDelete(Category category);
  }

  private final List<Category> items = new ArrayList<>();
  private final Map<Long, Integer> counts = new HashMap<>();
  private final Listener listener;

  public CategoryAdapter(Listener listener) {
    this.listener = listener;
  }

  public void submit(List<Category> categories, Map<Long, Integer> productCounts) {
    items.clear();
    items.addAll(categories);
    counts.clear();
    if (productCounts != null) counts.putAll(productCounts);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    holder.bind(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  class VH extends RecyclerView.ViewHolder {
    private final ItemCategoryBinding binding;

    VH(ItemCategoryBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(Category category) {
      Context ctx = binding.getRoot().getContext();
      binding.tvName.setText(category.name);

      if (category.description == null || category.description.trim().isEmpty()) {
        binding.tvDescription.setVisibility(View.GONE);
      } else {
        binding.tvDescription.setVisibility(View.VISIBLE);
        binding.tvDescription.setText(category.description);
      }

      Integer count = counts.get(category.id);
      binding.tvCount.setText(ctx.getString(R.string.product_count, count == null ? 0 : count));

      int background =
          MaterialColors.getColor(
              binding.tvCount, com.google.android.material.R.attr.colorPrimaryContainer);
      int foreground =
          MaterialColors.getColor(
              binding.tvCount, com.google.android.material.R.attr.colorOnPrimaryContainer);
      binding.tvCount.setBackgroundTintList(ColorStateList.valueOf(background));
      binding.tvCount.setTextColor(foreground);

      binding.getRoot().setOnClickListener(v -> listener.onEdit(category));
      binding.btnMore.setOnClickListener(this::showMenu);
    }

    private void showMenu(View anchor) {
      Category category = items.get(getBindingAdapterPosition());
      PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
      popup.inflate(R.menu.item_actions);
      popup.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.action_edit) {
          listener.onEdit(category);
          return true;
        }
        if (item.getItemId() == R.id.action_delete) {
          listener.onDelete(category);
          return true;
        }
        return false;
      });
      popup.show();
    }
  }
}
