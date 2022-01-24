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
    } else if (Object.is(300, code)) {
        $toast.addClass("bg-warning");
    } else {
        $toast.addClass("bg-danger")
    }
    // Object.is(200, code) ? $toast.addClass("bg-success") : $toast.addClass("bg-danger");
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
                            "s": {label: result['data'][i].description, value: result['data'][i].id} , // story
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

/*****文件上传*/
$('.file-input').on('change', function () {
    // 恢复提交按钮
    $('#uploadFileBtn').attr('disabled', false);
})

$('#uploadFileBtn').click(function (e) {
    e.preventDefault();
    // 置灰提交按钮，防止重复提交
    $(this).attr('disabled', true);
    const formData = new FormData();
    formData.append("file", $('#inputGroupFile04')[0].files[0]);
    const $progressbar = $('#progressbar');
    $.ajax({
        url: "/story/upload",
        type: 'post',
        data: formData,
        processData: false,
        contentType: false,
        xhr: function () {
            const xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (event) {
                const perc = Math.round((event.loaded / event.total) * 100);
                $progressbar.text(perc + "%");
                $progressbar.css('width', perc + "%");
            };
            return xhr;
        },
        beforeSend: function (xhr) {
            $progressbar.text('');
            $progressbar.css('width', "0%");
        }
    })
        .done(function (result) {
            $('#inputGroupFile04').val();
            $('#uploadFileBtn').attr("disabled", false);
            let tempMap = new Map(Object.entries(result.data));
            $('#attachmentId').val(tempMap.get('docId'));
        })
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