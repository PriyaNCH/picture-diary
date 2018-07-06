package com.lilac.priyacoder.materialdesigninkotlin

/**
 * Created by 1021422 on 10/15/2017.
 */
class MonthComparator {


    companion object : Comparator<String> {

        private val months =  arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")

        override fun compare(month1: String, month2: String): Int = when {

            months.indexOf(month1) == months.indexOf(month2) -> 0
            else -> months.indexOf(month1) - months.indexOf(month2)
        }
    }
}