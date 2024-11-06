package com.example.quickybulkemail.data.remote_model

class EmailModel {

    data class EmailRequest(
        val personalizations: List<Personalization>,
        val from: Sender,
        val subject: String,
        val content: List<Content>
    )

    data class Personalization(val to: List<Recipient>)
    data class Sender(val email: String)
    data class Content(val type: String, val value: String)
    data class Recipient(val email: String)

}