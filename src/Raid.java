import java.io.File;

public class Raid
{
    private int numbOfDrives;
    private DriverController driverController;

    /**
     * This method initializes the drives
     *
     * @param numbOfDrives
     * @param sizeOfDrives
     */
    public void initialize(int numbOfDrives, int sizeOfDrives, File baseDir)
    {
        this.numbOfDrives = numbOfDrives;
        driverController = new DriverController(numbOfDrives, sizeOfDrives, baseDir);
    }

    /**
     * This function looks to see if a drive is missing and fixes it if it is
     *
     * It returns true if the data is good and false if it has been corrupted
     */
    public boolean checkDrives()
    {
        if (!this.driverController.checkData())
        {
            System.out.println("Data corrupted");
            return false;
        }
        else
        {
            System.out.println("Data has not been corrupted");
            return true;
        }
    }

    /**
     * Writes data to drives
     *
     * @param startIndex
     * @param data
     */
    public void writeData(int startIndex, byte[] data)
    {
        byte[] newData = new byte[this.numbOfDrives - 1];
        int numbOfRows = data.length / newData.length;
        int i = 0;
        int offset = startIndex % (this.numbOfDrives - 1);
        int startRow = startIndex / (this.numbOfDrives - 1);
        int startingUp = 0; //this.numbOfDrives - 1 - offset;
        int index = 0;

        if (data.length % newData.length > 0)
            numbOfRows++;

        if (offset > 0)
        {
            byte[] readData;
            readData = driverController.readRow(startIndex / this.numbOfDrives);

            if (driverController.getParityDrives()[startRow] < offset)
            {
                for (int j = 0; j < offset - 1; j++)
                {
                    newData[j] = readData[j + 1];
                    index = j;
                }
            }
            else
            {
                for (int j = 0; j < offset; j++)
                {
                    newData[j] = readData[j];
                    index = j;
                }
            }

            for (int k = index+1; k < newData.length; k++)
            {
                newData[k] = data[k - index];
            }

            byte parity = updateParity(newData);
            driverController.writeRow(newData, parity, startRow);

            startRow++;
        }


        for (i = 0; i < numbOfRows; i++)
        {
            for (int j = 0; j < newData.length && (i * newData.length + j + index) < data.length; j++)
            {
                newData[j] = data[i * newData.length + j + index];
//                System.out.print(newData[j]);
            }

            byte parity = updateParity(newData);
            driverController.writeRow(newData, parity, ((startRow) + i));
        }
    }

    /**
     * This method returns some data
     *
     * @param startIndex
     * @param length
     * @return
     */
    public byte[] readData(int startIndex, int length)
    {
        byte[] data = new byte[length];
        int size = (this.numbOfDrives - 1);

        for (int i = 0; i < length; i++)
        {
            data[i] = this.driverController.readRow((startIndex + i) / size)[(startIndex + i) % size];
        }

        return data;
    }

    /**
     * This method deletes a bunch of data
     *
     * @param startIndex
     * @param length
     */
    public void deleteData(int startIndex, int length)
    {

    }

    /**
     * This method returns the parity for some data
     *
     * @param data
     */
    private byte updateParity(byte[] data)
    {
        byte parity = data[0];

        for (int i = 1; i < data.length; i++)
        {
            parity ^= data[i];
        }

        return parity;
    }

    /**
     * This method replaces a drive
     */
    public void replaceADrive(int drive)
    {
        this.driverController.replaceDrive(drive - 1);
    }

    public void print()
    {
        this.driverController.print();
    }

    public void deleteAndReplace(int drive)
    {
        this.driverController.deleteDrive(drive);
        this.driverController.replaceDrive(drive);
    }

    public void flipABit(int index)
    {
        this.driverController.flipAByte(index);
    }
}
