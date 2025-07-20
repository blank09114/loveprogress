package packages.loveprogress.setting;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImgUtil
{
    public static void resizeImg(File inputFile, File outputFile, int maxWidth) throws IOException
    {
        String name = inputFile.getName();
        String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

        // GIF는 리사이징 없이 복사
        if (extension.equals("gif"))
        {
            Files.copy(inputFile.toPath(), outputFile.toPath());
            return;
        }

        // 원본 이미지 크기 확인
        BufferedImage originalImage = ImageIO.read(inputFile);
        int width = originalImage.getWidth();

        // 가로 크기가 1300px 이하면 그대로 복사
        if (width <= maxWidth)
        {
            Files.copy(inputFile.toPath(), outputFile.toPath());
            return;
        }

        // 1300px 초과 시 리사이징
        Thumbnails.of(inputFile)
        .size(maxWidth, maxWidth) // 비율 유지
        .outputFormat(extension)
        .toFile(outputFile);
    }
}
