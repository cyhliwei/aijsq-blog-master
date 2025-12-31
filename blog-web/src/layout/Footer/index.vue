<template>
  <!-- <footer class="site-footer">
    <div class="footer-content">
      <div class="footer-info">
        <div class="running-time">
          <span class="icon">⏱</span>
          本站居然运行了 <span class="time-value">{{ days }}</span> 天
          <span class="time-value">{{ hours }}</span> 时
          <span class="time-value">{{ minutes }}</span> 分
          <span class="time-value">{{ seconds }}</span> 秒
        </div>
        <div class="copyright">
          Copyright©{{ startYear }}-{{ currentYear }} {{ $store.state.webSiteInfo.name }}
          <span class="divider">·</span>
          <a href="https://beian.miit.gov.cn/" 
             target="_blank" 
             rel="noopener"
             class="record">
             {{$store.state.webSiteInfo.recordNum}}
          </a>
        </div>
      </div>
    </div>
  </footer> -->
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
</template>

<script>
export default {
  name: 'TheFooter',
  data() {
    return {
      startYear: 2021,
      days: 0,
      hours: 0,
      minutes: 0,
      seconds: 0,
      timer: null,
      startDate: new Date('2021-08-31')
    }
  },
  computed: {
    currentYear() {
      return new Date().getFullYear()
    }
  },
  methods: {
    calculateRunningTime() {
      const now = new Date()
      const diff = now - this.startDate
      
      this.days = Math.floor(diff / (1000 * 60 * 60 * 24))
      this.hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
      this.minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
      this.seconds = Math.floor((diff % (1000 * 60)) / 1000)
    }
  },
  mounted() {
    this.calculateRunningTime()
    this.timer = setInterval(this.calculateRunningTime, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  }
}
</script>

<style lang="scss" scoped>
.site-footer {
  background: var(--card-bg);
  padding: $spacing-lg 0;
  margin-top: auto;
  border-top: 1px solid rgba(125, 125, 125, 0.1);
}

.footer-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 $spacing-xl;
}

.footer-info {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.running-time {
  font-size: 0.875rem;
  color: var(--text-secondary);
  
  .time-value {
    color: #ff6b81;
    font-family: 'Fira Code', monospace;
  }
  
  .icon {
    color: #4CAF50;
    margin-right: 4px;
  }
}

.copyright {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-sm;
  flex-wrap: wrap;
  font-size: 0.875rem;
  color: var(--text-secondary);



  .record {
    color: inherit;
    text-decoration: none;
    transition: color 0.2s ease;
    
    &:hover {
      color: $primary;
    }
  }
}

@include responsive(sm) {
  .site-footer {
    padding: $spacing-md 0;
  }

  .footer-content {
    padding: 0 $spacing-md;
  }

  .copyright {
    font-size: 0.8125rem;
    gap: $spacing-xs;
  }
  
  .running-time {
    font-size: 0.8125rem;
  }
}


/* 底部样式 */
.main-footer {
  background: #707088;
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
