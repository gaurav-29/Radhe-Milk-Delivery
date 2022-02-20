package com.example.radhegausala.Activity;

import static com.example.radhegausala.Utils.Utils.isInternetAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.radhegausala.Model.LoginModel;
import com.example.radhegausala.R;
import com.example.radhegausala.Utils.ProgressHubKt;
import com.example.radhegausala.Utils.RetrofitClient;
import com.example.radhegausala.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding mBinding;
    LoginActivity mcontext = LoginActivity.this;
    SharedPreferences spf;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mcontext, R.layout.activity_login);
        spf = getSharedPreferences("Login", MODE_PRIVATE);
        editor = spf.edit();
        if (spf.getString("isLogin", "").equalsIgnoreCase("true")) {
            Intent intent = new Intent(mcontext, MainActivity.class);
            startActivity(intent);
            finish();
        }
        clickListner();
    }

    private void clickListner() {
        mBinding.tvLogin.setOnClickListener(v -> {
            if (mBinding.edtMobileNumber.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (mBinding.edtPassword.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();
            } else {
                if (isInternetAvailable(mcontext)) {

                    ProgressHubKt.showLoader(mcontext);
                    Call<LoginModel> loginCall = RetrofitClient.getInstance().getApi().Login(paramLogin());

                    loginCall.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.code()==200) {
                                if (response.body().status == 200) {
                                    editor.putString("isLogin", "true");
                                    editor.putString("cid", response.body().data.cid);
                                    editor.putString("name", response.body().data.name);
                                    editor.putString("email", response.body().data.email);
                                    editor.putString("mobile_no", response.body().data.mobile_no);
                                    editor.putString("dob", response.body().data.dob);
                                    editor.putString("address", response.body().data.address);
                                    editor.putString("city", response.body().data.city);
                                    editor.putString("profile_image", response.body().data.profile_image);
                                    editor.putString("status", response.body().data.status);
                                    editor.putString("token", response.body().data.api_token);
                                    editor.apply();
                                    editor.commit();
                                    Log.d("TOKEN", response.body().data.api_token);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    ProgressHubKt.dismissLoader();
                                } else {
                                    Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    ProgressHubKt.dismissLoader();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                ProgressHubKt.dismissLoader();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            ProgressHubKt.dismissLoader();
                            Log.d("msg", throwable.getMessage().toString());
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Map<String, String> paramLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_no", mBinding.edtMobileNumber.getText().toString());
        params.put("password", mBinding.edtPassword.getText().toString());
        params.put("device_id", "dGs3DdL5saA%3AAPA91bGwteSY3Qk16-BVxlePfkvjXer9J53zASAMgl8Ywy1Qzcd0txcVH4BnoHJZ19QVT3BMQRtYKutCeEqd9amOsK7fsRWjQS7XopQY3vovvlR1CMioW7npx1AnkgBvO1Ox8Z0HI7ob\n");
        Log.d("param", params.toString());
        return params;
    }

}