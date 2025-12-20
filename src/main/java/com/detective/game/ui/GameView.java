package com.detective.game.ui;

import com.detective.game.model.*;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.stream.Collectors;

public class GameView extends StackPane {
    private final GameData data;
    private final GameState state;
    private final Sfx sfx;

    private final Label hud = new Label();
    private final ProgressBar actionBar = new ProgressBar(1.0);

    private final Label proximityBadge = new Label("COLD");
    private final ProgressBar radarBar = new ProgressBar(0.0);

    private final TextArea log = new TextArea();

    private final ListView<Location> locationsView = new ListView<>();
    private final ListView<Suspect> suspectsView = new ListView<>();
    private final ListView<Evidence> evidenceView = new ListView<>();

    private final TextArea details = new TextArea();
    private final ImageView bigPreview = new ImageView();

    private final StackPane overlay = new StackPane();

    public GameView(GameData data, GameState state, Sfx sfx) {
        this.data = data;
        this.state = state;
        this.sfx = sfx;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setTop(buildHeader());
        root.setCenter(buildTabs());
        root.setBottom(buildLog());

        getChildren().add(root);
        setupOverlay();

        refreshUI();
        append("Case opened: " + data.getCaseTitle());
        append("Tip: Locations -> SEARCH LOCATION to collect evidence.");
        append("Then: Suspects -> Interview -> Present Evidence.");
    }

    private VBox buildHeader() {
        Label title = new Label(data.getCaseTitle());
        title.getStyleClass().add("title-label");

        TextArea intro = new TextArea(data.getCaseIntro());
        intro.setEditable(false);
        intro.setWrapText(true);
        intro.setFocusTraversable(false);
        intro.setPrefRowCount(4);

        hud.getStyleClass().add("hud");
        actionBar.setPrefWidth(340);
        actionBar.setMaxWidth(Double.MAX_VALUE);

        ImageView radarIcon = new ImageView();
        try {
            radarIcon.setImage(new Image(getClass().getResourceAsStream("/assets/ui/radar.png")));
            radarIcon.setFitWidth(28);
            radarIcon.setFitHeight(28);
            radarIcon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        radarBar.setPrefWidth(240);
        radarBar.setMaxWidth(Double.MAX_VALUE);

        proximityBadge.getStyleClass().add("badge-cold");

        Button reset = new Button("New Game");
        reset.getStyleClass().add("btn-ghost");
        reset.setOnAction(e -> {
            sfx.click();
            state.reset(data);
            locationsView.getSelectionModel().selectFirst();
            suspectsView.getSelectionModel().selectFirst();
            log.clear();
            append("New game started.");
            append("Tip: Locations -> SEARCH LOCATION.");
            refreshUI();
            refreshLocationPanel(locationsView.getSelectionModel().getSelectedItem());
        });

        Button help = new Button("Hint");
        help.getStyleClass().add("btn-ghost");
        help.setOnAction(e -> {
            sfx.click();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Hint");
            a.setHeaderText("How to progress fast");
            a.setContentText(
                    "1) SEARCH 3-4 locations to collect evidence.\n" +
                    "2) Interview suspects to see statements.\n" +
                    "3) Present evidence to expose contradictions.\n" +
                    "4) Accuse when you have 3+ evidence and 1+ contradiction."
            );
            a.showAndWait();
        });

        HBox topRow = new HBox(12, title, new Region(), help, reset);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        HBox radarRow = new HBox(10, radarIcon, new Label("Radar:"), radarBar, proximityBadge);
        radarRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, topRow, intro, hud, actionBar, radarRow);
        card.getStyleClass().add("header-card");
        return card;
    }

    private TabPane buildTabs() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(tabLocations());
        tabs.getTabs().add(tabSuspects());
        tabs.getTabs().add(tabCaseBoard());
        tabs.getTabs().add(tabAccuse());
        return tabs;
    }

    private Tab tabLocations() {
        locationsView.setItems(FXCollections.observableArrayList(data.getLocations()));
        locationsView.getSelectionModel().selectFirst();

        locationsView.setCellFactory(lv -> new CardCell<>(
                Location::getName,
                Location::getDescription,
                Location::getImagePath
        ));

        locationsView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                searchLocation();
            }

    });

        Button search = new Button("SEARCH LOCATION (costs 1 action)");
        search.getStyleClass().add("btn-primary");
        search.setMaxWidth(Double.MAX_VALUE);
        search.setOnAction(e -> { sfx.click(); searchLocation(); });

        details.setEditable(false);
        details.setWrapText(true);

        bigPreview.setFitWidth(560);
        bigPreview.setFitHeight(320);
        bigPreview.setPreserveRatio(true);
        bigPreview.setSmooth(true);

        locationsView.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> refreshLocationPanel(n));

        Label locLabel = new Label("Locations");

        VBox left = new VBox();
        left.setSpacing(10);
        left.getChildren().addAll(locLabel, locationsView, search);

