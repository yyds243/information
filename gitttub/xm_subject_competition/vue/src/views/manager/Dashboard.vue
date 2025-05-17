<template>
  <div>
    <div style="display: flex; align-items: center; grid-gap: 10px; margin-bottom: 10px">
      <div style="flex: 1">
        <div class="card" style="display: flex; font-size: 20px; text-align: center; padding: 20px 0">
          <div style="flex: 1">赛事数量</div>
          <div style="flex: 1;  font-weight: bold; color: #005dff">{{ data.count.event }}</div>
        </div>
      </div>
      <div style="flex: 1">
        <div class="card" style="display: flex; font-size: 20px; text-align: center; padding: 20px 0">
          <div style="flex: 1">报名总数</div>
          <div style="flex: 1; font-weight: bold; color: #ff5600">{{ data.count.sign }}</div>
        </div>
      </div>
      <div style="flex: 1">
        <div class="card" style="display: flex; font-size: 20px; text-align: center; padding: 20px 0">
          <div style="flex: 1">用户总数</div>
          <div style="flex: 1;  font-weight: bold; color: #6ad500">{{ data.count.user }}</div>
        </div>
      </div>
      <div style="flex: 1">
        <div class="card" style="display: flex; font-size: 20px; text-align: center; padding: 20px 0">
          <div style="flex: 1">主办方总数</div>
          <div style="flex: 1;  font-weight: bold; color: #4333e3">{{ data.count.host }}</div>
        </div>
      </div>
    </div>
    <div style="display: flex; grid-gap: 10px">
      <div style="flex: 1; padding: 20px" class="card">
        <div id="bar" style="height: 400px"></div>
      </div>
      <div style="flex: 1; padding: 20px" class="card">
        <div id="pie" style="height: 400px"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from "vue";
import * as echarts from 'echarts'
import request from "@/utils/request.js";

const data = reactive({
  count: {}
})

request.get('/selectCount').then(res => {
  data.count = res.data
})

const barOption = {
  title: {
    text: '分类赛事数量统计',
    subtext: '柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  grid: {
    left: '15%',
    bottom:'15%'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      interval: 0,
      rotate: 45
    }
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      smooth: true,
      itemStyle: {
        normal: {
          color: function(params) { // 根据索引返回对应的颜色
            let colorList = ['#ffaa2e','#32C5E9','#fa4c4c','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ]
}

const pieOption = {
  title: {
    text: '赛事报名统计',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['65%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}
// 等页面所有元素加载完成后再设置 echarts图表
onMounted(() => {
  // 请求数据  初始化图表

  // 柱状图
  let barDom = document.getElementById('bar')
  let barChart = echarts.init(barDom)

// 饼图
  let pieDom = document.getElementById('pie')
  let pieChart = echarts.init(pieDom)

  request.get('/selectBar').then(res => {
    barOption.xAxis.data = res.data.type || []
    barOption.series[0].data = res.data.count || []
    barChart.setOption(barOption)
  })

  request.get('/selectPie').then(res => {
    pieOption.series[0].data = res.data || []
    pieChart.setOption(pieOption)
  })
})
</script>