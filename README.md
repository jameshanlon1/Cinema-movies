

# Kotlin Cinema-Movies App

This is a cinema-movies app built with Kotlin. It has CRUD and search functions, and a mutable set for actors. The app uses a Boolean to indicate whether a movie is currently being shown in the cinema or not. Movie data is persisted using JSON.

## Movie Data

Each movie has the following data:

- Title: the title of the movie.
- Category: the category of the movie.
- Rating: the rating of the movie.

## Actor Data

Each actor has the following data:

- Name: the name of the actor.
- Oscar Winner: a Boolean indicating whether the actor has won an Oscar or not.

## Functionality

The app provides the following functionality:

- Create, read, update, and delete movies.
- Search for movies by title, category, or rating.
- Add and remove actors from movies.
- Sort actors by whether they are Oscar winners or not.
- Indicate whether a movie is currently being shown in the cinema or not.

## Persistence

The app uses JSON to persist movie data. When a movie is created, updated, or deleted, the data is written to a JSON file. When the app starts up, it reads the data from the JSON file.

## Getting Started

To use the app, simply clone the repository and run it in an IDE that supports Kotlin.


## License

This project is licensed to James Hanlon
