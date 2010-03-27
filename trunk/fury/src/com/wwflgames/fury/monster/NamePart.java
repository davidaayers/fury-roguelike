package com.wwflgames.fury.monster;

public class NamePart {

    private String part;
    private int minPoints;
    private int maxPoints;

    public NamePart(String part, int minPoints, int maxPoints) {
        this.part = part;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public static NamePart newNamePart(String part, int minPoints, int maxPoints) {
        return new NamePart(part, minPoints, maxPoints);
    }

    public String getPart() {
        return part;
    }

    public boolean validFor(int points) {
        return points >= minPoints && points <= maxPoints;
    }
}

