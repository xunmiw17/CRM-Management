layui.use(['layer', 'echarts'], function () {
    let $ = layui.jquery,
        echarts = layui.echarts;

    $.ajax({
        type: "get",
        url: "/crm" + "/customer/countCustomerMakeup",
        dataType: "json",
        success: function (data) {
            let myChart = echarts.init(document.getElementById("make"));
            let option = {
                title: {
                    text: '客户构成分析'
                },
                tooltip: {},
                xAxis: {
                    type:'category',
                    data: data.xAxis
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: data.yAxis,
                    type: 'line'
                }]
            };
            myChart.setOption(option);
        }
    });

    $.ajax({
        type: "get",
        url: "/crm" + "/customer/countCustomerMakeup02",
        dataType: "json",
        success: function (data) {
            let myChart = echarts.init(document.getElementById("make02"));

            let option = {
                title: {
                    text: "客户构成分析",
                    subtext: "来自CRM",
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b} : {c} ({d}%'
                },
                legend: {
                    left: 'center',
                    top: 'bottom',
                    data: data.xAxis
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {
                            show: true,
                            type: ['pie', 'funnel']
                        },
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                series: [
                    {
                        name: '',
                        type: 'pie',
                        radius: [20, 100],
                        center: ['25%', '50%'],
                        roseType: 'radius',
                        label: {
                            show: false
                        },
                        emphasis: {
                            label: {
                                show: true
                            }
                        },
                        data: data.yAxis
                    },
                    {
                        name: '',
                        type: 'pie',
                        radius: [30, 110],
                        center: ['75%', '50%'],
                        roseType: 'area',
                        data: data.yAxis
                    }
                ]
            };

            myChart.setOption(option);
        }
    })
});