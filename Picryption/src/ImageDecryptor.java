//Eric Carlton
//Picryption
//06.27.2014

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class ImageDecryptor {

	//An image that contains a hidden message
	BufferedImage image;
	//records how many times each color appears in the image
	HashMap<Color, Integer> colorAppearances;
	//translates morse into English characters
	HashMap<String, Character> morseDecoder;
	//list of locations of coded pixels
	ArrayList<Integer> decodeColorPixelPositions;
	//list of distances between each coded pixel
	ArrayList<Character> distancesBetweenRelevantPixels;

	public ImageDecryptor(File file){
		this.image = null;
		colorAppearances = new HashMap<Color,Integer>();
		morseDecoder = new HashMap<String,Character>();
		decodeColorPixelPositions = new ArrayList<Integer>();
		distancesBetweenRelevantPixels = new ArrayList<Character>();
		//try to load the image specified
		try {
			this.image = ImageIO.read(file);
		}catch(Exception e){

		}
		initializeMorseDecoder();
	}

	public String decryptImage(){
		//get a count of all colors in the image
		countColors();
		//the encoded color is the only one that should appear exactly 700 times
		if(!colorAppearances.containsValue(700))
		{
			//if no color appears 700 times, the picture doesn't have a message
			JOptionPane.showMessageDialog(null, "Cannot find message!", "Picryption", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		//find out what color is the color to be used for decoding
		Color decodeColor = getDecodeColor();
		if(decodeColor == null){
			JOptionPane.showMessageDialog(null, "Unable to decode message!", "Picryption", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		//find positions of all pixels of the decode color
		getPositionsOfPixelsWithColor(decodeColor);
		//find distance between pixels of decode color
		getOffsets();
		//convert offsets to a string of characters
		String morseSequence = getStringRepresentation();
		//return the decoded message
		return decodeMessage(morseSequence).trim();
	}

	private String decodeMessage(String msg){
		String decoded = "";
		//a space represents the end of a letter, 
		//so each index will contain the morse characters
		//that correspond to one letter
		String[] letters = msg.split(" ");
		//run each morse character through the decoder
		//to piece together the hidden message
		for(String curStr : letters){
			if(morseDecoder.get(curStr) != null)
				decoded += morseDecoder.get(curStr);
			else
				decoded += " ";
		}

		return decoded;
	}

	private String getStringRepresentation()
	{    
		//piece together a string from an arraylist of characters
		StringBuilder builder = new StringBuilder(distancesBetweenRelevantPixels.size());
		for(Character ch: distancesBetweenRelevantPixels)
		{
			builder.append(ch);
		}
		return builder.toString().trim();
	}

	//get the space between relevant pixels
	private void getOffsets(){
		int cur;
		int prev;
		int offset;
		char intToChar;
		for(int i=0;i<decodeColorPixelPositions.size();i++){
			//first pixel is actually the first number to record
			if(i == 0){
				offset = decodeColorPixelPositions.get(i);

			}
			//otherwise record offset
			else{
				cur = decodeColorPixelPositions.get(i);
				prev = decodeColorPixelPositions.get(i-1);
				offset = cur - prev;
			}
			//convert distances to their character equivalent
			intToChar = (char)offset;
			distancesBetweenRelevantPixels.add(intToChar);
		}
	}

	private void getPositionsOfPixelsWithColor(Color clr){

		//figure out the appropriate carriage for the image
		int smaller = Math.min(image.getWidth(), image.getHeight());
		int length = (int)(Math.log10(smaller)+1);
		int carriage = (int)(Math.pow(10, length-1));

		//the encoding process is different depending on whether the or not 
		//the picture is longer than wide or wider than long.  The decoding 
		//process matches that
		if(image.getHeight() > image.getWidth()){
			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					int  intCol   = image.getRGB(x, y);
					Color curClr = new Color(intCol);

					//if the pixel is the same color as the decode color
					//store the position
					if(curClr.equals(clr))
						decodeColorPixelPositions.add(carriage*y+x);
				}
			}
		}
		else{
			for (int y = 0; y < image.getWidth(); y++) {
				for (int x = 0; x < image.getHeight(); x++) {
					int  intCol   = image.getRGB(y, x);
					Color curClr = new Color(intCol);

					//if the pixel is the same color as the decode color
					//store the position
					if(curClr.equals(clr))
						decodeColorPixelPositions.add(carriage*y+x);
				}
			}
		}
	}

	//find the key of the value 700 in colorAppearances
	private Color getDecodeColor(){
		for (java.util.Map.Entry<Color, Integer> entry : colorAppearances.entrySet()) {
			if (entry.getValue().equals(700)) {
				return entry.getKey();
			}
		}
		return null;
	}


	private void countColors(){
		//loop through pixels in image, adding each unique color as 
		//a key in colorAppearances.  If the key already exists,
		//increment its count
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color  clr   = new Color(image.getRGB(x, y)); 
				if(colorAppearances.containsKey(clr)){
					Integer seen = colorAppearances.get(clr);
					seen += 1;
					colorAppearances.put(clr, seen);
				}
				else
					colorAppearances.put(clr, 1);	
			}
		}
	}

	private void initializeMorseDecoder(){
		morseDecoder.put(".-", 'A');
		morseDecoder.put( "-...",'B');
		morseDecoder.put( "-.-.",'C');
		morseDecoder.put( "-..",'D');
		morseDecoder.put( ".",'E');
		morseDecoder.put( "..-.",'F');
		morseDecoder.put( "--.",'G');
		morseDecoder.put( "....",'H');
		morseDecoder.put( "..",'I');
		morseDecoder.put( ".---",'J');
		morseDecoder.put( "-.-",'K');
		morseDecoder.put( ".-..",'L');
		morseDecoder.put( "--",'M');
		morseDecoder.put( "-.",'N');
		morseDecoder.put( "---",'O');
		morseDecoder.put( ".--.",'P');
		morseDecoder.put( "--.-",'Q');
		morseDecoder.put( ".-.",'R');
		morseDecoder.put( "...",'S');
		morseDecoder.put( "-",'T');
		morseDecoder.put( "..-",'U');
		morseDecoder.put( "...-",'V');
		morseDecoder.put( ".--",'W');
		morseDecoder.put( "-..-",'X');
		morseDecoder.put( "-.--",'Y');
		morseDecoder.put( "--..",'Z');
		morseDecoder.put( ".----",'1');
		morseDecoder.put( "..---",'2');
		morseDecoder.put( "...--",'3');
		morseDecoder.put( "....-",'4');
		morseDecoder.put( ".....",'5');
		morseDecoder.put( "-....",'6');
		morseDecoder.put( "--...",'7');
		morseDecoder.put( "---..",'8');
		morseDecoder.put( "----.",'9');
		morseDecoder.put( "-----",'0');
		morseDecoder.put( "---.-",'!');
		morseDecoder.put( "--.--",'@');
		morseDecoder.put( "--.-.",'#');
		morseDecoder.put( "--..-",'$');
		morseDecoder.put( "-.---",'%');
		morseDecoder.put( "-.--.",'^');
		morseDecoder.put( "-.-.-",'&');
		morseDecoder.put( "-.-..",'*');
		morseDecoder.put( "-..--",'(');
		morseDecoder.put( "-..-.",')');
		morseDecoder.put( "-...-",'-');
		morseDecoder.put( ".---.",'_');
		morseDecoder.put( ".--.-",'=');
		morseDecoder.put( ".--..",'+');
		morseDecoder.put( ".-.--",';');
		morseDecoder.put( ".-.-.",':');
		morseDecoder.put( ".-..-",'\'');
		morseDecoder.put( ".-...",'"');
		morseDecoder.put( "..--.",',');
		morseDecoder.put( "..-.-",'.');
		morseDecoder.put( "..-..",'?');
		morseDecoder.put( "...-.",' ');

	}
}
