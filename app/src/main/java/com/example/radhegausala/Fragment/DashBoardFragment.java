package com.example.radhegausala.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.radhegausala.Activity.MainActivity.visiblity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radhegausala.Adapter.CustomerEveningDetailsAdapter;
import com.example.radhegausala.Adapter.CustomerMorningDetailsAdapter;
import com.example.radhegausala.Model.AddMilkModel;
import com.example.radhegausala.Model.CustomerListModel;
import com.example.radhegausala.R;
import com.example.radhegausala.Utils.ProgressHubKt;
import com.example.radhegausala.Utils.RetrofitClient;
import com.example.radhegausala.Utils.Utils;
import com.example.radhegausala.databinding.FragmentDashBoardBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardFragment extends Fragment {
    FragmentDashBoardBinding mBinding;
    DatePickerDialog picker;
    private String[] DayNight = {"Evening", "Morning"};
    String url = "https://ambtechapplication.com/radhe/api/v1/ship/users/get?page=1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SelectedDate = df.format(c);
        finalSelectedDate = SelectedDate;
        mBinding.tvDateFormat.setText(SelectedDate);
        getSpinnerData();
        dateSelection();
        mBinding.llMorningList.setVisibility(View.GONE);
        mBinding.llEvenigList.setVisibility(View.VISIBLE);
        GetEveningData();
        onClick();
        mBinding.tvRearrange.setOnClickListener(v -> {
            visiblity(View.VISIBLE);
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new RearrangeFragment();
            fragmentManager.beginTransaction().replace(R.id.llContainLayout, fragment).commit();
        });
        return mBinding.getRoot();
    }

    String finalSelectedDate = "";

    private void onClick() {
        mBinding.tvSearch.setOnClickListener(v -> {
            finalSelectedDate = SelectedDate;
            if (mBinding.SpinnerDayTime.getSelectedItemPosition() == 1) {
                mBinding.llMorningList.setVisibility(View.VISIBLE);
                mBinding.llEvenigList.setVisibility(View.GONE);
                GetMorningData();
            } else {
                mBinding.llMorningList.setVisibility(View.GONE);
                mBinding.llEvenigList.setVisibility(View.VISIBLE);
                GetEveningData();
            }
        });
        mBinding.tvClear.setOnClickListener(v -> {
            mBinding.edtSearch.setText("");
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SelectedDate = df.format(c);
            finalSelectedDate = SelectedDate;
            mBinding.tvDateFormat.setText(SelectedDate);
            getSpinnerData();
            dateSelection();
            mBinding.llMorningList.setVisibility(View.GONE);
            mBinding.llEvenigList.setVisibility(View.VISIBLE);
            GetEveningData();
        });
    }

    private void AddMilk(String literCount, int id, String morning, String milk_date) {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");

            Call<AddMilkModel> call = RetrofitClient.getInstance().getApi().AddMilk(token, passParameter(literCount, id, morning, milk_date));

            call.enqueue(new Callback<AddMilkModel>() {
                @Override
                public void onResponse(Call<AddMilkModel> call, Response<AddMilkModel> response) {
                    ProgressHubKt.dismissLoader();
                    if (response.code() == 200) {
                        if (response.body().status == 200) {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            if (mBinding.llMorningList.getVisibility() == View.VISIBLE) {
                                GetMorningData();
                            } else {
                                GetEveningData();
                            }
                            Log.d("ADDMILK", response.body().message);
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AddMilkModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    ProgressHubKt.dismissLoader();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, String> passParameter(String literCount, int id, String morning, String milk_date) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(id));
        map.put("morning_evening", morning);
        map.put("ltr", literCount);
        map.put("delivery_date", milk_date);
        Log.d("param", map.toString());
        return map;
    }

    public static String SelectedDate = "";
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    private void dateSelection() {
        datePickerDialog();

        mBinding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });
    }

    private void datePickerDialog() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabel();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void updateLabel() throws ParseException {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        mBinding.tvDateFormat.setText(sdf.format(myCalendar.getTime()));
        SelectedDate = sdf.format(myCalendar.getTime());
    }

    private void getSpinnerData() {
        ArrayAdapter aa = new ArrayAdapter(getContext(), R.layout.simple_spinner_item, DayNight);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mBinding.SpinnerDayTime.setAdapter(aa);
        mBinding.SpinnerDayTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 1) {
//                    mBinding.tvSave.setOnClickListener(v -> {
//                        mBinding.llEvenigList.setVisibility(View.GONE);
//                        mBinding.llMorningList.setVisibility(View.VISIBLE);
//                        GetMorningData();
//                    });
//
//                } else {
//                    mBinding.tvSave.setOnClickListener(v -> {
//                        mBinding.llMorningList.setVisibility(View.GONE);
//                        mBinding.llEvenigList.setVisibility(View.VISIBLE);
//                        GetEveningData();
//                    });
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    CustomerMorningDetailsAdapter customerMorningDetailsAdapter;
    CustomerEveningDetailsAdapter customerEveningDetailsAdapter;
    int page = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    ArrayList<CustomerListModel.Data> mData = new ArrayList<>();

    private void resultActionMorning(CustomerListModel customerListModel) {
        isLoading = false;
        if (customerListModel != null) {
            customerMorningDetailsAdapter.addItems(customerListModel.data.data);
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
        mData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mBinding.rvlistCustomerMorning.setLayoutManager(layoutManager);
        customerMorningDetailsAdapter = new CustomerMorningDetailsAdapter(getActivity(), mData, (String LiterCount, int id, String milk_date) -> {
            AddMilk(LiterCount, id, "morning", milk_date);
        });
        mBinding.rvlistCustomerMorning.setAdapter(customerMorningDetailsAdapter);
        mBinding.nsvMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) mBinding.nsvMain.getChildAt(mBinding.nsvMain.getChildCount() - 1);

                int diff = (view.getBottom() - (mBinding.nsvMain.getHeight() + mBinding.nsvMain
                        .getScrollY()));

                if (diff == 0) {
                    Log.d("istrue", "istreu");
                    isLoading = true;
                    if (!isLastPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData("morning");
                            }
                        }, 200);
                    }
                }
            }
        });
        loadData("morning");
    }

    private void resultActionEvening(CustomerListModel customerListModel) {
        isLoading = false;
        if (customerListModel != null) {
            customerEveningDetailsAdapter.addItems(customerListModel.data.data);
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
        mData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mBinding.rvlistCustomerEvening.setLayoutManager(layoutManager);
        customerEveningDetailsAdapter = new CustomerEveningDetailsAdapter(getActivity(), mData, (String LiterCount, int id, String milk_date) -> {
            AddMilk(LiterCount, id, "evening", milk_date);
        });
        mBinding.rvlistCustomerEvening.setAdapter(customerEveningDetailsAdapter);
        mBinding.nsvMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) mBinding.nsvMain.getChildAt(mBinding.nsvMain.getChildCount() - 1);

                int diff = (view.getBottom() - (mBinding.nsvMain.getHeight() + mBinding.nsvMain
                        .getScrollY()));

                if (diff == 0) {
                    Log.d("istrue", "istreu");
                    isLoading = true;
                    if (!isLastPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData("evening");
                            }
                        }, 200);
                    }
                }
            }
        });

        loadData("evening");
    }

    CustomerListModel mModel = new CustomerListModel();

    private void loadData(String type) {
        if (Utils.isInternetAvailable(getActivity())) {
            ProgressHubKt.showLoader(getActivity());
            SharedPreferences preferences = this.getActivity().getSharedPreferences("Login", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("token", "");
            Log.d("token", preferences.getString("token", ""));
            String Search = mBinding.edtSearch.getText().toString();
            Call<CustomerListModel> call;
//            if (mBinding.llMorningList.getVisibility() == View.VISIBLE)
//                call = RetrofitClient.getInstance().getApi().getCustomerList(token, Search, finalSelectedDate, page, "morning");
//            else
                call = RetrofitClient.getInstance().getApi().getCustomerList(token, Search, finalSelectedDate, page, type);
            call.enqueue(new Callback<CustomerListModel>() {
                @Override
                public void onResponse(Call<CustomerListModel> call, Response<CustomerListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().status == 200) {
                            mModel = response.body();
                            if (response.body() != null && response.body().data.data.size() > 0) {
                                if (response.body().data.last_page == page) {
                                    isLastPage = true;
                                }
                                if (mBinding.llMorningList.getVisibility() == View.VISIBLE) {
                                    Log.d("testEvening", "gone");

                                    resultActionMorning(response.body());
                                } else {
                                    Log.d("testMorning", "gone");
                                    resultActionEvening(response.body());
                                }
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
                public void onFailure(Call<CustomerListModel> call, Throwable t) {
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