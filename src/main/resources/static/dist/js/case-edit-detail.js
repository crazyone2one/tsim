const detail_modal = document.getElementById('case-detail-modal');
detail_modal.addEventListener('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const flag = button.data("whatever");
    const case_id = button.closest('tr').find("#id").attr('value');
    $.ajax({
            url: '/case/queryCase/',
            type: 'post',
            data: JSON.stringify({
                id: case_id,
            }),
            dataType: 'JSON',
            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (arg) {
                $("#projectCode-d").val(arg['data'].projectId);
                $("#moduleId-d").val(arg['data'].moduleId);
                $("#caseTitle-d").val(arg['data'].name);
                $("#description-d").val(arg['data'].description);
                $("#precondition-d").val(arg['data'].precondition);
                let _priority = "";
                switch (arg['data'].priority) {
                    case 0:
                        _priority = "低";
                        break
                    case 1:
                        _priority = "中";
                        break
                    case 2:
                        _priority = "高";
                        break
                }
                $("#priority-d").val(_priority);
                let _testMode = "";
                switch (arg['data'].testMode) {
                    case 0:
                        _testMode = "手动";
                        break
                    case 1:
                        _testMode = "自动";
                        break
                }
                $("#testMode-d").val(_testMode);
                // 填充测试步骤
                loadStepAndResult(arg);
            }
        }
    )
});

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
