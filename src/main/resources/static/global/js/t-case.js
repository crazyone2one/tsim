/*增加测试步骤*/
function addCaseStep() {
    const caseStepArea = document.getElementById("case-step-area");
    let div_index = caseStepArea.getElementsByClassName("row").length;
    div_index++;
    $(caseStepArea).append('<div class="row rounded" id="row_' + div_index + '">\n' +
        '                                            <div style="width: 47%;">\n' +
        '                                                <label for="caseSteps_' + div_index + '" class="form-label">测试步骤</label>\n' +
        '                                                <input name="caseSteps[]" id="caseSteps_' + div_index + '" type="text"\n' +
        '                                                          class="form-control line_under_input">\n' +
        '                                            </div>\n' +
        '                                            <div style="width: 47%;">\n' +
        '                                                <label for="caseExpectedResults_' + div_index + '" class="form-label">预期结果</label>\n' +
        '                                                <input name="caseExpectedResults[]" id="caseExpectedResults_' + div_index + '"\n' +
        '                                                          type="text"\n' +
        '                                                          class="form-control line_under_input">\n' +
        '                                            </div>\n' +
        '                                            <div class="position-relative" style="width: 6%;">\n' +
        '                                                <a class="position-absolute top-50 start-50" title="移除">\n' +
        '                                                    <i class="bi bi-backspace" style="color: #bb2d3b" id="del"\n' +
        '                                                       onclick="removeCaseStep(this)" ></i>\n' +
        '                                                </a>\n' +
        '                                            </div>\n' +
        '                                        </div>')
}

const addEditStep = () => {
    const caseStepArea = document.getElementById('edit-case-step-table');
    let div_index = caseStepArea.getElementsByClassName("div").length;
    // div_index++;
    $(caseStepArea).append('<div class="row" id="row' + div_index + '">' +
        '<input class="line_under_input" style="width: 47%;" id="step' + div_index + '">' +
        '<input class="line_under_input" style="width: 47%;" id="result' + div_index + '">' +
        '<a style="width: 6%;" title="移除本条"><i class="bi bi-backspace" style="color: #bb2d3b" id="remove' + div_index + '"></i></a>' +
        '</div>');
}

/*删除测试步骤*/
function removeCaseStep(obj) {
    const caseStepArea = document.getElementById("case-step-area");
    const x = caseStepArea.getElementsByClassName("row");
    if (x.length > 1) {
        $(obj).parent("a").parent("div").parent("div").remove();
    } else {
        alert("至少保留一条", 'warning')
    }
}

function saveEditCaseInfo() {
    const p_div = document.getElementById("edit-case-step-table");
    const s_div = p_div.getElementsByClassName("row");
    const temp_steps = [];
    for (let i = 0; i < s_div.length; i++) {
        const temp_json = {};
        temp_json['t_s'] = s_div[i].getElementsByTagName("input")[0].value;
        temp_json['t_r'] = s_div[i].getElementsByTagName("input")[1].value;
        const result = {};
        result[i] = temp_json;
        temp_steps.push(result);
    }
    const case_step = document.getElementById("editCaseSteps");
    case_step.setAttribute("value", JSON.stringify(temp_steps));
    !$('#mode-manual-e').prop('checked') && $('#mode-manual-e').val();
    !$('#mode-auto-e').prop('checked') && $('#mode-auto-e').val();
    const data = $("#edit-case-form").serialize();
    $.ajax({
        url: "/case/save",
        type: 'POST',
        data: data,
        // contentType: "application/json;charset=UTF-8",
        dataType: 'JSON',
        success: function (arg) {
            if (Object.is(arg['code'], 200)) {
                resetModal("case-edit-modal", "edit-case-form");
                $('#case-edit-modal').modal('hide');
                refresh_table();
            }
            showToast(arg['code'], arg['msg']);
        }
    });
}

/*保存测试用例*/
function saveCaseInfo() {
    const p_div = document.getElementById("case-step-area");
    const s_div = p_div.getElementsByClassName("row");
    const temp_steps = [];
    for (let i = 0; i < s_div.length; i++) {
        const temp_json = {};
        temp_json['t_s'] = s_div[i].getElementsByTagName("input")[0].value;
        temp_json['t_r'] = s_div[i].getElementsByTagName("input")[1].value;
        if (!temp_json['t_s'] && !temp_json['t_r']) {
            $('#toast-div').addClass("is-invalid");
            return;
        } else {
            removeClass('#toast-div', 'is-invalid');
        }
        const result = {};
        result[i] = temp_json;
        temp_steps.push(result);
    }
    const case_step = document.getElementById("caseSteps");
    case_step.setAttribute("value", JSON.stringify(temp_steps));
    !$('#mode-manual').prop('checked') && $('#mode-manual').val();
    !$('#mode-auto').prop('checked') && $('#mode-auto').val();
    const data = $("#add-case-from").serialize();
    validateCaseInfo(data);
}

function loadStepAndResult(arg) {
    $('#step-result > tbody').html("");
    const tbody = $('#step-result').find('tbody');
    const stepResults = arg['data'].caseSteps;
    for (let i = 0; i < stepResults.length; i++) {
        const _temp = stepResults[i];
        const new_tr = $("<tr></tr>")
        const _id = _temp.id;
        const hiddenInput = $("<input name='id' id='id' type='hidden' value='" + _id + "'>");
        new_tr.append(hiddenInput);
        const _order = _temp.caseOrder;
        const order = $("<input name='caseOrder' id='caseOrder' type='hidden' value='" + _order + "'>");
        new_tr.append(order);
        const step_td = $("<td></td>")
        const _step = _temp.caseStep;
        step_td.text(_step);
        new_tr.append(step_td);
        const res_td = $("<td></td>")
        const _result = _temp.caseStepResult;
        res_td.text(_result)
        new_tr.append(res_td);
        tbody.append(new_tr);
    }
}

