package BasicUtils;

/**
 * Author:ZeonHon
 * Time:08-14-2024
 * Description:此类为命令行工具类，用于实现cmd命令行输入java等相关命令时进行解析
 */
public class Cmd {
    private boolean isRightFormat = true; //是否为正确的输入格式
    private boolean isRightOption = true; //是否为正确的选项
    private boolean helpFlag;             //是否为help，查看帮助信息
    private boolean versionFlag;          //是否为version，查看版本信息
    private String cpOption;              //classPath的路径，即java -cp/-classpath 其后跟的路径
    /* 此变量XjreOption为空，则表示使用java的默认路径，否则使用XjreOption的路径
       这是一个非标准选项，java命令中是不存在的
       使用这个选项目的是用来指定启动类路径来寻找和加载Java标准库中的类
       即JAVA_HOME/jre的路径；
       这里要注意的是，如果真的要指定XjreOption，那么其路径值必须要用双引号包含起来
     */
    private String xJreOption;
    private String clazz;                 //要执行的类名,包含了main方法的class文件
    private String[] args;                //执行clazz文件所需要的参数，也就是命令行中类名后跟着的参数(可能有多个)

    public Cmd(String[] args) { parseCmd(args); }
    public Cmd(String cmdLine) { parseCmd(cmdLine); }

    public void parseCmd(String cmdLine) {
        //解析命令行参数，以单或者多个空格分开，路径名中间不能有空格不然会解析失败
        String[] args = cmdLine.trim().split("\\s+");
        parseCmd(args);
    }
    public void parseCmd(String[] args) {
        int classNameIndex = 1; //用于记录类名的位置
        //参数列表必须>=2 ， 因为最简单的命令比如java -version也有两个参数
        if(args.length < 2) {
            isRightFormat = false;
            return;
        }
        //判断命令是不是以java开头的，如果不是就可以返回了，全错！
        if(!args[0].equals("java")) {
            isRightFormat = false;
        } else {
            if(args[1].equals("-version")) {
                versionFlag = true;
            } else if(args[1].equals("-help") || args[1].equals("-?")) {
                helpFlag = true;
            } else if(args[1].equals("-classpath") || args[1].equals("-cp")) {
                //如果走到了这一步，说明命令格式正确，那么命令必定是这种形式；java -cp/-classpath 路径名 类名 参数1 参数2 ...至少有四个参数
                if(args.length < 4) {
                    isRightFormat = false;
                }
                classNameIndex = 3;
                this.cpOption = args[2]; //记录路径所在
            } else if(args[1].equals("-Xjre")) {
                //如果走到了这一步，说明命令格式正确，那么命令必定是这种形式；java -Xjre "C:\Program Files\Java\jdk1.8.0_20\jre" java.lang.Object至少有四项
                if(args.length < 4) {
                    isRightFormat = false;
                }
                classNameIndex = 3;
                this.xJreOption = args[2]; //记录jre所在路径
            } else if(args[1].equals("-")) {
                isRightOption = false;
            }
            this.clazz = args[classNameIndex]; //获取类名
            this.args = new String[args.length - classNameIndex - 1]; //获取参数列表
            System.arraycopy(args, classNameIndex + 1, this.args, 0, args.length - classNameIndex - 1);
        }
    }
    public void printUsage() {
        System.out.println("Usage: java [-options] class [args...]");
        System.out.println("Specially,we don't support the path that contains space!");
    }
    //封装一些基本方法
    public boolean isRightFmt() {
        return isRightFormat;
    }

    public boolean isRightOpt() {
        return isRightOption;
    }

    public boolean isHelpFlag() {
        return helpFlag;
    }

    public boolean isVersionFlag() {
        return versionFlag;
    }

    public String getCpOption() {
        return cpOption;
    }

    public String getXJreOption() {
        return xJreOption;
    }

    public String getClazz() {
        return clazz;
    }

    public String[] getArgs() {
        return args;
    }
}
