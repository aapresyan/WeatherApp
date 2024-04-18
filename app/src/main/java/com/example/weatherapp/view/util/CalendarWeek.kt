package com.example.weatherapp.view.util

class CalendarWeek {

    fun getNamesStartingFrom(from: String): List<String> {
        val res = mutableListOf("Tomorrow")
        val fromIndex = (names.indexOf(from) + 2) % 7
        for (i in 0..5) {
            res.add(names[(fromIndex + i) % 7])
        }
        return res
    }

    companion object {
        const val MONDAY = "Monday"
        const val TUESDAY = "Tuesday"
        const val WEDNESDAY = "Wednesday"
        const val THURSDAY = "Thursday"
        const val FRIDAY = "Friday"
        const val SATURDAY = "Saturday"
        const val SUNDAY = "Sunday"
        private val names = listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)
    }
}