package org.ort_rehovot.bubble_shooter;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
	@Getter
	private Ball player;

	private Ball[][] grid;
	private int rows = 5, cols = 28;

	@Getter
	@Setter
	private int width;

	@Getter
	@Setter
	private int height;
	private final GamePanel view;
	//private final GamePanel gameOverView;

	private final static int MIN_NUM_TO_EXPLODE = 3;

	@Getter
	@Setter
	private boolean gameOver = false;

	private int throwsCounter = 0;

	/***
	 * constructor
	 * @param panel panel
	 */
	public GameModel(GamePanel panel) {
		this.view = panel;
		initGame();
	}


	/***
	 * get panel
	 * @return panel
	 */
	public GamePanel getView() {
		if (!gameOver) {
			return view;
		} else {
			// TODO
			return view;
		}
	}

	private void initRandomBalls() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				grid[r][c] = Ball.create(r, c, this);
			}
		}
	}

	private void initDebugBallsInternal() {
		grid[0][0] = Ball.create(0, 0, this, 5);
		grid[0][1] = Ball.create(0, 1, this, 1);
		grid[0][2] = Ball.create(0, 2, this, 1);
		grid[0][3] = Ball.create(0, 3, this, 5);
		grid[0][4] = Ball.create(0, 4, this, 2);
		grid[0][5] = Ball.create(0, 5, this, 2);
		grid[0][6] = Ball.create(0, 6, this, 3);
		grid[0][7] = Ball.create(0, 7, this, 6);
		grid[0][8] = Ball.create(0, 8, this, 4);
		grid[0][9] = Ball.create(0, 9, this, 1);
		grid[0][10] = Ball.create(0, 10, this, 1);
		grid[0][11] = Ball.create(0, 11, this, 1);
		grid[0][12] = Ball.create(0, 12, this, 6);
		grid[0][13] = Ball.create(0, 13, this, 3);
		grid[0][14] = Ball.create(0, 14, this, 5);
		grid[0][15] = Ball.create(0, 15, this, 1);
		grid[0][16] = Ball.create(0, 16, this, 4);
		grid[0][17] = Ball.create(0, 17, this, 4);
		grid[0][18] = Ball.create(0, 18, this, 1);
		grid[0][19] = Ball.create(0, 19, this, 2);
		grid[0][20] = Ball.create(0, 20, this, 5);
		grid[0][21] = Ball.create(0, 21, this, 3);
		grid[0][22] = Ball.create(0, 22, this, 2);
		grid[0][23] = Ball.create(0, 23, this, 4);
		grid[0][24] = Ball.create(0, 24, this, 1);
		grid[0][25] = Ball.create(0, 25, this, 5);
		grid[0][26] = Ball.create(0, 26, this, 4);
		grid[0][27] = Ball.create(0, 27, this, 5);
		grid[1][0] = Ball.create(1, 0, this, 5);
		grid[1][1] = Ball.create(1, 1, this, 1);
		grid[1][2] = Ball.create(1, 2, this, 4);
		grid[1][3] = Ball.create(1, 3, this, 5);
		grid[1][4] = Ball.create(1, 4, this, 6);
		grid[1][5] = Ball.create(1, 5, this, 1);
		grid[1][6] = Ball.create(1, 6, this, 2);
		grid[1][7] = Ball.create(1, 7, this, 1);
		grid[1][8] = Ball.create(1, 8, this, 1);
		grid[1][9] = Ball.create(1, 9, this, 5);
		grid[1][10] = Ball.create(1, 10, this, 4);
		grid[1][11] = Ball.create(1, 11, this, 3);
		grid[1][12] = Ball.create(1, 12, this, 5);
		grid[1][13] = Ball.create(1, 13, this, 3);
		grid[1][14] = Ball.create(1, 14, this, 3);
		grid[1][15] = Ball.create(1, 15, this, 1);
		grid[1][16] = Ball.create(1, 16, this, 4);
		grid[1][17] = Ball.create(1, 17, this, 1);
		grid[1][18] = Ball.create(1, 18, this, 5);
		grid[1][19] = Ball.create(1, 19, this, 4);
		grid[1][20] = Ball.create(1, 20, this, 6);
		grid[1][21] = Ball.create(1, 21, this, 4);
		grid[1][22] = Ball.create(1, 22, this, 5);
		grid[1][23] = Ball.create(1, 23, this, 1);
		grid[1][24] = Ball.create(1, 24, this, 5);
		grid[1][25] = Ball.create(1, 25, this, 4);
		grid[1][26] = Ball.create(1, 26, this, 2);
		grid[1][27] = Ball.create(1, 27, this, 1);
		grid[2][0] = Ball.create(2, 0, this, 5);
		grid[2][1] = Ball.create(2, 1, this, 6);
		grid[2][2] = Ball.create(2, 2, this, 6);
		grid[2][3] = Ball.create(2, 3, this, 1);
		grid[2][4] = Ball.create(2, 4, this, 4);
		grid[2][5] = Ball.create(2, 5, this, 5);
		grid[2][6] = Ball.create(2, 6, this, 3);
		grid[2][7] = Ball.create(2, 7, this, 3);
		grid[2][8] = Ball.create(2, 8, this, 1);
		grid[2][9] = Ball.create(2, 9, this, 2);
		grid[2][10] = Ball.create(2, 10, this, 2);
		grid[2][11] = Ball.create(2, 11, this, 2);
		grid[2][12] = Ball.create(2, 12, this, 4);
		grid[2][13] = Ball.create(2, 13, this, 6);
		grid[2][14] = Ball.create(2, 14, this, 6);
		grid[2][15] = Ball.create(2, 15, this, 4);
		grid[2][16] = Ball.create(2, 16, this, 1);
		grid[2][17] = Ball.create(2, 17, this, 1);
		grid[2][18] = Ball.create(2, 18, this, 2);
		grid[2][19] = Ball.create(2, 19, this, 2);
		grid[2][20] = Ball.create(2, 20, this, 5);
		grid[2][21] = Ball.create(2, 21, this, 1);
		grid[2][22] = Ball.create(2, 22, this, 4);
		grid[2][23] = Ball.create(2, 23, this, 2);
		grid[2][24] = Ball.create(2, 24, this, 4);
		grid[2][25] = Ball.create(2, 25, this, 6);
		grid[2][26] = Ball.create(2, 26, this, 6);
		grid[2][27] = Ball.create(2, 27, this, 6);
		grid[3][0] = Ball.create(3, 0, this, 4);
		grid[3][1] = Ball.create(3, 1, this, 4);
		grid[3][2] = Ball.create(3, 2, this, 4);
		grid[3][3] = Ball.create(3, 3, this, 5);
		grid[3][4] = Ball.create(3, 4, this, 6);
		grid[3][5] = Ball.create(3, 5, this, 1);
		grid[3][9] = Ball.create(3, 9, this, 5);
		grid[3][10] = Ball.create(3, 10, this, 3);
		grid[3][11] = Ball.create(3, 11, this, 2);
		grid[3][12] = Ball.create(3, 12, this, 4);
		grid[3][13] = Ball.create(3, 13, this, 6);
		grid[3][14] = Ball.create(3, 14, this, 2);
		grid[3][15] = Ball.create(3, 15, this, 1);
		grid[3][16] = Ball.create(3, 16, this, 3);
		grid[3][17] = Ball.create(3, 17, this, 4);
		grid[3][18] = Ball.create(3, 18, this, 1);
		grid[3][19] = Ball.create(3, 19, this, 5);
		grid[3][20] = Ball.create(3, 20, this, 6);
		grid[3][21] = Ball.create(3, 21, this, 6);
		grid[3][22] = Ball.create(3, 22, this, 5);
		grid[3][23] = Ball.create(3, 23, this, 2);
		grid[3][24] = Ball.create(3, 24, this, 5);
		grid[3][25] = Ball.create(3, 25, this, 1);
		grid[3][26] = Ball.create(3, 26, this, 2);
		grid[3][27] = Ball.create(3, 27, this, 6);
		grid[4][0] = Ball.create(4, 0, this, 1);
		grid[4][1] = Ball.create(4, 1, this, 5);
		grid[4][2] = Ball.create(4, 2, this, 6);
		grid[4][3] = Ball.create(4, 3, this, 1);
		grid[4][4] = Ball.create(4, 4, this, 2);
		grid[4][5] = Ball.create(4, 5, this, 5);
		grid[4][6] = Ball.create(4, 6, this, 5);
		grid[4][9] = Ball.create(4, 9, this, 3);
		grid[4][10] = Ball.create(4, 10, this, 5);
		grid[4][11] = Ball.create(4, 11, this, 6);
		grid[4][12] = Ball.create(4, 12, this, 3);
		grid[4][13] = Ball.create(4, 13, this, 1);
		grid[4][14] = Ball.create(4, 14, this, 5);
		grid[4][15] = Ball.create(4, 15, this, 4);
		grid[4][16] = Ball.create(4, 16, this, 6);
		grid[4][17] = Ball.create(4, 17, this, 3);
		grid[4][18] = Ball.create(4, 18, this, 2);
		grid[4][19] = Ball.create(4, 19, this, 5);
		grid[4][20] = Ball.create(4, 20, this, 6);
		grid[4][21] = Ball.create(4, 21, this, 1);
		grid[4][22] = Ball.create(4, 22, this, 1);
		grid[4][23] = Ball.create(4, 23, this, 4);
		grid[4][24] = Ball.create(4, 24, this, 4);
		grid[4][25] = Ball.create(4, 25, this, 3);
		grid[4][26] = Ball.create(4, 26, this, 4);
		grid[4][27] = Ball.create(4, 27, this, 1);
		grid[5][7] = Ball.create(5, 7, this, 1);
		grid[6][7] = Ball.create(6, 7, this, 1);

	}
	void initDebugBalls() {
		initDebugBallsInternal();
		updateRows();
	}
	/***
	 * initializes the game
	 */
	public void initGame()
	{
		setNewPlayer();
		grid = new Ball[Constants.MAX_ROWS+1][Constants.MAX_COLS];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = new Ball(0, 0, 50,this,false);
				grid[i][j].setInvisible();
				grid[i][j].setRow(i);
				grid[i][j].setColumn(j);
			}
		initRandomBalls();
		//initDebugBalls();
	}

	public void printDebug() {
		for (int r=0; r<grid.length; r++) {
			for (int c=0; c<grid[r].length; c++) {
				Ball ball = grid[r][c];
				if (!ball.isInvisible()) {
					System.out.printf("grid[%d][%d] = Ball.create(%d, %d, this, %d);\n", r, c, r, c, ball.getColor());
				}
			}
		}
	}

	@AllArgsConstructor
	@Getter
	private static class GridCoords {
		private final int row;
		private final int column;
	}

	/***
	 * checks for which ball it collided
	 * @param x x position
	 * @param y y position
	 * @param r radius
	 * @return the collided ball
	 */
	private GridCoords collides(int x,int y,int r) {
		for (int i = rows-1; i >= 0; i--) {
			for (int j = 0; j < cols; j++) {
				if (grid[i][j].isInvisible()) {
					continue;
				}
				int ballX = grid[i][j].getX();
				int ballY = grid[i][j].getY();
				int dx = ballX - x;
				int dy = ballY - y;
				double d = dx*dx + dy*dy;
				if (d < 4*r*r) {
					return new GridCoords(i, j);
				}

			}
		}
		return null;
	}

	/***
	 * update the amount of rows in the game
	 */
	void updateRows() {
		int newRows = 0;
		for (Ball[] arrBall : grid) {
			for (Ball ball : arrBall) {
				if (!ball.isInvisible()) {
					newRows++;
					break;
				}
			}
		}
		rows = newRows;
	}

	/***
	 * find which sector the ball collided with another ball
	 * @param ballX x1 position
	 * @param ballY y1 position
	 * @param playerX x2 position
	 * @param playerY y2 position
	 * @return the number of sector the ball collided with
	 */
	private static int findSector(int ballX, int ballY, int playerX, int playerY) {
		if (ballX == playerX) {
			if (playerY > ballY) {
				return 7;
			}
			return 2;
		}

		double deltaX = playerX - ballX;
		double deltaY = -(playerY - ballY);

		double beta = Math.atan2(deltaY, deltaX) * 180.0 / Math.PI;
		int alpha = ((int)beta + 360) % 360;

		return 1 + alpha / 45;
	}

	/**
	 *
	 * @param x x - center of the player
	 * @param y y - center of the player
	 * @param width dimensions of the ball
	 * @param color color of the player
	 * @return true iff collides
	 */
	public boolean checkCollision(int x,int y,int width,int color)
	{

		GridCoords gridCoords = collides(x, y, width/2);

		if (gridCoords == null) {
			return false;
		}

		int row = gridCoords.getRow();
		int column = gridCoords.getColumn();

		int otherX = grid[row][column].getX();
		int otherY = grid[row][column].getY();

		int sector = findSector(otherX, otherY, x, y);



		int newRow = row+1;
		int newColumn = column;
		if (newRow >= Constants.MAX_ROWS || newColumn >= grid[newRow].length) {
			setGameOver(true);
			updateRows();
			setNewPlayer();
			return true;
		}
		if (newRow % 2 == 0) {
			switch (sector) {
				case 1:
					newRow--;
					newColumn++;
					break;
				case 4:
					newColumn--;
					newRow--;
					break;
				case 5:
				case 6:
					newColumn--;
					break;
				case 7:
				case 8:
					break;
				default:
					printDebug();
					throw new RuntimeException(String.format("Unknown sector %d", sector));
			}
		} else {
			switch (sector) {
				case 1:
					newColumn++;
					newRow--;
					break;
				case 4:
					newColumn--;
					newRow--;
					break;
				case 5:
				case 6:
					break;
				case 7:
					break;
				case 8:
					//newColumn++;
					break;
				default:
					printDebug();
					throw new RuntimeException(String.format("Unknown sector %d", sector));
			}
		}
		System.out.printf("OldGrid = (%d, %d) Sector = %d NewGrid (%d, %d) color = %d\n", row, column, sector, newRow, newColumn,color);

		if (!grid[newRow][newColumn].isInvisible()) {
			switch (sector) {
				case 7, 6, 8 -> newColumn++;
				default -> {
				}
			}

		}

		grid[newRow][newColumn] = Ball.create(newRow, newColumn, this, color);

		if (newRow >= Constants.MAX_ROWS || newColumn >= grid[newRow].length) {
			setGameOver(true);
			updateRows();
			setNewPlayer();
			return true;
		}

		printGrid(row, column, newRow, newColumn);

		List<Ball> collisions = getCollisions(newRow, newColumn, color);
		if (collisions.size() >= MIN_NUM_TO_EXPLODE) {
			explode(collisions);
			view.playExplosion();
			throwsCounter = 0;
		} else {
			view.playBoom();
			throwsCounter++;
		}
		System.out.printf("Throws = %d\n", throwsCounter);

		collisions = getLonelyBalls(newRow);
		if (!collisions.isEmpty()) {
			explode(collisions);
			view.playExplosion();
		}

		moveRowsDown(newRow);

		System.out.println("//////////////////////////////////////////////////////////////////");
		printDebug();

		updateRows();
		setNewPlayer();
		return true;
	}

	private void moveRowsDown(int newRow) {
		if (throwsCounter >= Constants.MAX_BAD_THROWS) {
			addRow(newRow);
		//	printGrid(row, column, newRow, newColumn);
			throwsCounter = 0;
		}
	}

	private void printGrid(int row, int column, int newRow, int newColumn) {
		for (int r=0; r<grid.length; r++) {
			System.out.printf("%d :", r);
			for (int c=0; c<grid[r].length; c++) {
				Ball ball = grid[r][c];
				if (ball.isInvisible()) {
					System.out.print(" . ");
				} else {
					if (r == row && c == column) {
						System.out.printf("|%d|", ball.getColor());
					} else {
						if (r == newRow && c == newColumn) {
							System.out.printf("[%d]", ball.getColor());
						} else {
							System.out.printf(" %d ", ball.getColor());
						}
					}
				}
			}
			System.out.println();
		}
	}

	private void addRow(int lastRow) {
		lastRow++;
		if (lastRow >= Constants.MAX_ROWS || lastRow >= grid[lastRow].length) {
			setGameOver(true);
			updateRows();
			setNewPlayer();
			return;
		}
		for (int r = lastRow; r>=1; r--) {
			for (int c=0; c<Constants.MAX_COLS; c++) {
				grid[r][c] = grid[r-1][c];
				grid[r][c].setRow(r);
				grid[r][c].reinitCoords();
			}
		}
		for (int c=0; c<Constants.MAX_COLS; c++) {
			grid[0][c] = Ball.create(0, c, this);
			grid[0][c].reinitCoords();
		}
	}
	/***
	 * explodes all the balls that need to be destroyed
	 * @param balls future-destroyed balls
	 */
	private void explode(List<Ball> balls) {
		for (val b : balls) {
			//b.setInvisible();
			b.setExplosion(0);
		}
	}

	public boolean hasBallsToExplode() {
		for (int r = 0; r < Constants.MAX_ROWS; r++) {
			for (int c=0; c<Constants.MAX_COLS; c++) {
				if(grid[r][c].getExplosion() >= 0) {
					return true;
				}
			}
		}
		return false;
	}

	private List<Ball> getLonelyBalls(int lastRow)
	{
		List<Ball> out = new ArrayList<>();

		for (int r = lastRow; r>=1; r--) {
			for (int c=1; c<Constants.MAX_COLS-1; c++) {
				if(!grid[r][c].isInvisible() &&
						grid[r+1][c+1].isInvisible()
						&& grid[r][c-1].isInvisible()
						&& grid[r-1][c].isInvisible()
						&& grid[r-1][c+1].isInvisible()
						&& grid[r][c+1].isInvisible())
				{
					out.add(grid[r][c]);
				}
			}
		}
		return out;
	}
	/***
	 * get all the balls that need to be destroyed
	 * @param i row in grid
	 * @param j column in grid
	 * @param color color of the ball
	 * @return the collided balls
	 */
	private List<Ball> getCollisions(int i, int j, int color) {
		List<Ball> out = new ArrayList<>();
		for (Ball[] arrBall : grid) {
			for (Ball ball : arrBall) {
				ball.setVisited(false);
			}
		}

		getCollisionsHelper(i, j, color, out);
		return out;
	}

	/***
	 * recursive method to get all the balls that need to be destroyed
	 * @param i row in grid
	 * @param j column in grid
	 * @param color color of the ball
	 * @param out list of the future-destroyed balls
	 */
	private void getCollisionsHelper(int i, int j, int color, List<Ball> out) {
		if (i < 0 || i >= grid.length) {
			return;
		}
		if (j < 0 || j >= grid[i].length) {
			return;
		}
		if (!grid[i][j].isValid() || grid[i][j].isVisited()) {
			return;
		}

		if (grid[i][j].getColor() == color) {
			grid[i][j].setVisited(true);
			out.add(grid[i][j]);
			if (i % 2 != 0) {
				getCollisionsHelper(i-1, j, color, out);
				getCollisionsHelper(i, j+1, color, out);
				getCollisionsHelper(i+1, j, color, out);
				getCollisionsHelper(i+1, j-1, color, out);
				getCollisionsHelper(i, j-1, color, out);
				getCollisionsHelper(i-1, j-1, color, out);
			} else {
				getCollisionsHelper(i-1, j, color, out);
				getCollisionsHelper(i-1, j+1, color, out);
				getCollisionsHelper(i, j+1, color, out);
				getCollisionsHelper(i+1, j+1, color, out);
				getCollisionsHelper(i, j+1, color, out);
				getCollisionsHelper(i, j-1, color, out);
			}
		}
	}

	public void setNewPlayer() {
		if (gameOver) {
			player = new Ball(Constants.PLAYER_X, Constants.PLAYER_Y, Constants.SPRITE_R, this, false);
			player.setInvisible();
		} else {
			player = new Ball(Constants.PLAYER_X, Constants.PLAYER_Y, Constants.SPRITE_R, this, true);
		}
	}

	public Ball[][] getGrid() {
		return grid;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

}
