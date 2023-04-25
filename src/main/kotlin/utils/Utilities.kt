package utils

import models.description
import models.movie

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notesToFormat: List<movie>): String =
        notesToFormat
            .joinToString(separator = "\n") { movie ->  "$movie" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<description>): String =
        itemsToFormat
            .joinToString(separator = "\n") { description ->  "\t${description}" }

}