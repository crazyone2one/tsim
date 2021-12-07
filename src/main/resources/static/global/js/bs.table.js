/*bootstrap-table 插件相关js*/
const _table = $("#table");
/**
 * 加载表格数据
 * @param url 请求路径
 * @param columns 表格表头
 * @returns {any}
 * @constructor
 */
const InitTable = function (url, columns) {
    // 销毁表格
    _table.bootstrapTable("destroy");
    // 加载表格
    _table.bootstrapTable({
        showHeader: true,
        url: url,
        method: 'post',
        dataType: "json",
        toolbar: "#toolbar",
        cache: false,
        uniqueId: "id",
        // showRefresh: true,
        undefinedText: "-",
        pageNumber: 1, // 初始化加载第一页
        pagination: true, // 是否分页
        pageSize: 10, // 单页记录数
        clickToSelect: true,
        paginationLoop: false,
        sidePagination: 'server', // server:服务器端分页|client：前端分页
        queryParamsType: '',
        queryParams: queryParams,
        columns: columns,

    });
    return InitTable;
};

/**
 * 请求参数
 * @param params
 * @returns {{pageSize: (number|*), pn: (number|*)}}
 */
function queryParams(params) {
    return {
        pageSize: params.pageSize,
        pn: params.pageNumber
    }
}

function refresh() {
    _table.bootstrapTable('refresh');
}

function getSelections() {
    return _table.bootstrapTable('getSelections');
}

window.operateEvents = {
    // 当点击 class=add 时触发
    'click .add': function (e, value, row, index) {
        // 在 console 打印出整行数据
        console.log(row);
    }
};



