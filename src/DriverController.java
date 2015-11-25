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
    }

    /**
     * This method replaces all the bytes in a row
     *
     * @param bytes
     * @param parity
     * @param row
     */
    public void writeRow(byte[] bytes, byte parity, int row)
    {

    }

    /**
     * This method reads a byte row
     *
     * @param row
     * @return
     */
    public byte[] readRow(int row)
    {
        return new byte[]{0};
    }
}
