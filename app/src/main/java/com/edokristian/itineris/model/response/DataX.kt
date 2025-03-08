package com.edokristian.itineris.model.response

data class DataX(
    val created_at: String,
    val employee_id: Int,
    val employee_name: String,
    val end_date: String,
    val id: Int,
    val leave_type: String,
    val reason: String,
    val rejection_note: String,
    val start_date: String,
    val status: String,
    val total_days: Int
)