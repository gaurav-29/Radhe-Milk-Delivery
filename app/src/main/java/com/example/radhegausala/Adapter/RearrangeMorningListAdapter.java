package com.example.radhegausala.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radhegausala.Model.MorningReformatModel;
import com.example.radhegausala.Model.RearrangeMorningListModel;
import com.example.radhegausala.R;
import com.example.radhegausala.databinding.RearrangeListItemBinding;

import java.util.ArrayList;

public class RearrangeMorningListAdapter extends RecyclerView.Adapter<RearrangeMorningListAdapter.ViewHolder> {

    Context context;
    public ArrayList<RearrangeMorningListModel.Data> MorningData = new ArrayList<>();
    public ArrayList<MorningReformatModel.Data> reformatList = new ArrayList<>();

    public RearrangeMorningListAdapter(Context context, ArrayList<RearrangeMorningListModel.Data> MorningData) {
        this.MorningData = MorningData;
        this.context = context;
    }

    @Override
    public RearrangeMorningListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RearrangeListItemBinding rearrangeListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.rearrange_list_item, parent, false);
        RearrangeMorningListAdapter.ViewHolder viewHolder = new RearrangeMorningListAdapter.ViewHolder(rearrangeListItemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RearrangeMorningListAdapter.ViewHolder holder, int position) {

        RearrangeMorningListModel.Data selectedData = MorningData.get(position);
        holder.mBinding.tvCID.setText(selectedData.cid);
        holder.mBinding.tvAddress.setText(selectedData.address);
        holder.mBinding.tvName.setText(selectedData.name + " " + "(" + selectedData.mobile_no + ")");
        if (selectedData.sort_morning_no > 0) {
            holder.mBinding.tvShort.setText(selectedData.sort_morning_no + "");
        } else {
            holder.mBinding.tvShort.setText("0");
        }

        holder.mBinding.tvShort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int selectedPosition = -1;
                if (reformatList.size() > 0) {
                    for (int i = 0; i < reformatList.size(); i++) {
                        if (String.valueOf(MorningData.get(position).id).equalsIgnoreCase(reformatList.get(i).UserId)) {
                            selectedPosition = i;
                            break;
                        }
                    }
                }
                if (selectedPosition >= 0) {
                    reformatList.get(selectedPosition).ShortNum = s.toString();
                } else {
                    MorningReformatModel.Data model = new MorningReformatModel.Data();
                    model.UserId = String.valueOf(MorningData.get(position).id);
                    model.ShortNum = s.toString();
                    reformatList.add(model);
                }
            }
        });

        if (position % 2 == 0) {
            holder.mBinding.llBackground.setBackgroundResource(R.color.DarkBlue);
        } else {
            holder.mBinding.llBackground.setBackgroundResource(R.color.DarkRed);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return MorningData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RearrangeListItemBinding mBinding;

        public ViewHolder(@NonNull RearrangeListItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public void addItems(ArrayList<RearrangeMorningListModel.Data> items) {
        int size = items.size();
        MorningData.addAll(items);
        notifyItemRangeChanged(size, items.size());
    }
}
