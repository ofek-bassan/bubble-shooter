package org.ort_rehovot.bubble_shooter;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    @Getter
    private Ball player;
    @Getter
    private Ball rivalPlayer;

    private Ball[][] grid;
    private int rows = Constants.START_NUM_ROWS;
    private final int cols = Constants.START_NUM_COLS;

    @Getter
    @Setter
    private int width;

    @Getter
    @Setter
    private int height;
    private final GamePanel view;

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
        return view;
    }

    private void initRandomBalls() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols - 1; c++) {
                grid[r][c] = Ball.create(r, c);
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int c = cols + 1; c < Constants.MAX_COLS; c++) {
                grid[r][c] = Ball.create(r, c, grid[r][c - cols - 1].getColor());
            }
        }
    }

    private void initDebugBallsInternal() {
    }

    void initDebugBalls() {
        initDebugBallsInternal();
        updateRows();
    }

    /***
     * initializes the game
     */
    public void initGame() {
        setNewPlayerOrRival(true);
        setNewPlayerOrRival(false);
        grid = new Ball[Constants.MAX_ROWS + 1][Constants.MAX_COLS];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Ball(0, 0);
                grid[i][j].setInvisible();
                grid[i][j].setRow(i);
                grid[i][j].setColumn(j);
            }
        initRandomBalls();
        //initDebugBalls();
    }

    public void printDebug() {
        /*
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                Ball ball = grid[r][c];
                if (!ball.isInvisible()) {
                    System.out.printf("grid[%d][%d] = Ball.create(%d, %d, this, %d);\n", r, c, r, c, ball.getColor());
                }
            }
        }

         */
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
    private GridCoords collides(int x, int y, int r, boolean isPlayer) {
        if (y <= 0) {
            return new GridCoords(-1, -1);
        }
        if (isPlayer)
            return getGridCoords(x, y, r, 0, cols);
        else
            return getGridCoords(x, y, r, cols + 1, Constants.MAX_COLS);
    }

    private GridCoords getGridCoords(int x, int y, int r, int startCol, int endCol) {
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = startCol; j < endCol; j++) {
                if (grid[i][j].isInvisible()) {
                    continue;
                }
                int ballX = grid[i][j].getX();
                int ballY = grid[i][j].getY();
                int dx = ballX - x;
                int dy = ballY - y;
                double d = dx * dx + dy * dy;
                if (d < 4 * r * r) {
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
        int alpha = ((int) beta + 360) % 360;

        return 1 + alpha / 45;
    }

    /**
     * @param x     x - center of the player
     * @param y     y - center of the player
     * @param width dimensions of the ball
     * @param color color of the player
     * @return true iff collides
     */
    public boolean checkCollision(int x, int y, int width, int color, boolean isPlayer) {

        GridCoords gridCoords = collides(x, y, width / 2, isPlayer);

        if (gridCoords == null) {
            return false;
        }

        int row = gridCoords.getRow();
        int column = gridCoords.getColumn();

        int newRow = row + 1;
        int newColumn = column;

        int sector = -1;
        if (row == -1 && column == -1) {
            System.out.printf("Grid = (%d, %d)\n", (x - 50) / Constants.BALL_WIDTH, 0);
            newColumn = (x - 50) / Constants.BALL_WIDTH;
            newRow = 0;
        } else {
            int otherX = grid[row][column].getX();
            int otherY = grid[row][column].getY();
            sector = findSector(otherX, otherY, x, y);
        }

        if (newRow >= Constants.MAX_ROWS || newColumn >= grid[newRow].length) {
            setGameOver(true);
            updateRows();
            setNewPlayerOrRival(column < cols + 1);
            return true;
        }
        if (newRow % 2 == 0) {
            switch (sector) {
                case 2:
                    newRow -= 2;
                    newColumn++;
                    break;
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
            }
        } else {
            switch (sector) {
                case 2:
                    newRow -= 2;
                    newColumn++;
                    break;
                case 1:
                    newColumn++;
                    newRow--;
                    break;
                case 4:
                    newColumn--;
                    newRow--;
                case 6:
                case 7:
                case 5:
                case 8:
                    break;
                default:
                    printDebug();
            }
        }
        //System.out.printf("OldGrid = (%d, %d) Sector = %d NewGrid (%d, %d) color = %d\n", row, column, sector, newRow, newColumn, color);

        if (!grid[newRow][newColumn].isInvisible()) {
            switch (sector) {
                case 5 -> {
                    newRow--;
                    newColumn--;
                }
                case 8, 7, 6, -1 -> newColumn++;
                default -> {
                }
            }

        }

        grid[newRow][newColumn] = Ball.create(newRow, newColumn, color);

        if (newRow >= Constants.MAX_ROWS || newColumn >= grid[newRow].length) {
            setGameOver(true);
            updateRows();
            setNewPlayerOrRival(column < cols + 1);
            return true;
        }

        //printGrid(row, column, newRow, newColumn);

        List<Ball> collisions = getCollisions(newRow, newColumn, color);

        boolean exploded = false;

        if (collisions.size() >= MIN_NUM_TO_EXPLODE) {
            GlobalState.getInstance().updateScore(collisions.size());
            explode(collisions);
            exploded = true;
            if(isPlayer)
                throwsCounter = 0;
        } else {
            SoundSystem.getInstance().playBoom();
            if(isPlayer)
                throwsCounter++;
        }
        //System.out.printf("Throws = %d\n", throwsCounter);

        List<Ball> singletons = getClusters();
        /*
        System.out.println("=========================================================");
        System.out.printf("Collisions %d singletons %d\n,", collisions.size(), singletons.size());
        System.out.println("=========================================================");
         */

        if (!singletons.isEmpty()) {
            GlobalState.getInstance().updateScore(singletons.size());
            explode(singletons);
            exploded = true;
        }
        if (exploded) {
            SoundSystem.getInstance().playExplosion();
        }


        moveRowsDown(newRow);
        singletons = getClusters();

        if (!singletons.isEmpty()) {
            explode(singletons);
            exploded = true;
        }
        if (exploded) {
            SoundSystem.getInstance().playExplosion();
        }
        /*
        System.out.println("//////////////////////////////////////////////////////////////////");
        printDebug();
        System.out.println("//////////////////////////////////////////////////////////////////");
        printGrid(row, column, newRow, newColumn);

         */

        updateRows();
        return true;
    }

    private void moveRowsDown(int newRow) {
        if (throwsCounter >= Constants.MAX_BAD_THROWS) {
            throwsCounter = 0;
            GameProtocol.sendRowDown(newRow);
            addRow(newRow, true);
        }
    }

    private void printGrid(int row, int column, int newRow, int newColumn) {
        for (int r = 0; r < grid.length; r++) {
            System.out.printf("%d :", r);
            for (int c = 0; c < grid[r].length; c++) {
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

    public void addRow(int lastRow, boolean isPlayer) {
        lastRow++;
        int startcol,endcol;
        if(isPlayer)
        {
            startcol =0;
            endcol = cols-1;
        }
        else
        {
            startcol = cols+1;
            endcol = Constants.MAX_COLS;
        }
        if (lastRow >= Constants.MAX_ROWS || lastRow >= grid[lastRow].length) {
            setGameOver(true);
            updateRows();
            setNewPlayerOrRival(isPlayer);
            return;
        }
        for (int r = lastRow; r >= 1; r--) {
            for (int c = startcol; c < endcol; c++) {
                Ball ball = grid[r - 1][c];
                grid[r][c] = Ball.create(r, c, ball.getColor());
            }
        }
        for (int c = startcol; c < endcol; c++) {
            grid[0][c] = Ball.create(0, c);
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
            b.setExplosion((int) (100 * Math.random()) % 3);
        }
    }

    public List<Ball> hasBallsToExplode() {
        List<Ball> result = new ArrayList<>();
        for (int r = 0; r < Constants.MAX_ROWS; r++) {
            for (int c = 0; c < Constants.MAX_COLS; c++) {
                if (grid[r][c].getExplosion() >= 0) {
                    result.add(grid[r][c]);
                }
            }
        }
        return result;
    }

    private List<Ball> getClusters() {
        List<Ball> out = new ArrayList<>();
        for (Ball[] arrBall : grid) {
            for (Ball ball : arrBall) {
                ball.setVisited(false);
            }
        }

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < Constants.MAX_COLS; c++) {
                if (grid[r][c].isInvisible()) {
                    continue;
                }
                List<Ball> cluster = new ArrayList<>();
                if (isCluster(r, c, cluster)) {
                    out.addAll(cluster);
                }
                for (Ball b : cluster) {
                    b.setVisited(false);
                }
            }
        }
        return out;
    }

    public boolean isCluster(int r, int c, List<Ball> out) {
        if (r == -1 || r >= grid.length) {
            return true;
        }
        if (r == 0) {
            if (c < 0 || c >= grid[r].length) {
                return true;
            }
            return grid[r][c].isInvisible();

        }
        if (c < 0 || c >= grid[r].length) {
            return true;
        }
        if (grid[r][c].isInvisible() || grid[r][c].isVisited()) {
            return true;
        }
        grid[r][c].setVisited(true);
        out.add(grid[r][c]);
        if (r % 2 != 0) {
            return isCluster(r - 1, c, out) &&
                    isCluster(r, c + 1, out) &&
                    isCluster(r + 1, c, out) &&
                    isCluster(r + 1, c - 1, out) &&
                    isCluster(r, c - 1, out) &&
                    isCluster(r - 1, c - 1, out);
        } else {
            return isCluster(r - 1, c, out) &&
                    isCluster(r - 1, c + 1, out) &&
                    isCluster(r, c + 1, out) &&
                    isCluster(r + 1, c + 1, out) &&
                    isCluster(r, c + 1, out) &&
                    isCluster(r, c - 1, out);
        }
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
     * @param r row in grid
     * @param c column in grid
     * @param color color of the ball
     * @param out list of the future-destroyed balls
     */
    private void getCollisionsHelper(int r, int c, int color, List<Ball> out) {
        if (r < 0 || r >= grid.length) {
            return;
        }
        if (c < 0 || c >= grid[r].length) {
            return;
        }
        if (!grid[r][c].isValid() || grid[r][c].isVisited()) {
            return;
        }

        if (grid[r][c].getColor() == color) {
            grid[r][c].setVisited(true);

            out.add(grid[r][c]);
            if (r % 2 != 0) {
                getCollisionsHelper(r - 1, c, color, out);
                getCollisionsHelper(r, c + 1, color, out);
                getCollisionsHelper(r + 1, c, color, out);
                getCollisionsHelper(r + 1, c - 1, color, out);
                getCollisionsHelper(r, c - 1, color, out);
                getCollisionsHelper(r - 1, c - 1, color, out);
            } else {
                getCollisionsHelper(r - 1, c, color, out);
                getCollisionsHelper(r - 1, c + 1, color, out);
                getCollisionsHelper(r, c + 1, color, out);
                getCollisionsHelper(r + 1, c + 1, color, out);
                getCollisionsHelper(r, c + 1, color, out);
                getCollisionsHelper(r, c - 1, color, out);
            }
        }
    }

    public void setNewPlayerOrRival(boolean isPlayer) {
        if (isPlayer)
        {
            player = new Ball(Constants.PLAYER_X, Constants.PLAYER_Y);
            Constants.PLAYER_COLOR = player.getColor();
        }
        else
        {
            rivalPlayer = new Ball(Constants.RIVAL_X, Constants.PLAYER_Y);
            Constants.RIVAL_COLOR = rivalPlayer.getColor();
        }
        System.out.println("Constants.RIVAL_COLOR:"+Constants.RIVAL_COLOR+" Constants.PLAYER_COLOR:"+Constants.PLAYER_COLOR);
    }

    public void setRivalAndPlayerColor(int player_color,int rival_color)
    {
        System.out.println("-----------------rival color:"+rival_color+" player_color:"+player_color+"-----------------");
        player = new Ball(Constants.PLAYER_X, Constants.PLAYER_Y);
        player.setColor(rival_color);
        rivalPlayer = new Ball(Constants.RIVAL_X, Constants.PLAYER_Y);
        rivalPlayer.setColor(player_color);
        System.out.println("-----------------rival color:"+rivalPlayer.getColor()+" player_color:"+player.getColor()+"-----------------");
        GlobalState.getInstance().getGp().repaint();
    }

    public Ball[][] getGrid() {
        return grid;
    }

}
