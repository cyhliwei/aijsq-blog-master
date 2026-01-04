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
 
      <!-- 分类筛选 -->
      <div class="category-filter">
        <el-button
          v-for="category in categories"
          :key="category.value"
          :type="activeCategory === category.value ? 'primary' : 'text'"
          size="small"
          @click="changeCategory(category.value)"
        >
          {{ category.label }}
          <el-tag v-if="category.count" size="mini" type="info">
            {{ category.count }}
          </el-tag>
        </el-button>
      </div>
 
      <!-- 新闻网格 -->
      <el-row :gutter="20" class="news-grid" v-loading="loading">
        <el-col 
          v-for="article in newsList" 
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
 
      <!-- 热门话题 -->
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
import { getNewsCategoriesApi, getNewsListApi, getTrendingTopicsApi } from '@/api/home/news'
 
export default {
  name: 'NewsSection',
  
  data() {
    return {
      loading: false,
      activeCategory: 'all',
      categories: [],
      newsList: [],
      trendingTopics: []
    }
  },
  
  mounted() {
    this.loadNewsData()
  },
  
  methods: {
    async loadNewsData() {
      this.loading = true
      try {
        const [categoriesRes, topicsRes] = await Promise.all([
          getNewsCategoriesApi(),
          getTrendingTopicsApi()
        ])
        
        if (categoriesRes.code === 200) {
          this.categories = categoriesRes.data || []
        }
        if (topicsRes.code === 200) {
          this.trendingTopics = topicsRes.data || []
        }
        
        // 加载默认分类的新闻
        await this.loadNewsList()
      } catch (error) {
        console.error('加载新闻数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    async loadNewsList() {
      try {
        const res = await getNewsListApi({
          category: this.activeCategory,
          limit: 6
        })
        if (res.code === 200) {
          this.newsList = res.data || []
        }
      } catch (error) {
        console.error('加载新闻列表失败:', error)
      }
    },
    
    changeCategory(category) {
      this.activeCategory = category
      this.loadNewsList()
    },
    
    formatTime(timeString) {
      const date = new Date(timeString)
      const now = new Date()
      const diffHours = Math.floor((now - date) / (1000 * 60 * 60))
      
      if (diffHours < 1) return `${Math.floor((now - date) / (1000 * 60))}分钟前`
      if (diffHours < 24) return `${diffHours}小时前`
      return `${Math.floor(diffHours / 24)}天前`
    }
  }
}
</script>
<style lang="css" scoped>
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

</style>