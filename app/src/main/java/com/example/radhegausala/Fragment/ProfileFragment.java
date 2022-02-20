package com.example.radhegausala.Fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.radhegausala.Model.ProfileModel;
import com.example.radhegausala.Model.UpdateProfileModel;
import com.example.radhegausala.R;
import com.example.radhegausala.Utils.ProgressHubKt;
import com.example.radhegausala.Utils.RetrofitClient;
import com.example.radhegausala.Utils.Utils;
import com.example.radhegausala.databinding.FragmentProfileBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding mBinding;
    Context mContext;
    ArrayList<Uri> photoPaths = new ArrayList<>();
    File file;
    String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        mContext = getActivity();
        getData();
        onClick();
        return mBinding.getRoot();
    }

    private void onClick() {
        mBinding.ivEditImage.setOnClickListener(v -> {
            uploadImage();
        });
        mBinding.fbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ivEditImage.setVisibility(View.VISIBLE);
                mBinding.rlSubmit.setVisibility(View.VISIBLE);
                mBinding.edtName.setEnabled(true);
                mBinding.edtMobileNo.setEnabled(true);
                mBinding.edtAddress.setEnabled(true);
                mBinding.edtCity.setEnabled(true);
                mBinding.edtDateOfBirth.setEnabled(true);
                mBinding.edtEmail.setEnabled(true);
                Toast.makeText(getActivity(), "Edit your details now...", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtName.getText().toString().trim().equalsIgnoreCase(""))
                    Toast.makeText(mContext, "Please enter name", Toast.LENGTH_SHORT).show();
                else if (mBinding.edtMobileNo.getText().toString().trim().equalsIgnoreCase("") ||
                        mBinding.edtMobileNo.getText().toString().trim().length() < 10)
                    Toast.makeText(mContext, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                else if (mBinding.edtAddress.getText().toString().trim().equalsIgnoreCase(""))
                    Toast.makeText(mContext, "Please enter address", Toast.LENGTH_SHORT).show();
                else
                    updateProfile();
            }
        });
    }

    private void uploadImage() {
        Dexter.withContext(mContext)
                .withPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (SDK_INT >= Build.VERSION_CODES.R)
                                if (Environment.isExternalStorageManager()) {
                                    imagePicker();
                                } else {
                                    try {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.setData(Uri.parse(String.format("package:%s", new Object[]{mContext.getPackageName()})));
                                        startActivityForResult(intent, 2000);
                                    } catch (Exception e) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        startActivityForResult(intent, 2000);
                                    }
                                }
                            else {
                                imagePicker();
                            }
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showRationaleDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void imagePicker() {
//        FilePickerBuilder.getInstance()
//                .setMaxCount(1) //optional
//                .setActivityTheme(R.style.LibAppTheme) //optional
//                .pickPhoto(this, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            if (photoPaths.size() > 0) {
                Uri uri = photoPaths.get(0);
                Log.d("PHOTOPATHS", photoPaths.toString());
                path = null;
                try {
                    path = ContentUriUtils.INSTANCE.getFilePath(mContext, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Glide.with(mContext).load(uri).placeholder(R.drawable.ic_add_user).centerCrop().into(mBinding.ivProfile);
            }
        }
    }

    private void showRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Grant Permission");
        builder.setMessage("Permission is required to access images, files, audios & videos from this device");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void updateProfile() {
        String deviceId = "dGs3DdL5saA%3AAPA91bGwteSY3Qk16-BVxlePfkvjXer9J53zASAMgl8Ywy1Qzcd0txcVH4BnoHJZ19QVT3BMQRtYKutCeEqd9amOsK7fsRWjQS7XopQY3vovvlR1CMioW7npx1AnkgBvO1Ox8Z0HI7ob\n";
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), deviceId);
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtDateOfBirth.getText().toString());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtName.getText().toString());
        RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtMobileNo.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtEmail.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtAddress.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), mBinding.edtCity.getText().toString());

        if (path != null) {
            file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = null;
            if (!path.equalsIgnoreCase(""))
                try {
                    body = MultipartBody.Part.createFormData("profile_image", URLEncoder.encode(file.getName(), "utf-8"), requestFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            if (Utils.isInternetAvailable(getActivity())) {
                ProgressHubKt.showLoader(getActivity());
                SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
                String token = "Bearer " + preferences.getString("token", "");

                Call<UpdateProfileModel> call = RetrofitClient.getInstance().getApi().updateProfile(token, device_id, email, name, mobile_no, dob, address, city, body);
                call.enqueue(new Callback<UpdateProfileModel>() {
                    @Override
                    public void onResponse(Call<UpdateProfileModel> call, Response<UpdateProfileModel> response) {
                        ProgressHubKt.dismissLoader();
                        if (response.code() == 200) {
                            if (response.body().status == 200) {
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                                Log.d("UPDATE_PROFILE", response.body().message);
                            } else {
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileModel> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                        ProgressHubKt.dismissLoader();
                    }
                });

            } else {
                Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Utils.isInternetAvailable(getActivity())) {
                ProgressHubKt.showLoader(getActivity());
                SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
                String token = "Bearer " + preferences.getString("token", "");

                Call<UpdateProfileModel> call = RetrofitClient.getInstance().getApi().updateProfile2(token, device_id, email, name, mobile_no, dob, address, city);
                call.enqueue(new Callback<UpdateProfileModel>() {
                    @Override
                    public void onResponse(Call<UpdateProfileModel> call, Response<UpdateProfileModel> response) {
                        ProgressHubKt.dismissLoader();
                        if (response.code() == 200) {
                            if (response.body().status == 200) {
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                                Log.d("UPDATE_PROFILE", response.body().message);
                            } else {
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileModel> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                        ProgressHubKt.dismissLoader();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "No Internet Connection! Please connect working internet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData() {
        ProgressHubKt.showLoader(getActivity());
        SharedPreferences preferences = this.requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("token", "");
        if (token != null) {
            Call<ProfileModel> call = RetrofitClient.getInstance().getApi().getProfile(token);
            call.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    Log.d("GETDATA", response.body().toString());
                    if (response.code() == 200) {
                        if (response.body().status == 200) {
                            mBinding.edtName.setText(response.body().data.name);
                            mBinding.edtMobileNo.setText(response.body().data.mobile_no);
                            mBinding.edtEmail.setText(response.body().data.email);
                            mBinding.edtAddress.setText(response.body().data.address);
                            mBinding.edtDateOfBirth.setText(response.body().data.dob);
                            mBinding.edtCity.setText(response.body().data.city);
                            Glide.with(getActivity()).load(response.body().data.profile_image).placeholder(R.drawable.ic_add_user).into(mBinding.ivProfile);
                            ProgressHubKt.dismissLoader();
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
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    Log.d("msg", t.getMessage().toString());
                    ProgressHubKt.dismissLoader();
                }
            });
        }
    }
}