
package top.atzlt.wordSeg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 加载资源
 */
public class Detector {

    private static final Logger logger = LoggerFactory.getLogger(Detector.class);

    /**
     * 加载资源
     * @param resourceLoader 资源加载逻辑
     * @param resourcePaths 多个资源路径，用逗号分隔
     */
    public static void loadAndWatch(ResourceLoader resourceLoader, String resourcePaths) {
        resourcePaths = resourcePaths.trim();
        if("".equals(resourcePaths)){
            logger.info("没有资源可以加载");
            return;
        }
        logger.info("开始加载资源");
        long start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();
        for(String resource : resourcePaths.split("[,，]")){
            try{
                resource = resource.trim();
                if(resource.startsWith("classpath:")){
                    //处理类路径资源
                    result.addAll(loadClasspathResource(resource.replace("classpath:", ""), resourceLoader, resourcePaths));
                }else{
                    //处理非类路径资源
                    result.addAll(loadNoneClasspathResource(resource, resourceLoader, resourcePaths));
                }
            }catch(Exception e){
                logger.info("加载资源失败："+resource + e);
            }
        }
        logger.info("加载资源 "+result.size()+" 行");
        //调用自定义加载逻辑
        resourceLoader.clear();
        resourceLoader.load(result);
        long cost = System.currentTimeMillis() - start;
        logger.info("完成加载资源，耗时"+cost+" 毫秒");
    }
    /**
     * 加载类路径资源
     * @param resource 资源名称
     * @param resourceLoader 资源自定义加载逻辑
     * @param resourcePaths 资源的所有路径，用于资源监控
     * @return 资源内容
     * @throws IOException
     */
    private static List<String> loadClasspathResource(String resource, ResourceLoader resourceLoader, String resourcePaths) throws IOException{
        List<String> result = new ArrayList<>();
//        System.out.println("类路径资源："+resource);
        resource = "top/atzlt/wordSeg/" + resource;
        Enumeration<URL> ps = Detector.class.getClassLoader().getResources(resource);
        while(ps.hasMoreElements()) {
            URL url=ps.nextElement();
//            System.out.println("类路径资源URL："+url);
            if(url.getFile().contains(".jar!")){
                //加载jar资源
                result.addAll(load("classpath:"+resource));
                continue;
            }
            File file=new File(url.getFile());
            boolean dir = file.isDirectory();
            if(dir){
                //处理目录
                result.addAll(loadAndWatchDir(file.toPath(), resourceLoader, resourcePaths));
            }else{
                //处理文件
                result.addAll(load(file.getAbsolutePath()));
            }
        }
        return result;
    }

    /**
     * 加载非类路径资源
     * @param resource 资源路径
     * @param resourceLoader 资源自定义加载逻辑
     * @param resourcePaths 资源的所有路径，用于资源监控
     * @return 资源内容
     * @throws IOException
     */
    private static List<String> loadNoneClasspathResource(String resource, ResourceLoader resourceLoader, String resourcePaths) throws IOException {
        List<String> result = new ArrayList<>();
        Path path = Paths.get(resource);
        boolean exist = Files.exists(path);
        if(!exist){
            logger.info("资源不存在："+resource);
            return result;
        }
        boolean isDir = Files.isDirectory(path);
        if(isDir){
            //处理目录
            result.addAll(loadAndWatchDir(path, resourceLoader, resourcePaths));
        }else{
            //处理文件
            result.addAll(load(resource));
        }
        return result;
    }
    /**
     * 递归加载目录下面的所有资源
     * @param path 目录路径
     * @param resourceLoader 资源自定义加载逻辑
     * @param resourcePaths 资源的所有路径，用于资源监控
     * @return 目录所有资源内容
     */
    private static List<String> loadAndWatchDir(Path path, ResourceLoader resourceLoader, String resourcePaths) {
        final List<String> result = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    result.addAll(load(file.toAbsolutePath().toString()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            logger.info("加载资源失败："+ path + ex);
        }
        return result;
    }
    /**
     * 加载文件资源
     * @param path 文件路径
     * @return 文件内容
     */
    private static List<String> load(String path) {
        List<String> result = new ArrayList<>();
        try{
            InputStream in = null;
            logger.info("加载资源："+path);
            if(path.startsWith("classpath:")){
                in = Detector.class.getClassLoader().getResourceAsStream(path.replace("classpath:", ""));
            }else{
                in = new FileInputStream(path);
            }
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){
                String line;
                while((line = reader.readLine()) != null){
                    line = line.trim();
                    if("".equals(line) || line.startsWith("#")){
                        continue;
                    }
                    result.add(line);
                }
            }
        }catch(Exception e){
            logger.info("加载资源失败："+path + e);
        }
        return result;
    }

}