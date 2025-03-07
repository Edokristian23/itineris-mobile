package com.edokristian.itineris.services

import com.edokristian.itineris.model.request.LeaveRequest
import com.edokristian.itineris.model.request.LoginRequest
import com.edokristian.itineris.model.response.GetCurrentEmployeeResponse
import com.edokristian.itineris.model.response.GetLeaveHistoriesResponse
import com.edokristian.itineris.model.response.LeaveResponse
import com.edokristian.itineris.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("employees")
    suspend fun getEmployee(@Header("Authorization") token : String): Response<GetCurrentEmployeeResponse>

    @GET("leave-requests")
    suspend fun getAllLeaveHistories(@Header("Authorization") token : String): Response<GetLeaveHistoriesResponse>
    
    @POST("leave-requests")
    suspend fun leaveRequest(
        @Header("Authorization") token : String,
        @Body leaveRequest: LeaveRequest
        ): Response<LeaveResponse>

}