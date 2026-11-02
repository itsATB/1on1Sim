package com.ade.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ade.model.Player;
import com.ade.model.PlayerStats;

public class CsvPlayerRepository implements PlayerRepository {
	private static final String CSV_HEADER = "playerNumber,name,height,threePointShot,midRangeShot,layup,"
			+ "ballHandling,insideDefense,outsideDefense,block,steal,speed,agility,stamina,vertical";
	private static final int CSV_COLUMN_COUNT = 15;

	private final String filePath;

	public CsvPlayerRepository(String filePath) {
		if (filePath == null || filePath.isBlank()) {
			throw new IllegalArgumentException("CSV file path cannot be blank.");
		}
		this.filePath = filePath;
	}

	@Override
	public List<Player> loadPlayers() {
		Path databasePath = Path.of(filePath);
		if (!Files.exists(databasePath)) {
			return new ArrayList<>();
		}

		try {
			String csv = Files.readString(databasePath, StandardCharsets.UTF_8);
			List<List<String>> rows = parseCsv(csv);
			if (rows.isEmpty()) {
				return new ArrayList<>();
			}
			if (rows.get(0).size() != CSV_COLUMN_COUNT
					|| !String.join(",", rows.get(0)).equals(CSV_HEADER)) {
				throw new IllegalStateException("Player roster CSV has an invalid header.");
			}

			List<Player> players = new ArrayList<>();
			for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
				List<String> fields = rows.get(rowIndex);
				if (fields.size() == 1 && fields.get(0).isBlank()) {
					continue;
				}
				if (fields.size() != CSV_COLUMN_COUNT) {
					throw new IllegalStateException("Expected 15 columns on CSV row " + (rowIndex + 1) + ".");
				}
				players.add(parsePlayer(fields, rowIndex + 1));
			}
			return players;
		} catch (IOException exception) {
			throw new IllegalStateException("Could not load players from " + databasePath + ".", exception);
		}
	}

	@Override
	public void savePlayers(List<Player> players) {
		Objects.requireNonNull(players, "Players cannot be null.");
		String csv = serializePlayers(players);
		Path databasePath = Path.of(filePath);
		writeFile(databasePath, csv);
	}

	@Override
	public void exportPlayers(List<Player> players) {
		Objects.requireNonNull(players, "Players cannot be null.");
		StringBuilder export = new StringBuilder();
		for (Player player : players) {
			if (player == null) {
				throw new IllegalArgumentException("Roster cannot contain null players.");
			}
			if (export.length() > 0) {
				export.append(System.lineSeparator());
			}
			export.append(player.toString());
		}
		writeFile(getExportPath(Path.of(filePath)), export.toString());
	}

	private static Player parsePlayer(List<String> fields, int rowNumber) {
		try {
			PlayerStats stats = new PlayerStats(
					Integer.parseInt(fields.get(3)), Integer.parseInt(fields.get(4)),
					Integer.parseInt(fields.get(5)), Integer.parseInt(fields.get(6)),
					Integer.parseInt(fields.get(7)), Integer.parseInt(fields.get(8)),
					Integer.parseInt(fields.get(9)), Integer.parseInt(fields.get(10)),
					Integer.parseInt(fields.get(11)), Integer.parseInt(fields.get(12)),
					Integer.parseInt(fields.get(13)), Integer.parseInt(fields.get(14)));
			Player player = new Player(
					Integer.parseInt(fields.get(0)), fields.get(1), Integer.parseInt(fields.get(2)), stats);
			if (player.getPlayerNumber() <= 0 || player.getName().isBlank() || player.getHeight() <= 0
					|| !stats.isValidStats()) {
				throw new IllegalStateException("Invalid player data on CSV row " + rowNumber + ".");
			}
			return player;
		} catch (NumberFormatException exception) {
			throw new IllegalStateException("Invalid number on CSV row " + rowNumber + ".", exception);
		}
	}

	private static String serializePlayers(List<Player> players) {
		StringBuilder csv = new StringBuilder(CSV_HEADER).append(System.lineSeparator());
		for (Player player : players) {
			if (player == null || player.getStats() == null || player.getName() == null) {
				throw new IllegalArgumentException("Roster cannot contain null or incomplete players.");
			}
			PlayerStats stats = player.getStats();
			appendRow(csv,
					Integer.toString(player.getPlayerNumber()), player.getName(), Integer.toString(player.getHeight()),
					Integer.toString(stats.getThreePointShot()), Integer.toString(stats.getMidRangeShot()),
					Integer.toString(stats.getLayup()), Integer.toString(stats.getBallHandling()),
					Integer.toString(stats.getInsideDefense()), Integer.toString(stats.getOutsideDefense()),
					Integer.toString(stats.getBlock()), Integer.toString(stats.getSteal()),
					Integer.toString(stats.getSpeed()), Integer.toString(stats.getAgility()),
					Integer.toString(stats.getStamina()), Integer.toString(stats.getVertical()));
		}
		return csv.toString();
	}

	private static void appendRow(StringBuilder csv, String... values) {
		for (int index = 0; index < values.length; index++) {
			if (index > 0) {
				csv.append(',');
			}
			csv.append(escapeCsv(values[index]));
		}
		csv.append(System.lineSeparator());
	}

	private static String escapeCsv(String value) {
		if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}

	private static List<List<String>> parseCsv(String csv) {
		List<List<String>> rows = new ArrayList<>();
		List<String> fields = new ArrayList<>();
		StringBuilder field = new StringBuilder();
		boolean inQuotes = false;
		boolean quoteClosed = false;

		for (int index = 0; index < csv.length(); index++) {
			char character = csv.charAt(index);
			if (inQuotes) {
				if (character == '"' && index + 1 < csv.length() && csv.charAt(index + 1) == '"') {
					field.append('"');
					index++;
				} else if (character == '"') {
					inQuotes = false;
					quoteClosed = true;
				} else {
					field.append(character);
				}
			} else if (character == ',' ) {
				fields.add(field.toString());
				field.setLength(0);
				quoteClosed = false;
			} else if (character == '\n' || character == '\r') {
				fields.add(field.toString());
				rows.add(fields);
				fields = new ArrayList<>();
				field.setLength(0);
				quoteClosed = false;
				if (character == '\r' && index + 1 < csv.length() && csv.charAt(index + 1) == '\n') {
					index++;
				}
			} else if (character == '"' && field.length() == 0 && !quoteClosed) {
				inQuotes = true;
			} else {
				if (quoteClosed) {
					throw new IllegalStateException("Unexpected content after a quoted CSV field.");
				}
				field.append(character);
			}
		}

		if (inQuotes) {
			throw new IllegalStateException("Unclosed quoted field in player roster CSV.");
		}
		if (field.length() > 0 || !fields.isEmpty()) {
			fields.add(field.toString());
			rows.add(fields);
		}
		return rows;
	}

	private static Path getExportPath(Path databasePath) {
		Path absoluteDatabasePath = databasePath.toAbsolutePath().normalize();
		Path databaseDirectory = absoluteDatabasePath.getParent();
		if (databaseDirectory != null && databaseDirectory.getFileName() != null
				&& databaseDirectory.getFileName().toString().equalsIgnoreCase("storage")
				&& databaseDirectory.getParent() != null) {
			return databaseDirectory.getParent().resolve("output").resolve("PlayerRosterExport.txt");
		}
		return databaseDirectory == null
				? Path.of("PlayerRosterExport.txt").toAbsolutePath()
				: databaseDirectory.resolve("PlayerRosterExport.txt");
	}

	private static void writeFile(Path path, String content) {
		try {
			Path parent = path.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			Files.writeString(path, content, StandardCharsets.UTF_8);
		} catch (IOException exception) {
			throw new IllegalStateException("Could not write player roster to " + path + ".", exception);
		}
	}
}
