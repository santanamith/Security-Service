package com.poqh.utilities;

import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
/**
 *
 * @author sistem08user
 */
public final class OsUtils {

    /**
     * types of Operating Systems
     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    // cached result of OS detection
    protected static String detectedOS;

    /**
     * detect the operating system from the os.name System property and cache
     * the result
     *
     * @return - the operating system detected
     */
    public static String getOperatingSysstemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                detectedOS = "MacOS";
            } else if (OS.contains("win")) {
                detectedOS = "Windows";
            } else if (OS.contains("nux")) {
                detectedOS = "Linux";
            } else {
                detectedOS = "Other";
            }
        }
        return detectedOS;
    }

    public static String getPathOfVouchersDependingOS() {
        String path = "";
        String detectedOs = OsUtils.getOperatingSysstemType();
        switch (detectedOs) {
            case "MacOS":
                path = "";
                break;
            case "Windows":
                path = "C:/AppServ/www/comprobantes";
                break;
            case "Linux":
                path = "/var/www/html/comprobantes";
                break;
        }
        return path;
    }

    public static String getJSONDependingOS() {
        String path = "";
        String detectedOs = OsUtils.getOperatingSysstemType();
        switch (detectedOs) {
            case "MacOS":
                path = "";
                break;
            case "Windows":
                path = "C:/AppServ/www/comprobantes-log";
                break;
            case "Linux":
                path = "/var/www/html/comprobantes-log";
                break;
        }
        return path;
    }
    public static String getPath(String project) {
        String path = "";
        String detectedOs = OsUtils.getOperatingSysstemType();
        if(detectedOs.equalsIgnoreCase("MacOS")){
        	path = "";
        }else if(detectedOs.equalsIgnoreCase("Windows")){
        	path = "C:/AppServ/www/"+project+"/";
        }else if (detectedOs.equalsIgnoreCase("Linux")){
        	path = "/opt/usuario/"+project+"/";
        }
        return path;
    }
    public static String getDotEnvPath(String projectName) {
        String path = "";
        String detectedOs = OsUtils.getOperatingSysstemType();
        switch (detectedOs) {
            case "MacOS":
                path = "";
                break;
            case "Windows":
                char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                for (char letter : alphabet) {
                    path = letter + ":/dotenv/" + projectName;
                    File directory = new File(path);
                    if (directory.exists()) {
                        break;
                    }
                }
                break;
            case "Linux":
                path = "/opt/dotenv/"+projectName;
                break;
        }
        return path;
    }
    public static String getDotEnvPathUtil(String projectName) {
        String path = "";
        String detectedOs = OsUtils.getOperatingSysstemType();
        switch (detectedOs) {
            case "MacOS":
                path = "";
                break;
            case "Windows":
                char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                for (char letter : alphabet) {
                    path = letter + ":/dotenv/" + projectName;
                    File directory = new File(path);
                    if (directory.exists()) {
                        break;
                    }
                }
                break;
            case "Linux":
                path = "/opt/dotenv/"+projectName;
                break;
        }
        return path;
    }

    public void getFilesAPP(final File folder,JSONObject obj,JSONArray objs,JSONArray directories) throws IOException { //recursividad
        String path = "";// PARA COMPARAR
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if(fileEntry.getName().endsWith(".pdf")||fileEntry.getName().endsWith(".json")){
                    objs = new JSONArray();
                    if(path.equalsIgnoreCase(fileEntry.getParent())){
                        JSONObject o = directories.getJSONObject(directories.length()-1);
                        JSONArray arr = o.getJSONArray("folder");
                        arr.getJSONObject(arr.length()-1).getJSONArray("archivos")
                                .put(new JSONObject().put("ruta", fileEntry.getName()));
                    }else{
                        String absolut = fileEntry.getAbsolutePath().replace("\\", "/");
                        String []folders = absolut.split("/");
                        path = fileEntry.getParent();
                        for (int i = 2; i < folders.length; i++) {
                            String p = folders[i];
                            if( i ==(folders.length-1)){
                                JSONObject f = new JSONObject();
                                JSONArray files = new JSONArray();
                                JSONObject fix = new JSONObject();
                                fix.put("ruta", p);
                                files.put(fix);
                                f.put("archivos", files);
                                objs.put(f);
                            }else{
                                JSONObject fix = new JSONObject();
                                fix.put("ruta", p);
                                objs.put(fix);
                            }
                        }
                        obj = new JSONObject();
                        obj.put("folder", objs);
                        directories.put(obj);
                    }
                }
                if(fileEntry.getName().endsWith(".json")){
//                    System.out.println(fileEntry.getName());
                }
            } else {
//                System.out.println(fileEntry.getName());
                getFilesAPP(fileEntry,obj,objs,directories);
            }
            
        }
//        System.out.println(directories);
//        System.out.println("objs-> "+objs);
//        System.out.println("obj-> "+obj);
    }

            
    public static void main(String[] args) throws IOException {
        JSONObject obj = null;
        JSONArray objs = null;
        JSONArray directories = new JSONArray();
        OsUtils os = new OsUtils();
        final File folder = new File(OsUtils.getDotEnvPathUtil("APP"));
        os.getFilesAPP(folder,obj,objs,directories);
        System.out.println(directories);
    }
//    public static void main(String[] args) throws IOException {
//        Collection<Path> all = new ArrayList<Path>();
//        Path path = Paths.get("C:\\dotenv\\APP");
//        addTree(path, all);
//        System.out.println(all);
//    }
//    static void addTree(Path directory, final Collection<Path> all)
//            throws IOException {
//        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
//                    throws IOException {
//                all.add(file);
//                return FileVisitResult.CONTINUE;
//            }
//        });
//    }
    
    
    /**
     * 
     */
//    public static void main(String[] args) {
//      File folder = new File(OsUtils.getDotEnvPathUtil("roots"));
//      OsUtils listFiles = new OsUtils();
//      System.out.println("reading files before Java8 - Using listFiles() method");
//      listFiles.listAllFiles(folder);
//      System.out.println("-------------------------------------------------");
//      System.out.println("reading files Java8 - Using Files.walk() method");
////      listFiles.listAllFiles(OsUtils.getDotEnvPathUtil("roots"));
//
//     }
     // Uses listFiles method  
     public void listAllFiles(File folder){
         System.out.println("In listAllfiles(File) method");
         File[] fileNames = folder.listFiles();
         for(File file : fileNames){
             // if directory call the same method again
             if(file.isDirectory()){
                 listAllFiles(file);
             }else{
                 try {
                     readContent(file);
                 } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
        
             }
         }
     }
     // Uses Files.walk method   
     public void listAllFiles(String path){
         System.out.println("In listAllfiles(String path) method");
         try(Stream<Path> paths = Files.walk(Paths.get(path))) {
             paths.forEach(filePath -> {
                 if (Files.isRegularFile(filePath)) {
                     try {
                         readContent(filePath);
                     } catch (Exception e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                 }
             });
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } 
     }
     
     public void readContent(File file) throws IOException{
         System.out.println("read file " + file.getCanonicalPath() );
         try(BufferedReader br  = new BufferedReader(new FileReader(file))){
               String strLine;
               // Read lines from the file, returns null when end of stream 
               // is reached
               while((strLine = br.readLine()) != null){
                System.out.println("Line is - " + strLine);
               }
         }
     }
     
     public void readContent(Path filePath) throws IOException{
         System.out.println("read file " + filePath);
         List<String> fileList = Files.readAllLines(filePath);
         System.out.println("" + fileList);
     }
}
