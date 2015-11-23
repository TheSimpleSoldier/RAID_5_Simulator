
public class Drive
{
    byte[] bytes;
    int currentBit;
    
    public Drive(int size)
    {
        currentBit = 0;
        bytes = new byte[size];
    }


    /**
     * This method adds a bit to the end of the drive
     *
     * @param bit
     */
    public void writeNextByte(byte bit)
    {

    }

    /**
     * This method changes a byte at an index
     *
     * @param index
     * @param bit
     */
    public void writeByteAtIndex(int index, byte bit)
    {

    }


    /**
     * This method returns the bit at an index
     *
     * @param index
     * @return
     */
    public byte getByteAtIndex(int index)
    {

    }


    /**
     * This method deletes a bit
     *
     * @param index
     */
    public void deleteBit(int index)
    {

    }
}
