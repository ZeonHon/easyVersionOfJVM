package Test;

import BasicUtils.Cmd;

import java.util.Scanner;

/**
 * Author: ZeonHon
 * Date: 08-14-2024
 * @Description: 用于测试Cmd类的功能
 */
public class testCmd01 {
    public static void main(String[] args) {
        //用户进来首先弹出相关帮助信息
        System.out.println("Usage: java [-options] class [args...]");
        System.out.println("Specially,we don't support the path that contains space!");

        Scanner sc = new Scanner(System.in);
        String cmdLine = sc.nextLine();
        String[] cmds = cmdLine.split("\\s+");
        Cmd cmd = new Cmd(cmds);
        if(!cmd.isRightFmt()) {
            System.out.println("Incorrect command!");
            cmd.printUsage();
        } else if(!cmd.isRightOpt()) {
            System.out.println("Incorrect option!" + cmds[1]);
            cmd.printUsage();
        } else {
            if(cmd.isVersionFlag()) {
                System.out.println("java version \"1.8.0_20\"\n"
                        + "Java(TM) SE Runtime Environment (build 1.8.0_20-b26)\n"
                        + "Java HotSpot(TM) 64-Bit Server VM (build 25.20-b23, mixed mode)");
            } else if(cmd.isHelpFlag()) {
                cmd.printUsage();
            } else {
                System.out.println("cmd parsed successful!");
                // 检查 cmd.getArgs() 是否为 null 并进行安全处理
                if (cmd.getArgs() != null) {
                    for (String arg : cmd.getArgs()) {  // 使用增强型 for 循环
                        System.out.println(arg);
                    }
                } else {
                    System.out.println("No arguments provided.");
                }
            }
        }
    }
}
