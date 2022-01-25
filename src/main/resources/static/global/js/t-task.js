autoComplete("/project/queryList", "p-search", 'p', false);

$('#add-task-modal').on('shown.bs.modal', function () {
    autoComplete("/project/queryList", "t-add", 'p', true);
    removeClass('t-add', 'is-invalid');
})

/**
 * 编辑任务
 * @param id 任务id
 */
function editTask(id) {
    $.ajax({
        url: '/task/getTask/' + id,
        type: 'get',
        dataType: 'JSON',
        success: function (res) {
            console.log(res);
            if (Object.is(200, res['code'])) {
                fillContent(res['data']);
            } else {
                alert("数据查询失败", 'danger')
            }
        }
    })
}

/**
 * 填充
 * @param taskInfo
 */
function fillContent(taskInfo) {
    console.log(taskInfo);
    $("#task-id").attr("value", taskInfo.id);
    $("#generateReport").attr("data-id", taskInfo.id);
    $("#workDate").attr("data-id", taskInfo.issueDate);
    $("#projectName").attr("value", taskInfo.projectId);
    $("#projectName").attr("data-id", taskInfo.projectId);
    $("#projectDesc").attr("value", taskInfo.summaryDesc);
    $("#finish-status").attr("data-id", taskInfo.finishStatus);
    $("#new-case").attr("value", taskInfo.createCaseCount);
    $("#ex-case").attr("value", taskInfo.executeCaseCount);
    const $story = $("#story-1");
    $story.attr("data-id", taskInfo.storyId);

    //提交bug
    $("#new-level4").attr("value", taskInfo.subBug.level4);
    $("#new-level3").attr("value", taskInfo.subBug.level3);
    $("#new-level2").attr("value", taskInfo.subBug.level2);
    $("#new-level1").attr("value", taskInfo.subBug.level1);
    $("#new-sum").attr("value", taskInfo.subBug.total);
    //    回测bug
    $("#ex-level4").attr("value", taskInfo.fixBug.level4);
    $("#ex-level3").attr("value", taskInfo.fixBug.level3);
    $("#ex-level2").attr("value", taskInfo.fixBug.level2);
    $("#ex-level1").attr("value", taskInfo.fixBug.level1);
    $("#ex-count").attr("value", taskInfo.fixBug.total);
    //需求文件
    if ('reqDoc' in taskInfo) {
        $story.text(taskInfo.reqDoc);
        $story.attr("value", taskInfo.reqDoc);
    } else {
        $story.text("暂无关联需求");
        //无关联需求时设置可编辑
        $story.attr("value", "").removeAttr("readonly");
    }
    //    测试报告
    const $report = $("#report-doc");
    if ('reportDoc' in taskInfo) {
        $report.text(taskInfo.reportDoc);
        $report.attr("value", taskInfo.reportDoc);
    } else {
        $report.text("暂未生成测试报告");
        //无关联需求时设置可编辑
        $report.attr("value", "").removeAttr("readonly");
    }

}

/**
 * 更新任务数据
 */
$("#update-task").on('click', function () {
    $.ajax({
        url: "/task/editTask",
        type: "post",
        data: {
            "id": $("#task-id").val(),
            "finishStatus": $("#finish-status").val(),
            "deliveryStatus": $("#jiaofu-status").val(),
            "remark": $('#remark').val()
        },
        dataType: 'JSON',
        // contentType: "application/json; charset=utf-8",
        sync: false,
        success: function (result) {
            if (Object.is(200, result['code'])) {
                resetModal("#editSummaryModal", null);
                $("#table_refresh").load("/task/reloadTable");
            }
            showToast(result['code'], result['msg']);
        }
    })
})

// $("#generateReport").on('click', function () {
//     $.ajax({
//         type: 'get',
//         url: "/task/checkReport/" + $("#generateReport").data("id"),
//         success: function (result) {
//             switch (result['code']) {
//                 /*测试报告不存在时创建测试报告*/
//                 case 200:
//                     $.ajax({
//                         type: 'get',
//                         url: "/project/generateReport/" + $("#projectName").data("id") + "/" + $("#story-1").data("id") + "/" + $("#workDate").data("id"),
//                     })
//                     break;
//                 case 400:
//                     console.log("11");
//                     break;
//             }
//         }
//     });
// })

/**
 * 保存任务数据
 */
