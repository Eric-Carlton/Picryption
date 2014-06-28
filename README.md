Picryption
==========

Encodes message into, and decodes messages from, images supplied by the user

Usage
------

Image to encode must be no smaller than 250 X 250, although a larger image is highly recommended.

To encode a message into an image, open Picryption and press Encrypt.  On the next screen, enter the message that you want to encode.  The message must be no more than 140 characters.  In the case that a larger message is entered, Picryption will prompt you to either change the message or allow the program to only use the first 140 characters.

Select okay, then select the image file to encode the image in.  Picryption will check the size of the image.  If it is okay, Picryption will encode the message, and save back to the original file.

It is important to note that Picryption **overwrites** the original image.  If you would like to keep an unaltered copy, make a copy of the image **before** you encode an image into it.

To decode, open Picryption and selecte Decode.  You will be prompted to select an image to decode.  If a message is found in the image, it will be displayed.  Otherwise, you will be notified that no message could be found.

Encoding Method
----------------

The method used to encode a message into an image is pretty simple.  A random RGB value is chosen and the image is scanned to decide if that color is used anywhere in the image.  If it is, another random color is chosen.  The process continues until a color that is not used is found.

Next, the image is scanned, counting the appearances of each unique color.  This count is recorded.  This is important because the message will be encoded in a color that appears exactly 700 times, so it must be the only color to appear that many times.  This allows the decoder to identify where the message is encoded into the picture. If a color is found to appear exactly 700 times, one pixel of that color is altered, so that it only appears 699 times.  This continues until no color appears exactly 700 times.

The message entered by the user is then encoded into morse code - with a few extra mappings for punctuation and symbols.  Picryption places pixels of the encode color chosen earlier into the image, with the distance between them being equivalent to the int value of each character of the morse encoded message.  If the message is to encode is less than 700 morse characters, spaces are added at the end of the string that are cut off during the decoding process.

To decode, the image is scanned to find a color that appears 700 times.  The distances between the pixels are found, a string of morse characters is generated from that, and the morse string is decoded into letters using a HashMap.  

Credits
-------

Inspired by [this challenge from hackthissite.org](https://www.hackthissite.org/missions/prog/2/ "Programming Mission 2") 

