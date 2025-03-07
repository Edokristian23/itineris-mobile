package com.edokristian.itineris.model.employee

import com.google.gson.annotations.SerializedName

data class Employee(
    val id: Int,
    val nip: String,
    val name: String,
    val password: String,
    val role_id : Int,
    val leave_balance: Int,
    val position: String,
    val department: String,
    val is_pns: Boolean
)
