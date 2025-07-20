// 초기화 함수
async function reset(btn)
{
    const row = btn.closest("tr");
    const bannerId = row.dataset.id;

    // 확인창
    if (!confirm("이 배너를 초기화하시겠습니까?")) return;

    // UI 초기화
    row.querySelector("input[type='file']").value = "";
    row.querySelector(".colorInput").value = "";
    row.querySelector(".urlInput").value = "";
    row.querySelector(".imgPathInput").value = "";

    const img = row.querySelector("img");
    if (img) img.remove();

    // 서버 초기화 요청
    try
    {
        const res = await fetch("/api/admin/banner/clear",
        {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ num: bannerId })
        });

        const result = await res.json();
        if (result.result === "success")
        {
            alert("배너가 초기화되었습니다.");
            location.reload();
        }
        else { alert("서버 초기화 실패: " + result.message); }
    }
    catch (e)
    {
        alert("오류 발생");
        console.error(e);
    }
}

// 이미지 업로드 함수
async function uploadImg(input)
{
    const file = input.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("image", file);

    try
    {
        const res = await fetch("/api/img/upload/main",
        {
            method: "POST",
            body: formData
        });

        const result = await res.json();

        if (result.success === 1)
        {
            const path = result.url.replace("/uploads/", "");  // main/파일명.jpg
            const td = input.closest("td");

            // hidden input에 경로 저장
            td.querySelector(".imgPathInput").value = path;

            // 이미지 미리보기 교체
            const preview = td.querySelector("img");
            if (preview) { preview.src = result.url; }
            else
            {
                const img = document.createElement("img");
                img.src = result.url;
                img.style.width = "100px";
                td.prepend(img);
            }

            alert("이미지 업로드 완료");
        }
        else { alert("업로드 실패: " + result.message); }
    }
    catch (e)
    {
        alert("오류 발생");
        console.error(e);
    }
}

// 배너 수정 요청
async function updateBanner(btn)
{
    const row = btn.closest("tr");
    const bannerId = row.dataset.id;

    const formData = new URLSearchParams();
    formData.append("num", bannerId);

    const img = row.querySelector(".imgPathInput").value.trim();
    const color = row.querySelector(".colorInput").value.trim();
    const url = row.querySelector(".urlInput").value.trim();

    if (img !== "") formData.append("img", img);
    if (color !== "") formData.append("color", color);
    if (url !== "") formData.append("url", url);

    try
    {
        const res = await fetch("/api/admin/banner",
        {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData
        });
        const result = await res.json();
        if (result.result === "success")
        {
            alert("배너가 수정되었습니다.");
            location.reload();
        }
        else { alert("수정 실패: " + result.message); }
    }
    catch (e)
    {
        alert("오류 발생");
        console.error(e);
    }
}

// 링크 업데이트
async function updateLink(btn)
{
    const row = btn.closest("tr");
    const linkId = row.dataset.id;
    const url = row.querySelector(".linkInput").value.trim();

    try
    {
        const res = await fetch("/api/admin/link",
        {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ id: linkId, url })
        });
        const result = await res.json();
        if (result.result === "success")
        {
            alert("링크가 저장되었습니다.");
            location.reload();
        }
        else { alert("저장 실패: " + result.message); }
    }
    catch (e)
    {
        alert("오류 발생");
        console.error(e);
    }
}

// 링크 초기화
async function resetLink(btn)
{
    const row = btn.closest("tr");
    const linkId = row.dataset.id;

    // 확인창
    if (!confirm("이 링크를 초기화하시겠습니까?")) return;

    // UI 초기화
    row.querySelector(".linkInput").value = "";

    // 서버 초기화
    try
    {
        const res = await fetch("/api/admin/link/clear",
        {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ id: linkId })
        });
        const result = await res.json();
        if (result.result === "success")
        {
            alert("링크가 초기화되었습니다.");
            location.reload();
        }
        else { alert("초기화 실패: " + result.message); }
    }
    catch (e)
    {
        alert("오류 발생");
        console.error(e);
    }
}