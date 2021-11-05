let projectId = "";
let planId = "";
let work_date = "";
// 测试计划关联测试用例modal
const ref_modal = document.getElementById('ref-case-modal');
ref_modal.addEventListener('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    projectId = button.closest('tr').find('#project').attr('value');
    planId = button.closest('tr').find("#id").attr('value');
    work_date = button.closest('tr').find("#workDate").attr('value');
    const pn = 1;
    build_case_table(projectId, planId, work_date, pn);
});

// 测试计划关联测试用例执行modal
const ref_run_modal = document.getElementById('ref-run-modal');
ref_run_modal.addEventListener('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    // projectId = button.closest('tr').find('#project').attr('value');
    planId = button.closest('tr').find("#id").attr('value');
    const pn = 1;
    build_run_case_table(planId, pn);
});

function build_run_case_table(planId, pn) {
    $.ajax({
            url: '/planCaseRef/loadRefRecords/',
            type: 'post',
            data: JSON.stringify({
                planId: planId,
                pn: pn,
                pageSize: 10
            }),
            dataType: 'JSON',
            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (arg) {
                load_run_case_info(arg['data'].records);
                build_page_info(arg['data'], "#page-nav-area");
            }
        }
    )
}

function build_case_table(projectId, planId,work_date, pn) {
    // todo 不加载已关联的测试用例数据
    $.ajax({
            url: '/case/loadCaseByProject/',
            type: 'post',
            data: JSON.stringify({
                projectId: projectId,
                planId: planId,
                workDate: work_date,
                pn: pn
            }),
            dataType: 'JSON',
            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (arg) {
                loadCase(arg['data'].records);
                build_page_info(arg['data'], "#page_nav_area");
            }
        }
    )
}

// 执行测试用例modal加载数据
function load_run_case_info(res) {
    $('#refRunTable > tbody').html("");
    const tbody = $('#refRunTable').find('tbody');
    for (let i = 0; i < res.length; i++) {
        const id = res[i].id;
        const new_tr = $("<tr></tr>").addClass("text-center")
        //测试用例标题
        const title_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px");
        // 加上tooltips
        const title = res[i].testCase.name;
        title_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", title);
        title_td.text(title);
        //测试用例描述
        const desc_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px");
        const desc = res[i].testCase.description;
        desc_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", desc);
        desc_td.text(desc);
        //测试结果 todo 设置为可编辑
        const case_result = $("<td></td>")
        const result_context = res[i].runResult;
        case_result.text(result_context);
        // bug数据
        const case_bug = $("<td></td>");
        if (Object.is(res[i].runStatus, 0)) {
            const case_bug_a = $("<a type='button' class='btn'></a>");
            const case_bug_a_i = $("<i class='bi bi-bug hvr-grow' style='color: #cca1a1'></i>");
            case_bug_a.append(case_bug_a_i);
            case_bug.append(case_bug_a);
        } else {
            // 点击后弹出添加bug弹框
            // todo 当测试用例已添加相关的bug后，设置按钮不可用（不能继续添加bug数据）
            const case_bug_a = $("<a type='button' class='btn' data-bs-toggle='modal' data-bs-target='#add-bug-modal' onclick='addBug(" + JSON.stringify(res[i]) + ")'></a>");
            const case_bug_a_i = $("<i class='bi bi-bug hvr-grow' style='color: red'></i>");
            case_bug_a.append(case_bug_a_i);
            case_bug.append(case_bug_a);
        }
        // checkbox
        const checkItemTd = $('<td><input type="checkbox" class="select-item checkbox" name="brand" id="brand" onclick="selectItem($(this))" value="' + id + '"/></td>');
        new_tr.prepend(checkItemTd);
        new_tr.append(title_td);
        new_tr.append(desc_td);
        new_tr.append(case_result);
        new_tr.append(case_bug);
        tbody.append(new_tr);
    }
}

// 加载数据
function loadCase(res) {
    $('#refCaseTable > tbody').html("");
    const tbody = $('#refCaseTable').find('tbody');
    for (let i = 0; i < res.length; i++) {
        const id = res[i].id;
        const new_tr = $("<tr></tr>").addClass("text-center")
        const title_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px");
        const desc_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px");
        // const pre_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 90px");
        const step_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 160px");
        const result_td = $("<td></td>").attr("style", "overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 160px");
        const title = res[i].name;
        // 加上tooltips
        title_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", title);
        const desc = res[i].description;
        desc_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", desc);
        // const pre = res[i].precondition;
        const step = res[i].stepStore;
        step_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", step);
        const result = res[i].resultStore;
        result_td.attr("data-bs-toggle0", "tooltip").attr("data-bs-placement", "top").attr("title", result);
        title_td.text(title);
        desc_td.text(desc);
        // pre_td.text(pre);
        step_td.text(step);
        result_td.text(result);
        let checkItemTd = $('<td><input type="checkbox" class="select-item checkbox" name="brand" id="brand" onclick="selectItem($(this))" value="' + id + '"/></td>');
        if (res[i].refFlag) {
            checkItemTd = $('<td><input type="checkbox" class="select-item checkbox" name="brand" id="brand" disabled onclick="selectItem($(this))" value="' + id + '"/></td>');
        }
        new_tr.prepend(checkItemTd);
        new_tr.append(title_td);
        new_tr.append(desc_td);
        // new_tr.append(pre_td);
        new_tr.append(step_td);
        new_tr.append(result_td);
        tbody.append(new_tr);
    }
}

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
});

//选择单个数据
function selectItem(item) {
    let temp_ids = [];
    const checked = $(item).prop("checked");
    $(item).prop("checked", checked);
    if (checked) {
        const val = $(item).val();
        temp_ids.push(val);
    } else {
        const val = $(item).val();
        temp_ids.slice($.inArray(val, temp_ids), 1);
    }
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

function addBug(arg) {
    //项目名称赋值并不可更改
    $('#projectCode').val(arg.testCase.project.projectName).attr("readonly", "readonly");
    $('#functionDesc').val(arg.testCase.description);
    $('#remark').text(arg.testCase.description);
    load_user_option("#owner");
    $('#submitBug').attr("onclick", 'saveBug(0,' + JSON.stringify(arg) + ')');
}

/*
* 加载用户信息
* @param : selector 元素选择
* */
function load_user_option(selector) {
    $.get("/loadUser", function (result) {
        $.each(result.data, function (i, value) {
            $(selector).append($("<option value='" + value.id + "'>" + value.username + "</option>"))
        });
    })
}
