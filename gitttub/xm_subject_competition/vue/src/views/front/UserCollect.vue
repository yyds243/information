<template>
  <div style="width: 70%; margin: 0 auto; padding: 20px 0; min-height: 100vh">
    <div class="page-title">我收藏的赛事</div>
    <div style="margin-bottom: 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in data.tableData" :key="item.id">
          <div style="margin-bottom: 20px; cursor: pointer" @click="router.push('/front/eventDetail?id=' + item.eventId)">
            <img :src="item.eventImg" alt="" style="width: 100%; height: 260px; display: block; border-radius: 5px">
            <div style="margin: 15px 0; font-size: 20px; font-weight: 500; line-height: 20px; height: 20px" class="line1">{{ item.eventName }}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div v-if="data.total">
      <el-pagination @current-change="load" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
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
  pageSize: 8,  // 每页的个数
  name: null
})

// 加载数据
const load = () => {
  request.get('/collect/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id
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