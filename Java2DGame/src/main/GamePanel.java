package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	//Screen settings
	final int originalTileSize = 16; // 16x 16 tiles
	final int scale = 3; //to scale for better or bigger monitors
	final int tileSize = originalTileSize * scale; //48 to appear larger on monitor
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; //768 pixels
	final int screenHeight = tileSize * maxScreenRow;//576 pixels
	Thread gameThread; //something you can start or stop can keep your program running until you stop it can help with repetitous processes
	
	KeyHandler keyH = new KeyHandler();
	//set players default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	int FPS = 60;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		//this is similar to a canvas drawing a screen, can improve game performance
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	 public void startGameThread() {
		 gameThread = new Thread(this);//passing game panel to the thread constructor
		 gameThread.start();
	 }
	@Override
	public void run() {
		//0.01666 seconds
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime =System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		//you would use gamethread != null but thats the gist
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if(delta >= 1) {
				update();
				repaint();
				delta --;
				drawCount++;
			}
			if(timer >= 1000000000) {
				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	/**
	 * This is the previous loop have to copy newer loop to display FPS
	 * 
	 */
	//this is another loop
	public void sleepMethod() {
		double drawInterval = 1000000000/FPS; //This is to represent one second as we use nanoSeconds this is to get it every 1/60 of a seconds
		double nextDrawTime = System.nanoTime() + drawInterval;
		long timer = 0;
		int drawCount = 0;
		while(gameThread != null) {
			
			update();
			
			repaint(); 
			drawCount++;
			try {
				double remainingTime = nextDrawTime - System.nanoTime(); //how much time remaining until the next loop
				remainingTime = remainingTime /1000000; //to pass as milliseconds
				if(remainingTime < 0 ) {
					remainingTime = 0;
				}
				Thread.sleep((long)remainingTime);//ensures the loop runs 60 times a second
				
				nextDrawTime +=drawInterval;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void accumpulatorMethod() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime =System.nanoTime();
		long currentTime;
		//you would use gamethread != null but thats the gist
		while(true) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if(delta >= 1) {
				update();
				repaint();
				delta --;
			}
		}
	}
	public void update() {
		if(keyH.upPressed == true) {
			playerY -= playerSpeed;
		}
		if(keyH.downPressed == true) {
			playerY += playerSpeed;
		}
		if(keyH.leftPressed == true) {
			playerX -= playerSpeed;
		}
		if(keyH.rightPressed == true) {
			playerX += playerSpeed;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		
		g2.dispose();
	}
	
}
