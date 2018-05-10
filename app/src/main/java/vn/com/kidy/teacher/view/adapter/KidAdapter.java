package vn.com.kidy.teacher.view.adapter;

/**
 * Created by admin on 5/2/18.
 */

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.model.login.Kid;

public class KidAdapter extends RecyclerView.Adapter<KidAdapter.ItemsViewHolder> {

    private List<Kid> items;
    private ItemClickListener itemClickListener;
    private boolean isChoose = false;

    public KidAdapter() {
        items = Collections.emptyList();
    }

    public void setItems(List<Kid> items) {
        this.items = items;
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_kid, parent, false);
        return new KidAdapter.ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Kid item = items.get(position);

        holder.txt_kid_name.setText(item.getFullName() + " (" + item.getNickName() + ")");
//        holder.txt_kid_birthday.setText(item.getBirthday());

        Uri uri = Uri.parse(item.getAvatar().replaceAll("\\\\", "/"));
        holder.kid_avatar.setImageURI(uri);
        if (item.getHasComment()) {
            holder.txt_kid_birthday.setText("Đã nhận xét tuần");
        }

        if (!isChoose) {
            holder.chk_select.setVisibility(View.GONE);
        } else {
            holder.chk_select.setChecked(item.getIsChecked());
            holder.chk_select.setVisibility(View.VISIBLE);
            holder.chk_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemCheckedClick(items, item, position, b);
                    }
                }
            });
        }

        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(items, item, position);
            }
        });
    }

    public void setIsChoose(boolean isChoose) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setIsChecked(false);
        }
        this.isChoose = isChoose;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(List<Kid> classes, Kid cls, int position);
        void onItemCheckedClick(List<Kid> classes, Kid cls, int position, boolean isChecked);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kid_avatar)
        SimpleDraweeView kid_avatar;
        @BindView(R.id.txt_kid_name)
        TextView txt_kid_name;
        @BindView(R.id.txt_kid_birthday)
        TextView txt_kid_birthday;
        @BindView(R.id.chk_select)
        CheckBox chk_select;

        View itemView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
