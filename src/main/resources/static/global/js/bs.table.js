/*bootstrap-table 插件相关js*/
'use strict';
let _table = $("#table");
/**
 * 加载表格数据
 * @param url 请求路径
 * @param requestMethod 请求方法
 * @param columns 表格表头
 * @param parameters 请求参数
 * @param tableElement
 * @param styleFlag
 * @returns {any}
 * @constructor
 */
const InitTable = function (url, requestMethod, columns, parameters, tableElement, styleFlag) {
    let _rowStyle = {};
    if (styleFlag) {
        _rowStyle = rowStyle;
    }
    if (tableElement) {
        _table = $(tableElement);
    }
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
        rowStyle: _rowStyle,
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

/**
 * 处理相应数据
 * @param response
 * @returns {{total: (number|PaymentItem|number|*), rows: ([]|SQLResultSetRowList|number|HTMLCollectionOf<HTMLTableRowElement>|string|*)}|{total: number, rows: *[]}}
 */
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

/**
 * 刷新table数据
 */
function refresh_table() {
    _table.bootstrapTable('refresh');
}

/**
 * 获取选择的数据
 * @returns {*}
 */
function getSelections() {
    return _table.bootstrapTable('getSelections');
}

/**
 * 设置行颜色
 * @param row
 * @param index
 * @returns {{css: {"background-color": string}}|{}}
 */
const rowStyle = (row, index) => {
    if (row.active === 1) {
        // 测试用例无效时，改变底色
        return {css: {'background-color': '#797b7e'}};
    }
    return {};
}