function saveTask() {
    const pro_name = $("#t-add").val();
    const projectId = $("#s-p-id").val();
    const desc = $("#add-description-name").val();
    if (pro_name && desc) {
        removeClass('#s-p-id', "is-invalid");
        removeClass('#add-description-name', "is-invalid");
        $.ajax({
            url: "/task/addTask",
            type: "post",
            data: {"pro": pro_name, "desc": desc, "workDate": $("#workDate").val()},
            dataType: 'JSON',
            // contentType: "application/json; charset=utf-8",
            // sync: false,
            success: function (result) {
                if (Object.is(200, result['code'])) {
                    resetModal("#add-task-modal", "addTaskForm");
                    $('#add-task-modal').modal('hide');
                    $("#table_refresh").load("/task/reloadTable");
                }
                showToast(result['code'], result['msg']);
            }
        });
    } else {
        !pro_name && $('#t-add').addClass("is-invalid");
        !desc && $('#add-description-name').addClass("is-invalid");
    }
}

const _finishStatus = {
    '0': '已完成',
    '1': '进行中',
    '2': '待回测',
    '3': '已回测',
};
const _deliveryStatus = {
    '0': '已交付',
    '1': '未交付',
    '2': '不确定',
};
// 列表数据
const _table_columns = [
    [{field: 'id', align: 'center', title: 'id', rowspan: 2, colspan: 1, visible: false},
        {align: 'center', title: '所属项目', field: 'projectId', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '任务描述', field: 'summaryDesc', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '关联需求', field: '', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '完成状态', field: 'finishStatus', rowspan: 2, colspan: 1, valign: 'middle',
            formatter: function (value, row, index) {
                return _finishStatus[value];
            }},
        {align: 'center', title: '交付状态', field: 'deliveryStatus', rowspan: 2, colspan: 1, valign: 'middle',
            formatter:function (value, row, index) {
                return _deliveryStatus[value];
            }},
        {align: 'center', title: '用例数量', field: '', rowspan: 1, colspan: 2, valign: 'middle',class:'col-md-1'},
        {align: 'center', title: '提交bug', field: '', rowspan: 1, colspan: 5, valign: 'middle',class:'col-md-2'},
        {align: 'center', title: '回测bug', field: '', rowspan: 1, colspan: 5, valign: 'middle',class:'col-md-2'},
        {align: 'center', title: '测试文档', field: '', rowspan: 1, colspan: 2, valign: 'middle',class:'col-md-1'},
        {align: 'center', title: '任务时间', field: 'issueDate', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '测试人员', field: 'tester', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '备注', field: 'remark', rowspan: 2, colspan: 1, valign: 'middle'},
        {align: 'center', title: '操作', rowspan: 2, colspan: 1,valign: 'middle', formatter: option}
    ],
    [   // 用例数量
        {title: "新建", field: 'createCaseCount', align: 'center'},
        {title: "执行", field: 'executeCaseCount', align: 'center'},
        // 提交bug
        {title: "致命", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: red">' + row.subBug['level4'] + '</div>';
            }},
        {title: "严重", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: orange">' + row.subBug['level3'] + '</div>';
            }},
        {title: "一般", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: yellow">' + row.subBug['level2'] + '</div>';
            }},
        {title: "轻微", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: blue">' + row.subBug['level1'] + '</div>';
            }},
        {title: "总计", field: '', align: 'center',formatter:function (value, row, index) {
                return row.subBug['level4']+row.subBug['level3']+row.subBug['level2']+row.subBug['level1']
            }},
        // 回测bug
        {title: "致命", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: red">' + row.fixBug['level4'] + '</div>';
            }},
        {title: "严重", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: orange">' + row.fixBug['level3'] + '</div>';
            }},
        {title: "一般", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: yellow">' + row.fixBug['level2'] + '</div>';
            }},
        {title: "轻微", field: '', align: 'center',formatter:function (value, row, index) {
                return '<div style="color: blue">' + row.fixBug['level1'] + '</div>';
            }},
        {title: "总计", field: '', align: 'center',formatter:function (value, row, index) {
                return row.fixBug['level4']+row.fixBug['level3']+row.fixBug['level2']+row.fixBug['level1']
            }},
        {title: "bug文档", field: '', align: 'center'}, {title: "测试报告", field: '', align: 'center'},
    ],
]
// 查询条件
const _search_params = function (params) {
    return {
        projectName: $('#p-search').val().trim(), // 自定义查询条件
        status: $('#finishStatus').val(), // 自定义查询条件
        taskDate: $('#workDate-search').val(), // 自定义查询条件
        pageSize: params.limit,
        pageNum: params.offset / params.limit + 1,

    }
};
InitTable("/task/reloadTable", 'get', _table_columns, _search_params);

function option(value, row, index) {
    return '<button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editSummaryModal" onclick="editTask(\'' + row.id + '\')"><i class="bi-pencil-square"></i></button>' ;
}

function taskDetail(index, row) {
    console.log(row);
}
