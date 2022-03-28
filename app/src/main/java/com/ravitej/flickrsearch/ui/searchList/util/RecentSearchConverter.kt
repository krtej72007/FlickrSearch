package com.ravitej.flickrsearch.ui.searchList.util

object RecentSearchConverter {

    fun String.convertToListFromString(): List<String> {
        return this.split(";").filter { it.isNotEmpty() }
    }

    fun List<String>.convertToStringFromList(): String {
        val outputSB = StringBuilder()

        this.forEach {
            outputSB.append("$it;")
        }

        return outputSB.toString()
    }
}