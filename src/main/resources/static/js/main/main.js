let index = 0;
let totSlides;

// 슬라이드 이동
function moveSlideTo(targetIndex)
{
    const track = document.querySelector(".bannerTrack");
    const slides = document.querySelectorAll(".bannerImg");
    const topBanner = document.querySelector(".topBanner");

    if (targetIndex < 0) targetIndex = totSlides - 1;
    if (targetIndex >= totSlides) targetIndex = 0;

    index = targetIndex;
    const offset = -index * 100;
    track.style.transform = `translateX(${offset}%)`;

    // 배경색 변경
    const color = slides[index].dataset.color;
    if (color) { topBanner.style.backgroundColor = `#${color}`; }

    // 도트 상태 갱신
    const dots = document.querySelectorAll(".dot");
    dots.forEach((dot, idx) => { dot.classList.toggle("active", idx === index); });
}

function moveSlide(direction) { moveSlideTo(index + direction); }

// 반응형 높이 계산
function autoHeights()
{
    const bannerItem = document.querySelector(".bannerItem");
    const noticeMenu = document.querySelector(".banner");
    const noticeItems = document.querySelectorAll(".noticeMenuItem");

    if (window.innerWidth > 768)
    {
        if (bannerItem) bannerItem.style.height = "";
        if (noticeMenu) noticeMenu.style.height = "";
        noticeItems.forEach(item => item.style.height = "");
        return;
    }

    if (bannerItem)
    {
        const width = bannerItem.offsetWidth;
        bannerItem.style.height = `${width * (45 / 128)}px`;
    }

    if (noticeMenu)
    {
        const width = noticeMenu.offsetWidth;
        noticeMenu.style.height = `${width * (3 / 32)}px`;
    }

    noticeItems.forEach(item =>
    {
        const width = item.offsetWidth;
        item.style.height = `${width}px`;
    });
}

// 이벤트 리스너
window.addEventListener("DOMContentLoaded", () =>
{
    const slides = document.querySelectorAll(".bannerImg");
    totSlides = slides.length;

    // 페이지 이동 이벤트
    slides.forEach(slide =>
    {
        const url = slide.dataset.url;
        if (url)
        {
            slide.style.cursor = "pointer";
            slide.addEventListener("click", () => { location.href = url; });
        }
    });

    // 도트 초기화
    const dotMenu = document.getElementById("dotMenu");
    for (let i = 0; i < totSlides; i++)
    {
        const dot = document.createElement("div");
        dot.classList.add("dot");
        if (i === 0) dot.classList.add("active");

        dot.addEventListener("click", () => moveSlideTo(i));
        dotMenu.appendChild(dot);
    }

    moveSlide(0);
    autoHeights();

    // 자동 슬라이드
    setInterval(() => { moveSlide(1); }, 3000);
});

window.addEventListener("resize", autoHeights);