package org.ort_rehovot.bubble_shooter;

import lombok.Getter;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {
	
	public final static int NUM_BALLS = 6;
	public final static int NUM_EXPLOSIONS = 20;
	
	private final Image [] balls;
	@Getter
	private final Image bgImage;

	@Getter
	private final Image exit;

	@Getter
	private final Image logo;

	@Getter
	private final Image offline;

	@Getter
	private final Image online;

	@Getter
	private final Image arrow;

	@Getter
	private final Image winImage;

	@Getter
	private final Image pauseImage;

	@Getter
	private final Image borderImage;


	@Getter
	private final Image[] explosion;

	@Getter
	private final Image gameOverGif;

	@Getter
	private final Clip boom;

	@Getter
	private final Clip explode;

	@Getter
	private final Clip backgroundMusic;

	private ResourceLoader() throws IOException{

		bgImage = new ImageIcon(ResourceLoader.loadResource("screen/bg.png")).getImage();
		logo = new ImageIcon(ResourceLoader.loadResource("screen/BubbleShooterLogo.png")).getImage();
		exit = new ImageIcon(ResourceLoader.loadResource("screen/exit.png")).getImage();
		offline = new ImageIcon(ResourceLoader.loadResource("screen/offline.png")).getImage();
		online = new ImageIcon(ResourceLoader.loadResource("screen/online.png")).getImage();
		borderImage = new ImageIcon(ResourceLoader.loadResource("screen/border.png")).getImage();
		pauseImage = new ImageIcon(ResourceLoader.loadResource("screen/pause.png")).getImage();
		winImage = new ImageIcon(ResourceLoader.loadResource("screen/win2.jpg")).getImage();
		gameOverGif = new ImageIcon(ResourceLoader.loadResource("screen/gameover2.gif")).getImage();
		balls = new Image[NUM_BALLS];
		for (int i=1; i<=NUM_BALLS; i++) {
			balls[i-1] = new ImageIcon(ResourceLoader.loadResource("balls/Ball"+i+".png")).getImage();
		}

		explosion = new Image[NUM_EXPLOSIONS];
		for (int i=0; i<NUM_EXPLOSIONS; i++) {
			explosion[i] = new ImageIcon(ResourceLoader.loadResource("explosion/tile" + i + ".png")).getImage();
		}

		arrow = new ImageIcon(ResourceLoader.loadResource("screen/arrow.png")).getImage();

		boom = loadMusicResource("boom.wav");

		explode = loadMusicResource("explode.wav");

		backgroundMusic = loadMusicResource("background.wav");
	}

	private Clip loadMusicResource(String name) throws IOException {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(ResourceLoader.class.getResourceAsStream("/sound/" + name)));
			return clip;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Failed to load sound " + name, e);
		}
	}
	
	private static ResourceLoader instance = null;
	
	public static void init() throws IOException {
		if (instance != null) {
			throw new RuntimeException("Already initialized");
		}
		instance = new ResourceLoader();
	}
	

	
	public Image getBallImage(int color) {
		return balls[color-1];
	}
	
	
	public static ResourceLoader getInstance() {
		return instance;
	}




	private static byte[] loadResource(String path) throws IOException {
		try (InputStream res = ResourceLoader.class.getResourceAsStream("/" + path) ) {
			if (res == null) {
				throw new IOException("Resource /resources/" + path + " not found");
			}
			return res.readAllBytes();
		}
	}
}
