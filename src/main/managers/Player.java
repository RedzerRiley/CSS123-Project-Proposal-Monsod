package main.managers;

public class Player {
    private String name;
    private int score;
    private int lives;
    private int round;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.lives = 3;
        this.round = 1;
    }

    public String getName() { 
        return name; 
    }
    
    public int getScore() { 
        return score; 
    }
    
    public int getLives() { 
        return lives; 
    }
    
    public int getRound() { 
        return round; 
    }

    public void addScore(int points) { 
        this.score += points; 
    }
    
    public void loseLife() { 
        if (lives > 0) lives--; 
    }
    
    public boolean isAlive() { 
        return lives > 0; 
    }
    
    public void nextRound() {
        this.round++;
    }
    
    public void resetRound() {
        this.round = 1;
    }
}