document.addEventListener("DOMContentLoaded", function () {
    const boothListEl = document.getElementById("boothList");
    const tagEls = document.querySelectorAll(".sectionHeader .tag");

    let selectedTags = [];

    // 태그 클릭 이벤트
    tagEls.forEach(tagEl =>
    {
        tagEl.addEventListener("click", function ()
        {
            const tagText = this.textContent.replace("#", "");
            const tagClass = this.classList[1]; // all, adult, paint 등

            // 전체보기 클릭
            if (tagClass === "tot")
            {
                selectedTags = [];
                tagEls.forEach(t => t.classList.remove("active"));
                this.classList.add("active");
                fetchBoothList();
                return;
            }

            // '일반' 또는 '성인' 처리
            if (tagClass === "all" || tagClass === "adult")
            {
                // 기존 general 태그 제거
                selectedTags = selectedTags.filter(t => t !== "일반" && t !== "성인");

                // active 제거 후 현재 태그만 추가
                tagEls.forEach(t =>
                {
                    if (t.classList.contains("all") || t.classList.contains("adult"))
                    { t.classList.remove("active"); }
                });

                if (!selectedTags.includes(tagText))
                {
                    selectedTags.push(tagText);
                    this.classList.add("active");
                }
                else { this.classList.remove("active"); }

                document.querySelector(".tag.tot")?.classList.remove("active");
                fetchBoothList(selectedTags);
                return;
            }

            // 일반 태그들 (다중 선택)
            const idx = selectedTags.indexOf(tagText);
            if (idx > -1)
            {
                selectedTags.splice(idx, 1);
                this.classList.remove("active");
            }
            else
            {
                selectedTags.push(tagText);
                this.classList.add("active");
            }

            // 전체보기 비활성화
            document.querySelector(".tag.tot")?.classList.remove("active");

            fetchBoothList(selectedTags.length > 0 ? selectedTags : []);
        });
    });

    // 부스 목록 가져오기
    function fetchBoothList(tags = [])
    {
        const url = (tags.length === 0)
            ? `/api/booth/`
            : `/api/booth/filter?` + tags.map(t => `tag=${encodeURIComponent(t)}`).join("&");

        fetch(url)
            .then(res => res.json())
            .then(data =>
            {
                boothListEl.innerHTML = "";

                boothListEl.innerHTML = "";

                data.forEach(booth =>
                {
                    const tagNames =
                    [
                        booth.tag1,
                        booth.tag2,
                        booth.tag3,
                        booth.tag4,
                        booth.tag5,
                        booth.tag6
                    ].filter(tag => tag && tag.trim() !== "")

                    const tagListHTML = tagNames.map
                    (tag => `<li class="tagItem"><a class="tag ${tagClass(tag)}">#${tag}</a></li>`).join("");

                    // 참가자 링크 렌더링
                    const participants = [];

                     if (booth.owner)
                     {
                         const link = booth.ownerLink ? `href="${booth.ownerLink}" target="_blank"` : "";
                         participants.push(`<a class="boothUser" ${link}>${booth.owner}</a>`);
                     }
                     if (booth.user1)
                     {
                         const link = booth.user1Link ? `href="${booth.user1Link}" target="_blank"` : "";
                         participants.push(`<a class="boothUser" ${link}>${booth.user1}</a>`);
                     }
                     if (booth.user2)
                     {
                         const link = booth.user2Link ? `href="${booth.user2Link}" target="_blank"` : "";
                         participants.push(`<a class="boothUser" ${link}>${booth.user2}</a>`);
                     }
                     if (booth.user3)
                     {
                         const link = booth.user3Link ? `href="${booth.user3Link}" target="_blank"` : "";
                         participants.push(`<a class="boothUser" ${link}>${booth.user3}</a>`);
                     }

                     const participantLine = participants.length > 0
                         ? `<p class="boothUsers">${participants.join("　·　")}</p>`
                         : "";

                    const li = document.createElement("li");
                    li.className = "boothItem";
                    li.innerHTML =
                    `
                        <div class="boothImg"
                             style="background-image: url('${booth.img}');"
                             data-default="${booth.img}"
                             data-hover="${booth.hoverImg}"></div>
                        <h2 class="boothTitle">${booth.name}</h2>
                        ${participantLine}
                        <ul class="tagList" style="margin: 0;">${tagListHTML}</ul>
                    `;
                    boothListEl.appendChild(li);
                });

                applyHoverEffect();
            });
    }

    // 태그 클래스 매핑
    function tagClass(tag)
    {
        const map =
        {
            "일반": "all",
            "성인": "adult",
            "그림 회지": "paint",
            "글 회지": "write",
            "팬시 굿즈": "fancy",
            "수공예품": "craft",
            "무료나눔": "free"
        };
        return map[tag] || "";
    }

    // 호버 효과
    function applyHoverEffect()
    {
        document.querySelectorAll(".boothImg").forEach(img =>
        {
            const def = img.dataset.default;
            const hov = img.dataset.hover;
            img.addEventListener("mouseenter", () => img.style.backgroundImage = `url(${hov})`);
            img.addEventListener("mouseleave", () => img.style.backgroundImage = `url(${def})`);
        });
    }

    // 초기 전체 부스 조회
    fetchBoothList();
    document.querySelector(".tag.tot")?.classList.add("active");
});