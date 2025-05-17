<template>
  <div class="main-content">
    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 30px;">
        <el-input v-model="data.eventName" clearable @clear="load" placeholder="请输入赛事名称查询" style="width: 400px; height: 40px"></el-input>
        <el-button type="info" plain style="margin-left: 10px; height: 40px" @click="load">查询</el-button>
      </div>

      <div style="margin-bottom: 20px;">
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column prop="eventName" label="赛事名称" show-overflow-tooltip>
            <template #default="scope">
              <a target="_blank" style="color: #005dff" :href="'/front/eventDetail?id=' + scope.row.eventId">{{ scope.row.eventName }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="联系方式"></el-table-column>
          <el-table-column prop="certificate" label="资格证书">
            <template #default="scope">
              <el-image style="width: 80px; height: 50px; border-radius: 5px" :src="scope.row.certificate" :preview-src-list="[scope.row.certificate]" preview-teleported></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template v-slot="scope">
              <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
              <el-tag type="warning" v-if="scope.row.status === '待审核'">待审核</el-tag>
              <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
              <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="审核意见"></el-table-column>
          <el-table-column prop="time" label="报名时间" width="160"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template v-slot="scope">
              <el-button v-if="scope.row.status === '待审核'" type="danger" size="small" @click="changeStatus(scope.row, '已取消')">取消</el-button>
            </template>
          </el-table-column>
        </el-table>

      </div>
      <div v-if="data.total">
        <el-pagination @current-change="load" layout="total,prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>


    <el-dialog title="审核" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="审核意见" prop="reason">
          <el-input type="textarea" :rows="3" v-model="data.form.reason" placeholder="审核意见"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="赛事结果" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="获奖状态" prop="status">
          <el-select v-model="data.form.status">
            <el-option label="未获奖" value="未获奖"></el-option>
            <el-option label="已获奖" value="已获奖"></el-option>
            <el-option label="取消资格" value="取消资格"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="奖项" prop="awardId">
          <el-select style="width: 100%" v-model="data.form.awardId">
            <el-option v-for="item in data.awardList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="颁奖日期" prop="date">
          <el-date-picker style="width: 100%" v-model="data.form.date" type="date" placeholder="颁奖日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="领奖地址" prop="address">
          <el-input type="textarea" :rows="3" v-model="data.form.address" placeholder="领奖地址"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="data.form.phone" placeholder="主办方联系方式"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="saveResult">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

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
  eventName: null,
  awardList: [],
  rules: {
    status: [
      {required: true, message: '请选择结果', trigger: 'change'}
    ],
  }
})


const handleResult = (row) => {
  data.formVisible = true
  data.form = { eventId: row.id, userId: data.user.id }

  request.get('/award/selectAll', {
    params: {
      eventId: row.id
    }
  }).then(res => {
    data.awardList = res.data
  })
}

const saveResult = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/eventResult/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

// 加载表格数据
const load = () => {
  request.get('/sign/selectPage', {
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
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = '拒绝'
  data.formVisible = true
}

// 新增
const add = () => {
  request.post('/sign/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const changeStatus = (row, status) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = status
  ElMessageBox.confirm('您确定' + status + '吗？', '二次确认', {type: "warning"}).then(response => {
    request.put('/sign/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)  // 弹出错误的信息
      }
    })
  }).catch(e => {})
}

// 更新
const update = () => {
  request.put('/sign/update', data.form).then(res => {
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
    request.delete('/sign/delete/' + id).then(res => {
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
    request.delete('/sign/delete/batch', {data: data.ids}).then(res => {
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
  load()
}

load()

</script>