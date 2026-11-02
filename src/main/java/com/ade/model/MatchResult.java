package com.ade.model;

// Data class to hold the result of a match between two players
public class MatchResult {
	private final Player winner;
	private final int player1CategoryWins;
	private final int player2CategoryWins;
	private final double player1TotalScore;
	private final double player2TotalScore;
	private final String summary;

	public MatchResult(
			Player winner,
			int player1CategoryWins,
			int player2CategoryWins,
			double player1TotalScore,
			double player2TotalScore,
			String summary) {
		this.winner = winner;
		this.player1CategoryWins = player1CategoryWins;
		this.player2CategoryWins = player2CategoryWins;
		this.player1TotalScore = player1TotalScore;
		this.player2TotalScore = player2TotalScore;
		this.summary = summary;
	}

	public Player getWinner() { return winner; }
 	public boolean isTie() { return winner == null; }
	public int getPlayer1CategoryWins() { return player1CategoryWins; }
	public int getPlayer2CategoryWins() { return player2CategoryWins; }
	public double getPlayer1TotalScore() { return player1TotalScore; }
	public double getPlayer2TotalScore() { return player2TotalScore; }
	public String getSummary() { return summary; }
}