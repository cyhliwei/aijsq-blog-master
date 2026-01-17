<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="标题" prop="title">
          <el-input v-model="queryParams.title" placeholder="请输入文章标题" clearable @keyup.enter="handleQuery" />
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
            <el-option v-for="item in statusOptions" :key="item.id" :value="item.value" :label="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容类型" prop="contentType">
          <el-select v-model="queryParams.contentType" placeholder="请选择类型" clearable>
            <el-option label="全部" value="" />
            <el-option label="文章" value="0" />
            <el-option label="教程" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度级别" prop="difficulty" v-if="queryParams.contentType === '1'">
          <el-select v-model="queryParams.difficulty" placeholder="请选择难度" clearable>
            <el-option label="初级" value="0" />
            <el-option label="中级" value="1" />
            <el-option label="高级" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          <el-button type="warning" icon="MagicStick" @click="showAIOptions = !showAIOptions">
            AI操作
          </el-button>
        </el-form-item>
      </el-form>

      <!-- AI快速操作选项 -->
      <el-collapse-transition>
        <div v-show="showAIOptions" class="ai-options-panel">
          <el-row :gutter="10">
            <el-col :span="8">
              <el-button type="info" size="small" @click="batchSetAsTutorial">
                <el-icon><Promotion /></el-icon>
                批量设为教程
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button type="info" size="small" @click="generateAIDescriptions">
                <el-icon><MagicStick /></el-icon>
                生成AI描述
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button type="info" size="small" @click="analyzeContentTrends">
                <el-icon><TrendCharts /></el-icon>
                分析内容趋势
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-collapse-transition>
    </div>

    <!-- 操作按钮区域 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="flex justify-between">
            <div>
              <el-button type="primary" icon="Plus" @click="handleAdd" v-permission="['sys:article:add']">新增文章</el-button>
              <el-dropdown @command="handleAICreate">
                <el-button type="primary">
                  AI快速创建<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="ai-tutorial">AI教程</el-dropdown-item>
                    <el-dropdown-item command="ai-article">AI文章</el-dropdown-item>
                    <el-dropdown-item command="ai-translate">AI翻译</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div>
              <el-button type="danger" icon="Delete" :disabled="selectedIds.length === 0"
                v-permission="['sys:article:delete']" @click="handleBatchDelete">批量删除</el-button>
              <el-button type="warning" icon="Setting" v-permission="['sys:article:reptile']"
                @click="reptileDialog.visible = true">爬取文章</el-button>
            </div>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="封面" align="center" width="133">
          <template #default="scope">
            <el-image style="width: 120px; height: 80px;border-radius: 10px" :src="scope.row.cover" />
          </template>
        </el-table-column>
        <el-table-column label="标题" align="center" prop="title" width="200" show-overflow-tooltip>
          <template #default="scope">
            <div class="flex flex-col items-start">
              <span style="color: var(--el-color-primary);">{{ scope.row.title }}</span>
              <div v-if="scope.row.contentType" class="mt-1">
                <el-tag 
                  size="mini" 
                  :type="scope.row.contentType === '1' ? 'success' : 'info'"
                >
                  {{ scope.row.contentType === '1' ? '教程' : '文章' }}
                </el-tag>
                <el-tag 
                  v-if="scope.row.difficulty" 
                  size="mini" 
                  :type="getDifficultyType(scope.row.difficulty)"
                  class="ml-1"
                >
                  {{ getDifficultyLabel(scope.row.difficulty) }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="作者" align="center" prop="nickname" show-overflow-tooltip />
        <el-table-column label="分类" align="center" prop="categoryName" />
        <el-table-column label="标签" align="center" width="200">
          <template #default="scope">
            <el-tag v-for="tag in scope.row.tags" :key="tag.id" class="mx-1" size="small">
              {{ tag.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布状态" align="center" prop="status">
          <template #default="scope">
            <el-switch @change="handleChangeStatus(scope.row)"
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" v-model="scope.row.status"
              :active-value="1" :inactive-value="0" />
          </template>
        </el-table-column>
        <el-table-column label="是否推荐" align="center">
          <template #default="{ row }">
            <span v-for="item in yesNoOptions">
              <el-tag :type="item.style" v-if="row.isRecommend === Number(item.value)">
                {{ item.label }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="是否置顶" align="center">
          <template #default="{ row }">
            <span v-for="item in yesNoOptions">
              <el-tag :type="item.style" v-if="row.isStick === Number(item.value)">
                {{ item.label }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="是否精选" align="center" v-if="queryParams.contentType === '1'">
          <template #default="{ row }">
            <el-tag v-if="row.isFeatured" type="warning" size="small">精选</el-tag>
            <el-tag v-else type="info" size="small">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否热门" align="center" v-if="queryParams.contentType === '1'">
          <template #default="{ row }">
            <el-tag v-if="row.isHot" type="danger" size="small">热门</el-tag>
            <el-tag v-else type="info" size="small">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="阅读量" align="center" prop="quantity" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)"
              v-permission="['sys:article:update']">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)"
              v-permission="['sys:article:delete']">删除</el-button>
            <el-dropdown>
              <el-button type="info" link icon="More" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleSetAsTutorial(scope.row)">
                    {{ scope.row.contentType === '1' ? '设为文章' : '设为教程' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleGenerateDescription(scope.row)">
                    生成AI描述
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleSetFeatured(scope.row)" v-if="scope.row.contentType === '1'">
                    {{ scope.row.isFeatured ? '取消精选' : '设为精选' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleSetHot(scope.row)" v-if="scope.row.contentType === '1'">
                    {{ scope.row.isHot ? '取消热门' : '设为热门' }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]" :total="total" :background="true"
          layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="1400px" top="3vh" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="文章标题" prop="title">
          <div class="flex items-center">
            <el-input v-model="form.title" placeholder="请输入文章标题" style="flex: 1" />
            <el-button 
              type="text" 
              @click="generateAITitle" 
              class="ml-2"
              icon="MagicStick"
              v-if="dialog.type === 'add'"
            >
              AI生成标题
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="文章封面" prop="cover">
          <UploadImage v-model="form.cover" :limit="1" :source="'article-cover'" />
        </el-form-item>

        <el-form-item label="文章简介" prop="summary">
          <div class="flex items-start">
            <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入文章简介" style="flex: 1" />
            <el-button 
              type="text" 
              @click="generateAISummary" 
              class="ml-2"
              icon="MagicStick"
            >
              AI生成简介
            </el-button>
          </div>
        </el-form-item>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryName">
              <el-tag
                type="success"
                v-show="form.categoryName"
                style="margin: 0 1rem 0 0"
                :closable="true"
                @close="removeCategory()"
              >
                {{ form.categoryName }}
              </el-tag>
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                v-if="!form.categoryName"
              >
                <div class="popover-title">分类</div>
                <el-input
                  style="width: 100%"
                  v-model="categoryName"
                  placeholder="请输入分类名,enter添加自定义分类"
                  @keyup.enter="saveCategory"
                />
                <div class="popover-container">
                  <div>添加分类</div>
                  <el-tag
                    v-for="(item, index) of categoryOptions"
                    :key="index"
                    style="margin-left: 3px; margin-top: 2px"
                    class="category-item"
                    @click="addCategory(item.name)"
                  >
                    {{ item.name }}
                  </el-tag>
                </div>
                <template #reference>
                  <el-button type="success" plain> 添加分类 </el-button>
                </template>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签" prop="tags">
              <el-tag
                v-for="(item, index) of form.tags"
                :key="index"
                style="margin: 0 1rem 0 0"
                :closable="true"
                @close="removeTag(item)"
              >
                {{ item }}
              </el-tag>
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                v-if="form.tags && form.tags.length < 3"
              >
                <div class="popover-title">标签</div>
                <el-input
                  style="width: 100%"
                  v-model="tagName"
                  placeholder="请输入标签名,enter添加自定义标签"
                  @keyup.enter="saveTag"
                />
                <div class="popover-container">
                  <div>添加标签</div>
                  <el-tag
                    v-for="(item, index) of tagOptions"
                    :key="index"
                    style="margin-left: 3px; margin-top: 2px"
                    @click="addTag(item.name)"
                  >
                    {{ item.name }}
                  </el-tag>
                </div>
                <template #reference>
                  <el-button type="primary" plain> 添加标签 </el-button>
                </template>
              </el-popover>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- AI相关内容类型选择 -->
        <el-row :gutter="20" class="mb-20">
          <el-col :span="8">
            <el-form-item label="内容类型" prop="contentType">
              <el-select v-model="form.contentType" placeholder="请选择内容类型" @change="handleContentTypeChange">
                <el-option label="文章" value="0" />
                <el-option label="教程" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="form.contentType === '1'">
            <el-form-item label="难度级别" prop="difficulty">
              <el-select v-model="form.difficulty" placeholder="请选择难度">
                <el-option label="初级" value="0" />
                <el-option label="中级" value="1" />
                <el-option label="高级" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
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
        </el-row>

        <!-- 教程相关字段 -->
        <el-row :gutter="20" class="mb-20" v-if="form.contentType === '1'">
          <el-col :span="8">
            <el-form-item label="是否精选" prop="isFeatured">
              <el-switch v-model="form.isFeatured" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否热门" prop="isHot">
              <el-switch v-model="form.isHot" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="URL友好标题" prop="slug">
              <el-input v-model="form.slug" placeholder="用于SEO友好的URL" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="8">
            <el-form-item label="阅读方式" prop="readType">
              <el-select v-model="form.readType" placeholder="请选择阅读方式">
                <el-option label="免费" :value="1" />
                <el-option label="会员" :value="2" />
                <el-option label="付费" :value="3" />
              </el-select>
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
              <el-input v-model="form.keywords" placeholder="请输入关键词" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="转载地址" prop="originalUrl" v-if="form.isOriginal === 0" class="mb-20">
          <el-input v-model="form.originalUrl" placeholder="请输入转载地址" />
        </el-form-item>

        <el-row :gutter="20" class="mb-20">
          <el-col :span="6">
            <el-form-item label="是否置顶" prop="isStick">
              <el-switch style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" v-model="form.isStick"
                :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否发布" prop="status">
              <el-select v-model="form.status" placeholder="请选择文章状态">
                <el-option v-for="item in statusOptions" :key="item.id" :value="Number(item.value)"
                  :label="item.label" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="首页轮播" prop="isCarousel">
              <el-switch style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" v-model="form.isCarousel"
                :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否推荐" prop="isRecommend">
              <el-switch style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                v-model="form.isRecommend" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- AI描述 -->
        <el-form-item label="AI描述" prop="aiDescribe" class="mb-20">
          <div class="flex items-start">
            <el-input 
              v-model="form.aiDescribe" 
              type="textarea" 
              :rows="3" 
              placeholder="AI生成的简短描述" 
              style="flex: 1"
            />
            <el-button 
              type="text" 
              @click="generateAIDescription" 
              class="ml-2"
              icon="MagicStick"
            >
              生成AI描述
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="文章内容" prop="contentMd" class="mb-20">
          <mavon-editor placeholder="输入文章内容..." style="height: 500px; width: 100%" ref="mdRef" v-model="form.contentMd"
            @imgDel="imgDel" @imgAdd="imgAdd">
            <template #left-toolbar-after>
              <!-- AI写作助手 -->
              <el-dropdown>
                <span class="el-dropdown-link">
                  <i title="AI写作"></i>
                  <el-icon class="op-icon fa el-icon-magic-stick">
                    <MagicStick />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="generateContentOutline">
                      生成大纲
                    </el-dropdown-item>
                    <el-dropdown-item @click="improveContent">
                      优化内容
                    </el-dropdown-item>
                    <el-dropdown-item @click="checkGrammar">
                      语法检查
                    </el-dropdown-item>
                    <el-dropdown-item @click="translateContent">
                      翻译内容
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-dropdown>
                <span class="el-dropdown-link">
                  <i title="上传视频"></i>
                  <el-icon class="op-icon fa el-icon-video-camera"
                    ><VideoPlay
                  /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item>
                      <el-upload
                        style="display: inline-block"
                        :show-file-list="false"
                        name="filedatas"
                        action=""
                        :http-request="uploadVideo"
                        multiple
                      >
                        <span>上传视频</span>
                      </el-upload>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <div @click="dialogVisible = true">添加视频地址</div>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </mavon-editor>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="warning" @click="saveAsAIDraft" v-if="dialog.type === 'add'">
            保存为AI草稿
          </el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 爬取文章对话框 -->
    <el-dialog title="爬取文章" v-model="reptileDialog.visible" width="800px">
      <el-form ref="reptileFormRef" :model="reptileForm" :rules="rules" label-width="100px">
        <el-form-item label="爬取地址" prop="url">
          <el-input v-model="reptileForm.url" placeholder="请输入爬取地址" />
        </el-form-item>
        <el-form-item label="内容类型" prop="contentType">
          <el-select v-model="reptileForm.contentType" placeholder="请选择内容类型">
            <el-option label="文章" value="0" />
            <el-option label="教程" value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px;">
        <el-alert title="暂时只支持Csdn的文章爬取" type="success" :closable="false" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitReptile">确 定</el-button>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { MagicStick, Promotion, TrendCharts, ArrowDown, VideoPlay } from '@element-plus/icons-vue'
import UploadImage from '@/components/Upload/Image.vue'
import { getCategoryListApi } from '@/api/article/category'
import { getTagListApi } from '@/api/article/tag'
import {
  getArticleListApi, getDetailApi, deleteArticleApi,
  addArticleApi, updateArticleApi, updateStatusApi, reptileArticleApi,
  // batchUpdateContentTypeApi,
  // generateAIDescriptionApi,
  // setFeaturedApi,
  // setHotApi
} from '@/api/article'
import { uploadApi, deleteFileApi } from '@/api/file'
import { getDictDataByDictTypesApi } from '@/api/system/dict'

// 模拟数据
const categoryOptions = ref<any>([])
const tagOptions = ref<any>([])

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  categoryId: undefined,
  tagId: undefined,
  status: undefined,
  contentType: '',
  difficulty: ''
})

// AI操作面板显示
const showAIOptions = ref(false)

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()
const mdRef = ref()
const submitLoading = ref(false)

// 选中项数组
const selectedIds = ref<string[]>([])

// 弹窗控制
const dialog = reactive({
  title: '',
  visible: false,
  type: 'add'
})

const reptileDialog = reactive({
  visible: false,
})

// 表单数据
const form = reactive<any>({
  id: undefined,
  title: '',
  cover: '',
  summary: '',
  categoryName: '',
  tags: [],
  content: '',
  contentMd: '',
  readType: 1,
  isOriginal: 1,
  originalUrl: '',
  isStick: 0,
  status: 1,
  isCarousel: 0,
  isRecommend: 0,
  keywords: '',
  contentType: '0',
  difficulty: '0',
  readTime: 10,
  isFeatured: 0,
  isHot: 0,
  slug: '',
  aiDescribe: ''
})

const reptileForm = reactive({
  url: '',
  contentType: '0'
})

const statusOptions = ref<any>([])
const yesNoOptions = ref<any>([])

const dialogVisible = ref(false)
const videoInput = ref('')

const tagName = ref('')
const categoryName = ref('')

// 难度级别映射
const difficultyMap = {
  '0': '初级',
  '1': '中级',
  '2': '高级'
}

const difficultyTypeMap = {
  '0': 'success',
  '1': 'warning',
  '2': 'danger'
}

// 表单校验规则
const rules = reactive<FormRules>({
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  categoryName: [
    { required: true, message: '请选择文章分类', trigger: 'change' }
  ],
  contentMd: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ],
  summary: [
    { required: true, message: '请输入文章简介', trigger: 'blur' },
    { max: 500, message: '简介最多500个字符', trigger: 'blur' }
  ],
  readType: [
    { required: true, message: '请选择阅读方式', trigger: 'change' }
  ],
  isOriginal: [
    { required: true, message: '请选择文章类型', trigger: 'change' }
  ],
  tags: [
    { required: true, message: '请选择文章标签', trigger: 'change' }
  ],
  originalUrl: [
    {
      required: true,
      message: '请输入转载地址',
      trigger: 'blur',
      validator: (rule: any, value: string, callback: any) => {
        if (form.isOriginal === 0 && !value) {
          callback(new Error('转载文章必须填写转载地址'))
        } else {
          callback()
        }
      }
    }
  ],
  contentType: [
    { required: true, message: '请选择内容类型', trigger: 'change' }
  ],
  difficulty: [
    {
      required: false,
      trigger: 'change',
      validator: (rule: any, value: string, callback: any) => {
        if (form.contentType === '1' && !value) {
          callback(new Error('教程必须选择难度级别'))
        } else {
          callback()
        }
      }
    }
  ]
})

// 处理内容类型变更
const handleContentTypeChange = (value: string) => {
  if (value === 'article') {
    form.difficulty = ''
    form.isFeatured = 0
    form.isHot = 0
    form.slug = ''
  }
}

// 获取难度标签
const getDifficultyLabel = (difficulty: string) => {
  return difficultyMap[difficulty] || difficulty
}

// 获取难度类型
const getDifficultyType = (difficulty: string) => {
  return difficultyTypeMap[difficulty] || 'info'
}

// 删除标签
const removeTag = (tag: string) => {
  form.tags = form.tags.filter((item: string) => item !== tag)
}

const addTag = (tag: any) => {
  if (form.tags.includes(tag)) {
    ElMessage.warning('标签已存在')
    return
  }
  form.tags.push(tag)
}

const saveTag = () => {
  if (tagName.value.trim() !== "") {
    addTag(tagName.value);
    tagName.value = "";
  }
}

const removeCategory = () => {
  form.categoryName = ''
}

const addCategory = (category: any) => {
  if (form.categoryName.includes(category)) {
    ElMessage.warning('分类已存在')
    return
  }
  form.categoryName = category
}

const saveCategory = () => {
  if (categoryName.value.trim() !== "") {
    addCategory(categoryName.value);
    categoryName.value = "";
  }
}

// 删除图片
function imgDel(pos: any, $file: any) {
   deleteFileApi(pos[0]).then((res) => {
     ElMessage.success('删除成功')
   })
}

// 添加图片
function imgAdd(pos: any, $file: any) {
  var formdata = new FormData();
  formdata.append("file", $file);
  uploadApi(formdata, 'article-content').then((res) => {
    mdRef.value.$img2Url(pos, res.data);
  });
}

// 上传视频
const uploadVideo = (param: any) => {
  const loading = ElLoading.service({
    lock: true,
    text: 'Loading',
    background: 'rgba(0, 0, 0, 0.7)',
  })
  var formData = new FormData();
  formData.append("file", param.file);
  return uploadApi(formData, 'article-content').then((res) => {
    const $vm = mdRef.value;
    $vm.insertText($vm.getTextareaDom(), {
      prefix: `<video height=100% width=100% controls autoplay src="${res.data}"></video>`,
      subfix: "",
      str: "",
    });
    return res;
  }).finally(() => {
    loading.close();
  });
}

/**
 * 添加网络视频地址
 */
const addVideo = () => {
  const $vm = mdRef.value;
  $vm.insertText($vm.getTextareaDom(), {
    prefix: `<video height=100% width=100% controls autoplay src="${videoInput.value}"></video>`,
    subfix: "",
    str: "",
  });

  dialogVisible.value = false;
  videoInput.value = "";
}

// 获取列表
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getArticleListApi(queryParams)
    tableData.value = data.records
    total.value = data.total
  } catch (error) {
    console.error(error)
  }
  loading.value = false
}

// 获取状态列表
const getStatusList = async () => {
  const { data } = await getDictDataByDictTypesApi(['article_status', 'sys_yes_no'])
  statusOptions.value = data.article_status.list
  yesNoOptions.value = data.sys_yes_no.list
}

// 表格选择项变化
const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
}

// 爬取文章
const submitReptile = () => {
  if (!reptileForm.url) return
  reptileArticleApi({
    url: reptileForm.url,
    contentType: reptileForm.contentType
  }).then((res) => {
    ElMessage.success('爬取成功')
    getList()
    reptileDialog.visible = false
    reptileForm.url = ''
    reptileForm.contentType = '0'
  })
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return

  ElMessageBox.confirm(`是否确认删除 ${selectedIds.value.length} 篇文章?`, '警告', {
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
      console.error(error)
    }
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除 ${row.title} 这篇文章?`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteArticleApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error(error)
    }
  })
}

// 发布文章
const handleChangeStatus = (row: any) => {
  updateStatusApi({ id: row.id, status: row.status }).then((res) => {
    ElMessage.success('修改成功')
    getList()
  })
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.contentType = ''
  queryParams.difficulty = ''
  handleQuery()
}

// 清空表单
const clearForm = () => {
  form.id = undefined
  form.title = ''
  form.cover = undefined
  form.summary = ''
  form.categoryName = ''
  form.tags = []
  form.content = ''
  form.contentMd = ''
  form.originalUrl = ''
  form.isStick = 0
  form.status = 1
  form.isCarousel = 0
  form.isRecommend = 0
  form.keywords = ''
  form.contentType = '0'
  form.difficulty = '0'
  form.readTime = 10
  form.isFeatured = 0
  form.isHot = 0
  form.slug = ''
  form.aiDescribe = ''
}

// 新增
const handleAdd = () => {
  dialog.type = 'add'
  dialog.title = '新增文章'
  dialog.visible = true
  clearForm()
}

// 修改
const handleUpdate = (row: any) => {
  clearForm()
  dialog.type = 'edit'
  dialog.title = '修改文章'
  dialog.visible = true
  getDetailApi(row.id).then((res) => {
    Object.assign(form, res.data)
  })
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      form.content = mdRef.value.d_render;
      if (form.contentType === '1' && !form.slug && form.title) {
        form.slug = generateSlug(form.title)
      }
      try {
        if (dialog.type === 'add') {
          await addArticleApi(form)
          ElMessage.success('新增成功')
        } else {
          await updateArticleApi(form)
          ElMessage.success('修改成功')
        }
        getList()
        dialog.visible = false
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 取消按钮
const cancel = () => {
  dialog.visible = false
  reptileDialog.visible = false
  formRef.value?.resetFields()
  reptileForm.url = ''
  reptileForm.contentType = '0'
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

// 初始化
onMounted(() => {
  getList()
  getCategoryListApi({ pageNum: 1, pageSize: 1000 }).then((res) => {
    categoryOptions.value = res.data.records
  })
  getTagListApi({ pageNum: 1, pageSize: 1000 }).then((res) => {
    tagOptions.value = res.data.records
  })

  getStatusList()
})

// ============ AI相关功能 ============

// 1. AI快速创建
const handleAICreate = (command: string) => {
  switch (command) {
    case 'ai-tutorial':
      handleAdd()
      form.contentType = '1'
      form.title = 'AI教程：'
      break
    case 'ai-article':
      handleAdd()
      form.contentType = '0'
      form.title = 'AI文章：'
      break
    case 'ai-translate':
      ElMessage.info('AI翻译功能开发中...')
      break
  }
}

// 2. 批量设为教程
const batchSetAsTutorial = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要操作的文章')
    return
  }
  
  ElMessageBox.confirm(`确定将选中的 ${selectedIds.value.length} 篇文章设为教程吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchUpdateContentTypeApi({
        ids: selectedIds.value,
        contentType: '1',
        difficulty: '0'
      })
      ElMessage.success('操作成功')
      getList()
    } catch (error) {
      console.error(error)
    }
  })
}

