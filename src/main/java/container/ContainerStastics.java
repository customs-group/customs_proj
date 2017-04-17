package container;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import util.EdMath;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by edwardlol on 15/11/13.
 */
public class ContainerStastics {
    private static int[] daysArray;
    private static HashMap<Integer, Integer> daysMap;
    private static Multiset<Integer> daysSet;
    private static HashMap<String, Integer> idMap;

    public void init(String thmFile) {
        ThmFileReader reader = ThmFileReader.getInstance();
        daysArray = reader.read(thmFile);
        Arrays.sort(daysArray);
        daysSet = HashMultiset.create();
        for (int day : daysArray) {
            daysSet.add(day);
        }
    }

    /**
     * check if the data matches normal distribution
     * <p>
     * Kolmogorov-Smirnov是比较一个频率分布f(x)与理论分布g(x)或者两个观测值分布的检验方法
     *
     * @return p value in Kolmogorov-Smirnov Test
     */
    public boolean check_ND() {
        if (daysArray == null) {
            System.err.println("must init first!");
            return false;
        }

        double[] daysInDouble = new double[daysArray.length];
        for (int i = 0; i < daysArray.length; i++) {
            daysInDouble[i] = daysArray[i];
        }

        double mean = EdMath.mean(daysArray);
        double sd = EdMath.standardDeviation(daysArray);

        NormalDistribution nd = new NormalDistribution(mean, sd);
        KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
        double p = kst.kolmogorovSmirnovStatistic(nd, daysInDouble);
        System.out.println("mean: " + mean + "; stantard deviation: " + sd + "; P: " + p);
        return p > 0.05d;
    }

    /**
     * display the top days ordered by count desc
     *
     * @param top_cnt number of days to display
     */
    public static void disp_top(int top_cnt) {
        BigDecimal total_days = BigDecimal.valueOf(daysArray.length);
        // sort by count
        List<Map.Entry<Integer, Integer>> value_list = new ArrayList<>(daysMap.entrySet());
        value_list.sort((Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) ->
                entry2.getValue().compareTo(entry1.getValue()));

        System.out.println("total_days: " + total_days + "; max days: " + daysArray[daysArray.length - 1]);
        Iterator<Map.Entry<Integer, Integer>> it = value_list.iterator();
        for (int i = 0; i < top_cnt; i++) {
            Map.Entry<Integer, Integer> entry = it.next();
            double per = BigDecimal.valueOf(entry.getValue() * 100).divide(total_days, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("day: " + entry.getKey() + "; count: " + entry.getValue() + "; per: " + per);
        }
    }

    /**
     * @param step
     */
    public static void disp_data(int step) {
        System.out.println("---------------------------------");
        for (int i = 0; i < daysArray.length; i += (daysArray.length / step)) {
            System.out.print(daysArray[i] + "\t");
        }
        System.out.println("");
        System.out.println("---------------------------------");
    }

    /**
     * 生成数据集, main work should be here
     *
     * @return defaultcategorydataset
     */
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        // addValue(Y值，图例名，X值)
        for (Multiset.Entry<Integer> entry : daysSet.entrySet()) {
            defaultcategorydataset.addValue(entry.getCount(), "First", entry.getElement());
        }
//        daysMap.forEach((days, count) -> defaultcategorydataset.addValue(count, "First", days));
        return new SlidingCategoryDataset(defaultcategorydataset, 0, daysMap.size());
    }

