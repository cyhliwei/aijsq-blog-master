<template>
  <section class="tools-section">
    <div class="container">
      <div class="section-header">
        <div>
          <h2 class="section-title">AI工具库</h2>
          <p class="section-subtitle">发现和使用最优秀的AI工具</p>
        </div>
        <el-button type="text" @click="$router.push('/tools')">
          查看全部工具 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>
 
      <el-row :gutter="20" v-loading="loading">
        <!-- 左侧：热门工具 -->
        <el-col :xs="24" :sm="24" :md="16">
          <el-card class="hot-tools-card" shadow="never">
            <div slot="header" class="card-header">
              <span class="card-title">热门工具</span>
              <el-button type="text" @click="$router.push('/tools?sort=hot')">更多</el-button>
            </div>
            <el-table :data="tools" style="width: 100%">
              <el-table-column label="工具名称" width="200">
                <template slot-scope="scope">
                  <div class="tool-name">
                    <el-avatar :src="scope.row.logo" :size="32" />
                    <span>{{ scope.row.name }}</span>
                    <el-tag v-if="scope.row.isNew" type="danger" size="mini">NEW</el-tag>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="category" label="分类" width="120" />
              <el-table-column prop="description" label="描述" />
              <el-table-column label="评分" width="120">
                <template slot-scope="scope">
                  <el-rate 
                    v-model="scope.row.rating" 
                    disabled 
                    show-score 
                    text-color="#ff9900"
                  />
                </template>
              </el-table-column>
              <el-table-column label="价格" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getPriceType(scope.row.price)" size="mini">
                    {{ getPriceLabel(scope.row.price) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button 
                    type="primary" 
                    size="mini" 
                    @click="handleToolClick(scope.row)"
                  >
                    查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
 
        <!-- 右侧：工具分类 -->
        <el-col :xs="24" :sm="24" :md="8">
          <el-card class="tool-categories-card" shadow="never">
            <div slot="header" class="card-header">
              <span class="card-title">工具分类</span>
            </div>
            <div class="tool-categories">
              <div 
                v-for="cat in toolCategories" 
                :key="cat.name"
                class="tool-category-item"
                @click="handleCategoryClick(cat)"
              >
                <div class="category-icon">{{ cat.icon }}</div>
                <div class="category-info">
                  <div class="category-name">{{ cat.name }}</div>
                  <div class="category-count">{{ cat.count }}个工具</div>
                </div>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
 
<script>
import { getHotToolsApi, getToolCategoriesApi } from '@/api/home/tools'
 
export default {
  name: 'ToolsSection',
  
  data() {
    return {
      loading: false,
      tools: [],
      toolCategories: []
    }
  },
  
  mounted() {
    this.loadToolsData()
  },
  
  methods: {
    async loadToolsData() {
      this.loading = true
      try {
        const [toolsRes, categoriesRes] = await Promise.all([
          getHotToolsApi({ limit: 10 }),
          getToolCategoriesApi()
        ])
        
        if (toolsRes.code === 200) {
          this.tools = toolsRes.data || []
        }
        if (categoriesRes.code === 200) {
          this.toolCategories = categoriesRes.data || []
        }
      } catch (error) {
        console.error('加载工具数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    handleToolClick(tool) {
      this.$router.push(`/tool/${tool.id}`)
    },
    
    handleCategoryClick(category) {
      this.$router.push(`/tools?category=${category.name}`)
    },
    
    getPriceType(price) {
      switch (price) {
        case 'free': return 'success'
        case 'freemium': return 'warning'
        case 'paid': return 'danger'
        default: return 'info'
      }
    },
    
    getPriceLabel(price) {
      switch (price) {
        case 'free': return '免费'
        case 'freemium': return '免费+付费'
        case 'paid': return '付费'
        default: return price
      }
    }
  }
}
</script>
<style lang="css" scoped>
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
</style>