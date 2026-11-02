package com.ade.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PlayerTest {
    @Test
    public void storesPlayerDetails() {
        Player player = new Player(23, "Example Player", 78, new PlayerStats());

        assertEquals(23, player.getPlayerNumber());
        assertEquals("Example Player", player.getName());
        assertEquals(78, player.getHeight());
    }

    @Test
    public void hasStatsByDefaultAndAllowsReplacingThem() {
        Player player = new Player();
        PlayerStats stats = new PlayerStats();

        assertNotNull(player.getStats());
        player.setStats(stats);
        assertSame(stats, player.getStats());
    }

    @Test
    public void validatesValidStats() {
        Player player = new Player();
        player.setStats(new PlayerStats(50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50));

        player.validateStats();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsWhenStatsAreInvalid() {
        new Player().validateStats();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullStats() {
        new Player().setStats(null);
    }
}
