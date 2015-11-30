import java.util.HashMap;
import java.util.Scanner;

/**
 * Basic methods used to modify files, creating a level of abstraction between
 * the user and the actual data.
 */
public class FileManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileManager mgr = new FileManager(4, 32, true);
        mgr.run();
    }

    private final int segments;
    private final Raid raid5;
    private final HashMap<String, Integer[]> fileTable;
    private final HashMap<String, String> testRAID;
    private final boolean debug;
    private final Scanner scan = new Scanner(System.in);

    public FileManager(int driveCount, int size, boolean debug) {
        raid5 = new Raid();
        raid5.initialize(driveCount, size);
        fileTable = new HashMap<>();
        segments = size;
        testRAID = new HashMap<>();
        this.debug = debug;
    }

    /**
     * Get the indices of a space in memory large enough for the specified size
     *
     * @param size
     * @return array { driveID, segment }
     */
    public Integer[] getFreespace(int size) {
        Integer[] free = null;
        return free;
    }

    /**
     * Create a new file
     *
     * @param name file name
     * @param bits file's data
     */
    public void createFile(String name, String bits) {
        // Add zeros to the end of this string to ensure full bytes are parsed
        int padZeros = bits.length() % 8;
        for (int i = 0; i < padZeros; i++) {
            bits = bits.concat("0");
        }

        byte[] bytes = parseBytes(bits);

        if (fileTable.containsKey(name)) {
            if (confirm(" File already exists. Overwrite file?")) {
                changeFile(name, bits);
            }
        } else {
            Integer[] free = getFreespace(bits.length() / 8);
            fileTable.put(name, free);
            int dataIndex = 0;
            for (int i = 0; i < free.length; i += 2) {
                int nextIndex = dataIndex + free[i + 1];
                raid5.writeData(free[i], getInRange(bytes, dataIndex, nextIndex));
                dataIndex = nextIndex;
            }

            if (debug) {
                testRAID.put(name, bits);
            }
        }
    }

    /**
     * Retrieve the specified file.
     *
     * @param name file name
     * @return byte array containing file's data
     */
    public byte[] getFile(String name) {
        byte[] data = null;
        
        Integer[] indices = fileTable.get(name);
        
        for (int i = 0; i < indices.length; i++) {
            
        }
        
        return data;
    }

    /**
     * Verify the drives, print out a message
     */
    public void checkDrives() {
        raid5.checkDrives();
    }

    /**
     * Corrupt the specified file, modifying the data but not the parity
     *
     * @param name file name
     * @param bits changes to make
     */
    public void corruptFile(String name, String bits) {
        // Add zeros to the end of this string to ensure full bytes are parsed
        int padZeros = bits.length() % 8;
        for (int i = 0; i < padZeros; i++) {
            bits = bits.concat("0");
        }
    }

    /**
     * Modify the specified file
     *
     * @param name file name
     * @param bits changes to make
     */
    public void changeFile(String name, String bits) {
        // Add zeros to the end of this string to ensure full bytes are parsed
        int padZeros = bits.length() % 8;
        for (int i = 0; i < padZeros; i++) {
            bits = bits.concat("0");
        }
    }

    /**
     * Delete the specified file
     *
     * @param name file name
     */
    public void deleteFile(String name) {
        Integer[] indices = fileTable.get(name);
        fileTable.remove(name);

        for (int i = 0; i < indices.length; i += 2) {
            raid5.deleteData(indices[i], indices[i + 1]);
        }

        if (debug) {
            testRAID.remove(name);
        }
    }

    /**
     * Retrieve data from the specified drive, index, and size
     *
     * @param drive name of the drive
     * @param index staring index
     * @param length number of bits
     * @return byte array containing the data
     */
    public byte[] getData(String drive, int index, int length) {
        byte[] data = null;
        return data;
    }

    /**
     * Run the program. Enable the user to explore the program's functions
     * through a command prompt interface.
     */
    public void run() {
        System.out.println("----------File Manager----------");

        RUN_PROGRAM:    // Label used to exit program
        while (true) {
            // Print options for user
            System.out.println("Options:");
            System.out.println(" 1) Create file");
            System.out.println(" 2) Retrieve file");
            System.out.println(" 3) Verify drive");
            System.out.println(" 4) Corrupt file");
            System.out.println(" 5) Modify file");
            System.out.println(" 6) Delete file");
            System.out.println(" 7) Get data");
            System.out.print(" Any other key to exit: ");

            // Get user input
            String input = scan.nextLine();
            int selection;
            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                selection = -1;
            }
            String name;
            System.out.println();

            // Run method based on user input
            switch (selection) {
                case 1: // Create file
                    System.out.println(" Create file, enter 0 to cancel.");

                    System.out.print(" New file's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    System.out.print(" Bit string: ");
                    input = scan.nextLine();
                    System.out.println();

                    createFile(name, input);
                    System.out.println(" Created file " + name);
                    break;
                case 2: // Get file
                    System.out.println(" Retrieve file, enter 0 to cancel.");

                    System.out.print(" File's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    getFile(name);
                    System.out.println(" Retrieved file " + name);
                    break;
                case 3: // Verify drive
                    System.out.println(" Checking drives");

                    checkDrives();
                    System.out.println(" Drive check complete");
                    break;
                case 4: // Corrupt file
                    System.out.println(" Corrupt file, enter 0 to cancel.");

                    System.out.print(" File's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    System.out.print(" Bit string: ");
                    input = scan.nextLine();
                    System.out.println();

                    corruptFile(name, input);
                    System.out.println(" Corrupted file " + name);
                    break;
                case 5: // Change file
                    System.out.println(" Modify file, enter 0 to cancel.");

                    System.out.print(" File's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    System.out.print(" Bit string: ");
                    input = scan.nextLine();
                    System.out.println();

                    changeFile(name, input);
                    System.out.println(" Modified file " + name);
                    break;
                case 6: // Delete file
                    System.out.println(" Delete file, enter 0 to cancel.");

                    System.out.print(" File's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    deleteFile(name);
                    System.out.println(" Deleted file " + name);
                    break;
                case 7: // Get data
                    System.out.println(" Retrieve data, enter 0 to cancel.");

                    System.out.print(" Drive's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    int length,
                     index;
                    System.out.print(" Index: ");
                    try {
                        index = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException e) {
                        break;
                    }
                    System.out.println();
                    System.out.print(" Length: ");
                    try {
                        length = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException e) {
                        break;
                    }
                    System.out.println();

                    getData(name, index, length);
                    System.out.println(" End data retrieveal");
                    break;
                default:// Exit
                    System.out.println();
                    System.out.println("-----End program-----");

                    break RUN_PROGRAM;
            }
            System.out.println();
        }
    }

    /**
     * Parse the value of a given bit string, with one bit per byte.
     *
     * @param bits
     * @param bitPerByte
     * @return
     */
    private byte[] parseBytes(String bits) {
        int size = bits.length();
        byte[] output = new byte[size];
        for (int i = 0; i < size; i++) {
            char next = bits.charAt(i);
            if (next == '0') {
                output[i] = 0;
            } else {
                output[i] = 1;
            }
        }
        return output;
    }

    private boolean confirm(String message) {
        boolean confirmation = false;

        System.out.println(message + " (y/n)");
        String input = scan.nextLine();
        input = input.toLowerCase();
        if (input.contains("y")) {
            confirmation = true;
        }

        return confirmation;
    }

    /**
     * Return a copy of the given array from index a (inclusive) to b
     * (exclusive)
     *
     * @param array
     * @param a
     * @param b
     * @return
     */
    private byte[] getInRange(byte[] array, int a, int b) {
        byte[] output = new byte[b - a];

        for (int i = 0; i < output.length; i++) {
            output[i + a] = array[i + a];
        }

        return output;
    }

    /**
     * Combine byte arrays a and b
     *
     * @param a
     * @param b
     * @return
     */
    private byte[] mergeArrays(byte[] a, byte[] b) {
        byte[] output;
        if (a == null) {
            output = a;
        } else if (b == null) {
            output = b;
        } else {
            output = new byte[a.length + b.length];

            for (int i = 0; i < a.length; i++) {
                output[i] = a[i];
            }

            int index = a.length;
            for (int i = 0; i < b.length; i++) {
                output[index] = b[i];
                index++;
            }
        }
        return output;
    }
}
