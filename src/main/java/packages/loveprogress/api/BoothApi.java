package packages.loveprogress.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.BoothDTO;
import packages.loveprogress.service.BoothService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booth")
public class BoothApi
{
    private final BoothService boothService;

    // 전체 조회
    @GetMapping("/")
    public List<BoothDTO> getAllBooths() { return boothService.getAllBooths(); }

    // 태그 필터링 (단일 or 다중)
    @GetMapping("/filter")
    public List<BoothDTO> getBoothsByTags(@RequestParam List<String> tag) { return boothService.getBoothsByTags(tag); }
}