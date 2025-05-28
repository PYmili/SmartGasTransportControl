<template>
  <el-container class="main-container" :class="{ 'dark-theme': isDark }">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <!-- 顶部Logo区域 -->
      <div class="logo-section" :class="{ collapsed: isCollapse }">
        <div class="logo-icon">
          <el-icon :size="24" color="#409EFF">
            <component :is="Grid" />
          </el-icon>
        </div>
        <transition name="fade">
          <span v-if="!isCollapse" class="logo-text">SGTC</span>
        </transition>
      </div>

      <!-- 用户信息区域 -->
      <div class="user-section" :class="{ collapsed: isCollapse }">
        <el-avatar 
          :size="isCollapse ? 32 : 48" 
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          class="user-avatar"
        />
        <transition name="fade">
          <div v-if="!isCollapse" class="user-info">
            <div class="user-name">John Doe</div>
            <div class="user-role">Administrator</div>
          </div>
        </transition>
      </div>

      <!-- 导航菜单 -->
      <div class="menu-section">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          class="sidebar-menu"
          background-color="transparent"
          text-color="#b8c5d1"
          active-text-color="#409EFF"
        >
          <!-- 区域管理 -->
          <el-sub-menu index="area" class="menu-menu">
            <template #title>
              <el-icon><component :is="Location" /></el-icon>
              <span>区域管理</span>
            </template>
            <el-menu-item index="/route" class="sub-item">
              <!-- <el-icon><component :is="" /></el-icon> -->
               <template #title>线路</template>
            </el-menu-item>
            <el-menu-item index="/pipline_table" class="sub-item">
              <!-- <el-icon><component :is="" /></el-icon> -->
               <template #title>监控</template>
            </el-menu-item>
          </el-sub-menu>

          <!-- 用户管理 -->
          <el-sub-menu index="user" class="sub-menu">
            <template #title>
              <el-icon><component :is="User" /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/users" class="sub-menu-item">
              <el-icon><component :is="UserFilled" /></el-icon>
              <template #title>所有用户</template>
            </el-menu-item>
          </el-sub-menu>
          
          <!-- 系统设置 -->
          <el-menu-item index="/settings" class="menu-item">
            <el-icon><component :is="Setting" /></el-icon>
            <template #title>系统设置</template>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 底部功能区 -->
      <div class="bottom-section">
        <!-- 主题切换按钮 -->
        <el-tooltip 
          :content="isDark ? '切换到亮色主题' : '切换到暗色主题'" 
          placement="right"
          :disabled="!isCollapse"
        >
          <div class="action-button theme-toggle" @click="toggleTheme">
            <el-icon :size="18">
              <component :is="isDark ? Sunny : Moon" />
            </el-icon>
            <transition name="fade">
              <span v-if="!isCollapse" class="button-text">
                {{ isDark ? '亮色模式' : '暗色模式' }}
              </span>
            </transition>
          </div>
        </el-tooltip>

        <!-- 折叠按钮 -->
        <el-tooltip 
          :content="isCollapse ? '展开侧边栏' : '收起侧边栏'" 
          placement="right"
          :disabled="!isCollapse"
        >
          <div class="action-button collapse-toggle" @click="toggleCollapse">
            <el-icon :size="18">
              <component :is="isCollapse ? Expand : Fold" />
            </el-icon>
            <transition name="fade">
              <span v-if="!isCollapse" class="button-text">收起菜单</span>
            </transition>
          </div>
        </el-tooltip>
      </div>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-content">
      <!-- 顶部导航栏 -->
      <el-header class="header" height="60px">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>当前页面</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="5" class="notification-badge">
            <el-icon :size="20" class="header-icon">
              <component :is="Bell" />
            </el-icon>
          </el-badge>
          <el-dropdown trigger="click" class="user-dropdown">
            <div class="header-user">
              <el-avatar 
                :size="32" 
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              />
              <el-icon class="dropdown-icon">
                <component :is="ArrowDown" />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>账户设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="content-main">
        <div class="content-wrapper">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { 
  Fold, 
  Expand, 
  Grid, 
  Setting, 
  User, 
  UserFilled,
  Location,
  Moon, 
  Sunny, 
  Bell, 
  ArrowDown 
} from "@element-plus/icons-vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const isCollapse = ref(false);
const activeMenu = ref("area");
const isDark = ref(false);
// vuex
const store = useStore();
// router
const router = useRouter();

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

