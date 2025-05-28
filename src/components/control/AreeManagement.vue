<template>
  <div class="area-container">
    <el-card>
      <!-- 区域选择 -->
      <el-select 
        v-model="selectedAreas" 
        multiple 
        placeholder="请选择区域"
        class="area-selector"
      >
        <el-option
          v-for="area in areas"
          :key="area.id"
          :label="area.name"
          :value="area.id"
        />
      </el-select>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="time" label="时间" width="180" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedAreas = ref([])
const areas = ref([
  { id: 1, name: '测试区域1' },
  { id: 2, name: '测试区域2' }
])

const tableData = ref([
  { time: '2023-09-20 10:00', location: 'A区1号点', status: '正常' },
  { time: '2023-09-20 10:05', location: 'A区2号点', status: '预警' }
])

const statusType = {
  '正常': 'success',
  '预警': 'danger'
}

// request methods
const getAreas = () => {
  
};
</script>

<style scoped>
.area-selector {
  margin-bottom: 20px;
  width: 300px;
}
</style>