<template>
  <div class="article-page" v-loading="loading">
    <div class="content-layout" id="articleBox" :class="{ center: !showSidebar }">
      <!-- 左侧导航栏 -->
      <el-aside  class="sidebar">
        <div class="sidebar-header">
          <span v-if="!isCollapsed">知识库目录</span>
          <el-tooltip v-else content="知识库目录" placement="right">
            <i class="el-icon-notebook-2"></i>
          </el-tooltip>
          <div class="sidebar-controls">
            <el-tooltip :content="isCollapsed ? '展开侧边栏' : '折叠侧边栏'" placement="right">
              <!-- <i 
                class="el-icon-s-unfold collapse-icon" 
                @click="toggleSidebar"
              ></i> -->
            </el-tooltip>
            <el-tooltip v-if="!isCollapsed" content="添加新菜单" placement="right">
              <i 
                class="el-icon-circle-plus add-icon" 
                @click="$router.push('/shejishi_paths/1')"
              ></i>
            </el-tooltip>
          </div>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          :default-openeds="defaultOpenedKeys"
          :collapse="isCollapsed"
          :collapse-transition="false"
          class="el-menu-vertical"
          @select="handleMenuSelect"
          text-color="#333"
          active-text-color="#1890ff"
          style="height: calc(100vh - 120px); overflow-y: auto;"
        >
          <!-- 通往AGI之路 - 特殊项 -->
          <!-- <div 
            :class="['menu-item-highlight', { 'collapsed': isCollapsed }]"
            @click="handleSpecialMenu('agi-road')">
            <i class="menu-icon">🌈</i>
            <span v-if="!isCollapsed">通往AI设计师之路</span>
            <el-tooltip v-else content="通往AI设计师之路" placement="right">
              <span>🌈</span>
            </el-tooltip>
          </div> -->

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
                    @click.stop="$router.push('/waytoagi-editor/0')"
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
          @click="$router.push('/waytoagi-editor/0')">
          <i class="el-icon-circle-plus-outline"></i>
          <span>添加新菜单</span>
        </div>
      </el-aside>
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
         <div v-if="article.id != undefined">
            <Comment 
              :article-id="article.id" 
              :comment-count="article.commentNum || 0"
              :article-author-id="article.userId || ''" 
              @comment-added="handleCommentAdded"
              @comment-deleted="handleCommentDeleted"
            />
         </div>
        
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

export default {
  name: 'shejishi',
  components: {
    Comment,
    //PaymentDialog,
    MembershipDialog
  },
  data() {
    return {
      article: {
        id:undefined,
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
      defaultOpenedKeys: [],
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
    // 提取所有父菜单的ID
    getAllParentKeys(menuItems) {
      const keys = []
      
      function traverse(items) {
        items.forEach(item => {
          if (item.children && item.children.length > 0) {
            // 如果是父菜单，添加其ID
            keys.push(String(item.id))
            // 递归遍历子菜单
            //traverse(item.children)
          }
        })
      }
      
      traverse(menuItems)
      return keys
    },
    async getAijsqMenuList(){
      try {
        let menu_params = {}
        menu_params.type="1"
        const { data } = await getMenuListApi(menu_params)
        this.menuItems = data
        //this.treeSelectOptions = this.transformToTreeSelectOptions(this.menuItems)
        // 获取所有父菜单的key并设置默认展开
        this.defaultOpenedKeys = this.getAllParentKeys(this.menuItems)
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
        if(menuId == null|| (typeof menuId === "string" && menuId.trim() === "")){
          menuId = this.$route.params.menuId
        }
        const res = await getDetailByMenuIdApi(menuId)
        this.article = {
          ...res.data,
          content: res.data.content ? this.addLazyLoadToImages(res.data.content) : ''
        }
        console.log(this.article.id)
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
    await this.getArticle("B5bDcbGhAnRvzWkvF8PcZgk9KsS970UJJiVjYqyJjpd9jx")
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

<style lang="scss" scoped src="../../styles/article-layout.scss"></style>