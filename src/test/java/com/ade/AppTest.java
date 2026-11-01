package com.ade;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;
import org.junit.Test;

public class AppTest {
    @Test
    public void menuDispatchesFeaturesAndRejectsInvalidChoices() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        java.io.InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;

        try {
            System.setIn(new ByteArrayInputStream(
                    "not a number\n4\n1\n2\n3\n0\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            App.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String result = output.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Welcome to the 1-on-1 Basketball Game Simulator!"));
        assertTrue(result.contains("Invalid choice. Please enter an integer between 0 and 3."));
        assertTrue(result.contains("Player roster management is not implemented yet."));
        assertTrue(result.contains("Game simulation is not implemented yet."));
        assertTrue(result.contains("Player roster export is not implemented yet."));
        assertTrue(result.contains("Exiting program. Goodbye!"));
        assertTrue(result.indexOf("Exiting program. Goodbye!")
                > result.indexOf("Player roster export is not implemented yet."));
    }
}
