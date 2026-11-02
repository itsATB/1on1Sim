package com.ade.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ade.model.Player;
import com.ade.model.PlayerStats;
import com.ade.repository.PlayerRepository;

public class PlayerServiceTest {
    @Test
    public void managesPlayersByNumber() {
        MemoryPlayerRepository repository = new MemoryPlayerRepository();
        repository.players.add(createPlayer(1, "First Player"));
        PlayerService service = new PlayerService(repository);

        service.addPlayer(createPlayer(2, "Second Player"));
        assertEquals("Second Player", service.getPlayerByNumber("2").getName());

        service.updatePlayer("2", createPlayer(3, "Updated Player"));
        assertEquals("Updated Player", service.getAllPlayers().get(1).getName());
        assertEquals(3, service.getPlayerByNumber("3").getPlayerNumber());

        service.deletePlayer("1");
        service.saveChanges();

        assertEquals(1, service.getAllPlayers().size());
        assertEquals(1, repository.savedPlayers.size());
        assertEquals("Updated Player", repository.savedPlayers.get(0).getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsDuplicatePlayerNumbers() {
        PlayerService service = new PlayerService(new MemoryPlayerRepository());
        service.addPlayer(createPlayer(1, "First Player"));

        service.addPlayer(createPlayer(1, "Another Player"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonNumericPlayerNumber() {
        new PlayerService(new MemoryPlayerRepository()).getPlayerByNumber("not-a-number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnknownPlayerNumber() {
        new PlayerService(new MemoryPlayerRepository()).deletePlayer("404");
    }

    private static Player createPlayer(int number, String name) {
        return new Player(number, name, 78,
                new PlayerStats(50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50));
    }

    private static final class MemoryPlayerRepository implements PlayerRepository {
        private final List<Player> players = new ArrayList<>();
        private List<Player> savedPlayers = new ArrayList<>();

        @Override
        public List<Player> loadPlayers() {
            return new ArrayList<>(players);
        }

        @Override
        public void savePlayers(List<Player> players) {
            savedPlayers = new ArrayList<>(players);
        }

        @Override
        public void exportPlayers(List<Player> players) {
        }
    }
}