    /**
     * 通过数据集生成图
     *
     * @return 图对象jfreechart
     */
    private static JFreeChart createChart() {
        CategoryDataset categorydataset = createDataset();

        JFreeChart jfreechart = ChartFactory.createLineChart(
                "集装箱境外游天数统计",  // 图表标题
                "days",      // X轴标签
                "count",       // Y轴标签
                categorydataset,            // 数据集
                PlotOrientation.VERTICAL,   // 方向
                false,               // 是否包含图例
                true,              // 提示信息是否显示
                false);               // 是否使用urls

        // 设置图表的背景颜色
        jfreechart.setBackgroundPaint(Color.white);

        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        // 坐标轴范围内的颜色
        categoryplot.setBackgroundPaint(Color.lightGray);
        // 网格,超难看
        categoryplot.setRangeGridlinesVisible(false);

        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        // 设置X轴上的Lable让其45度倾斜
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.0);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.0);

        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();

        lineandshaperenderer.setDrawOutlines(true);
        lineandshaperenderer.setUseFillPaint(true);
        lineandshaperenderer.setBaseFillPaint(Color.white);
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
        lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
        lineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));

        // 显示数据值
        // 数据点显示数据值的格式
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");
        // 设置数据项标签的生成器
        lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}", decimalformat1));
        // 基本项标签显示
        lineandshaperenderer.setBaseItemLabelsVisible(true);
        // 在数据点显示实心的小图标
        lineandshaperenderer.setBaseShapesFilled(true);

        return jfreechart;
    }

    /**
     * @param file file name to save the pic
     * @return idk
     */
    public JPanel draw(String file) {
        JFreeChart jfreechart = createChart();
        try {
            ChartUtilities.saveChartAsPNG(
                    new File(file), // 不能创建文件,暂时用现有的糊弄
                    jfreechart, // 图表对象
                    1366, // 宽
                    768); // 高
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ChartPanel(jfreechart);
    }

    /**
     * @return
     */
    public static int get_max_derivate() {
        double max_derivate = Double.MIN_VALUE;
        int the_day = 0;
        // sort by days
        List<Integer> keyList = new ArrayList<>(daysMap.keySet());
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size() - 1; i++) {
            int key = keyList.get(i);
            int next_key = keyList.get(i + 1);
            int base_day = daysMap.get(key);
            int cmp_day = daysMap.get(next_key);
            double temp = 1.0 * (cmp_day - base_day) / base_day;
            if (temp > max_derivate) {
                max_derivate = temp;
                the_day = next_key;
            }
        }
        return the_day;
    }

    /**
     * @param days
     */
    public static void get_conta_id_list(int days) {
        idMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (FileReader fr = new FileReader("datasets/original/thm_conta_ei");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            while (line != null) {
                String[] attrs = line.split("@@");
                if (attrs.length != 24) {
                    break;
                }
                if (sdf.parse(attrs[3]).getTime() - sdf.parse(attrs[4]).getTime() <= 0
                        && Integer.parseInt(attrs[5]) == days) {
                    String id = attrs[2];
                    Integer count = 1;
                    if (idMap.containsKey(id)) {
                        count += idMap.get(id);
                    }
                    idMap.put(id, count);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void recordDays(String file, int lowBound) {
        recordDays(file, lowBound, daysMap.size() + 1);
    }

    /**
     * record days info to file
     *
     * @param file      record file name
     * @param lowBound  min days of record
     * @param highBound max days of record
     */
    public void recordDays(String file, int lowBound, int highBound) {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {

//            int low_index = 0, high_index = daysMap.size();
            int low_index = 0, high_index = daysSet.size();
//            List<Integer> keyList = new ArrayList<>(daysMap.keySet());
            List<Integer> keyList = new ArrayList<>(daysSet.elementSet());
            Collections.sort(keyList);
            for (int i = 0; i < keyList.size(); i++) {
                if (keyList.get(i) < lowBound) {
                    low_index++;
                }
                if (keyList.get(i) > highBound) {
                    high_index = i;
                    break;
                }
            }

            List<Integer> keyList2 = new ArrayList<>(keyList.subList(low_index, high_index));
            for (Integer key : keyList2) {
                Integer count = daysMap.get(key);
                bw.append(Integer.toString(key))
                        .append(':').append(' ')
                        .append(Integer.toString(count))
                        .append('\n');
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     */
    public static void record_ids(String file) {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {

            System.out.println("container count: " + idMap.size());

            List<Map.Entry<String, Integer>> value_list = new ArrayList<>(idMap.entrySet());
            value_list.sort((Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) ->
                    entry2.getValue().compareTo(entry1.getValue()));

            Iterator<Map.Entry<String, Integer>> it = value_list.iterator();
            for (int i = 0; i < value_list.size(); i++) {
                Map.Entry<String, Integer> entry = it.next();
                bw.append(entry.getKey()).append(':').append(' ').append(Integer.toString(entry.getValue())).append('\n');
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
