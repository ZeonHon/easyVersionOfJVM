package classPath;

import java.io.*;

/**
 *  Author:ZeonHon
 *  Date:08-16-2024
 *  Description:表示目录形式的类路径
 */
public class DirEntry extends Entry{
    private String absDir;
    public DirEntry(String path) {
        File dir = new File(path);
        //若文件存在，则获取其绝对路径并存储
        if(dir.exists()) {
            absDir = dir.getAbsolutePath();
        }
    }

    @Override
    String printClassName() {
        return absDir;
    }
    @Override
    public byte[] readClass(String className) throws IOException {
        File file = new File(absDir, className);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream out = new ByteArrayOutputStream(1024)) {

            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = in.read(temp)) != -1) {
                out.write(temp, 0, len);
            }
            // 直接返回 ByteArrayOutputStream 的 toByteArray 方法的结果
            return out.toByteArray();
        }
    }

}
