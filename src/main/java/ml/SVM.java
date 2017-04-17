package ml;

import libsvm.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class SVM {
    //~ Static fields/initializers ---------------------------------------------

    private static SVM instance;

    private static svm_print_interface svm_print_null = s -> {
    };

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");

    //~ Instance fields --------------------------------------------------------

    private String modelFile = "./results/svm/model";

    private svm_parameter param = new svm_parameter();

    public int C_BASE = 2;
    public int G_BASE = 2;
    public int C_STEP = 1;
    public int G_STEP = 1;
    public int C_START_VALUE = -8;
    public int C_STOP_VALUE = 8;
    public int G_START_VALUE = -8;
    public int G_STOP_VALUE = 8;

    //~ Constructors -----------------------------------------------------------

    private SVM() {
        // default params
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.C = 0.015625; // for C_SVC, EPSILON_SVR and NU_SVR, default 1.0 / data.getFeatureNum()
        param.gamma = 0.0625;
        param.eps = 0.01;
        param.cache_size = 100.0d;
    }

    public static SVM getInstance() {
        if (instance == null) {
            instance = new SVM();
        }
        return instance;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * train svm model
     *
     * @param data data used to train the model
     * @return model
     */
    public svm_model train(SVMData data) {
        return train(data.getData(SVMData.data_type.scaled),
                data.getLabels(),
                true);
    }

    /**
     * train svm model
     *
     * @param set    train set
     * @param labels train labels
     * @return model
     */
    private svm_model train(Vector<svm_node[]> set, Vector<Double> labels, boolean saveModel) {

		/* set svm problem */
        svm_problem problem = new svm_problem();
        problem.l = set.size();
        problem.x = new svm_node[problem.l][];
        problem.y = new double[problem.l];
        for (int i = 0; i < problem.l; i++) {
            problem.x[i] = set.get(i);
            problem.y[i] = labels.get(i);
        }

        /* train svm model */
        String error_msg = svm.svm_check_parameter(problem, this.param);
        if (error_msg == null) {
            svm_model model = svm.svm_train(problem, this.param);
            if (saveModel) {
                try {
                    svm.svm_save_model(this.modelFile, model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return model;
        } else {
            System.out.println("svm parameter error:");
            System.out.println(error_msg);
            return null;
        }
    }

    /**
     * predicte the labels of test data
     *
     * @param model model trained by train
     * @param data  data used to test the model
     */
    public void test(svm_model model, SVMData data, String resultFile) {

        Vector<svm_node[]> set = data.getData(SVMData.data_type.scaled);
        Vector<Double> labels = data.getLabels();

        /* preparation for the log file */
        Date now = new Date();
        String suffix = dateFormat.format(now);

        try (FileWriter fileWriter = new FileWriter(resultFile + suffix + ".log");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            int hit = 0;

            for (int i = 0; i < data.getSampleNum(); i++) {
                svm_node[] sample = set.get(i);
                double real_label = labels.get(i);
                double predict_label = svm.svm_predict(model, sample);

                bufferedWriter.append("predict label: ")
                        .append(Double.toString(predict_label))
                        .append("; real label: ")
                        .append(Double.toString(real_label))
                        .append(' ');

                for (int j = 0; j < data.getFeatureNum(); j++) {
                    bufferedWriter.append(Integer.toString(sample[j].index))
                            .append(':')
                            .append(Double.toString(sample[j].value))
                            .append(' ');
                }
                bufferedWriter.append('\n');
                bufferedWriter.flush();
                if (Math.abs(predict_label - real_label) < 0.001) {
                    hit++;
                }
            }
            double hitRate = 100.0 * hit / data.getSampleNum();
            System.out.println("SVM accuracy: " + String.format("%.2f", hitRate) + "%");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * valid model accuracy
     *
     * @param model  model to valid
     * @param set    test set
     * @param labels test labels
     * @return total hit num in test set
     */
    private int valid(svm_model model, Vector<svm_node[]> set, Vector<Double> labels) {
        int hit = 0;

        for (int i = 0; i < set.size(); i++) {
            svm_node[] sample = set.get(i);
            double real_label = labels.get(i);
            double predict_label = svm.svm_predict(model, sample);
            if (Math.abs(predict_label - real_label) < 0.001) {
                hit++;
            }
        }
        return hit;
    }

    /**
     * search the best svm parameter
     *
     * @param data training data
     * @return svm_parameter
     */
    public svm_parameter updateParam(SVMData data) {
        // no training outputs
        svm.svm_set_print_string_function(svm_print_null);

        int best_power_of_c = C_START_VALUE;
        int best_power_of_g = G_START_VALUE;

        double best_hit_rate = 0.0;

        for (int power_of_c = C_START_VALUE; power_of_c < C_STOP_VALUE; power_of_c += C_STEP) {
            for (int power_of_g = G_START_VALUE; power_of_g < G_STOP_VALUE; power_of_g += G_STEP) {
                // TODO: 17-4-17 hard code 10 fold, but it seems to be reasonable
                double hitRate = crossValidation(data, power_of_c, power_of_g, 10);
                System.out.printf("power of c: " + power_of_c + "; power of g: " + power_of_g + "; hit rate: %.2f%%", hitRate);
                if (hitRate > 0.6
                        && (
                        (hitRate > best_hit_rate)
                                || (
                                Math.abs(hitRate - best_hit_rate) < 0.00001
                                        && power_of_c < best_power_of_c
                        ))) {
                    best_hit_rate = hitRate;
                    best_power_of_c = power_of_c;
                    best_power_of_g = power_of_g;
                }
                System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
//                if (hitRate < 0.6) { // pruning hehe
//                    System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
//                } else if ((hitRate > best_hit_rate)
//                        || (Math.abs(hitRate - best_hit_rate) < 0.00001
//                        && power_of_c < best_power_of_c)) {
//                    best_hit_rate = hitRate;
//                    best_power_of_c = power_of_c;
//                    best_power_of_g = power_of_g;
//                    System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
//                } else {
//                    System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
//                }
            }
        }
        param.C = Math.pow(2, best_power_of_c);
        param.gamma = Math.pow(2, best_power_of_g);
        System.out.println("best C: " + param.C + "; best gamma: " + param.gamma + "; accuracy: " + best_hit_rate);
        return param;
    }

    /**
     * do cross validation
     *
     * @param data       training data
     * @param power_of_c power of c, see{@link svm_parameter#C}
     * @param power_of_g power of g, see{@link svm_parameter#gamma}
     * @param fold_n     the number n of n fold validation
     * @return best accuracy under this set of c and g
     */
    private double crossValidation(SVMData data, int power_of_c, int power_of_g, int fold_n) {
        Vector<svm_node[]> set = data.getData(SVMData.data_type.scaled);
        Vector<Double> labels = data.getLabels();

        param.C = Math.pow(C_BASE, power_of_c);
        param.gamma = Math.pow(G_BASE, power_of_g);

        int total_hit = 0;
        for (int i = 0; i <= fold_n; i++) {
            Vector<svm_node[]> trainData = new Vector<>();
            Vector<svm_node[]> validData = new Vector<>();
            Vector<Double> trainLabels = new Vector<>();
            Vector<Double> validLabels = new Vector<>();

            int vs_len = set.size() / fold_n;
            int vs_start = i * vs_len;
            int vs_end = i == fold_n ? set.size() : (i + 1) * vs_len;

            for (int j = 0; j < vs_start; j++) {
                trainData.add(set.get(j));
                trainLabels.add(labels.get(j));
            }
            for (int j = vs_start; j < vs_end; j++) {
                validData.add(set.get(j));
                validLabels.add(labels.get(j));
            }
            for (int j = vs_end; j < set.size(); j++) {
                trainData.add(set.get(j));
                trainLabels.add(labels.get(j));
            }
            svm_model model = train(trainData, trainLabels, false);
            total_hit += valid(model, validData, validLabels);
        }
        // n is in set.size()
        return 100.0 * total_hit / set.size();
    }

    //~ Getter/Setter Methods --------------------------------------------------

    public void setModelFile(String _modelFile) {
        modelFile = _modelFile;
    }

    /**
     * set the svm type
     *
     * @param svmType see{@link svm_parameter#svm_type}
     */
    public void setSVMType(int svmType) {
        param.svm_type = svmType;
    }

    /**
     * set the svm kernal type
     *
     * @param kernelType see{@link svm_parameter#kernel_type}
     */
    public void setKernelType(int kernelType) {
        param.kernel_type = kernelType;
    }

    /**
     * set the svm C
     *
     * @param C see{@link svm_parameter#C}
     */
    public void setC(int C) {
        param.C = C;
    }

    /**
     * set the svm gamma
     *
     * @param gamma see{@link svm_parameter#gamma}
     */
    public void setGamma(double gamma) {
        param.gamma = gamma;
    }

    /**
     * set the svm eps
     *
     * @param eps see{@link svm_parameter#eps}
     */
    public void setEps(double eps) {
        param.eps = eps;
    }
}