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
        // Use absolute path from resources
        String resourcePath = "/main/resources/" + filename;
        InputStream input = getClass().getResourceAsStream(resourcePath);
        
        if (input == null) {
            // Try without main/resources prefix
            input = getClass().getResourceAsStream("/" + filename);
        }
        
        if (input == null) {
            System.err.println("Could not find resource: " + filename);
            addDefaultSentences();
            return;
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
    } catch (IOException e) {
        System.err.println("Error reading file: " + filename + "\n" + e.getMessage());
        addDefaultSentences();
    }
    
    if (sentences.isEmpty()) {
        addDefaultSentences();
    }
}


    private void addDefaultSentences() {
        if (filename.contains("easy")) {
            sentences.addAll(Arrays.asList(
                "The cat sat on the mat.",
                "Java is fun to learn.",
                "I love coding games.",
                "The sun is bright today.",
                "Dogs are loyal friends."
            ));
        } else if (filename.contains("medium")) {
            sentences.addAll(Arrays.asList(
                "Programming requires patience and practice.",
                "The quick brown fox jumps over the lazy dog.",
                "Software development is both art and science.",
                "Debugging is twice as hard as writing code.",
                "Code is read more often than it is written."
            ));
        } else {
            sentences.addAll(Arrays.asList(
                "Object-oriented programming paradigms facilitate modular design.",
                "Algorithmic complexity analysis determines computational efficiency.",
                "Polymorphism enables dynamic method resolution at runtime.",
                "Encapsulation provides data abstraction and information hiding.",
                "Inheritance promotes code reusability and hierarchical relationships."
            ));
        }
    }

    public String getRandomSentence() {
        if (sentences.isEmpty()) {
            return "No sentences available!";
        }
        Random rand = new Random();
        return sentences.get(rand.nextInt(sentences.size()));
    }

    public int getSentenceCount() {
        return sentences.size();
    }
}