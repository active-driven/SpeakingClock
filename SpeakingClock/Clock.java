package speakingClock;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author dream_tree
 * This class converts actual system time composed of digits into its word counterparts.
 * Polish language version.
 * String[] used.
 */

public class Clock {

	private final String[] HOURS = {"- północ, minut:", "pierwsza", "druga", "trzecia", "czwarta", "piąta", "szósta", "siódma", "ósma", 
									"dziewiąta", "dziesiąta", "jedenasta", "dwunasta", "trzynasta", "czternasta", "piętnasta", "szesnasta",
									"siedemnasta", "osiemnasta", "dziewiętnasta", "dwudziesta",
									"dwudziesta pierwsza", "dwudziesta druga", "dwudziesta trzecia"};
	private final String[] TENS_OF_MINUTES = {"zero", "", "dwadzieścia", "trzydzieści", "czterdzieści", "pięćdziesiąt"};
	private final String[] MINUTES = {"zero", "jeden", "dwa", "trzy", "cztery", "pięć", "sześć", "siedem", "osiem", "dziewięć", "dziesięć",
									"jedenaście", "dwanaście", "trzynaście", "czternaście", "piętnaście", "szesnaście", "siedemnaście",
									"osiemnaście", "dziewiętnaście", ""};
	
	private final String[] HOURS_WAV = {"0g.wav", "1g.wav", "2g.wav", "3g.wav", "4g.wav", "5g.wav", "6g.wav", "7g.wav", "8g.wav", "9g.wav",
									"10g.wav", "11g.wav", "12g.wav", "1g.wav", "14g.wav", "15g.wav", "16g.wav", "17g.wav", "18g.wav",
									"19g.wav", "20g.wav", "21g.wav", "22g.wav", "23g.wav"};
	private final String[] TENS_OF_MINUTES_WAV = {"cisza_kr.wav", "cisza_sr.wav", "20min.wav", "30min.wav", "40min.wav", "50min.wav"};
	private final String[] MINUTES_WAV = {"0m.wav", "1m.wav", "2m.wav", "3m.wav", "4m.wav", "5m.wav", "6m.wav", "7m.wav", "8m.wav",
									"9m.wav", "10m.wav", "11m.wav", "12m.wav", "13m.wav", "14m.wav", "15m.wav", "16m.wav", "17m.wav",
									"18m.wav", "19m.wav", ""};
	
	String actualTimeConverted;
	int extractedHours;
	int extractedTensOFMinutes;
	int extractedMinutes;
	 
	 public Clock() {
		 LocalTime actualTime = LocalTime.now();
		 this.actualTimeConverted = actualTime.toString();	
	 } 
	 
	 public int getExtractedTensOFMinutes() {
		 return extractedTensOFMinutes;
	 }
	 
	 public String getHOURS() {
		 return HOURS[extractedHours];
	 }
	 
	 public String getTENS_OF_MINUTES() {
		 return TENS_OF_MINUTES[extractedTensOFMinutes];
	 }
	 
	 public String getMINUTES() {
		 return MINUTES[extractedMinutes];
	 }
	 	 
	 public String getActualTimeConverted() {
		 return actualTimeConverted;
	 }
	 
	 /**
	  * Converts actual system time composed of digits into its word counterparts. 
	  */ 	 
	 public void convert()  {					
		extractedHours = Integer.parseInt(actualTimeConverted.substring(0,2));
		// minutes 0-9
		if (actualTimeConverted.charAt(3) == '0') {
			 extractedTensOFMinutes = 0;       
			 extractedMinutes = Integer.parseInt(actualTimeConverted.substring(4,5));
		// minutes 10-19	 
		} else if (actualTimeConverted.charAt(3) == '1') {	  
			 extractedTensOFMinutes = 0;                      // silence
			 extractedMinutes = Integer.parseInt(actualTimeConverted.substring(3,5));
		// minutes 20+ 
		} else {
			 extractedTensOFMinutes = Integer.parseInt(actualTimeConverted.substring(3,4));  
			 // avoiding displaying last "zero" in full minutes as in 20, 30 etc.
			 // extractedMinutes = 20; refers to empty String (in private final String[] TENS_OF_MINUTES)
			 if (actualTimeConverted.charAt(4) == '0') {                                       
				extractedMinutes = 20;											
			 } else {
				extractedMinutes = Integer.parseInt(actualTimeConverted.substring(4,5));
			 }
	    }
	}
	
	 /**
	  * Creates path to location of audio file in system file resources.
	  * @throws InterruptedException if when a thread is sleeping, 
	  * and the thread is interrupted, either before or during the activity.
	  */ 
     public void createPath() throws InterruptedException   {     
    
     	String path = "../vox/";	
     	String start = "../vox/jest godzina.wav";
 		String pathHours = path + HOURS_WAV[extractedHours];
 		String pathTensMinutes = path + TENS_OF_MINUTES_WAV[extractedTensOFMinutes];
 		String pathMinutes = path + MINUTES_WAV[extractedMinutes];   	 
    	    	 
		playAudioFile(start);
		Thread.sleep(1600);
		playAudioFile(pathHours);
		Thread.sleep(1000);
		playAudioFile(pathTensMinutes);
		Thread.sleep(1400);
		playAudioFile(pathMinutes);	
		Thread.sleep(1600);		
    }
			
	 /**
	  * Plays audio files.
	  * @param path location of audio file in system file resources.
	  */
     private static void playAudioFile(String path) {      
        try {
            File file = new File(path);
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        } catch(UnsupportedAudioFileException|IOException|LineUnavailableException e) {
			System.err.println("Error playing sound.");
            e.printStackTrace();
        }
    }  
 }	
