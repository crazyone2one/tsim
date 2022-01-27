'use strict'
// autoComplete("/project/queryList", "p-search", 'p', false);

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
    $("#task-desc-edit").val(taskInfo.summaryDesc);
    $("#finish-status").attr("data-id", taskInfo.finishStatus);
    select_option_checked('finish-status', taskInfo.finishStatus);
    select_option_checked('jiaofu-status', taskInfo.deliveryStatus);
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
$("#update_target_submit").on('click', function () {
    $.ajax({
        url: "/task/editTask",
        type: "post",
        data: {
            "id": $("#task-id").val(),
            "taskDesc": $("#task-desc").val(),
            "finishStatus": $("#finish-status").val(),
            "deliveryStatus": $("#jiaofu-status").val(),
            "remark": $('#remark').val()
        },
        // dataType: 'JSON',
        contentType: 'application/x-www-form-urlencoded',
        sync: false,
        success: function (result) {
            if (Object.is(200, result['code'])) {
                closeModal('editSummaryModal');
                resetModal("#editSummaryModal", null);
                $('#table').bootstrapTable('refresh');

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
    desc ? removeClass('#add-description-name', "is-invalid") : $('#add-description-name').addClass("is-invalid");
    if (!pro_name) {
        $('#t-add').addClass("is-invalid");
    } else {
        removeClass('#s-p-id', "is-invalid");
        if (!projectId) {
            $('#project-error-tips').text(pro_name + '不存在,先在项目管理模块添加');
            $('#t-add').addClass("is-invalid");
            return false;
        } else {
            removeClass('#s-p-id', "is-invalid");
            $.ajax({
                url: '/project/checkUniqueProject',
                type: 'get',
                data: {name: pro_name, id: projectId},
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (res) {
                    if (Object.is(403, res['code'])) {
                        $('#project-error-tips').text('[' + pro_name + ']不存在,先在项目管理模块添加');
                        $('#t-add').addClass("is-invalid");
                        return false;
                    } else {
                        removeClass('#s-p-id', "is-invalid");
                        removeClass('#add-description-name', "is-invalid");
                        $.ajax({
                            url: "/task/addTask",
                            type: "post",
                            data: {"pro": projectId, "desc": desc, "workDate": $("#workDate").val()},
                            dataType: 'JSON',
                            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                            // sync: false,
                            success: function (result) {
                                if (Object.is(200, result['code'])) {
                                    resetModal("#add-task-modal", "addTaskForm");
                                    closeModal('add-task-modal');
                                    $('#table').bootstrapTable('refresh');
                                }
                                showToast(result['code'], result['msg']);
                            }
                        });
                    }
                }
            });
        }
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

function taskDetail(index, row) {
    console.log(row);
}