/**
 * 测试用例删除、置为无效
 * @param flag disable--置为无效，del--删除
 *
 */
function updateCase(flag) {
    // let toast_msg = Object.is('disable', flag) ? "选择置为无效的测试用例数据" : "选择待删除的测试用例数据";
    let replace_msg = Object.is('disable', flag) ? "确认将选择的测试用例数据置为无效吗" : "确认删除选择的测试用例数据吗";
    let _url = Object.is('disable', flag) ? "/case/disable" : "/case/delete";
    const selections = getSelections();
    // if (selections.length === 0) {
    //     showToast(300, toast_msg);
    //     return;
    // }
    // 处理选择已被置为无效状态的测试用例数据
    if (Object.is('disable', flag)) {
        for (let i = 0; i < selections.length; i++) {
            if (Object.is(1, selections[i].active)) {
                showToast(300, "确认是否选择无效状态的测试用例");
                return;
            }
        }
    }
    forwardToConfirmModal('confirm-modal', replace_msg);
    // 删除时更改图标颜色
    if (Object.is('del', flag)) {
        removeClass('modal-i-label', 'text-warning');
        $('#modal-i-label').addClass('text-danger');
        removeClass('btn-confirm', 'btn-warning');
        $('#btn-confirm').addClass('btn-danger');
    }

    $('#btn-confirm').on('click', function () {
        const ids = [];
        for (let i = 0; i < selections.length; i++) {
            ids.push(selections[i].id);
        }
        $.ajax({
            type: "post",
            url: _url,
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
        })
        closeModal('confirm-modal');
    });
}

/**
 * 测试用例数据非空验证：项目、模块、测试用例标题
 * @param case_info_data
 */
function validateCaseInfo(case_info_data) {
    const _projectId = '#projectId';
    const _moduleId = '#moduleId';
    const _caseTitle = '#caseTitle';
    const tempProjectName = $(_projectId).val();
    const tempModuleName = $(_moduleId).val();
    const tempHiddenProjectId = $('#c-a-p').val();
    const tempCaseTitle = $(_caseTitle).val();
    const plan_name = $('#ref-plan').val();
    const hidden_plan_id = $('#add-case-ref-plan').val();

    if (plan_name) {
        if (!hidden_plan_id) {
            $('#ref-plan').addClass("is-invalid");
            $('#plan-error-tips').text(plan_name + '不存在,先添加测试计划');
        } else {
            removeClass('#ref-plan', 'is-invalid');
            $.ajax({
                url: '/plan/getPlan',
                type: 'get',
                data: {name: tempProjectName, id: hidden_plan_id},
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (result) {
                    if (Object.is(403, result['code'])) {
                        $('#ref-plan').addClass("is-invalid");
                        $('#plan-error-tips').text(plan_name + '不存在,先添加测试计划');
                    } else {
                        if (!Object.is(result['data'].name, plan_name)) {
                            $('#ref-plan').addClass("is-invalid");
                            $('#plan-error-tips').text(plan_name + '不存在,先添加测试计划');
                        }
                    }
                },
            });
        }
    }

    // 所属模块名称非空验证
    tempModuleName ? removeClass(_moduleId, 'is-invalid') : $(_moduleId).addClass("is-invalid");
    // 测试用例标题为空验证
    tempCaseTitle ? removeClass(_caseTitle, 'is-invalid') : $(_caseTitle).addClass("is-invalid");
    if (!tempProjectName) {
        $(_projectId).addClass("is-invalid");
    } else {
        removeClass(_projectId, 'is-invalid');
        if (!tempHiddenProjectId) {
            $('#project-error-tips').text(tempProjectName + '不存在,先在项目管理模块添加');
            $(_projectId).addClass("is-invalid");
        } else {
            removeClass('_projectId', 'is-invalid');
            $.ajax({
                url: '/project/checkUniqueProject',
                type: 'get',
                data: {name: tempProjectName, id: tempHiddenProjectId},
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (result) {
                    if (Object.is(403, result['code'])) {
                        $('#project-error-tips').text('[' + tempProjectName + ']不存在,先在项目管理模块添加');
                        $(_projectId).addClass("is-invalid");
                    } else {
                        $.ajax({
                            url: "/case/save",
                            type: 'POST',
                            data: case_info_data,
                            // contentType: "application/json;charset=UTF-8",
                            dataType: 'JSON',
                            success: function (arg) {
                                if (Object.is(arg['code'], 200)) {
                                    resetModal("#add-case-modal", "add-case-from");
                                    $('#add-case-modal').modal('hide');
                                    refresh_table();
                                }
                                showToast(arg['code'], arg['msg']);
                            }
                        });
                    }
                }
            })
        }
    }
}

// 导入测试用例js
$('#case-upload-modal').on('show.bs.modal', function (event) {
    autoComplete("/project/queryList", "import-project", 'p', true);
    // myModal.off('shown.bs.modal');//去除绑定
    $("input[type=hidden][id='hidden-import-project']").val('');
    $("input[type=hidden][id='add-case-ref-plan']").val('');
});
$("#import-ref-plan")[0].addEventListener("focus", function () {
    $('#hidden-import-project').attr('value') && autoComplete("/plan/getPlans/" + $('#hidden-import-project').attr('value'), "import-ref-plan", 'plan', true);
});
