import java.io.File;

/**
 * Created by personal on 11/15/15.
 */
public class Main {


    public static void main(String[] args)
    {
        
        FileManager mgr = new FileManager(4, 32, true);
        mgr.run();
        
//        Raid raid = new Raid();
//        raid.initialize(3, 20, new File("/tmp"));
//
//        byte[] data = {0,1,1,1,1,0,0,1,0,1,1,0,0,0,0,1,1,1,1};
//
//        System.out.println("Write Data");
//
//        for (int i = 0; i < data.length; i++)
//        {
//            System.out.print(data[i]);
//        }
//
//        System.out.println();
//
//        raid.writeData(0, data);
//
//        System.out.println();
//
//        //raid.print();
//
//        byte[] readData = raid.readData(0, data.length);
//
//        for (int i = 0; i < readData.length; i++)
//        {
//            System.out.print(readData[i]);
//        }
//        System.out.println();
//
//        raid.checkDrives();
//
//        raid.deleteAndReplace(0);
//
//        System.out.println("Data after reconstructing drive 0");
//        readData = raid.readData(0, data.length);
//
//        for (int i = 0; i < readData.length; i++)
//        {
//            System.out.print(readData[i]);
//        }
//        System.out.println();
//
//        raid.deleteAndReplace(1);
//
//        System.out.println("Data after reconstructing drive 1");
//        readData = raid.readData(0, data.length);
//
//        for (int i = 0; i < readData.length; i++)
//        {
//            System.out.print(readData[i]);
//        }
//        System.out.println();
//
//        raid.deleteAndReplace(2);
//
//        System.out.println("Data after reconstructing drive 2");
//        readData = raid.readData(0, data.length);
//
//        for (int i = 0; i < readData.length; i++)
//        {
//            System.out.print(readData[i]);
//        }
//        System.out.println();
//
//        raid.flipABit(15);
//
//        System.out.println("Data after fliping a bit");
//        readData = raid.readData(0, data.length);
//
//        for (int i = 0; i < readData.length; i++)
//        {
//            System.out.print(readData[i]);
//        }
//        System.out.println();
//        raid.checkDrives();
    }
}
