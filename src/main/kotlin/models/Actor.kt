package models

data class Actor (var actorId: Int = 0, var actorContents : String, var isActorComplete: Boolean = false){

    override fun toString() =
        if (isActorComplete)
            "$actorId: $actorContents (Complete)"
        else
            "$actorId: $actorContents (TODO)"

}
