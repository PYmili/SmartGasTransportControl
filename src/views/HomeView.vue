<template>
  <div class="home-container">
    <!-- 导航栏 -->
    <header class="navbar">
      <div class="nav-content">
        <div class="logo-section">
          <div class="logo-icon">
            <el-icon :size="28" color="#409EFF">
              <Connection />
            </el-icon>
          </div>
          <h1 class="brand-title">SmartGasTransportControl</h1>
        </div>
        <nav class="nav-menu">
          <a href="#features" class="nav-link">功能特性</a>
          <a href="#monitoring" class="nav-link">监控中心</a>
          <a href="#about" class="nav-link">关于系统</a>
          <el-button type="primary" @click="goToLogin" class="login-btn">
            进入系统
          </el-button>
        </nav>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 英雄区域 -->
      <section class="hero-section">
        <div class="hero-content">
          <div class="hero-text">
            <h1 class="hero-title">
              智能气体运输控制系统
              <span class="highlight">SmartGasTransportControl</span>
            </h1>
            <p class="hero-description">
              专业的气体线路传输监控与控制平台，实现全方位的气体运输安全管理，
              提供实时监控、智能预警、数据分析等核心功能，保障气体运输的安全与高效。
            </p>
            <div class="hero-actions">
              <el-button
                type="primary"
                size="large"
                @click="goToLogin"
                class="primary-btn"
              >
                <el-icon class="btn-icon"><ArrowRight /></el-icon>
                立即使用
              </el-button>
              <el-button
                size="large"
                @click="scrollToFeatures"
                class="secondary-btn"
              >
                了解更多
              </el-button>
            </div>
          </div>
          <div class="hero-visual">
            <div class="dashboard-preview">
              <div class="preview-header">
                <div class="preview-dots">
                  <span class="dot red"></span>
                  <span class="dot yellow"></span>
                  <span class="dot green"></span>
                </div>
                <span class="preview-title">实时监控面板</span>
              </div>
              <div class="preview-content">
                <div class="monitor-grid">
                  <div class="monitor-item active">
                    <div class="monitor-icon">
                      <el-icon><Monitor /></el-icon>
                    </div>
                    <div class="monitor-info">
                      <span class="monitor-label">线路A</span>
                      <span class="monitor-status online">正常运行</span>
                    </div>
                  </div>
                  <div class="monitor-item">
                    <div class="monitor-icon">
                      <el-icon><Monitor /></el-icon>
                    </div>
                    <div class="monitor-info">
                      <span class="monitor-label">线路B</span>
                      <span class="monitor-status warning">维护中</span>
                    </div>
                  </div>
                  <div class="monitor-item">
                    <div class="monitor-icon">
                      <el-icon><Monitor /></el-icon>
                    </div>
                    <div class="monitor-info">
                      <span class="monitor-label">线路C</span>
                      <span class="monitor-status online">正常运行</span>
                    </div>
                  </div>
                </div>
                <div class="chart-area">
                  <div class="chart-placeholder">
                    <div class="chart-lines">
                      <div
                        v-for="i in 10"
                        :key="i"
                        class="chart-line"
                        :style="{ height: `${Math.random() * 60 + 20}px` }"
                      ></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 功能特性区域 -->
      <section id="features" class="features-section">
        <div class="section-container">
          <div class="section-header">
            <h2 class="section-title">核心功能特性</h2>
            <p class="section-subtitle">全面的气体运输监控与控制解决方案</p>
          </div>
          <div class="features-grid">
            <div
              v-for="feature in features"
              :key="feature.id"
              class="feature-card"
            >
              <div class="feature-icon">
                <el-icon :size="32">
                  <component :is="feature.icon" />
                </el-icon>
              </div>
              <h3 class="feature-title">{{ feature.title }}</h3>
              <p class="feature-description">{{ feature.description }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 监控中心展示 -->
      <section id="monitoring" class="monitoring-section">
        <div class="section-container">
          <div class="monitoring-content">
            <div class="monitoring-text">
              <h2 class="section-title">实时监控中心</h2>
              <p class="section-description">
                通过先进的传感器网络和数据采集系统，实现对气体线路的24/7不间断监控。
                系统能够实时检测压力、流量、温度等关键参数，确保运输过程的安全可控。
              </p>
              <div class="monitoring-stats">
                <div class="stat-item">
                  <div class="stat-number">99.9%</div>
                  <div class="stat-label">系统可靠性</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">24/7</div>
                  <div class="stat-label">实时监控</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">1s</div>
                  <div class="stat-label">响应时间</div>
                </div>
              </div>
            </div>
            <div class="monitoring-visual">
              <div class="pipeline-diagram">
                <div class="pipeline-node start">
                  <div class="node-icon">
                    <el-icon><Location /></el-icon>
                  </div>
                  <span class="node-label">起点站</span>
                </div>
                <div class="pipeline-line">
                  <div class="flow-indicator"></div>
                </div>
                <div class="pipeline-node middle">
                  <div class="node-icon">
                    <el-icon><Monitor /></el-icon>
                  </div>
                  <span class="node-label">监控点</span>
                </div>
                <div class="pipeline-line">
                  <div class="flow-indicator"></div>
                </div>
                <div class="pipeline-node end">
                  <div class="node-icon">
                    <el-icon><Flag /></el-icon>
                  </div>
                  <span class="node-label">终点站</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 关于系统 -->
      <section id="about" class="about-section">
        <div class="section-container">
          <div class="about-content">
            <h2 class="section-title">关于系统</h2>
            <p class="about-description">
              SmartGasTransportControl是一套专业的智能气体运输控制系统，
              采用先进的物联网技术和人工智能算法，为气体运输行业提供全方位的监控与控制解决方案。
              系统具备高可靠性、实时性和智能化特点，能够有效提升运输安全性和运营效率。
            </p>
            <div class="tech-stack">
              <div class="tech-item">
                <el-icon><Cpu /></el-icon>
                <span>物联网技术</span>
              </div>
              <div class="tech-item">
                <el-icon><DataLine /></el-icon>
                <span>大数据分析</span>
              </div>
              <div class="tech-item">
                <el-icon><Connection /></el-icon>
                <span>云端部署</span>
              </div>
              <div class="tech-item">
                <el-icon><Lock /></el-icon>
                <span>安全防护</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-info">
          <div class="footer-logo">
            <el-icon :size="24" color="#409EFF">
              <Connection />
            </el-icon>
            <span class="footer-brand">SmartGasTransportControl</span>
          </div>
          <p class="footer-description">智能气体运输控制系统</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>产品</h4>
            <a href="#">实时监控</a>
            <a href="#">数据分析</a>
            <a href="#">预警系统</a>
          </div>
          <div class="link-group">
            <h4>支持</h4>
            <a href="#">帮助文档</a>
            <a href="#">技术支持</a>
            <a href="#">联系我们</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 SmartGasTransportControl. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  Connection,
  ArrowRight,
  Monitor,
  Location,
  Flag,
  Cpu,
  DataLine,
  Lock,
  Warning,
  Bell,
  TrendCharts,
  Setting,
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

// router
const router = useRouter();

const features = ref([
  {
    id: 1,
    icon: Monitor,
    title: '实时监控',
    description:
      '24小时不间断监控气体线路状态，实时显示压力、流量、温度等关键参数',
  },
  {
    id: 2,
    icon: Warning,
    title: '智能预警',
    description: '基于AI算法的智能预警系统，提前发现潜在风险，确保运输安全',
  },
  {
    id: 3,
    icon: TrendCharts,
    title: '数据分析',
    description: '强大的数据分析功能，生成详细报表，支持历史数据查询和趋势分析',
  },
  {
    id: 4,
    icon: Bell,
    title: '报警系统',
    description: '多级报警机制，支持短信、邮件、APP推送等多种通知方式',
  },
  {
    id: 5,
    icon: Lock,
    title: '安全防护',
    description: '多重安全防护措施，确保系统稳定运行和数据安全',
  },
  {
    id: 6,
    icon: Setting,
    title: '远程控制',
    description: '支持远程控制功能，可远程调节阀门、泵站等设备运行参数',
  },
])

const goToLogin = async () => {
  await router.push('/login');
}

const scrollToFeatures = () => {
  document.getElementById('features').scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.home-container {
  min-height: 100vh;
  background: #ffffff;
}

/* 导航栏 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  z-index: 1000;
  transition: all 0.3s ease;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 10px;
}

.brand-title {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  letter-spacing: -0.5px;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 32px;
}

.nav-link {
  color: #606266;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: #409eff;
}

.login-btn {
  border-radius: 8px;
  padding: 8px 20px;
}

/* 主要内容 */
.main-content {
  padding-top: 70px;
}

/* 英雄区域 */
.hero-section {
  min-height: 90vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  display: flex;
  align-items: center;
  padding: 80px 24px;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  color: #1a202c;
  line-height: 1.2;
  margin-bottom: 24px;
}

.highlight {
  background: linear-gradient(135deg, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
  font-size: 36px;
  margin-top: 8px;
}

.hero-description {
  font-size: 18px;
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 40px;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.primary-btn {
  height: 48px;
  padding: 0 32px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
}

.secondary-btn {
  height: 48px;
  padding: 0 32px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  border: 2px solid #409eff;
  color: #409eff;
}

.btn-icon {
  margin-right: 8px;
}

/* 仪表板预览 */
.dashboard-preview {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

.preview-header {
  background: #f8fafc;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid #e2e8f0;
}

.preview-dots {
  display: flex;
  gap: 6px;
}

.dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.dot.red {
  background: #ef4444;
}
.dot.yellow {
  background: #f59e0b;
}
.dot.green {
  background: #10b981;
}

.preview-title {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.preview-content {
  padding: 24px;
}

.monitor-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.monitor-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.monitor-item.active {
  border-color: #409eff;
  background: rgba(64, 158, 255, 0.05);
}

.monitor-icon {
  color: #6b7280;
}

.monitor-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.monitor-label {
  font-size: 12px;
  color: #374151;
  font-weight: 500;
}

.monitor-status {
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 500;
}

.monitor-status.online {
  background: #dcfce7;
  color: #166534;
}

.monitor-status.warning {
  background: #fef3c7;
  color: #92400e;
}

.chart-area {
  height: 120px;
  background: #f8fafc;
  border-radius: 8px;
  padding: 16px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: end;
  justify-content: center;
}

.chart-lines {
  display: flex;
  gap: 8px;
  align-items: end;
}

.chart-line {
  width: 12px;
  background: linear-gradient(to top, #409eff, #67c23a);
  border-radius: 2px;
  animation: chartPulse 2s ease-in-out infinite;
}

@keyframes chartPulse {
  0%,
  100% {
    opacity: 0.7;
  }
  50% {
    opacity: 1;
  }
}

/* 功能特性区域 */
.features-section {
  padding: 100px 24px;
  background: #ffffff;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
}

.section-header {
  text-align: center;
  margin-bottom: 80px;
}

.section-title {
  font-size: 36px;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 16px;
}

.section-subtitle {
  font-size: 18px;
  color: #6b7280;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 32px;
}

.feature-card {
  background: #ffffff;
  padding: 32px;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
  text-align: center;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.feature-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 16px;
  color: white;
  margin-bottom: 24px;
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 12px;
}

.feature-description {
  color: #6b7280;
  line-height: 1.6;
}

/* 监控中心区域 */
.monitoring-section {
  padding: 100px 24px;
  background: #f8fafc;
}

.monitoring-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.section-description {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 40px;
}

.monitoring-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

/* 管道图表 */
.pipeline-diagram {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
}

.pipeline-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.node-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.node-label {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
}

.pipeline-line {
  width: 80px;
  height: 4px;
  background: #e2e8f0;
  position: relative;
  margin: 0 16px;
}

.flow-indicator {
  width: 20px;
  height: 4px;
  background: #409eff;
  border-radius: 2px;
  animation: flow 2s linear infinite;
}

@keyframes flow {
  0% {
    transform: translateX(-20px);
  }
  100% {
    transform: translateX(80px);
  }
}

/* 关于系统区域 */
.about-section {
  padding: 100px 24px;
  background: #ffffff;
}

.about-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
}

.about-description {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 48px;
}

.tech-stack {
  display: flex;
  justify-content: center;
  gap: 48px;
  flex-wrap: wrap;
}

.tech-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #409eff;
}

.tech-item span {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

/* 页脚 */
.footer {
  background: #1a202c;
  color: #ffffff;
  padding: 48px 24px 24px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.footer-brand {
  font-size: 18px;
  font-weight: 600;
}

.footer-description {
  color: #a0aec0;
  margin-bottom: 24px;
}

.footer-links {
  display: flex;
  gap: 48px;
}

.link-group h4 {
  margin-bottom: 16px;
  color: #ffffff;
}

.link-group a {
  display: block;
  color: #a0aec0;
  text-decoration: none;
  margin-bottom: 8px;
  transition: color 0.3s ease;
}

.link-group a:hover {
  color: #409eff;
}

.footer-bottom {
  max-width: 1200px;
  margin: 24px auto 0;
  padding-top: 24px;
  border-top: 1px solid #2d3748;
  text-align: center;
  color: #a0aec0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 48px;
    text-align: center;
  }

  .hero-title {
    font-size: 36px;
  }

  .monitoring-content {
    grid-template-columns: 1fr;
    gap: 48px;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .nav-menu {
    display: none;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .monitoring-stats {
    justify-content: center;
  }

  .tech-stack {
    gap: 24px;
  }
}
</style>