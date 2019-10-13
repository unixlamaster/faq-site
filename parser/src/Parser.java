package faq;
import org.htmlcleaner.*;
import java.io.*;
import java.net.URL;
import faq.ReadConfig;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.mongodb.*;
    
public class Parser {
    static String find_tag(TagNode html, String xpath) {
        try {
            Object[] tags = html.evaluateXPath(xpath);
            if (tags != null && tags.length > 0) {                        
                return tags[0].toString().trim();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
   public static BasicDBObject page_parser(HtmlCleaner htmlCleaner, Map<String, Object> config) {
      BasicDBObject body = new BasicDBObject();
      try {
          TagNode html = htmlCleaner.clean(new URL((String)config.get("page_url")));     
          Map<String, Object> fields = (Map<String, Object>)config.get("fields");
          for (Map.Entry<String, Object> pair: fields.entrySet()) {
             System.out.print("key="+pair.getKey());
             System.out.println(" xpath="+pair.getValue());
             String tag_value = find_tag(html, (String)pair.getValue());
             System.out.println("value="+tag_value);
          }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null; 
  }
  public static void main(String[] args) throws Exception {
    System.out.println(String.join(", ", args));
    final CleanerProperties props = new CleanerProperties();
    final HtmlCleaner htmlCleaner = new HtmlCleaner(props);
    final SimpleHtmlSerializer htmlSerializer = 
        new SimpleHtmlSerializer(props);
    Map<String, Object> config = ReadConfig.map("config.yaml");
    for (Map.Entry<String, Object> pair: config.entrySet()) {
         System.out.println(pair.getKey()+"=>"+pair.getValue());
    }     
    // make 10 threads using the same cleaner and the same serializer 

    for (int i = 1; i <= 1; i++) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BasicDBObject body = page_parser(htmlCleaner, config);
            }
        });
        t.start();
        t.join();	
    }
  }
}
