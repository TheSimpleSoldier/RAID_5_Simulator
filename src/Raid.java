public class Raid
{
    private int numbOfDrives;
    private DriverController driverController;

    public Raid()
    {
    }

    /**
     * This method adds some data to the drives
     *
     * @param data
     */
    public void addData(byte[] data)
    {

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
     * This function looks to see if a drive is missing and fixes it
     * if it is
     */
    public String checkData()
    {
        return "";
    }

    /**
     * This method returns some data
     *
     * @param startIndex
     * @param length
     * @return
     */
    public byte[] getData(int startIndex, int length)
    {
        return new byte[]{0};
    }

    /**
     * This method returns the parity for some data
     *
     * @param startIndex
     * @param data
     */
    private byte[] updateParity(int startIndex, byte[]data)
    {

    }
}
