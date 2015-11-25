package container;

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
import org.jfree.ui.RefineryUtilities;
import util.EdMath;
import util.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by edwardlol on 15/11/13.
 */
public class Container_Stastics {
    private static int[] days_array;
    private static HashMap<Integer, Integer> days_map;
    private static HashMap<String, Integer> id_map;

    /**
     * transfrom days data from array to map
     */
    public static void make_map() {
        days_map = new HashMap<>();
        for (int day : days_array) {
            Integer cnt = 1;
            if (days_map.containsKey(day)) {
                cnt += days_map.get(day);
            }
            days_map.put(day, cnt);
        }
    }

    /**
     * check if the data matches normal distribution
     * @return p value in Kolmogorov-Smirnov Test
     */
    public static double check_ND() {
        double[] days_in_double = new double[days_array.length];
        for (int i = 0; i < days_array.length; i++) {
            days_in_double[i] = days_array[i];
        }
        double mean = EdMath.calculate_mean(days_array);
        double sd = EdMath.calculate_sd(days_array);
        org.apache.commons.math3.distribution.NormalDistribution nd = new org.apache.commons.math3.distribution.NormalDistribution(mean, sd);

        KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
        double p = kst.kolmogorovSmirnovStatistic(nd, days_in_double);
        System.out.println("mean: " + mean + "; stantard deviation: " + sd + "; P: " + p);
        return p;
    }

    /**
     * display the top days ordered by count desc
     * @param top_cnt number of days to display
     */
    public static void disp_top(int top_cnt) {
        BigDecimal total_days = BigDecimal.valueOf(days_array.length);
        // sort by count
        List<Map.Entry<Integer, Integer>> value_list = new ArrayList<>(days_map.entrySet());
        Collections.sort(value_list, (Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) ->
                entry2.getValue().compareTo(entry1.getValue())
        );

        System.out.println("total_days: " + total_days + "; max days: " + days_array[days_array.length - 1]);
        Iterator<Map.Entry<Integer, Integer>> it = value_list.iterator();
        for (int i = 0; i < top_cnt; i++) {
            Map.Entry<Integer, Integer> entry = it.next();
            double per = BigDecimal.valueOf(entry.getValue() * 100).divide(total_days, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("day: " + entry.getKey() + "; count: " + entry.getValue() + "; per: " + per);
        }
    }

    /**
     *
     * @param step
     */
    public static void disp_data(int step) {
        System.out.println("---------------------------------");
        for (int i = 0; i < days_array.length; i += (days_array.length / step)) {
            System.out.print(days_array[i] + "\t");
        }
        System.out.println("");
        System.out.println("---------------------------------");
    }

    /**
     * 通过数据集生成图
     * @param categorydataset 数据集
     * @return 图对象jfreechart
     */
    private static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createLineChart(
                "集装箱境外游天数统计", // 图表标题
                "days", // X轴标签
                "count", // Y轴标签
                categorydataset, // 数据集
                PlotOrientation.VERTICAL, // 方向
                false, // 是否包含图例
                true, // 提示信息是否显示
                false); // 是否使用urls

        // 设置图表的背景颜色
        jfreechart.setBackgroundPaint(Color.white);

        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray); // 坐标轴范围内的颜色
//        categoryplot.setRangeGridlinePaint(Color.white); // 和点上的坐标值颜色有关
        categoryplot.setRangeGridlinesVisible(false); // 网格,超难看

        // 设置X轴上的Lable让其45度倾斜
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 设置X轴上的Lable让其45度倾斜
        domainAxis.setLowerMargin(0.0); // 设置距离图片左端距离
        domainAxis.setUpperMargin(0.0); // 设置距离图片右端距离

        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();

        lineandshaperenderer.setDrawOutlines(true);
        lineandshaperenderer.setUseFillPaint(true);
        lineandshaperenderer.setBaseFillPaint(Color.white);
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
        lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
        lineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));
