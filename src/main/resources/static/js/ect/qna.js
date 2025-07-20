let currentPage = 1;
const itemsPerPage = 10;
let allQnaData = [];

// 이미지 매핑
function getImageName(num)
{
    const map =
    {
        1: "파이논", 2: "용사님", 3: "■■■■■",
        4: "아낙사", 5: "작가님", 6: "드로마스팬", 7: "부스를훔치는자"
    };
    return map[num];
}

// 이미지 선택
function changeImg(selectEl)
{
    const value = selectEl.value;
    const charaDiv = document.querySelector("#regQna .chara");
    const basePath = "/img/QnA/";
    const imageUrl = basePath + value + ".jpg";
    charaDiv.style.backgroundImage = `url('${imageUrl}')`;
}

// 질문 등록
function submitQna()
{
    const nameInput = document.getElementById('nickname');
    const pwInput = document.getElementById('pw');
    const contentInput = document.getElementById('regQnaContent');
    const imgSelect = document.getElementById('profile');
    const secretCheck = document.querySelector('.secret input');

    const name = nameInput.value.trim();
    const rawPw = pwInput ? pwInput.value : null;
    const pw = rawPw && rawPw.trim() !== "" ? rawPw.trim() : null;
    const content = contentInput.value.trim();
    const selectedImg = imgSelect.value;
    const secret = secretCheck.checked;

    if (name === "") return alert("이름을 입력해주세요.");
    if (!isLogin && !pw) return alert("비밀번호를 입력해주세요.");
    if (content === "") return alert("질문 내용을 입력해주세요.");

    const match = selectedImg.match(/^\d+/);
    const imgNumber = match ? parseInt(match[0]) : 1;

    const dto = { qnaUserName: name, qnaPw: pw, qnaImg: imgNumber, qnaContent: content, qnaSecret: secret };

    fetch('/api/qna/register',
    {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dto)
    })
        .then(res => res.json())
        .then(data =>
        {
            if (data.success)
            {
                alert(data.message);
                location.reload();
            }
            else alert(data.message);
        })
        .catch(() => alert("에러가 발생했습니다."));
}

// 페이지네이션
function renderPagination(totalPages)
{
    const pagination = document.querySelector(".pages");
    pagination.innerHTML = "";

    for (let i = 1; i <= totalPages; i++)
    {
        const li = document.createElement("li");
        const a = document.createElement("a");
        a.textContent = i;
        a.href = "#";
        if (i === currentPage)
        {
            a.style.fontWeight = "bold";
            a.style.color = "#428cd2";
        }
        a.onclick = e =>
        {
            e.preventDefault();
            currentPage = i;
            renderQnaList();
        };
        li.appendChild(a);
        pagination.appendChild(li);
    }
}

