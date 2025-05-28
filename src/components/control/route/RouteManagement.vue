<template>
  <div class="route-management">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="showAddDialog">新增线路</el-button>
      <el-button
        type="danger"
        :disabled="selectedRoutes.length === 0"
        @click="handleBatchDelete"
      >
        批量删除（已选 {{ selectedRoutes.length }} 项）
      </el-button>
    </div>

    <!-- 新增线路对话框 -->
    <el-dialog v-model="dialogVisible" title="新增线路" width="500px">
      <el-form 
        :model="formData" 
        :rules="formRules"
        ref="formRef"
        label-width="80px"
      >
        <el-form-item label="线路名称" prop="longName">
          <el-input 
            v-model="formData.longName" 
            placeholder="请输入线路名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入线路描述"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 数据表格 -->
    <el-table
      :data="routeList"
      style="width: 100%"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="longName" label="线路名称" />
      <el-table-column prop="description" label="描述" />
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      class="pagination"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 50]"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="fetchRouteData"
      @current-change="fetchRouteData"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useStore } from "vuex";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

const API_HOST = "http://localhost:8080/";
const store = useStore();
const router = useRouter();

// 响应式数据
const routeList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const selectedRoutes = ref([]);
const dialogVisible = ref(false);
const formRef = ref(null);
const formData = ref({
  longName: '',
  description: ''
});

// 表单验证规则
const formRules = {
  longName: [
    { required: true, message: '请输入线路名称', trigger: 'blur' },
    { min: 2, max: 155, message: '长度在2到255个字符', trigger: 'blur' }
  ],
  description: [
    { max: 255, message: '长度不超过255个字符', trigger: 'blur' }
  ]
};

// 显示新增对话框
const showAddDialog = () => {
  formData.value = { longName: '', description: '' };
  dialogVisible.value = true;
};

// 处理新增提交
const handleAddSubmit = async () => {
  try {
    // 验证表单
    const valid = await formRef.value.validate();
    if (!valid) return;

    const response = await axios.post(
      API_HOST + 'route/append',
      {
        longName: formData.value.longName,
        description: formData.value.description
      },
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`
        }
      }
    );

    if (response.data.code === 200) {
      ElMessage.success('新增成功');
      dialogVisible.value = false;
      await fetchRouteData(); // 刷新数据
    } else if (response.data.message === "NOT_LOGIN") {
      await handleNotLogin();
    } else {
      ElMessage.error(response.data.message || '新增失败');
    }
  } catch (error) {
    console.error('新增请求失败:', error);
    ElMessage.error(error.response?.data?.message || '新增线路失败');
  }
};

// 选中行变化处理
const handleSelectionChange = (selection) => {
  selectedRoutes.value = selection;
};

// 批量删除处理
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRoutes.value.length} 条线路吗？`,
      "删除确认",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const ids = selectedRoutes.value.map(item => item.id);
    const response = await axios.post(
      API_HOST + "route/delete",
      { ids: ids },
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );

    if (response.data.code === 200) {
      ElMessage.success("删除成功");
      await fetchRouteData();
      selectedRoutes.value = [];
    } else {
      ElMessage.error(response.data.message || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除操作失败:", error);
      ElMessage.error(error.response?.data?.message || "删除失败");
    }
  }
};

// 获取数据方法
const fetchRouteData = async () => {
  try {
    const response = await axios.post(
      API_HOST + "route/get_by_page",
      {
        current: (currentPage.value - 1) * pageSize.value,
        size: pageSize.value
      },
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );

    if (response.data.code === 200) {
      routeList.value = response.data.data.list; // 直接获取数组数据
      total.value = response.data.data.total;
    } else if (response.data.message === "NOT_LOGIN") {
      await handleNotLogin();
    } else {
      ElMessage.error(response.data.message || "获取数据失败");
    }
  } catch (error) {
    console.error("请求出错:", error);
    ElMessage.error(error.response?.data?.message || "请求错误！");
  }
};

// 处理未登录状态
const handleNotLogin = async () => {
  await store.dispatch("verifyJwt");
  if (!store.getters.isUserLoggedIn) {
    await router.push("/login");
  }
};

onMounted(() => {
  fetchRouteData();
});
</script>

<style scoped>
.route-management {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 12px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>