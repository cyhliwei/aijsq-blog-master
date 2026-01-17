<template>
  <div class="article-page" v-loading="loading">

    <!-- 添加固定操作栏 -->
    <!-- 左侧导航栏 -->
    <el-aside :width="isCollapsed ? '64px' : '245px'" class="sidebar">
      <div class="sidebar-header">
        <span v-if="!isCollapsed">知识库目录</span>
        <el-tooltip v-else content="知识库目录" placement="right">
          <i class="el-icon-notebook-2"></i>
        </el-tooltip>
        <div class="sidebar-controls">
          <el-tooltip :content="isCollapsed ? '展开侧边栏' : '折叠侧边栏'" placement="right">
            <i 
              class="el-icon-s-unfold collapse-icon" 
              @click="toggleSidebar"
            ></i>
          </el-tooltip>
          <el-tooltip v-if="!isCollapsed" content="添加新菜单" placement="right">
            <i 
              class="el-icon-circle-plus add-icon" 
              @click="handleAddMenu"
            ></i>
          </el-tooltip>
        </div>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        :collapse-transition="false"
        class="el-menu-vertical"
        @select="handleMenuSelect"
        background-color="#f5f5f5"
        text-color="#333"
        active-text-color="#1890ff"
        style="height: calc(100vh - 120px); overflow-y: auto;"
      >
        <!-- 通往AGI之路 - 特殊项 -->
        <div 
          :class="['menu-item-highlight', { 'collapsed': isCollapsed }]"
          @click="handleSpecialMenu('agi-road')">
          <i class="menu-icon">🌈</i>
          <span v-if="!isCollapsed">通往AGI之路</span>
          <el-tooltip v-else content="通往AGI之路" placement="right">
            <span>🌈</span>
          </el-tooltip>
        </div>

        <!-- 动态渲染菜单 -->
        <template v-for="item in menuItems">
          <el-submenu 
            v-if="item.children && item.children.length > 0"
            :key="item.id"
            :index="String(item.id)"
            :title="item.title">
            <template slot="title">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </template>
            
            <!-- 渲染子菜单 -->
            <template v-for="child in item.children">
              <el-submenu 
                v-if="child.children && child.children.length > 0"
                :key="child.id"
                :index="String(child.id)">
                <template slot="title">
                  <i :class="child.icon || 'el-icon-document'"></i>
                  <span>{{ child.title }}</span>
                </template>
                <el-menu-item 
                  v-for="subChild in child.children"
                  :key="subChild.id"
                  :index="String(subChild.id)"
                  @click="handleSubMenuClick(subChild)">
                  <i :class="subChild.icon || 'el-icon-tickets'"></i>
                  <span>{{ subChild.title }}</span>
                  <div class="menu-actions" v-if="!isCollapsed">
                    <el-tooltip content="编辑" placement="top">
                      <i 
                        class="el-icon-edit edit-icon"
                        @click.stop="handleEditMenu(subChild)"
                      ></i>
                    </el-tooltip>
                  </div>
                </el-menu-item>
              </el-submenu>
              
              <el-menu-item 
                v-else
                :key="child.id"
                :index="String(child.id)"
                @click="handleSubMenuClick(child)">
                <i :class="child.icon || 'el-icon-tickets'"></i>
                <span>{{ child.title }}</span>
                <div class="menu-actions" v-if="!isCollapsed">
                  <el-tooltip content="编辑" placement="top">
                    <i 
                      class="el-icon-edit edit-icon"
                      @click.stop="handleEditMenu(child)"
                    ></i>
                  </el-tooltip>
                </div>
              </el-menu-item>
            </template>
          </el-submenu>
          
          <el-menu-item 
            v-else
            :key="item.id"
            :index="String(item.id)"
            @click="handleMenuClick(item)">
            <i :class="item.icon"></i>
            <span>{{ item.title }}</span>
            <div class="menu-actions" v-if="!isCollapsed">
              <el-tooltip content="编辑" placement="top">
                <i 
                  class="el-icon-edit edit-icon"
                  @click.stop="handleEditMenu(item)"
                ></i>
              </el-tooltip>
            </div>
          </el-menu-item>
        </template>
      </el-menu>

      <!-- 添加菜单按钮（底部） -->
      <div 
        v-if="!isCollapsed" 
        class="add-menu-bottom"
        @click="handleAddMenu">
        <i class="el-icon-circle-plus-outline"></i>
        <span>添加新菜单</span>
      </div>
    </el-aside>

    <div class="content-layout" id="articleBox" :class="{ center: !showSidebar }">
      
      <main class="article-main">
        <!-- 文章头部 -->
        <header class="article-header">
          <div class="article-title">{{ article.title }}</div>

          <!-- 作者信息和元数据 -->
          <div class="article-info">
            <div class="author-info">
              <img v-lazy="article.avatar" alt="作者头像" class="author-avatar">
              <div class="author-meta">
                <span class="author-name">{{ article.nickname }}</span>
                <div class="post-meta">
                  <time class="publish-time">
                    <i class="far fa-calendar-alt"></i>
                    {{ article.createTime }}
                  </time>
                  <span class="meta-divider">·</span>
                  <span class="category">
                    <i class="fas fa-folder"></i>
                    {{ article.category.name }}
                  </span>
                </div>
              </div>
            </div>

            <div class="article-stats">
              <div class="stat-item">
                <i class="far fa-eye"></i>
                <span>{{ article.quantity }} 阅读</span>
              </div>
              <div class="stat-item">
                <i class="far fa-clock"></i>
                <span>{{ readTime }} 分钟</span>
              </div>
              <div class="stat-item">
                <i class="far fa-comment"></i>
                <span>{{ article.commentNum || 0 }} 评论</span>
              </div>
            </div>
          </div>
        </header>

        <!-- AI简短介绍 -->
        <div v-if="article.aiDescribe" class="ai-description">
          <div class="ai-header" @click="isAiDescriptionExpanded = !isAiDescriptionExpanded">
            <i class="fas fa-robot"></i>
            <span>AI 摘要</span>
            <i class="fas" :class="isAiDescriptionExpanded ? 'fa-chevron-up' : 'fa-chevron-down'" style="margin-left:auto;"></i>
          </div>
          <transition
                name="expand"
                @enter="startTransition"
                @leave="endTransition"
                mode="out-in"
          >
            <div class="ai-content" v-show="isAiDescriptionExpanded">
              <span class="typing-text" ref="typingText"></span>
              <div class="ai-description-text">
                摘要由平台通过智能技术生成
              </div>
            </div>
          </transition>
        </div>

        <!-- 文章内容 -->
        <article class="article-content">
          <!-- 免费内容 -->
          <div v-if="article.readType === 1" v-html="article.content"></div>
          
          <!-- 会员内容 -->
          <div v-else-if="article.readType === 2" class="locked-content member">
            <div class="preview-content" v-html="getPreviewContent(article.content)"></div>
            <div class="content-locker">
              <div class="locker-icon">
                <i class="fas fa-crown"></i>
              </div>
              <h3>会员专享内容</h3>
              <p>成为会员即可阅读全文</p>
              <el-button type="primary" @click="handleUpgrade">立即开通会员</el-button>
            </div>
          </div>
          
          <!-- 付费内容 -->
          <div v-else-if="article.readType === 3" class="locked-content paid">
            <div class="preview-content" v-html="getPreviewContent(article.content)"></div>
            <div class="content-locker">
              <div class="locker-icon">
                <i class="fas fa-lock"></i>
              </div>
              <h3>付费阅读</h3>
              <p>支付 1 元即可阅读全文</p>
              <el-button type="primary" @click="handlePurchase">立即购买</el-button>
            </div>
          </div>
        </article>

        <!-- 文章底部 -->
        <footer class="article-footer">
          <!-- 版权声明提示 -->
          <div class="copyright-notice">
            <div class="notice-header">
              <i class="fas fa-copyright"></i>
              <span>版权声明</span>
            </div>
            <div class="notice-content">
              <div v-if="article.isOriginal" class="notice-item">
                <i class="fas fa-check-circle"></i>
                <span>本文由 {{ article.nickname }} 原创发布</span>
              </div>
              <div v-else class="notice-item">
                <i class="fas fa-share-alt"></i>
                <span>本文转载自：<a :href="article.originalUrl" target="_blank" rel="noopener noreferrer">{{ article.originalUrl || '未知来源' }}</a></span>
              </div>
              <div class="notice-item">
                <i class="fas fa-calendar-alt"></i>
                <span>发布时间：{{ article.createTime }}</span>
              </div>
              <div class="notice-item">
                <i class="fab fa-creative-commons-sa"></i>
                <span>
                  版权协议：
                  <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" target="_blank" rel="noopener noreferrer">
                    CC BY-NC-SA 4.0
                  </a>
                </span>
              </div>
              <div class="notice-item notice-warning">
                <i class="fas fa-exclamation-triangle"></i>
                <span>未经许可，禁止转载、摘编、复制或建立镜像。欢迎转发分享！</span>
              </div>
            </div>
          </div>

          <!-- 标签部分保持不变 -->
          <div class="tags-section">
            <i class="fas fa-tags"></i>
            <div class="tags-list">
              <router-link v-for="tag in article.tags" :key="tag.id" :to="`/tags?tagId=${tag.id}&tagName=${tag.name}`"
                class="tag-item">
                {{ tag.name }}
              </router-link>
            </div>
          </div>

          <!-- 修改操作按钮部分 -->
          <div class="article-actions">
            <button class="action-btn like" :class="{ active: article.isLike }" @click="toggleLike">
              <i class="fas fa-heart"></i>
              <span>{{ article.likeNum }}</span>
            </button>
            <div class="share-dropdown" v-click-outside="closeShareMenu">
              <button class="action-btn share" @click="toggleShareMenu">
                <i class="fas fa-share-alt"></i>
                分享
              </button>
              <div class="share-menu" v-show="showShareMenu">
                <button class="share-item" @click="shareToQQ">
                  <i class="fab fa-qq"></i>
                  QQ好友
                </button>
                <button class="share-item" @click="shareToQzone">
                  <i class="fas fa-star"></i>
                  QQ空间
                </button>
                <button class="share-item" @click="shareToWeibo">
                  <i class="fab fa-weibo"></i>
                  微博
                </button>
                <button class="share-item" @click="shareToWechat">
                  <i class="fab fa-weixin"></i>
                  微信
                </button>
                <button class="share-item" @click="copyLink">
                  <i class="fas fa-link"></i>
                  复制链接
                </button>
              </div>
            </div>
          </div>

          <!-- 导航部分保持不变 -->
          <!-- <nav class="article-nav">
            <div 
              v-if="prevArticle" 
              class="nav-item prev"
              @click="goToArticle(prevArticle.id)"
            >
              <i class="fas fa-arrow-left"></i>
              <div class="nav-content">
                <span class="label">上一篇</span>
                <span class="title">{{ prevArticle.title }}</span>
              </div>
            </div>
            <div 
              v-if="nextArticle" 
              class="nav-item next"
              @click="goToArticle(nextArticle.id)"
            >
              <div class="nav-content">
                <span class="label">下一篇</span>
                <span class="title">{{ nextArticle.title }}</span>
              </div>
              <i class="fas fa-arrow-right"></i>
            </div>
          </nav> -->
        </footer>

        <!-- 添加评论组件 -->
        <Comment 
          :article-id="article.id" 
          :comment-count="article.commentNum || 0"
          :article-author-id="article.userId || ''" 
          @comment-added="handleCommentAdded"
          @comment-deleted="handleCommentDeleted"
        />
      </main>

      <!-- 侧边栏 -->
      <aside v-if="showSidebar" class="article-sidebar desktop-only">
        <div class="toc-container">
          <div class="toc-header">
            <div class="title-wrapper">
            <i class="fas fa-list"></i>
            <span>目录</span>
            </div>
            <div class="progress-wrapper" :class="{ completed: readProgress === 100 }">
              <i class="fas fa-book-reader"></i>
              <span class="progress-text">{{ readProgress }}</span>
            </div>
          </div>
          <div class="toc-content">
            <div v-for="(item, index) in tocItems" :key="index" class="toc-item" :class="{
              'active': activeHeading === item.id,
              [`level-${item.level}`]: true
            }" @click="scrollToHeading(item.id)">
              {{ item.text }}
            </div>
          </div>
        </div>
      </aside>
    </div>
    <mj-image-preview ref="imagePreview" />
    <!-- <payment-dialog
      :visible.sync="showPaymentDialog"
      :title="article.title"
      :price="1"
      :article-id="$route.params.id"
      @payment-success="handlePaymentSuccess"
    /> -->
    <membership-dialog
      :visible.sync="showMembershipDialog"
      @payment-success="handleMembershipSuccess"
    />
  </div>
