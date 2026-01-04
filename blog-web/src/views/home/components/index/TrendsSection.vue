<template>
  <section class="trends-section">
      <div class="container">
        <div class="section-header">
          <div>
            <h2 class="section-title">AI趋势观察</h2>
            <p class="section-subtitle">洞察AI技术发展动态</p>
          </div>
          <span class="trends-note">
            <i class="el-icon-info"></i> 基于过去24小时内的搜索量、讨论热度和媒体报道
          </span>
        </div>

        <el-row :gutter="15" class="trends-grid">
          <el-col 
            v-for="trend in trends" 
            :key="trend.id"
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="6"
          >
            <el-card class="trend-card" shadow="hover">
              <div class="trend-header">
                <span class="trend-category">{{ trend.category }}</span>
                <div :class="`trend-change ${trend.changePercent >= 0 ? 'up' : 'down'}`">
                  <i :class="trend.changePercent >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  {{ Math.abs(trend.changePercent) }}%
                </div>
              </div>
              <h3 class="trend-keyword">{{ trend.keyword }}</h3>
              <div class="trend-volume">{{ formatNumber(trend.searchVolume) }} 搜索量</div>
              
         
              <div class="trend-bar">
                <div 
                  class="bar-fill" 
                  :class="trend.change >= 0 ? 'up' : 'down'"
                  :style="{ width: `${Math.min(Math.abs(trend.changePercent), 100)}%` }"
                ></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div class="trends-footer">
          <el-button type="text" @click="$router.push('/trends')">
            查看完整趋势分析 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
      </div>
    </section>
  
</template>
 
<script>
import { getTrendsApi } from '@/api/home/trends'
 
export default {
  name: 'TrendsSection',
  
  data() {
    return {
      loading: false,
      trends: []
    }
  },
  
  mounted() {
    this.loadTrends()
  },
  
  methods: {
    async loadTrends() {
      this.loading = true
      try {
        const res = await getTrendsApi()
        if (res.code === 200) {
          this.trends = res.data.records || []
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    formatNumber(num) {
      if (num >= 10000) {
        return `${(num / 10000).toFixed(1)}万`
      } else if (num >= 1000) {
        return `${(num / 1000).toFixed(1)}千`
      }
      return num.toString()
    },
    
    formatTime(timeString) {
      const date = new Date(timeString)
      const now = new Date()
      const diffMinutes = Math.floor((now - date) / (1000 * 60))
      
      if (diffMinutes < 60) {
        return `${diffMinutes}分钟前更新`
      } else {
        const diffHours = Math.floor(diffMinutes / 60)
        return `${diffHours}小时前更新`
      }
    }
  }
}
</script>
<style lang="scss" scoped>
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
</style>