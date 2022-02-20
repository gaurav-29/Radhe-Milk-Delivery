package com.example.radhegausala.Adapter;

import static com.example.radhegausala.Fragment.DashBoardFragment.SelectedDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radhegausala.Model.CustomerListModel;
import com.example.radhegausala.R;
import com.example.radhegausala.databinding.CustomerDetailsEveningListItemBinding;

import java.util.ArrayList;

public class CustomerEveningDetailsAdapter extends RecyclerView.Adapter<CustomerEveningDetailsAdapter.ViewHolder> {
    Context context;
    ArrayList<CustomerListModel.Data> mData = new ArrayList<>();
    CustomerEveningDetailsAdapter.OnClick onClick;

    public CustomerEveningDetailsAdapter(Context context, ArrayList<CustomerListModel.Data> mData, CustomerEveningDetailsAdapter.OnClick onClick) {
        this.mData = mData;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public CustomerEveningDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CustomerDetailsEveningListItemBinding customerDetailsListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.customer_details_evening_list_item, parent, false);
        CustomerEveningDetailsAdapter.ViewHolder viewHolder = new CustomerEveningDetailsAdapter.ViewHolder(customerDetailsListItemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerEveningDetailsAdapter.ViewHolder holder, int position) {
        holder.mBinding.tvCID.setText(mData.get(position).cid);
        holder.mBinding.tvName.setText(mData.get(position).name + " " + "(" + mData.get(position).mobile_no + ")");
        holder.mBinding.tvAddress.setText(mData.get(position).address);
        holder.mBinding.tvDate.setText(SelectedDate);
        if (mData.get(position).milks.size() > 0) {
            holder.mBinding.tvLiters.setText(mData.get(position).milks.get(0).evening + "");
        } else
            holder.mBinding.tvLiters.setText("0");
        if (mData.get(position).milks.size() > 0 && mData.get(position).milks.get(0).evening != 0) {
            holder.mBinding.ivUnchecked.setVisibility(View.GONE);
            holder.mBinding.ivChecked.setVisibility(View.VISIBLE);
            holder.mBinding.llAddMilk.setVisibility(View.GONE);
        }
        holder.mBinding.tv05Liters.setOnClickListener(v -> {
            holder.mBinding.edtLiters.setText("0.5");
        });
        holder.mBinding.tv1Liters.setOnClickListener(v -> {
            holder.mBinding.edtLiters.setText("1.0");
        });
        holder.mBinding.tv15Liters.setOnClickListener(v -> {
            holder.mBinding.edtLiters.setText("1.5");
        });
        holder.mBinding.tv2Liters.setOnClickListener(v -> {
            holder.mBinding.edtLiters.setText("2.0");
        });
        holder.mBinding.tv25Liters.setOnClickListener(v -> {
            holder.mBinding.edtLiters.setText("2.5");
        });
        holder.mBinding.ivUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mBinding.ivUnchecked.setVisibility(View.GONE);
                holder.mBinding.ivChecked.setVisibility(View.VISIBLE);
                holder.mBinding.llAddMilk.setVisibility(View.VISIBLE);
            }
        });
        holder.mBinding.tvCancle.setOnClickListener(v -> {
            holder.mBinding.llAddMilk.setVisibility(View.GONE);
            holder.mBinding.ivChecked.setVisibility(View.GONE);
            holder.mBinding.ivUnchecked.setVisibility(View.VISIBLE);
        });
        holder.mBinding.tvSubmit.setOnClickListener(v -> {
            if (holder.mBinding.edtLiters.getText().toString().isEmpty()) {
                Toast.makeText(context, "Plese enter liters", Toast.LENGTH_SHORT).show();
            } else if (Float.parseFloat(holder.mBinding.edtLiters.getText().toString()) <= 0) {
                Toast.makeText(context, "Please enter liters", Toast.LENGTH_SHORT).show();
            } else {
                holder.mBinding.llAddMilk.setVisibility(View.GONE);
                onClick.OnClick(holder.mBinding.edtLiters.getText().toString(), mData.get(position).id, SelectedDate);
            }
        });

        if (position % 2 == 0) {
            holder.mBinding.llBackground.setBackgroundResource(R.color.DarkBlue);
            holder.mBinding.llmilkliters.setBackgroundResource(R.color.DarkBlue);
        } else {
            holder.mBinding.llBackground.setBackgroundResource(R.color.DarkRed);
            holder.mBinding.llmilkliters.setBackgroundResource(R.color.DarkRed);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CustomerDetailsEveningListItemBinding mBinding;

        public ViewHolder(@NonNull CustomerDetailsEveningListItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public void addItems(ArrayList<CustomerListModel.Data> items) {
        int size = mData.size();
        mData.addAll(items);
        notifyItemRangeChanged(size, items.size());
    }

    public interface OnClick {
        void OnClick(String LiterCount, int id, String milk_date);
    }
}
