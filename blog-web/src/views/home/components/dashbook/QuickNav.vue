<template>
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
          <h3>{{ nav.name }}</h3>
          <p>{{ nav.description }}</p>
          <div class="nav-stats">
            <span><i class="el-icon-document"></i> {{ nav.articles }}+</span>
            <span><i class="el-icon-user"></i> {{ nav.users }}+</span>
          </div>
        </div>
      </el-card>
    </div>
  </section>
</template>

<script>
import { getQuickNavsApi } from '@/api/home/api-home'

export default {
  name: 'QuickNav',
  data() {
    return {
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
    }
  },
  created() {
    this.loadQuickNavData()
  },
  methods: {
    async loadQuickNavData() {
      try {
        const res = await getQuickNavsApi()
        console.log(res.data)
        if (res.code === 200) {
          this.quickNavs = res.data
          
        }
      } catch (error) {
        console.error('加载快速导航数据失败:', error)
      }
    },
    handleNavClick(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
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
</style>