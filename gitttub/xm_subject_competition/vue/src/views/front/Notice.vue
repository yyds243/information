<template>
  <div class="main-content" style="width: 50%">
    <div class="card" style="padding: 20px">
      <div style="font-weight: bold; font-size: 20px; margin-bottom: 40px">系统公告</div>
      <el-timeline style="max-width: 600px">
        <el-timeline-item
            color="#fab75a"
            v-for="(item, index) in data.noticeData"
            :key="index"
            placement="top"
            :timestamp="item.time"
        >
          <div style="font-size: 16px; font-weight: bold; margin-bottom: 10px">{{ item.title }}</div>
          <div>{{ item.content }}</div>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>