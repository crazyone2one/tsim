/*
* 加载分页信息
* @param res : 数据
* @param nav_selector : 定位信息
* **/
function build_page_info(res, nav_selector) {
    console.log(res);
    const page_nav_area = $(nav_selector)
    page_nav_area.html("");
    const pages = res.pages;
    // 项目存在关联的测试用例时展示分页
    if (pages !== 0) {
        const ul = $("<ul></ul>").addClass("pagination pagination-sm");
        // 上一页按钮
        const previous_page = $("<li></li>").addClass("page-item")
            .append($("<a></a>").addClass("page-link").attr("href", "#").attr("aria-label", "Previous")
                .append($("<span></span>").attr("aria-hidden", "true").append("&laquo;"))
            );

        ul.append(previous_page);
        // 每页按钮
        const page_range = arr.range(1, pages);
        for (let i = 0; i < page_range.length; i++) {
            const per_page = $("<li></li>").addClass("page-item")
                .append($("<a></a>").addClass("page-link").append(page_range[i]));
            if (res.current === page_range[i]) {
                per_page.addClass("active");
            }
            per_page.click(function () {
                build_case_table(projectId, planId, page_range[i]);
            });
            ul.append(per_page);
        }
        //下一页按钮
        const next_page = $("<li></li>").addClass("page-item")
            .append($("<a></a>").addClass("page-link").attr("href", "#").attr("aria-label", "Next")
                .append($("<span></span>").attr("aria-hidden", "true").append("&raquo;"))
            );
        // 只有一页数据（10条数据）时，设置上一页/下一页按钮不可点击
        if (pages === 1) {
            previous_page.addClass("disabled");
            next_page.addClass("disabled");
        } else {
            previous_page.click(function () {
                build_case_table(projectId, planId, res.current - 1)
            });
            next_page.click(function () {
                build_case_table(projectId, planId, res.current + 1)
            });
        }
        ul.append(next_page);
        page_nav_area.append(ul);
    }
}

const arr = [];
Array.prototype.range = function (start, end) {
    const length = end - start + 1;
    let step = start - 1;
    return Array.apply(null, {length: length}).map(function (v, index) {
        step++;
        return step;
    })
};
