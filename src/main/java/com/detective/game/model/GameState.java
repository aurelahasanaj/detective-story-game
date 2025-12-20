package com.detective.game.model;

import java.util.*;

public class GameState {
    public static final int MAX_ACTIONS = 6;

    private int actionsLeft = MAX_ACTIONS;
    private final Set<String> evidenceIds = new LinkedHashSet<>();
    private final Map<String, Integer> suspicion = new HashMap<>();
    private final Set<String> contradictionsFound = new HashSet<>();
    private final Set<String> interviewed = new HashSet<>();
    private int lastProximityScore = 0;

    public GameState(GameData data) {
        for (Suspect s : data.getSuspects()) suspicion.put(s.getId(), 0);
    }

    public int getActionsLeft() { return actionsLeft; }
    public Set<String> getEvidenceIds() { return evidenceIds; }

    public boolean spendAction() {
        if (actionsLeft <= 0) return false;
        actionsLeft--;
        return true;
    }

    public void addEvidence(Evidence e) {
        evidenceIds.add(e.getId());
    }

    public int getSuspicion(String suspectId) {
        return suspicion.getOrDefault(suspectId, 0);
    }

    public void addSuspicion(String suspectId, int delta) {
        suspicion.put(suspectId, getSuspicion(suspectId) + delta);
    }

    public int bestSuspicion() {
        int best = 0;
        for (int v : suspicion.values()) best = Math.max(best, v);
        return best;
    }

    public void markContradictionFound(String statementId) {
        contradictionsFound.add(statementId);
    }

    public boolean isContradictionFound(String statementId) {
        return contradictionsFound.contains(statementId);
    }

    public int contradictionsCount() { return contradictionsFound.size(); }

    public void markInterviewed(String suspectId) { interviewed.add(suspectId); }
    public boolean isInterviewed(String suspectId) { return interviewed.contains(suspectId); }

    public void setLastProximityScore(int v) { lastProximityScore = v; }
    public int getLastProximityScore() { return lastProximityScore; }

    public void reset(GameData data) {
        actionsLeft = MAX_ACTIONS;
        evidenceIds.clear();
        contradictionsFound.clear();
        interviewed.clear();
        suspicion.clear();
        for (Suspect s : data.getSuspects()) suspicion.put(s.getId(), 0);
        lastProximityScore = 0;
        for (Location l : data.getLocations()) l.resetEvidence();
    }
}
