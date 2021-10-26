let projectId = "";
let planId = "";
const ref_modal = document.getElementById('ref-case-modal');
ref_modal.addEventListener('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    projectId = button.closest('tr').find('#project').attr('value');
    planId = button.closest('tr').find("#id").attr('value');
    const pn = 1;
    build_case_table(projectId, planId, pn);

});

function build_case_table(projectId, planId, pn) {
    $.ajax({
            url: '/case/loadCaseByProject/',
            type: 'post',
            data: JSON.stringify({
                projectId: projectId,
                planId: planId,
                pn: pn
            }),
            dataType: 'JSON',
            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (arg) {
                loadCase(arg['data'].records);
                build_page_info(arg['data']);
            }
        }
    )
}

// 加载数据
function loadCase(res) {
    $('#refCaseTable > tbody').html("");
    const tbody = $('#refCaseTable').find('tbody');
    for (let i = 0; i < res.length; i++) {
        const id = res[i].id;
        const new_tr = $("<tr></tr>")
        const title_td = $("<td></td>")
        const desc_td = $("<td></td>")
        const pre_td = $("<td></td>")
        const step_td = $("<td></td>")
        const result_td = $("<td></td>")
        const title = res[i].name;
        const desc = res[i].description;
        const pre = res[i].precondition;
        const step = res[i].stepStore;
        const result = res[i].resultStore;
        title_td.text(title);
        desc_td.text(desc);
        pre_td.text(pre);
        step_td.text(step);
        result_td.text(result);
        const checkItemTd = $('<td><input type="checkbox" class="select-item checkbox" name="brand" id="brand" onclick="selectItem($(this))" value="' + id + '"/></td>');
        new_tr.prepend(checkItemTd);
        new_tr.append(title_td);
        new_tr.append(desc_td);
        new_tr.append(pre_td);
        new_tr.append(step_td);
        new_tr.append(result_td);
        tbody.append(new_tr);
    }
}

// 加载分页信息
function build_page_info(res) {
    console.log(res);
    const page_nav_area = $('#page_nav_area')
    page_nav_area.empty();
    const pages = res.pages;
    // 项目存在关联的测试用例时展示分页
    if (pages !== 0) {
        const ul = $("<ul></ul>").addClass("pagination");
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
    const _self = this;
    const length = end - start + 1;
    let step = start - 1;
    return Array.apply(null, {length: length}).map(function (v, index) {
        step++;
        return step;
    })
};
// 全选/全不选
$("input.select-all").click(function () {
    let temp_ids = [];
    const checked = $(this).prop("checked");
    if (Object.is(checked, true)) {
        //全选
        $("input.select-item").each(function (index, item) {
            $(item).prop("checked", checked);
            temp_ids.push($(item).val());
        });
    } else {
        //反选
        $("input.select-item").each(function (index, item) {
            $(item).prop("checked", checked);
        });
        temp_ids = []
    }
    console.log(temp_ids);
});

//选择单个数据
function selectItem(item) {
    let temp_ids = [];
    const checked = $(item).prop("checked");
    $(item).prop("checked", checked);
    if (checked) {
        console.log(checked);
        const val = $(item).val();
        temp_ids.push(val);
    } else {
        console.log(checked);
        const val = $(item).val();
        temp_ids.slice($.inArray(val, temp_ids), 1);
    }
    console.log(temp_ids);
}

//提交保存选择的测试用例数据
$('#sub-ref').click(function () {
    const items = document.getElementsByClassName("select-item");
    let res = [];
    for (let i = 0; i < items.length; i++) {
        if ($(items[i]).is(":checked")) {
            res.push($(items[i]).val());
        }
    }
    const params = {
        caseId: res
    };
    $.ajax({
        url: '/plan/addRefCase/',
        type: 'post',
        data: JSON.stringify(params),
        dataType: 'json',
        contentType: "application/json",
        success: function (arg) {
            if (Object.is(arg['code'], 200)) {
                iziToast.show({
                        // title: 'Success',
                        message: arg['msg'],
                        position: 'topRight',
                        timeout: 2000,
                        color: 'green'
                    }
                );
                // 数据添加成功后关闭弹框
                $("#ref-case-modal").modal('hide');
                location.reload();
            } else {
                iziToast.show({
                        // title: 'Success',
                        message: arg['msg'],
                        position: 'topRight',
                        timeout: 2000,
                        color: 'red'
                    }
                );
            }
        }
    })
});
