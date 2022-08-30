package com.chuckerteam.chucker.sample

import android.content.Context
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.chuckerteam.chucker.api.SensitivityCheck
import com.chuckerteam.chucker.sample.helper.RegexSample.combinedRegex
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val BASE_URL = "https://httpbin.org"
private const val SEGMENT_SIZE = 8_192L

class HttpBinClient(
    context: Context
) {

    private val collector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR,
        sensitivityCheck = SensitivityCheck.Builder(combinedRegex)
            .isCheckHostSensitivity(true)
            .isCheckPathSensitivity(true)
            .isCheckResponseBodySensitivity(true)
            .build()
    )

    private val chuckerInterceptor = ChuckerInterceptor(
        context = context,
        collector = collector,
        maxContentLength = 250000L,
        headersToRedact = emptySet<String>()
    )

    private val httpClient =
        OkHttpClient.Builder()
            // Add a ChuckerInterceptor instance to your OkHttp client
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    private val api: HttpBinApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(HttpBinApi::class.java)
    }

    @Suppress("MagicNumber")
    internal fun doHttpActivity() {
        val cb = object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) = Unit

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                t.printStackTrace()
            }
        }

        with(api) {
            get().enqueue(cb)
            redirectTo("https://http2.akamai.com?lng=&lat=").enqueue(cb)
            redirectTo("https://http2.akamai.com?addr=").enqueue(cb)
            redirectTo("https://http2.akamai.com?longitude=&longitude=").enqueue(cb)
            // "sensitif path"
            redirectTo("https://http2.akamai.com?lng=123.234234&lat=-324.324234").enqueue(cb)
            redirectTo("https://http2.akamai.com?addr=Sampalan+Port,+Batununggul,+Kec.+Nusa+Penida").enqueue(cb)
            redirectTo("https://http2.akamai.com?longitude=123.234234&longitude=-324.324234").enqueue(cb)
        }
        downloadSampleImage(colorHex = "fff")
        downloadSampleImage(colorHex = "000")
        getResponsePartially()
    }

    internal fun initializeCrashHandler() {
        Chucker.registerDefaultCrashHandler(collector)
    }

    internal fun recordException() {
        collector.onError("Example button pressed", RuntimeException("User triggered the button"))
        // You can also throw exception, it will be caught thanks to "Chucker.registerDefaultCrashHandler"
        // throw new RuntimeException("User triggered the button");
    }

    private fun downloadSampleImage(colorHex: String) {
        val request = Request.Builder()
            .url("https://dummyimage.com/200x200/$colorHex/$colorHex.png")
            .get()
            .build()
        httpClient.newCall(request).enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) = Unit

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.source()?.use { it.readByteString() }
                }
            }
        )
    }

    private fun getResponsePartially() {
        val body = RequestBody.create(MediaType.get("application/json"), LARGE_JSON)
        val request = Request.Builder()
            .url("https://postman-echo.com/post")
            .post(body)
            .build()
        httpClient.newCall(request).enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) = Unit

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.source()?.use { it.readByteString(SEGMENT_SIZE) }
                }
            }
        )
    }
}
