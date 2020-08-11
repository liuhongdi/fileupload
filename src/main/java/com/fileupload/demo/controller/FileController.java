package com.fileupload.demo.controller;

import com.fileupload.demo.result.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    //文件的保存路径
    private final static String FILE_BASE_PATH = "/data/file/html";

    //多文件域上传页面
    @GetMapping("/uploadadd")
    public String uploadAdd(ModelMap modelMap) {
        return "image/uploadadd";
    }

    //处理多个文件域的表单上传
    @PostMapping("/uploadadded")
    @ResponseBody
    public RestResult uploadadded(@RequestParam Map<String,String> params,HttpServletRequest request) {
        RestResult res = new RestResult();
        int num =  Integer.parseInt(params.get("num"));
        //根据表单中文件元素的数量遍历
        for (int i=1;i<=num;i++) {
            //text
            String curText = params.get("text"+i);
            System.out.println("text:"+curText);
            //file
            MultipartFile curFile = ((MultipartHttpServletRequest)request).getFile("file"+i);
            if (curFile.isEmpty()) {
                continue;
            }
            //System.out.println("text:"+curText);
            String fileName = curFile.getOriginalFilename();
            System.out.println("文件名： " + fileName);
            // 文件后缀
            int lastDot = fileName.lastIndexOf(".");
            lastDot++;
            String fileType = fileName.substring(lastDot);
            // 重新生成唯一文件名，用于存储数据库
            String fileSn = UUID.randomUUID().toString();
            Map<String, String> map2 = new HashMap<String, String>();
            String destFilePath = FILE_BASE_PATH+"/"+fileSn+"."+fileType;
            File dest = new File(destFilePath);
            try {
                curFile.transferTo(dest);
            } catch (IOException e) {
                System.out.println("save ioexception");
                e.printStackTrace();
            }
        }

        return res.success(0,"上传成功");
    }


    //单文件/多文件上传页面
    @GetMapping("/upload")
    public String upload(ModelMap modelMap) {
        return "image/upload";
    }

    //处理单文件/多文件上传
    @PostMapping("/uploaded")
    @ResponseBody
    public RestResult uploaded(HttpServletRequest request) {
        RestResult res = new RestResult();
        List<MultipartFile> list = ((MultipartHttpServletRequest)request).getFiles("files");

        for (MultipartFile multipartFile : list) {
            if (list.isEmpty()) {
                continue;
            }

            // 文件名
            String fileName = multipartFile.getOriginalFilename();
            System.out.println("文件名： " + fileName);

            // 文件后缀
            int lastDot = fileName.lastIndexOf(".");
            lastDot++;
            String fileType = fileName.substring(lastDot);
                // 重新生成唯一文件名，用于存储数据库
                String fileSn = UUID.randomUUID().toString();
                Map<String, String> map2 = new HashMap<String, String>();
                String destFilePath = FILE_BASE_PATH+"/"+fileSn+"."+fileType;
                File dest = new File(destFilePath);
                try {
                    multipartFile.transferTo(dest);
                } catch (IOException e) {
                    System.out.println("save ioexception");
                    e.printStackTrace();
                    return res.error(0,"上传失败");
                }
        }
        return res.success(0,"上传成功");
    }
}

