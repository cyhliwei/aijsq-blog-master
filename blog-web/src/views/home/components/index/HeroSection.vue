<template>
  <section class="hero-section">
    <div class="container">
      <div class="hero-content">
        <div class="hero-text">
          <el-tag class="hero-tag" type="primary">引领AI学习新时代</el-tag>
          <h1 class="hero-title">
            探索
            <span class="text-gradient">人工智能的</span>
            <br />
            无限可能
          </h1>
          <p class="hero-description">
            AI技术圈为您提供最新的AI资讯、实用教程、工具评测和系统学习路径，
            助力您在AI时代保持竞争力。
          </p>
          
          <!-- 搜索框 -->
          <div class="hero-search">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索AI工具、教程或资讯..."
              size="large"
              class="search-input"
              @keyup.enter.native="handleSearch"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
            </el-input>
            <p class="search-hint">
              热门搜索：
              <el-link 
                v-for="tag in trendingTags" 
                :key="tag"
                type="primary"
              >
                {{ tag }}
              </el-link>
            </p>
          </div>
          
          <!-- 统计数据 -->
          <div class="stats-container">
            <div class="stat-item" v-for="stat in stats" :key="stat.label">
              <div class="stat-number">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </div>
        
        <!-- 轮播图 -->
        <div class="hero-carousel">
          <el-carousel :interval="4000" height="400px">
            <el-carousel-item v-for="(slide, index) in carouselData" :key="index">
              <div 
                class="slide-content"
                :style="{ backgroundImage: `url(${slide.imageUrl})`, backgroundSize: 'cover', backgroundPosition: 'center' }"
              >
                <div class="slide-overlay"></div>
                <div class="slide-text">
                  <h3>{{ slide.title }}</h3>
                  <p>{{ slide.description }}</p>
                  <el-button type="primary" round class="slide-btn" @click="handleSlideClick(slide)">
                    了解更多
                  </el-button>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>
  </section>
</template>
 
<script>
import { getHeroCarouselApi, getTrendingTagsApi, getHomeStatsApi } from '@/api/home/hero'
 
export default {
  name: 'HeroSection',
  
  data() {
    return {
      loading: false,
      searchKeyword: '',
      carouselData: [],
      trendingTags: [],
      stats: []
    }
  },
  
  mounted() {
    this.loadHeroData()
  },
  
  methods: {
    async loadHeroData() {
      this.loading = true
      try {
        // 并行请求多个接口
        const [carouselRes, tagsRes, statsRes] = await Promise.all([
          getHeroCarouselApi(),
          getTrendingTagsApi(),
          getHomeStatsApi()
        ])
        
        if (carouselRes.code === 200) this.carouselData = carouselRes.data || []
        if (tagsRes.code === 200) this.trendingTags = tagsRes.data || []
        if (statsRes.code === 200) this.stats = statsRes.data || []
      } catch (error) {
        console.error('加载英雄区域数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    handleSearch() {
      this.$store.commit('SET_SEARCH_VISIBLE', true)
    },
    
    handleSlideClick(slide) {
      if (slide.link) {
        window.open(slide.link, '_blank')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
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
</style>
 