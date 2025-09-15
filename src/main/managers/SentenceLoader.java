package main.managers;

import java.io.*;
import java.util.*;

public class SentenceLoader {
    private List<String> sentences;
    private String filename;

    public SentenceLoader(String filename) {
        this.filename = filename;
        this.sentences = new ArrayList<>();
        loadSentences();
    }

    private void loadSentences() {
        try {
            // Try multiple approaches to find the file
            InputStream input = null;
            
            // Try 1: Direct file path
            File file = new File("src/main/resources/" + filename);
            if (file.exists()) {
                input = new FileInputStream(file);
            }
            
            // Try 2: Class loader
            if (input == null) {
                input = getClass().getClassLoader().getResourceAsStream(filename);
            }
            
            // Try 3: Absolute class path
            if (input == null) {
                input = getClass().getResourceAsStream("/" + filename);
            }

            if (input == null) {
                throw new FileNotFoundException("Could not find resource: " + filename);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        sentences.add(line);
                    }
                }
            }

            if (sentences.isEmpty()) {
                throw new IOException("No sentences found in file: " + filename);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading sentences from " + filename + ": " + e.getMessage());
        }
    }

    public String getRandomSentence() {
        if (sentences.isEmpty()) {
            throw new IllegalStateException("No sentences available!");
        }
        Random rand = new Random();
        return sentences.get(rand.nextInt(sentences.size()));
    }

    public int getSentenceCount() {
        return sentences.size();
    }
}