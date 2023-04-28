package controllers

import Persistence.Serializer
import models.Description
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
            foundMovie.MoviePriority = Movie.MoviePriority
            foundMovie.MovieCategory = Movie.MovieCategory
            return true
        }

        // if the Movie was not found, return false, indicating that the update was not successful
        return false
    }

    fun archiveMovie(id: Int): Boolean {
        val foundMovie = findMovie(id)
        if (( foundMovie != null) && (!foundMovie.isMovieArchived)
        //  && ( foundMovie.checkMovieCompletionStatus())
        ){
            foundMovie.isMovieArchived = true
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

    fun listActiveMovies() =
        if (numberOfActiveMovies() == 0) "No active Movies stored"
        else formatListString(Movies.filter { Movie -> !Movie.isMovieArchived })

    fun listArchivedMovies() =
        if (numberOfArchivedMovies() == 0) "No archived Movies stored"
        else formatListString(Movies.filter { Movie -> Movie.isMovieArchived })

    // ----------------------------------------------
    //  COUNTING METHODS FOR Movie ArrayList
    // ----------------------------------------------
    fun numberOfMovies() = Movies.size
    fun numberOfArchivedMovies(): Int = Movies.count { Movie: Movie -> Movie.isMovieArchived }
    fun numberOfActiveMovies(): Int = Movies.count { Movie: Movie -> !Movie.isMovieArchived }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findMovie(MovieId : Int) =  Movies.find{ Movie -> Movie.MovieId == MovieId }

    fun searchMoviesByTitle(searchString: String) =
        formatListString(Movies.filter { Movie -> Movie.MovieTitle.contains(searchString, ignoreCase = true) })


    // ----------------------------------------------
    //  LISTING METHODS FOR Description
    // ----------------------------------------------
    fun listToDoDescriptions(): String =
        if (numberOfMovies() == 0) "No Movies stored"
        else {
            var listOfTodoDescription = ""
            for (Movie in Movies) {
                for (description in Movie.descriptions) {
                    if (!description.isDescriptionComplete) {
                        listOfTodoDescription += Movie.MovieTitle + ": " + description.descriptionContents + "\n"
                    }
                }
            }
            listOfTodoDescription
        }

    // ----------------------------------------------
    //  COUNTING METHODS FOR ITEMS
    // ----------------------------------------------
    fun numberOfToDoDescriptions(): Int {
        var numberOfToDoDescriptions = 0
        for (Movie in Movies) {
            for (description in Movie.descriptions) {
                if (!description.isDescriptionComplete) {
                    numberOfToDoDescriptions++
                }
            }
        }
        return numberOfToDoDescriptions
    }


    fun searchDescriptionByContents(searchString: String): String {
        return if (numberOfMovies() == 0) "No Movies stored"
        else {
            var listOfMovies = ""
            for (Movie in Movies) {
                for (description in Movie.descriptions) {
                    if (description.descriptionContents.contains(searchString, ignoreCase = true)) {
                        listOfMovies += "${Movie.MovieId}: ${Movie.MovieTitle} \n\t${description}\n"
                    }
                }
            }
            if (listOfMovies == "") "No description found for: $searchString"
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

