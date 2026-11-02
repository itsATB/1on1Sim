package com.ade.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ade.model.Player;
import com.ade.repository.PlayerRepository;

public class PlayerService {
	private final PlayerRepository playerRepository;
	private final List<Player> players;

	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = Objects.requireNonNull(playerRepository, "Player repository cannot be null.");
		this.players = new ArrayList<>(Objects.requireNonNull(
				playerRepository.loadPlayers(), "Player repository returned a null list."));
	}

	public List<Player> getAllPlayers() {
		return new ArrayList<>(players);
	}

	public Player getPlayerByNumber(String playerNumber) {
		int number = parsePlayerNumber(playerNumber);

		return players.stream()
				.filter(player -> player.getPlayerNumber() == number)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Player number was not found: " + number));
	}

	public void addPlayer(Player player) {
		validatePlayer(player);
		if (players.stream().anyMatch(existing -> existing.getPlayerNumber() == player.getPlayerNumber())) {
			throw new IllegalArgumentException("A player with this number already exists.");
		}

		player.setName(player.getName().trim());
		players.add(player);
	}

	public void updatePlayer(String playerNumber, Player updatedPlayer) {
		int index = findPlayerIndex(playerNumber);
		validatePlayer(updatedPlayer);

		for (int currentIndex = 0; currentIndex < players.size(); currentIndex++) {
			if (currentIndex != index
					&& players.get(currentIndex).getPlayerNumber() == updatedPlayer.getPlayerNumber()) {
				throw new IllegalArgumentException("A player with this number already exists.");
			}
		}

		updatedPlayer.setName(updatedPlayer.getName().trim());
		players.set(index, updatedPlayer);
	}

	public void deletePlayer(String playerNumber) {
		players.remove(findPlayerIndex(playerNumber));
	}

	public void saveChanges() {
		playerRepository.savePlayers(new ArrayList<>(players));
	}

	public void exportPlayers() {
		playerRepository.exportPlayers(new ArrayList<>(players));
	}

	private int findPlayerIndex(String playerNumber) {
		int number = parsePlayerNumber(playerNumber);

		for (int index = 0; index < players.size(); index++) {
			if (players.get(index).getPlayerNumber() == number) {
				return index;
			}
		}
		throw new IllegalArgumentException("Player number was not found: " + number);
	}

	private static int parsePlayerNumber(String playerNumber) {
		if (playerNumber == null || playerNumber.isBlank()) {
			throw new IllegalArgumentException("Player number cannot be blank.");
		}

		try {
			return Integer.parseInt(playerNumber.trim());
		} catch (NumberFormatException exception) {
			throw new IllegalArgumentException("Player number must be an integer.", exception);
		}
	}

	public static void validatePlayer(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("Player cannot be null.");
		}
		if (player.getName() == null || player.getName().isBlank()) {
			throw new IllegalArgumentException("Player name cannot be blank.");
		}
		if (player.getPlayerNumber() <= 0) {
			throw new IllegalArgumentException("Player number must be positive.");
		}
		if (player.getHeight() <= 0) {
			throw new IllegalArgumentException("Player height must be positive.");
		}
		player.validateStats();
	}
}
