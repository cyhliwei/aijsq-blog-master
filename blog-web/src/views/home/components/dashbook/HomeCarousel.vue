<template>
  <section class="feature-section">
    <el-carousel height="400px" :interval="5000" arrow="always">
      <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
        <div class="carousel-item" :style="{ background: item.color }">
          <div class="carousel-content">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button type="primary" size="medium" @click="$router.push(item.path)">{{ item.buttonText }}</el-button>
          </div>
          <div class="carousel-image">
            <img :src="item.image" :alt="item.title" />
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </section>
</template>

<script>
import { getCarouselsApi } from '@/api/home/api-home'
import router from '@/router';

export default {
  name: 'HomeCarousel',
  data() {
    return {
      carouselItems: [
        {
          title: '通往AGI之路',
          description: 'AGI点亮学习捷径，人人因AI强大',
          buttonText: '开始探索',
          color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          image: 'https://images.unsplash.com/photo-1677442136019-21780ecad995?auto=format&fit=crop&w=800&q=80',
          path: '/waytoagi/jNnFGW2JvwlbOzaRrwwqsCvS51YWuALUD62e7IuSxMBYLv'
        },
        {
          title: '加入AI技术社区',
          description: '与上万名AI爱好者交流学习，共同成长',
          buttonText: '加入社区',
          color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          image: 'https://images.unsplash.com/photo-1555949963-aa79dcee981c?auto=format&fit=crop&w=800&q=80',
          path: '/waytoagi/1'
        },
        {
          title: '掌握AI实战技能',
          description: '从入门到精通，系统化学习AI应用',
          buttonText: '学习教程',
          color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          image: 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?auto=format&fit=crop&w-800&q=80',
          path: '/waytoagi/2'
        }
      ]
    }
  },
  created() {
    //this.loadCarouselData()
  },
  methods: {
    async loadCarouselData() {
      try {
        const res = await getCarouselsApi()
        if (res.code === 200) {
          this.carouselItems = res.data
        }
      } catch (error) {
        console.error('加载轮播图数据失败:', error)
      }
    }
  }
}
</script>

<style scoped>
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
</style>