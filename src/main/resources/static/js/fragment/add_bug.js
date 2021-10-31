// 保存bug
function saveBug() {
    const bug_info = $("#add-bug-from").serialize();
    // bug模块直接添加bug数据
    if (Object.is(arguments[0], 1)) {
        $.ajax({
            url: "/bug/save",
            type: 'POST',
            data: bug_info,
            dataType: 'JSON',
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
                    $("#add-bug-modal").modal('hide');
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
        });
    } else {
        // 测试计划关联测试用例模块中录入bug信息
        // console.log(JSON.parse(bug_info));
        $.ajax({
                url: '/planCaseRef/saveRefInfo/',
                type: 'post',
                data: JSON.stringify({
                    bug_info: serializeObject($("#add-bug-from")),
                    plan_case: arguments[1]
                }),
                dataType: 'JSON',
                // contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (result) {
                    console.log(result);
                    // 数据添加成功后关闭弹框
                    $("#add-bug-modal").modal('hide');
                    location.reload();
                }
            }
        )
    }

    /*form表单转成*/
    function serializeObject() {
        const o = {};
        const a = arguments[0].serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || "");
            } else {
                o[this.name] = this.value || "";
            }
        });
        return o;
    }
}
