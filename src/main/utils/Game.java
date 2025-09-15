package main.utils;
import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}