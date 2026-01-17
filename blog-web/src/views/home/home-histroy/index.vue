<template>
  <div id="app">
    <el-container class= "navzhishi" style="height: calc(100vh-80px);max-width: 1300px;
  margin: 0 auto;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;">
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
          active-text-color="#1890ff">
          
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
              :index="item.id"
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
                  :index="child.id">
                  <template slot="title">
                    <i :class="child.icon || 'el-icon-document'"></i>
                    <span>{{ child.title }}</span>
                  </template>
                  <el-menu-item 
                    v-for="subChild in child.children"
                    :key="subChild.id"
                    :index="subChild.id"
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
                  :index="child.id"
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
              :index="item.id"
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

      <!-- 主内容区 -->
      <el-container>
        <el-header height="auto" class="main-header">
          <div class="header-left">
            <i 
              class="el-icon-arrow-left back-icon" 
              @click="handleBack"
              v-if="historyStack.length > 0"
            ></i>
            <div class="breadcrumb" v-if="!isCollapsed">
              <span 
                v-for="(item, index) in breadcrumb" 
                :key="index"
                class="breadcrumb-item">
                <span 
                  class="breadcrumb-text"
                  @click="handleBreadcrumbClick(item)">
                  {{ item.title }}
                </span>
                <i 
                  v-if="index < breadcrumb.length - 1" 
                  class="el-icon-arrow-right"
                ></i>
              </span>
            </div>
          </div>
          
          <div class="header-content">
            <h1 class="page-title">
              <span class="rainbow-icon">🌈</span>
              {{ currentContent.title || '通往AGI之路' }}
            </h1>
            <div class="header-meta">
              <span class="meta-item">{{ currentContent.createTime || '2023年4月26日创建' }}</span>
              <span class="meta-item">
                <i class="el-icon-view"></i> {{ currentContent.views || '2438364' }}
              </span>
              <span class="meta-item">
                <i class="el-icon-star-off"></i> {{ currentContent.stars || '5200661' }}
              </span>
              <span class="meta-item">
                <i class="el-icon-thumb"></i> {{ currentContent.likes || '2939' }}
              </span>
              <span class="meta-item">
                <i class="el-icon-chat-dot-round"></i> {{ currentContent.comments || '715' }}
              </span>
            </div>
          </div>
          
          <div class="header-right">
            <el-tooltip content="刷新内容" placement="bottom">
              <i 
                class="el-icon-refresh refresh-icon" 
                @click="refreshContent"
              ></i>
            </el-tooltip>
            <el-tooltip content="评论" placement="bottom">
              <i class="el-icon-message comment-icon"></i>
            </el-tooltip>
          </div>
        </el-header>

        <el-main class="main-content">
          <!-- Markdown内容显示区域 -->
          <div class="markdown-container" v-if="currentContent.content">
            <div class="markdown-toolbar">
              <el-tooltip content="编辑内容" placement="top">
                <el-button 
                  type="text" 
                  icon="el-icon-edit"
                  @click="handleEditContent"
                  v-if="!isEditingContent"
                >
                  编辑
                </el-button>
              </el-tooltip>
              <el-button 
                type="text" 
                icon="el-icon-download"
                @click="exportContent"
              >
                导出
              </el-button>
              <el-button 
                type="text" 
                icon="el-icon-printer"
                @click="printContent"
              >
                打印
              </el-button>
            </div>
            
            <!-- 内容编辑模式 -->
            <div v-if="isEditingContent" class="content-editor">
              <el-input
                v-model="editContent.title"
                placeholder="请输入标题"
                class="edit-title"
              ></el-input>
              <mavon-editor
                v-model="editContent.markdown"
                :toolbars="markdownToolbars"
                style="height: 500px; margin-top: 20px;"
              />
              <div class="editor-actions">
                <el-button @click="cancelEdit">取消</el-button>
                <el-button type="primary" @click="saveContent">保存</el-button>
              </div>
            </div>
            
            <!-- Markdown预览模式 -->
            <div v-else class="markdown-preview">
              <mavon-editor
                v-model="currentContent.markdown"
                :editable="false"
                :subfield="false"
                :defaultOpen="'preview'"
                :toolbarsFlag="false"
                :boxShadow="false"
                style="border: none;"
              />
            </div>
          </div>
          
          <!-- 默认内容（当没有选择具体菜单时） -->
          <div v-else class="default-content">
            <div class="content-section">
              <h2 class="section-title">
                <span class="target-icon">🎯</span>
                愿景和目标
              </h2>
              <div class="highlight-box">
                <p class="highlight-text">
                  <span class="bulb-icon">💡</span>
                  要去现通用人工智能（AGI）设计有很长的路要走，
                </p>
                <p class="highlight-text-main">
                  我们的目标是让每个人的学习程度走愈酷，让更多的人因 AI 而成长。
                </p>
              </div>
            </div>

            <div class="content-section">
              <h2 class="section-title">
                <span class="pen-icon">✏️</span>
                简介 | <span class="link-text">WaytoAGI.com</span>
              </h2>
              <div class="intro-container">
                <div class="intro-text">
                  <p>欢迎来到通往 AGI 之路 / WaytoAGI社区致力于千人工智能学习的知识库与社区平台，提供系统全面的 AI 学习路径，整合资源、组织实践活动，覆盖近 900 万学习者，知识库访问基础 8000 万次，联动 180 所高校及 100 + 企业，举办 500 + 场活动。</p>
                  <div class="link-buttons">
                    <el-button type="text" icon="el-icon-info">如何更好地使用</el-button>
                    <el-button type="text" icon="el-icon-chat-line-round">通往AGI之路社区 业务说明</el-button>
                    <el-button type="text" icon="el-icon-video-camera">WaytoAGI 品牌 VI</el-button>
                  </div>
                </div>
                <div class="qr-code">
                  <img src="https://via.placeholder.com/120x120?text=QR+Code" alt="QR Code">
                  <p class="qr-text">公众号：通往 AGI 之路</p>
                  <p class="qr-links">Bilibili | 小红书 | Twitter</p>
                </div>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 添加/编辑菜单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="menuDialogVisible"
      width="500px"
      @close="resetDialogForm"
    >
      <el-form :model="menuForm" :rules="menuRules" ref="menuFormRef" label-width="80px">
        <el-form-item label="父菜单" prop="parentId">
          <el-select 
            v-model="menuForm.parentId" 
            placeholder="请选择父菜单"
            clearable
            style="width: 100%;"
          >
            <el-option 
              v-for="item in allMenuItems"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="menuForm.title" placeholder="请输入菜单标题"></el-input>
        </el-form-item>
        
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="menuForm.icon" placeholder="请输入图标类名，如: el-icon-document">
            <template slot="append">
              <i class="el-icon-picture" @click="showIconSelector = true"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="菜单内容" prop="markdown">
          <el-input
            type="textarea"
            v-model="menuForm.markdown"
            :rows="5"
            placeholder="请输入Markdown格式的内容"
          ></el-input>
          <div class="markdown-tips">
            <small>支持Markdown语法：**加粗**、*斜体*、[链接](url)、`代码`等</small>
          </div>
        </el-form-item>
      </el-form>
      
      <span slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitMenuForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

