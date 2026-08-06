package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighScoreManager {
    private static final int MAX_SCORES = 5;

    public static void saveScore(String initials, int score) {
        List<ScoreEntry> entries = loadEntries();
        entries.add(new ScoreEntry(initials, score));
        entries.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        if (entries.size() > MAX_SCORES) {
            entries = entries.subList(0, MAX_SCORES);
        }

        saveEntries(entries);
    }

    public static List<String> getScoreLines() {
        List<String> lines = new ArrayList<>();
        List<ScoreEntry> entries = loadEntries();
        for (int i = 0; i < entries.size(); i++) {
            ScoreEntry entry = entries.get(i);
            lines.add(String.format("%d.  %s  -  %d", i + 1, entry.initials, entry.score));
        }
        return lines;
    }

    private static List<ScoreEntry> loadEntries() {
        List<ScoreEntry> entries = new ArrayList<>();
        File scoresFile = resolveScoresFile();

        if (!scoresFile.exists()) {
            return entries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scoresFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] parts = trimmed.split("\\s+", 2);
                if (parts.length == 2) {
                    String initials = parts[0].trim().toUpperCase();
                    int score = Integer.parseInt(parts[1].trim());
                    entries.add(new ScoreEntry(initials, score));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR al cargar scores: " + e.getMessage());
        }

        entries.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        return entries;
    }

    private static void saveEntries(List<ScoreEntry> entries) {
        File scoresFile = resolveScoresFile();
        File parent = scoresFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoresFile))) {
            for (ScoreEntry entry : entries) {
                writer.write(entry.initials + " " + entry.score);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR al guardar scores: " + e.getMessage());
        }
    }

    private static File resolveScoresFile() {
        File dir = new File("top scores");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, "highscores.txt");
    }

    private static class ScoreEntry {
        private final String initials;
        private final int score;

        private ScoreEntry(String initials, int score) {
            this.initials = initials;
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }
}
