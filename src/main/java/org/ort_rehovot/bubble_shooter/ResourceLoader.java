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
	
	private final Image [] balls;
	@Getter
	private final Image bgImage;
	@Getter
	private final Image arrow;
	@Getter
	private final Image winImage;

	@Getter
	private final Clip boom;

	@Getter
	private final Clip backgroundMusic;

	private ResourceLoader() throws IOException{

		bgImage = new ImageIcon(ResourceLoader.loadResource("bg.png")).getImage();
		winImage = new ImageIcon(ResourceLoader.loadResource("win2.jpg")).getImage();
		balls = new Image[NUM_BALLS];
		for (int i=1; i<=NUM_BALLS; i++) {
			balls[i-1] = new ImageIcon(ResourceLoader.loadResource("Ball"+i+".png")).getImage();
		}
		arrow = new ImageIcon(ResourceLoader.loadResource("arrow.png")).getImage();

		boom = loadMusicResource("boom.wav");

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
		//try (InputStream res = ResourceLoader.class.getResourceAsStream("/resources/" + path) ) {
		try (InputStream res = ResourceLoader.class.getResourceAsStream("/" + path) ) {
			if (res == null) {
				throw new IOException("Resource /resources/" + path + " not found");
			}
			return res.readAllBytes();
		}
	}
}
