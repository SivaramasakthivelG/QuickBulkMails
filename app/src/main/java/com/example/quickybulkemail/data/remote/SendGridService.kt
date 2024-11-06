package com.example.quickybulkemail.data.remote

import com.example.quickybulkemail.data.remote_model.EmailModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val API_KEY = ""

interface SendGridService {
    @Headers(
        "Authorization: Bearer $API_KEY",
        "Content-Type: application/json"
    )
    @POST("v3/mail/send")
    suspend fun sendEmail(@Body emailRequest: EmailModel.EmailRequest): Response<Void>
}
