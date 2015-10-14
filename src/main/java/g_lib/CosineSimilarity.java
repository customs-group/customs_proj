package g_lib;


import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CosineSimilarity
 * Created by edwardlol on 15/10/13.
 */
public class CosineSimilarity {

    /**
     * 判断输入是否为汉字
     * @param ch 输入的字符
     * @return boolean
     */
    public static boolean isChineseChar(char ch) {
        return (ch >= 0x4E00 && ch <= 0x9FA5);
    }

    /**
     * 根据输入的Unicode字符, 获取它的GB2312编码或者ascii编码
     * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) { // GB2312编码下一个汉字用两个字节表示
                return -1;
            }
            int b0 = (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Boolean isEnWord(String content) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public static void seperateENCH(String content) {

    }

    public static double getSimilarity(String doc1, String doc2) {
        if (doc1 == null || doc1.trim().length() == 0) {
            System.out.println("Document 1 is empty!");
            return 0;
        }
        if (doc2 == null || doc2.trim().length() == 0) {
            System.out.println("Document 2 is empty!");
            return 0;
        }

        // 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
        Map<Integer, int[]> AlgorithmMap = new HashMap<>();
        for (int i = 0; i < doc1.length(); i++) {
            char d1 = doc1.charAt(i);
            if (isChineseChar(d1)) {
                int charIndex = getGB2312Id(d1);
                if (charIndex != -1) {
                    int[] fq = AlgorithmMap.get(charIndex);
                    if(fq != null && fq.length == 2){
                        fq[0]++;
                    } else {
                        fq = new int[2];
                        fq[0] = 1;
                        fq[1] = 0;
                        AlgorithmMap.put(charIndex, fq);
                    }
                }
            }
        }
        for (int i = 0; i < doc2.length(); i++) {
            char d2 = doc2.charAt(i);
            if (isChineseChar(d2)) {
                int charIndex = getGB2312Id(d2);
                if (charIndex != -1) {
                    int[] fq = AlgorithmMap.get(charIndex);
                    if (fq != null && fq.length == 2) {
                        fq[1]++;
                    } else {
                        fq = new int[2];
                        fq[0] = 0;
                        fq[1] = 1;
                        AlgorithmMap.put(charIndex, fq);
                    }
                }
            }
        }

        Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
        double sqdoc1 = 0;
        double sqdoc2 = 0;
        double numerator = 0;
        while (iterator.hasNext()) {
            int[] c = AlgorithmMap.get(iterator.next());
            numerator += c[0] * c[1];
            sqdoc1 += c[0] * c[0];
            sqdoc2 += c[1] * c[1];
        }
        return numerator / Math.sqrt(sqdoc1 * sqdoc2);
    }


    public static void main(String[] args) {
        String aaa = "edwardlol";
        String bbb = "edward";
        System.out.println(getSimilarity(aaa, bbb));

        String[] saaa = aaa.split("[|,.()\\s]");
        for (String temp : saaa) {
            System.out.println(temp);
        }

        if (isEnWord("agas3451345d呵呵")) {
            System.out.println("en");
        } else {
            System.out.println("not en");
        }

    }
}
