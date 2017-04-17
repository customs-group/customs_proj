package container;

import io.EdAbstractFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardlol on 17-4-17.
 */
public class ThmFileReader extends EdAbstractFileReader<int[]> {
    //~ Static fields ----------------------------------------------------------

    private static ThmFileReader reader = new ThmFileReader();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //~ Constructors -----------------------------------------------------------

    private ThmFileReader() {
        seperator = "@@";
        columnNumber = 24;
    }

    public static ThmFileReader getInstance() {
        return reader;
    }

    //~ Methods ---------------------------------------------------------------

    @Override
    public int[] read(String file) {
        List<Integer> daysList = new ArrayList<>();

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            while (line != null) {
                String[] attrs = line.split(seperator);
                if (attrs.length != columnNumber) {
                    System.err.println("problem line find!");
                    System.err.println("expect line length: " + columnNumber);
                    System.err.println("actual line length: " + attrs.length);
                    System.err.println("line contents: " + line);
                    break;
                }
                if (sdf.parse(attrs[3]).getTime() - sdf.parse(attrs[4]).getTime() <= 0) {
                    int days = Integer.parseInt(attrs[5]);
                    daysList.add(days);
                }
                line = br.readLine();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        int[] daysArray = new int[daysList.size()];
        for (int i = 0; i < daysList.size(); i++) {
            daysArray[i] = daysList.get(i);
        }

        return daysArray;
    }

    //~ Setter/getter methods --------------------------------------------------

    public void setSdf(String dateFormat) {
        sdf = new SimpleDateFormat(dateFormat);
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }
}