// kjo është KRITIKE
        VBox.setVgrow(locationsView, Priority.ALWAYS);

// mos e shtrydh
        left.setPrefWidth(480);
        left.setMinWidth(480);



        VBox right = new VBox(10, new Label("Preview"), bigPreview, new Label("Details"), details);
        HBox root = new HBox(14, left, right);
        root.setPadding(new Insets(10));
        HBox.setHgrow(right, Priority.ALWAYS);

        Tab tab = new Tab("📍 Locations", root);
        refreshLocationPanel(locationsView.getSelectionModel().getSelectedItem());
        return tab;
    }

    private void refreshLocationPanel(Location loc) {
        if (loc == null) return;
        try { bigPreview.setImage(new Image(getClass().getResourceAsStream(loc.getImagePath()))); }
        catch (Exception ignored) { bigPreview.setImage(null); }

        String msg = loc.getName() + "\n\n" + loc.getDescription() + "\n\n";
        msg += loc.hasMoreEvidence() ? "This location still has evidence." : "No more evidence here.";
        msg += "\n\nUse: SEARCH LOCATION (or double-click).";
        details.setText(msg);
        refreshHeaderOnly();
    }

    private Tab tabSuspects() {
        suspectsView.setItems(FXCollections.observableArrayList(data.getSuspects()));
        suspectsView.getSelectionModel().selectFirst();

        suspectsView.setCellFactory(lv -> new SuspectCell(state::getSuspicion));

        evidenceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        evidenceView.setCellFactory(lv -> new EvidenceCell());

        Button interview = new Button("Interview (costs 1 action)");
        interview.getStyleClass().add("btn-primary");
        interview.setOnAction(e -> { sfx.click(); interviewSuspect(); });

        Button present = new Button("Present Selected Evidence (FREE)");
        present.getStyleClass().add("btn-ghost");
        present.setOnAction(e -> { sfx.click(); presentEvidence(); });

        TextArea suspectFile = new TextArea();
        suspectFile.setEditable(false);
        suspectFile.setWrapText(true);

        suspectsView.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> suspectFile.setText(buildSuspectFile(n)));
        suspectFile.setText(buildSuspectFile(suspectsView.getSelectionModel().getSelectedItem()));

        VBox col1 = new VBox(10, new Label("Suspects"), suspectsView, interview);
        col1.setPrefWidth(420);

        VBox col2 = new VBox(10, new Label("Your Evidence"), evidenceView, present);
        col2.setPrefWidth(420);

        VBox col3 = new VBox(10, new Label("Suspect File"), suspectFile);
        HBox root = new HBox(14, col1, col2, col3);
        root.setPadding(new Insets(10));
        HBox.setHgrow(col3, Priority.ALWAYS);

        return new Tab("🧑 Suspects", root);
    }

    private Tab tabCaseBoard() {
        TextArea board = new TextArea();
        board.setEditable(false);
        board.setWrapText(true);

        Button refresh = new Button("Refresh Board");
        refresh.getStyleClass().add("btn-ghost");
        refresh.setOnAction(e -> { sfx.click(); board.setText(buildBoard()); });

        VBox root = new VBox(10, refresh, board);
        root.setPadding(new Insets(10));
        board.setText(buildBoard());

        return new Tab("🧾 Case Board", root);
    }

    private Tab tabAccuse() {
        ComboBox<Suspect> pick = new ComboBox<>(FXCollections.observableArrayList(data.getSuspects()));
        pick.getSelectionModel().selectFirst();

        Label tip = new Label("Recommended: 3+ evidence and 1+ contradiction before accusing.");
        tip.getStyleClass().add("subtle");

        Button accuse = new Button("Make Accusation");
        accuse.getStyleClass().add("btn-primary");
        accuse.setOnAction(e -> { sfx.click(); accuse(pick.getSelectionModel().getSelectedItem()); });

        VBox root = new VBox(12, new Label("Choose suspect:"), pick, tip, accuse);
        root.setPadding(new Insets(20));

        return new Tab("⚖ Accuse", root);
    }

    private VBox buildLog() {
        log.setEditable(false);
        log.setWrapText(true);
        log.setPrefRowCount(7);
        VBox box = new VBox(8, new Label("Investigation Log"), log);
        box.setPadding(new Insets(8, 8, 0, 8));
        return box;
    }

    private void refreshUI() {
        refreshHeaderOnly();

        var evidence = state.getEvidenceIds().stream()
                .map(id -> data.getEvidenceById().get(id))
                .filter(x -> x != null)
                .sorted(Comparator.comparing(Evidence::getId))
                .collect(Collectors.toList());

        evidenceView.setItems(FXCollections.observableArrayList(evidence));
        suspectsView.refresh();
    }

    private void refreshHeaderOnly() {
        hud.setText("Actions: " + state.getActionsLeft()
                + " | Evidence: " + state.getEvidenceIds().size()
                + " | Contradictions: " + state.contradictionsCount());

        actionBar.setProgress(state.getActionsLeft() / (double) GameState.MAX_ACTIONS);
        updateProximity();
    }

    private void updateProximity() {
        int score = state.getEvidenceIds().size() + state.contradictionsCount()*3 + state.bestSuspicion();
        state.setLastProximityScore(score);

        double p = Math.min(1.0, score / 14.0);
        radarBar.setProgress(p);

        String label;
        String css;
        if (p < 0.25) { label = "COLD"; css = "badge-cold"; }
        else if (p < 0.55) { label = "WARMING"; css = "badge-neutral"; }
        else if (p < 0.80) { label = "HOT"; css = "badge-hot"; }
        else { label = "VERY CLOSE"; css = "badge-hot"; }

        proximityBadge.getStyleClass().removeAll("badge-cold","badge-neutral","badge-hot");
        proximityBadge.getStyleClass().add(css);
        proximityBadge.setText(label);
    }

    private void searchLocation() {
        Location loc = locationsView.getSelectionModel().getSelectedItem();
        if (loc == null) return;

        if (!state.spendAction()) {
            append("No actions left. Go to Accuse tab.");
            refreshUI();
            return;
        }

        Evidence found = loc.takeNextEvidence();
        if (found == null) {
            append("Searched " + loc.getName() + " - nothing new.");
        } else {
            state.addEvidence(found);
            append("FOUND EVIDENCE: " + found.getTitle() + " @ " + loc.getName());
            sfx.found();
            flash(log);
        }
        refreshUI();
        refreshLocationPanel(loc);
    }

    private void interviewSuspect() {
        Suspect s = suspectsView.getSelectionModel().getSelectedItem();
        if (s == null) return;

        if (!state.spendAction()) {
            append("No actions left. Go to Accuse tab.");
            refreshUI();
            return;
        }

        state.markInterviewed(s.getId());
        append("Interviewed " + s.getName() + ".");
        append("Statements:");
        for (Statement st : s.getStatements()) append(" - " + st.getText());
        flash(log);
        refreshUI();
    }

    private void presentEvidence() {
        Suspect s = suspectsView.getSelectionModel().getSelectedItem();
        Evidence e = evidenceView.getSelectionModel().getSelectedItem();
        if (s == null || e == null) { append("Pick a suspect AND an evidence item first."); return; }

        boolean hit = false;
        for (Statement st : s.getStatements()) {
            String needed = data.getContradictionMap().get(st.getId());
            if (needed != null && needed.equals(e.getId())) {
                hit = true;
                if (!state.isContradictionFound(st.getId())) {
                    state.markContradictionFound(st.getId());
                    state.addSuspicion(s.getId(), 3);
                    append("CONTRADICTION! " + s.getName() + " lied:");
                    append("\"" + st.getText() + "\"");
                    append("Evidence: " + e.getTitle());
                    sfx.found();
                    flash(log);
                } else {
                    append("That contradiction was already found.");
                }
            }
        }

        if (!hit) {
            append("No contradiction with that evidence. Suspicion increases slightly.");
            state.addSuspicion(s.getId(), 1);
        }
        refreshUI();
    }

    private String buildSuspectFile(Suspect s) {
        if (s == null) return "";
        return "Name: " + s.getName() + "\n"
                + "Role: " + s.getRole() + "\n"
                + "Interviewed: " + (state.isInterviewed(s.getId()) ? "YES" : "NO") + "\n"
                + "Suspicion: " + state.getSuspicion(s.getId()) + "\n\n"
                + "Statements:\n"
                + s.getStatements().stream().map(st -> " - " + st.getText()).collect(Collectors.joining("\n"));
    }

    private String buildBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("CASE BOARD\n\n");
        sb.append("Radar: ").append(proximityBadge.getText()).append("\n");
        sb.append("Evidence collected (").append(state.getEvidenceIds().size()).append("):\n");
        for (String id : state.getEvidenceIds()) {
            Evidence ev = data.getEvidenceById().get(id);
            if (ev != null) sb.append(" - ").append(ev.getTitle()).append("\n   ").append(ev.getDescription()).append("\n\n");
        }
        sb.append("Suspects:\n");
        for (Suspect s : data.getSuspects()) {
            sb.append(" - ").append(s.getName()).append(" (").append(s.getRole()).append(")")
              .append(" | suspicion=").append(state.getSuspicion(s.getId()))
              .append(" | interviewed=").append(state.isInterviewed(s.getId()) ? "yes" : "no")
              .append("\n");
        }
        sb.append("\nContradictions found: ").append(state.contradictionsCount()).append("\n");
        sb.append("Tip: Present evidence to suspects to expose lies.\n");
        return sb.toString();
    }

    private void accuse(Suspect accused) {
        if (accused == null) return;

        int evidenceCount = state.getEvidenceIds().size();
        int contradictions = state.contradictionsCount();
        boolean correct = accused.getId().equals(data.getCulpritSuspectId());

        int score = evidenceCount*10 + contradictions*25 + state.bestSuspicion()*5 + state.getActionsLeft()*3;
        String rank = rank(score);

        if (evidenceCount < 3 || contradictions < 1) {
            sfx.fail();
            showEnding(false, "INSUFFICIENT EVIDENCE",
                    "You accused too early.\nCollect more evidence and expose contradictions first.",
                    "Rank: " + rank + " (score " + score + ")");
            append("Accusation made too early: " + accused.getName());
            return;
        }

        if (correct) {
            sfx.win();
            showEnding(true, "IMPOSTOR FOUND",
                    "You exposed the real culprit: " + accused.getName() + "\nCase solved.",
                    "Rank: " + rank + " (score " + score + ")");
            append("IMPOSTOR FOUND: " + accused.getName());
        } else {
            sfx.fail();
            showEnding(false, "WRONG ACCUSATION",
                    "You accused " + accused.getName() + ".\nThe real impostor escaped.",
                    "Rank: " + rank + " (score " + score + ")");
            append("Wrong accusation: " + accused.getName());
        }
    }

    private String rank(int score) {
        if (score >= 220) return "Master Detective";
        if (score >= 170) return "Detective";
        if (score >= 120) return "Investigator";
        return "Rookie";
    }

    private void flash(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(220), node);
        ft.setFromValue(0.65);
        ft.setToValue(1.0);
        ft.play();
    }

    private void append(String s) { log.appendText(s + "\n"); }

    private void setupOverlay() {
        overlay.getStyleClass().add("overlay");
        overlay.setVisible(false);
        overlay.setManaged(false);
        overlay.setPickOnBounds(true);
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        getChildren().add(overlay);
    }

    private void showEnding(boolean win, String header, String body, String footer) {
        overlay.getChildren().clear();

        VBox card = new VBox(10);
        card.getStyleClass().add("overlay-card");
        card.setMaxWidth(720);

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(win ? "/assets/ui/trophy.png" : "/assets/ui/impostor.png")));
            icon.setFitWidth(72);
            icon.setFitHeight(72);
            icon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label h = new Label(header);
        h.getStyleClass().add("big-result");

        Label b = new Label(body);
        b.getStyleClass().add("subtle");
        b.setStyle("-fx-font-size: 16px;");
        b.setWrapText(true);

        Label f = new Label(footer);
        f.getStyleClass().add("subtle");

        Button close = new Button("Continue (New Game)");
        close.getStyleClass().add("btn-primary");
        close.setOnAction(e -> {
            sfx.click();
            overlay.setVisible(false);
            overlay.setManaged(false);
            state.reset(data);
            locationsView.getSelectionModel().selectFirst();
            suspectsView.getSelectionModel().selectFirst();
            log.clear();
            append("New game started.");
            append("Tip: Locations -> SEARCH LOCATION.");
            refreshUI();
        });

        HBox top = new HBox(14, icon, h);
        top.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(top, b, f, close);
        overlay.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        overlay.setVisible(true);
        overlay.setManaged(true);
        flash(card);
    }
}
