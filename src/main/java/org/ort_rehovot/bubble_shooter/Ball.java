package org.ort_rehovot.bubble_shooter;

//23-5-2021
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.awt.*;
import java.util.Random;



public class Ball{
	@Getter
	@Setter
	private int x;

	@Getter
	@Setter
	private int y;


	@Getter
	@Setter
	private int row;

	@Getter
	@Setter
	private int column;

	private final int width;

	@Getter
	@Setter
	private boolean visited;

	@Getter
	@Setter
	private int color;

	@Getter
	@Setter
	private int explosion = -1;

	private int dirx = 1;
	private int diry = 1;
	private final GameModel gameModel;
	private boolean isThrowable;

	@Getter
	@Setter
	private boolean isLeft;

	private double m;
	private Image image;

	@Getter
	@Setter
	private boolean activated;

	private static final Random rnd = new Random(0);

	private static int evalX(int r, int c) {
		if (r % 2 == 0) {
			return c * Constants.BALL_WIDTH + 50;
		}
		return c * Constants.BALL_WIDTH + 25;
	}

	private static int evalY(int r, int c) {
			return r * Constants.BALL_WIDTH + 30;
	}

	/***
	 * creates a ball
	 * @param r row
	 * @param c col
	 * @param gameModel panel
	 * @return created ball
	 */
	public static Ball create(int r, int c, GameModel gameModel) {
		Ball ret;
		if (r % 2 == 0) {
			ret = new Ball(c * Constants.BALL_WIDTH + 50, r * Constants.BALL_WIDTH + 30, Constants.BALL_WIDTH,gameModel,false);
		} else {
			ret = new Ball(c * Constants.BALL_WIDTH + 25, r * Constants.BALL_WIDTH + 30, Constants.BALL_WIDTH,gameModel,false);
		}
		ret.setRow(r);
		ret.setColumn(c);
		return ret;
	}

	public void reinitCoords() {
		if (row % 2 == 0) {
			x = column * Constants.BALL_WIDTH + 50;
			y = row * Constants.BALL_WIDTH + 30;
		} else {
			x = column * Constants.BALL_WIDTH + 25;
			y = row * Constants.BALL_WIDTH + 30;
		}
	}

	/***
	 * creates a ball with specific color
	 * @param r row
	 * @param c col
	 * @param gameModel panel
	 * @return created ball
	 */
	public static Ball create(int r, int c, GameModel gameModel, int color) {
		Ball ret = create(r,c,gameModel);
		ret.color = color;
		ret.image = ResourceLoader.getInstance().getBallImage(color);
		return ret;
	}

	public void changeColor() {
		color = 1 + ((color + 1) % ResourceLoader.NUM_BALLS);
		image = ResourceLoader.getInstance().getBallImage(color);
	}

	/***
	 * constructor
	 * @param x x position
	 * @param y y position
	 * @param width width
	 * @param gameModel panel
	 * @param throwable if the ball is throwable
	 */
	public Ball(int x, int y, int width,GameModel gameModel,boolean throwable) {

		this.x = x;
		this.y = y;
		this.width = Constants.BALL_WIDTH;
		this.gameModel = gameModel;
		this.color = rnd.nextInt(6) + 1;
		isThrowable = throwable;
		isLeft = false;

		if (color != -1) {
			image = ResourceLoader.getInstance().getBallImage(color);
		}
		activated = false;
	}

	/***
	 * constructor with specific color
	 * @param x x position
	 * @param y y position
	 * @param width width
	 * @param color color of the ball
	 * @param gameModel panel
	 * @param throwable if the ball is throwable
	 */
	public Ball(int x, int y, int width,int color,GameModel gameModel,boolean throwable) {

		this.x = x;
		this.y = y;
		this.width = Constants.BALL_WIDTH;
		this.color = color;
		this.gameModel = gameModel;
		isThrowable = throwable;
		isLeft = false;

		if (color != -1) {
			image = ResourceLoader.getInstance().getBallImage(color);
		}
	}

	/***
	 * draws the ball
	 * @param g graphic object
	 */
	public void draw(Graphics g) {
		if (!isInvisible())
		{
			g.drawImage(image, x - width / 2, y - width / 2, width, width, null);
		} else {
			if (explosion > -1) {
				g.drawImage(ResourceLoader.getInstance().getExplosion()[explosion], x - width / 2, y - width / 2, width, width, null);
			}
		}

	}

	public void advanceExplosionAnimation() {
		explosion++;
		System.out.println("explosion:"+explosion);
		if (explosion >= ResourceLoader.getInstance().getExplosion().length) {
			explosion = -1;
			setInvisible();
		}
	}

	/***
	 * check if the ball collided
	 * @return true iff the ball collided with another ball
	 */
	public boolean checkThrow() {
		if (isThrowable) {
			boolean flag = gameModel.checkCollision(x, y, width, color);
			if(flag)
			{
				isThrowable=false;
				setInvisible();
			}
				
			return flag;
		}
		return false;
	}

	/***
	 * "destroys" the ball
	 */
	public void setInvisible () {
		color = -1;
	}

	/***
	 * check if the ball is destroyed
	 * @return true iff color is invisible
	 */
	public boolean isInvisible() { return color == -1 || explosion > -1; }

	public boolean isNoneColor(){return color == -1;}

	/***
	 * check if a ball is destroyed
	 * @return true iff ball isn't destroyed
	 */
	public boolean isValid() {
		return color != -1;
	}

	/***
	 * sets the slope of the direction
	 * @param m slope
	 */
	public void setSlope(double m) {
		if (m > 0) {
			m *= -1;
			isLeft = true;
		}
		this.m = m;
	}

	/***
	 * get slope
	 * @return slope
	 */
	public double getSlope()
	{
		return this.m;
	}

	/***
	 * moves x position
	 * @param x x position to move
	 */
	public void addX(int x)
	{
		this.x+=x;
	}
	/***
	 * moves y position
	 * @param y y position to move
	 */
	public void addY(int y)
	{
		this.y+=y;
	}
}
