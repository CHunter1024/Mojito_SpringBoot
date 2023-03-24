package com.freedom.mojito.controller;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.util.EmailUtils;
import com.freedom.mojito.util.FileUtils;
import com.freedom.mojito.util.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
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
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <p>CreateTime: 2022-07-25 下午 2:59</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "通用相关API")
@Slf4j
@RestController
@RequestMapping("/**/common")
public class CommonController {

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation(value = "文件上传", notes = "请求处理成功返回文件名")
    @PostMapping("/upload")
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
    @GetMapping("/download")
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

    @ApiOperation("根据邮箱地址生成验证码并发送出去")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱地址", required = true, paramType = "path", dataTypeClass = String.class),
            @ApiImplicitParam(name = "purpose", value = "用途（login/update）", required = true, paramType = "path", dataTypeClass = String.class)
    })
    @GetMapping("/getCode/{email}/{purpose}")
    public Result<Object> getCodeByEmail(@PathVariable("email") String email,
                                         @PathVariable("purpose") String purpose) {
        if (StringUtils.hasText(email) && ("login".equals(purpose) || "update".equals(purpose))) {
            String codeKey = email + "_" + purpose + "_code";
            String againKey = email + "_" + purpose + "_again";

            // 检查该邮箱地址在60秒内是否已获取过验证码
            if (redisTemplate.opsForValue().get(againKey) != null) {
                return Result.fail("操作过于频繁，请60秒后重试");
            }

            // 生成随机6位数字验证码
            String code = RandomUtils.getNumStr(6);
            log.info("验证码：{}", code);

            // 通过 Springboot mail 将验证码发送出去
            emailUtils.sendCode(email, code);

            // 存放到redis中，并设置验证码过期时间和可再次获取时间
            redisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(againKey, "", 60, TimeUnit.SECONDS);
            return Result.succeed(null);
        }
        return Result.fail("参数有误");
    }
}