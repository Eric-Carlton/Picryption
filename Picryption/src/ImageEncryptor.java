//Eric Carlton
//Picryption
//06.27.2014

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class ImageEncryptor {

	//Image to hide message in
	BufferedImage image;
	//location of image
	File file;
	//message to in image
	String message;
	//how many times each existing color appears in the image
	HashMap<Color, Integer> colorAppearances;
	//Handles encoding to morse code
	HashMap<Character, String> morseEncoder;

	public ImageEncryptor(File image, String message){
		this.file = image;
		this.message = message.toUpperCase();
		this.image = null;
		colorAppearances = new HashMap<Color,Integer>();
		morseEncoder = new HashMap<Character,String>();
		//try to retrieve image from location specified
		try {
			this.image = ImageIO.read(file);
			if(this.image.getHeight() < 256 || this.image.getWidth() < 256){
				JOptionPane.showMessageDialog(null, "Image must be 256 X 256 or larger for operation to succeed.", "Picryption", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
				
		}catch(Exception e){

		}
		initializeMorseEncoder();
	}

	public BufferedImage encryptImage(){
		Color encodeColor = null;
		//find a color that does not appear in the image
		while(encodeColor == null)
			encodeColor = scanImageForUnusedColor();
		//get a count of all colors in image
		countColors();
		/*
		 * The encode color will appear exactly 700 times.  This allows the decryptor
		 * to easily find it.  If any other color appears exactly 700 times, one pixel 
		 * of that color needs to be changed so that only the encode will appear exactly 700 times.
		 * */
		while(colorAppearances.containsValue(700)){
			changeNecessaryColors();
			countColors();
		}
		//encode message to morse
		String encodedMessage = encodeMessage();
		BufferedImage picryption = picryptMessage(encodedMessage, encodeColor);
		return picryption;
	}

	private Color scanImageForUnusedColor(){
		//generate a random color
		Color attempt = new Color(getRandomIntInRange(0,255),getRandomIntInRange(0,255),getRandomIntInRange(0,255));
		//search the image for the generated color
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color  clr   = new Color(image.getRGB(x, y)); 
				if(attempt.equals(clr))
					//return null if the image contains the random color
					return null;
			}
		}
		//if the image doesn't contain the random color, it can be used
		return attempt;
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

	private void changeNecessaryColors(){
		Set<Color> keys = new HashSet<Color>();
		//get all colors that appear exactly 700 times
		for(Map.Entry<Color,Integer> entry: colorAppearances.entrySet()){
			if(entry.getValue() == 700)
				keys.add(entry.getKey()); //no break, looping entire hashtable
		}

		//find the first occurrence of any color that appears exactly 700 times
		//and change it slightly
		for(Color color : keys){
			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					Color  clr   = new Color(image.getRGB(x, y)); 
					if(color.equals(clr)){
						Color changeTo = new Color(color.getRed()+1, color.getGreen(),color.getBlue());
						image.setRGB(x, y, changeTo.getRGB());
						break;
					}
				}
			}
		}
	}

	private void initializeMorseEncoder(){
		morseEncoder.put('A', ".-");
		morseEncoder.put('B', "-...");
		morseEncoder.put('C', "-.-.");
		morseEncoder.put('D', "-..");
		morseEncoder.put('E', ".");
		morseEncoder.put('F', "..-.");
		morseEncoder.put('G', "--.");
		morseEncoder.put('H', "....");
		morseEncoder.put('I', "..");
		morseEncoder.put('J', ".---");
		morseEncoder.put('K', "-.-");
		morseEncoder.put('L', ".-..");
		morseEncoder.put('M', "--");
		morseEncoder.put('N', "-.");
		morseEncoder.put('O', "---");
		morseEncoder.put('P', ".--.");
		morseEncoder.put('Q', "--.-");
		morseEncoder.put('R', ".-.");
		morseEncoder.put('S', "...");
		morseEncoder.put('T', "-");
		morseEncoder.put('U', "..-");
		morseEncoder.put('V', "...-");
		morseEncoder.put('W', ".--");
		morseEncoder.put('X', "-..-");
		morseEncoder.put('Y', "-.--");
		morseEncoder.put('Z', "--..");
		morseEncoder.put('1', ".----");
		morseEncoder.put('2', "..---");
		morseEncoder.put('3', "...--");
		morseEncoder.put('4', "....-");
		morseEncoder.put('5', ".....");
		morseEncoder.put('6', "-....");
		morseEncoder.put('7', "--...");
		morseEncoder.put('8', "---..");
		morseEncoder.put('9', "----.");
		morseEncoder.put('0', "-----");
		morseEncoder.put('!', "---.-");
		morseEncoder.put('@', "--.--");
		morseEncoder.put('#', "--.-.");
		morseEncoder.put('$', "--..-");
		morseEncoder.put('%', "-.---");
		morseEncoder.put('^', "-.--.");
		morseEncoder.put('&', "-.-.-");
		morseEncoder.put('*', "-.-..");
		morseEncoder.put('(', "-..--");
		morseEncoder.put(')', "-..-.");
		morseEncoder.put('-', "-...-");
		morseEncoder.put('_', ".---.");
		morseEncoder.put('=', ".--.-");
		morseEncoder.put('+', ".--..");
		morseEncoder.put(';', ".-.--");
		morseEncoder.put(':', ".-.-.");
		morseEncoder.put('\'', ".-..-");
		morseEncoder.put('"', ".-...");
		morseEncoder.put(',', "..--.");
		morseEncoder.put('.', "..-.-");
		morseEncoder.put('?', "..-..");
		morseEncoder.put(' ', "...-.");

	}

	private int getRandomIntInRange(int max, int min){
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	//translate all characters in message to morse code
	private String encodeMessage(){
		String encoded = "";
		for(int i=0; i<message.length(); i++){
			char cur = message.charAt(i);
			encoded = encoded+morseEncoder.get(cur)+" ";
		}
		return encoded;
	}

	private BufferedImage picryptMessage(String msg, Color clr){
		
		//make message 700 characters long
		for(int i = msg.length(); i < 700; i++)
			msg = msg + " ";

		BufferedImage result = image;
		
		//figure out how to convert the x,y coordinates to one number
		int smaller = Math.min(image.getWidth(), image.getHeight());
		int length = (int)(Math.log10(smaller)+1);
		int carriage = (int)(Math.pow(10, length-1));
		
		int offset=0;
		int x=0;
		int y=0;
		
		//loop through every character in message
		for(int i=0;i<msg.length();i++){
			//add up numerical representation of the x,y
			//coordinates where the next pixel should be placed
			offset = offset+msg.charAt(i);

			//break the numerical representation into x,y coordinates
			if(image.getWidth() > image.getHeight()){
				y = offset%carriage;
				x = offset/carriage;
			}
			else{
				y = offset/carriage;
				x = offset%carriage;
			}
			
			//place the pixel
			result.setRGB(x, y, clr.getRGB());
		}
		return result;
	}
}
