package com.ade.model;

public class Player {
    private String name;
    private int height;
    private int playerNumber;
    private PlayerStats stats = new PlayerStats();

    public Player() {}

    public Player(int playerNumber, String name, int height, PlayerStats stats) {
        this.playerNumber = playerNumber;
        this.name = name;
        this.height = height;
        setStats(stats);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getPlayerNumber() { return playerNumber; }
    public void setPlayerNumber(int playerNumber) { this.playerNumber = playerNumber; }

    public PlayerStats getStats() { return stats; }
    public void setStats(PlayerStats stats) {
        if (stats == null) {
            throw new IllegalArgumentException("Player stats cannot be null.");
        }
        this.stats = stats;
    }

    public void validateStats() {
        if (!stats.isValidStats()) {
            throw new IllegalStateException("Player stats must all be between 1 and 99.");
        }
    }
}
