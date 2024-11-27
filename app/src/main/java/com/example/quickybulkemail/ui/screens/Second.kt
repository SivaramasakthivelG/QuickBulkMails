package com.example.quickybulkemail.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quickybulkemail.data.remote_model.EmailList
import com.example.quickybulkemail.ui.viewmodel.EmailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Second(
    navHostController: NavHostController,
    viewModel: EmailViewModel,
    modifier: Modifier,
    p: PaddingValues
){
    var mailId by remember { mutableStateOf("") }
    var userMailId by remember { mutableStateOf("") }
    var emailTitle by remember { mutableStateOf("") }
    var emailContent by remember { mutableStateOf("") }
    val emailData by viewModel.emailData.collectAsState(EmailList(emptyList()))
    val context = LocalContext.current
    Scaffold(modifier = modifier.fillMaxSize(), content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = userMailId,
                onValueChange = { userMailId = it },
                label = { Text(text = "Your Email (From)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = mailId,
                onValueChange = { mailId = it },
                label = { Text(text = "Recipient Email (To)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = emailTitle,
                onValueChange = { emailTitle = it },
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = emailContent,
                onValueChange = { emailContent = it },
                label = { Text(text = "Subject") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.isEmailsValid(listOf(mailId)).apply {
                            mailId.isNotEmpty().apply {
                                emailData.emails = mailId.split(",").map{
                                    it.trim()
                                }
                            }
                        }
                        viewModel.updateUserMailId(userMailId,emailTitle,emailContent)

                        if(viewModel.isEmailsValid(emailData.emails)){
                            emailData.emails.forEach{
                                viewModel.sendSendGridEmail1(it)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Send Single Email", style = MaterialTheme.typography.labelLarge)
            }

        }
    })

}