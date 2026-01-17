<template>
  <div class="home-container">
    <div class="main-content">
      <!-- 轮播图组件 -->
      <home-carousel />
      
      <!-- 快速导航组件 -->
      <quick-nav />
      
      <!-- AI热点组件 -->
      <AINewsSection />
      
      <!-- 学习路径组件 -->
      <learning-paths-section />
      
      <!-- 热门教程组件 -->
      <popular-tutorials-section />
      
      <!-- AI工具组件 -->
      <AIToolsSection />
      
      <!-- 社区动态组件 -->
      <community-section />
      
      <!-- 资源下载组件 -->
      <resources-section />
    </div>
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
.home-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f2f5 0%, #ffffff 100%);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .main-content {
    padding: 15px;
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
  }
}
</style>