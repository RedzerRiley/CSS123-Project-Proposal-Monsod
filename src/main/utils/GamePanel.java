package main.utils;
import main.managers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JFrame implements TimerManager.TimerCallback {
    private Player player;
    private SentenceLoader loader;
    private TimerManager timerManager;
    private Difficulty difficulty;
    
    // UI Components
    private JLabel roundLabel, scoreLabel, livesLabel, timerLabel;
    private JLabel sentenceLabel;
    private JTextField inputField;
    private JLabel feedbackLabel;
    private JPanel statsPanel;
    
    private String currentSentence;
    private boolean gameActive;

    public GamePanel(Player player, Difficulty difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.loader = new SentenceLoader(difficulty.getSentenceFile());
        this.timerManager = new TimerManager(difficulty.getInitialTime(), this);
        this.gameActive = true;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadNewSentence();
        timerManager.start();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Typing Speed Game - Playing");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Stats labels
        roundLabel = new JLabel("Round: " + player.getRound());
        scoreLabel = new JLabel("Score: " + player.getScore());
        livesLabel = new JLabel("Lives: " + player.getLives());
        timerLabel = new JLabel("Time: " + difficulty.getInitialTime());

        // Game components
        sentenceLabel = new JLabel();
        sentenceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sentenceLabel.setHorizontalAlignment(JLabel.CENTER);
        sentenceLabel.setBorder(BorderFactory.createTitledBorder("Type this sentence:"));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createTitledBorder("Your input:"));

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER);
        feedbackLabel.setBorder(BorderFactory.createTitledBorder("Feedback:"));

        // Style the timer based on time remaining
        updateTimerDisplay();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Stats panel (top)
        statsPanel = new JPanel(new GridLayout(1, 4, 10, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.add(roundLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(livesLabel);
        statsPanel.add(timerLabel);

        // Game panel (center)
        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        gamePanel.add(sentenceLabel, BorderLayout.NORTH);
        gamePanel.add(inputField, BorderLayout.CENTER);
        gamePanel.add(feedbackLabel, BorderLayout.SOUTH);

        // Instructions panel (bottom)
        JLabel instructionsLabel = new JLabel(
            "<html><center>Press ENTER to submit your answer<br>" +
            "Every correct answer: +10 points and +5 seconds</center></html>"
        );
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        add(statsPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(instructionsLabel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        inputField.addActionListener(e -> {
            if (gameActive) {
                checkInput();
            }
        });

        // Focus on input field when window opens
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                inputField.requestFocus();
            }
        });
    }

    private void loadNewSentence() {
        currentSentence = loader.getRandomSentence();
        sentenceLabel.setText("<html><center>" + currentSentence + "</center></html>");
        inputField.setText("");
        inputField.requestFocus();
        
        // Clear previous feedback after a short delay
        Timer clearFeedback = new Timer(3000, e -> {
            if (feedbackLabel.getText().contains("Correct") || 
                feedbackLabel.getText().contains("Wrong")) {
                feedbackLabel.setText(" ");
            }
        });
        clearFeedback.setRepeats(false);
        clearFeedback.start();
    }

    private void checkInput() {
        if (!gameActive) return;

        String typed = inputField.getText().trim();
        
        if (typed.equals(currentSentence)) {
            // Correct answer
            player.addScore(10);
            player.nextRound();
            timerManager.addTime(5);
            
            // Show positive feedback
            feedbackLabel.setText("Correct! +10 points and +5 seconds!");
            feedbackLabel.setForeground(Color.GREEN);
            
            // Update displays
            updateDisplays();
            
            // Check for speed increase every 5 rounds
            if ((player.getRound() - 1) % 5 == 0 && player.getRound() > 1) {
                timerManager.adjustSpeedForRound(player.getRound());
            
            }
            
            loadNewSentence();
            
        } else {
            // Wrong answer
            player.loseLife();
            feedbackLabel.setText("Wrong! Life lost. Try again!");
            feedbackLabel.setForeground(Color.RED);
            
            // Clear input field and refocus
            inputField.setText("");
            inputField.requestFocus();
            
            updateDisplays();
            
            // Check if game should end
            if (!player.isAlive()) {
                endGame();
            }
        }
    }

    private void updateDisplays() {
        roundLabel.setText("Round: " + player.getRound());
        scoreLabel.setText("Score: " + player.getScore());
        livesLabel.setText("Lives: " + player.getLives());
        
        // Update lives label color
        if (player.getLives() <= 1) {
            livesLabel.setForeground(Color.RED);
        } else if (player.getLives() <= 2) {
            livesLabel.setForeground(Color.ORANGE);
        } else {
            livesLabel.setForeground(Color.BLACK);
        }
    }

    private void updateTimerDisplay() {
        int time = timerManager.getTimeRemaining();
        timerLabel.setText("Time: " + time);
        
        // Color coding for timer
        if (time <= 5) {
            timerLabel.setForeground(Color.RED);
        } else if (time <= 10) {
            timerLabel.setForeground(Color.ORANGE);
        } else {
            timerLabel.setForeground(Color.BLACK);
        }
    }

    // TimerManager.TimerCallback implementation
    @Override
    public void onTimeUpdate(int timeRemaining) {
        SwingUtilities.invokeLater(() -> updateTimerDisplay());
    }

    @Override
    public void onTimeUp() {
        SwingUtilities.invokeLater(() -> {
            gameActive = false;
            feedbackLabel.setText("Time's up! Game Over!");
            feedbackLabel.setForeground(Color.RED);
            
            Timer delay = new Timer(2000, e -> endGame());
            delay.setRepeats(false);
            delay.start();
        });
    }

    private void endGame() {
        gameActive = false;
        timerManager.stop();
        new GameOverFrame(player, difficulty);
        dispose();
    }
}