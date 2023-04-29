package models

data class Actor (var actorId: Int = 0, var actorName : String, var isActorOscar: Boolean = false){

    override fun toString() =
        if (isActorOscar)
            "$actorId: $actorName (Oscar Winner)"
        else
            "$actorId: $actorName (Not Oscar Winner)"

}
