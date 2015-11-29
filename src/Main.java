/**
 * Created by personal on 11/15/15.
 */
public class Main {


    public static void main(String[] args)
    {
        Raid raid = new Raid();
        raid.initialize(3, 20);

        byte[] data = {0,1,1,1,1,0,0,1,0,1,1,0,0,0,0,1,1,1,1};

        System.out.println("Write Data");

        for (int i = 0; i < data.length; i++)
        {
            System.out.print(data[i]);
        }

        System.out.println();

        raid.writeData(0, data);

        System.out.println();

        //raid.print();

        byte[] readData = raid.readData(0, data.length);

        for (int i = 0; i < readData.length; i++)
        {
            System.out.print(readData[i]);
        }
        System.out.println();

        raid.checkDrives();
    }
}
