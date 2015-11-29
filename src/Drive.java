
public class Drive
{
    byte[] bytes;
    int length;

    public Drive(int size)
    {
        bytes = new byte[size];
        length = 0;
    }

    /**
     * This method changes a byte at an index
     *
     * @param index
     * @param newByte
     */
    public void writeByte(int index, byte newByte)
    {
        // TODO: Write byte to file
        if (index > length)
        {
            length = index; // TODO: Add saving length to file
        }
        bytes[index] = newByte;
    }

    /**
     * This method returns the byte at an index
     *
     * @param index
     * @return
     */
    public byte readByte(int index)
    {
        // TODO: Read Byte from file
        return bytes[index];
    }

    /**
     * This method deletes a byte of data
     *
     * @param index
     */
    public void deleteByte(int index)
    {
        // TODO: have delete work on file
        bytes[index] = 0;
    }

    public void print()
    {
        System.out.println();

        for (int i = 0; i < bytes.length; i++)
        {
            System.out.print(bytes[i]);
        }
        System.out.println();
    }

    /**
     * This method will flip a byte
     */
    public void flipAByte(int index)
    {
        bytes[index] ^= 1;
    }
}
