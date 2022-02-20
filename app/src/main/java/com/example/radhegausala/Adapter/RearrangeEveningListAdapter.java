package com.example.radhegausala.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radhegausala.Model.EveningReformatModel;
import com.example.radhegausala.Model.MorningReformatModel;
import com.example.radhegausala.Model.RearrangeEveningListModel;
import com.example.radhegausala.Model.RearrangeMorningListModel;
import com.example.radhegausala.R;
import com.example.radhegausala.databinding.RearrangeListItemBinding;

import java.util.ArrayList;

public class RearrangeEveningListAdapter extends RecyclerView.Adapter<RearrangeEveningListAdapter.ViewHolder> {

    Context context;
    public ArrayList<RearrangeEveningListModel.Data> EveningData = new ArrayList<>();
    public ArrayList<EveningReformatModel.Data> reformatEveningList = new ArrayList<>();

    public RearrangeEveningListAdapter(Context context, ArrayList<RearrangeEveningListModel.Data> EveningData) {
        this.EveningData = EveningData;
        this.context = context;
    }

    @Override
    public RearrangeEveningListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RearrangeListItemBinding rearrangeListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.rearrange_list_item, parent, false);
        RearrangeEveningListAdapter.ViewHolder viewHolder = new RearrangeEveningListAdapter.ViewHolder(rearrangeListItemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RearrangeEveningListAdapter.ViewHolder holder, int position) {

        RearrangeEveningListModel.Data selectedData = EveningData.get(position);
        holder.mBinding.tvCID.setText(selectedData.cid);
        holder.mBinding.tvAddress.setText(selectedData.address);
        holder.mBinding.tvName.setText(selectedData.name + " " + "(" + selectedData.mobile_no + ")");
        if (selectedData.sort_morning_no > 0) {
            holder.mBinding.tvShort.setText(selectedData.sort_evening_no + "");
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
                if (reformatEveningList.size() > 0) {
                    for (int i = 0; i < reformatEveningList.size(); i++) {
                        if (String.valueOf(EveningData.get(position).id).equalsIgnoreCase(reformatEveningList.get(i).UserId)) {
                            selectedPosition = i;
                            break;
                        }
                    }
                }
                if (selectedPosition >= 0) {
                    reformatEveningList.get(selectedPosition).ShortNum = s.toString();
                } else {
                    EveningReformatModel.Data model = new EveningReformatModel.Data();
                    model.UserId = String.valueOf(EveningData.get(position).id);
                    model.ShortNum = s.toString();
                    reformatEveningList.add(model);
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
        return EveningData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RearrangeListItemBinding mBinding;

        public ViewHolder(@NonNull RearrangeListItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public void addItems(ArrayList<RearrangeEveningListModel.Data> items) {
        int size = EveningData.size();
        EveningData.addAll(items);
        notifyItemRangeChanged(size, items.size());
    }
}
