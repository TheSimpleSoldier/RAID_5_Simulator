
public class Drive
{
    byte[] bytes;

    public Drive(int size)
    {
        bytes = new byte[size];
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
}
