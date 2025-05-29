<template>
  <div class="table-container">
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      header-row-class-name="custom-header"
      :row-class-name="tableRowClassName"
    >
      <!-- 时间 -->
      <el-table-column label="时间" width="220" align="center">
        <template #default="{ row }">
          <span>{{ row.dateTime.split('T').join(' ') }}</span>
        </template>
      </el-table-column>

      <!-- 管线名 -->
      <el-table-column prop="pipelineName" label="管线名" align="center" width="190" />

      <!-- 温度 -->
      <el-table-column label="温度" align="center">
        <template #default="{ row }">
          <span :class="row.temperatureClass">{{ row.temperatures }}</span>
        </template>
      </el-table-column>

      <!-- 压力 -->
      <el-table-column label="压力" align="center">
        <template #default="{ row }">
          <span :class="row.pressureClass">{{ row.pressure }}</span>
        </template>
      </el-table-column>

      <!-- 差压 -->
      <el-table-column label="差压" align="center">
        <template #default="{ row }">
          <span :class="row.differentialClass">{{ row.differentialPressure }}</span>
        </template>
      </el-table-column>

      <!-- 状态 -->
      <el-table-column label="状态" align="center" width="100">
        <template #default="{ row }">
          <span :class="row.statusClass">{{ row.status }}</span>
        </template>
      </el-table-column>

      <!-- 输差 -->
      <el-table-column prop="Difference" label="输差" align="center" width="100"/>

      <!-- 输差率 -->
      <el-table-column prop="DifferenceRate" label="输损率" align="center" width="120" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import { useStore } from "vuex";
import { ElMessage, ElNotification } from "element-plus";
import moment from 'moment';

// store
const store = useStore();

const tableData = ref([]);
const pipelines = ref([]);
const tableRowClassName = ({ row }) => {
  return row.status === '异常' ? 'warning-row' : '';
};

const handleRequestGetPipelineList = async () => {
  const requestAddress = import.meta.env.VITE_API_HOST + "/pipeline/list";
  try {
    const response = await axios.post(
      requestAddress,
      {},
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );

    if (response.data.code !== 200) {
      ElMessage.error(response.data.msg || "请求失败！");
    } else {
      pipelines.value = response.data.data;
      // ElMessage.success("获取成功！");
    }
  } catch (error) {
    console.error("网络请求错误：", error);
    ElMessage.error("服务器错误！");
  }
};

const handleRequestTableData = async (params) => {
  const requestAddress = import.meta.env.VITE_API_HOST + "/pipeline_data/get";
  try {
    const response = await axios.post(
      requestAddress,
      params,
      {
        headers: {
          Authorization: `Bearer ${store.state.jwtToken}`,
        },
      }
    );
    if (response.data.code !== 200) {
      ElMessage.error(response.data.msg || "请求失败！");
    } else {
      return response.data.data;
    }
  } catch (error) {
    console.error("网络请求错误：", error);
    ElMessage.error("服务器错误！");
  }
  return [];
};

onMounted(async () => {
  await handleRequestGetPipelineList();
  // console.log(pipelines.value.map(item => item.name));
  tableData.value = await handleRequestTableData({ 
    pipelineNames: pipelines.value.map(item => item.name)
  });

  // console.log(tableData.value);
  tableData.value.filter(async item => {
    const historicalDateMoment = moment(item.dateTime);
    historicalDateMoment.subtract(1, 'year');
    const historicalData = await handleRequestTableData({
      pipelineName: item.pipelineName,
      dateTime: historicalDateMoment.format('YYYY-MM-DD HH:mm:ss')
    });
    // console.log(historicalDateMoment.format('YYYY-MM-DD HH:mm:ss'));
    
    item.Difference = parseFloat((item.airIntake - item.airOutlet).toFixed(4));
    item.DifferenceRate = parseFloat(((item.airIntake - item.airOutlet) / item.airIntake / 100).toFixed(4));

    // 计算偏差值
    let temperatureDeviation;
    let pressureDeviation;
    let differentialDeviation;
    historicalData.filter(i => {
      if (i.pipelineName === item.pipelineName) {
        temperatureDeviation = item.temperatures - i.temperatures;
        pressureDeviation = item.pressure - i.pressure;
        differentialDeviation = item.differentialPressure - i.differentialPressure;
      }
    });
    
    // console.log(item.pipelineName + 
    //   `\n\t温度差：${temperatureDeviation}` + 
    //   `\n\t压力差：${pressureDeviation}` + 
    //   `\n\t差压差：${differentialDeviation}`
    // );

    // 创建异常信息数组
    const abnormalMessages = [];

    // 温度警告判断
    if (temperatureDeviation > 3 || temperatureDeviation < -3) {
      abnormalMessages.push(`温度偏差 ${temperatureDeviation.toFixed(2)}`);
      item.temperatureClass = 'data-status-error';
    }

    // 压力警告判断
    if (pressureDeviation > 1 || pressureDeviation < -1) {
      abnormalMessages.push(`压力偏差 ${pressureDeviation.toFixed(2)}`);
      item.pressureClass = 'data-status-error';
    }

    // 差压警告判断
    if (differentialDeviation > 1 || differentialDeviation < -1) {
      abnormalMessages.push(`差压偏差 ${differentialDeviation.toFixed(2)}`);
      item.differentialClass = 'data-status-error';
    }

    // 如果有异常信息则触发通知
    if (abnormalMessages.length > 0) {
      ElNotification({
        title: '管线监测异常',
        dangerouslyUseHTMLString: true,
        message: `
          <div style="line-height: 1.8;">
            <b>管线名称：</b>${item.pipelineName}<br>
            <b>异常指标：</b><br>
            ${abnormalMessages.join('<br>')}
          </div>
        `,
        type: 'warning',
        position: 'bottom-right',
        duration: 6000
      });
    }

    console.log(item.DifferenceRate);

    // 输差率判断
    if (item.DifferenceRate > 0.001 || item.DifferenceRate < -0.001) {
        item.status = '异常';
        item.statusClass = "data-status-error";
    } else {
        item.status = '正常';
        item.statusClass = "data-status";
    }
    item.DifferenceRate = String((item.DifferenceRate * 100).toFixed(2)) + '%';
  });
});
</script>

<style lang="scss" scoped>
.table-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

:deep(.custom-header th) {
  background-color: #f5f7fa;
  font-weight: 600;
  color: #606266;
  font-size: 14px;
}

:deep(.el-table__row td) {
  padding: 12px 0;
  font-size: 13px;
}

/* 新增通知样式 */
:deep(.el-notification) {
  border: 1px solid #e6a23c;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  
  .el-notification__title {
    color: #e6a23c;
    font-size: 16px;
  }
  
  .el-notification__content {
    margin-top: 8px;
    color: #606266;
  }
}

.data-status {
  color: #67c23a;
  font-weight: 500;
}

.data-status-error {
  color: #ff0000;
  font-weight: 500;
}

/* 新增行警告样式 */
:deep(.el-table .warning-row) {
  --el-table-tr-bg-color: #fff3f0;  /* 浅红色背景 */
  background-color: var(--el-table-tr-bg-color);
  
  /* 若需要hover状态保持颜色 */
  /* &:hover td {
    background-color: var(--el-table-tr-bg-color) !important;
  } */
}


:deep(.el-table--border) {
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

:deep(.el-table__cell) {
  transition: background-color 0.2s;
}
</style>