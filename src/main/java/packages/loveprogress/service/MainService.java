package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.BannerDTO;
import packages.loveprogress.dto.MainLinkDTO;
import packages.loveprogress.mapper.MainMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService
{
    private final MainMapper mainMapper;

    // 배너 출력
    public List<BannerDTO> getAllBanners() { return mainMapper.getAllBanners(); }

    // 링크 연결
    public List<MainLinkDTO> getMainLinks() { return mainMapper.selectMainLinks(); }
}