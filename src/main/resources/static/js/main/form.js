// ========== 로그인 ==========
// 유효성 검사
function checkID() { return document.getElementById("userID").value.trim() !== ""; }
function checkPW() { return document.getElementById("userPW").value.trim() !== ""; }

// 로그인
function login()
{
    const id = document.getElementById("userID").value.trim();
    const pw = document.getElementById("userPW").value.trim();
    const save = document.getElementById("saveId").checked;

    if (!id)
    {
        alert("ID를 입력해주세요.");
        document.getElementById("userID").focus();
        return;
    }

    if (!pw)
    {
        alert("비밀번호를 입력해주세요.");
        document.getElementById("userPW").focus();
        return;
    }

    // 로컬 스토리지에 ID 저장
    if (save) { localStorage.setItem("savedUserId", id); }
    else { localStorage.removeItem("savedUserId"); }

    // 폼 제출
    document.getElementById("loginForm").submit();
}

// 로컬 스토리지에 저장된 ID 불러오기
document.addEventListener("DOMContentLoaded", function ()
{
    const savedId = localStorage.getItem("savedUserId");
    if (savedId)
    {
        document.getElementById("userID").value = savedId;
        document.getElementById("saveId").checked = true;
    }
});




// ========== 회원가입 ==========
// ID 중복 확인 여부 전역 변수
let isIdChecked = false;
let lastCheckedId = "";

// ID 유효성 검사
function idValidation()
{
    const id = document.getElementById("idField").value.trim();
    const warning = document.getElementById("idWarning");
    const ok = document.getElementById("idOK");

    // ID가 이전에 확인된 값과 다르면 중복 확인 무효화
    if (id !== lastCheckedId) {
        isIdChecked = false;
        ok.textContent = "";
    }

    if (id === "") {
        warning.textContent = "ID는 필수 요소입니다.";
        return false;
    }

    const onlyNumber = /^[0-9]+$/;
    const validFormat = /^[a-zA-Z0-9]+$/;

    if (!validFormat.test(id)) {
        warning.textContent = "ID 형식이 올바르지 않습니다.(영문, 숫자만 허용)";
        return false;
    }

    if (onlyNumber.test(id)) {
        warning.textContent = "ID에는 영문을 포함해야 합니다.";
        return false;
    }

    warning.textContent = "";
    return true;
}

function idDupCheck()
{
    const id = document.getElementById("idField").value.trim();
    const ok = document.getElementById("idOK");
    const warning = document.getElementById("idWarning");

    if (!idValidation())
    {
        ok.textContent = "";
        isIdChecked = false;
        return;
    }

    ok.textContent = "확인 중...";

    fetch(`/signup/check-id?userId=${encodeURIComponent(id)}`)
        .then(res => res.json())
        .then(isTaken =>
        {
            if (isTaken)
            {
                warning.textContent = "이미 사용 중인 ID입니다.";
                ok.textContent = "";
                isIdChecked = false;
            }
            else
            {
                ok.textContent = "사용할 수 있는 ID입니다.";
                warning.textContent = "";
                isIdChecked = true;
                lastCheckedId = id;
            }
        })
        .catch(err =>
        {
            warning.textContent = "중복 확인 중 오류가 발생했습니다.";
            ok.textContent = "";
            isIdChecked = false;
        });
}

// PW 유효성 검사
function pwValidation()
{
    const pw = document.getElementById("pwField").value.trim();
    const warning = document.getElementById("pwWarning");

    if (pw === "")
    {
        warning.textContent = "비밀번호는 필수 요소입니다.";
        return false;
    }

    const validFormat = /^[a-zA-Z0-9]+$/;
    const hasLetter = /[a-zA-Z]/.test(pw);
    const hasNumber = /[0-9]/.test(pw);

    if (!validFormat.test(pw))
    {
        warning.textContent = "비밀번호 형식이 올바르지 않습니다.(영문, 숫자만 허용)";
        return false;
    }

    if (!(hasLetter && hasNumber))
    {
        warning.textContent = "비밀번호에는 영문과 숫자를 모두 포함해야 합니다.";
        return false;
    }

    if (pw.length < 8)
    {
        warning.textContent = "비밀번호는 8글자 이상으로 해야 합니다.";
        return false;
    }

    warning.textContent = "";
    return true;
}

