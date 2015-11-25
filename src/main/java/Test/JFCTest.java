package Test;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * Created by edwardlol on 15/11/24.
 */
public class JFCTest {
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
        defaultcategorydataset.addValue(212, "First", "900");
        defaultcategorydataset.addValue(504, "First", "1001");
        defaultcategorydataset.addValue(1520, "First", "1210");

        return defaultcategorydataset;
    }

    /**
     *
     * @param file file name to save the pic
     * @return idk
     */
    public static JPanel createDemoPanel(String file) {
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

    public static void main(String[] args) {
        JPanel jPanel = createDemoPanel("./test.png");
        //jPanel.setPreferredSize(new Dimension(500, 270));
    }
}