export default {
  name: 'WayToAGI',
  components: {
    mavonEditor
  },
  data() {
    return {
      isCollapsed: false,
      activeMenu: '',
      currentContent: {},
      isEditingContent: false,
      editContent: {
        title: '',
        markdown: ''
      },
      historyStack: [],
      breadcrumb: [],
      
      // 菜单数据
      menuItems: [
        {
          id: '1',
          title: '0. 从这里起程',
          icon: 'el-icon-document',
          markdown: '# 从这里起程\n\n欢迎来到通往AGI之路！这是你的起点。',
          children: []
        },
        {
          id: '2',
          title: 'A1领导力修炼之路',
          icon: 'el-icon-star-off',
          markdown: '# A1领导力修炼之路\n\n学习如何成为AI时代的领导者。',
          children: []
        },
        {
          id: '3',
          title: '社区共创项目及比赛',
          icon: 'el-icon-trophy',
          markdown: '# 社区共创项目\n\n参与我们的社区项目和比赛。',
          children: []
        },
        {
          id: '4',
          title: 'AI训练营',
          icon: 'el-icon-reading',
          markdown: '# AI训练营\n\n系统的AI培训课程。',
          children: []
        },
        {
          id: '5',
          title: 'WaytoAGI 高校探索',
          icon: 'el-icon-school',
          markdown: '# 高校探索\n\n与高校合作的项目。',
          children: []
        },
        {
          id: '6',
          title: '1.1 入门: AI 学习路径与课程',
          icon: 'el-icon-notebook-2',
          markdown: '# AI学习路径\n\n从入门到精通的学习路线。',
          children: []
        },
        {
          id: '16',
          title: '2.1 喂起码: 个人专栏',
          icon: 'el-icon-folder-opened',
          children: [
            {
              id: '16-1',
              title: '提示词目录',
              icon: 'el-icon-tickets',
              markdown: '# 提示词目录\n\n所有提示词的索引。'
            },
            {
              id: '16-2',
              title: '小土堆-提示词',
              icon: 'el-icon-document',
              markdown: '# 小土堆提示词\n\n来自小土堆的提示词分享。'
            },
            {
              id: '16-3',
              title: '蓝衣侠者-AIGC提炼火花',
              icon: 'el-icon-document',
              markdown: '# AIGC提炼火花\n\n蓝衣侠者的AIGC经验分享。'
            },
            {
              id: '16-4',
              title: '云中江树- LangGPT 提示词',
              icon: 'el-icon-document',
              markdown: '# LangGPT提示词\n\n云中江树的LangGPT提示词。'
            },
            {
              id: '16-5',
              title: '周慎谦士-提示词',
              icon: 'el-icon-document',
              markdown: '# 周慎谦士提示词\n\n周慎谦士的提示词分享。'
            },
            {
              id: '16-6',
              title: '繁鹤居-提示词',
              icon: 'el-icon-document',
              markdown: '# 繁鹤居提示词\n\n繁鹤居的提示词分享。'
            }
          ]
        }
      ],
      
      // 菜单对话框相关
      menuDialogVisible: false,
      dialogTitle: '添加菜单',
      editingMenuId: null,
      menuForm: {
        parentId: '',
        title: '',
        icon: 'el-icon-document',
        markdown: ''
      },
      menuRules: {
        title: [
          { required: true, message: '请输入菜单标题', trigger: 'blur' }
        ]
      },
      
      // Markdown编辑器工具栏配置
      markdownToolbars: {
        bold: true,
        italic: true,
        header: true,
        underline: true,
        strikethrough: true,
        mark: true,
        superscript: true,
        subscript: true,
        quote: true,
        ol: true,
        ul: true,
        link: true,
        imagelink: true,
        code: true,
        table: true,
        fullscreen: true,
        readmodel: true,
        htmlcode: true,
        help: true,
        undo: true,
        redo: true,
        trash: true,
        save: false,
        navigation: true,
        subfield: true,
        preview: true
      }
    }
  },
  computed: {
    // 所有可选的父菜单（用于下拉选择）
    allMenuItems() {
      const items = []
      
      const flatten = (menuList) => {
        menuList.forEach(item => {
          // 只添加没有子菜单或者子菜单未展开的项作为父菜单
          if (!item.children || item.children.length === 0) {
            items.push({
              id: item.id,
              title: item.title
            })
          } else {
            items.push({
              id: item.id,
              title: item.title + ' (文件夹)'
            })
            flatten(item.children)
          }
        })
      }
      
      flatten(this.menuItems)
      return items
    }
  },
  methods: {
    // 切换侧边栏折叠状态
    toggleSidebar() {
      this.isCollapsed = !this.isCollapsed
    },
    
    // 处理菜单选择
    handleMenuSelect(index, indexPath) {
      this.activeMenu = index
      this.loadContentById(index)
    },
    
    // 处理特殊菜单点击
    handleSpecialMenu(type) {
      this.activeMenu = 'agi-road'
      this.currentContent = {
        title: '通往AGI之路',
        markdown: `# 通往AGI之路\n\n## 社区数据\n- 成员: 2438364\n- 学习时长: 5200661\n- 课程: 2939\n- 合作伙伴: 715\n\n## 核心目标\n让每个人在AI时代都不掉队！`
      }
      this.breadcrumb = [{ id: 'agi-road', title: '通往AGI之路' }]
    },
    
    // 处理子菜单点击
    handleSubMenuClick(menuItem) {
      this.loadContent(menuItem)
    },
    
    // 处理菜单项点击
    handleMenuClick(menuItem) {
      this.loadContent(menuItem)
    },
    
    // 加载内容
    loadContent(menuItem) {
      this.currentContent = {
        title: menuItem.title,
        markdown: menuItem.markdown || '# ' + menuItem.title + '\n\n内容正在建设中...',
        createTime: '2023年4月26日创建',
        views: Math.floor(Math.random() * 1000000).toString(),
        stars: Math.floor(Math.random() * 1000000).toString(),
        likes: Math.floor(Math.random() * 1000).toString(),
        comments: Math.floor(Math.random() * 1000).toString()
      }
      
      // 添加到历史记录
      this.addToHistory(menuItem)
      
      // 更新面包屑
      this.updateBreadcrumb(menuItem)
    },
    
    // 根据ID加载内容
    loadContentById(id) {
      const findMenu = (items, targetId) => {
        for (const item of items) {
          if (item.id === targetId) {
            return item
          }
          if (item.children) {
            const found = findMenu(item.children, targetId)
            if (found) return found
          }
        }
        return null
      }
      
      const menuItem = findMenu(this.menuItems, id)
      if (menuItem) {
        this.loadContent(menuItem)
      }
    },
    
    // 添加到历史记录
    addToHistory(menuItem) {
      // 移除重复项
      this.historyStack = this.historyStack.filter(item => item.id !== menuItem.id)
      // 添加到栈顶
      this.historyStack.unshift({
        id: menuItem.id,
        title: menuItem.title
      })
      // 限制历史记录数量
      if (this.historyStack.length > 10) {
        this.historyStack.pop()
      }
    },
    
    // 更新面包屑导航
    updateBreadcrumb(menuItem) {
      this.breadcrumb = [
        { id: 'root', title: '首页' },
        { id: menuItem.id, title: menuItem.title }
      ]
    },
    
    // 处理面包屑点击
    handleBreadcrumbClick(item) {
      if (item.id === 'root') {
        this.currentContent = {}
        this.breadcrumb = []
        this.activeMenu = ''
      } else {
        this.loadContentById(item.id)
      }
    },
    
    // 处理返回
    handleBack() {
      if (this.historyStack.length > 1) {
        // 移除当前项
        this.historyStack.shift()
        // 加载上一项
        const prevItem = this.historyStack[0]
        this.loadContentById(prevItem.id)
      }
    },
    
    // 刷新内容
    refreshContent() {
      if (this.activeMenu) {
        this.loadContentById(this.activeMenu)
        this.$message({
          message: '内容已刷新',
          type: 'success'
        })
      }
    },
    
    // 添加菜单
    handleAddMenu() {
      this.dialogTitle = '添加菜单'
      this.editingMenuId = null
      this.menuForm = {
        parentId: '',
        title: '',
        icon: 'el-icon-document',
        markdown: '# 新菜单\n\n请输入内容...'
      }
      this.menuDialogVisible = true
    },
    
    // 编辑菜单
    handleEditMenu(menuItem) {
      this.dialogTitle = '编辑菜单'
      this.editingMenuId = menuItem.id
      this.menuForm = {
        parentId: this.findParentId(menuItem.id),
        title: menuItem.title,
        icon: menuItem.icon || 'el-icon-document',
        markdown: menuItem.markdown || '# ' + menuItem.title
      }
      this.menuDialogVisible = true
    },
    
    // 查找父菜单ID
    findParentId(targetId) {
      const findParent = (items, targetId, parentId = null) => {
        for (const item of items) {
          if (item.id === targetId) {
            return parentId
          }
          if (item.children) {
            const result = findParent(item.children, targetId, item.id)
            if (result !== null) return result
          }
        }
        return null
      }
      
      return findParent(this.menuItems, targetId)
    },
    
    // 提交菜单表单
    submitMenuForm() {
      this.$refs.menuFormRef.validate((valid) => {
        if (valid) {
          if (this.editingMenuId) {
            // 编辑现有菜单
            this.updateMenuItem(this.editingMenuId, this.menuForm)
            this.$message({
              message: '菜单已更新',
              type: 'success'
            })
          } else {
            // 添加新菜单
            this.addMenuItem(this.menuForm)
            this.$message({
              message: '菜单已添加',
              type: 'success'
            })
          }
          this.menuDialogVisible = false
        }
      })
    },
    
    // 添加菜单项
    addMenuItem(formData) {
      const newItem = {
        id: 'menu-' + Date.now(),
        title: formData.title,
        icon: formData.icon,
        markdown: formData.markdown
      }
      
      if (formData.parentId) {
        // 添加到指定父菜单
        this.addToParentMenu(formData.parentId, newItem)
      } else {
        // 添加到根菜单
        this.menuItems.push(newItem)
      }
    },
    
    // 更新菜单项
    updateMenuItem(menuId, formData) {
      const findAndUpdate = (items) => {
        for (let i = 0; i < items.length; i++) {
          if (items[i].id === menuId) {
            items[i].title = formData.title
            items[i].icon = formData.icon
            items[i].markdown = formData.markdown
            return true
          }
          if (items[i].children) {
            if (findAndUpdate(items[i].children)) {
              return true
            }
          }
        }
        return false
      }
      
      findAndUpdate(this.menuItems)
    },
    
    // 添加到父菜单
    addToParentMenu(parentId, newItem) {
      const findParent = (items) => {
        for (let i = 0; i < items.length; i++) {
          if (items[i].id === parentId) {
            if (!items[i].children) {
              this.$set(items[i], 'children', [])
            }
            items[i].children.push(newItem)
            return true
          }
          if (items[i].children) {
            if (findParent(items[i].children)) {
              return true
            }
          }
        }
        return false
      }
      
      findParent(this.menuItems)
    },
    
    // 重置对话框表单
    resetDialogForm() {
      this.$refs.menuFormRef.resetFields()
    },
    
    // 编辑内容
    handleEditContent() {
      this.isEditingContent = true
      this.editContent = {
        title: this.currentContent.title,
        markdown: this.currentContent.markdown
      }
    },
    
    // 保存内容
    saveContent() {
      this.currentContent.title = this.editContent.title
      this.currentContent.markdown = this.editContent.markdown
      
      // 更新对应的菜单项
      if (this.activeMenu) {
        this.updateMenuContent(this.activeMenu, this.editContent)
      }
      
      this.isEditingContent = false
      this.$message({
        message: '内容已保存',
        type: 'success'
      })
    },
    
    // 取消编辑
    cancelEdit() {
      this.isEditingContent = false
    },
    
    // 更新菜单内容
    updateMenuContent(menuId, content) {
      const findAndUpdate = (items) => {
        for (let i = 0; i < items.length; i++) {
          if (items[i].id === menuId) {
            items[i].title = content.title
            items[i].markdown = content.markdown
            return true
          }
          if (items[i].children) {
            if (findAndUpdate(items[i].children)) {
              return true
            }
          }
        }
        return false
      }
      
      findAndUpdate(this.menuItems)
    },
    
    // 导出内容
    exportContent() {
      const content = this.currentContent.markdown
      const blob = new Blob([content], { type: 'text/markdown' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `${this.currentContent.title}.md`
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
      URL.revokeObjectURL(url)
    },
    
    // 打印内容
    printContent() {
      window.print()
    }
  },
  mounted() {
    // 默认加载第一个菜单
    if (this.menuItems.length > 0) {
      this.loadContent(this.menuItems[0])
      this.activeMenu = this.menuItems[0].id
    }
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  color: #333;
}

/* 侧边栏样式 */
.sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
  transition: width 0.3s ease;
  position: relative;
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

.el-menu {
  border: none;
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

.edit-icon:hover {
  color: #1890ff;
}

.add-menu-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background-color: #f5f5f5;
  border-top: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  color: #666;
  transition: all 0.3s;
}

.add-menu-bottom:hover {
  background-color: #e8f4ff;
  color: #1890ff;
}

/* 主内容区样式 */
.main-header {
  background: white;
  border-bottom: 1px solid #e0e0e0;
  padding: 20px 40px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-icon {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  margin-top: 8px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb-text {
  color: #666;
  cursor: pointer;
  font-size: 14px;
  transition: color 0.3s;
}

.breadcrumb-text:hover {
  color: #1890ff;
}

.header-content {
  flex: 1;
  text-align: center;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.rainbow-icon {
  margin-right: 12px;
  font-size: 32px;
}

.header-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 14px;
  color: #666;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.refresh-icon,
.comment-icon {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  margin-top: 8px;
  transition: color 0.3s;
}

.refresh-icon:hover,
.comment-icon:hover {
  color: #1890ff;
}

.main-content {
  background: white;
  padding: 30px 40px;
  overflow-y: auto;
}

/* Markdown容器样式 */
.markdown-container {
  margin-top: 20px;
}

.markdown-toolbar {
  display: flex;
  justify-content: flex-end;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.markdown-toolbar .el-button {
  padding: 8px 16px;
}

.content-editor {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.edit-title {
  margin-bottom: 20px;
}

.editor-actions {
  margin-top: 20px;
  text-align: right;
}

.markdown-preview {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

/* 默认内容样式 */
.content-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.target-icon,
.pen-icon,
.emoji-icon {
  margin-right: 10px;
  font-size: 28px;
}

.highlight-box {
  background: linear-gradient(135deg, #fff9e6 0%, #ffefd5 100%);
  border-left: 4px solid #ffa726;
  padding: 24px 28px;
  border-radius: 6px;
  margin-top: 16px;
}

.highlight-text {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 8px;
}

.highlight-text-main {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  font-weight: 500;
}

.bulb-icon {
  margin-right: 8px;
  font-size: 20px;
}

.link-text {
  color: #1890ff;
  font-weight: 500;
}

.intro-container {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  margin-top: 20px;
}

.intro-text {
  flex: 1;
}

.intro-text p {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 16px;
}

.link-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  flex-wrap: wrap;
}

.link-buttons .el-button {
  font-size: 14px;
  padding: 8px 0;
}

.qr-code {
  text-align: center;
  min-width: 140px;
}

.qr-code img {
  width: 120px;
  height: 120px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-bottom: 8px;
}

.qr-text {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  margin-bottom: 4px;
}

.qr-links {
  font-size: 12px;
  color: #1890ff;
}

/* 对话框样式 */
.markdown-tips {
  color: #999;
  margin-top: 5px;
}

/* 滚动条样式 */
.sidebar::-webkit-scrollbar,
.main-content::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-thumb,
.main-content::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover,
.main-content::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* Element UI 自定义样式 */
.el-submenu__title:hover,
.el-menu-item:hover {
  background-color: #e8f4ff !important;
}

.el-menu-item.is-active {
  background-color: #e8f4ff !important;
  color: #1890ff !important;
}

.el-menu-item.is-active i {
  color: #1890ff !important;
}
</style>

<style>
/* 全局样式 */
@media print {
  .sidebar,
  .main-header,
  .markdown-toolbar {
    display: none !important;
  }
  
  .main-content {
    padding: 0 !important;
  }
  
  .markdown-preview {
    box-shadow: none !important;
  }
}
</style>