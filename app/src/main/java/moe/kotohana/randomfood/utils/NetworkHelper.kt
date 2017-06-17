package moe.kotohana.randomfood.utils

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by KOHA_DESKTOP on 2016. 6. 29..
 */
class NetworkHelper(private val context: Context) {
    companion object {
        val url = "https://openapi.naver.com/"

        var retrofit: Retrofit? = null

        val networkClient = OkHttpClient.Builder()
        val networkInstance: NetworkAPI
            get() {
                networkClient.addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder()
                            .header("X-Naver-Client-Id", "yQwvCnEirmlpHCICpzIU")
                            .header("X-Naver-Client-Secret", "CS8vIALW9F").build())
                }
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(url)
                            .client(networkClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
                return retrofit!!.create(NetworkAPI::class.java)
            }

        fun returnNetworkState(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
        }
    }
}