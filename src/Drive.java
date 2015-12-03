import java.io.*;

public class Drive
{
    int length;
    int size;
    File drive;

    public Drive(int size, File drive)
    {
        length = 0;
        this.size = 0;
        this.drive = drive;

        byte[] bytes = new byte[size];
        for(int k = 0; k < size; k++)
        {
            bytes[k] = (byte)0;
        }
        try
        {
            drive.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        setBytes(bytes);
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

        byte[] bytes = getBytes();
        bytes[index] = newByte;
        setBytes(bytes);
    }

    /**
     * This method returns the byte at an index
     *
     * @param index
     * @return
     */
    public byte readByte(int index)
    {
        byte[] bytes = getBytes();
        return bytes[index];
    }

    /**
     * This method deletes a byte of data
     *
     * @param index
     */
    public void deleteByte(int index)
    {
        writeByte(index, (byte)0);
    }

    public void print()
    {
        byte[] bytes = getBytes();
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
        byte[] bytes = getBytes();
        bytes[index] ^= 1;
        setBytes(bytes);
    }

    private byte[] getBytes()
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(drive));
            String bytes = in.readLine();
            in.close();
            byte[] toReturn = new byte[bytes.length()];
            for(int k = 0; k < toReturn.length; k++)
            {
                toReturn[k] = Byte.decode(bytes.substring(k, k + 1));
            }

            return toReturn;
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private void setBytes(byte[] bytes)
    {
        String string = "";
        for(int k = 0; k < bytes.length; k++)
        {
            string = string.concat(Byte.toString(bytes[k]));
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new PrintWriter(new FileWriter(drive)));
            out.write(string);
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
