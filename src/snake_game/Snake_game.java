package snake_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Snake_game extends JFrame {

int cellSize = 15;
int speed = 200;
int gridWidth = 50;
int gridHeight = 30;
int marginX = 100;
int marginY = 100;
String direction = "Right";
Color gameZoneColor = Color.BLACK;
Color headColor = Color.RED;
Color bodyColor = Color.GREEN;
Color foodColor = Color.CYAN;
JPanel gamePanel;
Timer clock;
ArrayList<Cell> snake;
Cell food;
ArrayList<Cell> obstacles;
Color obstacleColor = Color.BLUE;

void initObstacles() {
	obstacles = new ArrayList<Cell>();
	for(int i = gridWidth / 4; i < gridWidth * 3 / 4 ; i++) {
		obstacles.add(new Cell(i, gridHeight / 3, obstacleColor));
		obstacles.add(new Cell(i, gridHeight * 2 / 3, obstacleColor));
	}
}

void initSnake() {
	snake = new ArrayList<Cell>();
	snake.add(new Cell(10, gridHeight / 2, headColor));
	snake.add(new Cell(9, gridHeight / 2, bodyColor));
	snake.add(new Cell(8, gridHeight / 2, bodyColor));
	snake.add(new Cell(7, gridHeight / 2, bodyColor));
	snake.add(new Cell(6, gridHeight / 2, bodyColor));
}

void initFood() {
	Random random = new Random();
	boolean exist;
	int x, y;
	do {
		exist = false;
		x = random.nextInt(gridWidth - 1);
		y = random.nextInt(gridHeight - 1);
		for(Cell c : snake) {
			if(x == c.x && y == c.y) {
				exist = true;
				break;
			}
		}
		for(Cell c : obstacles) {
			if(x == c.x && y == c.y) {
				exist = true;
				break;
			}
		}
	} while (exist);
	food = new Cell(x, y, foodColor);
}

public Snake_game() {
	this.setTitle("Welcome to the Snake Game!");
	this.setSize(marginX * 2 + gridWidth * cellSize + 14, marginY * 2 + gridHeight * cellSize + 40);
	this.setLocationRelativeTo(null);
	this.initSnake();
	this.initObstacles();
	this.initFood();

	// JPanel component setup
	gamePanel = new JPanel() {
		public void paint(Graphics g) {
			super.paint(g);
			drawGrid(g);
			drawSnake(g);
			drawCell(food, g);
			drawObstacles(g);
		}
	};
	this.setContentPane(gamePanel);

	// Timer setup
	clock = new Timer(speed, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Move snake's body before the head
			for(int i = snake.size() - 1; i > 0; i--) {
				snake.get(i).x = snake.get(i - 1).x;
				snake.get(i).y = snake.get(i - 1).y;
			}
			
			// Move snake's head
			if(direction.equals("Right")) snake.get(0).x++;
			if(direction.equals("Left")) snake.get(0).x--;
			if(direction.equals("Up")) snake.get(0).y--;
			if(direction.equals("Down")) snake.get(0).y++;
				
			if(snake.get(0).x == gridWidth) snake.get(0).x = 0;
			if(snake.get(0).x == -1) snake.get(0).x = gridWidth - 1;
			if(snake.get(0).y == gridHeight) snake.get(0).y = 0;
			if(snake.get(0).y == -1) snake.get(0).y = gridHeight - 1;
			
			// Check if food is eaten by the head
			if(snake.get(0).x == food.x && snake.get(0).y == food.y) {
				initFood();
				snake.add(new Cell(snake.get(snake.size() - 1).x, snake.get(snake.size() - 1).y, bodyColor));
			}
			
			// Stop conditions
			for(int i = 1; i < snake.size(); i++) {
				if(snake.get(i).x == snake.get(0).x && snake.get(i).y == snake.get(0).y)
					clock.stop();
			}
			for(int i = 0; i < obstacles.size(); i++) {
				if(obstacles.get(i).x == snake.get(0).x && obstacles.get(i).y == snake.get(0).y)
					clock.stop();
			}
			repaint();
		}
	});
	clock.start();
	this.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			// VK_RIGHT is the key code for the right arrow key
			if(e.getKeyCode() == KeyEvent.VK_RIGHT && !direction.equals("Left")) direction = "Right";
			if(e.getKeyCode() == KeyEvent.VK_LEFT && !direction.equals("Right")) direction = "Left";
			if(e.getKeyCode() == KeyEvent.VK_UP && !direction.equals("Down")) direction = "Up";
			if(e.getKeyCode() == KeyEvent.VK_DOWN && !direction.equals("Up")) direction = "Down";
		}
	});
	this.setVisible(true);
}	

void drawGrid(Graphics g) {
	g.setColor(gameZoneColor);
	g.fillRect(marginX, marginY, gridWidth * cellSize, gridHeight * cellSize);
	g.setColor(Color.GRAY);
	for(int i = 0; i < gridWidth; i++)
		g.drawLine(marginX + i * cellSize, marginY, marginX + i * cellSize, marginY + gridHeight * cellSize);
	for(int i = 0; i < gridHeight; i++)
		g.drawLine(marginX, marginY + i * cellSize, marginX + gridWidth * cellSize, marginY + i * cellSize);
}

void drawSnake(Graphics g) {
	for(Cell c : snake) {
		drawCell(c, g);
	}
}

void drawCell(Cell cell, Graphics g) {
	g.setColor(cell.color);
	g.fillRect(marginX + cell.x * cellSize, marginY + cell.y * cellSize, cellSize, cellSize);
}

void drawObstacles(Graphics g) {
	for(Cell c : obstacles) {
		drawCell(c, g);
	}
}

public static void main(String[] args) {
	new Snake_game();
}
}

class Cell {
    int x, y;
    Color color;

    public Cell(int x, int y, Color color) {
        super();
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
