package ml;

import io.EdAbstractFileReader;
import libsvm.svm_node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by edwardlol on 17-4-17.
 */
public class SVMFileReader extends EdAbstractFileReader<SVMData> {

    //~ Static fields ----------------------------------------------------------

    private static SVMFileReader reader = null;

    //~ Constructors -----------------------------------------------------------

    private SVMFileReader() {
        seperator = " ";
        columnNumber = Integer.MAX_VALUE;
    }

    public static SVMFileReader getInstance() {
        if (reader == null) {
            reader = new SVMFileReader();
        }
        return reader;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public SVMData read(String file) {
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            SVMData svmData = new SVMData();

            String line = br.readLine();

            while (line != null) {
                String[] contents = line.split(seperator);
                // set feature num
                svmData.featureNum = contents.length - 1;
                svm_node[] sample = new svm_node[svmData.featureNum];
                svmData.labels.add(normalizeLabel(stod(contents[0])));
                for (int i = 0; i < svmData.featureNum; i++) {
                    sample[i] = new svm_node();

                    sample[i].index = i + 1;
                    sample[i].value = stod(contents[i + 1]);
                }
                svmData.originalSet.add(sample);
                line = br.readLine();
            }

            svmData.sampleNum = svmData.originalSet.size();
            System.out.println("SVMData preparation done! Read " + svmData.getSampleNum() + " samples in total");
            return svmData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SVMData read(Connection connection, String query) {
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            SVMData svmData = new SVMData();

            svmData.featureNum = rs.getMetaData().getColumnCount() - 1;

            while (rs.next()) {
                svm_node[] sample = new svm_node[svmData.featureNum];
                for (int i = 0; i < svmData.featureNum; i++) {
                    sample[i] = new svm_node();

                    sample[i].index = i + 1;

                    if (rs.getObject(i + 2).getClass().getName().equals("java.lang.String")) {
                        sample[i].value = stod(rs.getString(i + 2));
                    } else if ((rs.getObject(i + 2).getClass().getName().equals("java.lang.Long"))
                            || (rs.getObject(i + 2).getClass().getName().equals("java.math.BigDecimal"))) {
                        sample[i].value = rs.getDouble(i + 2);
                    } else {
                        // to be continued
                    }
                }
                svmData.originalSet.add(sample);
                svmData.labels.add(normalizeLabel(stod(rs.getString(1))));
            }
            svmData.sampleNum = svmData.originalSet.size();

            System.out.println("SVMData preparation done! " + svmData.getSampleNum() + " samples in total");
            return svmData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //~ tool Methods -----------------------------------------------------------

    /**
     * transfer String to Double, in case some features are "null", "I", "E" or ""
     * where "I" refers to "Import" and "E" refers to "Export"
     *
     * @param string feature in string
     * @return feature in double
     */
    private static Double stod(String string) {
        Double result = 2.0; // should handle this error: "cannnot convert string to Double"
        if (string == null || string.equals("") || string.equals("null") || string.equals("I")) {
            result = 0.0;
        } else if (string.equals("E")) {
            result = 1.0;
        } else {
            try {
                result = Double.parseDouble(string);
            } catch (NumberFormatException e) {
                String[] tmp = string.split(":");
                try {
                    result = Double.parseDouble(tmp[1]);
                } catch (NumberFormatException e2) {
                    e2.printStackTrace();
                }

            }
        }
        return result;
    }

    /**
     * normalize the labels to 1 or -1, instead of 0 or 1
     *
     * @param label original label
     * @return normalize label
     */
    private static Double normalizeLabel(Double label) {
        return (Math.abs(label - 0.0d) < 0.00001 || Math.abs(label + 1.0d) < 0.00001) ? -1.0d : 1.0d;
    }

}
