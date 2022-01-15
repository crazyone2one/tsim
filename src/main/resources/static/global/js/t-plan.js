/**
 * 执行测试计划关联的测试用例数据时加载测试用例数据
 * @param id 测试计划数据id
 */
function loadRunCaseInfo(id) {
    $('#ref-run-modal').on('shown.bs.modal', function () {
        $('#hidden-plan-id').val(id);
        const url = "/planCaseRef/loadRefRecords/" + id;

        // 测试用例步骤
        function refLinkFormatter(value, row, index) {
            const stepJsonArray = JSON.parse(row.testCase.stepStore);
            let step_result = '';
            for (let i = 0; i < stepJsonArray.length; i++) {
                const temp_step_json = JSON.parse(JSON.stringify(stepJsonArray[i]));
                const tempStepJsonElement = JSON.stringify(temp_step_json[i]);
                const map1 = eval("(" + tempStepJsonElement + ")");
                step_result = step_result + map1.t_s + "\r\n";
            }
            if (step_result) {
                let html = '';
                let tempSteps = step_result.split('\r\n');
                for (let i = 0; i < tempSteps.length; i++) {
                    html += '<div style="text-align: left">' + tempSteps[i] + '</div>';
                }
                return html;
            }
        }

        // 测试用例预期结果
        function refCaseResultFormatter(value, row, index) {
            const stepJsonArray = JSON.parse(row.testCase.stepStore);
            let step_result = '';
            for (let i = 0; i < stepJsonArray.length; i++) {
                const temp_step_json = JSON.parse(JSON.stringify(stepJsonArray[i]));
                const tempStepJsonElement = JSON.stringify(temp_step_json[i]);
                const map1 = eval("(" + tempStepJsonElement + ")");
                step_result = step_result + map1.t_r + "\r\n";
            }
            if (step_result) {
                let html = '';
                let tempSteps = step_result.split('\r\n');
                for (let i = 0; i < tempSteps.length; i++) {
                    html += '<div style="text-align: left">' + tempSteps[i] + '</div>';
                }
                return html;
            }
        }

        const _r_columns = [
            {field: 'checked', checkbox: true, align: "center", width: '40'},
            {title: 'id', field: 'id', visible: false},
            {
                title: '测试用例标题',
                field: 'name',
                align: 'center',
                class: 'col-md-3',
                formatter: function (value, row, index) {
                    return row.testCase.name;
                }
            },
            {title: '测试步骤', field: 'stepStore', align: 'center', class: 'col-md-3', formatter: refLinkFormatter},
            {
                title: '预期结果',
                field: 'resultStore',
                align: 'center',
                class: 'col-md-3',
                formatter: refCaseResultFormatter
            },
            {
                title: "优先级", field: 'priority', align: 'center', formatter: function (value, row, index) {
                    return row.testCase.priority;
                }
            },
            {
                field: 'Button',
                title: "操作",
                align: 'center',
                formatter: function (value, row, index) {
                    const temp_case = JSON.stringify(row).replace(/\"/g, "'");
                    if (row.bugId === '' || row.bugId == null) {
                        return '<button type="button" class="btn btn-sm btn-danger" onclick="addRefBug(' + temp_case + ')" ' +
                            'title="录入问题单即表示该条测试用例未通过本次测试" data-bs-toggle="modal" data-bs-target="#add-bug-modal"><i class="bi-bug"></i></button>';
                    } else {
                        return '<button disabled type="button" class="btn btn-sm btn-danger" onclick="addRefBug(' + temp_case + ')" ' +
                            'title="该条测试用例已执行完成" data-bs-toggle="modal" data-bs-target="#add-bug-modal"><i class="bi-bug"></i></button>';
                    }
                }
            }
        ];
        const _r_params = function (params) {
            return {
                // moduleName: $('#search-modal-name').val().trim(), // 自定义查询条件
                // title: $('#search-title').val().trim(), // 自定义查询条件
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,

            }
        };
        const _rowStyle = (row, index) => {
            if (row.runResult === 1) {
                return {css: {'background-color': '#f10836'}};
            }
            if (row.runResult === 0) {
                return {css: {'background-color': '#41d767'}};
            }
            return {};
        }
        InitTable(url, 'get', _r_columns, _r_params, "#refRunTable", _rowStyle);
    })
}

/**
 * 关联测试用例时加载数据
 * @param id 测试计划数据id值
 */
function loadCaseInfo(id) {
    $('#ref-case-modal').on('shown.bs.modal', function () {
        $('#hidden-plan-id').val(id);
        const url = "/case/loadCaseByPlan/" + id;

        // 测试用例步骤
        function refLinkFormatter(value, row, index) {
            const stepJsonArray = JSON.parse(row.stepStore);
            let step_result = '';
            for (let i = 0; i < stepJsonArray.length; i++) {
                const temp_step_json = JSON.parse(JSON.stringify(stepJsonArray[i]));
                const tempStepJsonElement = JSON.stringify(temp_step_json[i]);
                const map1 = eval("(" + tempStepJsonElement + ")");
                step_result = step_result + map1.t_s + "\r\n";
            }
            if (step_result) {
                let html = '';
                let tempSteps = step_result.split('\r\n');
                for (let i = 0; i < tempSteps.length; i++) {
                    html += '<div style="text-align: left">' + tempSteps[i] + '</div>';
                }
                return html;
            }
        }

        // 测试用例预期结果
        function refCaseResultFormatter(value, row, index) {
            const stepJsonArray = JSON.parse(row.stepStore);
            let step_result = '';
            for (let i = 0; i < stepJsonArray.length; i++) {
                const temp_step_json = JSON.parse(JSON.stringify(stepJsonArray[i]));
                const tempStepJsonElement = JSON.stringify(temp_step_json[i]);
                const map1 = eval("(" + tempStepJsonElement + ")");
                step_result = step_result + map1.t_r + "\r\n";
            }
            if (step_result) {
                let html = '';
                let tempSteps = step_result.split('\r\n');
                for (let i = 0; i < tempSteps.length; i++) {
                    html += '<div style="text-align: left">' + tempSteps[i] + '</div>';
                }
                return html;
            }
        }

        const _r_columns = [
            {field: 'checked', checkbox: true, align: "center", width: '40'},
            {title: 'id', field: 'id', visible: false},
            {
                title: '模块', field: 'moduleId', align: 'center', formatter: function (value, row, index) {
                    return row.module.moduleName;
                }
            },
            {title: '测试用例标题', field: 'name', align: 'center', class: 'col-md-3'},
            {title: '测试步骤', field: 'stepStore', align: 'center', class: 'col-md-3', formatter: refLinkFormatter},
            {
                title: '预期结果',
                field: 'resultStore',
                align: 'center',
                class: 'col-md-3',
                formatter: refCaseResultFormatter
            },
            {title: "优先级", field: 'priority', align: 'center',},
            {
                field: 'Button',
                title: "操作",
                align: 'center',
                formatter: function (value, row, index) {
                    return '<button type="button" class="btn btn-sm btn-info" onclick="addRefCase(\'' + row.id + '\')">添加</button>';
                }
            }
        ];
        const _r_params = function (params) {
            return {
                moduleName: $('#search-modal-name').val().trim(), // 自定义查询条件
                title: $('#search-title').val().trim(), // 自定义查询条件
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,

            }
        };
        InitTable(url, 'get', _r_columns, _r_params, "#refCaseTable");
    })
}

/**
 * 测试计划添加关联测试用例
 * @param id 待关联的测试用例数据id值
 */
function addRefCase(id) {
    let res = [];
    if (id) {
        res.push(id);
    } else {
        const selections = getSelections();
        if (selections.length <= 0) {
            showToast(300, "先选择数据");
            return;
        }
        for (let i = 0; i < selections.length; i++) {
            res.push(selections[i].id);
        }
    }
    const params = {
        planId: $('#hidden-plan-id').val(),
        caseId: JSON.stringify(res)
    };
    $.ajax({
        url: '/plan/addRefCase/',
        type: 'post',
        data: params,
        contentType: 'application/x-www-form-urlencoded',
        success: function (arg) {
            showToast(arg['code'], arg['msg']);
            // 关联测试用例数据后重新加载一次列表数据
            $('#refCaseTable').bootstrapTable('refresh');
        }
    });
    $('#ref-case-modal').off('shown.bs.modal');
}

/**
 * 测试计划关联测试用例：未通过测试用例录入bug数据弹框填充部分数据
 * @param params
 */
function addRefBug(params) {
    $('#add-bug-modal').on('shown.bs.modal', function () {
        console.log(params);
        const tempCase = params.testCase;
        $('#hidden-ref-id').val(params.id);
        $('#projectCode').val(tempCase.project.projectName).attr('readonly', true);
        $('#b-a-p').val(tempCase.projectId);
        $('#module').val(tempCase.module.moduleName);
        $('#b-a-m').val(tempCase.moduleId);
        const _temp_story = params.plan.story;
        $('#add-bug-story').val(Object.is(_temp_story,undefined)?"":_temp_story.description);
        $('#bug-story-ref').val(Object.is(params.plan.storyId, undefined) ? '' : params.plan.storyId);
        $('#functionDesc').val(tempCase.name);

        // 打开bug弹框时按钮增加属性用于modal之间的跳转
        $('#close-modal-btn').attr('data-bs-toggle', 'modal').attr('data-bs-target', '#ref-run-modal');
        $('#new_target_cancel').attr('data-bs-toggle', 'modal').attr('data-bs-target', '#ref-run-modal');
        $('#new_target_submit_bug').attr('data-bs-toggle', 'modal').attr('data-bs-target', '#ref-run-modal');
    });
}

function batchPassCase(id) {
    let res = [];
    if (id) {
        res.push(id);
    } else {
        const selections = getSelections();
        if (selections.length <= 0) {
            showToast(300, "先选择数据");
            return;
        }
        for (let i = 0; i < selections.length; i++) {
            // todo 暂过滤掉已执行过的测试用例数据
            if (selections[i].runStatus === 0) {
                res.push(selections[i].id);
            }
        }
    }
    const params = {
        // planId: $('#hidden-plan-id').val(),
        refIds: JSON.stringify(res)
    };
    console.log(res);
    $.ajax({
        url: '/planCaseRef/batchPass/',
        type: 'post',
        data: params,
        contentType: 'application/x-www-form-urlencoded',
        success: function (arg) {
            showToast(arg['code'], arg['msg']);
            // 关联测试用例数据后重新加载一次列表数据
            $('#refRunTable').bootstrapTable('refresh');
        }
    })
}