// 3. 生成AI描述
const generateAIDescriptions = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要操作的文章')
    return
  }
  
  try {
    await generateAIDescriptionApi(selectedIds.value)
    ElMessage.success('AI描述生成成功')
    getList()
  } catch (error) {
    console.error(error)
  }
}

// 4. 分析内容趋势
const analyzeContentTrends = () => {
  ElMessage.info('内容趋势分析功能开发中...')
}

// 5. 设为教程/文章
const handleSetAsTutorial = async (row: any) => {
  const newType = row.contentType === '1' ? '0' : '1'
  const action = newType === '1' ? '设为教程' : '设为文章'
  
  ElMessageBox.confirm(`确定将"${row.title}"${action}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateArticleApi({
        id: row.id,
        contentType: newType,
        difficulty: newType === '1' ? '0' : ''
      })
      ElMessage.success('操作成功')
      getList()
    } catch (error) {
      console.error(error)
    }
  })
}

// 6. 生成AI描述（单条）
const handleGenerateDescription = async (row: any) => {
  try {
    const res = await generateAIDescriptionApi([row.id])
    if (res.data && res.data.description) {
      row.aiDescribe = res.data.description
      await updateArticleApi({
        id: row.id,
        aiDescribe: res.data.description
      })
      ElMessage.success('AI描述生成成功')
    }
  } catch (error) {
    console.error(error)
  }
}

// 7. 设为精选
const handleSetFeatured = async (row: any) => {
  const newValue = row.isFeatured ? 0 : 1
  try {
    await setFeaturedApi({
      id: row.id,
      isFeatured: newValue
    })
    row.isFeatured = newValue
    ElMessage.success('操作成功')
  } catch (error) {
    console.error(error)
  }
}

// 8. 设为热门
const handleSetHot = async (row: any) => {
  const newValue = row.isHot ? 0 : 1
  try {
    await setHotApi({
      id: row.id,
      isHot: newValue
    })
    row.isHot = newValue
    ElMessage.success('操作成功')
  } catch (error) {
    console.error(error)
  }
}

// 9. 生成AI标题
const generateAITitle = async () => {
  if (!form.summary && !form.contentMd) {
    ElMessage.warning('请先填写简介或内容')
    return
  }
  
  // 模拟AI生成标题
  const keywords = form.keywords ? form.keywords.split(',') : []
  const baseTitle = form.contentType === '1' ? 'AI教程' : 'AI文章'
  const newTitle = `${baseTitle}：${keywords[0] || '人工智能'}的${form.contentType === '1' ? '完整指南' : '深度解析'}`
  form.title = newTitle
  ElMessage.success('AI标题生成成功')
}

// 10. 生成AI简介
const generateAISummary = async () => {
  if (!form.title) {
    ElMessage.warning('请先填写标题')
    return
  }
  
  // 模拟AI生成简介
  const baseSummary = `本文详细介绍了${form.title}的相关知识，涵盖了从基础概念到实践应用的完整内容。通过学习本文，您将能够掌握${form.title}的核心技术和应用方法。`
  form.summary = baseSummary
  ElMessage.success('AI简介生成成功')
}

// 11. 生成AI描述（表单）
const generateAIDescription = async () => {
  if (!form.title && !form.summary) {
    ElMessage.warning('请先填写标题或简介')
    return
  }
  
  // 模拟AI生成描述
  const aiDescription = `${form.title || '本文'}是一篇关于人工智能技术的${form.contentType === '1' ? '详细教程' : '深度文章'}，适合${form.difficulty === '0' ? '初学者' : form.difficulty === '1' ? '中级学习者' : '高级专业人士'}阅读。`
  form.aiDescribe = aiDescription
  ElMessage.success('AI描述生成成功')
}

// 12. 生成内容大纲
const generateContentOutline = async () => {
  if (!form.title) {
    ElMessage.warning('请先填写标题')
    return
  }
  
  const outline = `# ${form.title}

## 一、概述
- 背景介绍
- 主要目标

## 二、核心概念
- 基本定义
- 关键特性

## 三、实践应用
- 使用场景
- 操作步骤

## 四、总结
- 核心要点
- 未来展望`
  
  form.contentMd = outline
  ElMessage.success('内容大纲生成成功')
}

// 13. 优化内容
const improveContent = async () => {
  if (!form.contentMd) {
    ElMessage.warning('请先填写内容')
    return
  }
  
  // 简单的内容优化
  const improvedContent = form.contentMd
    .replace(/。/g, '。\n')
    .replace(/，/g, '，\n')
  
  form.contentMd = improvedContent
  ElMessage.success('内容优化成功')
}

// 14. 语法检查
const checkGrammar = () => {
  ElMessage.info('语法检查功能开发中...')
}

// 15. 翻译内容
const translateContent = () => {
  ElMessage.info('内容翻译功能开发中...')
}

// 16. 保存为AI草稿
const saveAsAIDraft = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      form.content = mdRef.value.d_render
      form.status = 0 // 草稿状态
      
      try {
        await addArticleApi(form)
        ElMessage.success('保存为AI草稿成功')
        getList()
        dialog.visible = false
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 辅助函数：生成URL友好的slug
const generateSlug = (title: string) => {
  return title
    .toLowerCase()
    .replace(/[^\w\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .trim()
}
</script>

<style lang="scss" scoped>
.app-container {
  .search-wrapper {
    .ai-options-panel {
      margin-top: 10px;
      padding: 15px;
      background-color: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #e4e7ed;
    }
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

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .mb-20 {
    margin-bottom: 20px;
  }

  .mt-1 {
    margin-top: 4px;
  }

  .ml-1 {
    margin-left: 4px;
  }

  .ml-2 {
    margin-left: 8px;
  }

  .op-icon {
    cursor: pointer;
    font-size: 16px;
    margin-left: 5px;
    
    &:hover {
      color: var(--el-color-primary);
    }
  }

  .flex {
    display: flex;
  }
  
  .flex-col {
    flex-direction: column;
  }
  
  .items-start {
    align-items: flex-start;
  }

  :deep(.el-dropdown-link) {
    cursor: pointer;
    display: flex;
    align-items: center;
  }

  :deep(.el-dialog) {
    .el-dialog__body {
      padding: 20px 40px;
    }
  }
}
</style>