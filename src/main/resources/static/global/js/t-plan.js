const case_priority = {
    0: '<span class="badge bg-danger">P0</span>',
    1: '<span class="badge bg-warning">P1</span>',
    2: '<span class="badge bg-info">P2</span>'
}
const case_run_result = {
    '1': '<span class="badge bg-success">通过</span>',
    '2': '<span class="badge bg-warning">失败</span>',
    '3': '<span class="badge bg-danger">阻塞</span>'
}
const case_run_status = {
    '0': '<span class="badge bg-secondary">未执行</span>',
    '1': '<span class="badge bg-success">已执行</span>',
    // '2': '<span class="badge bg-danger">阻塞</span>'
}

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
            const span = document.createElement('span');
            span.setAttribute('title', step_result);
            span.innerHTML = step_result;
            return span.outerHTML;
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
            const span = document.createElement('span');
            span.setAttribute('title', step_result);
            span.innerHTML = step_result;
            return span.outerHTML;
        }

        const _r_columns = [{field: 'checked', checkbox: true, align: "center", width: '40'}, {
            title: 'id', field: 'id', visible: false
        }, {
            title: '用例名称', field: 'name', align: 'center', class: 'col-md-3', formatter: function (value, row, index) {
                return row.testCase.name;
            }
        }, {
            title: "优先级", field: 'priority', align: 'center', formatter: function (value, row, index) {
                return case_priority[row.testCase.priority];
            }
        }, {field: '', align: "center", title: '类型'}, {
            field: 'testMode', align: "center", title: '测试方式', formatter: function (value, row, index) {
                if (Object.is(1, row.testCase.testMode)) {
                    return '自动';
                } else {
                    return '手动';
                }
            }
        }, {
            field: 'runStatus', align: "center", title: '执行状态', formatter: function (value) {
                if (Object.is(value, undefined)) {
                    return case_run_status['0'];
                }
                return case_run_status[value];
            }
        }, {
            field: 'runResult', align: "center", title: '执行结果', formatter: function (value) {
                return case_run_result[value];
            }
        }, {
            field: 'Button', title: "操作", align: 'center', formatter: function (value, row, index) {
                const temp_case = JSON.stringify(row).replace(/\"/g, "'");
                if (Object.is("1", row.runStatus)) {
                    return '<button disabled class="btn btn-sm hvr-grow me-1" data-bs-toggle="modal" data-bs-target="#execute-fragment-modal" onclick="executeCase(' + temp_case + ')">' + '<i class="bi-play-circle"></i></button>';
                }
                return '<button class="btn btn-sm hvr-grow me-1 bg-success" data-bs-toggle="modal" data-bs-target="#execute-fragment-modal" onclick="executeCase(' + temp_case + ')">' + '<i class="bi-play-circle"></i></button>';
            }
        }];
        const _r_params = function (params) {
            return {
                // moduleName: $('#search-modal-name').val().trim(), // 自定义查询条件
                // title: $('#search-title').val().trim(), // 自定义查询条件
                pageSize: params.limit, pageNum: params.offset / params.limit + 1,

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
        InitTable(url, 'get', _r_columns, _r_params, "#refRunTable");
    })
}

/**
 * 关联测试用例时加载数据
 * @param id 测试计划数据id值
 */
