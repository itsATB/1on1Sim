package com.ade.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PlayerStatsTest {
    @Test
    public void storesAllPlayerRatings() {
        PlayerStats stats = new PlayerStats(90, 80, 70, 60, 50, 40, 30, 20, 10, 11, 12, 13);

        assertEquals(90, stats.getThreePointShot());
        assertEquals(80, stats.getMidRangeShot());
        assertEquals(70, stats.getLayup());
        assertEquals(60, stats.getBallHandling());
        assertEquals(50, stats.getInsideDefense());
        assertEquals(40, stats.getOutsideDefense());
        assertEquals(30, stats.getBlock());
        assertEquals(20, stats.getSteal());
        assertEquals(10, stats.getSpeed());
        assertEquals(11, stats.getAgility());
        assertEquals(12, stats.getStamina());
        assertEquals(13, stats.getVertical());
    }

    @Test
    public void validatesAllRatingsAreBetweenOneAndNinetyNine() {
        PlayerStats stats = new PlayerStats(1, 99, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50);
        assertTrue(stats.isValidStats());

        stats.setThreePointShot(0);
        assertFalse(stats.isValidStats());
        stats.setThreePointShot(1);
        stats.setVertical(100);
        assertFalse(stats.isValidStats());
    }

}
