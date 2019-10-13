package faq;

import com.fasterxml.jackson.dataformat.yaml.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

public class ReadConfig {
  private static Map<String, Object> readYamlConfig(String yaml) {
    try {
      ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
      Map<String, Object> obj = yamlReader.readValue(yaml, new TypeReference<Map<String,Object>>(){});
      return obj;
    } catch(Exception ioe)
      {
          System.out.println("I/O Exception: " + ioe);
          return null;
      }
  }
  
  public static Map<String, Object> map(String config_file_name){
    try
    {
        String content = Files.readString(Paths.get(config_file_name));
        Map<String, Object> config = readYamlConfig(content);
        return config;
     } catch(Exception ioe)
      {
          System.out.println("I/O Exception: " + ioe); 
      }    
      return null;
  }
}

