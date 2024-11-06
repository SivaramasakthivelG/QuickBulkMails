package com.example.quickybulkemail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quickybulkemail.ui.theme.QuickyBulkEmailTheme
import com.example.quickybulkemail.ui.viewmodel.EmailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
const val FILE_PICKER_REQUEST_CODE = 100
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickyBulkEmailTheme {
                val viewModel: EmailViewModel = hiltViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UiSetUp(modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UiSetUp(modifier: Modifier, viewModel: EmailViewModel) {
    var mailId by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = mailId,
                        onValueChange = {
                            mailId = it
                        },
                        label = { Text(text = "Enter email address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        },
        content = {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (viewModel.isEmailValid(mailId)) {
                                viewModel.sendSendGridEmail1(mailId, context)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    },
                    enabled = mailId.isNotEmpty(),
                    modifier = Modifier
                        .height(80.dp)  // Increase button height for a more modern look
                        .width(180.dp)  // Make the button fill the width of the parent
                        .padding(16.dp)  // Add padding around the button for better spacing
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )  // Button background with rounded corners
                ) {
                    Text(
                        text = "Send Email",
                        style = TextStyle(
                            fontSize = 18.sp,  // Larger text for readability
                            fontStyle = FontStyle.Italic,  // Make the text italic
                            color = Color.White  // White color for the text to contrast against the button
                        ),
                    )
                }

//                Button(
//                    onClick = {
//                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                            type = "*/*"
//                            addCategory(Intent.CATEGORY_OPENABLE)
//                        }
//
//                    },
//                    enabled = mailId.isNotEmpty(),
//                    modifier = modifier
//                        .padding(16.dp)
//                        .height(80.dp)
//                        .width(180.dp)
//                        .background(
//                            MaterialTheme.colorScheme.primary,
//                            shape = MaterialTheme.shapes.medium
//                        )
//                ) {
//                    Text(
//                        text = "File pick",
//                        style = TextStyle(
//                            fontSize = 18.sp,
//                            fontStyle = FontStyle.Italic
//                        )
//                    )
//                }

            }
        }
    )

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickyBulkEmailTheme {

    }
}