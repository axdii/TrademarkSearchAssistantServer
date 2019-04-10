package top.atzlt.web;

import top.atzlt.cons.CommonConstant;

import java.io.File;
import java.io.IOException;

public class DirManager {

    private static File classesDir;
    private static File webAppRootDir;

    static {
        createClassesDir();
        createWebAppRootDir();
    }


    private static void createClassesDir(){
        String classesPath = DirManager.class.getResource("/").getPath();
        classesDir = new File(classesPath);
    }

    private static void createWebAppRootDir(){
        String webAppRootPath = classesDir.getParent();
        webAppRootDir = new File(webAppRootPath);
    }


    public static File getWebAppRootDir(){
        return webAppRootDir;
    }

    public static String getWebAppRootPath() {
//        return getWebAppRootDir().getPath();
        return "/usr/local/app/trademark";
    }

    public static File getClassesDir() {
        return classesDir;
    }

    public static String getClassDirPath(){
        return getClassesDir().getPath();
    }

    public static void setClassesDir(File classesDir) {
        DirManager.classesDir = classesDir;
    }

    public static String getDirPath(String dirCode) {
        String webAppRootPath = getWebAppRootPath();
        String classesRootPath = getClassDirPath();
        String path = "";
        switch (dirCode) {
            case CommonConstant.TRADEMARK_IMG_DIR:
                path = webAppRootPath + "/img/trademarkImg";
                break;
            case CommonConstant.TRADEMARK_TB_IMG_DIR:
                path = webAppRootPath + "/img/trademarkTbImg";
                break;
            case CommonConstant.WORD_SEG_DIR:
                path = classesRootPath + "/top/atzlt/wordSeg";
                break;
            case CommonConstant.WORD_SEG_LOCAL_DIR:
                path = classesRootPath + "\\top\\atzlt\\wordSeg";
                break;
            case CommonConstant.NEWS_DIR:
                path = classesRootPath + "/top/atzlt/webNewsSpider";
                break;
            case CommonConstant.NEWS_FILE_DIR:
                path = getDirPath(CommonConstant.NEWS_DIR) + "/news";
                break;
            default:
                path = webAppRootPath;

        }
        return path;

    }


}
