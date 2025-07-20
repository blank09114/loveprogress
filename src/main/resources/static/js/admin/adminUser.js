document.addEventListener("DOMContentLoaded", () =>
{
    // 부스 회원 등록
    document.querySelectorAll(".registerBtn").forEach(btn =>
    {
        btn.addEventListener("click", () =>
        {
            const userId = btn.dataset.id;
            if (!confirm(`${userId} 회원을 부스 회원으로 등록하시겠습니까?`)) return;

            fetch("/api/admin/user/promote",
            {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ userId })
            })
            .then(res => res.json())
            .then(data =>
            {
                if (data.result === "success")
                {
                    alert("등업 완료!");
                    location.reload();
                }
                else { alert("처리 실패"); }
            })
            .catch(err => { alert("오류 발생: " + err); });
        });
    });

    // 비밀번호 초기화
    document.querySelectorAll(".resetBtn").forEach(btn =>
    {
        btn.addEventListener("click", () =>
        {
            const userId = btn.dataset.id;
            if (!confirm(`${userId} 회원의 비밀번호를 '702430'으로 초기화하시겠습니까?`)) return;

            fetch("/api/admin/user/resetPassword",
            {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ userId })
            })
            .then(res => res.json())
            .then(data =>
            {
                if (data.result === "success") { alert("비밀번호 초기화 완료!"); }
                else { alert("처리 실패"); }
            })
            .catch(err => alert("오류 발생: " + err));
        });
    });
});