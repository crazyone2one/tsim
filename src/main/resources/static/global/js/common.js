// Toggle the side navigation
$("#sidebarToggle, #sidebarToggleTop").on('click', function (e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
        $('.sidebar .collapse').collapse('hide');
    }
});
/**
 * 使用bootstrap toast作为消息提示
 * @param code ajax请求后返回的状态码，用于决定toast的类型
 * @param msg 提示消息内容
 */
function showToast(code, msg) {
    if (Object.is(200, code)) {
        $('.toast').addClass("bg-success");
    } else {
        $('.toast').addClass("bg-danger");
    }
    $(".toast-body").text(msg);
    $('.toast').toast('show');
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
        success: function (result) {
            if (Object.is(result['code'], 200)) {
                if (result['data']) {
                    for (let i = 0; i < result['data'].length; i++) {
                        let temp = {}
                        switch (flag) {
                            case "p":
                                // project
                                temp = {label: result['data'][i].projectName, value: result['data'][i].id};
                                break;
                            case "m":
                                //module
                                temp = {label: result['data'][i].moduleName, value: result['data'][i].id};
                                break;
                            case "s":
                                //story
                                temp = {label: result['data'][i].description, value: result['data'][i].id};
                                break;
                        }
                        dataAttr.push(temp);
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
function resetModal(modalId, formId) {
    $(modalId).modal('toggle');
    $(modalId).on('hide.bs.modal', function () {
        if (formId) {
            document.getElementById(formId).reset();
        }
        console.log(" hide >> reset modal completed");
    });
    $(modalId).on('hidden.bs.modal', function () {
        if (formId) {
            document.getElementById(formId).reset();
        }
        console.log(" hidden >> reset modal completed");
    })
}
/*
手动关闭modal弹框时清空已输入的内容
1. 点击关闭、取消按钮
2. 点击 x
*/
$(".modal").on('hide.bs.modal', function (e) {
    $(".modal form")[0].reset();
});
