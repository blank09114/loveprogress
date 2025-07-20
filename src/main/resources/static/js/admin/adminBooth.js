let isEditMode = false;
let editingBoothId = null;

// 부스 등록
async function submitBooth()
{
    const name = document.getElementById("name");
    const sumImg = document.getElementById("sumImg");
    const hoverInput = document.getElementById("hoverImg");
    const owner = document.getElementById("owner");
    const pw = document.getElementById("pw");

    if (!name.value.trim()) return alert("부스 이름을 입력해주세요.");
    if (!isEditMode && (!sumImg.files || sumImg.files.length === 0)) return alert("부스컷 이미지를 등록해주세요.");

    const tagChecked = document.querySelector('input[name="tag"]:checked');
    if (!tagChecked) return alert("일반/성인 여부를 선택해주세요.");

    const keywordChecked = document.querySelectorAll('input[name="keyword"]:checked');
    if (keywordChecked.length === 0) return alert("최소 한 개 이상의 키워드를 선택해주세요.");

    if (!owner.value.trim()) return alert("대표자 닉네임을 입력해주세요.");
    if (!pw.value.trim()) return alert("부스 비밀번호를 입력해주세요.");

    // 이미지 처리
    let imgUrl = null;
    if (sumImg.files.length > 0)
    {
        const imgForm = new FormData();
        imgForm.append("image", sumImg.files[0]);
        const imgRes = await fetch("/api/img/upload/booth", { method: "POST", body: imgForm });
        const imgData = await imgRes.json();
        if (!imgData.success) return alert("부스컷 업로드 실패");
        imgUrl = imgData.url;
    }
    else if (isEditMode)
    { imgUrl = document.querySelector(`tr[data-id="${editingBoothId}"] img`).src; }

    let hoverImgUrl = null;
    if (hoverInput.files.length > 0)
    {
        const hoverForm = new FormData();
        hoverForm.append("image", hoverInput.files[0]);
        const hoverRes = await fetch("/api/img/upload/booth", { method: "POST", body: hoverForm });
        const hoverData = await hoverRes.json();
        if (!hoverData.success) return alert("롤오버 이미지 업로드 실패");
        hoverImgUrl = hoverData.url;
    }
    else if (isEditMode)
    {
        const hoverCell = document.querySelector(`tr[data-id="${editingBoothId}"]`).children[3];
        const imgEl = hoverCell.querySelector("img");
        if (imgEl) hoverImgUrl = imgEl.src;
    }

    // 태그 수집
    const tagList = [];
    const adultText = tagChecked.nextSibling.textContent.trim();
    if (adultText === "일반") tagList.push(1);
    else if (adultText === "성인") tagList.push(2);

    keywordChecked.forEach(cb =>
    {
        const text = cb.nextSibling.textContent.trim();
        if (text === "그림 회지") tagList.push(3);
        else if (text === "글 회지") tagList.push(4);
        else if (text === "팬시 굿즈") tagList.push(5);
        else if (text === "수공예품") tagList.push(6);
        else if (text === "무료나눔") tagList.push(7);
    });

    // 부스 정보 수집
    const booth = {};
    if (isEditMode) booth.boothId = parseInt(editingBoothId);

    booth.name = name.value.trim();
    booth.pw = pw.value.trim();
    booth.owner = owner.value.trim();
    booth.ownerLink = owner.nextElementSibling.value.trim() || null;

    booth.user1 = document.getElementById("user1").value.trim() || null;
    booth.user1Link = document.getElementById("user1Link").value.trim() || null;

    booth.user2 = document.getElementById("user2").value.trim() || null;
    booth.user2Link = document.getElementById("user2Link").value.trim() || null;

    booth.user3 = document.getElementById("user3").value.trim() || null;
    booth.user3Link = document.getElementById("user3Link").value.trim() || null;

    if (imgUrl) booth.img = imgUrl;
    if (hoverImgUrl) booth.hoverImg = hoverImgUrl;

    // 전송
    const endpoint = isEditMode ? "/api/admin/booth/update" : "/api/admin/booth/register";
    const res = await fetch(endpoint,
    {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ booth, tagList })
    });

    const data = await res.json();
    if (data.result === "success")
    {
        alert(isEditMode ? "수정 완료!" : "부스 등록 완료!");
        isEditMode = false;
        editingBoothId = null;
        document.querySelector("form h2").textContent = "부스 등록";
        document.querySelector(".sub").textContent = "등록하기";
        location.reload();
    }
    else { alert("요청 실패"); }
}

// 부스 수정
function editBooth(btn)
{
    const row = btn.closest("tr");
    editingBoothId = parseInt(row.getAttribute("data-id"));

    // 이름, 비밀번호
    document.getElementById("name").value = row.children[1].textContent.trim();
    document.getElementById("pw").value = row.children[6].textContent.trim();

    // 대표자
    document.getElementById("owner").value = row.dataset.owner || "";
    document.getElementById("owner").nextElementSibling.value = row.dataset.ownerLink || "";

    // 참가자
    document.getElementById("user1").value = row.dataset.user1 || "";
    document.getElementById("user1Link").value = row.dataset.user1Link || "";
    document.getElementById("user2").value = row.dataset.user2 || "";
    document.getElementById("user2Link").value = row.dataset.user2Link || "";
    document.getElementById("user3").value = row.dataset.user3 || "";
    document.getElementById("user3Link").value = row.dataset.user3Link || "";

    // 태그
    const tagText = row.children[5].textContent;
    const tagArray = tagText.split(",").map(t => t.trim());

    document.querySelectorAll('input[name="tag"]').forEach(r => { r.checked = tagArray.includes(r.nextSibling.textContent.trim()); });

    document.querySelectorAll('input[name="keyword"]').forEach(c => { c.checked = tagArray.includes(c.nextSibling.textContent.trim()); });

    // 수정 모드 전환
    isEditMode = true;
    document.querySelector("form h2").textContent = "부스 수정";
    document.querySelector(".sub").textContent = "수정하기";
}

// 부스 삭제
async function deleteBooth(btn)
{
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const row = btn.closest("tr");
    const boothId = parseInt(row.getAttribute("data-id"));

    const res = await fetch("/api/admin/booth/delete",
    {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ boothId })
    });

    const data = await res.json();
    if (data.result === "success") {
        alert("삭제 완료!");
        location.reload();
    }
    else { alert("삭제 실패"); }
}