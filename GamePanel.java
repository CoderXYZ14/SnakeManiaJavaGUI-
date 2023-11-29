import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 240;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Image backgroundImage;

    JButton restartButton;
    int highScore = 0;
    Image foodImage;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        backgroundImage = new ImageIcon("D:/Code/SnakeGame/bg.jpg").getImage();
        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        foodImage = new ImageIcon("D:/Code/SnakeGame/food.png").getImage();
        foodImage = foodImage.getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
        setLayout(new BorderLayout());
        add(restartButton, BorderLayout.SOUTH);
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);

        // Set initial position of the snake's head
        x[0] = playAreaX + playAreaSize / 2;
        y[0] = playAreaY + playAreaSize / 2;

        timer.start();
    }
    int playAreaSize = Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) - 150;
    int playAreaX = 70;  // Adjust the left position
    int playAreaY = 70;  // Adjust the top position
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw black border for the playing area
        int borderWidth = 5;
        g.setColor(Color.black);
        g.fillRect(playAreaX - borderWidth, playAreaY - borderWidth, playAreaSize + 2 * borderWidth, playAreaSize + 2 * borderWidth);

        // Draw playing area
        g.setColor(new Color(170, 236, 170));
        g.fillRect(playAreaX, playAreaY, playAreaSize, playAreaSize);

        // Draw snake head
        g.setColor(new Color(240, 124, 124));
        g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

        // Draw snake body
        g.setColor(new Color(124, 0, 124));
        for (int i = 1; i < bodyParts; i++) {
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }

        // Draw food image
        g.drawImage(foodImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);


        // Draw score and high score
        g.setColor(Color.black);
        g.setFont(new Font("New Tegomin", Font.BOLD, 20));
        g.drawString("Score: " + applesEaten + "  High Score: " + highScore, 20, 30);

        if (!running) {
            gameOver(g);
        }
    }




    public void draw(Graphics g) {

        if(running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i< bodyParts;i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = playAreaX + random.nextInt(playAreaSize / UNIT_SIZE) * UNIT_SIZE;
        appleY = playAreaY + random.nextInt(playAreaSize / UNIT_SIZE) * UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for (int i = 0; i < bodyParts; i++) {
            if (i != 0 && x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Check if head touches the border
        if (x[0] <playAreaX || x[0] >= playAreaX + playAreaSize ||
                y[0] < playAreaY || y[0] >= playAreaY + playAreaSize) {
            running = false;
        }


        if (!running) {
            timer.stop();
        }


    }
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());

        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        restartButton.setVisible(true);
        restartButton.setFocusPainted(false);
        restartButton.setFont(new Font("Ink Free", Font.BOLD, 20));
        restartButton.setBackground(new Color(255, 0, 0));
        restartButton.setForeground(Color.white);

        // Align the button text to the center
        restartButton.setHorizontalAlignment(SwingConstants.CENTER);
    }
    public void restartGame() {
        running = false;
        bodyParts = 1;
        applesEaten = 0;
        direction = 'R';
        newApple();
        x[0] = playAreaX + playAreaSize / 2;
        y[0] = playAreaY + playAreaSize / 2;
        startGame();
        restartButton.setVisible(false);  // Hide the restart button
        requestFocusInWindow();  // Request focus for the GamePanel
        repaint();
    }
    public void updateHighScore() {
        if (applesEaten > highScore) {
            highScore = applesEaten;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
            updateHighScore();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}