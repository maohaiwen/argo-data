package com.argo.data.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        //新建目标目录
        (new File(targetDir)).mkdirs();
        //获取源文件夹当下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                //源文件
                File sourceFile = file[i];
                //目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                //准备复制的源文件夹
                String dir1 = sourceDir + file[i].getName();
                //准备复制的目标文件夹
                String dir2 = targetDir + File.separator + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }

    }

    public static void copyFile(File sourcefile, File targetFile) throws IOException {

        //新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourcefile);
        BufferedInputStream inbuff = new BufferedInputStream(input);

        //新建文件输出流并对它进行缓冲
        FileOutputStream out = new FileOutputStream(targetFile);
        BufferedOutputStream outbuff = new BufferedOutputStream(out);

        //缓冲数组
        byte[] b = new byte[1024 * 5];
        int len = 0;
        while ((len = inbuff.read(b)) != -1) {
            outbuff.write(b, 0, len);
        }

        //刷新此缓冲的输出流
        outbuff.flush();

        //关闭流
        inbuff.close();
        outbuff.close();
        out.close();
        input.close();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private static boolean compressZip(ZipOutputStream zipOut, BufferedOutputStream out, String filePath, File... sourceFiles)
            throws IOException {
        if (null != filePath && !"".equals(filePath)) {
            filePath += filePath.endsWith(File.separator) ? "" : File.separator;
        } else {
            filePath = "";
        }
        boolean flag = true;
        for (File file : sourceFiles) {
            if (null == file) {
                continue;
            }
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (null == fileList) {
                    return false;
                } else if (1 > fileList.length) {
                    zipOut.putNextEntry(new ZipEntry(filePath + file.getName() + File.separator));
                } else {
                    flag = compressZip(zipOut, out, filePath + File.separator + file.getName(), fileList) && flag; // 只要flag有一次为false，整个递归的结果都为false。
                }
            } else {
                zipOut.putNextEntry(new ZipEntry(filePath + file.getName()));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                int bytesRead;
                while (-1 != (bytesRead = in.read())) {
                    out.write(bytesRead);
                }
                in.close();
            }
            out.flush();
        }
        return flag;
    }

    /**
     * 解压zip格式文件
     *
     * @param originFile zip文件。
     * @param targetDir  要解压到的目标路径。
     * @return 如果目标文件不是zip文件则返回false。
     * @throws IOException 如果发生I/O错误。
     */
    public static boolean decompressZip(File originFile, String targetDir) throws IOException {
        if (!targetDir.endsWith(File.separator)) {
            targetDir += File.separator;
        }
        ZipFile zipFile = new ZipFile(originFile);
        ZipEntry zipEntry;
        Enumeration<? extends ZipEntry> entry = zipFile.entries();
        while (entry.hasMoreElements()) {
            zipEntry = entry.nextElement();
            String fileName = zipEntry.getName();
            File outputFile = new File(targetDir + fileName);
            if (zipEntry.isDirectory()) {
                forceMkdirs(outputFile);
                continue;
            } else if (!outputFile.getParentFile().exists()) {
                forceMkdirs(outputFile.getParent());
            }
            OutputStream outputStream = new FileOutputStream(outputFile);
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            int len;
            byte[] buffer = new byte[8192];
            while (-1 != (len = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
        }
        zipFile.close();
        return true;
    }

    public static String convertCygwinPath(String path) {
        path = path.replaceAll("\\\\", "/");
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            path = "/cygdrive/" + path.replaceFirst(":", "");
        return path;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File forceMkdirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }
        return file;
    }

    public static File forceMkdirs(String pathName) {
        return forceMkdirs(new File(pathName));
    }

    public static File forceMkdirs(File parent, String child) {
        return forceMkdirs(new File(parent, child));
    }

    public static File forceMkdirs(String parent, String child) {
        return forceMkdirs(new File(parent, child));
    }

}
