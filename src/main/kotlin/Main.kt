import Persistence.JSONSerializer
import controllers.MovieAPI
import models.Actor
import models.Movie
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess



private val MovieAPI = MovieAPI(JSONSerializer(File("movies.json")))

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addMovie()
            2 -> listMovies()
            3 -> updateMovie()
            4 -> deleteMovie()
            5 -> cinemaMovie()
            6 -> addActorToMovie()
            7 -> updateActorsInMovie()
            8 -> deleteActor()
            9 -> markActorStatus()
            10 -> searchMovies()
            15 -> searchActors()
            16 -> listToDoActors()
            20  ->save()
            21  ->load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  Movie KEEPER APP                  |
         > -----------------------------------------------------  
         > | Movie MENU                                         |
         > |   1) Add a Movie                                   |
         > |   2) List Movies                                   |
         > |   3) Update a Movie                                |
         > |   4) Delete a Movie                                |
         > |   5) Add To Cinema                               |
         > -----------------------------------------------------  
         > | actor MENU                                         | 
         > |   6) Add Actor to a Movie                           |
         > |   7) Update Actor contents on a Movie               |
         > |   8) Delete Actor from a Movie                      |
         > |   9) Mark Actor as complete/todo                   | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR MovieS                             | 
         > |   10) Search for all Movies (by Movie title)        |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR actorS                             |                                
         > |   15) Search for all Actor (by Actor Name)  |
         > |   16) List TODO Actor                             |
         > |   17) .....                                       |
         > |   20) Save                                       |
         > |   21) Load                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

//------------------------------------
//Movie MENU
//------------------------------------
fun addMovie() {
    val MovieTitle = readNextLine("Enter a title for the Movie: ")
    val MoviePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val MovieCategory = readNextLine("Enter a category for the Movie: ")
    val isAdded = MovieAPI.add(Movie(MovieTitle = MovieTitle, MoviePriority = MoviePriority, MovieCategory = MovieCategory))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listMovies() {
    if (MovieAPI.numberOfMovies() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL Movies          |
                  > |   2) View ACTIVE Movies       |
                  > |   3) View ARCHIVED Movies     |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllMovies()
            2 -> listActiveMovies()
            3 -> listArchivedMovies()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No Movies stored")
    }
}

fun listAllMovies() = println(MovieAPI.listAllMovies())
fun listActiveMovies() = println(MovieAPI.listActiveMovies())
fun listArchivedMovies() = println(MovieAPI.listArchivedMovies())

fun updateMovie() {
    listMovies()
    if (MovieAPI.numberOfMovies() > 0) {
        // only ask the user to choose the Movie if Movies exist
        val id = readNextInt("Enter the id of the Movie to update: ")
        if (MovieAPI.findMovie(id) != null) {
            val MovieTitle = readNextLine("Enter a title for the Movie: ")
            val MoviePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val MovieCategory = readNextLine("Enter a category for the Movie: ")

            // pass the index of the Movie and the new Movie details to MovieAPI for updating and check for success.
            if (MovieAPI.update(id, Movie(0, MovieTitle, MoviePriority, MovieCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no Movies for this index number")
        }
    }
}

fun deleteMovie() {
    listMovies()
    if (MovieAPI.numberOfMovies() > 0) {
        // only ask the user to choose the Movie to delete if Movies exist
        val id = readNextInt("Enter the id of the Movie to delete: ")
        // pass the index of the Movie to MovieAPI for deleting and check for success.
        val MovieToDelete = MovieAPI.delete(id)
        if (MovieToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun cinemaMovie() {
    listActiveMovies()
    if (MovieAPI.numberOfActiveMovies() > 0) {
        // only ask the user to choose the Movie to archive if active Movies exist
        val id = readNextInt("Enter the id of the Movie to archive: ")
        // pass the index of the Movie to MovieAPI for archiving and check for success.
        if (MovieAPI.archiveMovie(id)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

//-------------------------------------------
//actor MENU (only available for active Movies)
//-------------------------------------------

//TODO

//------------------------------------
//Movie REPORTS MENU
//------------------------------------
fun searchMovies() {
    val searchTitle = readNextLine("Enter the actor to search by: ")
    val searchResults = MovieAPI.searchMoviesByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No Movies found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
//actor REPORTS MENU
//------------------------------------

//TODO

//------------------------------------
// Exit App
//------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

//------------------------------------
//HELPER FUNCTIONS
//------------------------------------

private fun askUserToChooseActiveMovie(): Movie? {
    listActiveMovies()
    if (MovieAPI.numberOfActiveMovies() > 0) {
        val Movie = MovieAPI.findMovie(readNextInt("\nEnter the id of the Movie: "))
        if (Movie != null) {
            if (Movie.isMovieArchived) {
                println("Movie is NOT Active, it is Archived")
            } else {
                return Movie //chosen Movie is active
            }
        } else {
            println("Movie id is not valid")
        }
    }
    return null //selected Movie is not active
}

private fun addActorToMovie() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        if (Movie.addActor(Actor(actorContents = readNextLine("\t actor Contents: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateActorsInMovie() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val actor: Actor? = askUserToChooseActor(Movie)
        if (actor != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (Movie.update(actor.actorId, Actor(actorContents = newContents))) {
                println("actor contents updated")
            } else {
                println("actor contents NOT updated")
            }
        } else {
            println("Invalid actor Id")
        }
    }
}

private fun askUserToChooseActor(Movie: Movie): Actor? {
    if (Movie.numberOfActors() > 0) {
        print(Movie.listActors())
        return Movie.findOne(readNextInt("\nEnter the id of the actor: "))
    }
    else{
        println ("No actors for chosen Movie")
        return null
    }
}

fun deleteActor() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val actor: Actor? = askUserToChooseActor(Movie)
        if (actor != null) {
            val isDeleted = Movie.delete(actor.actorId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markActorStatus() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val actor: Actor? = askUserToChooseActor(Movie)
        if (actor != null) {
            var changeStatus = 'X'
            if (actor.isActorComplete) {
                changeStatus = readNextChar("The actor is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    actor.isActorComplete = false
            }
            else {
                changeStatus = readNextChar("The actor is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    actor.isActorComplete = true
            }
        }
    }
}

fun searchActors() {
    val searchContents = readNextLine("Enter the actor contents to search by: ")
    val searchResults = MovieAPI.searchactorByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No actors found")
    } else {
        println(searchResults)
    }
}



fun listToDoActors(){
    if (MovieAPI.numberOfToDoactors() > 0) {
        println("Total TODO actors: ${MovieAPI.numberOfToDoactors()}")
    }
    println(MovieAPI.listToDoactors())
}

fun save() {
    try {
        MovieAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        MovieAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
