package faq;
import org.htmlcleaner.*;
import java.io.*;
import java.net.URL;
import faq.ReadConfig;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.mongodb.*;
    
public class Parser {
    static String find_tag(TagNode html, String xpath, Integer index) {
        try {
            Object[] tags = html.evaluateXPath(xpath);
            if (tags != null && tags.length > 0) {                        
                return tags[index].toString().trim();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
    static Integer pop_config_key(Map<String, Object> hash,List keys,String key,Integer def) {
        Integer val = def;
        if (hash.get(key)!=null) {
            val = (Integer)hash.get(key);
            keys.remove(keys.indexOf(key));
        }
        return val;
    }
    static ArrayList<BasicDBObject> config_hash_parse(TagNode html, Map<String, Object> hash) {
        List<String> keys = new ArrayList(hash.keySet());
        Integer index = pop_config_key(hash,keys,"index",0);
        Integer multi = pop_config_key(hash,keys,"multi",1);
        ArrayList<BasicDBObject> docs = new ArrayList<BasicDBObject>();
        for(Integer i=0;i<multi;i++) {
            BasicDBObject doc = new BasicDBObject();
            for(String key: keys) {
//               System.out.print("key="+key);
//               System.out.println(" xpath="+hash.get(key));
               String tag_value = find_tag(html, (String)hash.get(key), index+i);
//               System.out.println("value="+tag_value);
               doc.put(key,tag_value);
            }
            docs.add(doc);
        }
        return docs;
    }
   public static BasicDBObject page_parser(HtmlCleaner htmlCleaner, Map<String, Object> config) {
       BasicDBObject doc = new BasicDBObject();
       try {
          TagNode html = htmlCleaner.clean(new URL((String)config.get("page_url")));
          doc.put("page_name",(String)config.get("page_name"));
          doc.put("page_url",(String)config.get("page_url"));
          Object check_obj = (Object)config.get("fields");
          if (check_obj.getClass().getName()=="java.util.ArrayList") {
              ArrayList fields = (ArrayList)config.get("fields");
              for (Object ohash: fields) {
                  Map<String, Object> hash = (Map<String, Object>)ohash;
                  doc.put("fields",config_hash_parse(html,hash));
              }
          } else {
              if (check_obj.getClass().getName()=="java.util.LinkedHashMap") {
                  Map<String, Object> fields = (Map<String, Object>)config.get("fields");
                  doc.put("fields",config_hash_parse(html,fields));
              } else {
                  Error ref = new Error(); // создаем экземпляр
                  throw ref;               // "бросаем" его
              }
          }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return doc; 
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
    Mongo mongo = new Mongo();
    DB db = mongo.getDB( args[0] );
    DBCollection coll = db.getCollection("pages");
    BasicDBObject query = new BasicDBObject();
    // условие, что поле profession должно иметь значение engineer
    query.put("page_url", (String)config.get("page_url"));
    // получение объекта DBCursor на основе запроса 
    DBCursor cur = coll.find(query);  
    cur.remove();
    for (int i = 1; i <= 1; i++) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                coll.insert(page_parser(htmlCleaner, config));
            }
        });
        t.start();
        t.join();	
    }
  }
}
