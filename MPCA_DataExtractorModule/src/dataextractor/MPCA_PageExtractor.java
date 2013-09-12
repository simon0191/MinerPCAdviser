/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author simon
 */
public class MPCA_PageExtractor {
    public static Pattern SPLITTER = Pattern.compile("[ ]+[0-9]+");
    //public static String CHROME_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) "
            //+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36";
    public static String CHROME_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/28.0.1468.0 Safari/537.36";
    
    public static Map<String,Element> get(String url, Map<String,List<MPCA_Selector> > descriptor) throws IOException {
        //Document doc = Jsoup.connect(url).userAgent(MPCA_PageExtractor.CHROME_AGENT).get();
        //Document doc = Jsoup.parse(new URL(url), 3000);
        Document doc = Jsoup.parse(new URL(url), 10000);
        
        Set<String> keys = descriptor.keySet();
        Map<String,Element> result = new HashMap<String,Element>();
        
        for(String k:keys) {
            
            List<MPCA_Selector> selectors = descriptor.get(k);
            Element e = doc.body();
            for(int i = 0;i<selectors.size();++i) {
                Elements els = e.select(selectors.get(i).getSelector());
                if(!els.isEmpty())
                    e = els.get(selectors.get(i).getChildNumber());
                else {
                    e = null;
                    break;
                }
            }
            result.put(k, e);
        }
        return result;
        
    }
    public static Map<String,List<MPCA_Selector>> parseDescriptor(String str) {
        
        HashMap<String,List<MPCA_Selector>> descriptor = new HashMap<String,List<MPCA_Selector>>();
        
        String[] lines = str.split("\n+");
        
        for(int i = 0;i<lines.length;++i) {
            int firstSpace = lines[i].indexOf(' ');
            String key = lines[i].substring(0, firstSpace);
            String selector = lines[i].substring(firstSpace).trim();
            Matcher m = SPLITTER.matcher(selector);
           
            ArrayList<Pair> pos = new ArrayList<Pair>();
            int lastStart = 0;
            while(m.find()) {
                pos.add(new Pair(lastStart,m.start()));
                pos.add(new Pair(m.start(),m.end()));
                lastStart = m.end();
            }
            if(lastStart != selector.length()) {
                pos.add(new Pair(lastStart,selector.length()));
            }
            
            List<MPCA_Selector> selectors = new ArrayList<MPCA_Selector>();
            for(int j = 0;j<pos.size();++j) {
                if(j+1 < pos.size()) {
                    selectors.add(new MPCA_Selector(
                            selector.substring(pos.get(j).first,pos.get(j).second).trim(),
                            Integer.parseInt(selector.substring(pos.get(j+1).first,pos.get(j+1).second).trim())
                            ));    
                    ++j;
                }
                else{
                    System.out.println("Selector: " + selector.substring(pos.get(j).first,pos.get(j).second));
                    selectors.add(new MPCA_Selector(
                            selector.substring(pos.get(j).first,pos.get(j).second)));
                }
                
            }
            descriptor.put(key, selectors);
            
            /*for (MPCA_Selector selector1 : selectors) {
                System.out.println("selector = " + selector1.getSelector() + ", child = " + selector1.getChildNumber());
            }*/
        }
        return descriptor;
    }
    public static Map<String,List<MPCA_Selector>> parseDescriptor(File file) throws FileNotFoundException, IOException {
        StringBuffer fileData = readFile(file);
        return parseDescriptor(fileData.toString());
    }

    public static StringBuffer readFile(File file) throws FileNotFoundException, IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData;
    }
}
class Pair {
    int first,second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }
    
}