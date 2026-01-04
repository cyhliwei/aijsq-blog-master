<template>
  <section class="tutorials-section">
    <div class="container">
      <div class="section-header">
        <div>
          <h2 class="section-title">推荐教程</h2>
          <p class="section-subtitle">从入门到精通，精选优质教程</p>
        </div>
        <el-button type="text" @click="$router.push('/tutorials')">
          查看全部教程 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>
 
      <!-- 教程分类 -->
      <div class="tutorial-categories">
        <div 
          v-for="category in categories" 
          :key="category.name"
          class="category-tile"
          :style="{ background: category.bgColor }"
          @click="handleCategoryClick(category)"
        >
          <div class="category-icon">{{ category.icon }}</div>
          <div class="category-info">
            <div class="category-name">{{ category.name }}</div>
            <div class="category-count">{{ category.count }}个教程</div>
          </div>
        </div>
      </div>
 
      <!-- 教程列表 -->
      <el-row :gutter="20" class="tutorials-grid" v-loading="loading">
        <el-col 
          v-for="tutorial in tutorials" 
          :key="tutorial.id"
          :xs="24" 
          :sm="12" 
          :md="6"
        >
          <el-card class="tutorial-card" shadow="hover">
            <div class="tutorial-header">
              <el-tag 
                :type="getDifficultyType(tutorial.difficulty)" 
                size="small"
              >
                {{ tutorial.difficulty }}
              </el-tag>
              <el-tag v-if="tutorial.isFeatured" type="danger" size="mini">推荐</el-tag>
            </div>
            <h3 class="tutorial-title">{{ tutorial.title }}</h3>
            <p class="tutorial-excerpt">{{ tutorial.excerpt }}</p>
            <div class="tutorial-meta">
              <div class="author">
                <el-avatar :src="tutorial.author.avatar" :size="32" />
                <div class="author-info">
                  <div class="author-name">{{ tutorial.author.name }}</div>
                  <div class="author-title">{{ tutorial.author.title }}</div>
                </div>
              </div>
            </div>
            <div class="tutorial-stats">
              <span><i class="el-icon-view"></i> {{ tutorial.views }}</span>
              <span><i class="el-icon-star-on"></i> {{ tutorial.likes }}</span>
              <span><i class="el-icon-time"></i> {{ tutorial.readTime }}分钟</span>
            </div>
            <el-button 
              type="primary" 
              size="small" 
              class="tutorial-btn"
              @click="handleTutorialClick(tutorial)"
            >
              开始学习
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
 
<script>
import { getFeaturedTutorialsApi, getTutorialCategoriesApi } from '@/api/home/tutorials'
 
export default {
  name: 'TutorialsSection',
  
  data() {
    return {
      loading: false,
      tutorials: [],
      categories: []
    }
  },
  
  mounted() {
    this.loadTutorialData()
  },
  
  methods: {
    async loadTutorialData() {
      this.loading = true
      try {
        const [tutorialsRes, categoriesRes] = await Promise.all([
          getFeaturedTutorialsApi({ limit: 4 }),
          getTutorialCategoriesApi()
        ])
        
        if (tutorialsRes.code === 200) {
          this.tutorials = tutorialsRes.data || []
        }
        if (categoriesRes.code === 200) {
          this.categories = categoriesRes.data || []
        }
      } catch (error) {
        console.error('加载教程数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    handleTutorialClick(tutorial) {
      this.$router.push(`/tutorial/${tutorial.id}`)
    },
    
    handleCategoryClick(category) {
      this.$router.push(`/tutorials?category=${category.name}`)
    },
    
    getDifficultyType(difficulty) {
      switch (difficulty) {
        case '初级': return 'success'
        case '中级': return 'warning'
        case '高级': return 'danger'
        default: return 'info'
      }
    }
  }
}
</script>
<style lang="css" scoped>
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

</style>