function loadCaseInfo(id) {
    $('#ref-case-modal').on('shown.bs.modal', function () {
        $('#ass-hidden-plan-id').val(id);
        // fixme 再次打开modal时没有清空查询条件
        $('#ass-search-name').val();
        $('#ass-search-title').val();
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
            const span = document.createElement('span');
            span.setAttribute('title', step_result);
            span.innerHTML = step_result;
            return span.outerHTML;
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
            const span = document.createElement('span');
            span.setAttribute('title', step_result);
            span.innerHTML = step_result;
            return span.outerHTML;
        }

        const _r_columns = [{field: 'checked', checkbox: true, align: "center", width: '40'}, {
            title: 'id', field: 'id', visible: false
        }, {
            title: '模块', field: 'moduleId', align: 'center', formatter: function (value, row, index) {
                return row.module.moduleName;
            }
        }, {title: '测试用例标题', field: 'name', align: 'center', class: 'col-md-3'}, {
            title: '测试步骤',
            field: 'stepStore',
            align: 'center',
            class: 'col-md-3',
            cellStyle: formatTableUnit,
            formatter: refLinkFormatter
        }, {
            title: '预期结果',
            field: 'resultStore',
            align: 'center',
            class: 'col-md-3',
            cellStyle: formatTableUnit,
            formatter: refCaseResultFormatter
        }, {
            title: "优先级", field: 'priority', align: 'center', formatter: function (value, row, index) {
                return case_priority[value];
            }
        }, {
            field: 'Button', title: "操作", align: 'center', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-sm btn-info hvr-grow" onclick="addRefCase(\'' + row.id + '\')"><i class="bi-plus-circle"></i> </button>';
            }
        }];
        const _r_params = function (params) {
            return {
                moduleName: $('#ass-search-name').val().trim(), // 自定义查询条件
                title: $('#ass-search-title').val().trim(), // 自定义查询条件
                pageSize: params.limit, pageNum: params.offset / params.limit + 1,

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
    }
    const params = {
        planId: $('#ass-hidden-plan-id').val(), caseId: JSON.stringify(res)
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
// 批量关联测试用例
$('#batch-add-case-ref').on('click',function () {
    let res = [];
    const selections = getSelections();
    if (selections.length <= 0) {
        showToast(300, "先选择数据");
        return;
    }
    for (let i = 0; i < selections.length; i++) {
        res.push(selections[i].id);
    }
    const params = {
        planId: $('#ass-hidden-plan-id').val(), caseId: JSON.stringify(res)
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
})

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
        $('#add-bug-story').val(Object.is(_temp_story, undefined) ? "" : _temp_story.description);
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

/**
 * 测试步骤弹框数据加载
 * @param option
 */
function executeCase(option) {
    // console.log(option);
    const executeModal = document.getElementById('execute-fragment-modal');
    executeModal.addEventListener('shown.bs.modal', function (event) {
        $('#add-bug-label').prop('checked', false);
        $('#floatingTextarea2').val("");
        $('#execute-hidden-plan-id').val(option['planId']);
        $('#execute-hidden-case-id').val(option['caseId']);
        $('#execute-case-name').text(option['testCase']['name']);
        $('#temp-priority').remove();
        $('#execute-case-priority').append('<div id="temp-priority">' + case_priority[option['testCase']['priority']] + '</div>');
        const _columns = [{field: 'id', align: 'center', title: 'id', visible: false}, {
            field: 'caseStep',
            align: 'center',
            title: '步骤描述',
            class: 'col-sm-4',
            formatter: function (value, row, index) {
                return '<span id="step' + index + '">' + value + '</span>';
            }
        }, {
            field: 'caseStepResult',
            align: 'center',
            title: '预期结果',
            class: 'col-sm-4',
            formatter: function (value, row, index) {
                return '<span id="preResult' + index + '">' + value + '</span>';
            }
        }, {
            field: '', align: 'center', title: '实际结果', class: 'col-sm-4', formatter: function (value, row, index) {
                return '<div><input id="temp_case_result' + row.caseOrder + '"></div>';
            }
        }, {
            field: '', align: 'center', title: '步骤执行结果', class: 'col-sm-2', formatter: function (value, row, index) {
                return '<div><select id="temp-execute-result' + row.caseOrder + '" class="form-select" aria-label="Default select example">\n' + '  <option value="1">通过</option>\n' + '  <option value="2">失败</option>\n' + '  <option value="3">阻塞</option>\n' + '</select></div>';
            }
        },];
        const _params = function (params) {
            return {
                'caseId': option['caseId'], pageSize: params.limit, pageNum: params.offset / params.limit + 1,

            }
        };
        InitTable("/testcaseSteps/step4execute", "get", _columns, _params, '#execute-case-table');
        //移除分页信息
        const element = document.getElementById('execute-case-table').parentNode.parentNode.parentNode.lastElementChild;
        element.remove();
    });
}

/**
 * 提交测试用例执行结果
 */
$('#execute-case-submit').on('click', function (e) {
    let tempIssueContent4Step = '[测试步骤]\r\n';
    let tempIssueContent4PreResult = '[预期结果]\r\n';
    let tempIssueContent4Result = '[实际结果]\r\n';
    const planID = $('#execute-hidden-plan-id').val();
    const caseID = $('#execute-hidden-case-id').val();
    let requestParams = [];
    let executeResultList = []; // 用于存放测试步骤执行结果
    const caseSize = $('#execute-case-table tbody tr').length;
    for (let i = 0; i < caseSize; i++) {
        const tempExecuteResult = $('#temp-execute-result' + i).val();
        const preResult = $('#preResult' + i).text();
        const $resultEle = $('#temp_case_result' + i);
        // 测试用例通过时将预期结果赋值给实际结果
        if (Object.is('1', tempExecuteResult)) {
            $resultEle.val(preResult);
        }
        executeResultList.push(tempExecuteResult);
        const tempResult = $resultEle.val();
        tempIssueContent4Step += '步骤' + (i + 1) + ':' + $('#step' + i).text() + ' \r\n';
        tempIssueContent4PreResult += '步骤' + (i + 1) + ':' + preResult + ' \r\n';
        tempIssueContent4Result += '步骤' + (i + 1) + ':' + tempResult + ' \r\n';
        let tempParam = {
            'caseStepId': $('#execute-case-table tr[data-index="' + i + '"]').attr('data-uniqueid'),
            'caseResult': tempResult,
            'executeResult': tempExecuteResult,
        };
        requestParams.push(tempParam);
    }
    if (executeResultList.includes('2') || executeResultList.includes('3')) {
        forwardToConfirmModal('confirm-modal', '存在失败的测试步骤，是否添加问题单?');
    }
    let tempBugParams = {
        'step': tempIssueContent4Step,
        'preResult': tempIssueContent4PreResult,
        'executeResult': tempIssueContent4Result,
    };
    // 确认框点击确定按钮，填充测试步骤、结果数据
    $('#btn-confirm').on('click', function () {
        const $floatingTextarea2 = $('#floatingTextarea2');
        $floatingTextarea2.val(tempIssueContent4Step + '\r\n' + tempIssueContent4PreResult + '\r\n' + tempIssueContent4Result);
        $floatingTextarea2.textareaAutoHeight();
        $('#add-bug-label').prop('checked', true);
        closeModal('confirm-modal');

        $.ajax({
            url: '/plan-case-result/addRef',
            type: 'post',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                'planId': planID,
                'caseId': caseID,
                'caseFragment': JSON.stringify(requestParams),
                'bugFragment': JSON.stringify(tempBugParams),
            },
            success: function (res) {
                console.log(res);
            }
        })
    })
    $('#btn-dismiss').on('click', function () {
        $.ajax({
            url: '/plan-case-result/addRef',
            type: 'post',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                'planId': planID,
                'caseId': caseID,
                'caseFragment': JSON.stringify(requestParams),
            },
            success: function (res) {
                console.log(res);
            }
        })
    })
});
/**
 * 测试报告
 */
const loadReport = (planId) => {
    $.ajax({
        url: '/plan/loadReport',
        type: 'get',
        contentType: 'application/x-www-form-urlencoded',
        data: {
            'planId': planId,
        },
        success: function (res) {
            // console.log(res);
            const result = res['data'];
            // 基本信息
            const plan = result['plan'];
            $('#report-project-name').text(plan['project']['projectName']);
            $('#report-begin-date').text(plan['startDate'].toString().split(' ')[0]);
            $('#report-end-date').text(plan['finishDate'].toString().split(' ')[0]);
            // 测试用例执行情况
            const caseInfo = result['caseInfo'];
            const $table = $('#report-plan-info-table')
            $table.bootstrapTable("destroy");
            $table.bootstrapTable({data: caseInfo});
            //   统计图
            const statistics = result['statistics'];
            const option = {
                title: {
                    text: '测试结果统计图',
                    // subtext: 'Fake Data',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        // center: ['50%', '50%'],
                        name: '测试用例执行结果',
                        type: 'pie',
                        radius: '50%',
                        data: [
                            {value: statistics['unExecute'], name: '未执行'},
                            {value: statistics['pass'], name: '通过'},
                            {value: statistics['fail'], name: '失败'},
                            {value: statistics['blocking'], name: '阻塞'},
                            // { value: 300, name: 'Video Ads' }
                        ],
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
            const chartDom = document.getElementById('statistics-echarts');
            const statisticsEcharts = echarts.init(chartDom);
            option && statisticsEcharts.setOption(option);
            window.onresize=function () {
                chartDom.resize();
            }
        }
    })
};
