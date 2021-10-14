$(function () {
    function initTableCheckbox() {
        const $thr = $('table thead tr');
        const $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll"></th>');
        /*将全选/反选复选框添加到表头最前，即增加一列*/
        $thr.prepend($checkAllTh);
        /*“全选/反选”复选框*/
        const $checkAll = $thr.find('input');
        $checkAll.click(function (event) {
            /*将所有行的选中状态设成全选框的选中状态*/
            $tbr.find('#checkItem').prop('checked', $(this).prop('checked'));
            /*并调整所有选中行的CSS样式*/
            if ($(this).prop('checked')) {
                $tbr.find('input').parent().parent().addClass('warning');
            } else {
                $tbr.find('input').parent().parent().removeClass('warning');
            }
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击全选框所在单元格时也触发全选框的点击操作*/
        $checkAllTh.click(function () {
            $(this).find('input').click();
        });
        let $tbr = $('table tbody tr');
        const $checkItemTd = $('<td><input type="checkbox" name="checkItem" id="checkItem" /></td>');
        /*每一行都在最前面插入一个选中复选框的单元格*/
        $tbr.prepend($checkItemTd);
        /*点击每一行的选中复选框时*/
        $tbr.find('checkItem').click(function (event) {
            /*调整选中行的CSS样式*/
            $(this).parent().parent().toggleClass('warning');
            /*如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态*/
            $checkAll.prop('checked', $tbr.find('input:checked').length === $tbr.length);
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击每一行时也触发该行的选中操作*/
        $tbr.click(function () {
            $(this).find('checkItem').click();
        });
    }

    initTableCheckbox();
});
//Select all check boxes : Setting the checked property to true in checkAll() function
function checkAll() {
    const items = document.getElementsByName('brand');
    for (let i = 0; i < items.length; i++) {
        if (items[i].type === 'checkbox') {
            items[i].checked = true;
        }
    }
}
// Clear all check boxes : Setting the checked property to false in uncheckAll() function
function uncheckAll() {
    const items = document.getElementsByName('brand');
    for (let i = 0; i < items.length; i++) {
        if (items[i].type === 'checkbox')
            items[i].checked = false;
    }
}

