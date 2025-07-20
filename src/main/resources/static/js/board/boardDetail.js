let deleteTarget = null;
const loginUserRole = document.body.dataset.userRole;

// 댓글 작성
function subComment()
{
    const textarea = document.getElementById("commentText");
    const content = textarea.value.trim();

    if (content === "")
    {
        alert("댓글 내용을 입력해주세요.");
        textarea.focus();
        return;
    }

    const postId = document.body.dataset.postId;
    if (!postId)
    {
        alert("게시글 ID를 찾을 수 없습니다.");
        return;
    }

    fetch("/api/comments",
    {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postId, commentContent: content })
    })
    .then(res =>
    {
        if (res.ok)
        {
            textarea.value = "";
            loadComments();
        }
        else { alert("댓글 작성 실패"); }
    })
    .catch(err =>
    {
        console.error("댓글 등록 오류:", err);
        alert("서버 오류 발생");
    });
}

// 댓글 목록 불러오기
function loadComments()
{
    const postId = document.body.dataset.postId;

    fetch(`/api/comments?postId=${postId}`)
        .then(res => res.json())
        .then(data =>
        {
            const listEl = document.querySelector(".commentList");
            listEl.innerHTML = "";

            if (data.length === 0)
            {
                listEl.innerHTML = "<p style='color: gray;'>등록된 댓글이 없습니다.</p>";
                document.querySelector(".commentHeader span").textContent = "0";
                return;
            }

            for (const comment of data)
            {
                const item = document.createElement("div");
                item.className = "commentItem";
                item.dataset.commentId = comment.commentId;

                const info = document.createElement("h3");
                info.className = "info";
                info.textContent = `${comment.writerName} · ${comment.commentDate.substring(0, 10)}`;

                const content = document.createElement("p");
                content.className = "commentContent";
                content.textContent = comment.commentContent;

                // 수정 폼
                const form = document.createElement("div");
                form.className = "editForm";
                form.style.display = "none";

                const formHeader = document.createElement("p");
                formHeader.className = "editFormHeader";
                formHeader.textContent = "댓글 수정";

                const textarea = document.createElement("textarea");
                textarea.className = "editTextarea";
                textarea.value = comment.commentContent;

                const btnBox = document.createElement("div");
                btnBox.className = "editBtns";

                const saveBtn = document.createElement("button");
                saveBtn.type = "button";
                saveBtn.className = "comBtn2";
                saveBtn.textContent = "저장";
                saveBtn.addEventListener("click", () => saveEdit(saveBtn));

                const cancelBtn = document.createElement("button");
                cancelBtn.type = "button";
                cancelBtn.className = "comBtn2";
                cancelBtn.textContent = "취소";
                cancelBtn.addEventListener("click", () => cancelEdit(cancelBtn));

                btnBox.appendChild(saveBtn);
                btnBox.appendChild(document.createTextNode(" · "));
                btnBox.appendChild(cancelBtn);

                form.appendChild(formHeader);
                form.appendChild(textarea);
                form.appendChild(btnBox);

                item.appendChild(info);
                item.appendChild(content);
                item.appendChild(form);

                // 버튼 조건: 작성자 or 관리자
                const isOwner = comment.owner;
                const isAdmin = loginUserRole === "ADMIN";

                if (isOwner || isAdmin)
                {
                    const btnWrap = document.createElement("div");
                    btnWrap.className = "comBtnsWrap";

                    if (isOwner)
                    {
                        const editBtn = document.createElement("button");
                        editBtn.className = "comBtn2";
                        editBtn.textContent = "수정";
                        editBtn.addEventListener("click", () => editFormOpen(editBtn));
                        btnWrap.appendChild(editBtn);
                        btnWrap.appendChild(document.createTextNode(" · "));
                    }

                    const delBtn = document.createElement("button");
                    delBtn.className = "comBtn2";
                    delBtn.textContent = "삭제";
                    delBtn.addEventListener("click", () => openModal(delBtn));
                    btnWrap.appendChild(delBtn);

                    item.appendChild(btnWrap);
                }

                listEl.appendChild(item);
            }

            document.querySelector(".commentHeader span").textContent = data.length;
        })
        .catch(err => { console.error("댓글 로딩 실패:", err); });
}

// 수정 폼 열기
function editFormOpen(btn)
{
    document.querySelectorAll(".editForm").forEach(f => f.style.display = "none");
    document.querySelectorAll(".commentContent").forEach(c => c.style.display = "block");

    const commentItem = btn.closest(".commentItem");
    commentItem.querySelector(".commentContent").style.display = "none";
    commentItem.querySelector(".editForm").style.display = "block";
}

// 수정
function saveEdit(btn)
{
    const commentItem = btn.closest(".commentItem");
    const textarea = commentItem.querySelector(".editTextarea");
    const content = textarea.value.trim();

    if (content === "")
    {
        alert("내용을 입력해주세요.");
        return;
    }

    const commentId = commentItem.dataset.commentId;

    fetch(`/api/comments/${commentId}/edit`,
    {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ commentContent: content })
    })
    .then(res => {
        if (res.ok)
        {
            commentItem.querySelector(".commentContent").textContent = content;
            commentItem.querySelector(".commentContent").style.display = "block";
            commentItem.querySelector(".editForm").style.display = "none";
        }
        else { alert("수정 실패"); }
    })
    .catch(err =>
    {
        console.error("수정 요청 실패:", err);
        alert("서버 오류 발생");
    });
}

// 수정 취소
function cancelEdit(btn)
{
    const commentItem = btn.closest(".commentItem");
    commentItem.querySelector(".editForm").style.display = "none";
    commentItem.querySelector(".commentContent").style.display = "block";
}

// 삭제 모달
function openModal(btn)
{
    deleteTarget = btn.closest(".commentItem");
    document.querySelector(".modalOverlay").style.display = "flex";
}

function closeModal()
{
    deleteTarget = null;
    document.querySelector(".modalOverlay").style.display = "none";
}

// 삭제 확정
function Delete()
{
    if (!deleteTarget) return;

    const commentId = deleteTarget.dataset.commentId;

    fetch(`/api/comments/${commentId}`, { method: "DELETE" })
    .then(res =>
    {
        if (res.ok)
        {
            deleteTarget.remove();
            deleteTarget = null;
            document.querySelector(".modalOverlay").style.display = "none";
            loadComments(); // 새로고침으로 전체 다시 불러옴
        }
        else { alert("삭제 실패"); }
    })
    .catch(err =>
    {
        console.error("삭제 요청 실패:", err);
        alert("서버 오류 발생");
    });
}

// 게시글 삭제
function deletePost()
{
    const postId = document.body.dataset.postId;
    const postType = document.body.dataset.boardType;

    if (!confirm("정말 이 게시글을 삭제하시겠습니까?")) return;

    fetch(`/api/board/${postType}/delete/${postId}`, { method: "DELETE"  })
    .then(res =>
    {
        if (res.ok)
        {
            alert("삭제되었습니다.");
            location.href = `/board/${postType.toLowerCase()}`;
        }
        else if (res.status === 403) { alert("관리자만 삭제할 수 있습니다."); }
        else { alert("삭제 실패"); }
    })
    .catch(err =>
    {
        console.error("삭제 오류:", err);
        alert("서버 오류 발생");
    });
}

// 페이지 진입 시
document.addEventListener("DOMContentLoaded", function () { loadComments(); });