'use strict';
const test_stage = {
    "1": '单元测试', "2": '集成测试', "3": '系统测试', "4": '验收测试', "5": '回归测试',
}
// 列表
const _columns = [{field: 'id', align: 'center', title: 'id', visible: false}, {
    field: 'name', align: 'center', title: '名称', class: 'col-sm-2'
}, {
    field: 'workStatus', align: 'center', title: '当前状态', class: 'col-sm-2', formatter: function (value, row, index) {
        if (row.finishProcess && !Object.is('100.0', row.finishProcess)) {
            return '<span class="badge bg-info">进行中</span>';
        } else if (Object.is(0, value)) {
            return '<span class="badge bg-secondary">未开始</span>';
        } else {
            return '<span class="badge bg-success">已完成</span>';
        }
    }
}, {
    field: 'passRate', align: 'center', title: '通过率', class: 'col-sm-2',formatter:function (value) {
        return value + ' %';
    }
},{
    field: 'runCaseCount', align: 'center', title: '已测用例', class: 'col-sm-2'
},{
    field: 'finishProcess', align: 'center', title: '测试进度', class: 'col-sm-2', formatter: function (value, row, index) {
        let progress;
        if (value) {
            const per = value.split('.')[0];
            progress = '<div class="progress" style="height: 10px;">\n' + '  <div class="progress-bar" role="progressbar" style="width: ' + per + '%" aria-valuenow="' + per + '" aria-valuemin="0" aria-valuemax="100">' + value + '%' + '</div>' + '</div>';
        } else {
            progress = '<div class="progress" style="height: 10px;">\n' + '  <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>\n' + '</div>';
        }
        return progress;
    }
}, {
    field: 'testStage', align: 'center', title: '测试阶段', class: 'col-sm-2', formatter: function (value) {
        return test_stage[value];
    }
},];


const _params = function (params) {
    return {
        sortName: this.sortName,
        sortOrder: this.sortOrder,
        pageSize: params.limit,
        pageNum: params.offset / params.limit + 1,
        currentUser: "yes",
    }
};
_table.on('sort.bs.table', function (e, name, order) {//点击排序
    _params.orderField = name
    _params.order = order
});
InitTable("/plan/loadPlan", "get", _columns, _params, '#current-user-plan-table');
//移除分页信息
const element = document.getElementById('current-user-plan-table').parentNode.parentNode.parentNode.lastElementChild;
element.remove();