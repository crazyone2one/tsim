/**
 * 提示消息
 * @param message 消息内容
 * @param type 消息类别 ['success' ,'warning', 'danger' ...]
 */
function alert(message, type) {
    const liveAlertPlaceholder = $('#liveAlertPlaceholder');
    liveAlertPlaceholder.empty();
    let svg_str = '<svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>';
    switch (type) {
        case 'warning':
            svg_str = '<svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Warning:"><use xlink:href="#exclamation-triangle-fill"/></svg>';
            break;
        case "danger":
            svg_str = '<svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>';
            break;
    }
    const alertPlaceholder = document.getElementById('liveAlertPlaceholder');
    const wrapper = document.createElement('div');
    wrapper.innerHTML = '<div class="alert alert-' + type + ' d-flex align-items-center alert-dismissible" role="alert">' +
        svg_str + '<div>' + message + '</div></div>'
    alertPlaceholder.append(wrapper);
    liveAlertPlaceholder.fadeTo(2000, 500).slideUp(500, function () {
        $("#liveAlertPlaceholder").slideUp(500);
    });
    // $('#liveAlertPlaceholder').remove();
    if (Object.is(type, 'success')) {
        location.reload();
    }
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
