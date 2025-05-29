<template>
  <div class="user-management">
    <el-card>
      <div class="header">
        <el-button type="primary" @click="handleAdd"> 添加用户 </el-button>
      </div>

      <!-- 用户表格 - 显示ID和用户名 -->
      <el-table :data="users" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column label="是否可用">
          <template #default="{ row }">
            {{ row.action === true ? '是' : '否' }}
          </template>
        </el-table-column>
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
import { ref, onMounted } from "vue";
import axios from "axios";
import CryptoJS from "crypto-js";
import UserDialog from "@/components/control/UserDialog.vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

// store
const store = useStore();

const API_HOST = import.meta.env.VITE_API_HOST;
const users = ref([]);
const dialogVisible = ref(false);
const currentUser = ref(null);
const dialogType = ref("add");

// 获取用户列表
const fetchUsers = async () => {
  try {
    const response = await axios.post(
      `${API_HOST}/user/list`,
      {},
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );
    users.value = response.data.data;
  } catch (error) {
    console.error("获取用户列表失败:", error);
    ElMessage.error("获取用户列表失败");
  }
};

// 初始化获取用户数据
onMounted(() => {
  fetchUsers();
});

const handleAdd = () => {
  dialogType.value = "add";
  currentUser.value = null;
  dialogVisible.value = true;
};

const handleEdit = (user) => {
  dialogType.value = "edit";
  currentUser.value = { ...user };
  dialogVisible.value = true;
};

// 处理提交（新增/编辑）
const handleSubmit = async (userData) => {
  try {
    // 密码哈希处理
    if (userData.password) {
      userData.passwordHash = CryptoJS.SHA256(userData.password).toString();
    }

    if (dialogType.value === "add") {
      // 新增用户
      await axios.post(
        `${API_HOST}/user/register`,
        {
          username: userData.username,
          passwordHash: userData.passwordHash,
        },
        {
          headers: {
            Authorization: `Bearer ${store.state.jwtToken}`,
          },
        }
      );
      ElMessage.success("用户添加成功");
    } else {
      // 编辑用户
      await axios.post(
        `${API_HOST}/user/update`,
        {
          id: userData.id,
          username: userData.username,
          passwordHash: userData.passwordHash,
          email: userData.email,
          action: userData.action
        },
        {
          headers: {
            Authorization: `Bearer ${store.state.jwtToken}`,
          },
        }
      );
      ElMessage.success("用户更新成功");
    }

    dialogVisible.value = false;
    fetchUsers(); // 刷新用户列表
  } catch (error) {
    console.error("操作失败:", error);
    ElMessage.error("操作失败");
  }
};

// 删除用户
const handleDelete = async (user) => {
  try {
    await axios.post(
      `${API_HOST}/user/delete`,
      { id: user.id },
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );
    ElMessage.success("用户删除成功");
    fetchUsers(); // 刷新用户列表
  } catch (error) {
    console.error("删除失败:", error);
    ElMessage.error("删除失败");
  }
};
</script>