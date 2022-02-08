'use strict';

/**
 * 使用bootstrap toast作为消息提示
 * @param code ajax请求后返回的状态码，用于决定toast的类型
 * @param msg 提示消息内容
 */
const showToast = (code, msg) => {
    const $toast = $('.toast');
    $toast.hasClass('bg-success') && $toast.removeClass('bg-success');
    $toast.hasClass('bg-warning') && $toast.removeClass('bg-warning');
    $toast.hasClass('bg-danger') && $toast.removeClass('bg-danger');
    if (Object.is(200, code)) {
        $toast.addClass("bg-success")
    } else if (Object.is(400, code)) {
        $toast.addClass("bg-danger")
    } else {
        $toast.addClass("bg-warning");
    }
    $(".toast-body").text(msg);
    $toast.toast('show');
}

/**
 * 删除class属性
 * @param idSelector
 * @param className
 */
const removeClass = (idSelector, className) => {
    const t = $(idSelector);
    t.hasClass(className) && t.removeClass(className);
}

/**
 * 从一个对象中 delete 一个属性.使用函数方式创建一个没有此属性的新对象.
 *
 * const toto = { a: 55, b: 66 }
 * const totoWithoutB = removeProperty(toto, 'b') // { a: 55 }
 * @param target
 * @param propertyToRemove
 * @returns {Pick<*, Exclude<keyof *, never>>}
 */
const removeProperty = (target, propertyToRemove) => {
    const {[propertyToRemove]: _, ...newTarget} = target
    return newTarget
}

/**
 * 输入框自动提示
 * @param url 数据加载地址
 * @param idSelector id选择器
 * @param flag p--project,m--module
 * @param needChange 是否需要将value更改为id
 */
function autoComplete(url, idSelector, flag, needChange) {
    const dataAttr = [];
    $.ajax({
        url: url,
        beforeSend: function (xmlHttp) {
            xmlHttp.setRequestHeader("If-Modified-Since", "0");
            xmlHttp.setRequestHeader("Cache-Control", "no-cache");
        },
        async: false,
        success: function (result) {
            if (Object.is(result['code'], 200)) {
                if (result['data']) {
                    dataAttr.length = 0;
                    for (let i = 0; i < result['data'].length; i++) {
                        let temp = {
                            "p": {label: result['data'][i].projectName, value: result['data'][i].id}, // project
                            "m": {label: result['data'][i].moduleName, value: result['data'][i].id},  // module
                            "s": {label: result['data'][i].storyName, value: result['data'][i].id}, // story
                            "plan": {label: result['data'][i].name, value: result['data'][i].id}  // story
                        }
                        dataAttr.push(temp[flag]);
                    }
                }
            }
        }
    });
    new Autocomplete(document.getElementById(idSelector), {
        data: dataAttr,
        tsf: needChange
    });
}

/**
 * modal关闭时清空内容
 * @param modalId
 * @param formId
 */
const resetModal = (modalId, formId) => {
    // $(modalId).modal('toggle');
    $(modalId).on('hide.bs.modal', function () {
        formId && document.getElementById(formId).reset()
    });
    $(modalId).on('hidden.bs.modal', function () {
        formId && document.getElementById(formId).reset()
    })
}

/*
手动关闭modal弹框时清空已输入的内容
1. 点击关闭、取消按钮
2. 点击 x
*/
$(".modal").on('hide.bs.modal', function (e) {
    if (!Object.is(e.currentTarget.id, 'logoutModal')) {
        $(".modal form")[0].reset();
        // 使用jquery 的 .off() 方式去移除掉已经绑定事件的操作，保证事件只绑定一次
        $('.modal').off('shown.bs.modal');
    }
});
/**
 * modal关闭时删除is-invalid类，清空相关的提示信息
 */
$('.modal').on('show.bs.modal', function () {
    const $invalid = $('.invalid-item');
    $invalid.hasClass('is-invalid') && $invalid.removeClass('is-invalid');
})

/**
 * 设置select控件选中
 * @param selectId select的id值
 * @param checkValue 选中option的值
 */
const select_option_checked = (selectId, checkValue) => {
    const select = document.getElementById(selectId);
    for (let i = 0; i < select.options.length; i++) {
        if (Object.is(Number(select.options[i].value), checkValue) || Object.is(select.options[i].value, checkValue)) {
            select.options[i].selected = true;
            break;
        }
    }
}
/**
 * 关闭模态框
 * @param modalIdSelection
 */
const closeModal = (modalIdSelection) => {
    $('#' + modalIdSelection).modal('hide');
};
/**
 * 展示确认框
 * @param modalIdSelection modal id
 * @param replaceContent 需要替换的内容
 */
const forwardToConfirmModal = (modalIdSelection, replaceContent) => {
    const elementById = document.getElementById(modalIdSelection);
    const modalBody = elementById.getElementsByClassName("modal-body")[0];
    $(modalBody).find('.replace-content').text(replaceContent);
    $('#' + modalIdSelection).modal('show');
}
/**
 * 查询操作
 */
$(document).on('click', "#search-button", function () {
    $('#table').bootstrapTable('refresh');
});
/**
 * 重置操作
 */
$(document).on('click', "#reset-button", function () {
    $('#search-form')[0].reset();
    $('#table').bootstrapTable('refresh');
});

// Enable popovers everywhere
const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
const popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl)
});

/**
 * ajax方式删除文件
 * @param docId 文件数据id
 */
const delete_file_by_ajax = (docId) => {
    $.ajax({
        url: '/deleteFile/' + docId,
        type: "POST",
        success: function (res) {
            console.log(res);
        }
    })
}

function createXhr() {
    if (typeof XMLHttpRequest != 'undefined') {
        return new XMLHttpRequest();
    } else if (typeof ActiveXObject != 'undefined') {
        const callee = arguments.callee;
        if (!Object.is(typeof callee.activeXString, 'string')) {
            const versions = ["MSXML2.XMLHttp.6.0", "MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp"];
            const len = versions.length;
            for (let i = 0; i < len; i++) {
                try {
                    new ActiveXObject(versions[i]);
                    callee.activeXString = versions[i];
                    break;
                } catch (e) {

                }
            }
        }
        return new ActiveXObject(callee.activeXString);
    } else {
        throw new Error("No XHR object available.");
    }
}

/**
 * ajax方式下载文件
 * @param uuidName
 */
function download_file_by_ajax(uuidName) {
    const url = "/download-file/" + uuidName;
    const xhr = createXhr();
    xhr.onload = function (event) {
        if ((xhr.status >= 200 && xhr.status < 300) || xhr.status === 304) {
            const blob = this.response;
            const reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onload = function (ev) {
                const a = document.createElement('a');
                const decode = decodeURI(xhr.getResponseHeader("Content-Disposition"));
                const cArray = decode.split(";");
                const subStr = cArray[cArray.length - 1].split("=")[1];
                a.download = subStr.substring(1, subStr.length - 1);
                a.href = ev.target.result;
                $('body').append(a);
                a.click();
                $(a).remove();
            };
        } else {
            showToast(400, "request unsuccessful:" + xhr.status);
        }
    };
    xhr.open("get", url, true);
    xhr.responseType = 'blob';
    xhr.send(null);
}