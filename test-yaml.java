package com.faqsite;
import java.io.*;
import java.nio.file.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class test {
  public String convertYamlToJson(String yaml) {
    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
    Object obj = yamlReader.readValue(yaml, Object.class);

    ObjectMapper jsonWriter = new ObjectMapper();
    return jsonWriter.writeValueAsString(obj);
  }
  
  public static void main(String[] args){
      
    try
    {
        String content = Files.readString(Paths.get("config.yaml"));
        System.out.print(content);
  
     } catch(IOException ioe)
      {
          System.out.println("I/O Exception: " + ioe); 
      }    
  }
}