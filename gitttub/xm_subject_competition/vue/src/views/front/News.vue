<template>
  <div style="width: 60%; margin: 0 auto; padding: 20px 0; min-height: 100vh">
    <div style="margin-bottom: 20px">
      <el-input clearable @clear="load" style="width: 400px; height: 40px" v-model="data.title" placeholder="请输入标题关键字搜索"></el-input>
      <el-button type="primary" style="height: 40px; margin-left: 10px" @click="load">搜 索</el-button>
    </div>
    <div>
      <div v-for="item in data.tableData" :key="item.id">
        <div class="card" style="display: flex; grid-gap: 20px; margin-bottom: 10px">
          <img :src="item.img" alt="" style="display: block; width: 200px; height: 130px; border-radius: 5px">
          <div style="flex: 1; cursor: pointer" @click="router.push('/front/newsDetail?id=' + item.id)">
            <div style="font-size: 20px; font-weight: 500;">{{ item.title }}</div>
            <div style="color: #666; margin: 15px 0; height: 40px; line-height: 20px" class="line2">{{ item.description }}</div>
            <div style="display: flex; align-items: center; color: #666">
              <el-icon size="16"><View /></el-icon><span style="margin-left: 5px">{{ item.readCount }}</span>
              <el-icon style="margin-left: 20px" size="16"><Clock /></el-icon><span style="margin-left: 5px">{{ item.time }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="data.total" style="margin-bottom: 50px; margin-top: 20px">
      <el-pagination @current-change="load" layout="total,prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  title: null
})

// 加载数据
const load = () => {
  request.get('/news/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      status: '通过'
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>

<style scoped>
.item {
  transition: all 0.5s;
}
.item:hover {
  scale: 1.05;
}
</style>