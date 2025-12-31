<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="标题" prop="title">
          <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="内容类型" prop="contentType">
          <el-select v-model="queryParams.contentType" placeholder="请选择内容类型" clearable>
            <el-option v-for="item in contentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
            <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tagId">
          <el-select v-model="queryParams.tagId" placeholder="请选择标签" clearable>
            <el-option v-for="item in tagOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option v-for="item in statusOptions" :key="item.value" :value="item.value" :label="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度" prop="difficulty" v-if="queryParams.contentType === 'tutorial'">
          <el-select v-model="queryParams.difficulty" placeholder="请选择难度" clearable>
            <el-option v-for="item in difficultyOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="flex justify-between items-center">
            <div>
              <el-button type="primary" icon="Plus" @click="handleAdd" v-permission="['sys:article:add']">新增内容</el-button>
              <el-button type="danger" icon="Delete" :disabled="selectedIds.length === 0"
                v-permission="['sys:article:delete']" @click="handleBatchDelete">批量删除</el-button>
              <el-button type="warning" icon="Setting" v-permission="['sys:article:reptile']"
                @click="reptileDialog.visible = true">爬取文章</el-button>
            </div>
            <div>
              <el-button type="info" plain @click="exportContent">导出数据</el-button>
              <el-dropdown @command="handleBulkAction">
                <el-button type="primary" plain>
                  批量操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="publish">批量发布</el-dropdown-item>
                    <el-dropdown-item command="unpublish">批量下架</el-dropdown-item>
                    <el-dropdown-item command="featured">设为精选</el-dropdown-item>
                    <el-dropdown-item command="unfeatured">取消精选</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="封面" align="center" width="133">
          <template #default="scope">
            <el-image 
              style="width: 120px; height: 80px;border-radius: 10px" 
              :src="scope.row.cover" 
              :preview-src-list="[scope.row.cover]"
              hide-on-click-modal
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="标题" align="center" prop="title" width="200" show-overflow-tooltip>
          <template #default="scope">
            <div class="flex flex-col items-start">
              <span class="text-primary font-medium mb-1">{{ scope.row.title }}</span>
              <div class="flex items-center gap-1">
                <el-tag size="small" :type="scope.row.contentType === 'tutorial' ? 'success' : 'info'">
                  {{ contentTypeMap[scope.row.contentType] }}
                </el-tag>
                <el-tag v-if="scope.row.isFeatured" size="small" type="warning">精选</el-tag>
                <el-tag v-if="scope.row.isHot" size="small" type="danger">热门</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="作者" align="center" prop="nickname" show-overflow-tooltip />
        <el-table-column label="分类" align="center" prop="categoryName" />
        <el-table-column label="难度" align="center" v-if="queryParams.contentType === 'tutorial'">
          <template #default="scope">
            <el-tag 
              :type="difficultyTypeMap[scope.row.difficulty]" 
              size="small"
              v-if="scope.row.difficulty"
            >
              {{ difficultyMap[scope.row.difficulty] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标签" align="center" width="200">
          <template #default="scope">
            <div class="flex flex-wrap gap-1 justify-center">
              <el-tag 
                v-for="tag in scope.row.tags" 
                :key="tag.id" 
                size="small"
                class="cursor-pointer hover:opacity-80"
                @click="handleTagClick(tag)"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="发布状态" align="center" prop="status">
          <template #default="scope">
            <el-switch 
              @change="handleChangeStatus(scope.row)"
              :active-value="1" 
              :inactive-value="0" 
              v-model="scope.row.status"
              active-text="已发布"
              inactive-text="下架"
            />
          </template>
        </el-table-column>
        <el-table-column label="阅读量" align="center" prop="quantity" width="100">
          <template #default="scope">
            <span class="font-medium">{{ scope.row.quantity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="点赞数" align="center" prop="likeCount" width="100">
          <template #default="scope">
            <span class="text-red-500 font-medium">{{ scope.row.likeCount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="View" @click="handleView(scope.row)">预览</el-button>
            <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)"
              v-permission="['sys:article:update']">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)"
              v-permission="['sys:article:delete']">删除</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="info" link icon="More" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="copy" v-permission="['sys:article:add']">复制</el-dropdown-item>
                  <el-dropdown-item command="featured" v-permission="['sys:article:update']">
                    {{ scope.row.isFeatured ? '取消精选' : '设为精选' }}
                  </el-dropdown-item>
                  <el-dropdown-item command="hot" v-permission="['sys:article:update']">
                    {{ scope.row.isHot ? '取消热门' : '设为热门' }}
                  </el-dropdown-item>
                  <el-dropdown-item command="stick" v-permission="['sys:article:update']">
                    {{ scope.row.isStick ? '取消置顶' : '设为置顶' }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination 
          v-model:current-page="queryParams.pageNum" 
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]" 
          :total="total" 
          :background="true"
          layout="total, sizes, prev, pager, next, jumper" 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" 
        />
      </div>
    </el-card>

    <!-- 添加或修改对话框 -->
    <el-dialog 
      :title="dialog.title" 
      v-model="dialog.visible" 
      width="1400px" 
      top="3vh" 
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20" class="mb-20">
          <el-col :span="16">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="内容类型" prop="contentType">
              <el-select v-model="form.contentType" placeholder="请选择内容类型" @change="handleContentTypeChange">
                <el-option v-for="item in contentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="封面" prop="cover">
          <UploadImage v-model="form.cover" :limit="1" :source="'article-cover'" />
        </el-form-item>

        <el-form-item label="简介" prop="summary">
          <el-input 
            v-model="form.summary" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入简介" 
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select 
                v-model="form.categoryId" 
                placeholder="请选择分类" 
                filterable
                clearable
                @change="handleCategoryChange"
              >
                <el-option 
                  v-for="item in filteredCategoryOptions" 
                  :key="item.id" 
                  :label="item.name" 
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.contentType === 'tutorial'">
            <el-form-item label="难度" prop="difficulty">
              <el-select v-model="form.difficulty" placeholder="请选择难度">
                <el-option v-for="item in difficultyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="标签" prop="tagIds">
          <el-select
            v-model="form.tagIds"
            multiple
            filterable
            placeholder="请选择标签"
            style="width: 100%"
          >
            <el-option
              v-for="item in filteredTagOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="8">
            <el-form-item label="阅读时间" prop="readTime">
              <el-input-number 
                v-model="form.readTime" 
                :min="1" 
                :max="120" 
                placeholder="分钟" 
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="文章类型" prop="isOriginal">
              <el-select v-model="form.isOriginal" placeholder="请选择文章类型">
                <el-option label="原创" :value="1" />
                <el-option label="转载" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关键词" prop="keywords">
              <el-input v-model="form.keywords" placeholder="请输入关键词，用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="转载地址" prop="originalUrl" v-if="form.isOriginal === 0" class="mb-20">
          <el-input v-model="form.originalUrl" placeholder="请输入转载地址" />
        </el-form-item>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="6">
            <el-form-item label="是否置顶" prop="isStick">
              <el-switch v-model="form.isStick" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否发布" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="下架" :value="0" />
                <el-option label="发布" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="首页轮播" prop="isCarousel">
              <el-switch v-model="form.isCarousel" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否推荐" prop="isRecommend">
              <el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="mb-20" v-if="form.contentType === 'tutorial'">
          <el-col :span="6">
            <el-form-item label="是否精选" prop="isFeatured">
              <el-switch v-model="form.isFeatured" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否热门" prop="isHot">
              <el-switch v-model="form.isHot" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="URL友好标题" prop="slug">
              <el-input v-model="form.slug" placeholder="用于生成SEO友好的URL" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="AI描述" prop="aiDescribe">
          <el-input 
            v-model="form.aiDescribe" 
            type="textarea" 
            :rows="3" 
            placeholder="AI生成的简短描述（如需要可自动生成）" 
          />
        </el-form-item>

        <el-form-item label="文章内容" prop="contentMd">
          <mavon-editor 
            ref="mdRef" 
            v-model="form.contentMd" 
            placeholder="输入文章内容..." 
            style="height: 500px; width: 100%" 
            @imgDel="imgDel" 
            @imgAdd="imgAdd"
            :toolbars="editorToolbars"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="success" :loading="saveLoading" @click="handleSaveDraft" v-if="dialog.type === 'add'">
            保存草稿
          </el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 爬取文章对话框 -->
    <el-dialog title="爬取文章" v-model="reptileDialog.visible" width="800px">
      <el-form ref="reptileFormRef" :model="reptileForm" :rules="reptileRules" label-width="100px">
        <el-form-item label="爬取地址" prop="url">
          <el-input v-model="reptileForm.url" placeholder="请输入爬取地址" />
        </el-form-item>
        <el-form-item label="内容类型" prop="contentType">
          <el-select v-model="reptileForm.contentType" placeholder="请选择内容类型">
            <el-option label="文章" value="article" />
            <el-option label="教程" value="tutorial" />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px;">
        <el-alert title="暂时只支持CSDN的文章爬取" type="success" :closable="false" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="reptileDialog.visible = false">取 消</el-button>
          <el-button type="primary" :loading="reptileLoading" @click="submitReptile">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 添加视频地址对话框 -->
    <el-dialog center title="添加视频" v-model="dialogVisible" width="30%">
      <el-input v-model="videoInput" placeholder="视频地址"></el-input>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="addVideo">确 定</el-button>
          <el-button @click="dialogVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { ArrowDown, Picture, View, Edit, Delete, More, Search, Refresh, Plus, Setting } from '@element-plus/icons-vue'
import UploadImage from '@/components/Upload/Image.vue'
import { getCategoryListApi } from '@/api/article/category'
import { getTagListApi } from '@/api/article/tag'
// import {
//   getArticleListApi, 
//   getDetailApi, 
//   deleteArticleApi,
//   addArticleApi, 
//   updateArticleApi, 
//   updateStatusApi, 
//   reptileArticleApi,
//   batchUpdateArticleApi,
//   copyArticleApi
// } from '@/api/article'
import {
  getArticleListApi, 
  getDetailApi, 
  deleteArticleApi,
  addArticleApi, 
  updateArticleApi, 
  updateStatusApi, 
  reptileArticleApi
} from '@/api/article'
import { uploadApi, deleteFileApi } from '@/api/file'

// 常量定义
const contentTypeOptions = [
  { label: '全部', value: '' },
  { label: '文章', value: 'article' },
  { label: '教程', value: 'tutorial' }
]

const contentTypeMap = {
  article: '文章',
  tutorial: '教程'
}

const difficultyOptions = [
  { label: '初级', value: 'beginner' },
  { label: '中级', value: 'intermediate' },
  { label: '高级', value: 'advanced' }
]

const difficultyMap = {
  beginner: '初级',
  intermediate: '中级',
  advanced: '高级'
}

const difficultyTypeMap = {
  beginner: 'success',
  intermediate: 'warning',
  advanced: 'danger'
}

// 编辑器工具栏配置
const editorToolbars = {
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
  alignleft: true,
  aligncenter: true,
  alignright: true,
  subfield: true,
  preview: true
}

// 响应式数据
const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const selectedIds = ref<number[]>([])
const categoryOptions = ref<any[]>([])
const tagOptions = ref<any[]>([])
const statusOptions = ref<any[]>([])

const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()
const reptileFormRef = ref<FormInstance>()
const mdRef = ref()

const submitLoading = ref(false)
const saveLoading = ref(false)
const reptileLoading = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  categoryId: undefined as number | undefined,
  tagId: undefined as number | undefined,
  status: undefined as number | undefined,
  contentType: '' as string,
  difficulty: '' as string
})

// 对话框
const dialog = reactive({
  title: '',
  visible: false,
  type: 'add' // add | edit
})

const reptileDialog = reactive({
  visible: false
})

// 表单数据
const form = reactive({
  id: undefined as number | undefined,
  title: '',
  cover: '',
  summary: '',
  content: '',
  contentMd: '',
  categoryId: undefined as number | undefined,
  categoryName: '',
  tagIds: [] as number[],
  tags: [] as any[],
  contentType: 'article' as string,
  difficulty: 'beginner' as string,
  readTime: 5,
  isOriginal: 1 as number,
  originalUrl: '',
  readType: 1 as number,
  isStick: 0 as number,
  status: 1 as number,
  isCarousel: 0 as number,
  isRecommend: 0 as number,
  isFeatured: 0 as number,
  isHot: 0 as number,
  slug: '',
  keywords: '',
  aiDescribe: ''
})

const reptileForm = reactive({
  url: '',
  contentType: 'article'
})

// 计算属性
const filteredCategoryOptions = computed(() => {
  if (form.contentType === 'tutorial') {
    return categoryOptions.value.filter(item => item.type === 'tutorial')
  }
  return categoryOptions.value.filter(item => item.type === 'article')
})

const filteredTagOptions = computed(() => {
  return tagOptions.value
})

// 表单验证规则
const rules = reactive<FormRules>({
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  contentType: [
    { required: true, message: '请选择内容类型', trigger: 'change' }
  ],
  contentMd: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ],
  summary: [
    { required: true, message: '请输入简介', trigger: 'blur' },
    { max: 500, message: '简介最多500个字符', trigger: 'blur' }
  ],
  slug: [
    { 
      required: form.contentType === 'tutorial', 
      message: 'URL友好标题不能为空', 
      trigger: 'blur' 
    }
  ]
})

const reptileRules = reactive<FormRules>({
  url: [
    { required: true, message: '请输入爬取地址', trigger: 'blur' }
  ]
})

// 方法
const handleContentTypeChange = (value: string) => {
  // 根据内容类型调整分类选项
  if (value === 'article') {
    form.difficulty = 'beginner'
  }
}

const handleCategoryChange = (value: number) => {
  const category = categoryOptions.value.find(item => item.id === value)
  if (category) {
    form.categoryName = category.name
  }
}

const handleTagClick = (tag: any) => {
  queryParams.tagId = tag.id
  handleQuery()
}

const getList = async () => {
  loading.value = true
  try {
    const { data } = await getArticleListApi(queryParams)
    tableData.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getCategoryList = async () => {
  try {
    const { data } = await getCategoryListApi({ pageNum: 1, pageSize: 1000 })
    categoryOptions.value = data.records
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const getTagList = async () => {
  try {
    const { data } = await getTagListApi({ pageNum: 1, pageSize: 1000 })
    tagOptions.value = data.records
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.contentType = ''
  queryParams.difficulty = ''
  handleQuery()
}

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

const clearForm = () => {
  Object.assign(form, {
    id: undefined,
    title: '',
    cover: '',
    summary: '',
    content: '',
    contentMd: '',
    categoryId: undefined,
    categoryName: '',
    tagIds: [],
    tags: [],
    contentType: 'article',
    difficulty: 'beginner',
    readTime: 5,
    isOriginal: 1,
    originalUrl: '',
    readType: 1,
    isStick: 0,
    status: 1,
    isCarousel: 0,
    isRecommend: 0,
    isFeatured: 0,
    isHot: 0,
    slug: '',
    keywords: '',
    aiDescribe: ''
  })
}

const handleAdd = () => {
  dialog.type = 'add'
  dialog.title = '新增内容'
  dialog.visible = true
  clearForm()
}

const handleUpdate = async (row: any) => {
  dialog.type = 'edit'
  dialog.title = '修改内容'
  dialog.visible = true
  
  try {
    const { data } = await getDetailApi(row.id)
    Object.assign(form, data)
    // 转换标签数组为ID数组
    if (data.tags && Array.isArray(data.tags)) {
      form.tagIds = data.tags.map(tag => tag.id)
    }
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

const handleView = (row: any) => {
  // 打开新窗口预览
  const routeData = router.resolve({
    path: `/article/${row.id}`,
    query: { preview: 'true' }
  })
  window.open(routeData.href, '_blank')
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定删除"${row.title}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteArticleApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的内容')
    return
  }
  
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条内容吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteArticleApi(selectedIds.value)
      ElMessage.success('删除成功')
      selectedIds.value = []
      getList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

const handleChangeStatus = async (row: any) => {
  try {
    await updateStatusApi({ id: row.id, status: row.status })
    ElMessage.success('状态更新成功')
    getList()
  } catch (error) {
    console.error('状态更新失败:', error)
    // 回滚状态
    row.status = row.status === 1 ? 0 : 1
  }
}

const handleMoreAction = async (command: string, row: any) => {
  switch (command) {
    case 'copy':
      await copyContent(row.id)
      break
    case 'featured':
      await toggleFeatured(row)
      break
    case 'hot':
      await toggleHot(row)
      break
    case 'stick':
      await toggleStick(row)
      break
  }
}

const handleBulkAction = async (command: string) => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要操作的内容')
    return
  }
  
  const params = {
    ids: selectedIds.value,
    status: 0,
    isFeatured: 0,
    isHot: 0,
    isStick: 0
  }
  
  switch (command) {
    case 'publish':
      params.status = 1
      break
    case 'unpublish':
      params.status = 0
      break
    case 'featured':
      params.isFeatured = 1
      break
    case 'unfeatured':
      params.isFeatured = 0
      break
  }
  
  try {
    await batchUpdateArticleApi(params)
    ElMessage.success('操作成功')
    selectedIds.value = []
    getList()
  } catch (error) {
    console.error('批量操作失败:', error)
  }
}

const copyContent = async (id: number) => {
  try {
    await copyArticleApi(id)
    ElMessage.success('复制成功')
    getList()
  } catch (error) {
    console.error('复制失败:', error)
  }
}

const toggleFeatured = async (row: any) => {
  try {
    await updateArticleApi({
      id: row.id,
      isFeatured: row.isFeatured ? 0 : 1
    })
    row.isFeatured = row.isFeatured ? 0 : 1
    ElMessage.success('操作成功')
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const toggleHot = async (row: any) => {
  try {
    await updateArticleApi({
      id: row.id,
      isHot: row.isHot ? 0 : 1
    })
    row.isHot = row.isHot ? 0 : 1
    ElMessage.success('操作成功')
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const toggleStick = async (row: any) => {
  try {
    await updateArticleApi({
      id: row.id,
      isStick: row.isStick ? 0 : 1
    })
    row.isStick = row.isStick ? 0 : 1
    ElMessage.success('操作成功')
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      // 设置内容
      form.content = mdRef.value.d_render
      
      // 设置发布时间
      if (form.status === 1 && !form.id) {
        form.publishedAt = new Date().toISOString()
      }
      
      // 生成slug（如果未提供且为教程）
      if (form.contentType === 'tutorial' && !form.slug && form.title) {
        form.slug = generateSlug(form.title)
      }
      
      if (dialog.type === 'add') {
        await addArticleApi(form)
        ElMessage.success('新增成功')
      } else {
        await updateArticleApi(form)
        ElMessage.success('修改成功')
      }
      
      dialog.visible = false
      getList()
    } catch (error) {
      console.error('提交失败:', error)
    } finally {
      submitLoading.value = false
    }
  })
}

const handleSaveDraft = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saveLoading.value = true
    try {
      form.content = mdRef.value.d_render
      form.status = 0 // 草稿状态
      
      if (form.contentType === 'tutorial' && !form.slug && form.title) {
        form.slug = generateSlug(form.title)
      }
      
      await addArticleApi(form)
      ElMessage.success('保存草稿成功')
      dialog.visible = false
      getList()
    } catch (error) {
      console.error('保存草稿失败:', error)
    } finally {
      saveLoading.value = false
    }
  })
}

const handleDialogClosed = () => {
  formRef.value?.resetFields()
  clearForm()
}

const cancel = () => {
  dialog.visible = false
}

const submitReptile = async () => {
  if (!reptileFormRef.value) return
  
  await reptileFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    reptileLoading.value = true
    try {
      await reptileArticleApi({
        url: reptileForm.url,
        contentType: reptileForm.contentType
      })
      ElMessage.success('爬取成功')
      reptileDialog.visible = false
      reptileForm.url = ''
      reptileForm.contentType = 'article'
      getList()
    } catch (error) {
      console.error('爬取失败:', error)
    } finally {
      reptileLoading.value = false
    }
  })
}

const exportContent = () => {
  // 导出功能实现
  ElMessage.info('导出功能开发中...')
}

const imgDel = async (pos: string) => {
  try {
    await deleteFileApi(pos)
    ElMessage.success('删除成功')
  } catch (error) {
    console.error('删除图片失败:', error)
  }
}

const imgAdd = (pos: any, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  
  uploadApi(formData, 'article-content').then((res) => {
    mdRef.value.$img2Url(pos, res.data)
  }).catch((error) => {
    console.error('上传图片失败:', error)
  })
}

const generateSlug = (title: string) => {
  return title
    .toLowerCase()
    .replace(/[^\w\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .trim()
}

// 初始化
onMounted(() => {
  getList()
  getCategoryList()
  getTagList()
  
  // 初始化状态选项
  statusOptions.value = [
    { label: '全部', value: undefined },
    { label: '已发布', value: 1 },
    { label: '下架', value: 0 }
  ]
})
</script>

<style lang="scss" scoped>
.app-container {
  .search-wrapper {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .el-form {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }
  
  .box-card {
    border-radius: 8px;
    overflow: hidden;
    
    :deep(.el-card__header) {
      background-color: #f8f9fa;
      border-bottom: 1px solid #e4e7ed;
      padding: 16px 20px;
    }
    
    .card-header {
      .flex {
        display: flex;
        align-items: center;
      }
      
      .justify-between {
        justify-content: space-between;
      }
    }
  }
  
  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    padding: 20px 0;
  }
  
  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: #f5f7fa;
    color: #909399;
  }
  
  :deep(.el-dialog) {
    .el-dialog__body {
      padding: 20px;
      max-height: 70vh;
      overflow-y: auto;
    }
    
    .mb-20 {
      margin-bottom: 20px;
    }
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }
  
  .text-primary {
    color: #409eff;
  }
  
  .font-medium {
    font-weight: 500;
  }
}
</style>