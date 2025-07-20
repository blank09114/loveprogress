// 링크 복사
function copyLink()
{
    const url = "https://phainaxa.com";
    navigator.clipboard.writeText(url)
    .then(() => showToast("링크가 복사되었습니다!"))
    .catch(() => showToast("복사에 실패했습니다."));
}

// 햄버거 메뉴 토글
function toggleHamburgur()
{
    const ham = document.querySelector(".hamContent");
    ham.classList.toggle("show");
}

// 스크롤
function scrollTop() { window.scrollTo({ top: 0, behavior: 'smooth' }); }
window.scrollTop = scrollTop;

// 토스트 오픈
function showToast(message)
{
    const toastWrap = document.querySelector(".toastWrap");
    const toastMsg = document.querySelector(".toastMessage");

    toastMsg.textContent = message;
    toastWrap.classList.add("show");

    clearTimeout(window.toastTimer);
    window.toastTimer = setTimeout(() => { toastWrap.classList.remove("show"); }, 3000);
}

// 토스트 숨김
function hideToast() { document.querySelector(".toastWrap").classList.remove("show"); }