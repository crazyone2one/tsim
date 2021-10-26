const ref_modal = document.getElementById('ref-case-modal');
ref_modal.addEventListener('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const temProId = button.closest('tr').find('#project').attr('value');
    const templanId = button.closest('tr').find("#id").attr('value');
    $.ajax({
            url: '/case/loadCaseByProject/',
            type: 'post',
            data: JSON.stringify({
                projectId: temProId,
                planId: templanId
            }),
            dataType: 'JSON',
            // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (arg) {
                loadCase(arg['data']);
            }
        }
    )
});

// 加载数据
function loadCase(res) {
    $('#refCaseTable > tbody').html("");
    const tbody = $('#refCaseTable').find('tbody');

    for (const index in res) {
        const id = res[index].id;
        const new_tr = $("<tr></tr>")
        const title_td = $("<td></td>")
        const desc_td = $("<td></td>")
        const pre_td = $("<td></td>")
        const step_td = $("<td></td>")
        const result_td = $("<td></td>")

        const title = res[index].name;
        const desc = res[index].description;
        const pre = res[index].precondition;
        const step = res[index].stepStore;
        const result = res[index].resultStore;
        title_td.text(title);
        desc_td.text(desc);
        pre_td.text(pre);
        step_td.text(step);
        result_td.text(result);
        const checkItemTd = $('<td><input type="checkbox" class="select-item checkbox" name="brand" id="brand" onclick="selectItem($(this))" value="' + id + '"/></td>');
        new_tr.prepend(checkItemTd);
        new_tr.append(title_td);
        new_tr.append(desc_td);
        new_tr.append(pre_td);
        new_tr.append(step_td);
        new_tr.append(result_td);
        tbody.append(new_tr);
    }
}

// 全选/全不选
$("input.select-all").click(function () {
    let temp_ids = [];
    const checked = $(this).prop("checked");
    if (Object.is(checked, true)) {
        //全选
        $("input.select-item").each(function (index, item) {
            $(item).prop("checked", checked);
            temp_ids.push($(item).val());
        });
    } else {
        //反选
        $("input.select-item").each(function (index, item) {
            $(item).prop("checked", checked);
        });
        temp_ids = []
    }
    console.log(temp_ids);
});

//选择单个数据
function selectItem(item) {
    let temp_ids = [];
    const checked = $(item).prop("checked");
    $(item).prop("checked", checked);
    if (checked) {
        console.log(checked);
        const val = $(item).val();
        temp_ids.push(val);
    } else {
        console.log(checked);
        const val = $(item).val();
        temp_ids.slice($.inArray(val, temp_ids), 1);
    }
    console.log(temp_ids);
}

//获取选择数据的id
$('#sub-ref').click(function () {
    const items = document.getElementsByClassName("select-item");
    let res = [];
    for (let i = 0; i < items.length; i++) {
        if ($(items[i]).is(":checked")) {
            res.push($(items[i]).val());
        }
    }
    console.log(res);
    const params = {
        caseId: res
    };
    $.ajax({
        url: '/plan/addRefCase/',
        type: 'post',
        data: JSON.stringify(params),
        dataType: 'json',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (arg) {
            if (Object.is(arg['code'], 200)) {
                iziToast.show({
                        // title: 'Success',
                        message: arg['msg'],
                        position: 'topRight',
                        timeout: 2000,
                        color: 'green'
                    }
                );
                // 数据添加成功后关闭弹框
                $("#ref-case-modal").modal('hide');
                location.reload();
            } else {
                iziToast.show({
                        // title: 'Success',
                        message: arg['msg'],
                        position: 'topRight',
                        timeout: 2000,
                        color: 'red'
                    }
                );
            }
        }
    })
});