</template>

<script>
import { getArticleDetailApi, likeArticleApi } from '@/api/article'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'
import Comment from '@/components/Comment/index.vue'
// import PaymentDialog from '@/components/PaymentDialog/index.vue'
import MembershipDialog from '@/components/MembershipDialog/index.vue'
import { marked } from 'marked'
import { getMenuListApi, getDetailByMenuIdApi } from '@/api/aijsqContent/aijsqMenu'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Article',
  components: {
    Comment,
    //PaymentDialog,
    MembershipDialog
  },
  data() {
    return {
      article: {
        title: '',
        category: {},
        isOriginal: true,
        readType: 1,
        price: 0,
      },
      prevArticle: {
        id: 1,
        title: '默认文章',
      },
      nextArticle: {
        id: 1,
        title: '默认文章',
      },
      tocItems: [],
      readProgress: 0,
      showShareMenu: false,
      activeHeading: '',
      readTime: 0,
      likeDebounce: false,
      loading: false,
      actionBarLeft: '0px',
      showSidebar: true,
      contentLayout: 'center',
      collapsedCodeBlocks: new Set(),
      images: [],
      showPaymentDialog: false,
      showMembershipDialog: false,
      isAiDescriptionExpanded: true,
      isCollapsed: false,
      activeMenu: '',
      menuItems:[],
      treeSelectOptions:[],
    }
  },
  computed: {
    currentUrl() {
      return window.location.href
    }
  },
  methods: {
    // 处理子菜单点击
    handleSubMenuClick(menuItem) {
      this.getArticle(menuItem.id)
    },
    
    // 处理菜单项点击
    handleMenuClick(menuItem) {
       this.getArticle(menuItem.id)
    },
    async getAijsqMenuList(){
      try {
        const { data } = await getMenuListApi()
        this.menuItems = data
        this.treeSelectOptions = this.transformToTreeSelectOptions(this.menuItems)
      } catch (error) {
      }
    },
    // 切换侧边栏折叠状态
    toggleSidebar() {
      this.isCollapsed = !this.isCollapsed
    },
    
    // 处理菜单选择
    handleMenuSelect(index, indexPath) {
      this.activeMenu = index
     
    },
    // 添加菜单
    handleAddMenu() {
      this.dialogTitle = '添加菜单'
      this.editingMenuId = null
      this.menuForm = {
        markdown: '# 新菜单\n\n请输入内容...'
      }
      this.menuDialogVisible = true
    },
    /**
     * 获取文章详情
     */
    async getArticle(menuId) {
      this.loading = true
      hljs.configure({
        ignoreUnescapedHTML: true
      })
      try {
        const res = await getDetailByMenuIdApi(menuId)
        this.article = {
          ...res.data,
          content: res.data.content ? this.addLazyLoadToImages(res.data.content) : ''
        }

        // 等待下一个 tick，确保文章内容渲染完成
        await this.$nextTick()
        
        // 使用 setTimeout 确保 DOM 完全渲染
        setTimeout(() => {
          this.generateToc()
          document.querySelectorAll('pre code').forEach((block) => {
            hljs.highlightBlock(block)
          })
          this.addCopyButtons()
          this.addLineNumbers()
          this.initImagePreview()
          this.updateActionBarPosition()
          
          // 添加一个额外的延时来处理代码块的展开/折叠
          this.initializeCodeBlocks()
          
          // AI摘要
          if (this.article.aiDescribe) {
            const typingText = this.$refs.typingText
            if (!typingText) return
            // 使用marked解析Markdown文本
            const htmlContent = marked(this.article.aiDescribe || '')
            typingText.innerHTML = htmlContent
          }
        }, 100)

        // 计算阅读时间
        const textContent = this.article.content.replace(/<[^>]+>/g, ' ')
        this.readTime = Math.ceil(textContent.split(/\s+/).length / 300)

      } catch (error) {
        this.$message.error('获取文章详情失败')
      } finally {
        this.loading = false
      }
    },
    /**
     * 为文章内容中的图片添加懒加载
     */
    addLazyLoadToImages(content) {
      // 使用data-src来存储实际图片地址，并添加lazy-image类用于识别
      return content.replace(
        /<img([^>]*)src="([^"]*)"([^>]*)>/gi,
        '<img$1src="' + this.getLoadingImage() + '" data-src="$2" class="lazy-image"$3>'
      )
    },
    /**
     * 获取加载中的图片
     */
    getLoadingImage() {
      return 'https://img.shiyit.com/base/mojian/lazy.gif'
    },
    /**
     * 生成目录
     */
    generateToc() {
      const headings = document.querySelectorAll('.article-content h1,.article-content h2,.article-content h3,.article-content h4,.article-content h5,.article-content h6')
      this.tocItems = Array.from(headings).map(heading => {
        const id = heading.textContent.trim().toLowerCase().replace(/\s+/g, '-')
        heading.id = id
        return {
          id,
          text: heading.textContent,
          level: parseInt(heading.tagName.charAt(1))
        }
      })
    },
    /**
     * 点赞
     */
    toggleLike() {
      //防止频繁点击 设置一个5秒的防抖
      if (this.likeDebounce) {
        this.$message.warning('请于 5 秒后再试')
        return
      }
      // 实现点赞功能
      likeArticleApi(this.$route.params.id).then(res => {
        if (this.article.isLike) {
          this.article.likeNum--
        } else {
          this.article.likeNum++
        }
        this.$message.success(this.article.isLike ? '取消点赞成功' : '点赞成功')
        this.article.isLike = !this.article.isLike
        // 设置一个5秒的防抖
        this.likeDebounce = true
        setTimeout(() => {
          this.likeDebounce = false
        }, 5000)
      })
    },
    /**
     * 分享
     */
    toggleShareMenu() {
      this.showShareMenu = !this.showShareMenu
    },
    /**
     * 关闭分享
     */
    closeShareMenu() {
      this.showShareMenu = false
    },
    /**
     * 分享到QQ
     */
    shareToQQ() {
      const url = encodeURIComponent(this.currentUrl)
      const title = encodeURIComponent(this.article.title)
      const summary = encodeURIComponent(this.article.summary || '')
      const pic = encodeURIComponent(this.article.avatar || '')
      window.open(
        `https://connect.qq.com/widget/shareqq/index.html?url=${url}&title=${title}&summary=${summary}&pics=${pic}`,
        "renren-share", "width=490,height=700");
      this.closeShareMenu()
    },
    /**
     * 分享到QQ空间
     */
    shareToQzone() {
      const url = encodeURIComponent(this.currentUrl)
      const title = encodeURIComponent(this.article.title)
      const summary = encodeURIComponent(this.article.summary || '')
      const pic = encodeURIComponent(this.article.avatar || '')
      window.open(
        `https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=${url}&title=${title}&summary=${summary}&pics=${pic}`,
        "renren-share", "width=490,height=700"
      )

      this.closeShareMenu()
    },
    /**
     * 分享到微博
     */
    shareToWeibo() {
      const url = encodeURIComponent(this.currentUrl)
      const title = encodeURIComponent(`${this.article.title} - 拾壹博客`)
      window.open(
        `http://service.weibo.com/share/share.php?url=${url}&title=${title}`,
        "renren-share", "width=490,height=700")
      this.closeShareMenu()
    },
    /**
     * 分享到微信
     */
    shareToWechat() {
      // 由于微信分享需要使用微信SDK，这样简单处理
      window.open(
        `https://api.pwmqr.com/qrcode/create/?url=${window.location.href}`,
        "renren-share", "width=490,height=700");
      this.closeShareMenu()
    },
    /**
     * 复制链接
     */
    async copyLink() {
      try {
        await navigator.clipboard.writeText(this.currentUrl)
        this.$message.success('链接已复制到剪贴板')
      } catch (err) {
        this.$message.error('复制失败，请手动复制')
      }
      this.closeShareMenu()
    },
    /**
     * 跳转到文章
     */
    goToArticle(id) {
      this.$router.push(`/article/${id}`)
    },
    /**
     * 更新阅读进度
     */
    handleScroll() {
      const docEl = document.documentElement
      const winHeight = window.innerHeight
      const docHeight = docEl.scrollHeight - winHeight
      const scrollTop = window.scrollY || docEl.scrollTop
      this.readProgress = Math.round((scrollTop / docHeight) * 100)
    },
    /**
     * 滚动到标题
     */
    scrollToHeading(id) {
      const element = document.getElementById(id)
      if (element) {
        const header = document.querySelector('.site-header')
        const headerHeight = header ? header.offsetHeight : 0
        const targetPosition = element.offsetTop - headerHeight - 20

        window.scrollTo({
          top: targetPosition,
          behavior: 'smooth'
        })
      }
    },
    /**
     * 更新激活标题
     */
    updateActiveHeading() {
      this.handleScroll()
      const headings = this.tocItems.map(item => document.getElementById(item.id))
      const header = document.querySelector('.site-header')
      const headerHeight = header ? header.offsetHeight : 0

      for (let i = headings.length - 1; i >= 0; i--) {
        const heading = headings[i]
        if (heading && heading.getBoundingClientRect().top <= headerHeight + 100) {
          this.activeHeading = heading.id
          break
        }
      }
    },
    /**
     * 添加复制按钮
     */
    addCopyButtons() {
      const codeBlocks = document.querySelectorAll('.article-content pre')
      if (!codeBlocks.length) return

      codeBlocks.forEach(pre => {
        // 检查是否已经添加过复制按钮
        if (pre.querySelector('.code-header')) return

        // 创建复制按钮容器
        const buttonWrapper = document.createElement('div')
        buttonWrapper.className = 'code-header'

        // 创建复制按钮
        const copyButton = document.createElement('button')
        copyButton.className = 'copy-button'
        copyButton.innerHTML = '<i class="fas fa-copy"></i> 复制'
        copyButton.title = '复制代码'

        // 添加点击事件
        copyButton.addEventListener('click', async () => {
          try {
            const code = pre.querySelector('code')
            await navigator.clipboard.writeText(code.textContent)
            copyButton.innerHTML = '<i class="fas fa-check"></i> 已复制'
            copyButton.classList.add('copied')
            setTimeout(() => {
              copyButton.innerHTML = '<i class="fas fa-copy"></i> 复制'
              copyButton.classList.remove('copied')
            }, 2000)
            this.$message.success('复制成功')
          } catch (err) {
            this.$message.error('复制失败，请手动复制')
          }
        })

        // 将按钮添加到代码块
        buttonWrapper.appendChild(copyButton)
        pre.appendChild(buttonWrapper)
      })
    },
    /**
     * 初始化图片预览
     */
    initImagePreview() {
      // 使用 IntersectionObserver 监听图片
      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            const img = entry.target
            const actualSrc = img.getAttribute('data-src')
            if (actualSrc) {
              // 创建一个新的图片对象来预加载
              const tempImg = new Image()
              tempImg.onload = () => {
                img.src = actualSrc
                img.classList.add('loaded')
              }
              tempImg.onerror = () => {
                img.src = 'https://img.shiyit.com/base/mojian/img-error.jpg'
                img.classList.add('error')
              }
              tempImg.src = actualSrc
              img.removeAttribute('data-src')
              observer.unobserve(img)
            }
          }
        })
      }, {
        rootMargin: '50px 0px',
        threshold: 0.1
      })

      // 监听所有带有 lazy-image 类的图片
      setTimeout(() => {
        const lazyImages = document.querySelectorAll('.lazy-image')
        lazyImages.forEach(img => {
          observer.observe(img)
        })

        // 收集所有图片URL用于预览
        this.images = Array.from(document.querySelectorAll('.article-content img')).map(img => 
          img.getAttribute('data-src') || img.getAttribute('src')
        )

        // 为图片添加点击事件
        document.querySelectorAll('.article-content img').forEach(img => {
          img.style.cursor = 'zoom-in'
          img.addEventListener('click', this.handleImageClick)
        })
      }, 200)
    },
    /**
     * 处理图片点击
     */
    handleImageClick(e) {
      const img = e.target
      if (img.tagName === 'IMG') {
        this.$refs.imagePreview.show(this.images, this.images.indexOf(img.src))
      }
    },
    toggleDislike() {
      // 实现点踩功能
      if (this.likeDebounce) {
        this.$message.warning('请于 5 秒后再试')
        return
      }
      // TODO: 调用点踩 API
      this.$message.success(this.article.isDislike ? '取消点踩成功' : '点踩成功')
      if (this.article.isDislike) {
        this.article.dislikeNum--
      } else {
        this.article.dislikeNum++
      }
      this.article.isDislike = !this.article.isDislike
      this.likeDebounce = true
      setTimeout(() => {
        this.likeDebounce = false
      }, 5000)
    },
    toggleFavorite() {
      this.$message.warning('暂未开放')
      return
      // 实现收藏功能
      this.$message.success(this.article.isFavorite ? '取消收藏成功' : '收藏成功')
      if (this.article.isFavorite) {
        this.article.favoriteNum--
      } else {
        this.article.favoriteNum++
      }
      this.article.isFavorite = !this.article.isFavorite
    },
    toggleSidebar() {
      this.showSidebar = !this.showSidebar
      // 等待 DOM 更新后重新计算操作栏位置
      this.$nextTick(() => {
        this.updateActionBarPosition()
      })
    },
    scrollToComments() {
      const commentsSection = document.querySelector('.comment-section')
      if (commentsSection) {
        commentsSection.scrollIntoView({ behavior: 'smooth' })
      }
    },
    updateActionBarPosition() {
      const articleBox = document.getElementById("articleBox")
      if (articleBox) {
        const rect = articleBox.getBoundingClientRect()
        this.actionBarLeft = (rect.left - 95) + 'px'
      }
    },
    /**
     * 初始化代码块的展开/折叠功能
     */
    initializeCodeBlocks() {
      const codeBlocks = document.querySelectorAll('.article-content pre')
      codeBlocks.forEach((pre, index) => {
        // 移除可能存在的旧按钮
        const oldButton = pre.querySelector('.expand-button')
        if (oldButton) {
          oldButton.remove()
        }

        // 获取代码块的实际高度
        const actualHeight = pre.scrollHeight
        
        if (actualHeight > 500) {
          // 添加折叠类
          pre.classList.add('collapsed')
          
          // 创建展开按钮
          const expandButton = document.createElement('button')
          expandButton.className = 'expand-button'
          expandButton.innerHTML = '<i class="fas fa-chevron-down"></i>展开代码'
          
          // 添加点击事件
          expandButton.onclick = (e) => {
            e.stopPropagation()
            const isCollapsed = pre.classList.contains('collapsed')
            if (isCollapsed) {
              pre.classList.remove('collapsed')
              expandButton.innerHTML = '<i class="fas fa-chevron-up"></i>收起代码'
              this.collapsedCodeBlocks.delete(index)
            } else {
              pre.classList.add('collapsed')
              expandButton.innerHTML = '<i class="fas fa-chevron-down"></i>展开代码'
              this.collapsedCodeBlocks.add(index)
            }
          }
          
          pre.appendChild(expandButton)
        }
      })
    },
    /**
     * 添加行号
     */
    addLineNumbers() {
      const codeBlocks = document.querySelectorAll('.article-content pre code')
      codeBlocks.forEach((code) => {
        const pre = code.parentElement
        
        // 检查是否已添加行号
        if (!pre.querySelector('.line-numbers')) {
          const lines = code.textContent.split('\n').length
          const lineNumbers = document.createElement('div')
          lineNumbers.className = 'line-numbers'

          for (let i = 1; i <= lines; i++) {
            const span = document.createElement('span')
            span.textContent = i
            lineNumbers.appendChild(span)
          }

          pre.insertBefore(lineNumbers, code)
        }
      })
    },
    /**
     * 获取预览内容
     */
    getPreviewContent(content) {
      // 返回内容的前300个字符，并确保HTML标签完整
      const div = document.createElement('div')
      div.innerHTML = content
      const text = div.textContent || div.innerText
      return text.substring(0, 300) + '...'
    },
    /**
     * 处理会员升级
     */
    handleUpgrade() {
      if (!this.$store.state.userInfo) {
        this.$message.warning('请先登录')
        return
      }
      this.showMembershipDialog = true
    },
    /**
     * 处理付费购买
     */
    handlePurchase() {
      if (!this.$store.state.userInfo) {
        this.$message.warning('请先登录')
        return
      }
      this.showPaymentDialog = true
    },
    /**
     * 处理支付成功
     */
    handlePaymentSuccess() {
      // 重新获取文章信息
      this.getArticle()
    },
    /**
     * 处理会员开通成功
     */
    handleMembershipSuccess() {
      // 重新获取文章信息
      this.getArticle()
    },
    /**
     * 处理评论添加
     */
    handleCommentAdded() {
      this.article.commentNum = (this.article.commentNum || 0) + 1;
    },

    /**
     * 处理评论删除
     */
    handleCommentDeleted() {
      this.article.commentNum = Math.max((this.article.commentNum || 0) - 1, 0);
    },

    startTransition(element) {
      element.style.height = 'auto'
      const height = element.scrollHeight
      element.style.height = '0px'
      // 触发回流
      element.offsetHeight
      element.style.height = height + 'px'
    },
    endTransition(element) {
      element.style.height = element.scrollHeight + 'px'
      // 触发回流
      element.offsetHeight
      element.style.height = '0px'
    }
  },
  async created() {
    //await this.getArticle()
    await this.getAijsqMenuList()
    window.addEventListener('resize', this.updateActionBarPosition)
  },
  mounted() {
    window.addEventListener('scroll', this.updateActiveHeading)
    this.$nextTick(() => {
      this.initImagePreview()
    })
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.updateActiveHeading)
    window.removeEventListener('resize', this.updateActionBarPosition)
    // 清理图片点击事件监听器
    const images = document.querySelectorAll('.article-content img')
    images.forEach(img => {
      img.removeEventListener('click', this.handleImageClick)
    })
  },
  watch: {
    // 监听路由参数变化
    '$route'(to, from) {
      if (to.params.id !== from.params.id) {
        // 重新获取文章数据
        this.getArticleData() 
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.article-page {
  max-width: 1300px;
  margin: 0 auto;
  padding: $spacing-lg;
  @include responsive(lg) {
    padding: $spacing-sm;
  }
}

.content-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 300px;
  gap: $spacing-md * 2;
  transition: all 0.3s ease;

  &.center {
    grid-template-columns: 1fr;
    max-width: 900px;
    margin: 0 auto;
  }

  @include responsive(lg) {
    grid-template-columns: 1fr;
    gap: $spacing-lg;
    padding: 0;
  }
}

.article-main {
  background: var(--card-bg);
  border-radius: $border-radius-lg;
  box-shadow: $shadow-md;
  overflow: hidden;
}

.article-header {
  padding: $spacing-lg $spacing-xl;
  position: relative;
  border-bottom: 1px solid var(--border-color);
  background: var(--card-bg);

  .article-title {
    color: var(--text-primary);
    font-size: 1.8em;
    line-height: 1.4;
    margin-bottom: $spacing-lg;
    text-align: left;
  }

  .article-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: $spacing-md;

    .author-info {
      display: flex;
      align-items: center;
      gap: $spacing-md;

      .author-avatar {
        width: 42px;
        height: 42px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid rgba($primary, 0.2);
        padding: 2px;
        background: var(--card-bg);
        transition: all 0.3s ease;

        &:hover {
          transform: rotate(360deg);
          border-color: $primary;
        }
      }

      .author-meta {
        display: flex;
        flex-direction: column;
        gap: $spacing-xs;

        .author-name {
          color: $primary;
          font-weight: 600;
          font-size: 1.1em;
        }

        .post-meta {
          display: flex;
          align-items: center;
          gap: $spacing-sm;
          color: var(--text-secondary);
          font-size: 0.9em;

          i {
            color: $primary;
            margin-right: 4px;
          }

          .meta-divider {
            color: var(--text-secondary);
            opacity: 0.5;
          }

          .category {
            color: $primary;
          }
        }
      }
    }

    .article-stats {
      display: flex;
      align-items: center;
      gap: $spacing-lg;

      .stat-item {
        display: flex;
        align-items: center;
        gap: $spacing-xs;
        color: var(--text-secondary);
        font-size: 0.95em;

        i {
          color: $primary;
          font-size: 1.1em;
        }
      }
    }
  }
}

.article-content {
  padding: 0 $spacing-md * 2;
  line-height: 1.8;
  color: var(--text-primary);
  font-size: 1.1em;

  :deep(h2) {
    font-size: 1.8em;
    margin: $spacing-xl 0 $spacing-lg;
    padding-bottom: $spacing-sm;
    border-bottom: 2px solid rgba($primary, 0.1);
    position: relative;
    color: var(--text-primary);

    &::after {
      content: '';
      position: absolute;
      bottom: -2px;
      left: 0;
      width: 50px;
      height: 2px;
      background: $primary;
    }
  }

  :deep(h3) {
    font-size: 1.4em;
    margin: $spacing-lg 0;
    color: var(--text-primary);
    position: relative;
    padding-left: $spacing-lg;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 4px;
      height: 20px;
      background: $primary;
      border-radius: $border-radius-sm;
    }
  }

  :deep(p) {
    margin: $spacing-md 0;
    color: var(--text-secondary);
    line-height: 1.8;
  }

  :deep(a) {
    color: $primary;
    text-decoration: none;
    border-bottom: 1px dashed $primary;
    transition: all 0.3s ease;

    &:hover {
      color: var(--primary-dark);
      border-bottom-style: solid;
    }
  }

  :deep(blockquote) {
    margin: $spacing-lg 0;
    padding: $spacing-md $spacing-lg;
    background: var(--hover-bg);
    border-left: 4px solid $primary;
    border-radius: $border-radius-sm;
    color: var(--text-secondary);
    font-style: italic;

    p {
      margin: 0;
    }
  }

  :deep(ul),
  :deep(ol) {
    margin: $spacing-md 0;
    padding-left: $spacing-xl;
    color: var(--text-secondary);

    li {
      margin-bottom: $spacing-sm;
      position: relative;

      &::marker {
        color: $primary;
      }
    }
  }

  :deep(code:not([class])) {
    font-size: 14px;
    line-height: 1.5;
    position: relative;
    color:rgb(239, 89, 84);
    background:rgb(243, 244, 244);
    border-radius: 6px;
    padding: $spacing-xs;
    margin: 0 $spacing-xs;
  }

  :deep(pre) {
    margin: 1em 0;
    position: relative;
    background: #282c34;
    border-radius: 6px;
    padding-top: 2.5em;
    overflow: hidden;
    max-height: 2000px;
    transition: max-height 0.4s ease-in-out;

    &.collapsed {
      max-height: 300px;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 60px;
        background: linear-gradient(transparent, #282c34);
        pointer-events: none;
        z-index: 2;
      }

      .expand-button {
        display: flex !important;
      }
    }

    .expand-button {
      position: absolute;
      bottom: 15px;
      left: 50%;
      transform: translateX(-50%);
      padding: 6px 16px;
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
      border-radius: 4px;
      color: #abb2bf;
      cursor: pointer;
      z-index: 3;
      font-size: 0.9em;
      align-items: center;
      gap: 6px;
      transition: all 0.2s ease;
      white-space: nowrap;

      &:hover {
        background: rgba(255, 255, 255, 0.2);
        color: #fff;
        transform: translateX(-50%) translateY(-2px);
      }

      i {
        font-size: 14px;
      }
    }

    /* 添加行号容器样式 */
    .line-numbers {
      position: absolute;
      left: 0;
      top: 2.5em;
      bottom: 0;
      font-size: 14px;
      padding: 1em 0;
      text-align: right;
      color: #666;
      border-right: 1px solid #404040;
      background: #2d323b;
      user-select: none;
      z-index: 1;

      span {
        display: block;
        padding: 0 0.5em;
        min-width: 2.5em;
        line-height: 1.5;
      }
    }

    /* 调整代码内容的样式 */
    code {
      display: block;
      padding: 1em;
      padding-left: 4em;
      /* 增加左侧padding */
      margin-left: 0;
      /* 移除margin */
      overflow-x: auto;
      font-family: 'Fira Code', monospace;
      font-size: 14px;
      line-height: 1.5;
      position: relative;
    
    }

    /* 添加仿 macOS 风格的按钮 */
    &::before {
      content: '';
      position: absolute;
      top: 12px;
      left: 12px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff5f56;
      box-shadow:
        20px 0 0 #ffbd2e,
        40px 0 0 #27c93f;
    }

    /* 复制按钮容器 */
    .code-header {
      position: absolute;
      top: 8px;
      right: 12px;
      z-index: 2;
      opacity: 0;
      transition: opacity 0.2s ease;
    }

    /* 显示复制按钮 */
    &:hover .code-header {
      opacity: 1;
    }

    /* 复制按钮样式 */
    .copy-button {
      padding: 4px 8px;
      background: rgba(255, 255, 255, 0.1);
      border: none;
      border-radius: 4px;
      color: #abb2bf;
      cursor: pointer;
      font-size: 14px;
      transition: all 0.2s ease;
      display: flex;
      align-items: center;
      gap: 4px;

      i {
        font-size: 14px;
      }

      &:hover {
        background: rgba(255, 255, 255, 0.2);
        color: #fff;
      }

      &.copied {
        background: #98c379;
        color: #fff;
      }
    }
  }

  :deep(img.lazy-image) {
    opacity: 0;
  
    
    &.loaded {
      opacity: 1;
    }

    &.error {
      opacity: 0.5;
    }
  }

  :deep(img) {
    max-width: 100%;
    border-radius: $border-radius-md;
    margin: $spacing-lg 0;
    transition: all 0.3s ease;
    box-shadow: $shadow-md;
    cursor: zoom-in;

    &:hover {
      transform: translateY(-2px);
      box-shadow: $shadow-lg;
    }
  }

  :deep(table) {
    width: 100%;
    margin: $spacing-lg 0;
    border-collapse: collapse;
    border-radius: $border-radius-md;
    overflow: hidden;

    th,
    td {
      padding: $spacing-sm $spacing-md;
      border: 1px solid var(--border-color);
    }

    th {
      background: var(--hover-bg);
      color: var(--text-primary);
      font-weight: 500;
      text-align: left;
    }

    tr:nth-child(even) {
      background: var(--hover-bg);
    }
  }

  :deep(hr) {
    margin: $spacing-xl 0;
    border: none;
    height: 1px;
    background: var(--border-color);
    position: relative;

    &::before {
      content: '§';
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      background: var(--card-bg);
      padding: 0 $spacing-lg;
      color: var(--text-secondary);
      font-size: 1.2em;
    }
  }

  .locked-content {
    position: relative;
    
    .preview-content {
      max-height: 300px;
      overflow: hidden;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 100px;
        background: linear-gradient(transparent, var(--card-bg));
        pointer-events: none;
      }
    }
    
    .content-locker {
      position: relative;
      margin-top: -60px;
      padding: $spacing-xl;
      text-align: center;
      background: var(--card-bg);
      border-radius: $border-radius-lg;
      box-shadow: $shadow-lg;
      z-index: 1;
      
      .locker-icon {
        width: 60px;
        height: 60px;
        margin: 0 auto $spacing-lg;
        background: rgba($primary, 0.1);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        i {
          font-size: 1.8em;
          color: $primary;
        }
      }
      
      h3 {
        color: var(--text-primary);
        font-size: 1.4em;
        margin-bottom: $spacing-sm;
      }
      
      p {
        color: var(--text-secondary);
        margin-bottom: $spacing-lg;
      }
      
      .el-button {
        padding: $spacing-sm $spacing-xl;
        font-size: 1.1em;
      }
    }
    
    &.member .locker-icon {
      background: rgba(#FFD700, 0.1);
      
      i {
        color: #FFD700;
      }
    }
    
    &.paid .locker-icon {
      background: rgba(#E6162D, 0.1);
      
      i {
        color: #E6162D;
      }
    }
  }
}

.article-footer {
  padding: $spacing-md * 2;
  border-top: 1px solid var(--border-color);

  @include responsive(lg) {
    padding: $spacing-sm;
  }

  .copyright-notice {
    margin-bottom: $spacing-xl;
    background: var(--hover-bg);
    border-radius: $border-radius-lg;
    overflow: hidden;

    .notice-header {
      padding: $spacing-md $spacing-lg;
      background: rgba($primary, 0.1);
      color: $primary;
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: $spacing-sm;
    }

    .notice-content {
      padding: $spacing-lg;
      color: var(--text-secondary);
      font-size: 0.95em;
      line-height: 1.6;

      .notice-item {
        display: flex;
        align-items: center;
        gap: $spacing-sm;
        padding: $spacing-xs 0;

        i {
          color: $primary;
          font-size: 1em;
          width: 16px;
          text-align: center;
        }

        a {
          color: $primary;
          text-decoration: none;
          border-bottom: 1px dashed $primary;
          transition: all 0.2s ease;

          &:hover {
            border-bottom-style: solid;
          }
        }

        &.notice-warning {
          margin-top: $spacing-sm;
          padding: $spacing-sm;
          background: rgba($primary, 0.05);
          border-radius: $border-radius-sm;

          i {
            color: #ff9800;
          }
        }
      }
    }
  }

  .tags-section {
    display: flex;
    align-items: center;
    gap: $spacing-md;
    margin-bottom: $spacing-xl;

    i {
      color: $primary;
    }

    .tags-list {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
    }

    .tag-item {
      padding: $spacing-xs $spacing-md;
      background: var(--hover-bg);
      color: var(--text-secondary);
      border-radius: $border-radius-lg;
      font-size: 0.9em;
      text-decoration: none;
      transition: all 0.3s ease;

      &:hover {
        background: $primary;
        color: white;
        transform: translateY(-2px);
      }
    }
  }

  .article-actions {
    display: flex;
    justify-content: center;
    gap: $spacing-lg;
    margin-bottom: $spacing-xl;

    .action-btn {
      display: inline-flex;
      align-items: center;
      gap: $spacing-sm;
      padding: $spacing-sm $spacing-xl;
      border: none;
      border-radius: 20px;
      font-size: 1em;
      cursor: pointer;
      transition: all 0.3s ease;

      &.like {
        background: var(--hover-bg);
        color: var(--text-secondary);

        &.active {
          background: $primary;
          color: white;
        }

        &:hover {
          transform: scale(1.05);
        }
      }

      &.share {
        background: $primary;
        color: white;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba($primary, 0.2);
        }
      }
    }

    :deep(.share-dropdown) {
      position: relative;

      .share-menu {
        position: absolute;
        bottom: calc(100% + 8px);
        right: 0;
        background: var(--card-bg);
        border-radius: $border-radius-lg;
        box-shadow: $shadow-lg;
        padding: $spacing-xs;
        min-width: 180px;
        z-index: 100;
        border: 1px solid var(--border-color);
        transform-origin: bottom right;
        animation: shareMenuIn 0.2s ease;

        &::before {
          content: '';
          position: absolute;
          bottom: -5px;
          right: 20px;
          width: 10px;
          height: 10px;
          background: var(--card-bg);
          border-left: 1px solid var(--border-color);
          border-top: 1px solid var(--border-color);
          transform: rotate(225deg);
        }

        .share-item {
          width: 100%;
          padding: $spacing-sm $spacing-lg;
          border: none;
          background: none;
          color: var(--text-secondary);
          font-size: 0.95em;
          display: flex;
          align-items: center;
          gap: $spacing-md;
          cursor: pointer;
          transition: all 0.3s ease;
          border-radius: $border-radius-sm;

          i {
            width: 16px;
            text-align: center;
            font-size: 1.1em;

            &.fa-qq {
              color: #12B7F5;
            }

            &.fa-star {
              color: #FFD700;
            }

            &.fa-weibo {
              color: #E6162D;
            }

            &.fa-weixin {
              color: #07C160;
            }

            &.fa-link {
              color: $primary;
            }
          }

          &:hover {
            background: var(--hover-bg);
            padding-left: $spacing-xl;
          }

          &:active {
            transform: scale(0.95);
          }
        }
      }
    }
  }
}

