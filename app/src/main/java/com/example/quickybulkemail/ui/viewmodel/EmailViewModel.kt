package com.example.quickybulkemail.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickybulkemail.data.remote_model.EmailList
import com.example.quickybulkemail.data.remote_model.EmailModel
import com.example.quickybulkemail.data.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.lang.StringBuilder
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(private val repository: EmailRepository) : ViewModel() {
    private val _emailList = MutableStateFlow(EmailList(emptyList()))
    val emailData: StateFlow<EmailList> = _emailList.asStateFlow()

    private val _userMail = MutableStateFlow("")
    val userMail: StateFlow<String> = _userMail.asStateFlow()

    private val _emailTitle = MutableStateFlow("")
    val emailTitle: StateFlow<String> = _emailTitle.asStateFlow()

    private val _emailSubject = MutableStateFlow("")
    val emailSubject: StateFlow<String> = _emailSubject.asStateFlow()


    suspend fun sendSendGridEmail1(mailId: String): Boolean {
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
            from = EmailModel.Sender(
                email = userMail.value.ifEmpty {
                    "youselflove82@gmail.com"
                }
            ),
            subject = emailTitle.value.ifEmpty { "No Reply" },
            content = listOf(
                EmailModel.Content(
                    type = "text/plain",
                    value = emailSubject.value.ifEmpty { "Awesome" }
                )
            )
        )

        return try {
            val service = repository.sendGridService(emailRequest = emailRequest)
            service.isSuccessful
        } catch (e: Exception) {
            println("Error sending email: ${e.message}")
            false
        }
    }


    fun clearEmails() {
        _emailList.value = EmailList(emptyList())
    }

    fun isEmailsValid(email: List<String>): Boolean {
        email.forEach {
            if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                return false
            }
        }
        return true;
    }

    fun updateUserMailId(mailId: String, emailTitle: String, emailContent: String) {
        if(isEmailsValid(listOf(mailId))){
            _userMail.value = mailId
            _emailTitle.value = emailTitle
            _emailSubject.value = emailContent
        }else{
            val TAG = "Invalid Email"
            Log.d(TAG, "222")
        }
    }

    fun extractEmailIdsFromUri(context: Context, it: Uri) {
        viewModelScope.launch {
            val emailList = mutableListOf<String>()
            val emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

            try {
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.let {
                    val reader = XWPFDocument(inputStream)
                    val extractedText = StringBuilder()

                    for(paragraph in reader.paragraphs){
                        extractedText.append(paragraph.text).append("\n")
                    }

                    val matcher = emailPattern.matcher(extractedText.toString())
                    while (matcher.find()) {
                        val email = matcher.group()
                        if (isEmailsValid(listOf(email))) {
                            emailList.add(email)
                        }
                    }
                    reader.close()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Error reading file", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
            _emailList.value = EmailList(emailList)
        }
    }


}