// 비밀번호 보기 toggle
function togglePw()
{
    const pwInput = document.getElementById("pwField");
    pwInput.type = (pwInput.type === "password") ? "text" : "password";
}

// 도메인 선택 핸들링
function handleDomain()
{
    const select = document.getElementById("domainSelect");
    const domainInput = document.getElementById("domainField");
    const selected = select.value;

    if (selected === "none")
    {
        domainInput.value = "";
        domainInput.readOnly = true;
    }
    else if (selected === "costum")
    {
        domainInput.value = "";
        domainInput.readOnly = false;
        domainInput.focus();
    }
    else
    {
        domainInput.value = selected + ".com";
        domainInput.readOnly = true;
    }

    emailValidation();
}

// 이메일 유효성 검사
function emailValidation()
{
    const local = document.getElementById("mailField").value.trim();
    const domain = document.getElementById("domainField").value.trim();
    const domainSelect = document.getElementById("domainSelect").value;
    const warning = document.getElementById("mailWarning");

    if (local === "")
    {
        warning.textContent = "이메일은 필수 입력 요소입니다.";
        return false;
    }

    if (domainSelect === "none" || domain === "")
    {
        warning.textContent = "도메인을 선택해주세요.";
        return false;
    }

    const email = local + "@" + domain;

    const validChars = /^[a-zA-Z0-9._-]+$/;
    if (!validChars.test(local) || !validChars.test(domain))
    {
        warning.textContent = "이메일 형식이 올바르지 않습니다.";
        return false;
    }

    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email))
    {
        warning.textContent = "이메일 형식이 올바르지 않습니다.";
        return false;
    }

    warning.textContent = "";
    return true;
}

// 전화번호 유효성 검사
function phoneValidation()
{
    const phone = document.getElementById("phone").value.trim();
    const warning = document.getElementById("phoneWarning");

    const validFormat = /^[0-9]{4}$/;

    if (phone === "")
    {
        warning.textContent = "";
        return true;
    }

    if (!validFormat.test(phone))
    {
        warning.textContent = "전화번호 형식이 올바르지 않습니다.";
        return false;
    }

    warning.textContent = "";
    return true;
}

// 회원가입 제출
function signUp()
{
    const idValid = idValidation();
    const pwValid = pwValidation();
    const emailValid = emailValidation();
    const phoneValid = phoneValidation();

    if (!idValid) return document.getElementById("idField").focus();
    if (!pwValid) return document.getElementById("pwField").focus();
    if (!emailValid) return document.getElementById("mailField").focus();
    if (!phoneValid) return document.getElementById("phone").focus();

    if (!isIdChecked)
    {
        alert("ID 중복 확인을 진행해주세요.");
        document.getElementById("idField").focus();
        return;
    }

    // 이메일 조합해서 숨겨진 필드에 저장
    const local = document.getElementById("mailField").value.trim();
    const domain = document.getElementById("domainField").value.trim();
    document.getElementById("emailFull").value = local + "@" + domain;

    // 최종 제출
    document.getElementById("joinForm").submit();
}



// 비밀번호 변경
function pwChange()
{
    const pwInput = document.getElementById("userPW");
    const newPw = pwInput.value.trim();

    if (newPw === "")
    {
        alert("새 비밀번호를 입력해주세요.");
        pwInput.focus();
        return;
    }

    fetch("/api/user/password",
    {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userPw: newPw })  // ← DTO에 맞는 필드명!
    })
    .then(res =>
    {
        if (!res.ok) throw new Error("비밀번호 변경 실패");
        return res.text();
    })
    .then(msg =>
    {
        alert("비밀번호가 성공적으로 변경되었습니다.");
        window.location.href = "/";  // 홈 또는 마이페이지로 리다이렉트
    })
    .catch(err =>
    {
        alert("비밀번호 변경 중 오류가 발생했습니다.");
        console.error(err);
    });
}
