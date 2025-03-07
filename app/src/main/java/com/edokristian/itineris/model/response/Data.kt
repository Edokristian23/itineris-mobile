package com.edokristian.itineris.model.response

data class Data(
    val created_at: String,
    val created_by: String,
    val id: Int,
    val leave_balance: Int,
    val name: String,
    val nip: String,
    val role: String,
    val total_pending_request: Int,
    val updated_at: String,
    val updated_by: String
)