public class DriverController
{
    private Drive[] drives;
    private int driveSize;
    private int[] parityDrives;

    public DriverController(int numbOfDrives, int driveSize)
    {
        drives = new Drive[numbOfDrives];
        this.driveSize = driveSize;
        // this keeps track of which drive has the parity byte for each row
        parityDrives = new int[driveSize];
        for(int k = 0; k < driveSize; k++)
        {
            parityDrives[k] = k % numbOfDrives;
        }

        for (int i = 0; i < drives.length; i++)
        {
            drives[i] = new Drive(driveSize);
        }
    }

    /**
     * This method replaces all the bytes in a row
     *
     * @param data
     * @param parity
     * @param row
     */
    public void writeRow(byte[] data, byte parity, int row)
    {
        int dataIndex = 0;
        for (int i = 0; i < this.drives.length; i++)
        {
            if (parityDrives[row] == i)
            {
                this.drives[i].writeByte(row, parity);
            }
            else
            {
                this.drives[i].writeByte(row, data[dataIndex]);
                dataIndex++;
            }
        }
    }

    /**
     * This method reads a byte row
     *
     * @param row
     * @return
     */
    public byte[] readRow(int row)
    {
        byte[] data = new byte[drives.length - 1];

        int dataIndex = 0;

        for (int i = 0; i < drives.length; i++)
        {
            if (parityDrives[row] == i);
            else
            {
                data[dataIndex] = drives[i].readByte(row);
                dataIndex++;
            }
        }

        return data;
    }

    /**
     * This method deletes a row
     *
     * @param row
     */
    public void deleteRow(int row)
    {
       for (int i = 0; i < drives.length; i++)
       {
           drives[i].deleteByte(row);
       }
    }

    /**
     * This method returns the parity for a row
     *
     * @param row
     * @return
     */
    public byte getRowParity(int row)
    {
        return drives[parityDrives[row]].readByte(row);
    }

    public void print()
    {
        for (int i = 0; i < drives.length; i++)
        {
            drives[i].print();
        }
    }
}
