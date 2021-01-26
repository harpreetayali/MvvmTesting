package com.harpreetayali.mvvmexample.networkCalls

import android.app.ProgressDialog
import android.content.Context

import android.util.Log

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitApiClass {
    lateinit var context:Context
    fun <T> callService(
        mContext: Context,
        dialogFlag: Boolean,
        token: String,
        requestProcessor: RetrofitApiResponse<T>
    ) {
        try {

            context=mContext
            if (dialogFlag) {
                showDialog(mContext)
            }

            var okHttpClient: OkHttpClient? = null
            if (token.isEmpty()) {
                okHttpClient = OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .build()
            } else {
                val client = OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .addInterceptor { chain ->
                        val original = chain.request()

                        val request = original.newBuilder()
                            .header("Authorization", "$token")
                            .method(original.method(), original.body())
                            .build()

                        chain.proceed(request)
                    }

                okHttpClient = client.build()

            }
            val retrofit = Retrofit.Builder()
                .baseUrl(WebApiKeys.BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()


            val retrofitApi = retrofit.create(RetrofitApi::class.java)

            val coRoutineExceptionHandler = CoroutineExceptionHandler { _, t ->
                t.printStackTrace()

                CoroutineScope(Dispatchers.Main).launch {
                    hideDialog()
                    requestProcessor.onException(t.message)
                    t.printStackTrace()

                    if (t.message.equals("Unable to resolve host")) {
                        //Alerts.commonAlert(mContext, "Unable to resolve host")
                    } else {
                        //timeout
                        //Alerts.commonAlert(mContext, mContext.resources.getString(R.string.server_error)
                        //)
                    }

                }
            }

            CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler).launch {

                val response = requestProcessor.sendRequest(retrofitApi) as Response<*>

                CoroutineScope(Dispatchers.Main).launch {
                    hideDialog()
                    requestProcessor.onResponse(response as T)
                    Log.e("resposeData=====  ", response.toString())
                    Log.e("resposeData=====  ", response.code().toString())
                    if (!response.isSuccessful) {
                        val res = response.errorBody()!!.string()
                        if (res == "Unauthorized") {
                            //requestProcessor.onError("Unauthorized")

                            //Alerts.commonAlert(mContext, "Unauthorized")
                        } else {
                            val jsonObject = JSONObject(res)
                            when {
                                jsonObject.has("message") && !jsonObject.isNull("message") -> {
                                    val message = jsonObject.getString("message")
                                    requestProcessor.onError(message)
                                    if (!message.equals("No record found")) {
                                        //Alerts.commonAlert(mContext, message)
                                    }

                                }
                                else -> {
//                                    requestProcessor.onError(mContext.resources.getString(R.string.password))
                                    /*//Alerts.commonAlert(
                                        mContext,
                                        mContext.resources.getString(R.string.password)
                                    )*/
                                }
                            }
                        }

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Throwable) {
            Log.e("dgfgf", "df" + e.printStackTrace())
        }
    }




    private  var progressDialog:ProgressDialog? = null
    private fun showDialog(mContext: Context) {

        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

    }

    private fun hideDialog() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }

    }
}