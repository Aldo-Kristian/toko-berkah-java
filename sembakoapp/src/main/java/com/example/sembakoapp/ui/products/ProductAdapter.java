package com.example.sembakoapp.ui.products;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.databinding.ItemProductBinding;
import com.example.sembakoapp.util.FormatUtils;
import com.google.android.material.color.MaterialColors;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

  public interface Listener {
    void onEdit(Product product);

    void onDelete(Product product);
  }

  private final List<Product> items = new ArrayList<>();
  private final Listener listener;

  public ProductAdapter(Listener listener) {
    this.listener = listener;
  }

  public void submit(List<Product> products) {
    items.clear();
    items.addAll(products);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    private final ItemProductBinding binding;

    VH(ItemProductBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(Product product) {
      Context ctx = binding.getRoot().getContext();
      binding.tvName.setText(product.name);

      String category =
          product.categoryName == null || product.categoryName.isEmpty()
              ? ctx.getString(R.string.without_category)
              : product.categoryName;
      String supplier =
          product.supplierName == null || product.supplierName.isEmpty()
              ? ctx.getString(R.string.without_supplier)
              : product.supplierName;
      binding.tvSubtitle.setText(
          ctx.getString(R.string.product_subtitle_format, category, supplier));

      String unit =
          product.unit == null || product.unit.isEmpty()
              ? ctx.getString(R.string.default_unit)
              : product.unit;
      binding.tvPrice.setText(FormatUtils.rupiah(product.price));
      binding.tvStock.setText(ctx.getString(R.string.stock_format, product.stock, unit));

      boolean lowStock = product.isLowStock();
      int backgroundAttr =
          lowStock
              ? com.google.android.material.R.attr.colorErrorContainer
              : com.google.android.material.R.attr.colorPrimaryContainer;
      int foregroundAttr =
          lowStock
              ? com.google.android.material.R.attr.colorOnErrorContainer
              : com.google.android.material.R.attr.colorOnPrimaryContainer;
      binding.tvStock.setBackgroundTintList(
          ColorStateList.valueOf(MaterialColors.getColor(binding.tvStock, backgroundAttr)));
      binding.tvStock.setTextColor(MaterialColors.getColor(binding.tvStock, foregroundAttr));

      binding.getRoot().setOnClickListener(v -> listener.onEdit(product));
      binding.btnMore.setOnClickListener(this::showMenu);
    }

    private void showMenu(View anchor) {
      int position = getBindingAdapterPosition();
      if (position == RecyclerView.NO_POSITION) return;
      Product product = items.get(position);
      PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
      popup.inflate(R.menu.item_actions);
      popup.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.action_edit) {
          listener.onEdit(product);
          return true;
        }
        if (item.getItemId() == R.id.action_delete) {
          listener.onDelete(product);
          return true;
        }
        return false;
      });
      popup.show();
    }
  }
}
