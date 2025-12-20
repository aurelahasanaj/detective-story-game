package com.detective.game.model;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final String id;
    private final String name;
    private final String description;
    private final String imagePath;
    private final List<Evidence> evidence;
    private int evidenceIndex = 0;

    public Location(String id, String name, String description, String imagePath, List<Evidence> evidence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.evidence = new ArrayList<>(evidence);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }

    public boolean hasMoreEvidence() {
        return evidenceIndex < evidence.size();
    }

    public Evidence takeNextEvidence() {
        if (!hasMoreEvidence()) return null;
        return evidence.get(evidenceIndex++);
    }

    public void resetEvidence() { evidenceIndex = 0; }

    @Override
    public String toString() { return name; }
}
