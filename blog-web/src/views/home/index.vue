<template>
  <div class="home-page">
    <!-- Hero区域 -->
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
                热门搜索：<el-link type="primary">ChatGPT</el-link> · 
                <el-link type="primary">Midjourney</el-link> · 
                <el-link type="primary">Stable Diffusion</el-link>
              </p>
            </div>
            
            <!-- 统计数据 -->
            <div class="stats-container">
              <div class="stat-item">
                <div class="stat-number">500+</div>
                <div class="stat-label">AI工具评测</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">1000+</div>
                <div class="stat-label">教程文章</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">50K+</div>
                <div class="stat-label">学习用户</div>
              </div>
            </div>
          </div>
          
          <!-- Hero轮播图 -->
          <div class="hero-carousel">
            <el-carousel :interval="4000" height="400px">
              <el-carousel-item v-for="(slide, index) in heroSlides" :key="index">
                <div 
                  class="slide-content"
                  :style="{ background: slide.bgColor }"
                >
                  <div class="slide-overlay"></div>
                  <div class="slide-text">
                    <h3>{{ slide.title }}</h3>
                    <p>{{ slide.description }}</p>
                    <el-button type="primary" round class="slide-btn">
                      了解更多
                    </el-button>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
            
            <!-- 趋势标签 -->
            <div class="trend-tags">
              <el-tag v-for="tag in trendingTags" :key="tag" class="trend-tag" effect="dark">
                <i class="el-icon-trending-up"></i>
                {{ tag }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 今日AI热点 -->
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

        <!-- 新闻网格 -->
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

    <!-- AI学习路径 -->
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
              
              <!-- 进度条 -->
              <div class="path-progress">
                <div class="progress-header">
                  <span>学习进度</span>
                  <span>{{ path.progress }}%</span>
                </div>
                <el-progress :percentage="path.progress" :show-text="false" />
              </div>
              
              <div class="path-actions">
                <el-button type="primary" plain @click="$router.push(`/academy/path/${path.id}`)">
                  开始学习
                </el-button>
                <span class="completed-count">
                  <i class="el-icon-success"></i> 
                  {{ Math.floor(path.courses * 0.3) }}人已完成
                </span>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 学习建议 -->
        <el-card class="learning-suggestion">
          <div class="suggestion-content">
            <div>
              <h3>不确定从何开始？</h3>
              <p>完成我们的AI技能评估，获取个性化学习建议</p>
            </div>
            <el-button type="primary" size="medium" round>
              开始技能评估
            </el-button>
          </div>
        </el-card>
      </div>
    </section>

    <!-- 热门AI教程 -->
    <section class="tutorials-section">
      <div class="container">
        <div class="section-header">
          <div>
            <h2 class="section-title">热门AI教程</h2>
            <p class="section-subtitle">精选实用教程，快速掌握AI技能</p>
          </div>
          <el-button type="text" @click="$router.push('/academy')">
            更多教程 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>

        <!-- 教程列表 -->
        <div class="tutorials-list">
          <el-card 
            v-for="tutorial in tutorials" 
            :key="tutorial.id"
            class="tutorial-card"
            shadow="hover"
          >
            <router-link :to="`/academy/tutorial/${tutorial.id}`">
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
                    <el-avatar :src="tutorial.author.avatar" size="medium" />
                    <div class="author-details">
                      <div class="author-name">{{ tutorial.author.name }}</div>
                      <div class="author-title">{{ tutorial.author.title }}</div>
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

        <!-- 教程分类 -->
        <div class="tutorial-categories">
          <h3>教程分类</h3>
          <el-row :gutter="15" class="categories-grid">
            <el-col 
              v-for="category in tutorialCategories" 
              :key="category.name"
              :xs="12" 
              :sm="6"
            >
              <div 
                class="category-card"
                :style="{ background: category.bgColor }"
                @click="$router.push(`/academy/category/${category.name.toLowerCase()}`)"
              >
                <div class="category-icon">{{ category.icon }}</div>
                <div class="category-name">{{ category.name }}</div>
                <div class="category-count">{{ category.count }}个教程</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </section>

    <!-- AI工具推荐 -->
    <section class="tools-section">
      <div class="container">
        <div class="section-header">
          <div>
            <h2 class="section-title">热门AI工具</h2>
            <p class="section-subtitle">精选实用AI工具推荐</p>
          </div>
          <el-button type="text" @click="$router.push('/tools')">
            更多工具 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>

        <div class="tools-list">
          <el-card 
            v-for="tool in tools" 
            :key="tool.id"
            class="tool-card"
            shadow="hover"
          >
            <router-link :to="`/tools/${tool.id}`">
              <div class="tool-content">
                <div class="tool-header">
                  <h3 class="tool-name">{{ tool.name }}</h3>
                  <div class="tool-badges">
                    <el-tag 
                      v-if="tool.isNew" 
                      type="danger" 
                      size="mini"
                    >
                      新
                    </el-tag>
                    <el-tag 
                      :type="getPriceType(tool.price)" 
                      size="mini"
                    >
                      <i class="el-icon-money"></i> {{ getPriceLabel(tool.price) }}
                    </el-tag>
                  </div>
                </div>
                
                <p class="tool-description">{{ tool.description }}</p>
                
                <div class="tool-footer">
                  <el-tag size="small">{{ tool.category }}</el-tag>
                  <div class="tool-rating">
                    <el-rate 
                      v-model="tool.rating" 
                      disabled 
                      show-score 
                      :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                    />
                  </div>
                </div>
              </div>
            </router-link>
          </el-card>
        </div>

        <!-- 工具分类 -->
        <div class="tool-categories">
          <h3>工具分类</h3>
          <el-row :gutter="15" class="category-tiles">
            <el-col 
              v-for="cat in toolCategories" 
              :key="cat.name"
              :xs="12" 
              :sm="8"
            >
              <div 
                class="tool-category-tile"
                @click="$router.push(`/tools/category/${cat.name.toLowerCase()}`)"
              >
                <div class="category-icon">{{ cat.icon }}</div>
                <div class="category-info">
                  <div class="category-name">{{ cat.name }}</div>
                  <div class="category-count">{{ cat.count }}个工具</div>
                </div>
                <i class="el-icon-arrow-right"></i>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </section>

    <!-- AI趋势观察 -->
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
                <div :class="`trend-change ${trend.change >= 0 ? 'up' : 'down'}`">
                  <i :class="trend.change >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  {{ Math.abs(trend.change) }}%
                </div>
              </div>
              <h3 class="trend-keyword">{{ trend.keyword }}</h3>
              <div class="trend-volume">{{ formatNumber(trend.volume) }} 搜索量</div>
              
              <!-- 趋势条 -->
              <div class="trend-bar">
                <div 
                  class="bar-fill" 
                  :class="trend.change >= 0 ? 'up' : 'down'"
                  :style="{ width: `${Math.min(Math.abs(trend.change), 100)}%` }"
                ></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  
  data() {
    return {
      searchKeyword: '',
      activeCategory: 'all',
      
      heroSlides: [
        {
          title: '掌握AI，从学习开始',
          description: '系统化AI学习路径，从入门到精通',
          bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
        },
        {
          title: '最新AI工具评测',
          description: '发现最适合你的AI工具',
          bgColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
        },
        {
          title: 'AI技术深度解析',
          description: '前沿技术分析与实战指南',
          bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
        }
      ],
      
      trendingTags: ['AI绘画', '大语言模型', '自动化', 'AI编程', '提示词工程'],
      
      newsCategories: [
        { label: '全部', value: 'all', count: 156 },
        { label: 'AI快讯', value: 'news', count: 89 },
        { label: '深度解读', value: 'analysis', count: 45 },
        { label: '行业报告', value: 'report', count: 18 },
        { label: '政策动态', value: 'policy', count: 12 }
      ],
      
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
      
      learningPaths: [
        {
          id: 1,
          title: '设计师AI路径',
          description: '从AI辅助设计到AI原生设计',
          icon: 'el-icon-picture-outline',
          difficulty: '初级',
          courses: 12,
          hours: 36,
          progress: 65
        },
        {
          id: 2,
          title: '程序员AI路径',
          description: 'AI编程助手到AI应用开发',
          icon: 'el-icon-cpu',
          difficulty: '中级',
          courses: 15,
          hours: 45,
          progress: 30
        },
        {
          id: 3,
          title: '运营人员AI路径',
          description: 'AI内容创作到数据驱动运营',
          icon: 'el-icon-data-line',
          difficulty: '初级',
          courses: 10,
          hours: 30,
          progress: 80
        },
        {
          id: 4,
          title: '学生AI路径',
          description: '学术研究到AI创新项目',
          icon: 'el-icon-notebook-2',
          difficulty: '初级',
          courses: 8,
          hours: 24,
          progress: 45
        },
        {
          id: 5,
          title: '管理者AI路径',
          description: 'AI战略到团队AI赋能',
          icon: 'el-icon-s-custom',
          difficulty: '高级',
          courses: 6,
          hours: 18,
          progress: 15
        }
      ],
      
      tutorials: [
        {
          id: 1,
          title: 'Midjourney V6完整使用指南',
          excerpt: '从基础提示词到高级参数设置，掌握AI绘画核心技术',
          category: 'AI绘画',
          difficulty: '中级',
          isFeatured: true,
          readTime: 15,
          views: 12500,
          likes: 856,
          author: {
            name: '张AI',
            avatar: 'https://picsum.photos/50/50',
            title: 'AI绘画专家'
          }
        },
        {
          id: 2,
          title: 'ChatGPT提示词工程实战',
          excerpt: '100+实用提示词模板，提升你的AI对话效率10倍',
          category: '大语言模型',
          difficulty: '初级',
          isFeatured: true,
          readTime: 20,
          views: 18700,
          likes: 1245,
          author: {
            name: '李智能',
            avatar: 'https://picsum.photos/50/50?random=1',
            title: 'LLM研究员'
          }
        }
      ],
      
      tutorialCategories: [
        { name: 'AI绘画', count: 156, icon: '🎨', bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: '大语言模型', count: 89, icon: '🤖', bgColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { name: 'AI视频', count: 42, icon: '🎥', bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { name: 'AI编程', count: 67, icon: '💻', bgColor: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
      ],
      
      tools: [
        {
          id: 1,
          name: 'ChatGPT',
          description: '最流行的AI对话助手',
          category: '文本生成',
          rating: 4.9,
          price: 'freemium',
          isNew: false
        },
        {
          id: 2,
          name: 'Midjourney',
          description: '高质量的AI图像生成',
          category: '图像生成',
          rating: 4.8,
          price: 'paid',
          isNew: false
        },
        {
          id: 3,
          name: 'Claude',
          description: 'Anthropic的AI助手',
          category: '文本生成',
          rating: 4.7,
          price: 'freemium',
          isNew: true
        }
      ],
      
      toolCategories: [
        { name: '文本生成', count: 45, icon: '📝' },
        { name: '图像生成', count: 32, icon: '🎨' },
        { name: '视频生成', count: 18, icon: '🎥' },
        { name: '代码辅助', count: 28, icon: '💻' },
        { name: '音频处理', count: 15, icon: '🎵' },
        { name: '效率工具', count: 67, icon: '⚡' }
      ],
      
      trends: [
        { id: 1, keyword: 'GPT-4.5', change: 42, category: '大语言模型', volume: 125000 },
        { id: 2, keyword: 'AI Agent', change: 38, category: 'AI应用', volume: 89000 },
        { id: 3, keyword: '多模态AI', change: 35, category: 'AI技术', volume: 76000 },
        { id: 4, keyword: '边缘AI', change: -15, category: 'AI硬件', volume: 32000 },
        { id: 5, keyword: 'AI安全', change: 28, category: 'AI治理', volume: 54000 },
        { id: 6, keyword: '深度学习框架', change: 22, category: '开发工具', volume: 48000 }
      ]
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
  
  methods: {
    handleSearch() {
      this.$store.commit('SET_SEARCH_VISIBLE', true)
    },
    // handleSearch() {
    //   if (this.searchKeyword.trim()) {
    //     this.$router.push(`/search?q=${encodeURIComponent(this.searchKeyword)}`)
    //   }
    // },
    
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
    },
    
    getDifficultyType(difficulty) {
      switch (difficulty) {
        case '初级': return 'success'
        case '中级': return 'warning'
        case '高级': return 'danger'
        default: return 'info'
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
  },
  
  // Vue 2 中移除 head() 方法，如果需要SEO支持可以添加 meta 信息
  mounted() {
    // 可以在这里设置页面标题
    document.title = 'AI技术圈 - AI领域的资源库、学习社区和趋势观察站'
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: #333;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.text-gradient {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

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

/* 通用区块样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  color: #1a202c;
}

.section-subtitle {
  font-size: 1.125rem;
  color: #718096;
}

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

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .hero-content,
  .tutorial-content {
    flex-direction: column;
  }
  
  .stats-container {
    justify-content: space-between;
  }
  
  .tool-category-tile {
    padding: 15px;
  }
  
  .suggestion-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
}

/* 修复 el-link 在 router-link 内的样式 */
a {
  text-decoration: none;
  color: inherit;
  
  &:hover {
    text-decoration: none;
  }
}

.router-link-active {
  text-decoration: none;
}
</style>