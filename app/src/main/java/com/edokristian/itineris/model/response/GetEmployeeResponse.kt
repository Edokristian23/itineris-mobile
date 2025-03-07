package com.edokristian.itineris.model.response

data class GetEmployeeResponse(
    val code: String,
    val `data`: Data,
    val message: String
)