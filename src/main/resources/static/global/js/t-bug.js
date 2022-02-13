// 保存bug
$('#new_target_submit_bug').on('click', function () {
    const bug_info = $("#add-bug-from").serialize();
    if (Object.is($('#hidden-ref-id').val(), '')) {
        /**
         * 直接录入bug数据
         */
        $.ajax({
            url: "/bug/save",
            type: 'POST',
            data: bug_info,
            dataType: 'JSON',
            success: function (arg) {
                if (Object.is(arg['code'], 200)) {
                    resetModal("#add-bug-modal", "add-bug-from");
                    $('#add-bug-modal').modal('hide');
                    refresh_table()
                }
                showToast(arg['code'], arg['msg']);
            }
        });
    } else {
        /**
         * 测试计划关联测试用例：未通过测试用例录入bug数据
         */
        $.ajax({
                url: '/planCaseRef/addBugByFailCase/',
                type: 'post',
                data: JSON.stringify(bug_info),
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (result) {
                    showToast(result['code'], result['msg']);
                    resetModal("#add-bug-modal", "add-bug-from");
                    //数据更新完成后跳转到执行窗口并刷新
                    $('#refRunTable').bootstrapTable('refresh');
                }
            }
        )
    }
});

/**
 * 删除问题单。传id参数时，删除单条数据。不传id参数，可视为批量删除。
 * @param id 问题单id
 */
function batchDelete(id) {
    let replaceMsg = id ? '确认删除该数据吗' : "确认删除选择的数据吗";
    const selections = getSelections();
    // 批量删除时判断选择数据的个数
    // if (!id) {
    //     if (selections.length === 0) {
    //         showToast(300, "先选择需要删除的数据");
    //         return;
    //     }
    // }
    forwardToConfirmModal('confirm-modal', replaceMsg);
    $('#btn-confirm').on('click', function () {
        const ids = [];
        if (id) {
            ids.push(id);
        } else {
            for (let i = 0; i < selections.length; i++) {
                ids.push(selections[i].id);
            }
        }
        $.ajax({
            type: "post",
            url: "/bug/batchDelete",
            data: {ids: JSON.stringify(ids)},
            // contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function (result) {
                if (Object.is(200, result['code'])) {
                    // resetModal("#delModal", null);
                    // $("#table_refresh").load("/project/reloadTable");
                    $('#table').bootstrapTable('refresh');
                }
                showToast(result['code'], result['msg']);
            }
        });
        closeModal('confirm-modal');
    })
}

/**
 * 添加、编辑问题单数据
 * @param id 问题单数据id
 */
function add_or_update_bug_info(id) {
    // 编辑数据时不使用字段提示功能
    !id && autoComplete4Project(true, '#projectCode', '#b-a-p');
    // 查询项目关联的模块，编辑数据时不使用字段提示功能
    !id && $("#module")[0].addEventListener("focus", function () {
        autoComplete4Module(true, '#module', '#b-a-m', $("#b-a-p").attr("value"));
    });
    // 查询项目关联的需求，编辑数据时不使用字段提示功能
    !id && $("#add-bug-story")[0].addEventListener("focus", function () {
        autoComplete4Story(true, '#add-bug-story', '#bug-story-ref', $("#b-a-p").attr("value"));
    });
    document.getElementById('add-bug-modal').querySelector('.modal-title').textContent = id ? '编辑问题单' : '新增问题单';
    // 编辑数据时回显信息
    id && $.ajax({
        url: '/bug/detail/' + id,
        type: "get",
        success: function (result) {
            const bug_info = result['data'];
            $('#hidden-bug-id').val(bug_info.id);
            $('#projectCode').val(bug_info.project.projectName).attr('readonly', '');
            $('#b-a-p').val(bug_info.projectId);
            $('#module').val(bug_info.module.moduleName).attr('readonly', '');
            $('#b-a-m').val(bug_info.moduleId);
            $('#functionDesc').val(bug_info.func);
            $('#title').val(bug_info.title);
            $('#bug_description').val(bug_info.bugDescription);
            $('#reproduce_steps').val(bug_info.reproduceSteps);
            $('#expect_result').val(bug_info.expectResult);
            $('#actual_result').val(bug_info.actualResult);
            select_option_checked('severity', bug_info.severity);
            select_option_checked('owner', bug_info.tester);
            select_option_checked('status', bug_info.bugStatus);
            select_option_checked('bugRecurrenceProbability', bug_info.bugRecurrenceProbability);
            $('#remark').val(bug_info.note);
        }
    });
}