//        lineandshaperenderer.setItemMargin(0.4); // 设置x轴每个值的间距（不起作用？？）

        // 显示数据值
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");// 数据点显示数据值的格式
        lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}", decimalformat1)); //  设置数据项标签的生成器
        lineandshaperenderer.setBaseItemLabelsVisible(true); // 基本项标签显示
        lineandshaperenderer.setBaseShapesFilled(true); // 在数据点显示实心的小图标

        return jfreechart;
    }

    /**
     * 生成数据集, main work should be here
     * @return defaultcategorydataset
     */
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        // addValue(Y值，图例名，X值)
        days_map.forEach((days, count) -> {
            defaultcategorydataset.addValue(count, "First", days);
        });
        return defaultcategorydataset;
    }

    /**
     *
     * @param file file name to save the pic
     * @return idk
     */
    public static JPanel draw(String file) {
        JFreeChart jfreechart = createChart(createDataset());
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
     *
     * @return
     */
    public static int get_max_derivate() {
        double max_derivate = Double.MIN_VALUE;
        int the_day = 0;
        // sort by days
        List<Integer> keyList = days_map.keySet().stream().collect(Collectors.toList());
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size() - 1; i++) {
            int key = keyList.get(i);
            int next_key = keyList.get(i + 1);
            int base_day = days_map.get(key);
            int cmp_day = days_map.get(next_key);
            double temp = 1.0 * (cmp_day - base_day) / base_day;
            if (temp > max_derivate) {
                max_derivate = temp;
                the_day = next_key;
            }
        }
        return the_day;
    }

    /**
     *
     * @param days
     */
    public static void get_conta_id_list(int days) {
        id_map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            FileReader fr = new FileReader(FileManager.conta_file);
            BufferedReader br = new BufferedReader(fr);
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
                    if (id_map.containsKey(id)) {
                        count += id_map.get(id);
                    }
                    id_map.put(id, count);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * record days info to file
     * @param file record file name
     * @param low_bound min days of record
     * @param high_bound max days of record
     */
    public static void record_days(String file, int low_bound, int high_bound) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            int low_index = 0, high_index = days_map.size();
            List<Integer> keyList = days_map.keySet().stream().collect(Collectors.toList());
            Collections.sort(keyList);
            for (int i = 0; i < keyList.size(); i++) {
                if (keyList.get(i) < low_bound) {
                    low_index++;
                }
                if (keyList.get(i) > high_bound) {
                    high_index = i;
                    break;
                }
            }
            
            List<Integer> keyList2 = new ArrayList<>(keyList.subList(low_index, high_index));
            for (Integer key : keyList2) {
                Integer count = days_map.get(key);
                bw.append(Integer.toString(key));
                bw.append(": ");
                bw.append(Integer.toString(count));
                bw.append("\n");
                bw.flush();
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file
     */
    public static void record_ids(String file) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            System.out.println("container count: " + id_map.size());

            List<Map.Entry<String, Integer>> value_list = new ArrayList<>(id_map.entrySet());
            Collections.sort(value_list, (Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) ->
                    entry2.getValue().compareTo(entry1.getValue())
            );

            Iterator<Map.Entry<String, Integer>> it = value_list.iterator();
            for (int i = 0; i < value_list.size(); i++) {
                Map.Entry<String, Integer> entry = it.next();
                bw.append(entry.getKey());
                bw.append(": ");
                bw.append(Integer.toString(entry.getValue()));
                bw.append("\n");
                bw.flush();

            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        days_array = FileManager.read_thm(FileManager.conta_file);

        Arrays.sort(days_array);
        make_map();
        record_days(FileManager.days_static, 0, days_map.size()+1);

        disp_top(8);

        disp_data(10);

        int the_day = get_max_derivate();
        System.out.println("the day: " + the_day);

        get_conta_id_list(the_day);

        record_ids(FileManager.conta_static);


        draw("./aaa.png");
    }
}
