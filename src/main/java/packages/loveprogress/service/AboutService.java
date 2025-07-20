package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packages.loveprogress.mapper.AboutMapper;

@Service
@RequiredArgsConstructor
public class AboutService
{

    private final AboutMapper aboutMapper;

    // 불러오기
    public String getContent() { return aboutMapper.getAboutContent(); }

    // 수정
    public void updateContent(String content) { aboutMapper.updateAboutContent(content); }
}