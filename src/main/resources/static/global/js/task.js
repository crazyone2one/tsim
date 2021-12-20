autoComplete("/project/queryList", "p-search", 'p', false);
autoComplete("/project/queryList", "t-add", 'p', true);

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
    const pro_name = $("#s-p-id").val();
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
                    $("#table_refresh").load("/task/reloadTable");
                }
                showToast(result['code'], result['msg']);
            }
        });
    } else {
        !pro_name && $('#s-p-id').addClass("is-invalid");
        !desc && $('#add-description-name').addClass("is-invalid");
    }

}
