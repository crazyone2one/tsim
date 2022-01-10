// 保存bug
function saveBug() {
    const bug_info = $("#add-bug-from").serialize();
    // bug模块直接添加bug数据
    if (Object.is(arguments[0], 1)) {
        $.ajax({
            url: "/bug/save",
            type: 'POST',
            data: bug_info,
            dataType: 'JSON',
            success: function (arg) {
                if (Object.is(arg['code'], 200)) {
                    resetModal("#add-bug-modal", "add-bug-from");
                    refresh_table()
                }
                showToast(arg['code'], arg['msg']);
            }
        });
    } else {
        // 测试计划关联测试用例模块中录入bug信息
        // console.log(JSON.parse(bug_info));
        $.ajax({
                url: '/planCaseRef/saveRefInfo/',
                type: 'post',
                data: JSON.stringify({
                    bug_info: serializeObject($("#add-bug-from")),
                    plan_case: arguments[1]
                }),
                dataType: 'JSON',
                // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (result) {
                    alert(arg['msg'], 'success')
                }
            }
        )
    }

    /*form表单转成*/
    function serializeObject() {
        const o = {};
        const a = arguments[0].serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || "");
            } else {
                o[this.name] = this.value || "";
            }
        });
        return o;
    }
}

/**
 * 删除问题单。传id参数时，删除单条数据。不传id参数，可视为批量删除。
 * @param id 问题单id
 */
function batchDelete(id) {
    let replaceMsg = id ? '确认删除该数据吗' : "确认删除选择的数据吗";
    const selections = getSelections();
    // 批量删除时判断选择数据的个数
    if (!id) {
        if (selections.length === 0) {
            showToast(300, "先选择需要删除的数据");
            return;
        }
    }
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
