package models

import utils.Utilities

data class Movie(
    var MovieId: Int = 0,
    var MovieTitle: String,
    var MoviePriority: Int,
    var MovieCategory: String,
    var isMovieArchived: Boolean = false,
    var descriptions: MutableSet<Description> = mutableSetOf()
) {
    private var lastDescriptionId = 0
    private fun getDescriptionId() = lastDescriptionId++

    fun addDescription(description: Description): Boolean {
        description.descriptionId = getDescriptionId()
        return descriptions.add(description)
    }
    fun numberOfDescriptions() = descriptions.size

    fun findOne(id: Int): Description? {
        return descriptions.find { description -> description.descriptionId == id }
    }

    fun delete(id: Int): Boolean {
        return descriptions.removeIf { description -> description.descriptionId == id }
    }

    fun update(id: Int, newdescription: Description): Boolean {
        val foundDescription = findOne(id)

        // if the object exists, use the details passed in the newdescription parameter to
        // update the found object in the Set
        if (foundDescription != null) {
            foundDescription.descriptionContents = newdescription.descriptionContents
            foundDescription.isDescriptionComplete = newdescription.isDescriptionComplete
            return true
        }

        // if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listDescriptions() =
        if (descriptions.isEmpty()) "\tNO descriptionS ADDED"
        else Utilities.formatSetString(descriptions)

    override fun toString(): String {
        val archived = if (isMovieArchived) 'Y' else 'N'
        return "$MovieId: $MovieTitle, Priority($MoviePriority), Category($MovieCategory), Archived($archived) \n${listDescriptions()}"
    }

    fun checkNoteCompletionStatus(): Boolean {
        if (descriptions.isNotEmpty()) {
            for (description in descriptions) {
                if (!description.isDescriptionComplete) {
                    return false
                }
            }
        }
        return true // a note with empty descriptions can be archived, or all descriptions are complete
    }
}
