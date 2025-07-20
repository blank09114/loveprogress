package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AboutMapper
{
    // 불러오기
    String getAboutContent();

    // 수정
    void updateAboutContent(String content);
}