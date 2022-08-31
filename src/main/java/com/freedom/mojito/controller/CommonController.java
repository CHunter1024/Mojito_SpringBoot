package com.freedom.mojito.controller;

import com.freedom.mojito.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Description:
 * <p>CreateTime: 2022-07-25 下午 2:59</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@RestController
public class CommonController {

    @Value("${file.temp-path}")
    private String tempPath;  // 临时存放目录
    @Value("${file.path}")
    private String path;  // 真正存放目录

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(path = {"/backend/common/upload", "front/common/upload"})
    private Result<String> upload(MultipartFile file) throws IOException {
        // 获取上传文件的文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        // 保存文件到临时目录中
        Path path = Paths.get(tempPath);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        file.transferTo(Paths.get(tempPath, fileName));

        return Result.succeed(fileName);
    }

    /**
     * 文件下载
     *
     * @param file
     * @param response
     */
    @GetMapping("/common/download")
    private void download(String file, HttpServletResponse response) {
        try (// 输入流，通过输入流读取文件内容
             InputStream inputStream = Files.newInputStream(Paths.get(path, file));
             // 输出流，通过输出流将文件写回浏览器
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("image/*");

            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}