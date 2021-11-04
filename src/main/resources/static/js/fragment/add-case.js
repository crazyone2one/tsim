/*增加测试步骤*/
function addCaseStep() {
    const caseStepArea = document.getElementById("case-step-area");
    let div_index = caseStepArea.getElementsByClassName("row").length;
    div_index++;
    $(caseStepArea).append('<div class="row rounded" id="row_' + div_index + '">\n' +
        '                                            <div class="col-md-5">\n' +
        '                                                <label for="caseSteps_' + div_index + '" class="form-label">测试步骤</label>\n' +
        '                                                <textarea name="caseSteps[]" id="caseSteps_' + div_index + '" type="text"\n' +
        '                                                          class="form-control"\n' +
        '                                                          rows="2"></textarea>\n' +
        '                                            </div>\n' +
        '                                            <div class="col-md-5">\n' +
        '                                                <label for="caseExpectedResults_' + div_index + '" class="form-label">预期结果</label>\n' +
        '                                                <textarea name="caseExpectedResults[]" id="caseExpectedResults_' + div_index + '"\n' +
        '                                                          type="text"\n' +
        '                                                          class="form-control"></textarea>\n' +
        '                                            </div>\n' +
        '                                            <div class="position-relative col-md-1">\n' +
        '                                                <a class="position-absolute top-50 start-50">\n' +
        '                                                    <i class="bi bi-backspace" style="color: #bb2d3b" id="del"\n' +
        '                                                       onclick="removeCaseStep(this)"></i>\n' +
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
        alter("至少保留一条", 'warning')
    }
}

/*提交测试用例*/
function sub() {
    const p_div = document.getElementById("case-step-area");
    const s_div = p_div.getElementsByClassName("row");
    const temp_steps = [];
    const temp_results = [];
    for (let i = 0; i < s_div.length; i++) {
        temp_steps.push(s_div[i].getElementsByTagName("textarea")[0].value);
        temp_results.push(s_div[i].getElementsByTagName("textarea")[1].value);
    }
    const case_step = document.getElementById("caseSteps");
    case_step.setAttribute("value", temp_steps.toString());
    const caseExp = document.getElementById("caseExpectedResults");
    caseExp.setAttribute("value", temp_results.toString());
    const data = $("#add-case-from").serialize();
    console.log(data);
    $.ajax({
        url: "/case/save",
        type: 'POST',
        data: data,
        // contentType: "application/json;charset=UTF-8",
        dataType: 'JSON',
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
    });
}
