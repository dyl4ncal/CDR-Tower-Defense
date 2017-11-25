/**
*@Author Raymond_Strohschein
*/

//Death sound is not my own, but Roblox's death noise 

package music;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;
import java.io.File;

public class BackGroundMusic
{
	private Clip clip;
	private AudioInputStream inputStream;
	private Random random = new Random();
	private String[] bgm;

	public BackGroundMusic() throws Exception
 	{
 		File dir = new File("music/bgm/");
        bgm = dir.list();
 	}

 	public void play() throws Exception
 	{
 		//Now it can play random death noises
 		int r = random.nextInt(bgm.length);
 		String in = "music/bgm/" + bgm[r];
 		File input = new File(in);
 		//System.out.println(in);
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