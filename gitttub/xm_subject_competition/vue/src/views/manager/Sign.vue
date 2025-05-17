<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.eventName" placeholder="请输入赛事名称查询" style="width: 240px; margin-right: 10px"></el-input>
      <el-input v-model="data.userName" placeholder="请输入用户名称查询" style="width: 240px"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'ADMIN'">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" v-if="data.user.role === 'ADMIN'"></el-table-column>
        <el-table-column prop="eventName" label="赛事名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="用户名称"></el-table-column>
        <el-table-column prop="phone" label="联系方式"></el-table-column>
        <el-table-column prop="certificate" label="资格证书">
          <template #default="scope">
            <el-image style="width: 80px; height: 50px; border-radius: 5px" :src="scope.row.certificate" :preview-src-list="[scope.row.certificate]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="warning" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="审核意见"></el-table-column>
        <el-table-column prop="time" label="报名时间" width="160"></el-table-column>
        <el-table-column label="审核" width="150" v-if="data.user.role === 'HOST'">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === '待审核'" size="small" type="success" @click="changeStatus(scope.row, '通过')">通过</el-button>
            <el-button v-if="scope.row.status === '待审核'" size="small" type="danger" @click="handleEdit(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" size="small" @click="handleResult(scope.row)" v-if="data.user.role === 'HOST' && scope.row.status === '通过'" :disabled="scope.row.result">设置结果</el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)" v-if="data.user.role === 'ADMIN'"></el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total,prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
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
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="赛事结果" v-model="data.formVisible1" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form1" label-width="100px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="获奖状态" prop="status">
          <el-select v-model="data.form1.status">
            <el-option label="未获奖" value="未获奖"></el-option>
            <el-option label="取消资格" value="取消资格"></el-option>
            <el-option label="已获奖" value="已获奖"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="奖项" prop="awardId" v-if="data.form1.status === '已获奖'">
          <el-select style="width: 100%" v-model="data.form1.awardId">
            <el-option v-for="item in data.awardList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="颁奖日期" prop="date" v-if="data.form1.status === '已获奖'">
          <el-date-picker style="width: 100%" v-model="data.form1.date" type="date" placeholder="颁奖日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="领奖地址" prop="address" v-if="data.form1.status === '已获奖'">
          <el-input type="textarea" :rows="3" v-model="data.form1.address" placeholder="领奖地址"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone" v-if="data.form1.status === '已获奖'">
          <el-input v-model="data.form1.phone" placeholder="主办方联系方式"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible1 = false">取消</el-button>
          <el-button type="primary" @click="saveResult">确定</el-button>
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
  formVisible1: false,
  form: {},
  form1: {},
  ids: [],
  eventName: null,
  userName: null,
  awardList: [],
  rules: {
    status: [
      {required: true, message: '请选择结果', trigger: 'change'}
    ],
  }
})


const handleResult = (row) => {
  request.get('/eventResult/selectUserResult', {
    params: {
      userId: row.userId,
      eventId: row.eventId
    }
  }).then(res => {
    data.formVisible1 = true
    data.form1 = res.data || { eventId: row.eventId, userId: row.userId, hostId: row.hostId, status: '未获奖' }
  })

  request.get('/award/selectAll', {
    params: {
      eventId: row.eventId
    }
  }).then(res => {
    data.awardList = res.data
  })
}

const saveResult = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/eventResult/add', data.form1).then(res => {
        if (res.code === '200') {
          ElMessage.success('设置成功')
          data.formVisible1 = false
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
      userName: data.userName,
      hostId: data.user.role === "HOST" ? data.user.id : null
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
  ElMessageBox.confirm('您确定审核' + status + '吗？', '确认审核', {type: "warning"}).then(response => {
    request.put('/sign/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('审核成功')
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
  data.userName = null
  load()
}

load()

</script>