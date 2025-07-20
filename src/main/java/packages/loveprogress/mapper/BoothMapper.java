package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import packages.loveprogress.dto.BoothDTO;

import java.util.List;

@Mapper
public interface BoothMapper
{
    // 전체 조회
    List<BoothDTO> boothListAll();

    // 태그 필터링
    List<BoothDTO> boothListByTag(@Param("tag") List<String> tag, @Param("tagCount") int tagCount);
}
