/**
 * This defines a sound that can be played when an enemy dies
 * Possible to add sounds by adding them to deathsounds
 *
 * Death sound is not our own, but Roblox's death noise
 */

package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Random;

public class DeathSound
{
	private Clip clip;
	private AudioInputStream inputStream;
	private Random random = new Random();
	private String[] noises;

	public DeathSound() throws Exception
 	{
 		File dir = new File("music/deathsounds/");
        noises = dir.list();
 	}

 	public void play() throws Exception
 	{
 		//Now it can play random death noises
 		int r = random.nextInt(noises.length);
 		String in = "music/deathsounds/" + noises[r];
 		File input = new File(in);
 		//System.out.println(in);
  		inputStream = AudioSystem.getAudioInputStream(input);
  		clip = AudioSystem.getClip();
 		clip.open(inputStream);
 		clip.loop(0);
 	}

 	public void stop()
 	{
 		clip.close();
 	}
}
