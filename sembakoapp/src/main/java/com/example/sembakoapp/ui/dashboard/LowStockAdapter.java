package com.example.sembakoapp.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Product;
import com.example.sembakoapp.databinding.ItemLowStockBinding;
import java.util.ArrayList;
import java.util.List;

public class LowStockAdapter extends RecyclerView.Adapter<LowStockAdapter.VH> {

  private final List<Product> items = new ArrayList<>();

  public void submit(List<Product> products) {
    items.clear();
    items.addAll(products);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        ItemLowStockBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    holder.bind(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class VH extends RecyclerView.ViewHolder {
    private final ItemLowStockBinding binding;

    VH(ItemLowStockBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(Product product) {
      Context ctx = binding.getRoot().getContext();
      String unit =
          (product.unit == null || product.unit.isEmpty())
              ? ctx.getString(R.string.default_unit)
              : product.unit;
      binding.tvName.setText(product.name);
      binding.tvDetail.setText(
          ctx.getString(R.string.low_stock_row_format, product.stock, unit, product.minStock));
      binding.tvStock.setText(String.valueOf(product.stock));
    }
  }
}
