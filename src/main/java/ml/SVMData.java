package ml;

import libsvm.svm_node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class SVMData {
    public enum data_type {original, scaled}

    Vector<Double> labels = new Vector<>();
    Vector<svm_node[]> originalSet = new Vector<>();
    private Vector<svm_node[]> scaledSet = new Vector<>();
    int sampleNum = 0;
    int featureNum = 0;
    private double scaleUpperBound = 1;
    private double scaleLowerBound = -1;

    /**
     * record data to file
     *
     * @param filename file name to store data
     * @param type     type of data to be recorded, original or scaled
     * @throws IOException
     */
    public void record_data(String filename, data_type type) {
        String _filename;
        Vector<svm_node[]> _set;
        /* set file name for record */
        switch (type) {
            case original:
                _filename = filename + "_original";
                _set = this.originalSet;
                break;
            case scaled:
                _filename = filename + "_scaled";
                _set = this.scaledSet;
                break;
            default:
                System.out.println("wrong data type, record failed");
                return;
        }

        try (FileWriter fileWriter = new FileWriter(filename);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (int i = 0; i < this.sampleNum; i++) {
                bufferedWriter.append(Double.toString(this.labels.get(i))).append(' ');

                svm_node[] sample = _set.get(i);

                for (int j = 0; j < this.featureNum; j++) {
                    bufferedWriter.append(Integer.toString(sample[j].index))
                    .append(':')
                    .append(Double.toString(sample[j].value))
                    .append(' ');
                }
                bufferedWriter.append('\n');
                bufferedWriter.flush();
            }
            System.out.println("SVMData record done! see " + _filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * scale training data so that all features fit in [scaleLowerBound, scaleUpperBound]
     * usually [-1, 1]
     *
     * @return scale parameter
     */
    public double[][] scaleData() {
		/* scale labels, not using for now
		double y_max = -Double.MAX_VALUE;
		double y_min = Double.MAX_VALUE;
		*/
		
		/* step 0: initiate scale param */
        double[][] scaleParam = new double[this.featureNum + 1][2];
        scaleParam[0][0] = this.scaleUpperBound;
        scaleParam[0][1] = this.scaleLowerBound;
		
		/* step 1: initiate feature bound */
        double[] featureMax = new double[this.featureNum];
        double[] featureMin = new double[this.featureNum];
        for (int i = 0; i < this.featureNum; i++) {
            featureMax[i] = -Double.MAX_VALUE;
            featureMin[i] = Double.MAX_VALUE;
        }
		
		/* step 2: find out min/max value */
        for (int i = 0; i < this.sampleNum; i++) {
			/* scale labels, not using for now
			y_max = Math.max(y_max, labels[i]);
			y_min = Math.min(y_min, labels[i]);
			*/
            svm_node[] sample = this.originalSet.get(i);
            for (int j = 0; j < this.featureNum; j++) {
                featureMax[j] = Math.max(featureMax[j], sample[j].value);
                featureMin[j] = Math.min(featureMin[j], sample[j].value);
                scaleParam[j + 1][0] = featureMax[j];
                scaleParam[j + 1][1] = featureMin[j];
            }
        }

		/* pass 3: scale */
        for (int i = 0; i < this.sampleNum; i++) {
            svm_node[] originalSample = this.originalSet.get(i);
            svm_node[] scaledSample = new svm_node[this.featureNum];
            for (int j = 0; j < this.featureNum; j++) {
                scaledSample[j] = new svm_node();
                scaledSample[j].index = originalSample[j].index;
                if (originalSample[j].value == featureMin[j]) {
                    scaledSample[j].value = this.scaleLowerBound;
                } else if (originalSample[j].value == featureMax[j]) {
                    scaledSample[j].value = this.scaleUpperBound;
                } else {
                    scaledSample[j].value = this.scaleLowerBound
                            + ((originalSample[j].value - featureMin[j])
                            / (featureMax[j] - featureMin[j])
                            * (this.scaleUpperBound - this.scaleLowerBound));
                }
            }
            this.scaledSet.add(scaledSample);
        }
        return scaleParam;
    }

    /**
     * scale testing data so that it has the same scale as the training data
     *
     * @param scaleParam the result returned by scaleData on training data
     */
    public void scaleDataFrom(double[][] scaleParam) {
		/* scale labels, not using for now
		double y_max = -Double.MAX_VALUE;
		double y_min = Double.MAX_VALUE;
		*/

		/* step 1: initiate feature bound */
        this.scaleUpperBound = scaleParam[0][0];
        this.scaleLowerBound = scaleParam[0][1];

        double[] feature_max = new double[this.featureNum];
        double[] feature_min = new double[this.featureNum];
        for (int i = 0; i < this.featureNum; i++) {
            feature_max[i] = scaleParam[i + 1][0];
            feature_min[i] = scaleParam[i + 1][1];
        }
		
		/* pass 3: scale */
        for (int i = 0; i < this.sampleNum; i++) {
            svm_node[] sample = this.originalSet.get(i);
            svm_node[] scaledSample = new svm_node[this.featureNum];
            for (int j = 0; j < this.featureNum; j++) {
                scaledSample[j] = new svm_node();
                scaledSample[j].index = sample[j].index;
                if (sample[j].value == feature_min[j]) {
                    scaledSample[j].value = this.scaleLowerBound;
                } else if (sample[j].value == feature_max[j]) {
                    scaledSample[j].value = this.scaleUpperBound;
                } else {
                    scaledSample[j].value = this.scaleLowerBound
                            + ((sample[j].value - feature_min[j])
                            / (feature_max[j] - feature_min[j])
                            * (this.scaleUpperBound - this.scaleLowerBound));
                }
            }
            this.scaledSet.add(scaledSample);
        }
    }

    //~ Getter/setter Methods --------------------------------------------------

    public Vector<svm_node[]> getData(data_type type) {
        switch (type) {
            case original:
                return this.originalSet;
            case scaled:
                return this.scaledSet;
            default:
                System.out.println("wrong data type, original set returned");
                return this.originalSet;
        }
    }

    public Vector<Double> getLabels() {
        return this.labels;
    }

    public int getSampleNum() {
        return this.sampleNum;
    }

    public int getFeatureNum() {
        return this.featureNum;
    }

    public void setScaleUpperBound(double scaleUpperBound) {
        this.scaleUpperBound = scaleUpperBound;
    }

    public void setScaleLowerBound(double scaleLowerBound) {
        this.scaleLowerBound = scaleLowerBound;
    }
}