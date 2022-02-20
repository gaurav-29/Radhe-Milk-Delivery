package com.example.radhegausala.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radhegausala.Adapter.RearrangeEveningListAdapter;
import com.example.radhegausala.Adapter.RearrangeMorningListAdapter;
import com.example.radhegausala.Model.EveningReformatModel;
import com.example.radhegausala.Model.MorningReformatModel;
import com.example.radhegausala.Model.RearrangeEveningListModel;
import com.example.radhegausala.Model.RearrangeMorningListModel;
import com.example.radhegausala.R;
import com.example.radhegausala.Utils.PaginationScrollListener;
import com.example.radhegausala.Utils.ProgressHubKt;
import com.example.radhegausala.Utils.RetrofitClient;
import com.example.radhegausala.Utils.Utils;
import com.example.radhegausala.databinding.FragmentRearrangeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RearrangeFragment extends Fragment {
    FragmentRearrangeBinding mBinding;
    private String[] DayNight = {"Evening", "Morning"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rearrange, container, false);
        onClick();
        getSpinnerData();
        return mBinding.getRoot();
    }

    private void getSpinnerData() {
        ArrayAdapter aa = new ArrayAdapter(getContext(), R.layout.simple_spinner_item, DayNight);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mBinding.SpinnerDayTime.setAdapter(aa);
        mBinding.SpinnerDayTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 1) {
                    mBinding.llEveningList.setVisibility(View.GONE);
                    mBinding.llMorningList.setVisibility(View.VISIBLE);
                    GetMorningData();

                } else {
                    mBinding.llMorningList.setVisibility(View.GONE);
                    mBinding.llEveningList.setVisibility(View.VISIBLE);
                    GetEveningData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void MorningResultAction(RearrangeMorningListModel rearrangeMorningListModel) {
        isLoading = false;
        if (rearrangeMorningListModel != null) {
            rearrangeMorningListAdapter.addItems(rearrangeMorningListModel.data.data);
            if (isLastPage) {
            } else {
                page = page + 1;
            }
        }
    }

    private void GetMorningData() {
        page = 1;
        isLoading = false;
        isLastPage = false;
        MorningData = new ArrayList<>();
        Log.d("MORNING DATA", MorningData.toString());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mBinding.rvlistCustomerMorning.setLayoutManager(layoutManager);
        rearrangeMorningListAdapter = new RearrangeMorningListAdapter(getActivity(), MorningData);
        mBinding.rvlistCustomerMorning.setAdapter(rearrangeMorningListAdapter);

        mBinding.rvlistCustomerMorning.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MorningloadData();
                        }
                    }, 200);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        MorningloadData();
    }

    private void EveningResultAction(RearrangeEveningListModel rearrangeEveningListModel) {
        isLoading = false;
        if (rearrangeEveningListModel != null) {
            rearrangeEveningListAdapter.addItems(rearrangeEveningListModel.data.data);
//            mDataMain.addAll(rearrangeListModel.data.data);
            if (isLastPage) {
            } else {
                page = page + 1;
            }
        }
    }

    private void GetEveningData() {
        page = 1;
        isLoading = false;
        isLastPage = false;
        EveningData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mBinding.rvlistCustomerEvening.setLayoutManager(layoutManager);
        rearrangeEveningListAdapter = new RearrangeEveningListAdapter(getActivity(), EveningData);
        mBinding.rvlistCustomerEvening.setAdapter(rearrangeEveningListAdapter);

        mBinding.rvlistCustomerEvening.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EveningloadData();
                        }
                    }, 200);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        EveningloadData();
    }

    private void EveningloadData() {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.getActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");
            Log.d("Token11", token);
            String Search = mBinding.edtSearch.getText().toString();
            Call<RearrangeEveningListModel> call = RetrofitClient.getInstance().getApi().getEveningRearrangeList(token, Search, page, "evening");
            call.enqueue(new Callback<RearrangeEveningListModel>() {
                @Override
                public void onResponse(Call<RearrangeEveningListModel> call, Response<RearrangeEveningListModel> response) {
//                  Log.d("responce", response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body().status == 200) {
                            Log.d("ParthGodhani", response.body().data.toString());
                            Log.e("response", response.body() + "");
                            eveningListModel = response.body();
                            if (response.body() != null && response.body().data.data.size() > 0) {
                                if (response.body().data.last_page == page) {
                                    isLastPage = true;
                                }
                                EveningResultAction(response.body());
                                ProgressHubKt.dismissLoader();
                            } else {
                                isLastPage = true;
                                ProgressHubKt.dismissLoader();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            ProgressHubKt.dismissLoader();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        ProgressHubKt.dismissLoader();
                    }
                }

                @Override
                public void onFailure(Call<RearrangeEveningListModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    Log.d("msg", t.getMessage().toString());
                    ProgressHubKt.dismissLoader();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMorningData() {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");

            Call<MorningReformatModel> call = RetrofitClient.getInstance().getApi().updateRearrange(token, passParameter());
            call.enqueue(new Callback<MorningReformatModel>() {
                @Override
                public void onResponse(Call<MorningReformatModel> call, Response<MorningReformatModel> response) {
                    ProgressHubKt.dismissLoader();
                    if (response.code() == 200) {
                        if (response.body().status == 200) {
                            GetMorningData();
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE", response.body().message);
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MorningReformatModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    ProgressHubKt.dismissLoader();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, String> passParameter() {
        Map<String, String> map = new HashMap<>();
        //     map.put("profile_image", "");
        map.put("morning_evening", "morning");
        map.put("data_json", rearrangeMorningListAdapter.reformatList.toString());
        Log.d("param", map.toString());
        return map;
    }

    private void updateEveningData() {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");

            Call<EveningReformatModel> call = RetrofitClient.getInstance().getApi().updateRearrange2(token, passParameter2());
            call.enqueue(new Callback<EveningReformatModel>() {
                @Override
                public void onResponse(Call<EveningReformatModel> call, Response<EveningReformatModel> response) {
                    ProgressHubKt.dismissLoader();
                    if (response.code() == 200) {
                        if (response.body().status == 200) {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            GetEveningData();
                            Log.d("RESPONSE", response.body().message);
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<EveningReformatModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    ProgressHubKt.dismissLoader();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, String> passParameter2() {
        Map<String, String> map = new HashMap<>();
        //     map.put("profile_image", "");
        map.put("morning_evening", "evening");
        map.put("data_json", rearrangeEveningListAdapter.reformatEveningList.toString());
        Log.d("param", map.toString());
        return map;
    }

    private void onClick() {
        mBinding.tvSearch.setOnClickListener(v -> {
            mBinding.tvClear.setVisibility(View.VISIBLE);
            if (mBinding.llMorningList.getVisibility() == View.VISIBLE) {
                mBinding.edtSearch.getText().toString();
                GetMorningData();
            } else {
                mBinding.edtSearch.getText();
                GetEveningData();
            }
        });
        mBinding.tvClear.setOnClickListener(v -> {
            mBinding.tvClear.setVisibility(View.GONE);
            mBinding.edtSearch.setText("");
            if (mBinding.llMorningList.getVisibility() == View.VISIBLE) {
                mBinding.edtSearch.getText().toString();
                GetMorningData();
            } else {
                mBinding.edtSearch.getText();
                GetEveningData();
            }
        });
        mBinding.tvSubmit.setOnClickListener(v -> {
//            Log.d("REARRANGE_DATA", rearrangeMorningListAdapter.reformatList.toString());
            boolean isUpdate = false;
            if (mBinding.llEveningList.getVisibility() == View.VISIBLE)
                for (int i = 0; i < rearrangeEveningListAdapter.reformatEveningList.size(); i++) {
                    if (rearrangeEveningListAdapter.reformatEveningList.get(i).ShortNum.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please enter valid input at " + rearrangeEveningListAdapter.reformatEveningList.get(i).UserId, Toast.LENGTH_LONG).show();
                        isUpdate = true;
                        break;
                    } else if (Integer.parseInt(rearrangeEveningListAdapter.reformatEveningList.get(i).ShortNum) <= 0) {
                        Toast.makeText(getActivity(), "Please enter valid input at " + rearrangeEveningListAdapter.reformatEveningList.get(i).UserId, Toast.LENGTH_LONG).show();
                        isUpdate = true;
                        break;
                    }
                }
            else {
                for (int i = 0; i < rearrangeMorningListAdapter.reformatList.size(); i++) {
                    if (rearrangeMorningListAdapter.reformatList.get(i).ShortNum.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please enter valid input at " + rearrangeMorningListAdapter.reformatList.get(i).UserId, Toast.LENGTH_LONG).show();
                        isUpdate = true;
                        break;
                    } else if (Integer.parseInt(rearrangeMorningListAdapter.reformatList.get(i).ShortNum) <= 0) {
                        Toast.makeText(getActivity(), "Please enter valid input at " + rearrangeMorningListAdapter.reformatList.get(i).UserId, Toast.LENGTH_LONG).show();
                        isUpdate = true;
                        break;
                    }
                }
            }
            if (mBinding.llEveningList.getVisibility() == View.VISIBLE) {
                Log.d("rearrangeEvening", rearrangeEveningListAdapter.reformatEveningList.toString());
            } else
                Log.d("rearrangeMorning", rearrangeMorningListAdapter.reformatList.toString());

            if (!isUpdate)
                if (mBinding.llMorningList.getVisibility() == View.VISIBLE) {
                    updateMorningData();

                } else if (mBinding.llEveningList.getVisibility() == View.VISIBLE) {
                    updateEveningData();
                }
        });
    }

    RearrangeMorningListAdapter rearrangeMorningListAdapter;
    RearrangeEveningListAdapter rearrangeEveningListAdapter;
    int page = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    ArrayList<RearrangeMorningListModel.Data> MorningData = new ArrayList<>();
    ArrayList<RearrangeEveningListModel.Data> EveningData = new ArrayList<>();
    ArrayList<MorningReformatModel> mDataMain = new ArrayList<>();

    RearrangeMorningListModel morningListModel = new RearrangeMorningListModel();
    RearrangeEveningListModel eveningListModel = new RearrangeEveningListModel();
    ArrayList<EveningReformatModel.Data> eveningDataNew = new ArrayList<>();

    private void MorningloadData() {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.getActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");
            Log.d("Token11", token);
            String Search = mBinding.edtSearch.getText().toString();
            Call<RearrangeMorningListModel> call = RetrofitClient.getInstance().getApi().getMorningRearrangeList(token, Search, page, "morning");
            call.enqueue(new Callback<RearrangeMorningListModel>() {
                @Override
                public void onResponse(Call<RearrangeMorningListModel> call, Response<RearrangeMorningListModel> response) {
//                  Log.d("responce", response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body().status == 200) {
                            Log.d("ParthGodhani", response.body().data.toString());
                            Log.e("response", response.body() + "");
                            morningListModel = response.body();
                            Log.d("MODEL", morningListModel.toString());
                            if (response.body() != null && response.body().data.data.size() > 0) {
                                if (response.body().data.last_page == page) {
                                    isLastPage = true;
                                }
                                Log.d("testEvening", "gone");
                                MorningResultAction(response.body());
                                ProgressHubKt.dismissLoader();
                            } else {
                                isLastPage = true;
                                ProgressHubKt.dismissLoader();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            ProgressHubKt.dismissLoader();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        ProgressHubKt.dismissLoader();
                    }
                }

                @Override
                public void onFailure(Call<RearrangeMorningListModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    Log.d("msg", t.getMessage().toString());
                    ProgressHubKt.dismissLoader();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
        }
    }
}