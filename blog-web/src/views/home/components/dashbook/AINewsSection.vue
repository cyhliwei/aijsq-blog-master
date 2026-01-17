<template>
  <section class="news-section">
    <div class="container">
      <div class="section-header">
        <div>
          <h2 class="section-title">今日AI热点</h2>
          <p class="section-subtitle">了解最新的AI动态与技术突破</p>
        </div>
        <el-button type="text" @click="$router.push('/ai-news')">
          查看全部 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>

      <div class="category-filter">
        <el-button
          v-for="category in newsCategories"
          :key="category.value"
          :type="activeCategory === category.value ? 'primary' : 'text'"
          size="small"
          @click="changeCategory(category.value)"
        >
          {{ category.label }}
          <el-tag v-if="category.value !== 'all'" size="mini" type="info">
            {{ category.count }}
          </el-tag>
        </el-button>
      </div>

      <el-row :gutter="20" class="news-grid">
        <el-col 
          v-for="article in filteredNews" 
          :key="article.id"
          :xs="24" 
          :sm="12" 
          :md="8"
        >
          <router-link :to="`/ai-news/${article.id}`">
            <el-card class="news-card" shadow="hover">
              <div class="news-header">
                <el-tag 
                  :type="article.isHot ? 'danger' : 'info'" 
                  size="small"
                >
                  {{ article.category }}
                </el-tag>
                <span class="news-time">{{ formatTime(article.publishedAt) }}</span>
              </div>
              <h3 class="news-title">{{ article.title }}</h3>
              <p class="news-excerpt">{{ article.excerpt }}</p>
              <div class="news-footer">
                <div class="news-meta">
                  <span>{{ article.author }}</span>
                  <span>· {{ article.readTime }}分钟阅读</span>
                </div>
                <div class="news-stats">
                  <span><i class="el-icon-view"></i> {{ article.views }}</span>
                  <span><i class="el-icon-chat-dot-round"></i> {{ article.comments }}</span>
                </div>
              </div>
            </el-card>
          </router-link>
        </el-col>
      </el-row>

      <div class="trending-topics">
        <h3>热门话题</h3>
        <div class="topic-tags">
          <el-tag
            v-for="topic in trendingTopics"
            :key="topic.name"
            class="topic-tag"
            effect="plain"
            @click="$router.push(`/ai-news/tag/${topic.name.toLowerCase()}`)"
          >
            #{{ topic.name }}
            <el-tag size="mini" type="primary">{{ topic.count }}</el-tag>
          </el-tag>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { getAINewsListApi, getAINewsCategoriesApi, getTrendingTopicsApi } from '@/api/home/api-ai-news'

export default {
  name: 'AINewsSection',
  data() {
    return {
      newsList: [
        {
          id: 1,
          title: 'OpenAI发布GPT-4.5，推理能力大幅提升',
          excerpt: '新版本在代码生成和逻辑推理方面有显著改进',
          category: 'AI快讯',
          isHot: true,
          publishedAt: '2024-01-15T10:30:00',
          author: '张AI',
          readTime: 5,
          views: 12500,
          comments: 128
        },
        {
          id: 2,
          title: 'AI Agent技术突破：自主完成任务',
          excerpt: '最新的AI Agent可以独立完成复杂的工作流程',
          category: '深度解读',
          isHot: true,
          publishedAt: '2024-01-15T09:15:00',
          author: '李智能',
          readTime: 8,
          views: 8900,
          comments: 76
        },
        {
          id: 3,
          title: '国产AI芯片性能达到国际先进水平',
          excerpt: '新一代AI芯片在能效比上表现突出',
          category: '行业报告',
          isHot: false,
          publishedAt: '2024-01-14T14:20:00',
          author: '王创新',
          readTime: 12,
          views: 15600,
          comments: 204
        }
      ],
      newsCategories: [
        { label: '全部', value: 'all', count: 156 },
        { label: 'AI快讯', value: 'news', count: 89 },
        { label: '深度解读', value: 'analysis', count: 45 },
        { label: '行业报告', value: 'report', count: 18 },
        { label: '政策动态', value: 'policy', count: 12 }
      ],
       trendingTopics: [
        { name: 'GPT-4.5', count: 128 },
        { name: 'Sora', count: 95 },
        { name: 'Gemini Ultra', count: 76 },
        { name: 'AI芯片', count: 64 },
        { name: 'AI安全', count: 52 },
        { name: '开源模型', count: 48 },
        { name: 'AI立法', count: 41 },
        { name: '多模态', count: 39 }
      ],
      activeCategory: 'all'
    }
  },
  computed: {
    filteredNews() {
      if (this.activeCategory === 'all') {
        return this.newsList
      }
      return this.newsList.filter(item => item.category === this.activeCategory)
    }
  },
  created() {
    this.loadAINewsData()
  },
  methods: {
    async loadAINewsData() {
      try {
        const [newsRes, categoriesRes, topicsRes] = await Promise.all([
          getAINewsListApi({ limit: 3 }),
          getAINewsCategoriesApi(),
          getTrendingTopicsApi()
        ])
        
        if (newsRes.code === 200) {
          this.newsList = newsRes.data
        }
        if (categoriesRes.code === 200) {
          this.newsCategories = categoriesRes.data
        }
        if (topicsRes.code === 200) {
          this.trendingTopics = topicsRes.data
        }
      } catch (error) {
        console.error('加载AI热点数据失败:', error)
      }
    },
    changeCategory(category) {
      this.activeCategory = category
    },
    formatTime(timeString) {
      const date = new Date(timeString)
      const now = new Date()
      const diffHours = Math.floor((now - date) / (1000 * 60 * 60))
      
      if (diffHours < 1) {
        return `${Math.floor((now - date) / (1000 * 60))}分钟前`
      } else if (diffHours < 24) {
        return `${diffHours}小时前`
      } else {
        return `${Math.floor(diffHours / 24)}天前`
      }
    }
  }
}
</script>

<style scoped>
.news-section {
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
}

.news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1) !important;
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
  height: 46px;
}

.topic-tag:hover {
  background-color: #ebf4ff;
  transform: translateY(-2px);
}
</style>