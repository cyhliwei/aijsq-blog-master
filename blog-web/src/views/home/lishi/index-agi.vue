<template>
  <div id="app">
    <el-container style="height: 100vh">
      <!-- 左侧导航栏 -->
      <el-aside width="245px" class="sidebar">
        <div class="sidebar-header">
          <span>知识库目录</span>
          <i class="el-icon-s-unfold"></i>
        </div>
        
        <el-menu
          :default-active="activeIndex"
          class="el-menu-vertical"
          @select="handleSelect"
          background-color="#f5f5f5"
          text-color="#333"
          active-text-color="#1890ff">
          
          <!-- 通往AGI之路 -->
          <div class="menu-item-highlight">
            <i class="menu-icon">🌈</i>
            <span>通往AGI之路</span>
          </div>

          <!-- 0. 从这里起程 -->
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>0. 从这里起程</span>
            </template>
          </el-submenu>

          <!-- A1领导力修炼之路 -->
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-star-off"></i>
              <span>A1领导力修炼之路</span>
            </template>
          </el-submenu>

          <!-- 社区共创项目及比赛 -->
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-trophy"></i>
              <span>社区共创项目及比赛</span>
            </template>
          </el-submenu>

          <!-- AI训练营 -->
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-reading"></i>
              <span>AI训练营</span>
            </template>
          </el-submenu>

          <!-- WaytoAGI 高校探索 -->
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-school"></i>
              <span>WaytoAGI 高校探索</span>
            </template>
          </el-submenu>

          <!-- 1.1 入门: AI 学习路径与课程 -->
          <el-submenu index="6">
            <template slot="title">
              <i class="el-icon-notebook-2"></i>
              <span>1.1 入门: AI 学习路径与课程</span>
            </template>
          </el-submenu>

          <!-- 1.2 Prompts (提示词) -->
          <el-submenu index="7">
            <template slot="title">
              <i class="el-icon-edit-outline"></i>
              <span>1.2 Prompts (提示词)</span>
            </template>
          </el-submenu>

          <!-- 1.3 AI Agents (智能体) -->
          <el-submenu index="8">
            <template slot="title">
              <i class="el-icon-cpu"></i>
              <span>1.3 AI Agents (智能体)</span>
            </template>
          </el-submenu>

          <!-- 1.4 AI 绘画 -->
          <el-submenu index="9">
            <template slot="title">
              <i class="el-icon-picture-outline"></i>
              <span>1.4 AI 绘画</span>
            </template>
          </el-submenu>

          <!-- 1.5 AI 视频 -->
          <el-submenu index="10">
            <template slot="title">
              <i class="el-icon-video-camera"></i>
              <span>1.5 AI 视频</span>
            </template>
          </el-submenu>

          <!-- 1.6 AI 音乐 -->
          <el-submenu index="11">
            <template slot="title">
              <i class="el-icon-headset"></i>
              <span>1.6 AI 音乐</span>
            </template>
          </el-submenu>

          <!-- 1.7 AI 数字人+语音合成 -->
          <el-submenu index="12">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>1.7 AI 数字人+语音合成</span>
            </template>
          </el-submenu>

          <!-- 1.8 AI 3D -->
          <el-submenu index="13">
            <template slot="title">
              <i class="el-icon-box"></i>
              <span>1.8 AI 3D</span>
            </template>
          </el-submenu>

          <!-- 1.9 ComfyUI 工作流 -->
          <el-submenu index="14">
            <template slot="title">
              <i class="el-icon-connection"></i>
              <span>1.9 ComfyUI 工作流</span>
            </template>
          </el-submenu>

          <!-- 2.0 AI 编程 -->
          <el-submenu index="15">
            <template slot="title">
              <i class="el-icon-document-copy"></i>
              <span>2.0 AI 编程</span>
            </template>
          </el-submenu>

          <!-- 2.1 喂起码: 个人专栏 - 展开状态 -->
          <el-submenu index="16" :default-active="true">
            <template slot="title">
              <i class="el-icon-folder-opened"></i>
              <span>2.1 喂起码: 个人专栏</span>
            </template>
            <el-menu-item index="16-1">
              <i class="el-icon-tickets"></i>
              <span>提示词目录</span>
            </el-menu-item>
            <el-submenu index="16-2">
              <template slot="title">
                <i class="el-icon-document"></i>
                <span>小土堆-提示词</span>
              </template>
            </el-submenu>
            <el-submenu index="16-3">
              <template slot="title">
                <i class="el-icon-document"></i>
                <span>蓝衣侠者-AIGC提炼火花</span>
              </template>
            </el-submenu>
            <el-submenu index="16-4">
              <template slot="title">
                <i class="el-icon-document"></i>
                <span>云中江树- LangGPT 提示词</span>
              </template>
            </el-submenu>
            <el-submenu index="16-5">
              <template slot="title">
                <i class="el-icon-document"></i>
                <span>周慎谦士-提示词</span>
              </template>
            </el-submenu>
            <el-menu-item index="16-6">
              <i class="el-icon-document"></i>
              <span>繁鹤居-提示词</span>
            </el-menu-item>
          </el-submenu>

        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <el-header height="auto" class="main-header">
          <i class="el-icon-arrow-left back-icon"></i>
          <div class="header-content">
            <h1 class="page-title">
              <span class="rainbow-icon">🌈</span>
              通往AGI之路
            </h1>
            <div class="header-meta">
              <span class="meta-item">2023年4月26日创建</span>
              <span class="meta-item"><i class="el-icon-view"></i> 2438364</span>
              <span class="meta-item"><i class="el-icon-star-off"></i> 5200661</span>
              <span class="meta-item"><i class="el-icon-thumb"></i> 2939</span>
              <span class="meta-item"><i class="el-icon-chat-dot-round"></i> 715</span>
            </div>
          </div>
          <i class="el-icon-message comment-icon"></i>
        </el-header>

        <el-main class="main-content">
          <!-- 愿景和目标 -->
          <div class="content-section">
            <h2 class="section-title">
              <span class="target-icon">🎯</span>
              愿景和目标
            </h2>
            <div class="highlight-box">
              <p class="highlight-text">
                <span class="bulb-icon">💡</span>
                要去现通用人工智能（AGI）设计有很长的路要走，
              </p>
              <p class="highlight-text-main">
                我们的目标是让每个人的学习程度走愈酷，让更多的人因 AI 而成长。
              </p>
            </div>
          </div>

          <!-- 简介 -->
          <div class="content-section">
            <h2 class="section-title">
              <span class="pen-icon">✏️</span>
              简介 | <span class="link-text">WaytoAGI.com</span>
            </h2>
            <div class="intro-container">
              <div class="intro-text">
                <p>欢迎来到通往 AGI 之路 / WaytoAGI社区致力于千人工智能学习的知识库与社区平台，提供系统全面的 AI 学习路径，整合资源、组织实践活动，覆盖近 900 万学习者，知识库访问基础 8000 万次，联动 180 所高校及 100 + 企业，举办 500 + 场活动。</p>
                <div class="link-buttons">
                  <el-button type="text" icon="el-icon-info">如何更好地使用</el-button>
                  <el-button type="text" icon="el-icon-chat-line-round">通往AGI之路社区 业务说明</el-button>
                  <el-button type="text" icon="el-icon-video-camera">WaytoAGI 品牌 VI</el-button>
                </div>
              </div>
              <div class="qr-code">
                <img src="https://via.placeholder.com/120x120?text=QR+Code" alt="QR Code">
                <p class="qr-text">公众号：通往 AGI 之路</p>
                <p class="qr-links">Bilibili | 小红书 | Twitter</p>
              </div>
            </div>
          </div>

          <!-- 全库内容任意问答 -->
          <div class="content-section">
            <h2 class="section-title">
              <span class="emoji-icon">🧠</span>
              全库内容任意问答，让知识"活"起来！
            </h2>
            <p class="section-text">WaytoAGI知识库内 9000+ 篇AI内容，现已支持【飞书知识库】功能！</p>
            <div class="feature-list">
              <span class="feature-item">📋 你问问题</span>
              <span class="feature-item">→ 🤖 AI理解</span>
              <span class="feature-item">→ 📊 智能总结</span>
              <span class="feature-item">→ 🔍 精准溯源</span>
            </div>
            <p class="section-text">以前翻译半天找不到想要的资料，现在一句话即可出现想要的高质量内容答复!</p>
            <div class="cta-section">
              <p class="cta-text">
                <span class="rocket-icon">🚀</span>
                <span>增加入口（需先点击【加入人选对话】再抽问） </span>
                <span class="link-emoji">👉 飞书空间咨询圈子</span>
              </p>
              <p class="warning-text">
                <span class="warning-icon">⚠️</span>
                诚邀测评：欢迎加入右侧体验群，不喜欢推点击咱，还是想牛场，均可在群内反馈！
              </p>
            </div>
            <div class="qr-code-bottom">
              <img src="https://via.placeholder.com/140x140?text=QR+Code+2" alt="QR Code 2">
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import HomeCarousel from './components/dashbook/HomeCarousel.vue'
import QuickNav from './components/dashbook/QuickNav.vue'
import AINewsSection from './components/dashbook/AINewsSection.vue'
import LearningPathsSection from './components/dashbook/LearningPathsSection.vue'
import PopularTutorialsSection from './components/dashbook/PopularTutorialsSection.vue'
import AIToolsSection from './components/dashbook/AIToolsSection.vue'
import CommunitySection from './components/dashbook/CommunitySection.vue'
import ResourcesSection from './components/dashbook/ResourcesSection.vue'

