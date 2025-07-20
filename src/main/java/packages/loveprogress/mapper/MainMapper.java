package packages.loveprogress.mapper;


import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import packages.loveprogress.dto.BannerDTO;
import packages.loveprogress.dto.MainLinkDTO;

import java.util.List;

@Mapper
public interface MainMapper
{
    // 배너 출력
    List<BannerDTO> getAllBanners();

    // 링크 연결
    List<MainLinkDTO> selectMainLinks();
}
