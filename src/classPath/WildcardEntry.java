package classPath;


import java.io.File;
import java.util.ArrayList;

/**
 * Author:ZeonHon
 * Date:08-16-2024
 * Description:处理的是路径匹配的 xxx.* 的情况,首先把路径末尾的星号去掉，得到baseDir，然后遍历该baseDir路径下的文件,只取以.jar 结尾的文件
 */
public class WildcardEntry extends Entry{
    public CompositeEntry compositeEntry;
    public WildcardEntry(String jreLibPath) {
        //去掉最后一个字符*
        String baseDir = jreLibPath.substring(0, jreLibPath.length() - 1);
        File dir = new File(baseDir);
        File[] files = dir.listFiles();
        compositeEntry.compositeEntryList = new ArrayList<>();
        for(File file : files) {
            if(file.isFile() && file.getName().endsWith(".jar")) {
                compositeEntry.compositeEntryList.add(new ZipJarEntry(file.getName()));
            }
        }
    }

    @Override
    byte[] readClass(String className) {
        return compositeEntry.readClass(className);
    }

    @Override
    String printClassName() {
        return null;
    }
}
