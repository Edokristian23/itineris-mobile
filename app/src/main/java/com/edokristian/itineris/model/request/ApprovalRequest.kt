package com.edokristian.itineris.model.request

data class ApprovalRequest(
    val id: Int,
    val rejection_note: String,
    val status: Int
)