package com.example.quickybulkemail.ui.viewmodel

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickybulkemail.data.remote.SendGridService
import com.example.quickybulkemail.data.remote_model.EmailModel
import com.example.quickybulkemail.data.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(private val repository: EmailRepository) : ViewModel() {

    fun sendSendGridEmail1(mailId: String, context: Context) {

        // Prepare email content
        val emailRequest = EmailModel.EmailRequest(
            personalizations = listOf(
                EmailModel.Personalization(
                    to = listOf(
                        EmailModel.Recipient(
                            email = mailId
                        )
                    )
                )
            ),
            from = EmailModel.Sender(email = "youselflove82@gmail.com"),
            subject = "NoReply",
            content = listOf(
                EmailModel.Content(
                    type = "text/plain",
                    value = "This Mail is regarding the test purpose thank you for using our app."
                )
            )
        )

        viewModelScope.launch {
            try {
                val service = repository.sendGridService(emailRequest = emailRequest)

                withContext(Dispatchers.Main) { // Switch to the main thread
                    if (service.isSuccessful) {
                        Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show()
                    } else {
                        println("Failed to send email: ${service.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                println("Error sending email: ${e.message}")
            }
        }

    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}