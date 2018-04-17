package me.mikusjelly.amon.utils;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Util {

    public static String getSystemTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd----hh:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String dateTime = sDateFormat.format(date);
        return dateTime;
    }

    public static ArrayList<String> copyArrayList(ArrayList<String> srcArrayList) {
        ArrayList<String> dstArrayList = new ArrayList<String>();
        for (String ele : srcArrayList)
            dstArrayList.add(ele);
        return dstArrayList;
    }

    public static String toHex(byte[] buf) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String h = Integer.toHexString(0xFF & buf[i]);
            while (h.length() < 2)
                h = "0" + h;

            hexString.append(h);
        }

        return hexString.toString();
    }

    public static int getFd(FileDescriptor fileDescriptor) {
        int fdInt = -1;
        try {
            if (fileDescriptor != null) {
                Field descriptor = fileDescriptor.getClass().getDeclaredField("descriptor");
                descriptor.setAccessible(true);
                fdInt = descriptor.getInt(fileDescriptor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fdInt;
    }

    public static int getTimeId() {
        int x = (int) System.nanoTime();
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        if (x < 0)
            x = 0 - x;
        return x;
    }

    public static String parseParameterTypes(Method method) {
        String parameterTypes = "";
        for (Class<?> parameterClass : method.getParameterTypes())
            parameterTypes += parseClassType(parameterClass);
        return parameterTypes;
    }

    public static String parseParameterTypes(Constructor<?> constructor) {
        String parameterTypes = "";
        for (Class<?> parameterClass : constructor.getParameterTypes())
            parameterTypes += parseClassType(parameterClass);
        return parameterTypes;
    }


    public static String parseReturnType(Method method) {
        String returnType = "";
        Class<?> returnClass = method.getReturnType();
        returnType = parseClassType(returnClass);
        return returnType;
    }

    public static String parseClassType(Class<?> classInst) {
        String classType = "";
        String className = classInst.getName();
        // Primitive type
        if (className.equals("void"))
            classType = "V";
        else if (className.equals("byte"))
            classType = "B";
        else if (className.equals("short"))
            classType = "S";
        else if (className.equals("int"))
            classType = "I";
        else if (className.equals("long"))
            classType = "L";
        else if (className.equals("float"))
            classType = "F";
        else if (className.equals("double"))
            classType = "D";
        else if (className.equals("char"))
            classType = "C";
        else if (className.equals("boolean"))
            classType = "Z";
            // Class type
        else if (className.contains(".")) {
            classType = className.replace(".", "/");
            if (!className.contains(";"))
                classType = classType + ";";
            if (!className.contains("L"))
                classType = "L" + classType;
        } else
            classType = className;

        return classType;
    }

    public static String generateRandomNums(int numCount) {
        StringBuilder randomNums = new StringBuilder();
        Random randomGenerator = new Random();
        for (int i = 1; i < numCount; i++) {
            int randomInt = randomGenerator.nextInt(10);
            randomNums.append(randomInt);
        }
        return randomNums.toString();
    }

    public static String generateRandomStrs(int strCount) {
        StringBuilder randomStrs = new StringBuilder();
        Random randomGenerator = new Random();
        for (int i = 1; i < strCount; i++) {
            int randomInt = randomGenerator.nextInt(26);
            randomStrs.append((char) (randomInt + 'a'));
        }

        return randomStrs.toString();
    }
}
