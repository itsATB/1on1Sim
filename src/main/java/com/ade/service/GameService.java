package com.ade.service;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import com.ade.model.MatchResult;
import com.ade.model.Player;
import com.ade.model.PlayerStats;

public class GameService {
	private static final int MIN_HEIGHT_INCHES = 68;
	private static final int MAX_HEIGHT_INCHES = 84;
	private static final double MIN_VARIANCE = -5.0;
	private static final double MAX_VARIANCE = 5.0;

	private final Random random;

	public GameService() {
		this(new Random());
	}

	public GameService(Random random) {
		this.random = random;
	}

	public MatchResult simulateGame(Player player1, Player player2) {
		PlayerService.validatePlayer(player1);
		PlayerService.validatePlayer(player2);

		EffectiveScores scores1 = calculateEffectiveScores(player1);
		EffectiveScores scores2 = calculateEffectiveScores(player2);

		double player1OffensiveEdge = scores1.offense - scores2.defense;
		double player2OffensiveEdge = scores2.offense - scores1.defense;

		int player1CategoryWins = 0;
		int player2CategoryWins = 0;
		int comparison = compare(scores1.athleticism, scores2.athleticism);
		player1CategoryWins += comparison > 0 ? 1 : 0;
		player2CategoryWins += comparison < 0 ? 1 : 0;

		int offenseWinner = compare(player1OffensiveEdge, player2OffensiveEdge);
		player1CategoryWins += offenseWinner > 0 ? 1 : 0;
		player2CategoryWins += offenseWinner < 0 ? 1 : 0;

		int defenseWinner = compare(scores1.defense, scores2.defense);
		player1CategoryWins += defenseWinner > 0 ? 1 : 0;
		player2CategoryWins += defenseWinner < 0 ? 1 : 0;

		double player1Total = scores1.total();
		double player2Total = scores2.total();
		int overallComparison = Integer.compare(player1CategoryWins, player2CategoryWins);
		if (overallComparison == 0) {
			overallComparison = compare(player1Total, player2Total);
		}

		Player winner = overallComparison > 0 ? player1 : overallComparison < 0 ? player2 : null;
		String summary = createSummary(
				player1, player2, scores1, scores2,
				player1OffensiveEdge, player2OffensiveEdge,
				comparison, offenseWinner, defenseWinner,
				player1CategoryWins, player2CategoryWins, overallComparison);

		return new MatchResult(
				winner, player1CategoryWins, player2CategoryWins,
				player1Total, player2Total, summary);
	}

	private EffectiveScores calculateEffectiveScores(Player player) {
		PlayerStats stats = player.getStats();
		// The current model has no strength or passing fields, so stamina and agility are used as proxies.
		double normalizedHeight = normalizeHeight(player.getHeight());
		double shooting = average(
				stats.getThreePointShot(), stats.getMidRangeShot(), stats.getLayup());

		double athleticism = average(
				stats.getSpeed(), stats.getVertical(), stats.getStamina(), normalizedHeight);
		double offense = average(shooting, stats.getBallHandling(), stats.getAgility());
		double defense = average(
				stats.getInsideDefense(), stats.getOutsideDefense(), stats.getSteal(), stats.getBlock());

		return new EffectiveScores(
				athleticism + variance(),
				offense + variance(),
				defense + variance());
	}

	private double variance() {
		return random.nextDouble(MIN_VARIANCE, MAX_VARIANCE);
	}

	private static double normalizeHeight(int heightInches) {
		int boundedHeight = Math.max(MIN_HEIGHT_INCHES, Math.min(MAX_HEIGHT_INCHES, heightInches));
		return 1.0 + (boundedHeight - MIN_HEIGHT_INCHES) * 98.0
				/ (MAX_HEIGHT_INCHES - MIN_HEIGHT_INCHES);
	}

	private static double average(double... values) {
		double total = 0;
		for (double value : values) {
			total += value;
		}
		return total / values.length;
	}

	private static int compare(double first, double second) {
		return Double.compare(first, second);
	}