.article-sidebar {
  .toc-container {
    position: sticky;
    top: 90px;
    background: var(--card-bg);
    border-radius: $border-radius-lg;
    box-shadow: $shadow-md;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid var(--border-color);
    backdrop-filter: blur(10px);
    
    &::before {
      content: '';
      position: absolute;
      inset: 0;
      background: linear-gradient(45deg, transparent, rgba($primary, 0.03), transparent);
      opacity: 0;
      transition: opacity 0.3s ease;
    }


    .toc-header {
      padding: $spacing-lg;
      background: var(--hover-bg);
      color: var(--text-primary);
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      position: relative;
      border-bottom: 1px solid var(--border-color);
      justify-content: space-between;
      
      .title-wrapper {
        display: flex;
        align-items: center;
        gap: $spacing-sm;

        i {
          color: $primary;
          font-size: 1.1em;
          transform-origin: center;
        }
      }

      .progress-wrapper {
        font-size: 0.9em;
        color: var(--text-secondary);
        display: flex;
        align-items: center;
        gap: $spacing-xs;
        padding: 4px 8px;
        background: rgba($primary, 0.05);
        border-radius: $border-radius-lg;
        transition: all 0.3s ease;

        i {
          color: $primary;
          font-size: 0.9em;
        }

        .progress-text {
          font-variant-numeric: tabular-nums;
          min-width: 3em;
          text-align: right;
          
          &::after {
            content: '%';
            margin-left: 2px;
            opacity: 0.7;
          }
        }
      }
    }

    .toc-content {
      padding: $spacing-lg;
      max-height: calc(100vh - 200px);
      overflow-y: auto;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        left: 24px;
        top: 0;
        bottom: 0;
        width: 1px;
        background: linear-gradient(
          to bottom,
          transparent,
          rgba($primary, 0.1),
          rgba($primary, 0.1),
          transparent
        );
      }

      .toc-item {
        padding: $spacing-sm $spacing-md;
        margin: 2px 0;
        cursor: pointer;
        border-radius: $border-radius-sm;
        color: var(--text-secondary);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        font-size: 0.95em;
        line-height: 1.4;
        position: relative;
        display: flex;
        align-items: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding-left: 16px;

        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 0;
          height: 0;
          background: $primary;
          border-radius: 50%;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          opacity: 0;
          box-shadow: 0 0 4px rgba($primary, 0.4);
        }

        &:hover {
          color: $primary;
          background: linear-gradient(90deg,
            rgba($primary, 0.05),
            rgba($primary, 0.02)
          );
          padding-left: 20px;

          &::before {
            width: 6px;
            height: 6px;
            opacity: 1;
          }
        }

        &.active {
          color: $primary;
          background: linear-gradient(90deg,
            rgba($primary, 0.1),
            rgba($primary, 0.05)
          );
          font-weight: 500;
          padding-left: 20px;

          &::before {
            width: 6px;
            height: 6px;
            opacity: 1;
            animation: tocDotPulse 1.5s infinite;
          }
        }

        &.level-1 {
          font-weight: 500;
          font-size: 1em;
        }

        &.level-2 {
          font-size: 0.95em;
        }

        &.level-3 {
          font-size: 0.9em;
        }

        &.level-4 {
          font-size: 0.88em;
        }

        &.level-5, &.level-6 {
          font-size: 0.86em;
          opacity: 0.8;
          
          &:hover {
            opacity: 1;
          }
        }
      }
    }
  }
}


