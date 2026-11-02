package com.ade;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.ade.model.Player;
import com.ade.model.PlayerStats;
import com.ade.repository.CsvPlayerRepository;
import com.ade.service.PlayerService;

public class AppTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void menuLoadsRosterAndExportsPlayerText() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        java.io.InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        PlayerService originalPlayerService = App.playerService;
        Path projectDirectory = temporaryFolder.getRoot().toPath();
        Path databasePath = projectDirectory.resolve("storage").resolve("PlayerRosterDB.csv");
        CsvPlayerRepository repository = new CsvPlayerRepository(databasePath.toString());
        repository.savePlayers(List.of(
                createPlayer(23, "Lebron James"),
                createPlayer(30, "Stephen Curry")));
        App.playerService = new PlayerService(repository);

        try {
            System.setIn(new ByteArrayInputStream(
                ("not a number\n4\n1\n1\n2\n3, Test Player, 77\n"
                    + "50,50,50,50,50,50,50,50,50,50,50,50\n"
                    + "1\n0\n2\n23\n30\n3\n0\n")
                            .getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            App.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
            App.playerService = originalPlayerService;
        }

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Welcome to the 1-on-1 Basketball Game Simulator!"));
        assertTrue(result.contains("Invalid choice. Please enter an integer between 0 and 3."));
        assertTrue(result.contains("--- Player Roster Menu ---"));
        assertTrue(result.contains("1. View player roster"));
        assertTrue(result.contains("2. Add new player"));
        assertTrue(result.contains("3. Delete player"));
        assertTrue(result.contains("Lebron James"));
        assertTrue(result.contains("Stephen Curry"));
        assertTrue(result.contains("Player added successfully!"));
        assertTrue(result.contains("Test Player"));
        assertTrue(result.contains("--- Simulate Game ---"));
        assertTrue(result.contains("Enter Player 1's number:"));
        assertTrue(result.contains("Enter Player 2's number:"));
        assertTrue(result.contains("Player 1: Lebron James vs. Player 2: Stephen Curry"));
        assertTrue(result.contains("FINAL RESULT:"));
        assertTrue(result.contains("--- Exporting Player Roster ---"));
        assertTrue(result.contains("Exiting program. Goodbye!"));
        assertTrue(result.indexOf("Exiting program. Goodbye!")
            > result.indexOf("--- Exporting Player Roster ---"));

        String exportedRoster = Files.readString(
            projectDirectory.resolve("output").resolve("PlayerRosterExport.txt"), StandardCharsets.UTF_8);
        assertTrue(exportedRoster.contains("[#23"));
        assertTrue(exportedRoster.contains("[#30"));
        assertTrue(exportedRoster.contains("Test Player"));
        }

        private static Player createPlayer(int playerNumber, String name) {
        return new Player(playerNumber, name, 78,
            new PlayerStats(50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50));
    }
}
