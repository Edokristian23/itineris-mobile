package com.edokristian.itineris.model.response

data class GetLeaveHistoriesResponse(
    val code: String,
    val `data`: List<DataX>,
    val message: String
)