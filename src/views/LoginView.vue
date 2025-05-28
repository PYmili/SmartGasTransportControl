<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- 登录表单卡片 -->
    <div class="login-card">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-icon">
          <el-icon :size="32" color="#409EFF">
            <component :is="'Grid'" />
          </el-icon>
        </div>
        <h1 class="brand-name">SGTC</h1>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <h2 class="title">欢迎回来</h2>
        <p class="subtitle">请登录您的账户</p>

        <el-form-item prop="username" class="form-item">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            size="large"
            class="custom-input"
          />
        </el-form-item>

        <el-form-item prop="password" class="form-item">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            size="large"
            class="custom-input"
          />
        </el-form-item>

        <el-form-item class="form-item">
          <el-button
            type="primary"
            native-type="submit"
            class="login-btn"
            :loading="loading"
            size="large"
            @click="handleLogin"
          >
            <span v-if="!loading">立即登录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>

        <!-- 额外信息 -->
        <div class="extra-info">
          <div class="divider">
            <span>或</span>
          </div>
          <p class="help-text">
            <el-icon><component :is="'InfoFilled'" /></el-icon>
            首次使用？请联系管理员获取账户
          </p>
        </div>
      </el-form>
    </div>

    <!-- 版权信息 -->
    <div class="footer">
      <p>&copy; 2025 SGTC. All rights reserved.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { User, Lock, Grid, InfoFilled } from "@element-plus/icons-vue";
import CryptoJs from 'crypto-js';
import axios from 'axios';
import { useStore } from "vuex";

// 表单引用
const loginFormRef = ref(null);
// 路由实例
const router = useRouter();
// vuex实例
const store = useStore();

// 表单数据
const loginForm = reactive({
  username: "",
  password: "",
});

// 加载状态
const loading = ref(false);

// 验证规则
const loginRules = reactive({
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 12, message: "长度在 3 到 50 个字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 3, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" },
  ],
});

/**
 * 密码哈希处理
 * @param password 需要hash的密码
 */
const hashPassword = (password) => {
  return CryptoJs.SHA256(password).toString();
};

const handleIsloggedIn = async () => {
  // vuex查看是否登录
  await store.dispatch('verifyJwt');
  const isLogged = store.getters.isUserLoggedIn;
  
  if (isLogged === true) {
    ElMessage.success("用户已登录！");
    await router.push("/control");
    return;
  }
};

/**
 * 登录处理
 */
const handleLogin = async () => {
  // 表单验证
  try {
    await loginFormRef.value.validate();
  } catch (error) {
    return;
  }

  loading.value = true;

  try {
    // 处理密码哈希
    const hashedPassword = hashPassword(loginForm.password);

    // 发送登录请求
    const response = await axios.post("http://localhost:8080/user/login", {
      username: loginForm.username,
      password: hashedPassword,
    });

    // 处理响应
    if (response.data.code === 200) {
      ElMessage.success("登录成功");

      // 设置状态为登录，并保存jwtToken
      store.dispatch('login', response.data.data);

      // 跳转页面
      await router.push("/control");
    } else {
      ElMessage.error(response.data.message || "登录失败");
    }
  } catch (error) {
    // 错误处理
    if (error.response) {
      // 请求已发出，服务器响应状态码非 2xx
      ElMessage.error(
        `错误代码：${error.response.status}，${error.response.data.message}`
      );
    } else if (error.request) {
      // 请求已发出但没有收到响应
      ElMessage.error("服务器未响应，请检查网络连接");
    } else {
      // 其他错误
      ElMessage.error("请求发送失败：" + error.message);
    }
  } finally {
    loading.value = false;
  }
};

const isLoggedIn = ref(false);

onMounted(async () => {
  await handleIsloggedIn();
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 10%;
  animation-delay: 2s;
}

.circle-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    opacity: 0.7;
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
    opacity: 0.3;
  }
}

/* 登录卡片 */
.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 2;
  animation: slideUp 0.8s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Logo区域 */
.logo-section {
  text-align: center;
  padding: 40px 40px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.logo-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border-radius: 16px;
  margin-bottom: 16px;
  box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
}

.brand-name {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
  letter-spacing: 1px;
}

/* 表单样式 */
.login-form {
  padding: 40px;
}

.title {
  text-align: center;
  margin-bottom: 8px;
  color: #2c3e50;
  font-size: 28px;
  font-weight: 600;
}

.subtitle {
  text-align: center;
  margin-bottom: 32px;
  color: #6c757d;
  font-size: 14px;
}

.form-item {
  margin-bottom: 24px;
}

.custom-input {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid #e1e8ed;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border: none;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

/* 额外信息 */
.extra-info {
  margin-top: 32px;
}

.divider {
  text-align: center;
  margin: 24px 0;
  position: relative;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e1e8ed;
}

.divider span {
  background: rgba(255, 255, 255, 0.95);
  padding: 0 16px;
  color: #6c757d;
  font-size: 14px;
  position: relative;
  z-index: 1;
}

.help-text {
  text-align: center;
  color: #6c757d;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

/* 版权信息 */
.footer {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2;
}

.footer p {
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    width: 90%;
    margin: 0 20px;
  }
  
  .login-form {
    padding: 30px 24px;
  }
  
  .logo-section {
    padding: 30px 24px 20px;
  }
  
  .title {
    font-size: 24px;
  }
}

/* 加载状态动画 */
.login-btn.is-loading {
  position: relative;
  pointer-events: none;
}

/* 表单验证错误样式优化 */
.form-item :deep(.el-form-item__error) {
  color: #f56565;
  font-size: 12px;
  margin-top: 4px;
}

/* 输入框聚焦时的动画 */
.custom-input :deep(.el-input__inner) {
  transition: all 0.3s ease;
}

/* 密码显示按钮样式 */
.custom-input :deep(.el-input__suffix) {
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__suffix:hover) {
  color: #409EFF;
}
</style>