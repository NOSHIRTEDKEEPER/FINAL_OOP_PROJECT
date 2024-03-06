import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Main extends JPanel implements KeyListener {

    private int playerX = 50;
    private int playerY = 950;
    private boolean isJumping = false;
    private boolean isCrouching = false;
    private boolean isFalling = false;
    private int xVelocity = 0;
    private int yVelocity = 0;
    private final Hitbox playerBox = new Hitbox(playerX,playerY, 50, 50);
    private final Hitbox playerBoxShort = new Hitbox(playerX, playerY, 50, 49);
    private final Hitbox groundBox = new Hitbox(0,1000,2000,100);
    private final Hitbox platformBox = new Hitbox(250, 800, 500, 10);
    private final Hitbox PlatformBox2 = new Hitbox(1100,450,250,10);
    private final Hitbox PlatformBox3 = new Hitbox(900,650,250,10);

    public Main() {
        JFrame frame = new JFrame("Platformer");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);
        StartGameLoop();

    }

    private void StartGameLoop(){
        //Keeps it updating even if you aren't moving
        new Thread(() -> {
            while(true){
                repaint();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update(){
        // set max velocity
        if (xVelocity > 15) {
            xVelocity = 15;
        } else if (xVelocity < -15) {
            xVelocity = -15;
        }
        playerX += xVelocity;
        // keep in frame
        if (playerX > 1810) {
            playerX = 1810;
        } else if (playerX < 0) {
            playerX = 0;
        }
        
        //Set Hitboxes to be in same place as player
        playerBox.y = playerY;
        playerBox.x = playerX;
        playerBoxShort.y = playerY;
        playerBoxShort.x = playerX;
        //If not jumping or falling, start a new thread to make you fall
        if(!isJumping && !isFalling) {
            new Thread(() -> {

                while (!playerBox.intersects(groundBox) && !playerBox.intersects(platformBox ) && !playerBox.intersects(PlatformBox2) && !playerBox.intersects(PlatformBox3)) {
                    isFalling = true;
                    playerY += yVelocity;
                    yVelocity += 2;
                    if(yVelocity > 49){
                        yVelocity = 49;
                    }
                    repaint();
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playerBox.x = playerX;
                    playerBox.y = playerY;
                    repaint();
                }
                isFalling = false;
                //once you have fell go up until the short box is not in the ground
                while (playerBoxShort.intersects(groundBox) || playerBoxShort.intersects(platformBox ) || playerBoxShort.intersects(PlatformBox2) || playerBoxShort.intersects(PlatformBox3)) {
                    playerY -= 1;
                    playerBoxShort.y = playerY;
                    playerBox.y = playerY;
                }
            }).start();
        }
        //if player underground go up
        if(playerY > 955) {
            playerY = 955;
            playerBox.y = playerY;
        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //before you draw update
        update();


        //draw platforms
        g.setColor(Color.DARK_GRAY);
        g.fillRect(900,650,250,10);
        g.fillRect(1100,450,250,10);
        g.fillRect(250, 800, 500, 10);
        //draw ground
        g.setColor(Color.BLACK);
        g.fillRect(0, 1000, 2000, 15);

        //Draw player
        new Player(playerX,playerY,g,isCrouching);

    }
    public void crouch() {
        new Thread(() -> {
            isCrouching = true;
            repaint();

        }).start();
        isCrouching = false;
    }

    public void jump() {
        //if not jumping or falling then set upward velocity to 49 and then apply gravity in another thread
        if (!isJumping && !isFalling) {
            isCrouching = false;
            isJumping = true;
            new Thread(() -> {
                yVelocity = 49;
                for (int i = 0; i < 10; i++) {
                    playerY -= yVelocity;
                    yVelocity -= 5;
                    repaint();
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //fall to the ground
                while (!playerBox.intersects(groundBox) && !playerBox.intersects(platformBox) && !playerBox.intersects(PlatformBox2) && !playerBox.intersects(PlatformBox3)) {
                    playerY += yVelocity;
                    yVelocity += 5;
                    if(yVelocity > 49){
                        yVelocity = 49;
                    }
                    repaint();
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playerBox.x = playerX;
                    playerBox.y = playerY;
                }
                //move up until small Hitbox is not in the ground
                while (playerBox.intersects(groundBox) || playerBox.intersects(platformBox) || playerBox.intersects(PlatformBox2) || playerBox.intersects(PlatformBox3)){
                    playerY -= 1;
                    playerBox.y = playerY;

                }
                playerBox.y = playerY;
                isJumping = false;
            }).start();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //if key pressed deal with the input
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W) {
            //up key or space pressed
            jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_S){
            //down key pressed
            crouch();
        }
        if (e.getKeyCode() == KeyEvent.VK_D){
            // Left key pressed
            xVelocity += 10;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_A){
            // Right key pressed
            xVelocity -= 10;
            repaint();
        }

    }



    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D){
            //when "D" is released reverse the velocity change
            xVelocity -= 5;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            //when "A" is released reverse the velocity change
            xVelocity += 5;
        } else if (e.getKeyCode() == KeyEvent.VK_S){
            //when "S" is released stop crouching
            isCrouching = false;
            repaint();
        }
    }
    //Main class is run
    public static void main(String[] args) {
        new Main();
    }
}
