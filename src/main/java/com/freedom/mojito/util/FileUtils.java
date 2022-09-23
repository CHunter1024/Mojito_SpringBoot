package com.freedom.mojito.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Description: 文件处理工具类
 * <p>CreateTime: 2022-08-13 下午 7:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Component
public class FileUtils {

    @Value("${file.temp-path}")
    private String tempPath;  // 临时文件目录
    @Value("${file.path}")
    private String path;  // 真正存放目录

    /**
     * 将文件从临时目录移动到上传目录
     *
     * @param fileName 文件名
     * @param subDir   子目录（多层目录用,隔开）
     * @throws IOException
     */
    public void saveNewFile(String fileName, String subDir) throws IOException {
        if (StringUtils.hasLength(fileName)) {
            String distDir = getDir(subDir);
            Path distDirPath = Paths.get(distDir);
            if (Files.notExists(distDirPath)) {
                Files.createDirectory(distDirPath);
            }
            Path srcPath = Paths.get(tempPath, fileName);
            Path distPath = Paths.get(distDir, fileName);
            Files.move(srcPath, distPath);
        }
    }

    /**
     * 新旧文件处理
     *
     * @param oldFileName 旧文件名
     * @param newFileName 新文件名
     * @param subDir      子目录
     */
    public void handleOldNewFile(String oldFileName, String newFileName, String subDir) throws IOException {
        // 仅当新旧文件名称不一致时处理
        if (!Objects.equals(oldFileName, newFileName)) {
            saveNewFile(newFileName, subDir);
            removeOldFile(oldFileName, subDir);
        }
    }

    /**
     * 将文件从上传目录中删除
     *
     * @param fileName 文件名
     * @param subDir   子目录
     * @throws IOException
     */
    public void removeOldFile(String fileName, String subDir) throws IOException {
        if (StringUtils.hasLength(fileName)) {
            Path oldPath = Paths.get(getDir(subDir), fileName);
            Files.deleteIfExists(oldPath);
        }
    }

    /**
     * 获取文件临时存放目录路径
     *
     * @return
     */
    public String getTempDir() {
        return tempPath;
    }

    /**
     * 获取文件存放目录
     *
     * @param subDir 子目录（多层目录用,隔开）
     * @return
     */
    public String getDir(String subDir) {
        if (StringUtils.hasText(subDir)) {
            StringBuilder dir = new StringBuilder(path);
            String[] subDirs = subDir.split(",");
            for (String sd : subDirs) {
                dir.append(sd).append(File.separator);
            }
            return dir.toString();
        } else {
            return path;
        }
    }
}
