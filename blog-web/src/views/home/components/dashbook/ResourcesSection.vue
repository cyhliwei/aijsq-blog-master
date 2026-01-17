<template>
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
          去查看
        </el-button>
      </div>
    </div>
  </section>
</template>

<script>
import { getPopularResourcesApi, downloadResourceApi } from '@/api/home/api-resource'

export default {
  name: 'ResourcesSection',
  data() {
    return {
      resources: [
        {
          name: 'ChatGPT提示词大全',
          description: '包含1000+优质Prompt模板',
          icon: 'el-icon-document',
          downloads: '12.3k',
          size: '15MB',
          category: '模板',
          path: '/waytoagi'
        },
        {
          name: 'AI绘画模型包',
          description: '精选20个高质量SD模型',
          icon: 'el-icon-picture-outline',
          downloads: '8.7k',
          size: '2.1GB',
          category: '模型',
          path: '/waytoagi'
        },
        {
          name: 'AI学习路线图',
          description: '2024最新AI学习路径指南',
          icon: 'el-icon-guide',
          downloads: '15.6k',
          size: '5MB',
          category: '指南',
          path: '/waytoagi'
        },
        {
          name: '开源AI项目集合',
          description: 'GitHub热门AI项目精选',
          icon: 'el-icon-collection',
          downloads: '6.4k',
          size: '800MB',
          category: '代码',
          path: '/waytoagi'
        }
      ],
      
    }
  },
  created() {
    //this.loadResourcesData()
  },
  methods: {
    async loadResourcesData() {
      try {
        const res = await getPopularResourcesApi()
        if (res.code === 200) {
          this.resources = res.data
        }
      } catch (error) {
        console.error('加载资源数据失败:', error)
      }
    },
    downloadResource(){
      this.$router.push('/resources')
    }
    // async downloadResource(resource) {
    //   try {
    //     const res = await downloadResourceApi(resource.id)
    //     if (res.code === 200) {
    //       window.location.href = res.data.downloadUrl
    //     }
    //   } catch (error) {
    //     console.error('下载资源失败:', error)
    //   }
    // }
  }
}
</script>

<style scoped>
.resources-section {
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
</style>