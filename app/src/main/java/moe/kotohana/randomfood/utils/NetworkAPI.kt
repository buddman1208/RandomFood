package moe.kotohana.randomfood.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST

/**
 * Created by Sunrin on 2017-05-29.
 */

interface NetworkAPI {
    @get:POST("/auth/login")
    val loginResponse: Call<ResponseBody>
}
