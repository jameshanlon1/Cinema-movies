package models

data class Description(var descriptionId: Int = 0, var descriptionContents: String, var isDescriptionComplete: Boolean = false) {

    override fun toString() =
        if (isDescriptionComplete)
            "$descriptionId: $descriptionContents (Complete)"
        else
            "$descriptionId: $descriptionContents (TODO)"
}
