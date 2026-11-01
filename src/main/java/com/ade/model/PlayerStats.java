package com.ade.model;

public class PlayerStats {
    // Offense
    private int threePointShot;
    private int midRangeShot;
    private int layup;
    private int ballHandling;
    // Defense
    private int insideDefense;
    private int outsideDefense;
    private int block;
    private int steal;
    // Physical
    private int speed;
    private int agility;
    private int stamina;
    private int vertical;

    public PlayerStats() {
    }

    public PlayerStats(
            int threePointShot,
            int midRangeShot,
            int layup,
            int ballHandling,
            int insideDefense,
            int outsideDefense,
            int block,
            int steal,
            int speed,
            int agility,
            int stamina,
            int vertical) {
        this.threePointShot = threePointShot;
        this.midRangeShot = midRangeShot;
        this.layup = layup;
        this.ballHandling = ballHandling;
        this.insideDefense = insideDefense;
        this.outsideDefense = outsideDefense;
        this.block = block;
        this.steal = steal;
        this.speed = speed;
        this.agility = agility;
        this.stamina = stamina;
        this.vertical = vertical;
    }

    public int getThreePointShot() { return threePointShot; }
    public void setThreePointShot(int threePointShot) { this.threePointShot = threePointShot; }

    public int getMidRangeShot() { return midRangeShot; }
    public void setMidRangeShot(int midRangeShot) { this.midRangeShot = midRangeShot; }

    public int getLayup() { return layup; }
    public void setLayup(int layup) { this.layup = layup; }

    public int getBallHandling() { return ballHandling; }
    public void setBallHandling(int ballHandling) { this.ballHandling = ballHandling; }

    public int getInsideDefense() { return insideDefense; }
    public void setInsideDefense(int insideDefense) { this.insideDefense = insideDefense; }

    public int getOutsideDefense() { return outsideDefense; }
    public void setOutsideDefense(int outsideDefense) { this.outsideDefense = outsideDefense; }

    public int getBlock() { return block; }
    public void setBlock(int block) { this.block = block; }

    public int getSteal() { return steal; }
    public void setSteal(int steal) { this.steal = steal; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }

    public int getStamina() { return stamina; }
    public void setStamina(int stamina) { this.stamina = stamina; }

    public int getVertical() { return vertical; }
    public void setVertical(int vertical) { this.vertical = vertical; }

    public boolean isValidStats() {
        return isValidRating(threePointShot)
                && isValidRating(midRangeShot)
                && isValidRating(layup)
                && isValidRating(ballHandling)
                && isValidRating(insideDefense)
                && isValidRating(outsideDefense)
                && isValidRating(block)
                && isValidRating(steal)
                && isValidRating(speed)
                && isValidRating(agility)
                && isValidRating(stamina)
                && isValidRating(vertical);
    }

    private static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 99;
    }
}