	private static String createSummary(
			Player player1,
			Player player2,
			EffectiveScores scores1,
			EffectiveScores scores2,
			double player1OffensiveEdge,
			double player2OffensiveEdge,
			int athleticismWinner,
			int offenseWinner,
			int defenseWinner,
			int player1CategoryWins,
			int player2CategoryWins,
			int overallComparison) {
		StringBuilder summary = new StringBuilder();
		summary.append("========================================\n")
				.append("       1-ON-1 MATCHUP SIMULATION\n")
				.append("========================================\n")
				.append("Player 1: ").append(player1.getName())
				.append(" vs. Player 2: ").append(player2.getName()).append("\n\n")
				.append("[1] ATHLETICISM BATTLE:\n")
				.append("    * ").append(player1.getName()).append(" (")
				.append(format(scores1.athleticism)).append(") vs. ")
				.append(player2.getName()).append(" (").append(format(scores2.athleticism)).append(")\n")
				.append("    * Winner: ").append(categoryWinner(athleticismWinner, player1, player2)).append("\n\n")
				.append("[2] OFFENSE BATTLE:\n")
				.append("    * ").append(player1.getName()).append(" Offense (")
				.append(format(scores1.offense)).append(") vs. ").append(player2.getName())
				.append(" Defense (").append(format(scores2.defense)).append(")\n")
				.append("    * ").append(player2.getName()).append(" Offense (")
				.append(format(scores2.offense)).append(") vs. ").append(player1.getName())
				.append(" Defense (").append(format(scores1.defense)).append(")\n")
				.append("    * Offensive edge: ").append(player1.getName()).append(" (")
				.append(format(player1OffensiveEdge)).append(") vs. ").append(player2.getName())
				.append(" (").append(format(player2OffensiveEdge)).append(")\n")
				.append("    * Winner: ").append(categoryWinner(offenseWinner, player1, player2)).append("\n\n")
				.append("[3] DEFENSE BATTLE:\n")
				.append("    * ").append(player1.getName()).append(" (")
				.append(format(scores1.defense)).append(") vs. ")
				.append(player2.getName()).append(" (").append(format(scores2.defense)).append(")\n")
				.append("    * Winner: ").append(categoryWinner(defenseWinner, player1, player2)).append("\n\n")
				.append("----------------------------------------\n")
				.append("FINAL RESULT:\n")
				.append("Winner: ").append(overallComparison == 0 ? "Tie" :
						overallComparison > 0 ? player1.getName() : player2.getName())
					.append(overallComparison == 0 ? " (total scores tied)" : " (won by category wins or total-score tie-break)")
					.append("\nMatch Score: ").append(player1.getName()).append(" ")
				.append(player1CategoryWins).append(" - ").append(player2CategoryWins).append(" ")
				.append(player2.getName()).append("\n")
				.append("Summary: ").append(overallSummary(
						player1, player2, player1CategoryWins, player2CategoryWins, overallComparison)).append("\n")
				.append("========================================");
		return summary.toString();
	}

	private static String categoryWinner(int comparison, Player player1, Player player2) {
		if (comparison == 0) {
			return "Tie";
		}
		return comparison > 0 ? player1.getName() : player2.getName();
	}

	private static String overallSummary(
			Player player1, Player player2,
			int player1Wins, int player2Wins, int overallComparison) {
		if (overallComparison == 0) {
			return "The players finished level across categories and total effective scores.";
		}
		if (player1Wins == player2Wins) {
			Player winner = overallComparison > 0 ? player1 : player2;
			return winner.getName() + " won on total effective score after the category count was tied.";
		}
		Player winner = overallComparison > 0 ? player1 : player2;
		int winningCategories = Math.max(player1Wins, player2Wins);
		return winner.getName() + " won " + winningCategories + " of the 3 categories.";
	}

	private static String format(double value) {
		return String.format(Locale.ROOT, "%.1f", value);
	}

	private static final class EffectiveScores {
		private final double athleticism;
		private final double offense;
		private final double defense;

		private EffectiveScores(double athleticism, double offense, double defense) {
			this.athleticism = athleticism;
			this.offense = offense;
			this.defense = defense;
		}

		private double total() {
			return athleticism + offense + defense;
		}
	}
}
