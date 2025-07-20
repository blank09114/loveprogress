package packages.loveprogress.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packages.loveprogress.setting.ImgUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/img")
public class ImgApi
{
    @Value("${file.upload.path}")
    private String uploadRoot;  // 예: /web/tmp/uploads

    private static final int MAX_WIDTH = 1300; // 최대 너비

    @PostMapping(value = "/upload/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadImage
    (@PathVariable("category") String category, @RequestParam("image") MultipartFile image)
    {
        Map<String, Object> result = new HashMap<>();

        if (image.isEmpty())
        {
            result.put("success", 0);
            result.put("message", "빈 파일입니다.");
            return result;
        }

        String originalName = image.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        String saveName = UUID.randomUUID().toString() + extension;

        File saveDir = new File(uploadRoot + "/" + category);
        if (!saveDir.exists()) saveDir.mkdirs();

        File tempFile = new File(saveDir, "temp_" + saveName);   // 임시 저장
        File finalFile = new File(saveDir, saveName);            // 최종 저장

        try
        {
            image.transferTo(tempFile);
            ImgUtil.resizeImg(tempFile, finalFile, MAX_WIDTH);
            tempFile.delete();

            result.put("success", 1);
            result.put("url", "/uploads/" + category + "/" + saveName);
        }
        catch (IOException e)
        {
            log.error("이미지 업로드 실패", e);
            result.put("success", 0);
            result.put("message", "업로드 실패: " + e.getMessage());
        }

        return result;
    }
}