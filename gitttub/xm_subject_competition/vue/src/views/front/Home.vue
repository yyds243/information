<template>
  <div class="main-content">
    <div style="margin-bottom: 40px">
      <el-carousel height="400px">
        <el-carousel-item v-for="item in data.carouselList" :key="item">
          <img :src="item.img" alt="" style="width: 100%; cursor: pointer; display: block" @click="router.push('/front/eventDetail?id=' + item.eventId)">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin-bottom: 40px">
      <div style="text-align: center; margin-bottom: 20px">
        <div style="font-size: 30px; margin-bottom: 10px; font-weight: 500">热 门 竞 赛</div>
        <div style="color: #666">赛出风格 赛出精彩</div>
      </div>
      <div style="text-align: right; margin-bottom: 20px"><b style="color: #005dff; cursor: pointer" @click="router.push('/front/event')">更多赛事</b></div>
      <div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.eventList" :key="item.id">
            <div style="margin-bottom: 20px; cursor: pointer" @click="router.push('/front/eventDetail?id=' + item.id)">
              <img :src="item.img" alt="" style="width: 100%; height: 260px; display: block; border-radius: 5px">
              <div style="margin: 15px 0; font-size: 20px; font-weight: 500; line-height: 20px; height: 20px" class="line1">{{ item.name }}</div>
              <div style="display: flex; align-items: center;">
                <div style="flex: 1; color: #666; display: flex; align-items: center">
                  <el-tag type="info">{{ item.typeName }}</el-tag>
                </div>
                <div style="font-size: 13px; color: #666; display: flex; align-items: center">
                  报名人数：{{ item.signCount }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div style="margin-bottom: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <div style="font-size: 30px; margin-bottom: 10px; font-weight: 500">热 门 资 讯</div>
        <div style="color: #666">为您传递一手赛事资讯</div>
      </div>
      <div style="text-align: right; margin-bottom: 20px"><b style="color: #585ffb; cursor: pointer" @click="router.push('/front/news')">更多资讯</b></div>
      <div>
        <div style="display: flex; grid-gap: 20px">
          <div style="flex: 1; cursor: pointer" @click="router.push('/front/newsDetail?id=' +  data.newsList[0].id)" v-if="data.newsList?.length">
            <img style="display: block; width: 100%; height: 500px; border-radius: 10px" :src="data.newsList[0].img" alt="">
            <div style="margin: 10px 0; font-size: 20px; font-weight: 500;">{{ data.newsList[0].title }}</div>
            <div style="color: #666; font-size: 13px; line-height: 20px; height: 40px" class="line2">{{ data.newsList[0].description }}</div>
          </div>
          <div style="flex: 1" v-if="data.newsList?.length > 1">
            <el-row :gutter="20">
              <el-col :span="12" v-for="item in data.newsList.slice(1, 5)" :key="item.id">
                <div style="margin-bottom: 20px; cursor: pointer" @click="router.push('/front/newsDetail?id=' + item.id)">
                  <img style="display: block; width: 100%; height: 200px; border-radius: 5px" :src="item.img" alt="">
                  <div style="margin: 10px 0; font-size: 20px; font-weight: 500; line-height: 20px; height: 20px" class="line1">{{item.title }}</div>
                  <div style="color: #666; font-size: 13px; line-height: 20px; height: 40px" class="line2">{{ item.description }}</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  carouselList: [],
  eventList: [],
  newsList: [],
})

request.get('/carousel/selectAll').then(res => {
  data.carouselList = res.data
})

request.get('/news/selectAll', {
  params: { status: '通过' }
}).then(res => {
  data.newsList = res.data
})

request.get('/event/selectHot').then(res => {
  data.eventList = res.data
})
</script>