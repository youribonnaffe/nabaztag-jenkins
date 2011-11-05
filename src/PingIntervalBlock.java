/**
 * Class wrapping a ping interval block.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class PingIntervalBlock extends Block
{
    /**
     * Creating a new ping interval block instance.
     * 
     * @param length ping interval in seconds.
     */
    public PingIntervalBlock(int length)
    {
	super(Block.PING_INTERVAL_BLOCK_TYPE);
	this.size = 1;
	this.data = new byte[] { (byte) length };
    }

    /**
     * Creating a new ping interval block instance with existing data.
     * 
     * @param data The data to store
     */
    public PingIntervalBlock(byte[] data)
    {
	super(Block.PING_INTERVAL_BLOCK_TYPE, 1, data);
    }

    /**
     * @see jNab.core.protocol.Block#toString()
     */
    public String toString()
    {
	String old = super.toString();
	old += "[Ping interval block set for " + this.data[0] + " seconds]\n";
	return old;
    }
}
