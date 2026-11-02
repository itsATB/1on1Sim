package com.ade.repository;

import java.util.List;

import com.ade.model.Player;

public interface PlayerRepository {
	List<Player> loadPlayers();

	void savePlayers(List<Player> players);

	void exportPlayers(List<Player> players);
}
