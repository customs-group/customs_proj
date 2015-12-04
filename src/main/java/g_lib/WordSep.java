package g_lib;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by edwardlol on 15/12/3.
 */
public class WordSep {
    public static void do_sep(String source_file, String target_file) {
        String symbol_re = "[`~!@#\\$%\\^&\\*\\(\\)\\-_=\\+[{]}\\|;:'\",<\\.>/\\?\\s]+";
        Pattern all_symbol = Pattern.compile("^" + symbol_re + "$");
        try {
            FileReader fr = new FileReader(source_file);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(target_file);
            BufferedWriter bw = new BufferedWriter(fw);

            String line = br.readLine();
            while (line != null) {
                String[] content = line.split("\t");
                if (content.length != 6) {
                    System.out.println("end file format error");
                    System.out.println(line);
                    line = br.readLine();
                    continue;
                }

                bw.append(content[0]);
                bw.append(":::");
                bw.append(content[1]);
                bw.append("\t");

                bw.append(content[5]);
                bw.append("\t");

                List<Term> parseResultList = NlpAnalysis.parse(content[5]);
                Iterator it = parseResultList.iterator();
                while (it.hasNext()) {
                    Term term = (Term) it.next();
                    String sec = term.getName();
                    Matcher matcher = all_symbol.matcher(sec);
                    if (!matcher.find()) { // 不全是符号
                        bw.append(sec);
                        if (it.hasNext()) {
                            bw.append(":::");
                        }
                    }
                }
                bw.append("\n");
                bw.flush();
                line = br.readLine();
            }
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        do_sep("datasets/original/end8703", "datasets/sep_8703");
    }
}
