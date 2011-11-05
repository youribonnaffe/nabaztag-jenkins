import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Wrapping class for choreographies.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Ville Antila
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class Choreography
{
    /**
     * Buffer used to store choreography raw data (excluding header and footer)
     */
    private ByteArrayOutputStream data;

    /**
     * Bottom LED identifier.
     */
    public static final byte LED_BOTTOM = (byte) 0x00;

    /**
     * Left LED identifer.
     */
    public static final byte LED_LEFT = (byte) 0x01;

    /**
     * Center LED identifier.
     */
    public static final byte LED_CENTER = (byte) 0x02;

    /**
     * Right LED identifier.
     */
    public static final byte LED_RIGHT = (byte) 0x03;

    /**
     * Nose LED identifier.
     */
    public static final byte LED_NOSE = (byte) 0x04;

    /**
     * Right ear identifier.
     */
    public static final byte EAR_RIGHT = (byte) 0x00;

    /**
     * Left ear identifier.
     */
    public static final byte EAR_LEFT = (byte) 0x01;

    /**
     * Forward direction identifier.
     */
    public static final byte DIRECTION_FORWARD = (byte) 0x00;

    /**
     * Backward direction identifier.
     */
    public static final byte DIRECTION_BACKWARD = (byte) 0x01;

    /**
     * Name of the choreography.
     */
    private String name;

    /**
     * Creating an empty choreography, giving its name.
     * 
     * @param name the name of the choreography.
     */
    public Choreography(String name)
    {
	this.data = new ByteArrayOutputStream();
	this.name = name;

	// Initially, the choreography is not part of any library
    }

    /**
     * Getting the name of the choreography.
     * 
     * @return name the name of the choreography.
     */
    public String getName()
    {
	return this.name;
    }


    /**
     * Setting choreography data.
     * 
     * @param dataBytes choreography raw data, excluding header and footer.
     */
    public void setData(byte[] dataBytes)
    {
	this.data.reset();
	try
	{
	    this.data.write(dataBytes);
	}
	catch (IOException e)
	{
	    // TODO maybe this method should return a boolean indicating if data could be written or not
	    this.data.reset();
	}
    }

    /**
     * Getting choreography data.
     * 
     * @return choreography raw data, excluding header and footer.
     */
    public byte[] getData()
    {
	return this.data.toByteArray();
    }

    /**
     * Appending a "tempo command" to choreography data.
     * 
     * @param time the delay after the previous command.
     * @param frequencyRatio the tempo frequency ratio, divider of 100 Hz (e.g. if frequency ratio is 1, frequency is 100 Hz, and if
     *            frequency ration is 10, frequency is 10 Hz).
     */
    public void addTempoCommand(int time, int frequencyRatio)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0x01);
	this.data.write((byte) frequencyRatio);
    }

    /**
     * Appending a "LED color" command to choreography data.
     * 
     * @param time the delay after the previous command.
     * @param led the identifier of the LED to set.
     * @param r red component value.
     * @param g green component value.
     * @param b blue component value.
     */
    public void addLedColorCommand(int time, byte led, int r, int g, int b)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0x07);
	this.data.write(led);
	this.data.write((byte) r);
	this.data.write((byte) g);
	this.data.write((byte) b);
	this.data.write((byte) 0x00);
	this.data.write((byte) 0x00);
    }

    public void addLedsColorCommand(int time, int r, int g, int b)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0x09);
	this.data.write((byte) r);
	this.data.write((byte) g);
	this.data.write((byte) b);
	this.data.write((byte) 0x00);
	this.data.write((byte) 0x00);
    }


    /**
     * Appending an "absolute ear move" command to choreography data.
     * 
     * @param time the delay after the previous command
     * @param ear the identifier of the ear to move.
     * @param pos the position to reach with the ear.
     * @param direction the direction taken by the ear (backward, forward).
     */
    public void addAbsoluteEarMoveCommand(int time, byte ear, int pos, byte direction)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0x08);
	this.data.write(ear);
	this.data.write((byte) pos);
	this.data.write(direction);
    }

    /**
     * Appending a "relative ear move" command to choreography data.
     * 
     * @param time the delay after the previous command.
     * @param ear the identifier of the ear to move.
     * @param steps the number of steps.
     */
    public void addRelativeEarMoveCommand(int time, byte ear, int steps)
    {
	// TODO checking in the protocol if a direction has to be given ?
	this.data.write((byte) time);
	this.data.write((byte) 0x11);
	this.data.write(ear);
	this.data.write((byte) steps);
    }

    /**
     * Appending a "LED palette" command to choreography data. This command is used to set LED color to a value that has been specified in
     * MessageBlock with PL or CL command.
     * 
     * @param time the delay after the previous command.
     * @param led the identifier of the LED to set.
     * @param value the value to take (0 : CL, 1-7 : PL)
     */
    public void addLedPaletteCommand(int time, byte led, int value)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0xE);
	this.data.write(led);
	this.data.write((byte) (240 + value));
    }

    /**
     * Appending a "play random midi music" command to choreography data.
     * 
     * @param time the delay after the previous command.
     */
    public void addPlayRandomMidi(int time)
    {
	this.data.write((byte) time);
	this.data.write((byte) 0x10);
    }

    /**
     * N.B. Choreographies are equal if their names are the same.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
	Choreography choreography = null;
	try
	{
	    choreography = (Choreography) o;
	}
	catch (ClassCastException e)
	{
	    return false;
	}
	return this.name.equals(choreography.name);
    }

}
