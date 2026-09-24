package com.example.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val code: String, val displayName: String) {
    HINDI("hi", "हिंदी"),
    ENGLISH("en", "English")
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.HINDI }

object Strings {
    fun appName(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "खेतमित्र (KhetMitra)" else "KhetMitra"

    fun farmerRole(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "किसान (Farmer)" else "Farmer"
    fun workerRole(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "मजदूर (Worker)" else "Worker"
    fun adminRole(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "एडमिन (Admin)" else "Admin"

    fun farmerPrimaryCta(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "+ मुझे मजदूर चाहिए\n(नया काम पोस्ट करें)" else "+ Need Workers\n(Post a New Job)"
    fun workerPrimaryCta(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "मुझे काम चाहिए\n(पास के काम देखें)" else "Need Work\n(Explore Jobs Near You)"

    fun activeJobs(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "सक्रिय काम (Active Jobs)" else "Active Jobs"
    fun jobsNearYou(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "आपके पास उपलब्ध काम (Jobs Near You)" else "Jobs Near You"
    fun applicants(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "आवेदक मजदूर" else "Applicants"
    fun wage(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "मजदूरी" else "Wage"
    fun perDay(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "/दिन" else "/day"
    fun applyNow(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "आवेदन करें (Apply Now)" else "Apply Now"
    fun applied(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "आवेदन भेजा गया" else "Applied"
    fun accepted(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "स्वीकृत (Accepted)" else "Accepted"
    fun checkIn(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "हाजिरी लगाएं (Check In)" else "Check In"
    fun checkOut(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "कार्य समाप्त (Check Out)" else "Check Out"
    fun markPaid(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "भुगतान दर्ज करें (Mark Paid)" else "Mark Paid"
    fun confirmPayment(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "भुगतान प्राप्ति स्वीकारें" else "Confirm Payment Received"
    fun rateUser(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "रेटिंग दें (Rate)" else "Rate"
    fun referral(lang: AppLanguage) = if (lang == AppLanguage.HINDI) "रेफर और रिवार्ड" else "Refer & Earn"
}
