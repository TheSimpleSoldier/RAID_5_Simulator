import java.io.File;

public class DriverController
{
    private Drive[] drives;
    private int driveSize;
    private int[] parityDrives;
    private int deletedDrive;
    private File baseDir;

    public DriverController(int numbOfDrives, int driveSize, File baseDir)
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
            drives[i] = new Drive(driveSize, new File(baseDir.getAbsolutePath() + "/" + i + ".txt"));
        }
        deletedDrive = -1;
        this.baseDir = baseDir;
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
        int currentRow = row;

//        System.out.println();
//        System.out.println();

        for (int i = 0; i < this.drives.length; i++)
        {
            if (parityDrives[currentRow] == i)
            {
                this.drives[i].writeByte(currentRow, parity);
            }
            else
            {
                this.drives[i].writeByte(currentRow, data[dataIndex]);
                dataIndex = (dataIndex + 1) % data.length;
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
        byte[][] data = new byte[drives.length][driveSize];
        for (int i = 0; i < drives.length; i++)
        {
            data[i] = drives[i].getBytes();
        }

        for(int k = 0; k < driveSize; k++)
        {
            for(int a = 0; a < drives.length; a++)
            {
                System.out.print(data[a][k] + " ");
            }
            System.out.println();
        }
    }
    
    public void print(int driveIndex) {
        drives[driveIndex].print();
    }

    /**
     * This method checks the data and returns true if the data is good
     * and false if it has been corrupted
     *
     * @return
     */
    public boolean checkData()
    {
        for (int i = 0; i < this.driveSize; i++)
        {
            byte parity = 0;
            for (int j = 0; j < this.drives.length; j++)
            {
                parity ^= this.drives[j].readByte(i);
            }

            if (parity == 1)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * This method deletes a drive
     *
     * @param drive
     */
    public void deleteDrive(int drive)
    {
        this.drives[drive] = new Drive(this.driveSize, new File(baseDir.getAbsolutePath() + "/" + drive + ".txt"));
        this.deletedDrive = drive;
    }

    public void replaceDrive(int drive)
    {
//        System.out.println(drive);
        drives[drive] = new Drive(driveSize, new File((drive) + ".txt"));
        for (int i = 0; i < this.driveSize; i++)
        {
            byte newByte = 0;
            for (int j = 0; j < this.drives.length; j++)
            {
                if (j != drive)
                {
                    newByte ^= this.drives[j].readByte(i);
                }
                this.drives[drive].writeByte(i, newByte);
            }
        }
    }

    public void flipAByte(int index)
    {
        int drive = index % this.drives.length;
        this.drives[drive].flipAByte(index / this.drives.length);
    }

    public int[] getParityDrives()
    {
        return parityDrives;
    }
}
