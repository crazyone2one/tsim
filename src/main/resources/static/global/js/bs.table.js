/*bootstrap-table 插件相关js*/
let _table = $("#table");
/**
 * 加载表格数据
 * @param url 请求路径
 * @param requestMethod 请求方法
 * @param columns 表格表头
 * @param parameters 请求参数
 * @param tableElement
 * @returns {any}
 * @constructor
 */
const InitTable = function (url, requestMethod, columns, parameters, tableElement) {
    if (tableElement) {
        _table = $(tableElement);
    }
    console.log(_table);
    // 销毁表格
    _table.bootstrapTable("destroy");
    // 加载表格
    _table.bootstrapTable({
        classes: "table",
        showHeader: true,
        url: url,
        method: requestMethod,
        // dataType: "json",
        contentType: 'application/x-www-form-urlencoded',
        toolbar: "#toolbar",
        cache: false,
        uniqueId: "id",
        // showRefresh: true,
        undefinedText: "-",
        pagination: true,
        pageSize: 10, //每页记录行数
        pageNumber: 1, //初始化加载第一页
        pageList: [10, 25, 50, 100, 200],
        clickToSelect: true,
        sidePagination: 'server', // server:服务器端分页|client：前端分页
        queryParams: parameters,
        columns: columns,
        responseHandler: responseHandler,//响应数据
    });
    return InitTable;
};

function responseHandler(response) {
    if (response) {
        if (Object.is(0, response.total)) {
            return {
                "rows": [],
                "total": 0
            }
        }
        return {
            "rows": response.rows,
            "total": response.total
        };
    } else {
        return {
            "rows": [],
            "total": 0
        }
    }
}

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

function refresh_table() {
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



