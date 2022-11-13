package com.praver.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.praver.springboot.common.Result;
import com.praver.springboot.controller.dto.LoginDTO;
import com.praver.springboot.controller.request.AdminPageRequest;
import com.praver.springboot.controller.request.LoginRequest;
import com.praver.springboot.controller.request.UserPageRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.User;
import com.praver.springboot.service.IAdminService;
import com.praver.springboot.service.IUseService;
import com.praver.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    IAdminService adminService;



    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "/file/";

    @PostMapping("/file/upload")
    public Result uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (StrUtil.isBlank(originalFilename)) {
            return Result.error("头像上传失败");
        }
        long flag = System.currentTimeMillis();
        String filePath = BASE_FILE_PATH + flag + "_" + originalFilename;
        try {

            FileUtil.mkParentDirs(filePath);  // 创建父级目录
            file.transferTo(FileUtil.file(filePath));
            Admin currentAdmin = TokenUtils.getCurrentAdmin();
            String token = TokenUtils.genToken(currentAdmin.getId().toString(), currentAdmin.getPassword(), 30);
            String url = "http://localhost:8090/api/admin/file/download/" + flag + "?&token=" + token;
            if (originalFilename.endsWith("png") || originalFilename.endsWith("jpg") || originalFilename.endsWith("pdf")) {
                url += "&play=1";
            }
            return Result.success(url);
        } catch (IOException e) {
            log.info("上传失败！");
        }
        return Result.error("图片上传失败");
    }

    @GetMapping("/file/download/{flag}")
    public void download(@PathVariable String flag, @RequestParam(required = false) String play, HttpServletResponse response) {
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(BASE_FILE_PATH);
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse(""); //  System.currentTimeMillis() + originalFilename
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                String realName = fileName.substring(fileName.indexOf("_") + 1);
                if ("1".equals(play)) {
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(realName, "UTF-8"));
                } else {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                }
                byte[] bytes = FileUtil.readBytes(BASE_FILE_PATH + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }

    @RequestMapping("/login")
    public Result login(@RequestBody LoginRequest request){
        LoginDTO login = adminService.login(request);
        if (login == null){
            return Result.error("用户名或密码错误！");
        }
        return Result.success(login);
    }



    @GetMapping("/list")
    public Result list() {
        return Result.success(adminService.list());
    }

    @GetMapping("/page")
    public Result page(AdminPageRequest adminPageRequest) {
        return Result.success(adminService.page(adminPageRequest));
    }

    @PostMapping("/save")
    public Result save(@RequestBody Admin admin) {
        return Result.success(adminService.save(admin));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(adminService.getById(id));
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        return Result.success(adminService.update(admin));
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteByID(@PathVariable Integer id) {
        return Result.success(adminService.deleteByID(id));
    }

    @DeleteMapping("/deleteList/{ids}")
    public Result deleteList(@PathVariable String ids) {
        String[] split = ids.split(",");
        int[] idList = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
        return Result.success(adminService.deleteList(idList));
    }
}
