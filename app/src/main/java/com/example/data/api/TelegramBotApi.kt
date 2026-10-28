package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class TelegramApiResponse<T>(
    @Json(name = "ok") val ok: Boolean,
    @Json(name = "result") val result: T? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "error_code") val errorCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class TgUser(
    @Json(name = "id") val id: Long,
    @Json(name = "is_bot") val isBot: Boolean = false,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String? = null,
    @Json(name = "username") val username: String? = null
)

@JsonClass(generateAdapter = true)
data class TgChat(
    @Json(name = "id") val id: Long,
    @Json(name = "type") val type: String,
    @Json(name = "title") val title: String? = null,
    @Json(name = "username") val username: String? = null,
    @Json(name = "description") val description: String? = null
)

@JsonClass(generateAdapter = true)
data class TgPhotoSize(
    @Json(name = "file_id") val fileId: String,
    @Json(name = "file_unique_id") val fileUniqueId: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "file_size") val fileSize: Long? = null
)

@JsonClass(generateAdapter = true)
data class TgDocument(
    @Json(name = "file_id") val fileId: String,
    @Json(name = "file_unique_id") val fileUniqueId: String,
    @Json(name = "file_name") val fileName: String? = null,
    @Json(name = "mime_type") val mimeType: String? = null,
    @Json(name = "file_size") val fileSize: Long? = null
)

@JsonClass(generateAdapter = true)
data class TgVideo(
    @Json(name = "file_id") val fileId: String,
    @Json(name = "file_unique_id") val fileUniqueId: String,
    @Json(name = "duration") val duration: Int = 0,
    @Json(name = "file_name") val fileName: String? = null,
    @Json(name = "mime_type") val mimeType: String? = null,
    @Json(name = "file_size") val fileSize: Long? = null
)

@JsonClass(generateAdapter = true)
data class TgAudio(
    @Json(name = "file_id") val fileId: String,
    @Json(name = "file_unique_id") val fileUniqueId: String,
    @Json(name = "duration") val duration: Int = 0,
    @Json(name = "file_name") val fileName: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "mime_type") val mimeType: String? = null,
    @Json(name = "file_size") val fileSize: Long? = null
)

@JsonClass(generateAdapter = true)
data class TgMessage(
    @Json(name = "message_id") val messageId: Long,
    @Json(name = "from") val from: TgUser? = null,
    @Json(name = "chat") val chat: TgChat,
    @Json(name = "date") val date: Long,
    @Json(name = "text") val text: String? = null,
    @Json(name = "caption") val caption: String? = null,
    @Json(name = "document") val document: TgDocument? = null,
    @Json(name = "video") val video: TgVideo? = null,
    @Json(name = "photo") val photo: List<TgPhotoSize>? = null,
    @Json(name = "audio") val audio: TgAudio? = null
)

@JsonClass(generateAdapter = true)
data class TgUpdate(
    @Json(name = "update_id") val updateId: Long,
    @Json(name = "message") val message: TgMessage? = null,
    @Json(name = "channel_post") val channelPost: TgMessage? = null
)

@JsonClass(generateAdapter = true)
data class TgFile(
    @Json(name = "file_id") val fileId: String,
    @Json(name = "file_unique_id") val fileUniqueId: String,
    @Json(name = "file_size") val fileSize: Long? = null,
    @Json(name = "file_path") val filePath: String? = null
)

interface TelegramBotApiService {

    @GET("bot{token}/getMe")
    suspend fun getMe(
        @Path("token") token: String
    ): TelegramApiResponse<TgUser>

    @GET("bot{token}/getUpdates")
    suspend fun getUpdates(
        @Path("token") token: String,
        @Query("limit") limit: Int = 100
    ): TelegramApiResponse<List<TgUpdate>>

    @GET("bot{token}/getChat")
    suspend fun getChat(
        @Path("token") token: String,
        @Query("chat_id") chatId: String
    ): TelegramApiResponse<TgChat>

    @GET("bot{token}/getFile")
    suspend fun getFile(
        @Path("token") token: String,
        @Query("file_id") fileId: String
    ): TelegramApiResponse<TgFile>
}

object TelegramApiClient {
    private const val BASE_URL = "https://api.telegram.org/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val service: TelegramBotApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TelegramBotApiService::class.java)
    }

    fun getDownloadUrl(token: String, filePath: String): String {
        return "https://api.telegram.org/file/bot$token/$filePath"
    }
}
