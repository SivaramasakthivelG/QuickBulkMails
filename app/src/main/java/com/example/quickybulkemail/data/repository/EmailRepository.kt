package com.example.quickybulkemail.data.repository

import com.example.quickybulkemail.data.remote.SendGridService
import com.example.quickybulkemail.data.remote_model.EmailModel
import retrofit2.Response
import javax.inject.Inject


@Suppress("UNREACHABLE_CODE")
class EmailRepository @Inject constructor(private val apiService: SendGridService) {

    suspend fun sendGridService(emailRequest: EmailModel.EmailRequest): Response<Void> {

        try {
            val response = apiService.sendEmail(emailRequest)
            if (response.isSuccessful) {
                return response
            } else {
                throw Exception("Failed to send email")
            }
        } catch (e: Exception) {
            println("Error Sending Email: ${e.message}")
        }
        return error("Failed to send email");
    }
}
