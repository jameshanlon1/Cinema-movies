package controllers

import Persistence.Serializer
import models.Movie
import utils.Utilities.formatListString
import java.util.ArrayList

class MovieAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var Movies = ArrayList<Movie>()

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0
    private fun getId() = lastId++

    // ----------------------------------------------
    //  CRUD METHODS FOR Movie ArrayList
    // ----------------------------------------------
    fun add(Movie: Movie): Boolean {
        Movie.MovieId = getId()
        return Movies.add(Movie)
    }

    fun delete(id: Int) = Movies.removeIf { Movie -> Movie.MovieId == id }

    fun update(id: Int, Movie: Movie?): Boolean {
        // find the Movie object by the index number
        val foundMovie = findMovie(id)

        // if the Movie exists, use the Movie details passed as parameters to update the found Movie in the ArrayList.
        if ((foundMovie != null) && (Movie != null)) {
            foundMovie.MovieTitle = Movie.MovieTitle
            foundMovie.MovieRating = Movie.MovieRating
            foundMovie.MovieCategory = Movie.MovieCategory
            return true
        }

        // if the Movie was not found, return false, indicating that the update was not successful
        return false
    }

    fun cinemaMovie(id: Int): Boolean {
        val foundMovie = findMovie(id)
        if (( foundMovie != null) && (!foundMovie.isMovieInCinema)
        //  && ( foundMovie.checkMovieCompletionStatus())
        ){
            foundMovie.isMovieInCinema = true
            return true
        }
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR Movie ArrayList
    // ----------------------------------------------
    fun listAllMovies() =
        if (Movies.isEmpty()) "No Movies stored"
        else formatListString(Movies)

    fun listNotCinemaMovies() =
        if (numberOfNotCinemaMovies() == 0) "No Out of Cinema Movies stored"
        else formatListString(Movies.filter { Movie -> !Movie.isMovieInCinema })

    fun listCinemaMovies() =
        if (numberOfCinemaMovies() == 0) "No Cinema Movies stored"
        else formatListString(Movies.filter { Movie -> Movie.isMovieInCinema })

    // ----------------------------------------------
    //  COUNTING METHODS FOR Movie ArrayList
    // ----------------------------------------------
    fun numberOfMovies() = Movies.size
    fun numberOfCinemaMovies(): Int = Movies.count { Movie: Movie -> Movie.isMovieInCinema }
    fun numberOfNotCinemaMovies(): Int = Movies.count { Movie: Movie -> !Movie.isMovieInCinema }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findMovie(MovieId : Int) =  Movies.find{ Movie -> Movie.MovieId == MovieId }

    fun searchMoviesByTitle(searchString: String) =
        formatListString(Movies.filter { Movie -> Movie.MovieTitle.contains(searchString, ignoreCase = true) })


    // ----------------------------------------------
    //  LISTING METHODS FOR actor
    // ----------------------------------------------
    fun listNonOscarActors(): String =
        if (numberOfMovies() == 0) "No Movies stored"
        else {
            var listOfNonOscarActor = ""
            for (Movie in Movies) {
                for (actor in Movie.actors) {
                    if (actor.isActorOscar) {
                        listOfNonOscarActor += Movie.MovieTitle + ": " + actor.actorName + "\n"
                    }
                }
            }
            listOfNonOscarActor
        }

    // ----------------------------------------------
    //  COUNTING METHODS FOR actor
    // ----------------------------------------------
    fun numberOfNonOscarActors(): Int {
        var numberOfNonOscarActors = 0
        for (Movie in Movies) {
            for (actor in Movie.actors) {
                if (actor.isActorOscar) {
                    numberOfNonOscarActors++
                }
            }
        }
        return numberOfNonOscarActors
    }


    fun searchActorByName(searchString: String): String {
        return if (numberOfMovies() == 0) "No Movies stored"
        else {
            var listOfMovies = ""
            for (Movie in Movies) {
                for (actor in Movie.actors) {
                    if (actor.actorName.contains(searchString, ignoreCase = true)) {
                        listOfMovies += "${Movie.MovieId}: ${Movie.MovieTitle} \n\t${actor}\n"
                    }
                }
            }
            if (listOfMovies == "") "No actor found for: $searchString"
            else listOfMovies
        }
    }

    @Throws(Exception::class)
    fun load() {
        Movies = serializer.read() as ArrayList<Movie>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(Movies)
    }

}

