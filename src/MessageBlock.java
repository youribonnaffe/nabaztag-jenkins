import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Class wrapping a message block.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class MessageBlock extends Block
{
    /**
     * Precalculated table of the inverse (modulo 256) of (2x+1) for x=0 to 127
     */
    private final static int[] INVTABLE = new int[] { 1, 171, 205, 183, 57, 163, 197, 239, 241, 27, 61, 167, 41, 19, 53, 223, 225, 139,
	    173, 151, 25, 131, 165, 207, 209, 251, 29, 135, 9, 243, 21, 191, 193, 107, 141, 119, 249, 99, 133, 175, 177, 219, 253, 103,
	    233, 211, 245, 159, 161, 75, 109, 87, 217, 67, 101, 143, 145, 187, 221, 71, 201, 179, 213, 127, 129, 43, 77, 55, 185, 35, 69,
	    111, 113, 155, 189, 39, 169, 147, 181, 95, 97, 11, 45, 23, 153, 3, 37, 79, 81, 123, 157, 7, 137, 115, 149, 63, 65, 235, 13,
	    247, 121, 227, 5, 47, 49, 91, 125, 231, 105, 83, 117, 31, 33, 203, 237, 215, 89, 195, 229, 15, 17, 59, 93, 199, 73, 51, 85, 255 };

    /**
     * The text commands of the block.
     */
    private String commands;

    /**
     * Creating a new message block instance.
     * 
     * @param id The id of the block.
     */
    public MessageBlock(int id)
    {
	super(Block.MESSAGE_BLOCK_TYPE, (byte) 0, null);
	this.commands = "ID " + id + "\n";
    }

    /**
     * Creating a new message block instance with existing data.
     * 
     * @param size the size of the data.
     * @param data the data.
     */
    public MessageBlock(int size, byte[] data)
    {
	super(Block.MESSAGE_BLOCK_TYPE, size, data);
    }

    /**
     * Appending a "playing sound file from any url" command to the block.
     * 
     * @param url the URL of the sound file to play.
     */
    public void addPlaySoundCommand(String url)
    {
	this.commands += "MU " + url + "\n";
    }

    /**
     * Appending a "playing local sound file " command to the block.
     * 
     * @param url the local URL (to the server) of the sound file to play.
     */
    public void addPlayLocalSoundCommand(String url)
    {
	this.commands += "MU broadcast/" + url + "\n";
    }

    /**
     * Appending a "playing stream" command to the block.
     * 
     * @param url the URL of the stream.
     */
    public void addPlayStreamCommand(String url)
    {
	this.commands += "ST " + url + "\n";
    }

    /**
     * Appending a "playing choreography from any url" command to the block.
     * 
     * @param url the URL of the choreography to play.
     */
    public void addPlayChoreographyCommand(String url)
    {
	this.commands += "CH " + url + "\n";
    }

    /**
     * Appending a "playing local choreography" command to the block.
     * 
     * @param url the local URL (to the server) of the choreography to play.
     */
    public void addPlayLocalChoreographyCommand(String url)
    {
	this.commands += "CH broadcast/" + url + "\n";
    }

    /**
     * Appending a "playing local choreography from library" command to the block.
     * 
     * @param name the name of the choreography to play.
     */
    public void addPlayChoreographyFromLibraryCommand(String name)
    {
	this.commands += "CH broadcast/chorlibrary/" + name + "\n";
    }

    /**
     * Appending a "setting palette" command to the block.
     * 
     * @param pl the palette to select.
     */
    public void addSetPaletteCommand(int pl)
    {
	if (pl < 1 || pl > 7) return;
	this.commands += "PL " + pl + "\n";
    }

    /**
     * Appending a "setting preset color" command to the block.
     * 
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     */
    public void addSetPresetColorCommand(int r, int g, int b)
    {
	if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) return;

	this.commands += "CL " + (b + (g * 255) + (r * 255 * 255)) + "\n";
    }

    /**
     * Appending a "waiting for previous command to end" command to the block.
     * 
     */
    public void addWaitPreviousEndCommand()
    {
	this.commands += "MW\n";
    }

    /**
     * Encoding the block using Violet's obfuscation algorithm.
     */
    protected void encodeBlock()
    {
	this.encode(this.commands);
    }

    /**
     * @see jNab.core.protocol.Block#getData()
     */
    public byte[] getData()
    {
	this.encodeBlock();
	return super.getData();
    }

    /**
     * Desobfuscating data stored in the block.
     * 
     * @return the decoded data.
     */
    public byte[] decode()
    {
	byte[] chars = new byte[this.size];

	char currentChar = 35;

	for (int i = 1; i < this.data.length; i++)
	{
	    char code = (char) this.data[i];
	    currentChar = (char) (((code - 47) * (1 + 2 * currentChar)) % 256);
	    chars[i] = (byte) currentChar;
	}

	return chars;
    }

    /**
     * Obfuscating and preparing the commands to be sent.
     * 
     * @param text the commands to send.
     */
    private void encode(String text)
    {
	// TODO checking in specs if the message commands are necessary ISO-8859-1 rather than US-ASCII

	// Create the encoder and decoder for ISO-8859-1
	Charset charset = Charset.forName("ISO-8859-1");
	CharsetDecoder decoder = charset.newDecoder();
	CharsetEncoder encoder = charset.newEncoder();

	String newData = null;

	try
	{
	    // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
	    // The new ByteBuffer is ready to be read.
	    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));

	    // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character
	    // ByteBuffer and then to a string.
	    // The new ByteBuffer is ready to be read.
	    CharBuffer cbuf = decoder.decode(bbuf);
	    newData = cbuf.toString();

	}
	catch (CharacterCodingException e)
	{
	    // Ignoring the exception
	}

	this.size = newData.length() + 1;
	this.data = new byte[this.size];

	this.data[0] = 1;

	int previousChar = 35;
	char currentChar;
	int code;

	for (int i = 0; i < newData.length(); i++)
	{
	    currentChar = newData.charAt(i);
	    code = ((INVTABLE[previousChar % 128] * currentChar + 47) % 256);
	    previousChar = currentChar;
	    this.data[i + 1] = (byte) code;
	}
    }

    /**
     * @see jNab.core.protocol.Block#toString()
     */
    public String toString()
    {
	String old = super.toString();
	old += "[MessageBlock data: " + this.commands + "]\n";
	return old;
    }
}
