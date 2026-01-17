<template>
  <section class="tools-section">
    <div class="section-header">
      <h2 class="section-title">🔥 热门AI工具推荐</h2>
      <el-button type="text" @click="$router.push('/tags')">
        查看全部 <i class="el-icon-arrow-right"></i>
      </el-button>
    </div>
    
    <div class="tools-grid">
      <el-card
        v-for="(tool, index) in aiTools"
        :key="index"
        shadow="hover"
        class="tool-card"
      >
        <div class="tool-header">
          <div class="tool-category" :style="{ backgroundColor: categoryColors[tool.category] }">
            {{ tool.category }}
          </div>
          <el-rate
            v-model="tool.rating"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}"
          ></el-rate>
        </div>
        
        <div class="tool-body">
          <h3>{{ tool.name }}</h3>
          <p class="tool-desc">{{ tool.description }}</p>
          
          <div class="tool-tags">
            <el-tag
              v-for="(tag, tagIndex) in tool.tags"
              :key="tagIndex"
              size="small"
              :type="tagTypes[tagIndex % tagTypes.length]"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>
        
        <div class="tool-footer">
          <div class="tool-price">
            <span v-if="tool.isFree" class="free-tag">免费</span>
            <span v-else class="price-tag">¥{{ tool.price }}</span>
          </div>
          <div class="tool-actions">
            <el-button
              size="small"
              type="primary"
              plain
              @click="viewToolDetail(tool)"
            >
              立即体验
            </el-button>
            <el-button
              size="small"
              @click="viewToolTutorial(tool)"
            >
              教程
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </section>
</template>

<script>
import { getPopularAIToolsApi } from '@/api/home/api-ai-tool'

export default {
  name: 'AIToolsSection',
  data() {
    return {
      aiTools: [
        {
          name: 'ChatGPT-4',
          category: '对话AI',
          description: 'OpenAI最新语言模型，支持多模态输入',
          rating: 4.8,
          tags: ['对话', '写作', '编程', '分析'],
          price: 20,
          isFree: false
        },
        {
          name: 'Midjourney',
          category: '图像生成',
          description: '最强大的AI绘画工具，艺术创作必备',
          rating: 4.7,
          tags: ['绘画', '设计', '艺术', '创作'],
          price: 30,
          isFree: false
        },
        {
          name: 'Claude 3',
          category: '对话AI',
          description: 'Anthropic最新模型，长文本处理能力强',
          rating: 4.6,
          tags: ['文档', '分析', '写作', '对话'],
          price: 20,
          isFree: false
        },
        {
          name: 'Stable Diffusion',
          category: '图像生成',
          description: '开源图像生成模型，本地部署可用',
          rating: 4.5,
          tags: ['开源', '绘画', '本地部署', '自定义'],
          price: 0,
          isFree: true
        },
        {
          name: 'Notion AI',
          category: '效率工具',
          description: '智能笔记助手，提升工作效率',
          rating: 4.4,
          tags: ['笔记', '写作', '整理', '管理'],
          price: 10,
          isFree: false
        },
        {
          name: 'GitHub Copilot',
          category: '编程助手',
          description: 'AI编程助手，提升开发效率',
          rating: 4.7,
          tags: ['编程', '代码', '开发', '智能提示'],
          price: 10,
          isFree: false
        }
      ],
      categoryColors: {
        '对话AI': '#409EFF',
        '图像生成': '#67C23A',
        '效率工具': '#E6A23C',
        '编程助手': '#F56C6C'
      },
      tagTypes: ['', 'success', 'info', 'warning', 'danger']
    }
  },
  created() {
    //this.loadAIToolsData()
  },
  methods: {
    async loadAIToolsData() {
      try {
        const res = await getPopularAIToolsApi()
        if (res.code === 200) {
          this.aiTools = res.data
        }
      } catch (error) {
        console.error('加载AI工具数据失败:', error)
      }
    },
    viewToolDetail(tool) {
      //this.$router.push(`/tools/${tool.id}`)
    },
    viewToolTutorial(tool) {
      //this.$router.push(`/tools/${tool.id}/tutorials`)
    }
  }
}
</script>

<style scoped>
.tools-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
}

.tool-card {
  height: 100%;
}

.tool-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.tool-category {
  padding: 4px 12px;
  border-radius: 12px;
  color: white;
  font-size: 12px;
}

.tool-body {
  margin-bottom: 15px;
}

.tool-body h3 {
  margin: 0 0 10px;
  font-size: 16px;
}

.tool-desc {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
}

.tool-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tool-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
}

.free-tag {
  background: #67C23A;
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.price-tag {
  color: #F56C6C;
  font-weight: bold;
}

.tool-actions {
  display: flex;
  gap: 10px;
}
</style>