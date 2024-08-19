package classPath;

import java.io.File;

/**
 * Author:ZeonHon
 * Date:08-16-2024
 * Description:对外使用的统一接口
 */
public class classPath {
    // jre路径
    private String jreDir;
    //分别存放三种类路径
    private Entry bootClasspath;
    private Entry extClasspath;
    private Entry userClasspath;

    //parse()函数使用 -Xjre 选项解析启动类路径和扩展类路径
    //使用-classpath/-cp选项解析用户类路径
    public classPath(String jreOption , String cpOption) {
        this.jreDir = getJreDir(jreOption);
        this.bootClasspath = parseBootClasspath();
        this.extClasspath = parseExtClasspath();
        this.userClasspath = parseUserClasspath(cpOption);
    }
    private Entry parseBootClasspath() {
        //可能出现的情况是: jre/lib/*
        //File.separator可以保证在不同系统的计算机上都能被正确解析
        String jreLibPath = jreDir + File.separator + "lib" + File.separator + "*";
        return new WildcardEntry(jreLibPath);
    }

    private Entry parseExtClasspath() {
        //可能出现的情况是：jre/lib/ext/*
        String jreExtPath = jreDir + File.separator + "lib" + File.separator + "ext" + File.separator + "*";
        return new WildcardEntry(jreExtPath);
    }

    //确定传进来的jre的路径是否有效；
    private String getJreDir(String jreOption) {
        File jreFile;
        if(jreOption != null && !jreOption.isEmpty()) {
            jreFile = new File(jreOption);
            if (jreFile.exists()) {
                return jreOption;
            }
        }
        //jreOption选项为空，那么在当前路径找
        jreFile = new File("jre");
        if (jreFile.exists()) {
            return jreFile.getAbsolutePath();
        }

        //在JAVA_HOME中找
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            return javaHome + File.separator + "jre";
        }

        throw new RuntimeException("Can not find jre folder!");
    }

    private Entry parseUserClasspath(String cpOption) {
        return Entry.createEntry(cpOption);
    }

    /**
     * ClassPath 对外的统一接口,实例化ClassPath时传入 userPath 路径和类名就可以读取字节码文件
     * 读取className 对应的字节码,注意顺序,我们的查找次序是:
     * bootClasspath => extClasspath => userClasspath;
     * @param className
     * @return 字节数组
     */
     public byte[] readClass(String className) throws Exception {
         //注意，用命令行加载java文件时，只写文件名，这里统一为文件名后补上“.class”的后缀；
         if (className.endsWith(".class")) {
             throw new RuntimeException("can't find or can't load the class: " + className);
         }
         className = className.replace(".", "/");
         className = className + ".class";
         try {
             byte[] data;
             if ((data = bootClasspath.readClass(className)) != null) {
                 return data;
             } else if ((data = extClasspath.readClass(className)) != null) {
                 return data;
             } else if ((data = userClasspath.readClass(className)) != null) {
                 return data;
             } else {
                 String message = "can't find or can't load the class: " + className +
                         ". Searched in: bootClasspath, extClasspath, userClasspath.";
                 throw new ClassNotFoundException(message);
             }
         } catch (Exception e) {
             // 如果 readClass 方法可能抛出其他异常，这里可以捕获并转换为 ClassNotFoundException
             throw new ClassNotFoundException("Error loading class: " + className, e);
         }

     }

    @Override
    public String toString() {
        return userClasspath.printClassName();
    }
}
