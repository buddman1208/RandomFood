package moe.kotohana.randomfood.utils

import android.content.Context
import android.net.ConnectivityManager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by KOHA_DESKTOP on 2016. 6. 29..
 */
class NetworkHelper(private val context: Context) {
    companion object {
        val url = "http://hangeulro.iwin247.kr"
        val port = 80

        var retrofit: Retrofit? = null

        val networkInstance: NetworkAPI
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(url + ":" + port)
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