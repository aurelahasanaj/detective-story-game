package com.detective.game.model;

import java.util.List;

public class Suspect {
    private final String id;
    private final String name;
    private final String role;
    private final List<Statement> statements;

    public Suspect(String id, String name, String role, List<Statement> statements) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.statements = statements;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public List<Statement> getStatements() { return statements; }

    @Override
    public String toString() { return name + " — " + role; }
}
