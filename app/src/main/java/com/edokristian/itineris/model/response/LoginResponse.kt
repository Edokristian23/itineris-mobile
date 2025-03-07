package com.edokristian.itineris.model.response


data class LoginResponse(
    val data: DataResponseLogin
    
)

class DataResponseLogin(
    val token: String,
    val token_type: String
)
