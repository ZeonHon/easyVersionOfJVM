package classPath;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Author:ZeonHon
 * Date:08-16-2024
 * Description:CompositeEntry由众多的Entry组成，正好可以表示成 Entry list;
 * 构造函数把参数（路径列表）按分隔符分成小路径，然后把每个小路径都转换成具体的 Entry实例
 */
public class CompositeEntry extends Entry{
    ArrayList<Entry> compositeEntryList;
    private String pathList;
    public CompositeEntry() {};
    public CompositeEntry(String pathList , String pathSeparator) {
        this.pathList = pathList;
        String[] paths = pathList.split(pathSeparator);
        compositeEntryList = new ArrayList<Entry>(paths.length);
        for(int i = 0; i < paths.length; i++) {
            compositeEntryList.add(Entry.createEntry(paths[i]));
        }
    }

    @Override
    String printClassName() {
        return pathList;
    }

    @Override
    byte[] readClass(String className) {
        byte[] classData ;
        for(Entry entry : compositeEntryList) {
            try {
                classData = entry.readClass(className);
                if (classData != null) {
                    return classData;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
