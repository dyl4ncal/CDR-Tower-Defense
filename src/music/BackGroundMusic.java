/**
 * This defines music that can be played during gameplay
 * Possible to add music by adding to bgm
 */

package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Random;

public class BackgroundMusic
{
	private Clip clip;
	private AudioInputStream inputStream;
	private Random random = new Random();
	private String[] bgm;

	public BackgroundMusic() throws Exception
 	{
 		File dir = new File("music/bgm/");
        bgm = dir.list();
 	}

 	public void play() throws Exception
 	{
 		//Now it can play random music
 		int r = random.nextInt(bgm.length);
 		String in = "music/bgm/" + bgm[r];
 		File input = new File(in);
  		inputStream = AudioSystem.getAudioInputStream(input);
  		clip = AudioSystem.getClip();
 		clip.open(inputStream);
 		clip.loop(Clip.LOOP_CONTINUOUSLY);
 	}

 	public void stop()
 	{
 		clip.close();
 	}
}