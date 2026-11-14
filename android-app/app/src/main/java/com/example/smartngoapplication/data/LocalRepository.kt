package com.example.smartngoapplication.data

import com.example.smartngoapplication.network.CampaignResponse
import com.example.smartngoapplication.network.DonationRequest
import com.example.smartngoapplication.network.DonationResponse
import com.example.smartngoapplication.network.VolunteerRequest
import com.example.smartngoapplication.network.VolunteerResponse
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NGOCampaign(
    val id: Long? = null,
    val title: String,
    val category: String,
    val description: String,
    val target: Int,
    val raised: Int
)

data class ImpactStory(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val metric: String,
    val icon: String
)

object LocalRepository {

    private var nextVolunteerId = 100L
    private var nextDonationId = 500L

    private val localVolunteers = mutableListOf(
        VolunteerResponse(
            id = 1,
            fullName = "Yash Yadav",
            phoneNumber = "9876543210",
            email = "yashyadav123@gmail.com",
            interest = "Teaching & Education",
            createdAt = getCurrentTimeStamp()
        ),
        VolunteerResponse(
            id = 2,
            fullName = "Prakash Sharma",
            phoneNumber = "9812345678",
            email = "prakash123@gmail.com",
            interest = "Food Distribution",
            createdAt = getCurrentTimeStamp()
        ),
        VolunteerResponse(
            id = 3,
            fullName = "Ananya Patel",
            phoneNumber = "9765432109",
            email = "ananya.patel@gmail.com",
            interest = "Healthcare Drive",
            createdAt = getCurrentTimeStamp()
        )
    )

    private val localDonations = mutableListOf(
        DonationResponse(
            id = 101,
            donorName = "Yash Yadav",
            amount = 5000.0,
            paymentMethod = "UPI (GPay)",
            status = "SUCCESS",
            createdAt = getCurrentTimeStamp()
        ),
        DonationResponse(
            id = 102,
            donorName = "Rajesh Gupta",
            amount = 1000.0,
            paymentMethod = "PhonePe",
            status = "SUCCESS",
            createdAt = getCurrentTimeStamp()
        ),
        DonationResponse(
            id = 103,
            donorName = "Anonymous Supporter",
            amount = 500.0,
            paymentMethod = "Paytm UPI",
            status = "SUCCESS",
            createdAt = getCurrentTimeStamp()
        )
    )

    private val localCampaigns = mutableListOf(
        NGOCampaign(1, "Feed a Family", "FOOD & HUNGER", "Help provide nutritious meals to families in need.", 100000, 68000),
        NGOCampaign(2, "Education for Every Child", "EDUCATION", "Support books, school supplies and learning resources.", 150000, 82500),
        NGOCampaign(3, "Healthcare for All", "HEALTHCARE", "Help communities access essential medical support.", 200000, 90000),
        NGOCampaign(4, "Care for Street Animals", "ANIMAL WELFARE", "Support food and care for vulnerable street animals.", 75000, 45000),
        NGOCampaign(5, "Clean Drinking Water Initiative", "COMMUNITY & WATER", "Installing water purification units and tube-wells in rural areas.", 120000, 55000),
        NGOCampaign(6, "Winter Warmth Drive", "SHELTER & CLOTHING", "Distributing warm blankets and winter clothes to homeless individuals.", 80000, 48000),
        NGOCampaign(7, "Women Skill Empowerment", "VOCATIONAL SKILLS", "Providing sewing machines and vocational training for women.", 110000, 62000),
        NGOCampaign(8, "Disaster Emergency Relief", "EMERGENCY RELIEF", "Emergency food, medical and shelter assistance for flood victims.", 250000, 185000),
        NGOCampaign(9, "Elderly Care & Support", "SENIOR CARE", "Providing monthly healthcare, groceries and companionship to senior citizens.", 95000, 52000),
        NGOCampaign(10, "Tree Plantation & Green Planet", "ENVIRONMENT", "Planting 10,000 native trees across urban and rural zones.", 60000, 38000),
        NGOCampaign(11, "Digital Literacy Drive", "EDUCATION & TECH", "Setting up computer labs in village schools for children.", 130000, 71000),
        NGOCampaign(12, "Child Nutrition Support", "CHILDCARE", "Providing daily protein milk & nutritious snacks to slum children.", 70000, 49000),
        NGOCampaign(13, "Free Eye Surgery Camp", "HEALTHCARE", "Sponsoring cataract surgeries and free spectacles for needy patients.", 180000, 112000),
        NGOCampaign(14, "Slum Library & Learning Hub", "COMMUNITY LEARNING", "Building safe, quiet reading rooms and libraries for street kids.", 90000, 54000),
        NGOCampaign(15, "Stray Animal Mobile Vet Care", "ANIMAL HEALTH", "Operating a mobile veterinary care van for injured stray animals.", 140000, 88000),
        NGOCampaign(16, "Youth Sports & Talent Academy", "YOUTH DEVELOPMENT", "Coaching underprivileged youth in football, athletics & cricket.", 85000, 43000)
    )

