'use script';
//表格数据加载------
const _table_columns = [
    {field: 'id', align: 'center', title: 'id', visible: false},
    {field: 'checked', checkbox: true, align: "center", width: '40'}, {
        field: 'reviewName',
        align: 'center',
        title: '评审名称',
        class: 'col-sm-4',
    }, {
        field: 'reviewUser',
        align: 'center',
        title: '评审人',
        class: 'col-sm-4',
    }, {
        field: 'reviewRemark',
        align: 'center',
        title: '评审任务内容',
        class: 'col-sm-4'
    }, {
        field: 'finishStatus',
        align: 'center',
        title: '当前状态', class: 'col-sm-4', formatter: function (value, row, index) {
            let span = '';
            switch (value) {
                case 0:
                default:
                    span = '<span class="badge bg-secondary">未开始</span>';
                    break;
                case 1:
                    span = '<span class="badge bg-info">进行中</span>';
                    break;
                case 2:
                    span = '<span class="badge bg-success">已结束</span>';
                    break
            }
            return span;
        }
    }, {
        field: 'finishDate',
        align: 'center',
        title: '结束时间',
        class: 'col-sm-4', formatter: function (value, row, index) {
            return value.toString().split(" ")[0];
        }
    },
    {title: '操作', align: 'center', field: 'id', class: 'col-sm-4', formatter: review_option,}
]

function review_option(value, row, index) {
    const _detail = '<button class="btn btn-sm btn-info me-1" data-bs-toggle="modal" data-bs-target="#case-detail-modal" onclick="detailCase(\'' + row.id + '\')">查看</button>';
    const _edit = '<button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#ref-case-modal" onclick="loadReviewCase(\'' + row.id + '\')"><i class="bi-link"></i></button>';
    return _detail + _edit;
}

const search_params = function (params) {
    return {
        // projectName: $('#p-search').val().trim(), // 自定义查询条件
        pageSize: params.limit,
        pageNum: params.offset / params.limit + 1,
    }
};
InitTable('/case-review/reloadTable', 'get', _table_columns, search_params);
// 表格数据加载------

// 保存评审任务数据
$('#new_review_save').on('click', function (e) {
    const review_name = $('#review-name').val();
    const review_label = $('#review-label').val();
    const review_man = $('#review-man').val();
    const review_finish_date = $('#review-finish-date').val();
    const review_remark = $('#review-remark').val();
    const request_data = {
        "reviewName": review_name,
        "reviewLabel": review_label,
        "reviewMan": review_man,
        "reviewFinishDate": review_finish_date,
        "reviewRemark": review_remark,
    };
    if (review_name) {
        removeClass('#review-name', "is-invalid");
    } else {
        $('#review-name').addClass("is-invalid");
        return false;
    }

    if (review_man) {
        removeClass('#review-man', "is-invalid");
    } else {
        $('#review-man').addClass("is-invalid");
        return false;
    }

    if (review_finish_date) {
        removeClass('#review-finish-date', "is-invalid");
    } else {
        $('#review-finish-date').addClass("is-invalid")
        return false;
    }

    $.ajax({
        url: '/case-review/save-review',
        type: 'post',
        data: request_data,
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function (result) {
            if (Object.is(result['code'], 200)) {
                resetModal("#add-review-modal", "add-review-from");
                $('#add-review-modal').modal('hide');
                refresh_table();
            }
            showToast(result['code'], result['msg']);
        }
    });
});

$(function () {
    _table.on('check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        console.log(getSelections().length);
        $('#start-review').prop('disabled', !Object.is(getSelections().length, 1));
        $('#review-ref-case').prop('disabled', !Object.is(getSelections().length, 1));
    })
})

// 评审任务关联测试用例
function loadReviewCase(id) {
    $('#ref-case-modal').on('shown.bs.modal', function () {
        $('#ass-hidden-plan-id').val(id);
        $('#ass-search-name').val();
        $('#ass-search-title').val();
    })
    const reviewAssociateCaseTable = [
        {field: 'id', align: 'center', title: 'id', visible: false},
        {field: 'checked', checkbox: true, align: "center", width: '40'},
        {
            field: 'name',
            align: 'center',
            title: '用例标题',
            class: 'col-sm-4',
        }, {
            field: 'caseOwner',
            align: 'center',
            title: '维护人',
            class: 'col-sm-4',
        }, {
            field: 'reviewStatus',
            align: 'center',
            title: '评审状态',
            class: 'col-sm-4'
        }, {
            title: '操作', align: 'center', field: 'id', class: 'col-sm-4', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-sm btn-info hvr-grow" id="' + row.id + '" onclick="addReviewCase(\'' + row.id + '\')"><i class="bi-plus-circle"></i></button>'
            }
        }
    ]
    const reviewAssociateCaseParams = function (params) {
        return {
            // projectName: $('#p-search').val().trim(), // 自定义查询条件
            pageSize: params.limit,
            pageNum: params.offset / params.limit + 1,
        }
    };
    InitTable('/case/loadUnReviewCases', 'get', reviewAssociateCaseTable, reviewAssociateCaseParams, '#refCaseTable');
}


function addReviewCase(id) {
    let res = [];
    if (id) {
        res.push(id);
    }
    const params = {
        reviewId: $('#ass-hidden-plan-id').val(), caseId: JSON.stringify(res)
    };
    $.ajax({
        url: '/case-review-ref/save-ref/',
        type: 'post',
        data: params,
        contentType: 'application/x-www-form-urlencoded',
        success: function (arg) {
            showToast(arg['code'], arg['msg']);
            // 关联测试用例数据后重新加载一次列表数据
            $('#refCaseTable').bootstrapTable('refresh');
        }
    });
    $('#ref-case-modal').off('shown.bs.modal');
}

$('#review-modal').on('shown.bs.modal', function () {
    const selections = getSelections();
    const reviewId = selections[0].id;
    $('#hidden-review-id').val(reviewId);
    $.get('/case-review-ref/getRefCases/', {reviewId: reviewId}, function (result) {
        const records = result['data'];
        records.forEach(function (record) {
            console.log(record);
        });
        // $('#projectId').val(record.testCase.projectId)
        // $('#moduleId').val(record.testCase.moduleId)
    })
})