<template>
  <div class="user-management">
    <el-card>
      <div class="header">
        <el-button type="primary" @click="handleAdd">
          添加用户
        </el-button>
      </div>

      <!-- 用户表格 -->
      <el-table :data="users" border style="width: 100%">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="role" label="角色" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 用户编辑对话框 -->
      <UserDialog 
        v-model="dialogVisible"
        :current-user="currentUser"
        :dialog-type="dialogType"
        @submit="handleSubmit"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import UserDialog from '@/components/control/UserDialog.vue'

const users = ref([
  { id: 1, username: 'admin', role: '管理员', email: 'admin@example.com' }
])

const dialogVisible = ref(false)
const currentUser = ref(null)
const dialogType = ref('add')

const handleAdd = () => {
  dialogType.value = 'add'
  currentUser.value = null
  dialogVisible.value = true
}

const handleEdit = (user) => {
  dialogType.value = 'edit'
  currentUser.value = { ...user }
  dialogVisible.value = true
}

const handleSubmit = (userData) => {
  if (dialogType.value === 'add') {
    users.value.push({ ...userData, id: Date.now() })
  } else {
    const index = users.value.findIndex(u => u.id === userData.id)
    users.value.splice(index, 1, userData)
  }
}

const handleDelete = (user) => {
  users.value = users.value.filter(u => u.id !== user.id)
}
</script>