import java.io.File;
import java.util.ArrayList;
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
//    public static void main(String[] args) {
//        FileManager mgr = new FileManager(4, 32, true);
//        mgr.run();
//    }

    private int freespace;
    private final int size;
    private final Raid raid5;
    private final HashMap<String, Integer[]> fileTable;
    private final ArrayList<String> fileList;
    private final HashMap<String, String> testRAID;
    private final boolean debug;
    private final Scanner scan = new Scanner(System.in);

    public FileManager(int driveCount, int bitsPerDrive, boolean debug) {
        raid5 = new Raid();
        raid5.initialize(driveCount, bitsPerDrive, new File("/tmp"));
        fileTable = new HashMap<>();
        freespace = bitsPerDrive*driveCount;
        fileList = new ArrayList<>();
        size = freespace;
                
        testRAID = new HashMap<>();
        this.debug = debug;
    }

    /**
     * Get the indices of a space in memory large enough for the specified size
     *
     * @param numberOfBits
     * @return array { driveID, segment }
     */
    public Integer[] getFreespace(int numberOfBits) {
        Integer[] free;
        
        if (numberOfBits > freespace) {
            free = null;
            System.out.println("Not enough memory. Free memory: " + freespace 
                    + "b; Requested memory: " + numberOfBits + "b.");
        } else {
            freespace -= numberOfBits;
            boolean[] isData = new boolean[size];
            for(String key : fileList) {
                Integer[] indices = fileTable.get(key);
                for (int i = 0; i < indices.length; i += 2) {
                    for (int j = indices[i]; j < indices[i] + indices[i+1]; j++) {
                        isData[j] = true;
                    }
                }
            }
            ArrayList<Integer> keys = new ArrayList<>();
            HashMap<Integer,Integer> saveTo = new HashMap<>();
            int index = -1;
            int length = 0;
            int bitsAdded = 0;
            for (int i = 0; i < isData.length; i++) {
                
                if (!isData[i] && length == 0) {
                    keys.add(i);
                    index = i;
                    length = 1;
                } else if (!isData[i]) {
                    length += 1;
                    if (bitsAdded + length == numberOfBits) {
                        saveTo.put(index, length);
                    }
                } else if (length > 0) {
                    if (bitsAdded + length >= numberOfBits) {
                        saveTo.put(index, numberOfBits - bitsAdded);
                        break;
                    } else {
                        saveTo.put(index, length);
                        bitsAdded += length;
                        index = -1;
                        length = 0;
                    }
                }
            }
            
            boolean addKeyValue = !saveTo.containsKey(index) 
                    && index >= 0 && length > 0;
            
            if (addKeyValue) {
                saveTo.put(index, numberOfBits - bitsAdded);
            }
            
            free = new Integer[saveTo.size() * 2];
            if (debug) {
                
                System.out.println(" " + numberOfBits + " bits stored at points (index,numBits):");
                System.out.print(" {");
            }
            for (int i = 0; i < saveTo.size() * 2; i += 2) {
                free[i] = keys.get(i/2);
                free[i+1] = saveTo.get(free[i]);
                if (debug) {
                    System.out.print("(" + free[i] + "," + free[i+1] + "),");
                }
            }
            if (debug) {
                System.out.println("}");
            }
        }
        
        return free;
    }

    /**
     * Create a new file
     *
     * @param name file name
     * @param bits file's data
     */
    private void createFile(String name, String bits) {

        byte[] bytes = parseBits(bits);

        if (fileTable.containsKey(name)) {
            if (confirm(" File already exists. Overwrite file?")) {
                changeFile(name, bits);
            }
        } else {
            // Even indices = index of first bit, odd indices = number of bits
            Integer[] free = getFreespace(bytes.length);
            fileTable.put(name, free);
            fileList.add(name);
            
            int dataIndex = 0;
            if (debug) {
                System.out.println(" Adding bits at locations (index,numBits,returned_bits):");
                System.out.print(" { ");
            }
            for (int i = 0; i < free.length; i += 2) {
                int nextIndex = dataIndex + free[i + 1];
                byte[] bitsAsBytes = getInRange(bytes, dataIndex, nextIndex);
                if (debug) {
                    System.out.print("(" + free[i] + "," + bitsAsBytes.length + ",");
                }
                raid5.writeData(free[i], bitsAsBytes);
                if (debug) {
                    System.out.print("),");
                }
                dataIndex = nextIndex;
            }
            
            if (debug) {
                System.out.println(" }");
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
    private byte[] getFile(String name) {
        
        Integer[] indices = fileTable.get(name);
        
//        byte[] data = null;
        
        ArrayList<Byte> data = new ArrayList<>();
        
        if (indices != null) {
            if (debug) {
                System.out.println(" Retrieving bits (index,length,returned_bits): ");
                System.out.print(" {");
            }
            for (int i = 0; i < indices.length; i += 2) {
                if (debug) {
                    System.out.print("(" + indices[i] + "," + indices[i+1] + ",");
                }
                byte[] segment = raid5.readData(indices[i], indices[i+1]);
                if (debug) {
                    for (int j = 0; j < segment.length; j++) {
                        System.out.print(segment[j]);
                    }
                    System.out.print("),");
                }
                for (int j = 0; j < segment.length; j++) {
                    data.add(segment[j]);
                }
//                data = mergeArrays(data,segment);
            }
            if (debug) {
                System.out.print("}");
            }
            System.out.println();
        }
        byte[] output = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            output[i] = data.get(i);
        }
        return output;
    }

    /**
     * Verify the drives, print out a message
     */
    private void checkDrives() {
        raid5.checkDrives();
    }

    /**
     * Corrupt the specified file, modifying the data but not the parity
     *
     * @param name file name
     * @param bits changes to make
     */
    private void corruptFile(String name, String bits) {
        if (fileList.contains(name)) {
            
            byte[] data = getFile(name);
            byte[] corrupt = parseBits(bits);
            
            Integer[] indices = fileTable.get(name);
            int bitIndex = 0;
            
            for (int i = 0; i < indices.length; i += 2) {
                for (int j = indices[i]; j < indices[i] + indices[i+1]; j++) {
                    if (data[bitIndex] != corrupt[bitIndex]) {
                        raid5.flipABit(j);
                    }
                    bitIndex++;
                    if (bitIndex == bits.length()) {
                        break;
                    }
                }
            }
        } else if (confirm(" File does not exist. Create new file?")) {
            createFile(name, bits);
        }
    }

    /**
     * Modify the specified file
     *
     * @param name file name
     * @param bits changes to make
     */
    private void changeFile(String name, String bits) {
        
        if (fileList.contains(name)) {
            
            deleteFile(name);
            createFile(name, bits);
            
        } else {
            System.out.println(" File does not exist. Creating new file: " + name);
            createFile(name, bits);
        }
    }

    /**
     * Delete the specified file by dereferencing its memory
     *
     * @param name file name
     */
    private void deleteFile(String name) {
        
        System.out.println(fileList.remove(name));
        Integer[] file = fileTable.remove(name);
        System.out.println(fileTable.containsKey(name));
        if (file != null) {
            for (int i = 1; i < file.length; i += 2) {
                freespace += file[i];
            }
            
            file = null;
            
            if (debug) {
                testRAID.remove(name);
            }
        }
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
            System.out.println(" 3) Modify file");
            System.out.println(" 4) Delete file");
            System.out.println(" 5) Verify drive");
            System.out.println(" 6) Reconstruct drive");
            System.out.println(" 7) Print drives");
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
                    
                    if (confirm(" Print all files?")) {
                        for (String fileName : fileList) {
                            
                        }
                    }

                    System.out.print(" File's name: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }

                    byte[] contents = getFile(name);
                    if (contents == null) {
                        System.out.println(" File does not exist.");
                    } else {
                        System.out.println(" Retrieved file " + name + ", contents: ");
                        System.out.print(" ");
                        for (int i = 0; i < contents.length; i++) {
                            System.out.print(contents[i]);
                        }
                        System.out.println();
                        if (debug) {
                            System.out.println(" Expected:");
                            System.out.println(" " + testRAID.get(name));
                        }
                    }
                    break;
                    
                case 3: // Change file
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
                    
                case 4: // Delete file
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
                    
                case 5: // Verify drive
                    System.out.println(" Checking drives");

                    checkDrives();
                    System.out.println(" Drive check complete");
                    break;
                    
                case 6: // Print drives
                    System.out.println(" Printing contents of all drives.");

                    break;
                case 7: // Reconstruct drive
                    System.out.println(" Reconstruct drive, enter 0 to cancel.");

                    System.out.print(" Drive number: ");
                    name = scan.nextLine();
                    name = name.trim();
                    System.out.println();

                    if (name.equals("0")) {
                        break;
                    }
                    break;
//                case 7: // Get data
//                    System.out.println(" Retrieve data, enter 0 to cancel.");
//
//                    System.out.print(" Drive's name: ");
//                    name = scan.nextLine();
//                    name = name.trim();
//                    System.out.println();
//
//                    if (name.equals("0")) {
//                        break;
//                    }
//
//                    int length,
//                     index;
//                    System.out.print(" Index: ");
//                    try {
//                        index = Integer.parseInt(scan.nextLine());
//                    } catch (NumberFormatException e) {
//                        break;
//                    }
//                    System.out.println();
//                    System.out.print(" Length: ");
//                    try {
//                        length = Integer.parseInt(scan.nextLine());
//                    } catch (NumberFormatException e) {
//                        break;
//                    }
//                    System.out.println();
//
//                    getData(name, index, length);
//                    System.out.println(" End data retrieveal");
//                    break;
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
    private byte[] parseBits(String bits) {
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
//        System.out.println(output.length);
        for (int i = 0; i < output.length; i++) {
            output[i] = array[i + a];
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
        if (a == null && b == null) {
            output = null;
        } else if (a == null) {
            output = b;
        } else if (b == null) {
            output = a;
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