// 질문 출력
function renderQnaList()
{
    const container = document.getElementById("qnaList");
    container.innerHTML = "";

    const filterMine = document.getElementById("filterMine")?.checked;
    const filtered = allQnaData.filter(q => !filterMine || loginUserId === q.userId);

    const totalPages = Math.ceil(filtered.length / itemsPerPage);
    const start = (currentPage - 1) * itemsPerPage;
    const pageItems = filtered.slice(start, start + itemsPerPage);

    renderPagination(totalPages);

    // 번호 기준: 오래된 순으로 정렬
    const numberMap = [...filtered].reverse();

    pageItems.forEach(qna =>
    {
        const isSecret = qna.qnaSecret;
        const isMineOrAdmin = isLogin && (loginUserId === qna.userId || isAdmin);

        const imgName = getImageName(qna.qnaImg);
        const imageUrl = `/img/QnA/${qna.qnaImg}${imgName}.jpg`;

        const qnaItem = document.createElement("div");
        qnaItem.className = "qnaItem";

        const question = document.createElement("div");
        question.className = "question";

        const qnaItemLeft = document.createElement("div");
        qnaItemLeft.className = "qnaItemLeft";

        const qnaInfo = document.createElement("div");
        qnaInfo.className = "qnaInfo";

        const qnaInfoLeft = document.createElement("div");
        qnaInfoLeft.className = "qnaInfoLeft";
        qnaInfoLeft.innerHTML =
        `
            <p class="qnaInfoItem">이름</p>
            <p class="qnaInfoItem name">${qna.qnaUserName}</p>
        `;

        // ✅ 오래된 질문 기준 번호 부여
        const number = numberMap.findIndex(q => q.qnaId === qna.qnaId) + 1;

        const qnaNum = document.createElement("p");
        qnaNum.className = "qnaInfoItem num";
        qnaNum.innerText = `No. ${number}`;

        qnaInfo.appendChild(qnaInfoLeft);
        qnaInfo.appendChild(qnaNum);

        const content = document.createElement("div");
        content.className = "qnaContent";
        const inner = document.createElement("div");
        inner.className = "qnaContentInner";

        if (isSecret && !isMineOrAdmin)
        {
            inner.innerText = "비공개 질문입니다.";

            if (!qna.userId)
            {
                const pwLabel = document.createElement("p");
                pwLabel.className = "qnaInfoItem pw";
                pwLabel.style.marginLeft = "20px";
                pwLabel.innerText = "비밀번호";

                const pwInputWrap = document.createElement("div");
                pwInputWrap.className = "password";
                const inputId = `pwInput-${qna.qnaId}`;
                pwInputWrap.innerHTML = `<input type="password" id="${inputId}">`;

                const viewBtn = document.createElement("button");
                viewBtn.className = "qnaInfoItem";
                viewBtn.innerText = "보기";
                viewBtn.style.marginLeft = "10px";
                viewBtn.style.border = "none";
                viewBtn.style.cursor = "pointer";
                viewBtn.onclick = () =>
                {
                    const pw = document.getElementById(inputId).value.trim();
                    if (!pw) return alert("비밀번호를 입력해주세요.");

                    fetch('/api/qna/verify',
                    {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ qnaId: qna.qnaId, qnaPw: pw })
                    })
                        .then(res => res.json())
                        .then(result =>
                        {
                            if (result.success)
                            {
                                inner.innerText = result.qnaContent;
                                const answerInnerEl = qnaItem.querySelector(".answer .qnaContentInner");
                                if (answerInnerEl) { answerInnerEl.innerText = result.answerContent || ""; }

                                pwInputWrap.remove();
                                pwLabel.remove();
                                viewBtn.remove();
                            }
                            else alert(result.message);
                        });
                };

                qnaInfoLeft.appendChild(pwLabel);
                qnaInfoLeft.appendChild(pwInputWrap);
                qnaInfoLeft.appendChild(viewBtn);
            }
        }
        else
        {
            inner.innerText = qna.qnaContent;
        }

        content.appendChild(inner);
        qnaItemLeft.appendChild(qnaInfo);
        qnaItemLeft.appendChild(content);

        const chara = document.createElement("div");
        chara.className = "chara";
        chara.style.backgroundImage = `url('${imageUrl}')`;

        question.appendChild(qnaItemLeft);
        question.appendChild(chara);
        qnaItem.appendChild(question);

        // 답변
        if (qna.answerContent)
        {
            const answer = document.createElement("div");
            answer.className = "answer";

            const answerContent = document.createElement("div");
            answerContent.className = "qnaContent";
            const answerInner = document.createElement("div");
            answerInner.className = "qnaContentInner";

            if (isSecret && !isMineOrAdmin) answerInner.innerText = "비공개 질문입니다.";
            else answerInner.innerText = qna.answerContent;

            answerContent.appendChild(answerInner);

            const answerName = document.createElement("div");
            answerName.className = "answerName";
            answerName.innerText = "교무처장";

            answer.appendChild(answerContent);
            answer.appendChild(answerName);

            qnaItem.appendChild(answer);
        }

        container.appendChild(qnaItem);
    });
}

function loadQnaList()
{
    fetch('/api/qna/list')
        .then(res => res.json())
        .then(data =>
        {
            allQnaData = data;
            renderQnaList();
        });
}

// 이벤트 리스너
window.addEventListener("DOMContentLoaded", () =>
{
    loadQnaList();
    document.getElementById("filterMine")?.addEventListener("change", () =>
    {
        currentPage = 1;
        renderQnaList();
    });
});