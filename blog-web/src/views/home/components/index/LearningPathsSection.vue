<template>
  <section class="learning-paths">
    <div class="container">
      <div class="section-header">
        <div>
          <h2 class="section-title">AI学习路径</h2>
          <p class="section-subtitle">选择你的职业方向，开始系统化学习</p>
        </div>
        <el-button type="text" @click="$router.push('/academy/learning-paths')">
          查看全部路径 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>
 
      <el-row :gutter="20" class="paths-grid" v-loading="loading">
        <el-col 
          v-for="path in learningPaths" 
          :key="path.id"
          :xs="24" 
          :sm="12" 
          :md="8"
        >
          <el-card class="path-card" shadow="hover">
            <div class="path-header">
              <div class="path-icon" :class="`icon-${path.id}`">
                <i :class="path.icon"></i>
              </div>
              <div class="path-difficulty">
                <el-tag 
                  size="mini" 
                  :type="getDifficultyType(path.difficulty)"
                >
                  {{ path.difficulty }}
                </el-tag>
              </div>
            </div>
            <h3 class="path-title">{{ path.title }}</h3>
            <p class="path-description">{{ path.description }}</p>
            <div class="path-stats">
              <span><i class="el-icon-reading"></i> {{ path.courses }}门课程</span>
              <span><i class="el-icon-time"></i> {{ path.hours }}小时</span>
            </div>
            <div class="path-progress">
              <span>学习进度: {{ path.progress }}%</span>
              <el-progress 
                :percentage="path.progress" 
                :stroke-width="6"
                :show-text="false"
              />
            </div>
            <el-button 
              type="primary" 
              size="small" 
              class="path-btn"
              @click="handlePathClick(path)"
            >
              {{ path.progress > 0 ? '继续学习' : '开始学习' }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
 
<script>
import { getLearningPathsApi } from '@/api/home/learning'
 
export default {
  name: 'LearningPathsSection',
  
  data() {
    return {
      loading: false,
      learningPaths: []
    }
  },
  
  mounted() {
    this.loadLearningPaths()
  },
  
  methods: {
    async loadLearningPaths() {
      this.loading = true
      try {
        const res = await getLearningPathsApi()
        if (res.code === 200) {
          this.learningPaths = res.data || []
        }
      } catch (error) {
        console.error('加载学习路径失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    handlePathClick(path) {
      this.$router.push(`/academy/learning-path/${path.id}`)
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

</style>
