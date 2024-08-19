package classPath;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Author:ZeonHon
 * Date:08-16-2024
 * Description:ZipJarEntry表示ZIP或JAR文件形式的类路径,避免和Java中的ZipEntry冲突,起名为ZipJarEntry;
 */
public class ZipJarEntry extends Entry{
    String absPath;    // E:\JavaSrc\JVMByHand\tmp\test.zip  全路径
    String zipName;     // test     压缩包名,不带 .zip 或者 jar

    public ZipJarEntry(String path) {
        File dir = new File(path);
        if(dir.exists()) {
            absPath = dir.getParentFile().getAbsolutePath();
            //去掉结尾的.zip或者.jar
            this.zipName = dir.getName().substring(0, dir.getName().lastIndexOf("."));
        }
    }

    public ZipJarEntry(String path , String zipName) {
        File dir = new File(path , zipName);
        if(dir.exists()) {
            absPath = dir.getAbsolutePath();
            //去掉结尾的.zip或者.jar
            this.zipName = zipName.substring(0, zipName.lastIndexOf("."));
        }
    }

    @Override
    String printClassName() {
        return absPath;
    }

    /**
     * 从zip或者jar文件中提取class文件;
     * @param className class文件的相对路径，路径之间用斜线 / 分隔，文件名有.class后缀
     * @return 字节数组
     * @throws Exception
     */
    @Override
    byte[] readClass(String className) throws Exception {
        File file = new File(absPath);
        try (ZipFile zipFile = new ZipFile(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipFile.getEntry(className)));
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024)) {

            if (zipFile.getEntry(className) == null) {
                return null;
            }

            int size = 0;
            byte[] temp = new byte[1024];
            while ((size = bufferedInputStream.read(temp)) != -1) {
                byteArrayOutputStream.write(temp, 0, size);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // 处理文件读取异常
            throw new Exception("Error reading class: " + className, e);
        }
    }
}
