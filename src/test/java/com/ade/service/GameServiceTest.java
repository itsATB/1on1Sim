package com.ade.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.ade.model.MatchResult;
import com.ade.model.Player;
import com.ade.model.PlayerStats;

public class GameServiceTest {
	@Test
	public void higherRatedPlayerWinsAndSummaryIncludesMatchDetails() {
		Player highRatedPlayer = createPlayer(1, "High Rated", 84, 99);
		Player lowRatedPlayer = createPlayer(2, "Low Rated", 68, 1);
		GameService gameService = new GameService(new Random(5));

		MatchResult result = gameService.simulateGame(highRatedPlayer, lowRatedPlayer);

		assertEquals(highRatedPlayer, result.getWinner());
		assertEquals(3, result.getPlayer1CategoryWins());
		assertEquals(0, result.getPlayer2CategoryWins());
		assertNotNull(result.getSummary());
		assertTrue(result.getSummary().contains("High Rated vs. Player 2: Low Rated"));
		assertTrue(result.getSummary().contains("FINAL RESULT:"));
	}

	@Test
	public void identicalPlayersWithNoVarianceTie() {
		Player player1 = createPlayer(1, "Player One", 72, 50);
		Player player2 = createPlayer(2, "Player Two", 72, 50);
		Random noVariance = new Random() {
			@Override
			public double nextDouble(double origin, double bound) {
				return 0.0;
			}
		};

		MatchResult result = new GameService(noVariance).simulateGame(player1, player2);

		assertTrue(result.isTie());
		assertEquals(0, result.getPlayer1CategoryWins());
		assertEquals(0, result.getPlayer2CategoryWins());
		assertTrue(result.getSummary().contains("Winner: Tie"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsMissingPlayers() {
		GameService gameService = new GameService(new Random(1));
		Player player = createPlayer(1, "Player", 72, 50);

		gameService.simulateGame(player, null);
	}

	private static Player createPlayer(int number, String name, int height, int rating) {
		return new Player(number, name, height,
				new PlayerStats(rating, rating, rating, rating, rating, rating,
						rating, rating, rating, rating, rating, rating));
	}
}