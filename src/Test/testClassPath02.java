package Test;

import BasicUtils.Cmd;
import classPath.classPath;
import java.util.Scanner;

/**
 * Author:ZeonHon
 * Date:08-19-2024
 * Description:
 * (1)java -cp C:\Users\zhangxin\Desktop Hello
 * (2)java java.lang.Object
 * for the first test case,you must confirm there is a Hello.class in your desktop
 * for the second test case,we just load the Object.class in your JAVA_HOME/jre/lib/rt.jar
 */
public class testClassPath02 {
    public static void main(String[] args) throws Exception {
        //先输出帮助信息
        System.out.println("Usage: java [-options] class [args...]");
        System.out.println("Specially,we don't support the path that contains space!");
        Scanner in = new Scanner(System.in);
        String cmdLine = in.nextLine();
        String[] cmds = cmdLine.split("\\s+");
        Cmd cmd = new Cmd(cmds);

        if (!cmd.isRightFmt()) {
            System.out.println("Unrecognized command!");
            cmd.printUsage();
        } else if (!cmd.isRightOpt()) {
            System.out.println("Unrecognized option: " + cmds[1]);
            cmd.printUsage();
        } else {
            if (cmd.isVersionFlag()) {
                System.out.println("java version \"1.8.0_20\"\n"
                        + "Java(TM) SE Runtime Environment (build 1.8.0_20-b26)\n"
                        + "Java HotSpot(TM) 64-Bit Server VM (build 25.20-b23, mixed mode)");
            } else if (cmd.isHelpFlag()) {
                cmd.printUsage();
            } else {
                System.out.println("cmd pared successful!");
                classPath ClassPath = new classPath(cmd.getXJreOption(), cmd.getCpOption());
                byte[] classData = ClassPath.readClass(cmd.getClazz());
                int len = classData.length;
                System.out.println("the length of file is: " + len);
                //每20个字节换一行，打印每个字节的十六进制表示
                for (int i = 0; i < len; i++) {
                    if (i % 20 == 0) {
                        System.out.println();
                    }
                    System.out.printf("%04X ", classData[i], 16);
                }
            }
        }
    }
}
