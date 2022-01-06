/*增加测试步骤*/
function addCaseStep() {
    const caseStepArea = document.getElementById("case-step-area");
    let div_index = caseStepArea.getElementsByClassName("row").length;
    div_index++;
    $(caseStepArea).append('<div class="row rounded" id="row_' + div_index + '">\n' +
        '                                            <div style="width: 47%;">\n' +
        '                                                <label for="caseSteps_' + div_index + '" class="form-label">测试步骤</label>\n' +
        '                                                <input name="caseSteps[]" id="caseSteps_' + div_index + '" type="text"\n' +
        '                                                          class="form-control">\n' +
        '                                            </div>\n' +
        '                                            <div style="width: 47%;">\n' +
        '                                                <label for="caseExpectedResults_' + div_index + '" class="form-label">预期结果</label>\n' +
        '                                                <input name="caseExpectedResults[]" id="caseExpectedResults_' + div_index + '"\n' +
        '                                                          type="text"\n' +
        '                                                          class="form-control">\n' +
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
        temp_json['t_s'] = s_div[i].getElementsByTagName("textarea")[0].value;
        temp_json['t_r'] = s_div[i].getElementsByTagName("textarea")[1].value;
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
                refresh_table();
            }
            showToast(arg['code'], arg['msg']);
        }
    });
}
