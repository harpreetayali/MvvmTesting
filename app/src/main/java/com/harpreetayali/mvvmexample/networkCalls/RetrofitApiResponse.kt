package com.harpreetayali.mvvmexample.networkCalls

interface RetrofitApiResponse<T> {
    suspend fun sendRequest(retrofitApi: RetrofitApi):T

    fun onResponse(res: T)

    fun onException(message: String?)

    fun onError(error:String)
}