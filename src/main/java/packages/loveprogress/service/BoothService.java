package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.BoothDTO;
import packages.loveprogress.mapper.BoothMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothService
{
    private final BoothMapper boothMapper;

    // 전체 조회
    public List<BoothDTO> getAllBooths() { return boothMapper.boothListAll(); }

    // 태그 필터링
    public List<BoothDTO> getBoothsByTags(List<String> tag) { return boothMapper.boothListByTag(tag, tag.size()); }
}