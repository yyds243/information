<template>
  <div class="main-content">
    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 30px;">
        <el-input v-model="data.eventName" clearable @clear="load" placeholder="请输入赛事名称查询" style="width: 400px; height: 40px"></el-input>
        <el-button type="info" plain style="margin-left: 10px; height: 40px" @click="load">查询</el-button>
      </div>

      <div style="margin-bottom: 20px;">
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column prop="eventName" label="赛事名称">
            <template #default="scope">
              <a target="_blank" style="color: #005dff" :href="'/front/eventDetail?id=' + scope.row.eventId">{{ scope.row.eventName }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="awardName" label="奖项名称"></el-table-column>
          <el-table-column prop="date" label="颁奖日期"></el-table-column>
          <el-table-column prop="address" label="领奖地址" show-overflow-tooltip></el-table-column>
          <el-table-column prop="phone" label="联系方式"></el-table-column>
          <el-table-column prop="status" label="状态">
            <template v-slot="scope">
              <el-tag type="warning" v-if="scope.row.status === '未获奖'">未获奖</el-tag>
              <el-tag type="success" v-if="scope.row.status === '已获奖'">已获奖</el-tag>
              <el-tag type="danger" v-if="scope.row.status === '取消资格'">取消资格</el-tag>
            </template>
          </el-table-column>
        </el-table>

      </div>
      <div v-if="data.total">
        <el-pagination @current-change="load" layout="total,prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  ids: [],
  eventName: null,
  userName: null,
  awardList: []
})

// 加载表格数据
const load = () => {
  request.get('/eventResult/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      eventName: data.eventName,
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
  data.form = {}
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  request.get('/award/selectAll', {
    params: {
      eventId: row.id
    }
  }).then(res => {
    data.awardList = res.data
    data.form = JSON.parse(JSON.stringify(row))
    data.formVisible = true
  })

}

// 新增
const add = () => {
  request.post('/eventResult/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put('/eventResult/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/eventResult/delete/' + id).then(res => {
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
    request.delete('/eventResult/delete/batch', {data: data.ids}).then(res => {
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
  data.form.id ? update() : add()
}

const reset = () => {
  data.eventName = null
  data.userName = null
  load()
}

load()

</script>