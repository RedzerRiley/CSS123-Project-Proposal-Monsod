package main.managers;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private String filename = "highscores.txt";

    public void saveScore(String name, int score) {
        Map<String, Integer> scores = loadScores();
        
        // Update score only if new score is higher, or add new player
        Integer currentScore = scores.get(name);
        if (currentScore == null || score > currentScore) {
            scores.put(name, score);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                pw.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }

    public Map<String, Integer> loadScores() {
        Map<String, Integer> scores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                        scores.put(parts[0], Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid score format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, return empty map
        }
        return scores;
    }

    public List<Map.Entry<String, Integer>> getTopScores(int limit) {
        Map<String, Integer> scores = loadScores();
        
        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(ArrayList::new, 
                        (list, entry) -> list.add(entry), 
                        (list1, list2) -> list1.addAll(list2));
    }

    public boolean isNewHighScore(String name, int score) {
        Map<String, Integer> scores = loadScores();
        Integer currentScore = scores.get(name);
        return currentScore == null || score > currentScore;
    }

    public int getPlayerRank(String name, int score) {
        List<Map.Entry<String, Integer>> topScores = getTopScores(Integer.MAX_VALUE);
        
        // Create a temporary entry for the current player
        boolean found = false;
        for (int i = 0; i < topScores.size(); i++) {
            if (topScores.get(i).getKey().equals(name)) {
                topScores.get(i).setValue(Math.max(score, topScores.get(i).getValue()));
                found = true;
                break;
            }
        }
        
        if (!found) {
            topScores.add(new AbstractMap.SimpleEntry<>(name, score));
        }
        
        // Re-sort
        topScores.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        // Find rank
        for (int i = 0; i < topScores.size(); i++) {
            if (topScores.get(i).getKey().equals(name)) {
                return i + 1;
            }
        }
        
        return topScores.size(); // Fallback
    }
}