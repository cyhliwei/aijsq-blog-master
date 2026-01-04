<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <!-- <el-header class="main-header">
      <div class="header-content">
        <div class="logo-area">
          <h1 class="logo">
            <span class="logo-icon">🤖</span>
            <span class="logo-text">AI技术圈</span>
            <span class="logo-domain">aijsq.com</span>
          </h1>
          <p class="slogan">连接AI技术，构建智能未来</p>
        </div>
        
        <div class="nav-menu">
          <el-menu
            :default-active="activeNav"
            mode="horizontal"
            background-color="transparent"
            text-color="#fff"
            active-text-color="#409EFF"
            @select="handleNavSelect"
          >
            <el-menu-item index="home">首页</el-menu-item>
            <el-menu-item index="tools">AI快讯</el-menu-item>
            <el-menu-item index="tools">AI学院</el-menu-item>
            <el-menu-item index="tools">AI工具</el-menu-item>
            <el-menu-item index="tutorials">实战教程</el-menu-item>
            <el-menu-item index="community">技术社区</el-menu-item>
            <el-menu-item index="news">前沿动态</el-menu-item>
            <el-menu-item index="resources">资源库</el-menu-item>
            <el-menu-item index="resources">技术圈</el-menu-item>
          </el-menu>
        </div>
        
        <div class="header-right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索AI工具、教程、资源..."
            size="small"
            class="search-input"
          >
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
          <el-button type="text" icon="el-icon-user" class="user-btn">登录/注册</el-button>
        </div>
      </div>
    </el-header> -->

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

      <!-- 底部区域 -->
      <footer class="main-footer">
        <div class="footer-content">
          <div class="footer-logo">
            <h2>AI技术圈</h2>
            <p>aijsq.com</p>
            <p class="footer-slogan">探索AI无限可能，共创智能未来</p>
          </div>
          
          <div class="footer-links">
            <div class="link-column">
              <h3>内容板块</h3>
              <ul>
                <li><a href="#">AI工具评测</a></li>
                <li><a href="#">实战教程</a></li>
                <li><a href="#">技术专栏</a></li>
                <li><a href="#">开源项目</a></li>
              </ul>
            </div>
            
            <div class="link-column">
              <h3>社区服务</h3>
              <ul>
                <li><a href="#">问答互助</a></li>
                <li><a href="#">作品展示</a></li>
                <li><a href="#">招聘信息</a></li>
                <li><a href="#">合作伙伴</a></li>
              </ul>
            </div>
            
            <div class="link-column">
              <h3>关于我们</h3>
              <ul>
                <li><a href="#">团队介绍</a></li>
                <li><a href="#">加入我们</a></li>
                <li><a href="#">联系我们</a></li>
                <li><a href="#">用户协议</a></li>
              </ul>
            </div>
          </div>
          
          <div class="footer-social">
            <h3>关注我们</h3>
            <div class="social-icons">
              <el-button circle><i class="el-icon-weibo"></i></el-button>
              <el-button circle><i class="el-icon-wechat"></i></el-button>
              <el-button circle><i class="el-icon-zhihu"></i></el-button>
              <el-button circle><i class="el-icon-github"></i></el-button>
            </div>
            <p class="copyright">© 2024 aijsq.com AI技术圈 版权所有</p>
          </div>
        </div>
      </footer>
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

/* 教程列表样式 */
.tutorials-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
}

.tutorial-card {
  height: 100%;
}

.tutorial-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.tutorial-level {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
}

.level-1 { background: #67C23A; }
.level-2 { background: #E6A23C; }
.level-3 { background: #F56C6C; }

.tutorial-meta {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 12px;
}

.tutorial-content h3 {
  margin: 0 0 10px;
  font-size: 16px;
}

.tutorial-skills {
  margin-top: 15px;
}

.skill-label {
  font-size: 12px;
  color: #909399;
  margin-right: 10px;
}

.tutorial-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.tutorial-author {
  display: flex;
  align-items: center;
  gap: 10px;
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