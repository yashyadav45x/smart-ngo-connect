package com.example.smartngoapplication.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class VolunteerRequest(
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val interest: String
)

data class VolunteerResponse(
    val id: Long? = null,
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val interest: String = "",
    val createdAt: String? = null
)

data class DonationRequest(
    val donorName: String = "Anonymous Donor",
    val amount: Double,
    val paymentMethod: String = "UPI (Demo)",
    val status: String = "SUCCESS"
)

data class DonationResponse(
    val id: Long? = null,
    val donorName: String? = null,
    val amount: Double = 0.0,
    val paymentMethod: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)

data class CampaignResponse(
    val id: Long? = null,
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val targetAmount: Double = 0.0,
    val raisedAmount: Double = 0.0
)

interface ApiService {

    @POST("api/volunteers")
    suspend fun registerVolunteer(@Body request: VolunteerRequest): Response<VolunteerResponse>

    @GET("api/volunteers")
    suspend fun getAllVolunteers(): Response<List<VolunteerResponse>>

    @POST("api/donations")
    suspend fun recordDonation(@Body request: DonationRequest): Response<DonationResponse>

    @GET("api/donations")
    suspend fun getAllDonations(): Response<List<DonationResponse>>

    @GET("api/campaigns")
    suspend fun getCampaigns(): Response<List<CampaignResponse>>
}
