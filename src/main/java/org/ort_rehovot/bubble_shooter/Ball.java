package org.ort_rehovot.bubble_shooter;

//23-5-2021
import lombok.Getter;
import lombok.Setter;

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
	private boolean isThrowable;

	private double m;
	private Image image;

	@Getter
	@Setter
	private boolean activated;

	private static final Random rnd = new Random(Constants.SEED);


	/***
	 * creates a ball
	 * @param r row
	 * @param c col
	 * @return created ball
	 */
	public static Ball create(int r, int c) {
		Ball ret;
		if (r % 2 == 0) {
			ret = new Ball(c * Constants.BALL_WIDTH + 50, r * Constants.BALL_WIDTH + 30,false);
		} else {
			ret = new Ball(c * Constants.BALL_WIDTH + 25, r * Constants.BALL_WIDTH + 30,false);
		}
		ret.setRow(r);
		ret.setColumn(c);
		return ret;
	}

	public void reinitCoords() {
		if (row % 2 == 0) {
			x = column * Constants.BALL_WIDTH + 50;
		} else {
			x = column * Constants.BALL_WIDTH + 25;
		}
		y = row * Constants.BALL_WIDTH + 30;
	}

	/***
	 * creates a ball with specific color
	 * @param r row
	 * @param c col
	 * @param gameModel panel
	 * @return created ball
	 */
	public static Ball create(int r, int c, GameModel gameModel, int color) {
		Ball ret = create(r,c);
		ret.color = color;
		if (color >= 0) {
			ret.image = ResourceLoader.getInstance().getBallImage(color);
		}
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
	 * @param throwable if the ball is throwable
	 */
	public Ball(int x, int y,boolean throwable) {

		this.x = x;
		this.y = y;
		this.width = Constants.BALL_WIDTH;
		this.color = rnd.nextInt(6) + 1;
		isThrowable = throwable;

		image = ResourceLoader.getInstance().getBallImage(color);
		activated = false;
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
		if(row==5 && column == 21)
			System.out.println(explosion);
		if (explosion >= ResourceLoader.getInstance().getExplosion().length) {
			explosion = -1;
			setInvisible();
		}
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
