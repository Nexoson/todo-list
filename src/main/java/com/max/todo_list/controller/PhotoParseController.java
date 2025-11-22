package com.max.todo_list.controller;

import com.max.todo_list.constant.ResultCodeConstant;
import com.max.todo_list.dto.RestResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

@Tag(name = "照片解析视图层")
@Slf4j
@RestController
@RequestMapping("/api/photo")
public class PhotoParseController {

    static {
        try {
            Loader.load(org.bytedeco.opencv.global.opencv_core.class);
            log.info("JavaCV OpenCV native library loaded successfully.");
        } catch (Exception e) {
            log.error("Failed to load JavaCV native library", e);
            throw new RuntimeException("OpenCV 初始化失败", e);
        }
    }

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResult<Object> parsePhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "blue") String bgColor
    ) {
        try {
            if (file.isEmpty()) {
                return new RestResult<>(ResultCodeConstant.CODE_FAILURE, "文件不能为空", null);
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"))) {
                return new RestResult<>(ResultCodeConstant.CODE_FAILURE, "只支持PNG和JPG格式的图片", null);
            }

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.lastIndexOf(".") > 0)
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            Scalar backgroundColor = parseBackgroundColor(bgColor);
            String processedFilename = processImage(filePath.toString(), backgroundColor);

            Map<String, Object> result = new HashMap<>();
            result.put("filename", processedFilename);
            result.put("originalName", originalFilename);
            result.put("url", UPLOAD_DIR + processedFilename);
            result.put("size", file.getSize());
            result.put("type", "image/png");

            return new RestResult<>(ResultCodeConstant.CODE_SUCCESS, ResultCodeConstant.CODE_SUCCESS_MSG, result);

        } catch (IOException e) {
            log.error("文件上传异常", e);
            return new RestResult<>(ResultCodeConstant.CODE_FAILURE, "文件上传失败：" + e.getMessage(), null);
        } catch (Exception e) {
            log.error("图像处理异常", e);
            return new RestResult<>(ResultCodeConstant.CODE_FAILURE, "图像处理失败：" + e.getMessage(), null);
        }
    }

    private Scalar parseBackgroundColor(String color) {
        switch (color.toLowerCase()) {
            case "red":
                return new Scalar(0, 0, 255, 0); // B, G, R, A
            case "blue":
                return new Scalar(255, 0, 0, 0);
            case "white":
                return new Scalar(255, 255, 255, 0);
            case "green":
                return new Scalar(0, 255, 0, 0);
            default:
                if (color.startsWith("#") && color.length() == 7) {
                    try {
                        int r = Integer.parseInt(color.substring(1, 3), 16);
                        int g = Integer.parseInt(color.substring(3, 5), 16);
                        int b = Integer.parseInt(color.substring(5, 7), 16);
                        return new Scalar(b, g, r, 0);
                    } catch (Exception ignored) {
                    }
                }
                return new Scalar(255, 0, 0, 0);
        }
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取图片失败: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }

    private String processImage(String inputPath, Scalar backgroundColor) {
        Mat image = imread(inputPath);
        if (image.empty()) {
            throw new RuntimeException("无法读取图片: " + inputPath);
        }

        try {
            if (image.channels() == 1) {
                Mat temp = new Mat();
                cvtColor(image, temp, COLOR_GRAY2BGR);
                image.release();
                image = temp;
            }

            Mat gray = new Mat();
            cvtColor(image, gray, COLOR_BGR2GRAY);

            Mat blurred = new Mat();
            GaussianBlur(gray, blurred, new Size(5, 5), 0);

            Mat edges = new Mat();
            Canny(blurred, edges, 50, 150);

            Mat kernel = getStructuringElement(MORPH_ELLIPSE, new Size(5, 5));
            morphologyEx(edges, edges, MORPH_CLOSE, kernel);

            // ✅ 修正1: 使用 MatVector，hierarchy 可传 null
            MatVector contours = new MatVector();
            findContours(edges, contours, new Mat(), RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

            if (contours.size() == 0) {
                throw new RuntimeException("未检测到有效轮廓");
            }

            long maxArea = 0;
            Mat maxContour = null;
            for (long i = 0; i < contours.size(); i++) {
                Mat contour = contours.get(i);
                // ✅ 修正2: contourArea 接受 Mat
                double area = contourArea(contour);
                if (area > maxArea) {
                    maxArea = (long) area;
                    maxContour = contour;
                }
            }

            Mat mask = new Mat(image.rows(), image.cols(), CV_8UC1, new Scalar(0));

            if (maxContour != null) {
                MatVector maxContours = new MatVector(1);
                maxContours.put(0, maxContour);
                // ✅ 修正3: fillPoly 使用 MatVector，Scalar 为4参数
                fillPoly(mask, maxContours, new Scalar(255, 255, 255, 0));
            }

            // ✅ 修正4: dilate 的 kernel 传 null 表示默认 3x3 核
            dilate(mask, mask, new Mat(), new Point(-1, -1), 2, BORDER_CONSTANT, new Scalar(0));

            Mat background = new Mat(image.rows(), image.cols(), CV_8UC3, backgroundColor);
            Mat foreground = new Mat();
            image.copyTo(foreground, mask);

            Mat invMask = new Mat();
            bitwise_not(mask, invMask);
            Mat bgPart = new Mat();
            background.copyTo(bgPart, invMask);

            Mat result = new Mat();
            add(foreground, bgPart, result);

            String outputPath = inputPath.substring(0, inputPath.lastIndexOf('.')) + "_processed.png";
            imwrite(outputPath, result);

            return outputPath.substring(outputPath.lastIndexOf('/') + 1);

        } finally {
            if (image != null && !image.isNull()) {
                image.release();
            }
        }
    }
}