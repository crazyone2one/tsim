const case_chart = echarts.init(document.getElementById('case_echarts'));
const bug_chart = echarts.init(document.getElementById('bug_echarts'));

const tempXAxi = JSON.parse($("#xAxisList").val())[0].result;

function loadCaseByProject(echarts_flag) {
    $.ajax({
        async: false,
        type: 'POST',
        url: '/getAnalysis',
        success: function (result) {
            let t_option;
            if (Object.is(200, result['code'])) {
                const series = [];
                let map;
                let text;
                if (Object.is("case", echarts_flag)) {
                    map = new Map(Object.entries(new Map(Object.entries(result['data'])).get("case")));
                    text = "测试用例变化情况";
                }
                if (Object.is("bug", echarts_flag)) {
                    map = new Map(Object.entries(new Map(Object.entries(result['data'])).get("bug")));
                    text = "问题单变化情况";
                }
                for (let proj of map.keys()) {
                    let tempData = map.get(proj);
                    series.push({name: proj, type: 'line', data: JSON.parse(tempData)[0].result});
                }
                // 测试统计图
                t_option = {
                    title: {
                        text: text
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    // legend: {
                    //     data: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
                    // },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: tempXAxi
                    },
                    yAxis: {
                        type: 'value',
                        minInterval: 1 // 只显示整数
                    },
                    series: series
                };
                if (Object.is("case", echarts_flag)) {
                    case_chart.setOption(t_option);
                    window.onresize=function () {
                        // $(case_chart).width($('#case_echarts').width());
                        case_chart.resize();
                    }
                }
                if (Object.is("bug", echarts_flag)) {
                    bug_chart.setOption(t_option);
                    window.onresize=function () {
                        // $(case_chart).width($('#case_echarts').width());
                        bug_chart.resize();
                    }
                }
            }
        }
    })
}

$(function () {
    loadCaseByProject("case");
    loadCaseByProject("bug");
})
