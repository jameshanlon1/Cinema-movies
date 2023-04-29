package models

import utils.Utilities

data class Movie(var MovieId: Int = 0,
                var MovieTitle: String,
                var MoviePriority: Int,
                var MovieCategory: String,
                var isMovieArchived: Boolean = false,
                var actors : MutableSet<Actor> = mutableSetOf()){
    private var lastActorId = 0
    private fun getActorId() = lastActorId++

    fun addActor(actor: Actor) : Boolean {
        actor.actorId = getActorId()
        return actors.add(actor)
    }
    fun numberOfActors() = actors.size

    fun findOne(id: Int): Actor?{
        return actors.find{ Actor -> Actor.actorId == id }
    }

    fun delete(id: Int): Boolean {
        return actors.removeIf { Actor -> Actor.actorId == id}
    }

    fun update(id: Int, newActor : Actor): Boolean {
        val foundActor = findOne(id)

        //if the object exists, use the details passed in the newActor parameter to
        //update the found object in the Set
        if (foundActor != null){
            foundActor.actorContents = newActor.actorContents
            foundActor.isActorComplete = newActor.isActorComplete
            return true
        }

        //if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listActors() =
        if (actors.isEmpty())  "\tNO ActorS ADDED"
        else  Utilities.formatSetString(actors)


    override fun toString(): String {
        val archived = if (isMovieArchived) 'Y' else 'N'
        return "$MovieId: $MovieTitle, Priority($MoviePriority), Category($MovieCategory), Archived($archived) \n${listActors()}"
    }


    fun checkMovieCompletionStatus(): Boolean {
        if (actors.isNotEmpty()) {
            for (Actor in actors) {
                if (!Actor.isActorComplete) {
                    return false
                }
            }
        }
        return true //a note with empty Actors can be archived, or all Actors are complete
    }


}