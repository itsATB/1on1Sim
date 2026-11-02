package com.ade.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ade.model.Player;
import com.ade.model.PlayerStats;

public class PlayerServiceTest {
    @Test
    public void managesPlayersByNumber() {
        PlayerService service = new PlayerService();
        service.addPlayer(createPlayer(1, "First Player"));

        service.addPlayer(createPlayer(2, "Second Player"));
        assertEquals("Second Player", service.getPlayerByNumber("2").getName());

        service.updatePlayer("2", createPlayer(3, "Updated Player"));
        assertEquals("Updated Player", service.getAllPlayers().get(1).getName());
        assertEquals(3, service.getPlayerByNumber("3").getPlayerNumber());

        service.deletePlayer("1");
        service.saveChanges();

        assertEquals(1, service.getAllPlayers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsDuplicatePlayerNumbers() {
        PlayerService service = new PlayerService();
        service.addPlayer(createPlayer(1, "First Player"));

        service.addPlayer(createPlayer(1, "Another Player"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonNumericPlayerNumber() {
        new PlayerService().getPlayerByNumber("not-a-number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnknownPlayerNumber() {
        new PlayerService().deletePlayer("404");
    }

    private static Player createPlayer(int number, String name) {
        return new Player(number, name, 78,
                new PlayerStats(50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50));
    }
}