document.addEventListener("DOMContentLoaded", async () =>
{
    const postType = document.body.dataset.postType;
    const tbody = document.getElementById("postListBody");
    const pagination = document.getElementById("pagination");

    const POSTS_PER_PAGE = 10;
    let postList = [];

    try
    {
        const res = await fetch(`/api/board/${postType}/list`);
        postList = await res.json();
        renderPage(1);
        renderPagination(1);
    }
    catch (e) { console.error("게시글 목록 로드 실패", e); }

    // 게시글 목록 출력
    function renderPage(page)
    {
        tbody.innerHTML = "";
        const start = (page - 1) * POSTS_PER_PAGE;
        const end = start + POSTS_PER_PAGE;
        const pagePosts = postList.slice(start, end);

        pagePosts.forEach((post, idx) =>
        {
            const tr = document.createElement("tr");
            tr.classList.add("write");

            const no = postList.length - (start + idx);
            const titleHtml =
            `
                <a href="/board/${post.postType}/${post.postId}" class="postLink">
                    ${post.postSecret ? '🔒 ' : ''}${post.postTitle}
                </a>
            `;
            const author = post.userRole === 'ADMIN' ? '교무처장' : post.userId;
            const date = post.postDate?.split('T')[0];

            tr.innerHTML =
            `
                <td>${no}</td>
                <td class="td-left">${titleHtml}</td>
                <td>${author}</td>
                <td class="td-time">${date}</td>
            `;

            tbody.appendChild(tr);
        });
    }

    // 페이지네이션
    function renderPagination(selectedPage = 1)
    {
        pagination.innerHTML = "";
        const totalPages = Math.ceil(postList.length / POSTS_PER_PAGE);

        for (let i = 1; i <= totalPages; i++)
        {
            const li = document.createElement("li");
            const a = document.createElement("a");
            a.textContent = i;
            a.href = "#";

            if (i === selectedPage)
            {
                a.style.fontWeight = "bold";
                a.style.color = "#428cd2";
            }

            a.addEventListener("click", e =>
            {
                e.preventDefault();
                renderPage(i);
                renderPagination(i);
            });

            li.appendChild(a);
            pagination.appendChild(li);
        }
    }
});