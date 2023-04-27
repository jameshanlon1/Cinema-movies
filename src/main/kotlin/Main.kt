import controllers.MovieAPI
import models.Description
import models.Movie
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess

private val MovieAPI = MovieAPI()

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addMovie()
            2 -> listMovies()
            3 -> updateMovie()
            4 -> deleteMovie()
            5 -> archiveMovie()
            6 -> addDescriptionToMovie()
            7 -> updateDescriptionContentsInMovie()
            8 -> deleteAndescription()
            9 -> markdescriptionStatus()
            10 -> searchMovies()
            15 -> searchdescriptions()
            16 -> listToDoDescriptions()
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
         > |   5) Archive a Movie                               |
         > -----------------------------------------------------  
         > | description MENU                                         | 
         > |   6) Add description to a Movie                           |
         > |   7) Update description contents on a Movie               |
         > |   8) Delete description from a Movie                      |
         > |   9) Mark description as complete/todo                   | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR MovieS                             | 
         > |   10) Search for all Movies (by Movie title)        |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR descriptionS                             |                                
         > |   15) Search for all descriptions (by description description)  |
         > |   16) List TODO descriptions                             |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

// ------------------------------------
// Movie MENU
// ------------------------------------
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
            if (MovieAPI.update(id, Movie(0, MovieTitle, MoviePriority, MovieCategory, false))) {
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

fun archiveMovie() {
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

// -------------------------------------------
// description MENU (only available for active Movies)
// -------------------------------------------

// TODO

// ------------------------------------
// Movie REPORTS MENU
// ------------------------------------
fun searchMovies() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = MovieAPI.searchMoviesByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No Movies found")
    } else {
        println(searchResults)
    }
}

// ------------------------------------
// description REPORTS MENU
// ------------------------------------

// TODO

// ------------------------------------
// Exit App
// ------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

// ------------------------------------
// HELPER FUNCTIONS
// ------------------------------------

private fun askUserToChooseActiveMovie(): Movie? {
    listActiveMovies()
    if (MovieAPI.numberOfActiveMovies() > 0) {
        val Movie = MovieAPI.findMovie(readNextInt("\nEnter the id of the Movie: "))
        if (Movie != null) {
            if (Movie.isMovieArchived) {
                println("Movie is NOT Active, it is Archived")
            } else {
                return Movie // chosen Movie is active
            }
        } else {
            println("Movie id is not valid")
        }
    }
    return null // selected Movie is not active
}

private fun addDescriptionToMovie() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        if (Movie.addDescription(Description(descriptionContents = readNextLine("\t description Contents: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateDescriptionContentsInMovie() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val description: Description? = askUserToChooseDescription(Movie)
        if (description != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (Movie.update(description.descriptionId, Description(descriptionContents = newContents))) {
                println("description contents updated")
            } else {
                println("description contents NOT updated")
            }
        } else {
            println("Invalid description Id")
        }
    }
}

private fun askUserToChooseDescription(Movie: Movie): Description? {
    if (Movie.numberOfDescriptions() > 0) {
        print(Movie.listDescriptions())
        return Movie.findOne(readNextInt("\nEnter the id of the description: "))
    } else {
        println("No descriptions for chosen Movie")
        return null
    }
}

fun deleteAndescription() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val description: Description? = askUserToChooseDescription(Movie)
        if (description != null) {
            val isDeleted = Movie.delete(description.descriptionId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markdescriptionStatus() {
    val Movie: Movie? = askUserToChooseActiveMovie()
    if (Movie != null) {
        val description: Description? = askUserToChooseDescription(Movie)
        if (description != null) {
            var changeStatus = 'X'
            if (description.isDescriptionComplete) {
                changeStatus = readNextChar("The description is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    description.isDescriptionComplete = false
            } else {
                changeStatus = readNextChar("The description is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    description.isDescriptionComplete = true
            }
        }
    }
}

fun searchdescriptions() {
    val searchContents = readNextLine("Enter the description contents to search by: ")
    val searchResults = MovieAPI.searchDescriptionByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No descriptions found")
    } else {
        println(searchResults)
    }
}

fun listToDoDescriptions() {
    if (MovieAPI.numberOfToDoDescriptions() > 0) {
        println("Total TODO descriptions: ${MovieAPI.numberOfToDoDescriptions()}")
    }
    println(MovieAPI.listToDoDescriptions())
}
