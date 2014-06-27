//Eric Carlton
//Picryption
//06.27.2014

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PicryptionMenu {

	public static void main(String[] args){
		//show menu to choose encryption or decryption
		Object[] options = {"Encrypt","Decrypt"};
		int n = JOptionPane.showOptionDialog(null, "What would you like to do?", "Picryption", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		switch(n){
		case 0:
			showEncryptionMenu("");
			break;
		case 1:
			showDecryptionMenu();
			break;
		default:
			break;
		}
	}

	public static void showEncryptionMenu(String initialMessage){
		//message to hide in the image
		String message = (String)JOptionPane.showInputDialog(
				null,
				"Type the message you want to encrypt:\n"
						+ "You are limited to 140 characters",
						"Picryption",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						initialMessage);
		if(message != null && message.trim().length() > 0)
			pickImageToEncrypt(message);

	}
	
	//get file that may contain a hidden message
	public static void showDecryptionMenu(){
		//limit to .jpg, .jpeg, and .png files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
		int result = fileChooser.showOpenDialog( null );
		if ( result == JFileChooser.APPROVE_OPTION ){
			File fileName = fileChooser.getSelectedFile();
			if ( ( fileName == null ) || ( fileName.getName().equals( "" ) ) )
			{
				JOptionPane.showMessageDialog( null, "Invalid File Name",
						"Invalid File Name", JOptionPane.ERROR_MESSAGE );
			}
			else{
				//if everything looks good, try to find the hidden message
				ImageDecryptor decryptor = new ImageDecryptor(fileName);
				decryptImage(decryptor);}
		} 
		//cancel button pressed, close application
		else System.exit(0);
		
		
	}

	//pick an image to hide the message in
	public static void pickImageToEncrypt(String message){
		if(message.length() > 140){
			int choice = JOptionPane.showConfirmDialog(null, "Only the first 140 characters will be taken.  Is that okay?", "Picryption", JOptionPane.ERROR_MESSAGE);
			switch(choice){
			case JOptionPane.YES_OPTION:
				message = message.substring(0, 140);
				break;
			case JOptionPane.NO_OPTION:
				showEncryptionMenu(message);
				break;
			default:
				break;
			}
		}

		
		//limit to .jpg, .jpeg, and .png files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int result = fileChooser.showOpenDialog( null );
		if ( result == JFileChooser.APPROVE_OPTION ){
			File fileName = fileChooser.getSelectedFile();
			if ( ( fileName == null ) || ( fileName.getName().equals( "" ) ) )
			{
				JOptionPane.showMessageDialog( null, "Invalid File Name",
						"Invalid File Name", JOptionPane.ERROR_MESSAGE );
			}
			else{
				//if everything looks good, try to hide the message in the image
				ImageEncryptor encryptor = new ImageEncryptor(fileName, message);

				encryptImage(encryptor);}
		} 
		//cancel button pressed, close application
		else System.exit(0);

		
	}
	
	public static void decryptImage(ImageDecryptor decryptor){
		//image successfully retrieved by decryptor, find and disply message
		if(decryptor.image != null){
			JOptionPane.showMessageDialog(null, "The decrypted message is: \n" + decryptor.decryptImage(), "Picryption", JOptionPane.DEFAULT_OPTION);
		}
		else{
			JOptionPane.showMessageDialog(null, "Your image file couldn't be loaded!", "Picryption", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void encryptImage(ImageEncryptor encryptor){
		//encryptor successfully found image
		//hide the message in the image and save the image back
		if(encryptor.image != null)
			saveImage(encryptor.encryptImage(), encryptor.file);
		//couldn't load the image. Try to get another one
		else{
			JOptionPane.showMessageDialog(null, "Your image file couldn't be loaded!", "Picryption", JOptionPane.ERROR_MESSAGE);
			pickImageToEncrypt(encryptor.message);
		}
	}

	public static void saveImage(BufferedImage toSave, File file){
		//try to save image with message hidden in it to file
		try {
			ImageIO.write(toSave, "png", file);
			JOptionPane.showMessageDialog(null, "Picryption successful! Image has been saved back to its original location!", "Picryption", JOptionPane.DEFAULT_OPTION);
		}
		//if unsuccessful, let the user know
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Picryption failed.  File could not be saved.", "Picryption", JOptionPane.ERROR_MESSAGE);
		}
	}

}
