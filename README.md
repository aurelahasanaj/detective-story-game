# Detective Story Game

Detective Story Game is a JavaFX investigation game where the player acts as a detective and tries to solve the case of a missing manuscript. The game is built with Java, JavaFX and Maven, and it uses an interactive UI with locations, suspects, evidence collection and accusation logic.

## Project Overview

The player starts with a limited number of actions and must investigate different locations, interview suspects, collect evidence and expose contradictions before making a final accusation.

The case is called:

```text
The Library Case: The Missing Manuscript
```

The goal is to identify the correct culprit by using evidence and logical reasoning.

## Features

* JavaFX desktop user interface
* Intro screen with game instructions
* Locations tab for searching evidence
* Suspects tab for interviewing suspects
* Evidence collection system
* Present evidence to expose contradictions
* Case board for tracking progress
* Accusation screen
* Action limit system
* Suspicion score system
* Custom UI styling with CSS
* Image assets for locations and UI elements
* Maven project structure

## Technologies Used

* Java 17
* JavaFX 21
* Maven
* Object-Oriented Programming
* CSS for JavaFX styling

## Project Structure

```text
detective-story-game
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── detective
│       │           └── game
│       │               ├── DetectiveStoryGameApp.java
│       │               ├── model
│       │               │   ├── Evidence.java
│       │               │   ├── GameData.java
│       │               │   ├── GameState.java
│       │               │   ├── Location.java
│       │               │   ├── Statement.java
│       │               │   └── Suspect.java
│       │               └── ui
│       │                   ├── CardCell.java
│       │                   ├── EvidenceCell.java
│       │                   ├── GameView.java
│       │                   ├── IntroView.java
│       │                   ├── Sfx.java
│       │                   └── SuspectCell.java
│       └── resources
│           ├── assets
│           └── styles.css
├── pom.xml
└── README.md
```

## How to Run

### Run with IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click **File > Open**.
3. Select the project folder.
4. Wait for Maven to load dependencies.
5. Open the Maven tool window.
6. Run:

```text
Plugins > javafx > javafx:run
```

### Run with Terminal

Make sure Java and Maven are installed.

Then run:

```bash
mvn clean javafx:run
```

If Maven is not recognized, install Maven or run the project through IntelliJ IDEA using the Maven panel.

## How to Play

1. Start the investigation.
2. You begin with 6 actions.
3. Go to the **Locations** tab.
4. Select a location and click **Search Location** to collect evidence.
5. Go to the **Suspects** tab.
6. Interview suspects to read their statements.
7. Select a suspect and evidence, then click **Present Selected Evidence** to expose contradictions.
8. Use the **Case Board** to review progress.
9. When ready, go to the **Accuse** tab and make your final accusation.

## Game Rules

* Searching a location costs 1 action.
* Interviewing a suspect costs 1 action.
* Presenting evidence is free.
* The player should collect enough evidence before accusing.
* The game rewards logical investigation and correct use of evidence.

## Main Concepts Demonstrated

This project demonstrates:

* Java classes and objects
* Encapsulation through model classes
* JavaFX layout and UI components
* Event handling
* Game state management
* Lists, maps and sets
* Custom ListView cells
* CSS styling in JavaFX
* Maven dependency management

## What I Learned

While building this project, I practiced:

* structuring a JavaFX application;
* separating model and UI logic;
* managing state in a desktop application;
* using Maven for dependencies;
* creating interactive UI screens;
* working with images and CSS in JavaFX;
* applying object-oriented programming principles in a complete project.

## Summary

Detective Story Game is a complete JavaFX desktop game focused on investigation, evidence collection and decision-making. It combines Java OOP, JavaFX UI development and game logic into one practical project.
