public class Raid
{
    private int numbOfDrives;
    private DriverController driverController;

    public Raid()
    {
    }

    /**
     * This method initializes the drives
     *
     * @param numbOfDrives
     * @param sizeOfDrives
     */
    public void initialize(int numbOfDrives, int sizeOfDrives)
    {
        this.numbOfDrives = numbOfDrives;
        driverController = new DriverController(numbOfDrives, sizeOfDrives);
    }

    /**
     * This function looks to see if a parity is wrong and notifies the manager
     * if it is
     */
    public String checkData(int index)
    {
        return "";
    }

    /**
     * This function looks to see if a drive is missing and fixes it if it is
     */
    public String checkDrives()
    {
        return "";
    }

    /**
     * Writes data to drives
     *
     * @param startIndex
     * @param data
     */
    public void writeData(int startIndex, byte[] data)
    {

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
        return new byte[]{0};
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
        return 0;
    }
}