@keyframes tocDotPulse {
  0% {
    box-shadow: 0 0 0 0 rgba($primary, 0.4);
  }
  70% {
    box-shadow: 0 0 0 4px rgba($primary, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba($primary, 0);
  }
}

@keyframes shareMenuIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.desktop-only {
  @include responsive(lg) {
    display: none;
  }
}

.floating-action-bar {
  position: fixed;
  top: 52%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: $spacing-md * 1.5;
  padding: $spacing-sm;
  border-radius: $border-radius-lg;
  z-index: 100;
  transition: left 0.3s ease;

  @include responsive(lg) {
    display: none;
  }

  .action-item {
    cursor: pointer;
    .action-button {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: var(--card-bg);
      transition: all 0.3s ease;

      i {
        font-size: 1.2em;
        color: var(--text-secondary);
        transition: all 0.3s ease;

        &.active {
          color: $primary;
        }
      }
    }

    .action-count {
      font-size: 0.8em;
      color: var(--text-secondary);
      min-width: 20px;
      text-align: center;
    }

    &:hover {
      .action-button {
        background: rgba($primary, 0.1);
        transform: translateY(-2px);

        i {
          color: $primary;
          transform: scale(1.1);
        }
      }
    }
  }

  .reward-item {
    position: relative;

    .reward-popup {
      position: absolute;
      left: calc(100% + 16px);
      top: 50%;
      transform: translateY(-50%);
      background: var(--card-bg);
      border-radius: $border-radius-lg;
      padding: $spacing-md;
      box-shadow: $shadow-lg;
      opacity: 0;
      visibility: hidden;
      transition: all 0.3s ease;
      pointer-events: none;
      width: 510px;
      
      &::before {
        content: '';
        position: absolute;
        left: -6px;
        top: 50%;
        transform: translateY(-50%) rotate(45deg);
        width: 12px;
        height: 12px;
        background: var(--card-bg);
        border-radius: 2px;
      }

      .reward-content {
        display: flex;
        gap: $spacing-md;
        margin-bottom: $spacing-sm;

        .reward-qr {
          width: 250px;
          height: 250px;
          border-radius: $border-radius-sm;
          object-fit: cover;
        }
      }

      .reward-text {
        text-align: center;
        color: var(--text-secondary);
        font-size: 0.9em;
      }
    }

    &:hover {
      .reward-popup {
        opacity: 1;
        visibility: visible;
        pointer-events: auto;
        transform: translateY(-50%) translateX(8px);
      }
    }
  }
}

.ai-description {
  margin: $spacing-md $spacing-xl;
  border-radius: $border-radius-lg;
  background-image: linear-gradient(180deg, rgba(247, 232, 255, .54), rgba(191, 223, 255, .35));
  border: 1px solid rgba(0, 150, 136, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;

  @include responsive(sm) {
    margin: $spacing-sm;
  }
  


  .ai-header {
    padding: $spacing-sm $spacing-md;
    font-weight: 500;
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    justify-content: flex-start;
    color: #a855f7;
    cursor: pointer;

    i {
      font-size: 1.1em;
    }
  }

  .ai-content {
    padding: $spacing-md;
    overflow: hidden;
  }
  .ai-description-text {
    margin-top: $spacing-sm;
    color: #8c8a8e;
  }
}

.expand-enter-active,
.expand-leave-active {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  height: 0;
}

.expand-enter-to,
.expand-leave-from {
  opacity: 1;
}
/* 侧边栏样式 */
.sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;  /* 允许垂直滚动 */
  overflow-x: hidden; /* 隐藏水平滚动 */
  transition: width 0.3s ease;
  position: relative;
  height: 100vh;
}

.sidebar-header {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #666;
  border-bottom: 1px solid #e0e0e0;
  height: 60px;
  flex-shrink: 0; /* 防止头部被压缩 */
}

.sidebar-controls {
  display: flex;
  gap: 10px;
}

.collapse-icon,
.add-icon {
  font-size: 18px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.collapse-icon:hover,
.add-icon:hover {
  color: #1890ff;
}

.menu-item-highlight {
  background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 100%);
  padding: 12px 20px;
  margin: 8px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-weight: 500;
  color: #1976d2;
  transition: all 0.3s;
  flex-shrink: 0;
}

.menu-item-highlight:hover {
  background: linear-gradient(135deg, #d1ecff 0%, #e8d4f0 100%);
}

.menu-item-highlight.collapsed {
  padding: 12px;
  justify-content: center;
}

.menu-icon {
  margin-right: 8px;
  font-size: 18px;
}

.menu-item-highlight.collapsed .menu-icon {
  margin-right: 0;
}

.el-menu-vertical {
  border: none;
  overflow-y: auto;
  overflow-x: hidden;
}

.el-menu--collapse {
  width: 64px;
}

.el-menu-item,
.el-submenu__title {
  height: 44px;
  line-height: 44px;
  font-size: 14px;
  position: relative;
  white-space: nowrap;
}

.el-menu-item i,
.el-submenu__title i {
  margin-right: 8px;
  color: #666;
}

.el-submenu .el-menu-item {
  padding-left: 50px !important;
  min-width: auto;
}

.menu-actions {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  opacity: 0;
  transition: opacity 0.3s;
}

.el-menu-item:hover .menu-actions,
.el-submenu__title:hover .menu-actions {
  opacity: 1;
}

.edit-icon {
  color: #666;
  font-size: 14px;
  cursor: pointer;
  padding: 4px;
}

</style>