package com.chuckerteam.chucker.sample

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

class HttpBinHttpTask(
    client: OkHttpClient,
) : HttpTask {
    private val api = Retrofit.Builder()
        .baseUrl("https://httpbin-latitude.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create<Api>()

    private val noOpCallback = object : Callback<Any?> {
        override fun onResponse(call: Call<Any?>, response: Response<Any?>) = Unit

        override fun onFailure(call: Call<Any?>, t: Throwable) {
            t.printStackTrace()
        }
    }

    @Suppress("MagicNumber")
    override fun run() = with(api) {
        get().enqueue(noOpCallback)
        redirectTo("https://http2.akamai.com?lng=&lat=").enqueue(noOpCallback)
        redirectTo("https://http2.akamai.com?addr=").enqueue(noOpCallback)
        redirectTo("https://http2.akamai.com?longitude=&longitude=").enqueue(noOpCallback)
        // "sensitif path"
        redirectTo("https://http2.akamai.com?lng=123.234234&lat=-324.324234").enqueue(noOpCallback)
        redirectTo("https://http2.akamai.com?addr=Sampalan+Port,+Batununggul,+Kec.+Nusa+Penida,+Kabupaten+Klungkung,+Bali+80771/").enqueue(noOpCallback)
        redirectTo("https://http2.akamai.com?longitude=123.234234&longitude=-324.324234").enqueue(noOpCallback)
    }

    private fun oneShotRequestBody() = object : RequestBody() {
        private val content = Buffer().writeUtf8("Hello, world!")
        override fun isOneShot() = true
        override fun contentType() = "text/plain".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            content.readAll(sink)
        }
    }

    @Suppress("TooManyFunctions")
    private interface Api {
        @GET("/get")
        fun get(): Call<Any?>

        @POST("/post?latitude=-212323&longitude=-324324234")
        fun post(@Body body: Data): Call<Any?>

        @PATCH("/patch")
        fun patch(@Body body: Data): Call<Any?>

        @PUT("/put")
        fun put(@Body body: Data): Call<Any?>

        @DELETE("/delete")
        fun delete(): Call<Any?>

        @GET("/status/{code}")
        fun status(@Path("code") code: Int): Call<Any?>

        @GET("/stream/{lines}")
        fun stream(@Path("lines") lines: Int): Call<Any?>

        @GET("/stream-bytes/{bytes}")
        fun streamBytes(@Path("bytes") bytes: Int): Call<Any?>

        @GET("/delay/{seconds}")
        fun delay(@Path("seconds") seconds: Int): Call<Any?>

        @GET("/bearer")
        fun bearer(@Header("Authorization") token: String): Call<Any?>

        @GET("/redirect-to")
        fun redirectTo(@Query("url") url: String): Call<Any?>

        @GET("/redirect/{times}")
        fun redirect(@Path("times") times: Int): Call<Any?>

        @GET("/relative-redirect/{times}")
        fun redirectRelative(@Path("times") times: Int): Call<Any?>

        @GET("/absolute-redirect/{times}")
        fun redirectAbsolute(@Path("times") times: Int): Call<Any?>

        @GET("/image")
        fun image(@Header("Accept") accept: String): Call<Any?>

        @GET("/brotli")
        @Headers("Accept-Encoding: br")
        fun brotliResponse(): Call<Any?>

        @GET("/gzip")
        @Headers("Accept-Encoding: gzip")
        fun gzipResponse(): Call<Any?>

        @POST("/post")
        @Headers("Content-Encoding: gzip")
        fun gzipRequest(@Body body: Data): Call<Any?>

        @GET("/xml")
        fun xml(): Call<Any?>

        @GET("/encoding/utf8")
        fun utf8(): Call<Any?>

        @GET("/deflate")
        fun deflate(): Call<Any?>

        @GET("/cookies/set")
        fun cookieSet(@Query("k1") value: String): Call<Any?>

        @GET("/basic-auth/{user}/{passwd}")
        fun basicAuth(
            @Path("user") user: String,
            @Path("passwd") passwd: String
        ): Call<Any?>

        @GET("/drip")
        fun drip(
            @Query("numbytes") bytes: Int,
            @Query("duration") seconds: Int,
            @Query("delay") delay: Int,
            @Query("code") code: Int
        ): Call<Any?>

        @GET("/deny")
        fun deny(): Call<Any?>

        @GET("/cache")
        fun cache(@Header("If-Modified-Since") ifModifiedSince: String): Call<Any?>

        @GET("/cache/{seconds}")
        fun cache(@Path("seconds") seconds: Int): Call<Any?>

        @FormUrlEncoded
        @POST("/post")
        fun postForm(@Field("key1") value1: String, @Field("key2") value2: String): Call<Any?>

        @POST("/post")
        fun postRawRequestBody(@Body body: RequestBody): Call<Any?>

        class Data(val thing: String)
    }
}
