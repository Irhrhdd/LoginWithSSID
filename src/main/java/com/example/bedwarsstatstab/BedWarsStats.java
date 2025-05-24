package com.example.bedwarsstatstab;

public class BedWarsStats {
    private final int stars;
    private final double fkdr;

    public BedWarsStats(int stars, double fkdr) {
        this.stars = stars;
        this.fkdr = fkdr;
    }

    public int getStars() {
        return stars;
    }

    public double getFkdr() {
        return fkdr;
    }
}
