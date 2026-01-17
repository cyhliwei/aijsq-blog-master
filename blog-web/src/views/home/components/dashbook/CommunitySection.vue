<template>
  <section class="community-section">
    <div class="section-header">
      <h2 class="section-title">👥 技术圈动态</h2>
      <!-- <el-button type="text" @click="$router.push('/community')"> -->
      <el-button type="text">
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
</template>

<script>
import { getHotDiscussionsApi, getOnlineExpertsApi, consultExpertApi } from '@/api/home/api-community'
import { getHomeStatsApi } from '@/api/home/api-home'

export default {
  name: 'CommunitySection',
  data() {
    return {
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
      stats: {
        totalUsers: '58,942',
        totalArticles: '2,356',
        totalTools: '1,047'
      }
    }
  },
  created() {
    //this.loadCommunityData()
  },
  methods: {
    async loadCommunityData() {
      try {
        const [discussionsRes, expertsRes, statsRes] = await Promise.all([
          getHotDiscussionsApi(),
          getOnlineExpertsApi(),
          getHomeStatsApi()
        ])
        
        if (discussionsRes.code === 200) {
          this.discussions = discussionsRes.data
        }
        if (expertsRes.code === 200) {
          this.onlineExperts = expertsRes.data
        }
        if (statsRes.code === 200) {
          this.stats = statsRes.data
        }
      } catch (error) {
        console.error('加载社区数据失败:', error)
      }
    },
    async consultExpert(expert) {
      try {
        const res = await consultExpertApi({
          expertId: expert.id,
          userId: this.$store.state.user.id
        })
        if (res.code === 200) {
          this.$message.success('咨询请求已发送')
        }
      } catch (error) {
        console.error('咨询专家失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.community-section {
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
</style>