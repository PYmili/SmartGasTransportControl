<template>
  <el-menu-item index="/area">
    <el-icon><Location /></el-icon>
    <template #title>区域管理</template>
  </el-menu-item>

  <el-menu-item index="/users">
    <el-icon><User /></el-icon>
    <template #title>用户管理</template>
  </el-menu-item>
</template>

<script setup>
import { Location, User } from '@element-plus/icons-vue'
</script>