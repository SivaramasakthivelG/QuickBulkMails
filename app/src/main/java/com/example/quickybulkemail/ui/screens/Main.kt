package com.example.quickybulkemail.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickybulkemail.data.remote_model.EmailList
import com.example.quickybulkemail.ui.viewmodel.EmailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Main(
    navController: NavController,
    viewModel: EmailViewModel,
    modifier: Modifier,
    padding: PaddingValues
){
    val emailData by viewModel.emailData.collectAsState(EmailList(emptyList()))
    val context = LocalContext.current

    // Launcher to pick a document and handle the result
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.extractEmailIdsFromUri(context, it)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(padding).padding(start = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Bulk Email Sender",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Compose and send emails effortlessly",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(paddingValues = padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (viewModel.isEmailsValid(emailData.emails)) {
                            for (i in emailData.emails) {
                                viewModel.sendSendGridEmail1(i)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT).show()
                                }
                            }
                            viewModel.clearEmails()
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonColors(Color.Black,Color.White,Color.Red,Color.Black),
                enabled = emailData.emails.isNotEmpty()
            ) {
                Text(text = "Send Email", style = MaterialTheme.typography.labelLarge)
            }

            Button(
                onClick = {
                    filePickerLauncher.launch(arrayOf("*/*"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonColors(Color.Black,Color.White,Color.Red,Color.Black)

            ) {
                Text(text = "Pick File", style = MaterialTheme.typography.labelLarge)
            }
            Button(
                onClick = {
                    navController.navigate("second_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonColors(Color.Black,Color.White,Color.Red,Color.Black)
            ) {
                Text(text = "Send Single Email", style = MaterialTheme.typography.labelLarge)
            }

        }

    }
    ExtractedEmails(modifier,emailData.emails)
}


@Composable
fun ExtractedEmails(modifier: Modifier,emailData: List<String>) {

    Column(
        modifier
            .fillMaxSize().padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (emailData.isNotEmpty()) {
            Box(
                modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(2.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                Column {
                    Text(
                        "Extracted Emails:",
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(emailData.size) {
                            Text(
                                emailData[it],
                                style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}