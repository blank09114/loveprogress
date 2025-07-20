let allQnaData = [];

// 질문 조회
function renderQnaList(list)
{
    const tbody = document.getElementById('adminQnaListBody');
    tbody.innerHTML = "";

    list.forEach((qna, idx) =>
    {
        const tr = document.createElement("tr");
        const number = list.length - idx;

        const writer = qna.userId
            ? `${qna.qnaUserName}(${qna.userId})`
            : `${qna.qnaUserName}(비회원)`;

        const contentDisplay = qna.qnaContent.length > 15
            ? qna.qnaContent.substring(0, 15) + "..."
            : qna.qnaContent;

        const secret = qna.qnaSecret ? "비공개" : "공개";
        const answered = qna.answerContent ? "답변완료" : "답변 대기";
        const dateOnly = qna.qnaDate ? qna.qnaDate.split("T")[0] : "-";

        tr.innerHTML =
        `
            <td>${number}</td>
            <td>${escapeHtml(writer)}</td>
            <td>${escapeHtml(contentDisplay)}</td>
            <td>${dateOnly}</td>
            <td>${secret}</td>
            <td>${answered}</td>
            <td>
                <button class="btn" onclick="viewDetail(${qna.qnaId})">상세 보기</button>
                <button class="btn" onclick="deleteQna(${qna.qnaId})">삭제</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

// XSS 방지
function escapeHtml(text)
{
    const div = document.createElement("div");
    div.innerText = text;
    return div.innerHTML;
}

// 질문 삭제
function deleteQna(qnaId)
{
    if (!confirm("질문을 삭제하시겠습니까?")) return;

    fetch(`/api/admin/qna/${qnaId}`, { method: 'DELETE' })
    .then(res =>
    {
        if (!res.ok) throw new Error("삭제 실패");
        return res.json();
    })
    .then(data =>
    {
        alert("삭제됐습니다.");

        // 목록 재조회
        return fetch('/api/admin/qna/list');
    })
    .then(res => res.json())
    .then(data =>
    {
        allQnaData = data;
        renderQnaList(data);

        // 답변 폼 초기화
        document.querySelector(".queInfo").innerHTML =
        `
            <p class="queInfoTitle"> 번호 </p><p></p>
            <p class="queInfoTitle"> ID </p><p></p>
        `;
        document.querySelector(".questionContent").innerText = "";
        document.getElementById("answerContent").value = "";
        delete document.getElementById("answer").dataset.qnaId;
    })
    .catch(err =>
    {
        console.error(err);
        alert("삭제 중 오류가 발생했습니다.");
    });
}

// 상세 보기
function viewDetail(qnaId)
{
    const target = allQnaData.find(q => q.qnaId === qnaId);
    if (!target) return;

    // 번호 & ID 출력
    document.querySelector(".queInfo").innerHTML =
    `
        <p class="queInfoTitle"> 번호 </p>
        <p>${target.qnaId}</p>
        <p class="queInfoTitle"> ID </p>
        <p>${target.userId ?? target.qnaUserName}</p>
    `;

    // 질문 내용 출력
    document.querySelector(".questionContent").innerText = target.qnaContent;

    const textarea = document.getElementById("answerContent");
    const btnWrap = document.querySelector(".btnWrap");

    // 답변 내용 표시
    textarea.value = target.answerContent ?? "";
    textarea.readOnly = false;
    btnWrap.style.display = "flex";

    // qnaId 저장
    document.getElementById("answer").dataset.qnaId = qnaId;
}

// 답변 등록
function subAnswer()
{
    const answerContent = document.getElementById('answerContent');
    const answer = answerContent.value.trim();
    const qnaId = document.getElementById("answer").dataset.qnaId;

    if (answer === "")
    {
        alert("답변 내용을 입력해주세요.");
        answerContent.focus();
        return;
    }

    if (!qnaId)
    {
        alert("QnA 항목을 선택해주세요.");
        return;
    }

    fetch('/api/admin/qna/answer',
    {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ qnaId: Number(qnaId), answerContent: answer })
    })
    .then(res =>
    {
        if (!res.ok) throw new Error("등록 실패");
        return res.json();
    })
    .then(data =>
    {
        alert("답변이 등록됐습니다.");

        // 목록 갱신
        return fetch('/api/admin/qna/list');
    })
    .then(res => res.json())
    .then(data =>
    {
        allQnaData = data;
        renderQnaList(data);
    })
    .catch(err =>
    {
        console.error(err);
        alert("답변 등록 중 오류가 발생했습니다.");
    });
}

// 이벤트 리스너
document.addEventListener("DOMContentLoaded", () =>
{
    fetch('/api/admin/qna/list')
        .then(res => res.json())
        .then(data =>
        {
            allQnaData = data;
            renderQnaList(data);
        });
});