    private val localStories = listOf(
        ImpactStory(
            id = 1,
            category = "FOOD • NUTRITION",
            title = "5,000+ Hot Meals Served",
            description = "Provided nutritious cooked meals and ration kits to homeless families and daily-wage workers during harsh weather conditions.",
            metric = "5,000+ Meals",
            icon = "🍲"
        ),
        ImpactStory(
            id = 2,
            category = "EDUCATION • YOUTH",
            title = "1,200+ Children Equipped for School",
            description = "Distributed school bags, notebooks, stationery, and uniforms to underprivileged students in rural and slum communities.",
            metric = "1,200+ Students",
            icon = "📚"
        ),
        ImpactStory(
            id = 3,
            category = "HEALTHCARE • COMMUNITY",
            title = "15+ Free Medical Health Checkup Camps",
            description = "Organized free medical diagnosis, general health checkups, eye tests, and free medicine distribution for elderly citizens.",
            metric = "15+ Health Camps",
            icon = "🩺"
        ),
        ImpactStory(
            id = 4,
            category = "ANIMAL WELFARE",
            title = "350+ Stray Animals Rescued & Treated",
            description = "Rescued injured street dogs and animals, provided vaccination, medical treatment, and daily food distribution.",
            metric = "350+ Animals",
            icon = "🐾"
        ),
        ImpactStory(
            id = 5,
            category = "CLEAN WATER • VILLAGES",
            title = "8 Clean Water Tube-Wells Installed",
            description = "Installed deep tube-wells and water purification units in drought-affected villages, providing safe drinking water.",
            metric = "3,000+ Villagers",
            icon = "🚰"
        ),
        ImpactStory(
            id = 6,
            category = "WOMEN EMPOWERMENT",
            title = "250+ Women Skill Training Workshops",
            description = "Trained women in tailoring, handicrafts, and basic computer skills to help them gain financial independence.",
            metric = "250+ Women",
            icon = "👩‍💻"
        )
    )

    fun addVolunteer(request: VolunteerRequest): VolunteerResponse {
        val newVol = VolunteerResponse(
            id = ++nextVolunteerId,
            fullName = request.fullName,
            phoneNumber = request.phoneNumber,
            email = request.email,
            interest = request.interest,
            createdAt = getCurrentTimeStamp()
        )
        localVolunteers.add(0, newVol)
        return newVol
    }

    fun getVolunteers(remoteList: List<VolunteerResponse>? = null): List<VolunteerResponse> {
        if (!remoteList.isNullOrEmpty()) {
            val ids = localVolunteers.mapNotNull { it.id }.toSet()
            remoteList.forEach { remoteItem ->
                if (remoteItem.id != null && !ids.contains(remoteItem.id)) {
                    localVolunteers.add(remoteItem)
                }
            }
        }
        return localVolunteers.toList()
    }

    fun getVolunteersCount(): Int {
        return 125 + localVolunteers.size
    }

    fun addDonation(request: DonationRequest): DonationResponse {
        val newDon = DonationResponse(
            id = ++nextDonationId,
            donorName = if (request.donorName.isBlank()) "Anonymous Supporter" else request.donorName,
            amount = request.amount,
            paymentMethod = request.paymentMethod,
            status = "SUCCESS",
            createdAt = getCurrentTimeStamp()
        )
        localDonations.add(0, newDon)
        return newDon
    }

    fun getDonations(remoteList: List<DonationResponse>? = null): List<DonationResponse> {
        if (!remoteList.isNullOrEmpty()) {
            val ids = localDonations.mapNotNull { it.id }.toSet()
            remoteList.forEach { remoteItem ->
                if (remoteItem.id != null && !ids.contains(remoteItem.id)) {
                    localDonations.add(remoteItem)
                }
            }
        }
        return localDonations.toList()
    }

    fun getCampaigns(remoteList: List<CampaignResponse>? = null): List<NGOCampaign> {
        if (!remoteList.isNullOrEmpty()) {
            val existingTitles = localCampaigns.map { it.title.lowercase() }.toSet()
            remoteList.forEach { remote ->
                if (!existingTitles.contains(remote.title.lowercase())) {
                    localCampaigns.add(
                        NGOCampaign(
                            id = remote.id,
                            title = remote.title,
                            category = remote.category,
                            description = remote.description,
                            target = remote.targetAmount.toInt(),
                            raised = remote.raisedAmount.toInt()
                        )
                    )
                }
            }
        }
        return localCampaigns.toList()
    }

    fun getCampaignsCount(): Int {
        return localCampaigns.size
    }

    fun getStoriesOfImpact(): List<ImpactStory> {
        return localStories
    }

    fun getTotalRaisedAmount(): Double {
        val campaignRaisedTotal = localCampaigns.sumOf { it.raised.toDouble() }
        val donationsTotal = localDonations.sumOf { it.amount }
        return campaignRaisedTotal + donationsTotal
    }

    fun getFormattedRaisedAmount(): String {
        val total = getTotalRaisedAmount()
        return if (total >= 100000) {
            val lakhValue = total / 100000.0
            val formatted = String.format(Locale.US, "%.1f", lakhValue)
            "₹${formatted}L"
        } else {
            "₹${total.toInt()}"
        }
    }

    fun getFormattedRaisedFull(): String {
        return try {
            val localeIn = Locale.Builder().setLanguage("en").setRegion("IN").build()
            val formatter = NumberFormat.getCurrencyInstance(localeIn)
            formatter.format(getTotalRaisedAmount()).replace(".00", "")
        } catch (_: Exception) {
            "₹${getTotalRaisedAmount().toInt()}"
        }
    }

    private fun getCurrentTimeStamp(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.format(Date())
        } catch (_: Exception) {
            "2026-10-24T21:00:00"
        }
    }
}
