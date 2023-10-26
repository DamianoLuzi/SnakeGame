import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static int delay = 150;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;


    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
     }

    public void draw(Graphics g){
        if(running){
            
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i<bodyParts ; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i] , UNIT_SIZE, UNIT_SIZE);
                } else {
                    //g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Italics", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("score: "+applesEaten, ((SCREEN_WIDTH - metrics.stringWidth("score: "+applesEaten))/2), 2*UNIT_SIZE);

        }else gameOver(g);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT :
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT :
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP :
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN :
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    public void move(){
        
        for(int i = bodyParts ; i>0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
      
        switch(direction){
            case 'U' :
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' : 
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;  
        }
        
    }

    public void checkApple() {

        if(appleX == x[0] && appleY == y[0]) {
            applesEaten++;
            bodyParts++;
            //delay--;
            newApple();
        }

    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE) )*UNIT_SIZE ;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE))*UNIT_SIZE ;
    }

    public void checkCollisions() {
        //checks body collisions
        for(int i = bodyParts; i>0 ; i--){
            if((x[0] == x[i] ) && (y[0] == y[i])){
                running = false;
            }
        }
        //checks borders collisions
        if(x[0] < 0){
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if(x[0] > SCREEN_WIDTH){
            System.out.println("About to hit the border");
            running = false;
        } 
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(running == false){
            timer.stop();
        }

    }

    public void gameOver(Graphics g) { 
        g.setColor(Color.red);
        g.setFont(new Font("Italics", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", ((SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2),SCREEN_HEIGHT/2); 
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            //draw(getGraphics());
            move();
            checkApple();
            checkCollisions();
            repaint();
        } else {
            //repaint();
            gameOver(getGraphics());
        }

    }
    
    


}
