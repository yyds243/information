<template>
  <div class="main-content" style="width: 60%">
    <div class="page-title">我的提现记录</div>
    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 20px;">
        <div style="margin-bottom: 20px">
          <el-button type="primary" @click="handleAdd">提现</el-button>
          <span style="margin-left: 20px">当前账户余额：{{ data.account }}元</span>
        </div>
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column prop="money" label="提现金额"></el-table-column>
          <el-table-column prop="type" label="账户类型"></el-table-column>
          <el-table-column prop="account" label="提现账号"></el-table-column>
          <el-table-column prop="time" label="提现时间"></el-table-column>
        </el-table>

      </div>
      <div v-if="data.total">
        <el-pagination @current-change="load" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>

    <el-dialog title="发起提现" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" :rules="data.rules" label-width="80px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="提现金额" prop="money">
          <el-input-number style="width: 200px" :min="1" :max="data.account" v-model="data.form.money" placeholder="提现金额"></el-input-number>
        </el-form-item>
        <el-form-item label="提现账户" prop="account">
          <el-input v-model="data.form.account" placeholder="提现账户"></el-input>
        </el-form-item>
        <el-form-item label="账户类型" prop="type" style="margin-top: 30px">
          <el-radio-group v-model="data.form.type">
            <el-radio value="微信支付"><img style="width: 100px" src="@/assets/imgs/微信支付.png" alt=""></el-radio>
            <el-radio value="支付宝"><img style="width: 100px" src="@/assets/imgs/支付宝.png" alt=""></el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

import {ref} from "vue"
const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  ids: [],
  name: null,
  account: 0,
  rules: {
    money: [
      { required: true, message: '请输入金额', trigger: 'blur' }
    ],
    account: [
      { required: true, message: '请输入提现账户', trigger: 'blur' }
    ],
  }
})

const loadAccount = () => {
  request.get('/user/selectById/' + data.user.id).then(res => {
    data.account = res.data.account || 0
  })
}
loadAccount()

// 加载表格数据
const load = () => {
  request.get('/drawCash/selectPage', {
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

// 打开新增弹窗
const handleAdd = () => {
  data.form = { type: '微信支付' }
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增
const add = () => {
  data.form.userId = data.user.id
  request.post('/drawCash/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
      loadAccount()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put('/drawCash/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
      loadAccount()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/drawCash/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

// 批量删除
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/drawCash/delete/batch', {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()  // 刷新表格数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => console.log(err))
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const reset = () => {
  data.name = null
  load()
}

load()

</script>