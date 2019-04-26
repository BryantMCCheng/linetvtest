package com.linetv.linetvtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linetv.linetvtest.Activity.DramaDetailActivity;
import com.linetv.linetvtest.Model.DramaModel;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private List<DramaModel.DataBean> dataList;
    private List<DramaModel.DataBean> dataListFiltered;

    public CustomAdapter(Context context, List<DramaModel.DataBean> list) {
        this.mContext = context;
        this.dataList = list;
        this.dataListFiltered = list;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder holder, int position) {
        final DramaModel.DataBean member = dataListFiltered.get(position);
        Glide.with(mContext).load(member.getThumb()).into(holder.iv_thumb);
        holder.tv_name.setText(member.getName());
        holder.tv_rating.setText(String.valueOf(member.getRating()));
        holder.tv_created_at.setText(member.getCreated_at());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DramaDetailActivity.class);
                Uri data = Uri.parse(Constant.DETAIL_URL + member.getDrama_id());
                intent.setData(data);
                intent.putExtra("type", 1);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    List<DramaModel.DataBean> filteredList = new ArrayList<>();
                    for (DramaModel.DataBean row : dataList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<DramaModel.DataBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumb;
        TextView tv_name, tv_rating, tv_created_at;

        ViewHolder(View itemView) {
            super(itemView);
            iv_thumb = itemView.findViewById(R.id.iv_thumb);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_created_at = itemView.findViewById(R.id.tv_created_at);
        }
    }
}