export default {
  name: 'Home',
  components: {
    HomeCarousel,
    QuickNav,
    AINewsSection,
    LearningPathsSection,
    PopularTutorialsSection,
    AIToolsSection,
    CommunitySection,
    ResourcesSection
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  color: #333;
}

/* 侧边栏样式 */
.sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
}

.sidebar-header {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #666;
  border-bottom: 1px solid #e0e0e0;
}

.menu-item-highlight {
  background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 100%);
  padding: 12px 20px;
  margin: 8px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-weight: 500;
  color: #1976d2;
}

.menu-icon {
  margin-right: 8px;
  font-size: 18px;
}

.el-menu {
  border: none;
}

.el-menu-item,
.el-submenu__title {
  height: 44px;
  line-height: 44px;
  font-size: 14px;
}

.el-menu-item i,
.el-submenu__title i {
  margin-right: 8px;
  color: #666;
}

.el-submenu .el-menu-item {
  padding-left: 50px !important;
  min-width: auto;
}

/* 主内容区样式 */
.main-header {
  background: white;
  border-bottom: 1px solid #e0e0e0;
  padding: 20px 40px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.back-icon {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  margin-right: 20px;
  margin-top: 8px;
}

.comment-icon {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  margin-top: 8px;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.rainbow-icon {
  margin-right: 12px;
  font-size: 32px;
}

.header-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.main-content {
  background: white;
  padding: 40px 60px;
  overflow-y: auto;
}

.content-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.target-icon,
.pen-icon,
.emoji-icon {
  margin-right: 10px;
  font-size: 28px;
}

.highlight-box {
  background: linear-gradient(135deg, #fff9e6 0%, #ffefd5 100%);
  border-left: 4px solid #ffa726;
  padding: 24px 28px;
  border-radius: 6px;
  margin-top: 16px;
}

.highlight-text {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 8px;
}

.highlight-text-main {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  font-weight: 500;
}

.bulb-icon {
  margin-right: 8px;
  font-size: 20px;
}

.link-text {
  color: #1890ff;
  font-weight: 500;
}

.intro-container {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  margin-top: 20px;
}

.intro-text {
  flex: 1;
}

.intro-text p {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 16px;
}

.link-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  flex-wrap: wrap;
}

.link-buttons .el-button {
  font-size: 14px;
  padding: 8px 0;
}

.qr-code {
  text-align: center;
  min-width: 140px;
}

.qr-code img {
  width: 120px;
  height: 120px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-bottom: 8px;
}

.qr-text {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  margin-bottom: 4px;
}

.qr-links {
  font-size: 12px;
  color: #1890ff;
}

.section-text {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 16px;
}

.feature-list {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 20px 0;
  flex-wrap: wrap;
}

.feature-item {
  font-size: 15px;
  color: #333;
}

.cta-section {
  margin-top: 24px;
}

.cta-text {
  font-size: 15px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 12px;
}

.rocket-icon {
  margin-right: 8px;
  font-size: 18px;
}

.link-emoji {
  color: #1890ff;
  margin-left: 4px;
}

.warning-text {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.warning-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.qr-code-bottom {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.qr-code-bottom img {
  width: 140px;
  height: 140px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

/* 滚动条样式 */
.sidebar::-webkit-scrollbar,
.main-content::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-thumb,
.main-content::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover,
.main-content::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* Element UI 自定义样式 */
.el-submenu__title:hover,
.el-menu-item:hover {
  background-color: #e8f4ff !important;
}

.el-menu-item.is-active {
  background-color: #e8f4ff !important;
  color: #1890ff !important;
}

</style>