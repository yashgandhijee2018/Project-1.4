package com.demo.incampus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(
            @Field("username") String username,
            @Field("name") String name,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("token")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<ResponseBody> getToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("code") String code);

    @FormUrlEncoded
    @POST("otp")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoieWFzaCIsImlhdCI6MTU4NDk2OTQ3MH0.y5elEi_0HoBx8H9pYn7p5qSn-669lixXK4RYYSvsBps")
    Call<ResponseBody> otpReceive(
            @Field("phone_number") String phoneNumber
    );

    @FormUrlEncoded
    @POST("verifyotp")
    Call<ResponseBody> verifyotp(
            @Field("otp") String OTP,
            @Field("session_id") String sessionID);
}
