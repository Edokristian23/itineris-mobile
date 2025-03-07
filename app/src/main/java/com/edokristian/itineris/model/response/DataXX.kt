package com.edokristian.itineris.model.response

data class DataXX(
    val created_at: String,
    val created_by: String,
    val department: String,
    val id: Int,
    val leave_balance: Int,
    val name: String,
    val nip: String,
    val position: String,
    val processed_leave: Int,
    val role: String,
    val total_leave: Int,
    val total_pending_request: Int,
    val updated_at: String,
    val updated_by: String
)