<template>
  <section class="learning-paths">
    <div class="container">
      <el-card class="learning-suggestion" style="margin-bottom: 50px;color: #409eff;">
        <div class="suggestion-content">
          <div>
            <h3>不确定从何开始？</h3>
            <p>AGI点亮学习捷径，人人因AI强大</p>
          </div>
          <el-button type="primary" size="medium" round @click="startAssessment">
            通往AGI之路 >>
          </el-button>
        </div>
      </el-card>
      <div class="section-header">
        <div>
          <h2 class="section-title">AI学习路径</h2>
          <p class="section-subtitle">选择你的职业方向，开始系统化学习</p>
        </div>
        <!-- <el-button type="text" @click="$router.push('/academy/learning-paths')"> -->
        <el-button type="text">
          查看全部路径  <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>

      <el-row :gutter="20" class="paths-grid">
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
            
            <div class="path-meta">
              <span><i class="el-icon-collection"></i> {{ path.courses }}个课程</span>
              <span><i class="el-icon-time"></i> {{ path.hours }}小时</span>
            </div>
            
            <div class="path-progress">
              <div class="progress-header">
                <span>学习进度</span>
                <span>{{ path.progress }}%</span>
              </div>
              <el-progress :percentage="path.progress" :show-text="false" />
            </div>
            
            <div class="path-actions">
              <el-button type="primary" plain @click="$router.push(path.url_)">
                开始学习
              </el-button>
              <span class="completed-count">
                <i class="el-icon-success"></i> 
                {{ Math.floor(path.courses * 0.3) }}人已学
              </span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      
    </div>
  </section>
</template>

<script>
import { getLearningPathsApi, startSkillAssessmentApi } from '@/api/home/api-learning-path'

export default {
  name: 'LearningPathsSection',
  data() {
    return {
      learningPaths: [
        // {
        //   id: 0,
        //   title: '通往AGI之路',
        //   description: 'AGI点亮学习捷径，人人因AI强大',
        //   icon: 'el-icon-picture-outline',
        //   difficulty: '初级',
        //   courses: 12,
        //   hours: 36,
        //   progress: 65,
        //   url_:'/waytoagi/1'
        // },
        {
          id: 1,
          title: '设计师AI路径',
          description: '从AI辅助设计到AI原生设计',
          icon: 'el-icon-picture-outline',
          difficulty: '初级',
          courses: 12,
          hours: 36,
          progress: 65,
          url_:'/shejishi_paths'
        },
        {
          id: 2,
          title: '程序员AI路径',
          description: 'AI编程助手到AI应用开发',
          icon: 'el-icon-cpu',
          difficulty: '中级',
          courses: 15,
          hours: 45,
          progress: 30,
          url_:'/chenxuyuan_paths'
        },
        {
          id: 3,
          title: '运营人员AI路径',
          description: 'AI内容创作到数据驱动运营',
          icon: 'el-icon-data-line',
          difficulty: '初级',
          courses: 10,
          hours: 30,
          progress: 80,
          url_:'/yunyin_paths'
        },
        {
          id: 4,
          title: '学生AI路径',
          description: '学术研究到AI创新项目',
          icon: 'el-icon-notebook-2',
          difficulty: '初级',
          courses: 538,
          hours: 24,
          progress: 45,
          url_:'/xuesheng_paths'
        },
        {
          id: 5,
          title: '管理者AI路径',
          description: 'AI战略到团队AI赋能',
          icon: 'el-icon-s-custom',
          difficulty: '高级',
          courses: 96,
          hours: 18,
          progress: 15,
          url_:'/guanli_paths'
        },
        {
          id: 6,
          title: 'AI岗位学习路线',
          description: 'AI战略到团队AI赋能',
          icon: 'el-icon-office-building',
          difficulty: '高级',
          courses: 866,
          hours: 18,
          progress: 15,
          url_:'/job_paths'
        }
      ],
    }
  },
  created() {
    //this.loadLearningPathsData()
  },
  methods: {
    async loadLearningPathsData() {
      try {
        const res = await getLearningPathsApi()
        if (res.code === 200) {
          this.learningPaths = res.data
        }
      } catch (error) {
        console.error('加载学习路径数据失败:', error)
      }
    },
    getDifficultyType(difficulty) {
      switch (difficulty) {
        case '初级': return 'success'
        case '中级': return 'warning'
        case '高级': return 'danger'
        default: return 'info'
      }
    },
    startAssessment() {
      this.$router.push('/waytoagi/jNnFGW2JvwlbOzaRrwwqsCvS51YWuALUD62e7IuSxMBYLv')
    }
  }
}
</script>

<style scoped>
.learning-paths {
  padding: 30px 0;
  padding-bottom: 40px;
  margin-bottom: 40px;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
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

.paths-grid {
  margin-bottom: 40px;
}

.path-card {
  height: 100%;
  padding-bottom: 10px;
  transition: transform 0.3s;
}

.path-card:hover {
  transform: translateY(-4px);
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
.icon-0 { background: linear-gradient(135deg, #43e97b 0%, #764ba2 100%); }
.icon-1 { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.icon-2 { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.icon-3 { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.icon-4 { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.icon-5 { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
.icon-6 { background: linear-gradient(135deg, #8970fa 0%, #fee140 100%); }

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