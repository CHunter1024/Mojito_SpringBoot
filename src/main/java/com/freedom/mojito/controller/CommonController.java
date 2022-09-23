package com.freedom.mojito.controller;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

@Api(tags = "通用相关API")
@RestController
public class CommonController {

    @Autowired
    private FileUtils fileUtils;


    @ApiOperation(value = "文件上传", notes = "请求处理成功返回文件名")
    @PostMapping("/**/common/upload")
    private Result<String> upload(@RequestPart MultipartFile file) throws IOException {
        // 获取上传文件的文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        // 保存文件到临时目录中
        String tempDir = fileUtils.getTempDir();
        Path path = Paths.get(tempDir);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        file.transferTo(Paths.get(tempDir, fileName));

        return Result.succeed(fileName);
    }


    @ApiOperation("文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "subDir", value = "子目录（多层目录用,隔开）", paramType = "query", dataTypeClass = String.class)
    })
    @GetMapping("/common/download")
    private void download(String fileName, @RequestParam(required = false) String subDir, HttpServletResponse response) {
        String dir = fileUtils.getDir(subDir);

        try (// 输入流，通过输入流读取文件内容
             InputStream inputStream = Files.newInputStream(Paths.get(dir, fileName));
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