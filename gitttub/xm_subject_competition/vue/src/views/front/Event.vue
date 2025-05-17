<template>
  <div style="width: 70%; margin: 0 auto; padding: 20px 0; min-height: 100vh">
    <div style="margin-bottom: 30px; display: flex; align-items: center">
      <div style="flex: 1">
        <div style="display: flex; align-items: center">
          <div @click="changeCategory(null)" style="padding-bottom: 5px; margin-right: 20px; cursor: pointer" :class="{'category-active' : data.activeCategoryId === null }">全部</div>
          <div @click="changeCategory(item.id)" style="padding-bottom: 5px; margin-right: 20px; cursor: pointer" :class="{'category-active' : data.activeCategoryId === item.id }" v-for="item in data.categoryList" :key="item.id">{{ item.name }}</div>
        </div>
      </div>
      <div>
        <el-input clearable @clear="load" style="width: 400px; height: 40px" v-model="data.name" placeholder="请输入赛事名称搜索"></el-input>
        <el-button type="primary" style="height: 40px; margin-left: 10px" @click="load">搜 索</el-button>
      </div>
    </div>
    <el-row :gutter="20">
      <el-col :span="6" style="margin-bottom: 20px" v-for="item in data.tableData" :key="item.id">
        <div class="card item" style="padding: 0; cursor: pointer" @click="router.push('/front/eventDetail?id=' + item.id)">
          <img style="width: 100%; height: 260px; border-radius: 5px 5px 0 0" :src="item.img" alt="">
          <div style="padding: 10px">
            <div style="font-size: 18px; margin-bottom: 5px">{{ item.name }}</div>
            <div style="display: flex; align-items: center">
              <div style="flex: 1">
                <el-tag type="info">{{ item.typeName }}</el-tag>
              </div>
              <div v-if="item.status === '未开始'" style="color: orange">{{ item.status }}</div>
              <div v-if="item.status === '进行中'" style="color: #35c300">{{ item.status }}</div>
              <div v-if="item.status === '已结束'" style="color: red">{{ item.status }}</div>
            </div>

          </div>
        </div>
      </el-col>
    </el-row>

    <div v-if="data.total" style="margin-top: 20px; margin-bottom: 50px">
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
  pageSize: 8,  // 每页的个数
  name: null,
  activeCategoryId: null,
  categoryList: []
})

request.get('/type/selectAll').then(res => {
  data.categoryList = res.data
})
const changeCategory = (categoryId) => {
  data.activeCategoryId = categoryId
  load()
}

// 加载数据
const load = () => {
  request.get('/event/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      typeId: data.activeCategoryId
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
.category-active {
  color: #006cff;
  border-bottom: 2px solid #006cff;
}
</style>