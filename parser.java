import org.htmlcleaner.*;
import java.io.*;
import java.net.URL;
    
class Parser {
  public static void main(String[] args) {
    System.out.println("!!!");
    final CleanerProperties props = new CleanerProperties();
    final HtmlCleaner htmlCleaner = new HtmlCleaner(props);
    final SimpleHtmlSerializer htmlSerializer = 
        new SimpleHtmlSerializer(props);
     
    // make 10 threads using the same cleaner and the same serializer 
    for (int i = 1; i <= 1; i++) {
        final String url = "https://otvet.mail.ru/question/216156098";
        final String fileName = "c:/temp/ebay_art" + i + ".xml";
        new Thread(new Runnable() {
            public void run() {
                try {
                    TagNode html = htmlCleaner.clean(new URL(url));
                    Object[] header = html.evaluateXPath("//h1[@class='q--qtext']/text()");
                    System.out.print("header\t: ");
                    if (header != null && header.length > 0) {                        
                        System.out.println(header[0].toString().trim());
                    } else {
                        System.out.println("not found");
                    }
    //                htmlSerializer.writeToFile(tagNode, fileName, "utf-8");
                } catch (IOException | XPatherException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
  }
}