/**
 * Generic class wrapping a block.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class Block
{
    /**
     * Identifier for ping interval block.
     */
    public final static byte PING_INTERVAL_BLOCK_TYPE = (byte) 0x03;

    /**
     * Identifier for ambient block.
     */
    public final static byte AMBIENT_BLOCK_TYPE = (byte) 0x04;

    /**
     * Identifier for reboot block.
     */
    public final static byte REBOOT_BLOCK_TYPE = (byte) 0x09;

    /**
     * Identifier for message block.
     */
    public final static byte MESSAGE_BLOCK_TYPE = (byte) 0x0A;

    /**
     * Type of the block :<br/>
     * <ul>
     * <li>03 : {@link PingIntervalBlock}</li>
     * <li>04 : {@link AmbientBlock}</li>
     * <li>09 : {@link RebootBlock}</li>
     * <li>10 : {@link MessageBlock}</li>
     * </ul>
     */
    protected byte type;

    /**
     * Size of the block.
     */
    protected int size;

    /**
     * Internal array of bytes representing the block data.
     */
    protected byte[] data;

    /**
     * Creating a new block instance.
     * 
     * @param type the type of the block.
     */
    public Block(byte type)
    {
	this.type = type;
    }

    /**
     * Creating a new block instance from pre-existing data.
     * 
     * @param type the type of the block.
     * @param size the size of the block.
     * @param data the data associated to the block.
     */
    public Block(byte type, int size, byte[] data)
    {
	this.type = type;
	this.size = size;
	this.data = data;
    }

    /**
     * Getting the type of the block.
     * 
     * @return the type of the block.
     */
    public byte getType()
    {
	return this.type;
    }

    /**
     * Getting the size of the block.
     * 
     * @return the size of the block
     */
    public int getSize()
    {
	return this.size;
    }

    /**
     * Getting the data of the block.
     * 
     * @return the data of the block
     */
    public byte[] getData()
    {
	return this.data;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
	String r = "[Data block type: " + this.type + " size: " + this.size + "]\n" + "[Raw data: ";
	for (byte element : this.getData())
	    r += element + " ";

	r += "]\n";
	return r;
    }
}