<template>
  <div style="width: 60%; margin: 0 auto; padding: 20px 0; min-height: 100vh">

    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 40px">
        <div style="font-size: 25px; font-weight: 500; margin-bottom: 10px; text-align: center">{{ data.news.title }}</div>
        <div style="text-align: center; color: #666">
          <span style="margin-right: 20px">阅读量：{{ data.news.readCount }}</span>
          <span>发布时间：{{ data.news.time }}</span>
        </div>
      </div>
      <div style="padding: 0 20px; text-align: justify; line-height: 24px">
        <div v-html="data.news.content"></div>
      </div>
    </div>


  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  news: {}
})


request.get('/news/selectById/' + data.id).then(res => {
  data.news = res.data
})
</script>

<style>

</style>