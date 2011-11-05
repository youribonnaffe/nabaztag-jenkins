import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class wrapping a packet to send to a bunny.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class Packet
{
    /**
     * Header byte.
     */
    protected static final byte headerByte = (byte) 0x7F;

    /**
     * Footer bytes.
     */
    protected static final byte[] footerBytes = new byte[] { (byte) 0xFF, (byte) 0x0A };

    /**
     * List of blocks embedded within this packet.
     */
    protected List<Block> blocks;

    /**
     * Data to send to bunny.
     */
    protected byte[] data;

    /**
     * Flag enabled when a ping block is within the packet.
     */
    protected boolean pingPacketPresent;

    /**
     * Flag enabled when an ambient block is within the packet.
     */
    protected boolean ambientBlockPresent;

    /**
     * Creating a new packet instance.
     */
    public Packet()
    {
	this.blocks = new ArrayList<Block>();
	this.pingPacketPresent = false;
	this.ambientBlockPresent = false;
    }

    /**
     * Creating a new packet instance, with existing data.
     * 
     * @param data packet raw data.
     */
    public Packet(byte[] data)
    {
	this();
	this.data = data;
	this.parseBlocks();
    }

    /**
     * Internal method used to rebuild blocks from raw data.
     */
    private void parseBlocks()
    {
	int pos = 1;

	// read while there is more than two int's left
	while (pos < this.data.length - 2)
	{

	    byte type = this.data[pos];

	    int size = this.data[pos + 3] + (255 * this.data[pos + 2]) + (255 * 255 * this.data[pos + 1]);
	    pos += 4;

	    byte[] raw = new byte[size];

	    for (int i = 0; i < size; i++)
		raw[i] = this.data[pos + i];

	    pos += size;

	    switch (type)
	    {
	    case Block.MESSAGE_BLOCK_TYPE:
		this.blocks.add(new MessageBlock(size, raw));
		break;
	    case Block.AMBIENT_BLOCK_TYPE:
		this.blocks.add(new AmbientBlock(size, raw));
		this.ambientBlockPresent = true;
		break;
	    case Block.PING_INTERVAL_BLOCK_TYPE:
		// FIXME this.blocks.add(new PingIntervalBlock(raw));
		this.pingPacketPresent = true;
		break;
	    default:
		this.blocks.add(new Block(type, size, raw));
	    }
	}
    }

    /**
     * Appending a block to the packet.
     * 
     * @param e the block to add.
     */
    public void addBlock(Block e)
    {
	// Appending the block at the end of the list
	this.blocks.add(e);

	// Checking the type of the block
	if (e.type == 3) this.pingPacketPresent = true;
	if (e.type == 4) this.ambientBlockPresent = true;
    }

    /**
     * Testing if a ping interval block is present within the packet.
     * 
     * @return <tt>true</tt> if a ping interval block is present within the packet, <tt>false</tt> if not.
     */
    public boolean isPingBlockPresent()
    {
	return this.pingPacketPresent;
    }

    /**
     * (Re)setting a ping interval block. If a ping interval block is already present within the packet, it is replaced by a new one with
     * the new ping value. If n o ping interval block is present within the packet, a new block is added.
     * 
     * @param pingInterval the ping interval to set.
     */
    public void setPingIntervalBlock(int pingInterval)
    {
	for (int i = 0; i < this.blocks.size(); i++)
	{
	    if (this.blocks.get(i).type == Block.PING_INTERVAL_BLOCK_TYPE)
	    {
		this.blocks.remove(i);
		break;
	    }
	}
	// FIXME this.blocks.add(new PingIntervalBlock(pingInterval));
	this.pingPacketPresent = true;
    }

    /**
     * Testing if an ambient block is present within the packet.
     * 
     * @return <tt>true</tt> if an ambient block is present within the packet, <tt>false</tt> if not.
     */
    public boolean isAmbientBlockPresent()
    {
	return this.ambientBlockPresent;
    }

    /**
     * Generating a byte array from the list of blocks
     * 
     * @return The byte array corresponding to this packet
     */
    public byte[] generatePacket()
    {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	// Adding the header byte
	bos.write(Packet.headerByte);

	// For each embedded block
	for (Block block : this.blocks)
	{
	    // Adding type of block
	    bos.write(block.getType());

	    // In case of a MessageBlock, encoding the data
	     if (block.getType() == Block.MESSAGE_BLOCK_TYPE) ((MessageBlock) block).encodeBlock();

	    // Adding length of the block
	    ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(bos2);
	    try
	    {
		dos.writeInt(block.getSize());
	    }
	    catch (IOException e)
	    {
		return null;
	    }
	    byte[] sizeBytes = bos2.toByteArray();
	    try
	    {
		dos.close();
		bos2.close();
	    }
	    catch (IOException e)
	    {
		// Ignoring the exception
	    }
	    bos.write(sizeBytes, 1, 3);

	    // Adding the raw data to the packet
	    try
	    {
		bos.write(block.getData());
	    }
	    catch (IOException e)
	    {
		return null;
	    }
	}

	// Adding the footer bytes
	try
	{
	    bos.write(Packet.footerBytes);
	}
	catch (IOException e)
	{
	    return null;
	}

	byte[] result = bos.toByteArray();
	try
	{
	    bos.close();
	}
	catch (IOException e)
	{
	    // Ignoring the exception
	}
	return result;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
	String res = "Packet :\n";
	for (Block b : this.blocks)
	{
	    res += b.toString();
	}
	return res;
    }
}
