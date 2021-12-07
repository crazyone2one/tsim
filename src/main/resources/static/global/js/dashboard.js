const chart1 = echarts.init(document.getElementById('issueStatistics'));
const chart2 = echarts.init(document.getElementById('demo1'));
const tempXAxi = JSON.parse($("#xAxisList").val())[0].data;
const tempCount = JSON.parse($("#yAxisList").val())[0].data;
// 指定图表的配置项和数据
const option = {
  tooltip: {},
  legend: {},
  xAxis: {
    data: tempXAxi
  },
  yAxis: {
    minInterval: 1 // 只显示整数
  },
  series: [{
    name: '新增',
    type: 'bar',
    data: tempCount
  }]
};
chart1.setOption(option);
const c2option = {
  tooltip: {},
  legend: {},
  xAxis: {
    data: tempXAxi
  },
  yAxis: {
    minInterval: 1 // 只显示整数
  },
  series: [{
    name: '新增',
    type: 'bar',
    data: tempCount
  }]
};
chart2.setOption(c2option)
