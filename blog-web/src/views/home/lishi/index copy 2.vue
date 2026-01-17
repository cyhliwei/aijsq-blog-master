<template>
  <div class="home-page">
    <div class="container">
      <HeroSection />
      <NewsSection />
      <LearningPathsSection />
      <TutorialsSection />
      <ToolsSection />
      <TrendsSection />
    </div>
  </div>
</template>
 
<script>
import HeroSection from './components/index/HeroSection.vue'
import NewsSection from './components/index/NewsSection.vue'
import LearningPathsSection from './components/index/LearningPathsSection.vue'
import TutorialsSection from './components/index/TutorialsSection.vue'
import ToolsSection from './components/index/ToolsSection.vue'
import TrendsSection from './components/index/TrendsSection.vue'
 
export default {
  name: 'HomePage',
  components: {
    HeroSection,
    NewsSection,
    LearningPathsSection,
    TutorialsSection,
    ToolsSection,
    TrendsSection
  }
}
</script>
 
<style lang="scss" scoped>
.home-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: #333;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.text-gradient {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Hero区域样式 */
.hero-section {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 60px 0;
}

.hero-content {
  display: flex;
  flex-wrap: wrap;
  gap: 60px;
  align-items: center;
}

.hero-text {
  flex: 1;
  min-width: 300px;
}

.hero-tag {
  margin-bottom: 20px;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 20px;
  color: #1a202c;
}

.hero-description {
  font-size: 1.25rem;
  color: #4a5568;
  margin-bottom: 30px;
  line-height: 1.6;
}

.hero-search {
  margin-bottom: 40px;
}

.search-input {
  margin-bottom: 10px;
}

.search-hint {
  font-size: 0.875rem;
  color: #718096;
}

.stats-container {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.stat-label {
  font-size: 0.875rem;
  color: #718096;
}

.hero-carousel {
  flex: 1;
  min-width: 300px;
}

.slide-content {
  position: relative;
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  color: white;
}

.slide-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.2);
}

.slide-text {
  position: absolute;
  bottom: 40px;
  left: 40px;
  right: 40px;
  z-index: 2;
}

.slide-text h3 {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 10px;
}

.slide-text p {
  font-size: 1.125rem;
  opacity: 0.9;
  margin-bottom: 20px;
}

.slide-btn {
  background: white;
  color: #667eea;
  font-weight: 600;
}

.trend-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 20px;
}

.trend-tag {
  cursor: pointer;
  transition: transform 0.2s;
  
  &:hover {
    transform: translateY(-2px);
  }
}

/* 通用区块样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  color: #1a202c;
}

.section-subtitle {
  font-size: 1.125rem;
  color: #718096;
}

/* 新闻区域 */
.news-section {
  padding: 80px 0;
}

.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 30px;
}

.news-grid {
  margin-bottom: 40px;
}

.news-card {
  height: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1) !important;
  }
}

.news-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.news-time {
  font-size: 0.875rem;
  color: #718096;
}

.news-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 10px;
  color: #2d3748;
  line-height: 1.4;
}

.news-excerpt {
  font-size: 0.875rem;
  color: #718096;
  margin-bottom: 20px;
  line-height: 1.5;
}

.news-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-meta {
  font-size: 0.75rem;
  color: #a0aec0;
}

.news-stats {
  display: flex;
  gap: 15px;
  font-size: 0.75rem;
  color: #a0aec0;
}

.trending-topics {
  margin-top: 40px;
  padding-top: 40px;
  border-top: 1px solid #e2e8f0;
}

.trending-topics h3 {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.topic-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.topic-tag {
  cursor: pointer;
  padding: 8px 16px;
  transition: all 0.2s;
  
  &:hover {
    background-color: #ebf4ff;
    transform: translateY(-2px);
  }
}

/* 学习路径 */
.learning-paths {
  padding: 80px 0;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
}

.paths-grid {
  margin-bottom: 40px;
}

.path-card {
  height: 100%;
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-4px);
  }
}

