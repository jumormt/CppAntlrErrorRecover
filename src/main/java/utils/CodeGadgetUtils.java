package utils;
import java.io.*;
import java.util.*;
import models.*;

public enum CodeGadgetUtils {

    CODE_GADGET_UTILS;

    public ArrayList<String> readFile(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        ArrayList<String> lines = new ArrayList<String>();
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        return lines;
    }

    public LinkedList<Integer> findByLineNum(LinkedList<FuncLocListItem> lineList, Integer line) {
        for (FuncLocListItem item : lineList) {
            if (item.getLineNum().equals(line)) {
                return item.getLineList();
            }
        }
        return null;
    }

    public Integer getLineCount(LinkedList<FuncLocListItem> tmp) {
        Integer ret = 0;
        for (FuncLocListItem item : tmp) {
            ret += item.getLineList().size();
        }
        return ret;
    }

    public void output(LinkedList<FuncLocListItem> tmp, ArrayList<String> fileContent, File f, Integer targetApiLine) throws Exception {
        String header = f.getCanonicalPath() + " " + targetApiLine;
        StringBuilder a = new StringBuilder();
        boolean flag = false;
        for (FuncLocListItem cdg : tmp) {
            for (Integer i : cdg.getLineList()) {
//                if (!flag && flawList.contains(i)) {
//                    flag = true;
//                }
                //resultOut.println(fileContent.get(i-1).trim());
                a.append(fileContent.get(i - 1).trim());
                a.append("\r\n");
            }
        }
        String content = a.toString();
        String md5 = MD5.StringToMd5(a.toString());
        System.out.println(header);
        System.out.println(content);
//        if (flag) {
//            System.out.println("1");
//        }else {
//            System.out.println("0");
//        }
        System.out.println("---------------------------------");
//        if (this.outputHashSet.contains(md5)) {
//            return;
//        } else {
//            this.outputHashSet.add(md5);
//            resultOut.println(header);
//            resultOut.print(content);
//            if (flag) {
//                resultOut.println("1");
//            } else {
//                resultOut.println("0");
//            }
//            resultOut.println("---------------------------------");
//            this.count++;
//            resultOut.flush();
//        }
    }
}
