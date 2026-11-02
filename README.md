# 1on1Sim

Java project incorporating the 4 principles of OOP. An application that stores information about NBA players and can simulate 1-on-1 games between them, determining the winner based on player stats 

## Core Features

- **Player roster:** `PlayerService` handles in-memory player management and validation. `CsvPlayerRepository` loads and saves the roster in `storage/PlayerRosterDB.csv`.
- **Game simulation:** `GameService` compares player attributes with random game-day variance and returns a `MatchResult` summary.
- **Roster export:** The roster can be exported as readable player details to `output/PlayerRosterExport.txt`.

## Run

Requires Java 22 or later and Maven. From the `1on1Sim` project directory, build the project with :

```text
mvn clean package
```

And run with: 

```text
java -jar target/1on1Sim.jar
```

