package me.mikusjelly.amon.utils;

import android.os.Environment;
import android.os.Process;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.robv.android.xposed.XposedBridge;

public class Util {

    public static String getSystemTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd----hh:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String dateTime = sDateFormat.format(date);
        return dateTime;
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public static void backup(String src) {
        final String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "amon" + File.separator + Process.myUid();
        File srcFile = new File(src);
        String srcName = srcFile.getName();
        String srcMd5 = calculateMD5(srcFile);
        final File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String dstName = srcName + "_" + srcMd5;
        String dst = new File(filePath, dstName).getAbsolutePath();

        cat(src, dst);

//        try {
//            copy(srcFile, dstFile);
//        } catch (IOException e) {
//            XposedBridge.log(e);
//        }
    }

    public static boolean cat(String src, String dst) {
        boolean isSuccess = false;
        String unixCommand = "cat " + src + " > " + dst;
        CommandResult result = Shell.SU.run(unixCommand);
        if (result.isSuccessful()) {
            isSuccess = true;
        }

        return isSuccess;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
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
