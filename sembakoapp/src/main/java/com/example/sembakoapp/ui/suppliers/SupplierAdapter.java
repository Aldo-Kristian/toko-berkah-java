package com.example.sembakoapp.ui.suppliers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sembakoapp.R;
import com.example.sembakoapp.data.model.Supplier;
import com.example.sembakoapp.databinding.ItemSupplierBinding;
import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.VH> {

  public interface Listener {
    void onEdit(Supplier supplier);

    void onDelete(Supplier supplier);
  }

  private final List<Supplier> items = new ArrayList<>();
  private final Listener listener;

  public SupplierAdapter(Listener listener) {
    this.listener = listener;
  }

  public void submit(List<Supplier> suppliers) {
    items.clear();
    items.addAll(suppliers);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        ItemSupplierBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    private final ItemSupplierBinding binding;

    VH(ItemSupplierBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(Supplier supplier) {
      binding.tvName.setText(supplier.name);
      binding.tvPhone.setText(supplier.phone == null ? "" : supplier.phone);
      binding.tvAddress.setText(supplier.address == null ? "" : supplier.address);

      binding.getRoot().setOnClickListener(v -> listener.onEdit(supplier));
      binding.btnMore.setOnClickListener(this::showMenu);
    }

    private void showMenu(View anchor) {
      Supplier supplier = items.get(getBindingAdapterPosition());
      PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
      popup.inflate(R.menu.item_actions);
      popup.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.action_edit) {
          listener.onEdit(supplier);
          return true;
        }
        if (item.getItemId() == R.id.action_delete) {
          listener.onDelete(supplier);
          return true;
        }
        return false;
      });
      popup.show();
    }
  }
}
