package com.example.radhegausala.Utils;

import com.example.radhegausala.Model.AddMilkModel;
import com.example.radhegausala.Model.CustomerListModel;
import com.example.radhegausala.Model.EveningReformatModel;
import com.example.radhegausala.Model.LoginModel;
import com.example.radhegausala.Model.MorningReformatModel;
import com.example.radhegausala.Model.ProfileModel;
import com.example.radhegausala.Model.RearrangeEveningListModel;
import com.example.radhegausala.Model.RearrangeMorningListModel;
import com.example.radhegausala.Model.UpdateProfileModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebServices {
    String BASE_URL = "https://radhegirgaushala.com/api/v1/ship/";

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> Login(@FieldMap Map<String, String> Map);

    @Multipart
    @POST("profile/save")
    Call<UpdateProfileModel> updateProfile(@Header("Authorization") String token, @Part("device_id") RequestBody device_id,
                                           @Part("email") RequestBody email, @Part("name") RequestBody name,
                                           @Part("mobile_no") RequestBody mobile_no, @Part("dob") RequestBody dob,
                                           @Part("address") RequestBody address, @Part("city") RequestBody city,
                                           @Part MultipartBody.Part file);

    @Multipart
    @POST("profile/save")
    Call<UpdateProfileModel> updateProfile2(@Header("Authorization") String token, @Part("device_id") RequestBody device_id,
                                            @Part("email") RequestBody email, @Part("name") RequestBody name,
                                            @Part("mobile_no") RequestBody mobile_no, @Part("dob") RequestBody dob,
                                            @Part("address") RequestBody address, @Part("city") RequestBody city);

    @GET("profile/get")
    Call<ProfileModel> getProfile(@Header("Authorization") String token);

    @GET("users/get")
    Call<CustomerListModel> getCustomerList(@Header("Authorization") String token, @Query("s") String search, @Query("date") String date, @Query("page") int page, @Query("morning_evening") String morning);

    @GET("rearrange/users/get")
    Call<RearrangeMorningListModel> getMorningRearrangeList(@Header("Authorization") String token, @Query("s") String search, @Query("page") int page, @Query("morning_evening") String morning);

    @GET("rearrange/users/get")
    Call<RearrangeEveningListModel> getEveningRearrangeList(@Header("Authorization") String token, @Query("s") String search, @Query("page") int page, @Query("morning_evening") String evening);

    @FormUrlEncoded
    @POST("milk/save")
    Call<AddMilkModel> AddMilk(@Header("Authorization") String token, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rearrange/save")
    Call<MorningReformatModel> updateRearrange(@Header("Authorization") String token, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rearrange/save")
    Call<EveningReformatModel> updateRearrange2(@Header("Authorization") String token, @FieldMap Map<String, String> map);
}
