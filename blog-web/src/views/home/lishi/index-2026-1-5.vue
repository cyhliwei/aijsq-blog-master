<template>
  <div class="home-container">
    <!-- 主要内容区 -->
    <div class="main-content">
      <!-- 轮播图/特色区域 -->
      <section class="feature-section">
        <el-carousel height="400px" :interval="5000" arrow="always">
          <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
            <div class="carousel-item" :style="{ background: item.color }">
              <div class="carousel-content">
                <h2>{{ item.title }}</h2>
                <p>{{ item.description }}</p>
                <el-button type="primary" size="medium">{{ item.buttonText }}</el-button>
              </div>
              <div class="carousel-image">
                <img :src="item.image" :alt="item.title" />
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </section>

      <!-- 快速导航卡片 -->
      <section class="quick-nav-section">
        <h2 class="section-title">快速导航</h2>
        <div class="quick-nav-grid">
          <el-card
            v-for="(nav, index) in quickNavs"
            :key="index"
            shadow="hover"
            class="nav-card"
            @click.native="handleNavClick(nav.path)"
          >
            <div class="nav-card-content">
              <div class="nav-icon" :style="{ backgroundColor: nav.color }">
                <i :class="nav.icon"></i>
              </div>
              <h3>{{ nav.title }}</h3>
              <p>{{ nav.description }}</p>
              <div class="nav-stats">
                <span><i class="el-icon-document"></i> {{ nav.articles }}+</span>
                <span><i class="el-icon-user"></i> {{ nav.users }}+</span>
              </div>
            </div>
          </el-card>
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
      <!-- AI工具推荐区 -->
      <section class="tools-section">
        <div class="section-header">
          <h2 class="section-title">🔥 热门AI工具推荐</h2>
          <el-button type="text" @click="goToPage('tools')">
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

      <!-- 最新教程区 -->
      <section class="tutorials-section">
        <div class="section-header">
          <h2 class="section-title">📚 最新实战教程</h2>
          <el-button type="text" @click="goToPage('tutorials')">
            更多教程 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        
        <div class="tutorials-list">
          <el-card
            v-for="(tutorial, index) in tutorials"
            :key="index"
            class="tutorial-card"
          >
            <div class="tutorial-header">
              <div class="tutorial-level" :class="'level-' + tutorial.level">
                {{ getLevelText(tutorial.level) }}
              </div>
              <div class="tutorial-meta">
                <span><i class="el-icon-time"></i> {{ tutorial.time }}</span>
                <span><i class="el-icon-view"></i> {{ tutorial.views }}</span>
              </div>
            </div>
            
            <div class="tutorial-content">
              <h3>{{ tutorial.title }}</h3>
              <p>{{ tutorial.summary }}</p>
              
              <div class="tutorial-skills">
                <span class="skill-label">涉及技能：</span>
                <el-tag
                  v-for="(skill, skillIndex) in tutorial.skills"
                  :key="skillIndex"
                  size="mini"
                  effect="plain"
                >
                  {{ skill }}
                </el-tag>
              </div>
            </div>
            
            <div class="tutorial-footer">
              <div class="tutorial-author">
                <el-avatar :size="30" :src="tutorial.author.avatar"></el-avatar>
                <span>{{ tutorial.author.name }}</span>
              </div>
              <el-button
                type="primary"
                size="small"
                @click="viewTutorial(tutorial)"
              >
                开始学习
              </el-button>
            </div>
          </el-card>
        </div>
      </section>

      <!-- 技术社区动态 -->
      <section class="community-section">
        <div class="section-header">
          <h2 class="section-title">👥 技术圈动态</h2>
          <el-button type="text" @click="goToPage('community')">
            进入社区 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="16">
            <el-card class="discussion-list">
              <div slot="header" class="clearfix">
                <span>热门讨论</span>
              </div>
              
              <div
                v-for="(discussion, index) in discussions"
                :key="index"
                class="discussion-item"
              >
                <div class="discussion-vote">
                  <el-button type="text" icon="el-icon-caret-top"></el-button>
                  <span class="vote-count">{{ discussion.votes }}</span>
                  <el-button type="text" icon="el-icon-caret-bottom"></el-button>
                </div>
                
                <div class="discussion-content">
                  <h4>{{ discussion.title }}</h4>
                  <div class="discussion-meta">
                    <span><i class="el-icon-user"></i> {{ discussion.author }}</span>
                    <span><i class="el-icon-chat-dot-round"></i> {{ discussion.replies }} 回复</span>
                    <span><i class="el-icon-time"></i> {{ discussion.time }}</span>
                  </div>
                  <div class="discussion-tags">
                    <el-tag
                      v-for="(tag, tagIndex) in discussion.tags"
                      :key="tagIndex"
                      size="mini"
                    >
                      {{ tag }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="8">
            <el-card class="expert-card">
              <div slot="header" class="clearfix">
                <span>专家在线</span>
              </div>
              
              <div
                v-for="(expert, index) in onlineExperts"
                :key="index"
                class="expert-item"
              >
                <el-avatar :size="40" :src="expert.avatar"></el-avatar>
                <div class="expert-info">
                  <h4>{{ expert.name }}</h4>
                  <p>{{ expert.title }}</p>
                </div>
                <el-button
                  size="mini"
                  type="primary"
                  plain
                  @click="consultExpert(expert)"
                >
                  咨询
                </el-button>
              </div>
            </el-card>
            
            <el-card class="stats-card" style="margin-top: 20px;">
              <div class="stats-content">
                <div class="stat-item">
                  <div class="stat-number">{{ stats.totalUsers }}</div>
                  <div class="stat-label">社区成员</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ stats.totalArticles }}</div>
                  <div class="stat-label">技术文章</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ stats.totalTools }}</div>
                  <div class="stat-label">AI工具</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </section>

      <!-- 资源下载区 -->
      <section class="resources-section">
        <h2 class="section-title">📦 热门资源下载</h2>
        <div class="resources-grid">
          <div
            v-for="(resource, index) in resources"
            :key="index"
            class="resource-item"
          >
            <div class="resource-icon">
              <i :class="resource.icon"></i>
            </div>
            <div class="resource-info">
              <h3>{{ resource.name }}</h3>
              <p>{{ resource.description }}</p>
              <div class="resource-meta">
                <span><i class="el-icon-download"></i> {{ resource.downloads }}</span>
                <span><i class="el-icon-timer"></i> {{ resource.size }}</span>
              </div>
            </div>
            <el-button
              type="primary"
              size="small"
              @click="downloadResource(resource)"
            >
              下载
            </el-button>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      activeNav: 'home',
      searchKeyword: '',
      
      carouselItems: [
        {
          title: '探索AI前沿技术',
          description: '每天发现最新AI工具，掌握最实用的AI技能',
          buttonText: '开始探索',
          color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          image: 'https://images.unsplash.com/photo-1677442136019-21780ecad995?auto=format&fit=crop&w=800&q=80'
        },
        {
          title: '加入AI技术社区',
          description: '与上万名AI爱好者交流学习，共同成长',
          buttonText: '加入社区',
          color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          image: 'https://images.unsplash.com/photo-1555949963-aa79dcee981c?auto=format&fit=crop&w=800&q=80'
        },
        {
          title: '掌握AI实战技能',
          description: '从入门到精通，系统化学习AI应用',
          buttonText: '学习教程',
          color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          image: 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?auto=format&fit=crop&w-800&q=80'
        }
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
      quickNavs: [
        {
          title: 'AI工具库',
          description: '收录1000+AI工具，分类评测',
          icon: 'el-icon-magic-stick',
          color: '#409EFF',
          path: '/tools',
          articles: 356,
          users: 12300
        },
        {
          title: '实战教程',
          description: '手把手教学，快速掌握AI技能',
          icon: 'el-icon-reading',
          color: '#67C23A',
          path: '/tutorials',
          articles: 189,
          users: 8900
        },
        {
          title: '技术社区',
          description: '与AI爱好者交流讨论',
          icon: 'el-icon-chat-dot-round',
          color: '#E6A23C',
          path: '/community',
          articles: 567,
          users: 25600
        },
        {
          title: '资源下载',
          description: 'Prompt模板、数据集等资源',
          icon: 'el-icon-download',
          color: '#F56C6C',
          path: '/resources',
          articles: 234,
          users: 7600
        }
      ],
      
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
      
      tutorials: [
        {
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
          title: 'Stable Diffusion本地部署教程',
          summary: '手把手教你本地部署SD，实现自由AI绘画',
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
      
      discussions: [
        {
          title: 'Sora模型何时开放给公众使用？',
          author: '技术探索者',
          time: '2小时前',
          votes: 156,
          replies: 42,
          tags: ['Sora', '视频生成', 'OpenAI']
        },
        {
          title: '国产大模型哪家强？深度对比分析',
          author: 'AI研究员',
          time: '5小时前',
          votes: 89,
          replies: 36,
          tags: ['国产模型', '文心一言', '通义千问', '对比']
        },
        {
          title: '如何用AI生成一致性角色？技术讨论',
          author: '数字艺术家',
          time: '1天前',
          votes: 234,
          replies: 78,
          tags: ['角色一致性', 'AI绘画', '技术实现']
        },
        {
          title: 'AI时代，程序员如何提升竞争力？',
          author: '职业规划师',
          time: '1天前',
          votes: 167,
          replies: 65,
          tags: ['职业发展', 'AI编程', '技能提升']
        }
      ],
      
      onlineExperts: [
        {
          name: '张博士',
          title: '机器学习专家',
          avatar: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=100&q=80',
          specialty: '深度学习、自然语言处理'
        },
        {
          name: '李工程师',
          title: 'AI产品架构师',
          avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=100&q=80',
          specialty: '产品设计、技术架构'
        },
        {
          name: '王设计师',
          title: 'AIGC艺术家',
          avatar: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?auto=format&fit=crop&w=100&q=80',
          specialty: 'AI绘画、视觉设计'
        }
      ],
      tutorialCategories: [
        { name: 'AI绘画', count: 156, icon: '🎨', bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: '大语言模型', count: 89, icon: '🤖', bgColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { name: 'AI视频', count: 42, icon: '🎥', bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { name: 'AI编程', count: 67, icon: '💻', bgColor: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
      ],
      resources: [
        {
          name: 'ChatGPT提示词大全',
          description: '包含1000+优质Prompt模板',
          icon: 'el-icon-document',
          downloads: '12.3k',
          size: '15MB',
          category: '模板'
        },
        {
          name: 'AI绘画模型包',
          description: '精选20个高质量SD模型',
          icon: 'el-icon-picture-outline',
          downloads: '8.7k',
          size: '2.1GB',
          category: '模型'
        },
        {
          name: 'AI学习路线图',
          description: '2024最新AI学习路径指南',
          icon: 'el-icon-guide',
          downloads: '15.6k',
          size: '5MB',
          category: '指南'
        },
        {
          name: '开源AI项目集合',
          description: 'GitHub热门AI项目精选',
          icon: 'el-icon-collection',
          downloads: '6.4k',
          size: '800MB',
          category: '代码'
        }
      ],
      
      stats: {
        totalUsers: '58,942',
        totalArticles: '2,356',
        totalTools: '1,047'
      },
      
      categoryColors: {
        '对话AI': '#409EFF',
        '图像生成': '#67C23A',
        '效率工具': '#E6A23C',
        '编程助手': '#F56C6C'
      },
      
      tagTypes: ['', 'success', 'info', 'warning', 'danger']
    };
  },
  methods: {
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
    handleNavSelect(key) {
      this.activeNav = key;
      // 在实际应用中，这里应该是路由跳转
      console.log('导航到:', key);
    },
    
    handleNavClick(path) {
      // 导航到对应页面
      console.log('跳转到:', path);
    },
    
    viewToolDetail(tool) {
      this.$message({
        message: `查看${tool.name}详情`,
        type: 'info'
      });
      // 实际应用中应该跳转到工具详情页
    },
    
    viewToolTutorial(tool) {
      this.$message({
        message: `查看${tool.name}教程`,
        type: 'info'
      });
    },
    
    viewTutorial(tutorial) {
      this.$message({
        message: `开始学习：${tutorial.title}`,
        type: 'success'
      });
    },
    
    consultExpert(expert) {
      this.$message({
        message: `咨询专家：${expert.name}`,
        type: 'info'
      });
    },
    
    downloadResource(resource) {
      this.$message({
        message: `下载资源：${resource.name}`,
        type: 'success'
      });
    },
    
    goToPage(page) {
      console.log('前往页面:', page);
    },
    
    getLevelText(level) {
      const levels = ['入门', '中级', '高级', '专家'];
      return levels[level - 1] || levels[0];
    }
  }
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f2f5 0%, #ffffff 100%);
}

/* 头部样式 */
.main-header {
  background: linear-gradient(135deg, #1a2a6c 0%, #3a7bd5 100%);
  color: white;
  padding: 0;
  height: auto;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-area {
  display: flex;
  flex-direction: column;
}

.logo {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 28px;
  font-weight: bold;
}

.logo-icon {
  font-size: 36px;
  margin-right: 10px;
}

.logo-text {
  color: white;
}

.logo-domain {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-left: 10px;
  font-weight: normal;
}

.slogan {
  margin: 5px 0 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-input {
  width: 300px;
}

.user-btn {
  color: white !important;
}

/* 主要内容区样式 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 轮播图样式 */
.feature-section {
  margin-bottom: 40px;
  border-radius: 10px;
  overflow: hidden;
}

.carousel-item {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40px;
  color: white;
}

.carousel-content {
  flex: 1;
  max-width: 50%;
}

.carousel-content h2 {
  font-size: 36px;
  margin-bottom: 20px;
}

.carousel-content p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.carousel-image {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.carousel-image img {
  max-width: 80%;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

/* 快速导航样式 */
.quick-nav-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 20px;
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

.quick-nav-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.nav-card {
  cursor: pointer;
  transition: transform 0.3s;
  height: 100%;
}

.nav-card:hover {
  transform: translateY(-5px);
}

.nav-card-content {
  text-align: center;
  padding: 20px;
}

.nav-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
  font-size: 24px;
}

.nav-card-content h3 {
  margin: 0 0 10px;
  font-size: 18px;
}

.nav-card-content p {
  color: #606266;
  margin-bottom: 15px;
  font-size: 14px;
}

.nav-stats {
  display: flex;
  justify-content: center;
  gap: 20px;
  color: #909399;
  font-size: 12px;
}

/* 工具推荐样式 */
.tools-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

/* 社区动态样式 */
.community-section {
  margin-bottom: 40px;
}

.discussion-list {
  margin-bottom: 20px;
}

.discussion-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.discussion-item:last-child {
  border-bottom: none;
}

.discussion-vote {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 50px;
  padding-right: 15px;
}

.vote-count {
  font-weight: bold;
  margin: 5px 0;
}

.discussion-content {
  flex: 1;
}

.discussion-content h4 {
  margin: 0 0 10px;
  font-size: 16px;
}

.discussion-meta {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 12px;
  margin-bottom: 10px;
}

.discussion-tags {
  display: flex;
  gap: 5px;
}

.expert-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.expert-item:last-child {
  border-bottom: none;
}

.expert-info {
  flex: 1;
  margin-left: 15px;
}

.expert-info h4 {
  margin: 0;
  font-size: 16px;
}

.expert-info p {
  margin: 5px 0 0;
  color: #909399;
  font-size: 12px;
}

.stats-content {
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

/* 资源下载样式 */
.resources-section {
  margin-bottom: 40px;
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.resource-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.resource-icon {
  width: 50px;
  height: 50px;
  background: #409EFF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  margin-right: 15px;
}

.resource-info {
  flex: 1;
}

.resource-info h3 {
  margin: 0 0 5px;
  font-size: 16px;
}

.resource-info p {
  color: #606266;
  font-size: 14px;
  margin: 0 0 10px;
}

.resource-meta {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 12px;
}

/* 底部样式 */
.main-footer {
  background: #2c3e50;
  color: white;
  padding: 40px 0;
  margin-top: 40px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 2fr 3fr 1fr;
  gap: 40px;
}

.footer-logo h2 {
  font-size: 24px;
  margin: 0 0 10px;
}

.footer-slogan {
  color: rgba(255, 255, 255, 0.8);
  margin-top: 10px;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.link-column h3 {
  font-size: 16px;
  margin: 0 0 15px;
}

.link-column ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.link-column li {
  margin-bottom: 10px;
}

.link-column a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: color 0.3s;
}

.link-column a:hover {
  color: #409EFF;
}

.footer-social h3 {
  font-size: 16px;
  margin: 0 0 15px;
}

.social-icons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.copyright {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  margin-top: 20px;
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

/* 响应式设计 */
@media (max-width: 992px) {
  .header-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .nav-menu {
    width: 100%;
  }
  
  .header-right {
    width: 100%;
    justify-content: center;
  }
  
  .search-input {
    width: 100%;
  }
  
  .carousel-item {
    flex-direction: column;
    text-align: center;
  }
  
  .carousel-content {
    max-width: 100%;
    margin-bottom: 20px;
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .tools-grid,
  .tutorials-list {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .footer-links {
    grid-template-columns: 1fr;
  }
  
  .quick-nav-grid {
    grid-template-columns: 1fr;
  }
  
  .resources-grid {
    grid-template-columns: 1fr;
  }
}
</style>