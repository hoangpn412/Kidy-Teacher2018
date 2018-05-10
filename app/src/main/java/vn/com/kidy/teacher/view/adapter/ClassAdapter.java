package vn.com.kidy.teacher.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.model.login.ClassInfo;

/**
 * Created by Family on 5/19/2017.
 */

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ItemsViewHolder> {

    private List<ClassInfo> items;
    private ItemClickListener itemClickListener;

    public ClassAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<ClassInfo> items) {
        this.items = items;
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kid, parent, false);
        return new ClassAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        ClassInfo item = items.get(position);

        holder.kid_name.setText(item.getClassName());

//        Uri uri = Uri.parse(item.getAvatar().replaceAll("\\\\", "/"));
//        holder.kid_avatar.setImageURI(uri);

        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(items, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(List<ClassInfo> classes, ClassInfo cls, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kid_avatar)
        SimpleDraweeView kid_avatar;
        @BindView(R.id.kid_name)
        TextView kid_name;

        View itemView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
