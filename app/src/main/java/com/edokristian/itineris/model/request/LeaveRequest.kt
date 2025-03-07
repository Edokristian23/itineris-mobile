package com.edokristian.itineris.model.request

data class LeaveRequest(
    val end_date: String,
    val leave_type: Int,
    val reason: String,
    val start_date: String
)