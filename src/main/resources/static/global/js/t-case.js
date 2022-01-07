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

/*提交测试用例*/
function saveCaseInfo() {
    const p_div = document.getElementById("case-step-area");
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
    const case_step = document.getElementById("caseSteps");
    case_step.setAttribute("value", JSON.stringify(temp_steps));
    const data = $("#add-case-from").serialize();
    $.ajax({
        url: "/case/save",
        type: 'POST',
        data: data,
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
 * 将测试用例置为无效
 */
function disableCase() {
    const selections = getSelections();
    if (selections.length === 0) {
        showToast(300, "选择置为无效的测试用例数据");
        return;
    }
    forwardToConfirmModal('confirm-modal', '确认将选择的测试用例数据置为无效吗');
    $('#btn-confirm').on('click', function () {
        const ids = [];
        for (let i = 0; i < selections.length; i++) {
            ids.push(selections[i].id);
        }
        $.ajax({
            type: "post",
            url: "/case/disable",
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
    })
}