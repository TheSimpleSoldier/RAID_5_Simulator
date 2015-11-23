public class DriverController
{
    private Drive[] drives;
    private int driveSize;
    // this int determines which drive gets the parity bit
    private int currentParityDrive;
    private int[] parityDrives;

    public DriverController(int numbOfDrives, int driveSize)
    {
        drives = new Drive[numbOfDrives];
        this.driveSize = driveSize;
        currentParityDrive = 0;
        // this keeps track of which drive has the parity bit for each row
        parityDrives = new int[driveSize];
    }

    /**
     * This method adds a row of bytes with a parity bit to the drives
     *
     * @param row
     * @param parity
     */
    public void writeRow(byte[] row, byte parity)
    {

    }

    /**
     * This method adds a row of bytes at an index
     *
     * @param row
     * @param parity
     * @param index
     */
    public void writeRowAtIndex(byte[] row, byte parity, int index)
    {

    }

    /**
     * This method reads a byte row at an index
     * which will have one less bit than the # of drives
     * b/c of the parity bit
     *
     * @param index
     * @return
     */
    public byte[] readRowAtIndex(int index)
    {

    }

    /**
     * This method deletes a row of bits
     *
     * @param index
     */
    public void deleteBit(int index)
    {

    }
}
