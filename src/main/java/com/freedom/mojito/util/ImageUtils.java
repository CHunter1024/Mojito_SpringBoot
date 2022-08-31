package com.freedom.mojito.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Description: 图片文件处理工具类
 * <p>CreateTime: 2022-08-13 下午 7:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Component
public class ImageUtils {

    @Value("${file.temp-path}")
    private String tempPath;  // 临时文件目录
    @Value("${file.path}")
    private String path;  // 真正存放目录

    /**
     * 将图片文件从临时目录移动到上传目录
     *
     * @param imageName 图片名称
     * @param subDir    子目录
     * @throws IOException
     */
    public void saveNewImage(String imageName, String subDir) throws IOException {
        if (StringUtils.hasLength(imageName)) {
            String distDir = subDir == null ? path : path + subDir;
            Path distDirPath = Paths.get(distDir);
            if (Files.notExists(distDirPath)) {
                Files.createDirectory(distDirPath);
            }
            Path srcPath = Paths.get(tempPath, imageName);
            Path distPath = Paths.get(distDir, imageName);
            Files.move(srcPath, distPath);
        }
    }

    /**
     * 新旧图片文件处理
     *
     * @param oldImageName 旧图片名称
     * @param newImageName 新图片名称
     * @param subDir       子目录
     */
    public void handleOldNewImage(String oldImageName, String newImageName, String subDir) throws IOException {
        // 仅当新旧图片文件名称不一致时处理
        if (!Objects.equals(oldImageName, newImageName)) {
            saveNewImage(newImageName, subDir);
            removeOldImage(oldImageName, subDir);
        }
    }

    /**
     * 将图片文件从上传目录中删除
     *
     * @param imageName 图片名称
     * @param subDir    子目录
     * @throws IOException
     */
    public void removeOldImage(String imageName, String subDir) throws IOException {
        if (StringUtils.hasLength(imageName)) {
            Path oldPath = subDir == null ? Paths.get(path, imageName) : Paths.get(path, subDir, imageName);
            Files.deleteIfExists(oldPath);
        }
    }
}
