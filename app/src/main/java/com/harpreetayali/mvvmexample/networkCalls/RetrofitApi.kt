package com.harpreetayali.mvvmexample.networkCalls

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {

    @FormUrlEncoded
    @POST(WebApiKeys.SIGN_UP)
    suspend fun callSignUp(
        @Field("user_type") type: String,
        @Field("mobile_number") phoneNumber: String,
        @Field("password") password: String
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST(WebApiKeys.LOGIN)
    suspend fun callLogin(
        @Field("mobile_number") phoneNumber: String,
        @Field("password") password: String
    ): Response<ResponseBody>




}