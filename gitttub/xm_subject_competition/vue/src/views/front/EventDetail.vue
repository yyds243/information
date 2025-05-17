<template>
  <div style="width: 60%; margin: 0 auto; padding: 20px 0; min-height: 100vh">
    <div class="card" style="margin-bottom: 20px">
      <div style="display: flex; grid-gap: 20px">
        <img :src="data.event.img" alt="" style="width: 400px; height: 400px">
        <div style="flex: 1">
          <div style="font-size: 25px; margin-bottom: 20px">{{ data.event.name }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">赛事简介：</span>{{  data.event.description }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">赛事类型：</span>{{  data.event.typeName }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">开始日期：</span>{{  data.event.start }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">结束日期：</span>{{  data.event.end }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">报名人数：</span>{{  data.event.signCount }}</div>
          <div style="margin-bottom: 10px"><span style="color: #666">主办方：</span>{{  data.event.hostName }}</div>
          <div style="margin-bottom: 80px"><span style="color: #666">赛事状态：</span>
            <span style="color: orange" v-if="data.event.status === '未开始'">{{data.event.status }}</span>
            <span style="color: #35c300" v-if="data.event.status === '进行中'">{{data.event.status }}</span>
            <span style="color: red" v-if="data.event.status === '已结束'">{{data.event.status }}</span>
          </div>
          <div>
            <el-button type="primary" style="width: 200px; height: 40px" @click="handleSign" :disabled="data.event.status !== '未开始'">立即报名</el-button>
            <el-button type="info" style="width: 200px; height: 40px" @click="collect" v-if="!data.event.userCollect">收藏</el-button>
            <el-button type="danger" style="width: 200px; height: 40px" @click="collect" v-if="data.event.userCollect">取消收藏</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="card" style="padding: 20px; margin-bottom: 20px">
      <div style="margin-bottom: 30px; font-size: 25px; padding-bottom: 10px; border-bottom: 1px solid #ddd;">奖项设置</div>
      <div>
        <span v-for="item in data.awardList" :key="item.id" style="margin-right: 40px"><b>{{ item.name }}</b>：{{ item.money }}元</span>
      </div>
    </div>

    <div class="card" style="padding: 20px; margin-bottom: 50px">
      <div style="margin-bottom: 30px; font-size: 25px; padding-bottom: 10px; border-bottom: 1px solid #ddd;">赛事详情</div>
      <div style="line-height: 24px">
        <div v-html="data.event.content"></div>
      </div>
    </div>

    <el-dialog title="赛事报名" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" :rules="data.rules" label-width="100px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="data.form.phone" placeholder="联系方式"></el-input>
        </el-form-item>
        <el-form-item label="资格证书" prop="certificate">
          <el-checkbox-group v-model="data.form.certificate" v-show="false"></el-checkbox-group>
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="add">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

import { ref } from "vue"
const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  event: {},
  form: {},
  awardList: [],
  formVisible: false,
  rules: {
    phone: [
      { required: true, message: '请输入联系方式', trigger: 'blur' }
    ],
    certificate: [
      {  required: true, message: '请上传资格证书', trigger: 'change' }
    ],
  }
})

const collect = () => {
  request.post('/collect/add', { userId: data.user.id, eventId: data.id }).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.certificate = res.data
}

request.get('/award/selectAll', {
  params: {
    eventId: data.id
  }
}).then(res => {
  data.awardList = res.data
})

const load = () => {
  request.get('/event/selectById/' + data.id).then(res => {
    data.event = res.data
  })
}
load()

const handleSign = () => {
  data.form = {}
  data.formVisible = true
}

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.userId = data.user.id
      data.form.hostId = data.event.hostId
      data.form.eventId = data.id
      data.form.status = '待审核'
      request.post('/sign/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('报名成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style>

</style>