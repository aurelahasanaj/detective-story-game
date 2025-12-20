package com.detective.game.model;

import java.util.*;

public class GameData {
    private final String caseTitle;
    private final String caseIntro;
    private final List<Location> locations;
    private final List<Suspect> suspects;

    // statementId -> evidenceId needed to contradict
    private final Map<String, String> contradictionMap;
    private final Map<String, Evidence> evidenceById;
    private final String culpritSuspectId;

    public GameData(String caseTitle,
                    String caseIntro,
                    List<Location> locations,
                    List<Suspect> suspects,
                    Map<String, String> contradictionMap,
                    Map<String, Evidence> evidenceById,
                    String culpritSuspectId) {
        this.caseTitle = caseTitle;
        this.caseIntro = caseIntro;
        this.locations = locations;
        this.suspects = suspects;
        this.contradictionMap = contradictionMap;
        this.evidenceById = evidenceById;
        this.culpritSuspectId = culpritSuspectId;
    }

    public String getCaseTitle() { return caseTitle; }
    public String getCaseIntro() { return caseIntro; }
    public List<Location> getLocations() { return locations; }
    public List<Suspect> getSuspects() { return suspects; }
    public Map<String, String> getContradictionMap() { return contradictionMap; }
    public Map<String, Evidence> getEvidenceById() { return evidenceById; }
    public String getCulpritSuspectId() { return culpritSuspectId; }

    public static GameData createDefaultCase() {
        // Evidence
        Evidence e1 = new Evidence("E1", "Coffee Receipt", "A receipt from Cafe Aurora, time-stamped during the theft window.");
        Evidence e2 = new Evidence("E2", "Keycard Log", "Security logs show a keycard used at the Rare Books Room door.");
        Evidence e3 = new Evidence("E3", "Ink Smudge", "Fresh ink smudge found near the manuscript display.");
        Evidence e4 = new Evidence("E4", "Maintenance Gloves", "Gloves with paper fibers found in the workshop.");
        Evidence e5 = new Evidence("E5", "Shift Note", "A shift swap note with a forged signature.");
        Evidence e6 = new Evidence("E6", "CCTV Snapshot", "A blurry snapshot of someone carrying a folder.");

        Map<String, Evidence> evidenceById = new HashMap<>();
        for (Evidence e : List.of(e1,e2,e3,e4,e5,e6)) evidenceById.put(e.getId(), e);

        // Locations (each has 1-2 evidence)
        Location l1 = new Location("L1", "Rare Books Room",
                "Locked cases, alarms, and a faint smell of coffee.",
                "/assets/locations/rare_books.png",
                List.of(e3, e2));
        Location l2 = new Location("L2", "Security Office",
                "Keycard logs, CCTV monitors, incident notes.",
                "/assets/locations/security.png",
                List.of(e6, e2));
        Location l3 = new Location("L3", "Cafe Aurora",
                "Barista notes, receipts, staff chatter.",
                "/assets/locations/cafe.png",
                List.of(e1));
        Location l4 = new Location("L4", "Telecom Records",
                "Phone logs and internal calls list.",
                "/assets/locations/telecom.png",
                List.of(e5));
        Location l5 = new Location("L5", "Maintenance Workshop",
                "Tools, gloves, spare keys, work orders.",
                "/assets/locations/workshop.png",
                List.of(e4));

        // Statements
        Statement s1 = new Statement("S1", "I never went to the cafe that day.");
        Statement s2 = new Statement("S2", "My keycard was with me all evening.");
        Statement s3 = new Statement("S3", "I wasn't near the Rare Books Room.");
        Statement s4 = new Statement("S4", "I didn't touch any ink or papers.");
        Statement s5 = new Statement("S5", "I didn't swap shifts with anyone.");

        // Suspects (culprit = curator)
        Suspect a = new Suspect("A", "Arta", "Archivist", List.of(s3, s5));
        Suspect b = new Suspect("B", "Blerim", "Security Officer", List.of(s2, s3));
        Suspect c = new Suspect("C", "Drita", "Cafe Barista", List.of(s1));
        Suspect d = new Suspect("D", "Luan", "Maintenance Tech", List.of(s4, s2));
        Suspect e = new Suspect("E", "Mira", "Curator", List.of(s1, s4, s5));

        List<Location> locations = new ArrayList<>(List.of(l1,l2,l3,l4,l5));
        List<Suspect> suspects = new ArrayList<>(List.of(a,b,c,d,e));

        // Contradictions: which evidence breaks which statement
        Map<String, String> contra = new HashMap<>();
        contra.put("S1", "E1"); // cafe denial contradicted by receipt
        contra.put("S2", "E2"); // keycard statement contradicted by logs
        contra.put("S4", "E3"); // ink/paper contradicted by ink smudge
        contra.put("S5", "E5"); // shift swap contradicted by forged note

        return new GameData(
                "The Library Case: The Missing Manuscript",
                "A rare manuscript vanished from the Rare Books Room.\n" +
                "You have 6 actions to investigate.\n" +
                "Search locations, interview suspects, and present evidence to expose contradictions.\n" +
                "When ready, accuse one suspect.",
                locations, suspects, contra, evidenceById,
                "E"
        );
    }
}