const toggleTheme = () => {
  isDark.value = !isDark.value;
  document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light');
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light');
};

// 初始化
onMounted(async () => {
  // vuex查看是否登录
  await store.dispatch('verifyJwt');
  const isLogged = store.getters.isUserLoggedIn;
  
  if (isLogged === false) {
    ElMessage.error("用户未登录！");
    await router.push("/login");
    return;
  }
  // 初始化主题
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme) {
    isDark.value = savedTheme === 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
  }
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.main-container {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  transition: all 0.3s ease;
}

.dark-theme {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
}

/* 侧边栏样式 */
.sidebar {
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.dark-theme .sidebar {
  background: linear-gradient(180deg, #1e1e1e 0%, #2a2a2a 100%);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.3);
}

/* Logo区域 */
.logo-section {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.logo-section.collapsed {
  justify-content: center;
  padding: 0;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 8px;
  margin-right: 12px;
}

.logo-section.collapsed .logo-icon {
  margin-right: 0;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #ffffff;
  letter-spacing: 0.5px;
}

/* 用户信息区域 */
.user-section {
  padding: 20px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.user-section.collapsed {
  justify-content: center;
  padding: 20px 0;
}

.user-avatar {
  margin-right: 12px;
  border: 2px solid rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.user-section.collapsed .user-avatar {
  margin-right: 0;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 4px;
}

.user-role {
  font-size: 12px;
  color: #b8c5d1;
}

/* 菜单区域 */
.menu-section {
  flex: 1;
  padding: 10px 0;
  overflow-y: auto;
}

.sidebar-menu {
  border: none;
}

/* 一级菜单项样式 */
.sidebar-menu .el-menu-item {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
  color: #b8c5d1;
}

.sidebar-menu .el-menu-item:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

.sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, #409EFF, #67C23A);
  color: #ffffff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

/* 子菜单样式 */
.sidebar-menu .el-sub-menu {
  margin: 4px 12px;
  border-radius: 8px;
  overflow: hidden;
}

.sidebar-menu .el-sub-menu .el-sub-menu__title {
  border-radius: 8px;
  transition: all 0.3s ease;
  color: #b8c5d1;
  padding-left: 20px;
}

.sidebar-menu .el-sub-menu .el-sub-menu__title:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

.sidebar-menu .el-sub-menu.is-active .el-sub-menu__title {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

/* 二级菜单项样式 */
.sidebar-menu .el-sub-menu .el-menu-item {
  margin: 2px 8px;
  border-radius: 6px;
  background: rgba(0, 0, 0, 0.1);
  color: #a0a8b8;
  padding-left: 40px;
}

.sidebar-menu .el-sub-menu .el-menu-item:hover {
  background: rgba(64, 158, 255, 0.15);
  color: #409EFF;
}

.sidebar-menu .el-sub-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, #409EFF, #67C23A);
  color: #ffffff;
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
}

/* 底部功能区 */
.bottom-section {
  padding: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.action-button {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #b8c5d1;
}

.action-button:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #409EFF;
  transform: translateX(2px);
}

.action-button:last-child {
  margin-bottom: 0;
}

.button-text {
  margin-left: 12px;
  font-size: 14px;
}

/* 主内容区 */
.main-content {
  background: transparent;
}

.header {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.dark-theme .header {
  background: rgba(30, 30, 30, 0.9);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.notification-badge {
  cursor: pointer;
}

.header-icon {
  color: #606266;
  transition: color 0.3s ease;
}

.header-icon:hover {
  color: #409EFF;
}

.dark-theme .header-icon {
  color: #b8c5d1;
}

.user-dropdown {
  cursor: pointer;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.header-user:hover {
  background: rgba(64, 158, 255, 0.1);
}

.dropdown-icon {
  font-size: 12px;
  color: #909399;
}

.content-main {
  padding: 0;
  background: transparent;
}

.content-wrapper {
  padding: 24px;
  min-height: calc(100vh - 60px);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px 0 0 0;
  margin: 0;
}

.dark-theme .content-wrapper {
  background: rgba(30, 30, 30, 0.8);
  color: #ffffff;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 滚动条样式 */
.menu-section::-webkit-scrollbar {
  width: 4px;
}

.menu-section::-webkit-scrollbar-track {
  background: transparent;
}

.menu-section::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
}

.menu-section::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
    height: 100vh;
  }
  
  .main-content {
    margin-left: 0;
  }
}
</style>
