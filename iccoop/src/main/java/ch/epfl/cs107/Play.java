package ch.epfl.cs107;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.play.engine.Game;
import ch.epfl.cs107.play.io.DefaultFileSystem;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.ResourceFileSystem;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.window.swing.SwingWindow;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Main entry point.
 */
public class Play {

	public static void jouerMusiqueDeFond() {
		try {
			// Charger le fichier audio depuis les ressources
			URL resource = Play.class.getClassLoader().getResource("music/song.wav");

			if (resource == null) {
				throw new FileNotFoundException("Le fichier audio n'a pas été trouvé dans le répertoire resources");
			}

			File audioFile = new File(resource.toURI());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);

			// Démarre la lecture de la musique de fond en boucle
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
			System.out.println("Musique de fond lancée !");
		} catch (Exception e) {
			System.out.println("Erreur lors de la lecture de la musique : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** One second in nano second */
    private static final float ONE_SEC = 1E9f;
	public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 800;

	/**
	 * Main entry point.
	 * @param args (Array of String): ignored
	 */
	public static void main(String[] args) {
		//jouerMusiqueDeFond();
		// Define cascading file system
		final FileSystem fileSystem = new ResourceFileSystem(DefaultFileSystem.INSTANCE);

        // Create a demo game and initialize corresponding texts
		final Game game = new ICoop();

        // Use Swing display
		final Window window = new SwingWindow(game.getTitle(), fileSystem, WINDOW_WIDTH, WINDOW_HEIGHT);
		window.registerFonts(ResourcePath.FONTS);

		try {
			if (game.begin(window, fileSystem)) {
				// Use system clock to keep track of time progression
                long currentTime = System.nanoTime();
				long lastTime;
				final float frameDuration = ONE_SEC / game.getFrameRate();

				// Run until the user try to close the window
				while (!window.isCloseRequested()) {

					// Compute time interval
                    lastTime = currentTime;
                    currentTime = System.nanoTime();
					float deltaTime = (currentTime - lastTime);

                    try {
                        int timeDiff = Math.max(0, (int) (frameDuration - deltaTime));
                        Thread.sleep((int) (timeDiff / 1E6), (int) (timeDiff % 1E6));
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep interrupted");
                    }

                    currentTime = System.nanoTime();
                    deltaTime = (currentTime - lastTime) / ONE_SEC;

                    // Let the game do its stuff
                    game.update(deltaTime);

                    // Render and update input
					game.draw();
                    window.update();
				}
			}
			game.end();
		} finally {
			// Release resources
			window.dispose();
		}
	}
}
