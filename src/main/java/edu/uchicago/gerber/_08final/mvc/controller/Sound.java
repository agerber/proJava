package edu.uchicago.gerber._08final.mvc.controller;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	//for sound playing. Limit the number of threads to 5 at a time.
	private static final ThreadPoolExecutor soundExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

	//A Looped thread is one that plays for an indefinite time until
	// you call the .stopSound() method.
	public static final Map<String, Clip> LOOP_SOUNDS;

	// Load all looping sounds in the static context.
	static {
		Path rootDirectory = Paths.get("src/main/resources/sounds");
		Map<String, Clip> localMap = null;
		try {
			localMap = loadLoopedSounds(rootDirectory);
		} catch (IOException e) {
			e.fillInStackTrace();
			throw new ExceptionInInitializerError(e);
		}
		LOOP_SOUNDS = localMap;

	}

	private static Map<String, Clip> loadLoopedSounds(Path rootDirectory) throws IOException {
		Map<String, Clip> soundClips = new HashMap<>();
		Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (file.toString().toLowerCase().endsWith("_loop.wav")) {
					try {
						Clip clip = getLoopClip(file);
						if (clip != null) {
							soundClips.put(file.getFileName().toString(), clip);
						}
					} catch (Exception e) {
						e.fillInStackTrace();
					}
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				exc.fillInStackTrace();
				return FileVisitResult.CONTINUE;
			}
		});
		return soundClips;
	}

	private static Clip getLoopClip(Path fileName) throws Exception {
		Clip clip = null;
		try {
			// Adjust the path to be relative to the resources directory
			String relativePath = "/sounds/" + fileName.getFileName().toString();
			InputStream audioSrc = Sound.class.getResourceAsStream(relativePath);

			if (audioSrc == null) {
				throw new IOException("No such sound file exists at " + relativePath);
			}

			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream aisStream = AudioSystem.getAudioInputStream(bufferedIn);
			clip = AudioSystem.getClip();
			clip.open(aisStream);

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.fillInStackTrace();
			throw e;
		}

		return clip;
	}

	public static void stopSound(final String strPath) {
		//non-looped sounds can not be 'stopped', they simply expire.
		if (strPath.contains("_loop")) {
			LOOP_SOUNDS.get(strPath).stop();
		}
	}


	//Sounds that can be played simultaneously, must be queued onto the soundExecutor at runtime.
	public static void playSound(final String strPath) {
		if (strPath.contains("_loop")){
			LOOP_SOUNDS.get(strPath).loop(Clip.LOOP_CONTINUOUSLY);
			return;
		}

		soundExecutor.execute(new Runnable() {
			public void run() {
				try {
					Clip clp = AudioSystem.getClip();

					InputStream audioSrc = Sound.class.getResourceAsStream("/sounds/" + strPath);
                    assert audioSrc != null;
                    InputStream bufferedIn = new BufferedInputStream(audioSrc);
					AudioInputStream aisStream = AudioSystem.getAudioInputStream(bufferedIn);

					clp.open(aisStream);
					clp.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});

	}



}
