package main.utils;
import main.managers.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameOverFrame extends JFrame {
    private Player player;
    private Difficulty difficulty;
    private HighScoreManager hsm;

    public GameOverFrame(Player player, Difficulty difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.hsm = new HighScoreManager();
        
        // Save the score
        hsm.saveScore(player.getName(), player.getScore());
        
        initializeComponents();
        setupLayout();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Game Over");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Game Over!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.RED);
        titlePanel.add(titleLabel);

        // Player stats panel
        JPanel playerStatsPanel = createPlayerStatsPanel();

        // High scores panel
        JPanel highScoresPanel = createHighScoresPanel();

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton tryAgainButton = new JButton("Try Again");
        tryAgainButton.setPreferredSize(new Dimension(120, 40));
        tryAgainButton.addActionListener(e -> {
            new StartMenu();
            dispose();
        });
        
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(80, 40));
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(tryAgainButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(exitButton);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(playerStatsPanel, BorderLayout.NORTH);
        contentPanel.add(highScoresPanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createPlayerStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Your Performance"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Player name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Player:"), gbc);
        gbc.gridx = 1;
        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(nameLabel, gbc);

        // Score
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Final Score:"), gbc);
        gbc.gridx = 1;
        JLabel scoreLabel = new JLabel(String.valueOf(player.getScore()));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        scoreLabel.setForeground(Color.BLUE);
        panel.add(scoreLabel, gbc);

        // Rounds completed
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Rounds Completed:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(player.getRound() - 1)), gbc);

        // Difficulty
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(difficulty.name().toLowerCase()), gbc);

        // Rank
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Your Rank:"), gbc);
        gbc.gridx = 1;
        int rank = hsm.getPlayerRank(player.getName(), player.getScore());
        JLabel rankLabel = new JLabel("#" + rank);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Color code the rank
        if (rank == 1) {
            rankLabel.setForeground(Color.RED);
        } else if (rank <= 3) {
            rankLabel.setForeground(Color.ORANGE);
        } else if (rank <= 5) {
            rankLabel.setForeground(Color.BLUE);
        }
        panel.add(rankLabel, gbc);

        // High score indicator
        if (hsm.isNewHighScore(player.getName(), player.getScore())) {
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            JLabel newHighScoreLabel = new JLabel("🎉 NEW HIGH SCORE! 🎉");
            newHighScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            newHighScoreLabel.setForeground(Color.RED);
            newHighScoreLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(newHighScoreLabel, gbc);
        }

        return panel;
    }

    private JPanel createHighScoresPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("High Scores"));

        // Create table data
        List<Map.Entry<String, Integer>> topScores = hsm.getTopScores(10);
        
        String[] columnNames = {"Rank", "Player", "Score"};
        Object[][] data = new Object[Math.min(10, topScores.size())][3];
        
        for (int i = 0; i < Math.min(10, topScores.size()); i++) {
            Map.Entry<String, Integer> entry = topScores.get(i);
            data[i][0] = "#" + (i + 1);
            data[i][1] = entry.getKey();
            data[i][2] = entry.getValue();
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false); // Make it non-editable
        table.getTableHeader().setReorderingAllowed(false);
        
        // Highlight current player's row
         table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;  // Add serialVersionUID
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (row < topScores.size() && topScores.get(row).getKey().equals(player.getName())) {
                    c.setBackground(Color.YELLOW);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));  // Changed to use c.setFont
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));  // Changed to use c.setFont
                }
                return c;
            }
        });

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}