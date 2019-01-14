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

    /**
     * 判断字符串是否为合法的变量名
     * @param name
     * @return
     */
    public boolean isValidVarName(String name) {
        if (name == null || name.length() == 0 || name.trim() == "") {
            return false;
        }

        //check the first character
        char first = name.charAt(0);
        if(!isFirstChar(first))
        {
            return false;
        }

        //check the content of the name after the first character
        for(int i = 1; i < name.length(); i++){
            char c = name.charAt(i);
            if((!Character.isLetterOrDigit(c)) && (c != '_'))
                return false;
        }

        return true;
    }

    /**
     * @param A character
     * @return true if the char contains in the list else return false
     */
    private static boolean isFirstChar(char c)
    {
        switch(c){
            case 'A': return true;
            case 'B': return true;
            case 'C': return true;
            case 'D': return true;
            case 'E': return true;
            case 'F': return true;
            case 'G': return true;
            case 'H': return true;
            case 'I': return true;
            case 'J': return true;
            case 'K': return true;
            case 'L': return true;
            case 'M': return true;
            case 'N': return true;
            case 'O': return true;
            case 'P': return true;
            case 'Q': return true;
            case 'R': return true;
            case 'S': return true;
            case 'T': return true;
            case 'U': return true;
            case 'V': return true;
            case 'W': return true;
            case 'X': return true;
            case 'Y': return true;
            case 'Z': return true;
            case 'a': return true;
            case 'b': return true;
            case 'c': return true;
            case 'd': return true;
            case 'e': return true;
            case 'f': return true;
            case 'g': return true;
            case 'h': return true;
            case 'i': return true;
            case 'j': return true;
            case 'k': return true;
            case 'l': return true;
            case 'm': return true;
            case 'n': return true;
            case 'o': return true;
            case 'p': return true;
            case 'q': return true;
            case 'r': return true;
            case 's': return true;
            case 't': return true;
            case 'u': return true;
            case 'v': return true;
            case 'w': return true;
            case 'x': return true;
            case 'y': return true;
            case 'z': return true;
            case '_': return true;
            case '$': return true;
        }
        return false;
    }
}