.path-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.path-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.icon-1 { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.icon-2 { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.icon-3 { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.icon-4 { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.icon-5 { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }

.path-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 10px;
  color: #2d3748;
}

.path-description {
  font-size: 0.875rem;
  color: #718096;
  margin-bottom: 20px;
  line-height: 1.5;
}

.path-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  color: #718096;
  margin-bottom: 20px;
}

.path-progress {
  margin-bottom: 20px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 0.875rem;
  color: #718096;
}

.path-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.completed-count {
  font-size: 0.875rem;
  color: #48bb78;
}

.learning-suggestion {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.suggestion-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.suggestion-content h3 {
  font-size: 1.5rem;
  margin-bottom: 5px;
}

.suggestion-content p {
  opacity: 0.9;
}

/* 教程区域 */
.tutorials-section {
  padding: 80px 0;
}

.tutorials-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 40px;
}

.tutorial-card {
  transition: transform 0.3s;
  
  &:hover {
    transform: translateX(4px);
  }
}

.tutorial-content {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

.tutorial-main {
  flex: 1;
  min-width: 300px;
}

.tutorial-tags {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.difficulty-初级 {
  background-color: #f0fff4;
  color: #38a169;
  border-color: #c6f6d5;
}

.difficulty-中级 {
  background-color: #feebc8;
  color: #dd6b20;
  border-color: #fed7d7;
}

.difficulty-高级 {
  background-color: #fed7d7;
  color: #c53030;
  border-color: #fc8181;
}

.tutorial-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 10px;
  color: #2d3748;
}

.tutorial-excerpt {
  font-size: 1rem;
  color: #718096;
  margin-bottom: 20px;
  line-height: 1.6;
}

.tutorial-stats {
  display: flex;
  gap: 20px;
  font-size: 0.875rem;
  color: #a0aec0;
}

.tutorial-sidebar {
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.author-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.author-details {
  margin-top: 10px;
}

.author-name {
  font-weight: 600;
  color: #2d3748;
}

.author-title {
  font-size: 0.875rem;
  color: #718096;
}

.tutorial-action {
  width: 100%;
}

.tutorial-categories {
  margin-top: 40px;
}

.tutorial-categories h3 {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.categories-grid {
  margin-top: 20px;
}

.category-card {
  height: 150px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-4px);
  }
}

.category-icon {
  font-size: 2.5rem;
  margin-bottom: 15px;
}

.category-name {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 5px;
}

.category-count {
  font-size: 0.875rem;
  opacity: 0.9;
}

/* 工具区域 */
.tools-section {
  padding: 80px 0;
  background: #f8fafc;
}

.tools-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.tool-card {
  height: 100%;
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-4px);
  }
}

.tool-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.tool-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2d3748;
}

.tool-badges {
  display: flex;
  gap: 5px;
}

.tool-description {
  font-size: 0.875rem;
  color: #718096;
  margin-bottom: 20px;
  line-height: 1.5;
}

.tool-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tool-rating {
  font-size: 0.875rem;
}

.tool-categories {
  margin-top: 40px;
}

.tool-categories h3 {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.category-tiles {
  margin-top: 20px;
}

.tool-category-tile {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e2e8f0;
  
  &:hover {
    border-color: #667eea;
    transform: translateX(4px);
  }
}

.category-icon {
  font-size: 2rem;
  margin-right: 15px;
}

.category-info {
  flex: 1;
}

.category-name {
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 5px;
}

.category-count {
  font-size: 0.875rem;
  color: #718096;
}

/* 趋势区域 */
.trends-section {
  padding: 80px 0;
}

.trends-note {
  font-size: 0.875rem;
  color: #718096;
  display: flex;
  align-items: center;
  gap: 5px;
}

.trends-grid {
  margin-top: 30px;
}

.trend-card {
  height: 100%;
}

.trend-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.trend-category {
  font-size: 0.875rem;
  color: #718096;
}

.trend-change {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.875rem;
  font-weight: 600;
  
  &.up {
    color: #48bb78;
  }
  
  &.down {
    color: #f56565;
  }
}

.trend-keyword {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 10px;
  color: #2d3748;
}

.trend-volume {
  font-size: 0.875rem;
  color: #718096;
  margin-bottom: 15px;
}

.trend-bar {
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  transition: width 0.3s;
  
  &.up {
    background: #48bb78;
  }
  
  &.down {
    background: #f56565;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .hero-content,
  .tutorial-content {
    flex-direction: column;
  }
  
  .stats-container {
    justify-content: space-between;
  }
  
  .tool-category-tile {
    padding: 15px;
  }
  
  .suggestion-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
}

/* 修复 el-link 在 router-link 内的样式 */
a {
  text-decoration: none;
  color: inherit;
  
  &:hover {
    text-decoration: none;
  }
}

.router-link-active {
  text-decoration: none;
}
</style>