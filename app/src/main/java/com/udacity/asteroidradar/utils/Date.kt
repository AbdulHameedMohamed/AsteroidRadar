package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.Date

object Date {
        fun getCurrentDate(): String {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            return formatter.format(date)
        }
}