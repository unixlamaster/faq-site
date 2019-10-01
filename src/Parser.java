package faq;
import org.htmlcleaner.*;
import java.io.*;
import java.net.URL;
import faq.ReadConfig;
import java.util.Map;
    
public class Parser {
  public static void main(String[] args) throws Exception {
    System.out.println("!!!");
    final CleanerProperties props = new CleanerProperties();
    final HtmlCleaner htmlCleaner = new HtmlCleaner(props);
    final SimpleHtmlSerializer htmlSerializer = 
        new SimpleHtmlSerializer(props);
    Map<String, String> config = ReadConfig.map("config.yaml");
    for (Map.Entry<String, String> pair: config.entrySet())
            {
                System.out.println(pair.getKey()+"=>"+pair.getValue());
            }     
    // make 10 threads using the same cleaner and the same serializer 

    for (int i = 1; i <= 1; i++) {
        final String url = "https://otvet.mail.ru/question/216156098";
        final String fileName = "c:/temp/ebay_art" + i + ".xml";
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    TagNode html = htmlCleaner.clean(new URL(url));
                    Object[] header = html.evaluateXPath(config.get("q_header"));
                    System.out.print("header\t: ");
                    if (header != null && header.length > 0) {                        
                        System.out.println(header[0].toString().trim());
                    } else {
                        System.out.println("not found");
                    }
    //                htmlSerializer.writeToFile(tagNode, fileName, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();	
    }
  }
}
