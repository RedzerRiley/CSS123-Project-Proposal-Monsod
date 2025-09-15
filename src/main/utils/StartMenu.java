package main.utils;
import main.managers.Player;
import main.managers.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JFrame {
    private JTextField nameField;
    private JRadioButton easyButton, mediumButton, hardButton;
    private ButtonGroup difficultyGroup;
    private JButton startButton;

    public StartMenu() {
        setTitle("Typing Game - Start Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    private void initializeComponents() {
        // Name input
        nameField = new JTextField(20);
        nameField.setText("Enter your name");
        nameField.setForeground(Color.GRAY);

        // Difficulty radio buttons
        easyButton = new JRadioButton("Easy (45 sec, easy text)", true);
        mediumButton = new JRadioButton("Medium (30 sec, medium text)");
        hardButton = new JRadioButton("Hard (20 sec, hard text)");

        difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(mediumButton);
        difficultyGroup.add(hardButton);

        // Start button
        startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 40));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Typing Speed Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Name input section
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Player Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        // Difficulty section
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Difficulty:"), gbc);

        JPanel difficultyPanel = new JPanel(new GridLayout(3, 1));
        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(difficultyPanel, gbc);

        // Start button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(startButton, gbc);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Name field focus events
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Enter your name")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Enter your name");
                    nameField.setForeground(Color.GRAY);
                }
            }
        });

        // Start button action
        startButton.addActionListener(e -> startGame());
        
        // Enter key on name field
        nameField.addActionListener(e -> startGame());
    }

    private void startGame() {
        String playerName = nameField.getText().trim();
        
        // Error handling for empty name
        if (playerName.isEmpty() || playerName.equals("Enter your name")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid player name!", 
                "Invalid Name", 
                JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        // Get selected difficulty
        Difficulty difficulty;
        if (easyButton.isSelected()) {
            difficulty = Difficulty.EASY;
        } else if (mediumButton.isSelected()) {
            difficulty = Difficulty.MEDIUM;
        } else {
            difficulty = Difficulty.HARD;
        }

        // Create player and start game
        Player player = new Player(playerName);
        new GamePanel(player, difficulty);
        dispose();
    }
}