<template>
  <section class="tutorials-section">
    <div class="container">
      <div class="section-header">
        <div>
          <h2 class="section-title">热门AI教程</h2>
          <p class="section-subtitle">精选实用教程，快速掌握AI技能</p>
        </div>
        <!-- <el-button type="text" @click="$router.push('/academy')"> -->
        <el-button type="text">
          更多教程 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>

      <div class="tutorials-list">
        <el-card 
          v-for="tutorial in tutorials" 
          :key="tutorial.id"
          class="tutorial-card"
          shadow="hover"
        >
          <router-link :to="`/post/${tutorial.id}`">
            <div class="tutorial-content">
              <div class="tutorial-main">
                <div class="tutorial-tags">
                  <el-tag size="small" :type="tutorial.isFeatured ? 'danger' : 'success'">
                    {{ tutorial.category }}
                  </el-tag>
                  <el-tag 
                    size="small" 
                    :class="`difficulty-${tutorial.difficulty}`"
                  >
                    {{ tutorial.difficulty }}
                  </el-tag>
                  <el-tag v-if="tutorial.isFeatured" type="warning" size="small">
                    精选
                  </el-tag>
                </div>
                
                <h3 class="tutorial-title">{{ tutorial.title }}</h3>
                <p class="tutorial-excerpt">{{ tutorial.excerpt }}</p>
                
                <div class="tutorial-stats">
                  <span><i class="el-icon-time"></i> {{ tutorial.readTime }}分钟阅读</span>
                  <span><i class="el-icon-view"></i> {{ formatNumber(tutorial.views) }}</span>
                  <span><i class="el-icon-star-off"></i> {{ tutorial.likes }}</span>
                </div>
              </div>
              
              <div class="tutorial-sidebar">
                <div class="author-info">
                  <el-avatar :src="tutorial.author_avatar" size="medium" />
                  <div class="author-details">
                    <div class="author-name">{{ tutorial.author_name }}</div>
                    <div class="author-title">{{ tutorial.author_title }}</div>
                  </div>
                </div>
                <el-button type="primary" class="tutorial-action">
                  <i class="el-icon-reading"></i> 开始学习
                </el-button>
              </div>
            </div>
          </router-link>
        </el-card>
      </div>

      <div class="tutorial-categories">
        <h3>教程分类</h3>
        <el-row :gutter="15" class="categories-grid">
          <el-col 
            v-for="category in tutorialCategories" 
            :key="category.name"
            :xs="12" 
            :sm="4"
            style="margin: 10px 0px;"
          >
            <div 
              class="category-card"
              :style="{ background: category.bg_color }"
              @click="$router.push(category.path)"

            >
              <!-- <div class="category-icon">{{ category.icon }}</div> -->
               <i :class="category.icon"></i>
              <div class="category-name">{{ category.name }}</div>
              <div class="category-count">{{ category.count }}个教程</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </section>
</template>

<script>
import { getPopularTutorialsApi, getTutorialCategoriesApi } from '@/api/home/api-tutorial'

export default {
  name: 'PopularTutorialsSection',
  data() {
    return {
      tutorials: [
        {
          id:315,
          title: 'ChatGPT高级提示词工程实战',
          summary: '掌握高级Prompt技巧，让AI输出更精准的结果',
          level: 2,
          time: '3小时前',
          views: '1.2k',
          skills: ['提示词', 'GPT-4', '优化技巧', '案例分析'],
          author: {
            name: 'AI技术达人',
            avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=100&q=80'
          }
        },
        {
          title: '谷歌Nano-Banana Pro模型：玩法超多，创意无限！',
          summary: '想让AI精准理解你的中文创意？想一次生成多角色、多场景的复杂画面？',
          level: 3,
          time: '1天前',
          views: '2.8k',
          skills: ['SD', '本地部署', '模型训练', 'WebUI'],
          author: {
            name: '技术老张',
            avatar: 'https://images.unsplash.com/photo-1568602471122-7832951cc4c5?auto=format&fit=crop&w=100&q=80'
          }
        },
        {
          title: 'AI辅助编程：GitHub Copilot实战',
          summary: '提升编程效率的AI工具使用全攻略',
          level: 2,
          time: '2天前',
          views: '1.8k',
          skills: ['编程', 'Copilot', 'VSCode', '效率提升'],
          author: {
            name: '代码艺术家',
            avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=100&q=80'
          }
        }
      ],
      tutorialCategories: [
        { name: 'AI绘画', count: 156, icon: '🎨', bg_color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: '大语言模型', count: 89, icon: '🤖', bg_color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { name: 'AI视频', count: 42, icon: '🎥', bg_color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { name: 'AI编程', count: 67, icon: '💻', bg_color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
      ],
    }
  },
  created() {
    this.loadTutorialData()
  },
  methods: {
    async loadTutorialData() {
      try {
        const [tutorialsRes, categoriesRes] = await Promise.all([
          getPopularTutorialsApi(),
          getTutorialCategoriesApi()
        ])
        
        if (tutorialsRes.code === 200) {
          this.tutorials = tutorialsRes.data
        }
        if (categoriesRes.code === 200) {
          this.tutorialCategories = categoriesRes.data
        }
      } catch (error) {
        console.error('加载教程数据失败:', error)
      }
    },
    formatNumber(num) {
      if (num >= 10000) {
        return `${(num / 10000).toFixed(1)}万`
      } else if (num >= 1000) {
        return `${(num / 1000).toFixed(1)}千`
      }
      return num.toString()
    }
  }
}
</script>

<style scoped>
.tutorials-section {
  padding: 80px 0;
  margin-bottom: 40px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-title {
  font-size: 24px;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
}

.section-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 20px;
  background: #409EFF;
  margin-right: 10px;
}

.section-subtitle {
  color: #909399;
  margin: 5px 0 0;
}

.tutorials-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 40px;
}

.tutorial-card {
  transition: transform 0.3s;
}

.tutorial-card:hover {
  transform: translateX(4px);
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
}

.category-card:hover {
  transform: translateY(-4px);
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