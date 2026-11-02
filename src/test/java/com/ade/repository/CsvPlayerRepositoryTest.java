package com.ade.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.ade.model.Player;
import com.ade.model.PlayerStats;

public class CsvPlayerRepositoryTest {
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void savesLoadsAndExportsRosterAsPlayerText() throws IOException {
		Path projectDirectory = temporaryFolder.getRoot().toPath();
		Path databasePath = projectDirectory.resolve("storage").resolve("PlayerRosterDB.csv");
		CsvPlayerRepository repository = new CsvPlayerRepository(databasePath.toString());
		Player player = new Player(23, "James, \"King\"", 81,
				new PlayerStats(80, 83, 97, 84, 74, 74, 57, 46, 87, 84, 88, 89));

		repository.savePlayers(List.of(player));

		List<Player> loadedPlayers = repository.loadPlayers();
		assertEquals(1, loadedPlayers.size());
		assertEquals(player.getName(), loadedPlayers.get(0).getName());
		assertEquals(player.getPlayerNumber(), loadedPlayers.get(0).getPlayerNumber());
		assertEquals(player.getStats().getVertical(), loadedPlayers.get(0).getStats().getVertical());

		repository.exportPlayers(loadedPlayers);
		Path exportPath = projectDirectory.resolve("output").resolve("PlayerRosterExport.txt");
		assertTrue(Files.exists(exportPath));
		assertEquals(player.toString(), Files.readString(exportPath, StandardCharsets.UTF_8));
		assertTrue(Files.readString(databasePath, StandardCharsets.UTF_8)
				.startsWith("playerNumber,name,height,threePointShot,"));
	}

	@Test
	public void returnsEmptyRosterWhenDatabaseDoesNotExist() {
		Path databasePath = temporaryFolder.getRoot().toPath().resolve("missing.csv");

		assertTrue(new CsvPlayerRepository(databasePath.toString()).loadPlayers().isEmpty());
	}
}