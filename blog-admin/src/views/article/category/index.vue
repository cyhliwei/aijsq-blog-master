<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入分类名称"
            clearable
            suffix-icon="Search"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="URL名称" prop="slug">
          <el-input
            v-model="queryParams.slug"
            placeholder="请输入URL友好名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="分类类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择分类类型" clearable>
            <el-option label="全部" value="" />
            <el-option label="文章" :value="0" />
            <el-option label="教程" :value="1" />
            <el-option label="工具" :value="2" />
            <el-option label="学习路径" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isActive">
          <el-select v-model="queryParams.isActive" placeholder="请选择状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
              <el-button
                v-permission="['sys:category:add']"
                type="primary"
                icon="Plus"
                @click="handleAdd"
              >新增分类</el-button>
              <el-button
                v-permission="['sys:category:delete']"
                type="danger"
                icon="Delete"
                :disabled="selectedIds.length === 0"
                @click="handleBatchDelete"
              >批量删除</el-button>
            </div>
            <div class="text-sm text-gray-600">
              共 {{ total }} 条记录
            </div>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="分类名称" align="center" prop="name" show-overflow-tooltip>
          <template #default="scope">
            <div class="flex items-center">
              <el-icon v-if="scope.row.icon" class="mr-2" :class="scope.row.icon" />
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="URL名称" align="center" prop="slug" show-overflow-tooltip />
        <el-table-column label="分类类型" align="center" prop="type">
          <template #default="scope">
            <el-tag :type="getCategoryTypeTag(scope.row.type)" size="small">
              {{ getCategoryTypeLabel(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" align="center" prop="sort" width="80" />
        <el-table-column label="状态" align="center" prop="isActive" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.isActive ? 'success' : 'danger'" size="small">
              {{ scope.row.isActive ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="导航显示" align="center" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.navEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleNavEnabledChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="导航排序" align="center" prop="navOrder" width="80" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="更新时间" align="center" prop="updateTime" width="180" />
        <el-table-column label="操作" align="center" width="280" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="['sys:category:update']"
              type="primary"
              link
              icon="Edit"
              @click="handleUpdate(scope.row)"
            >编辑</el-button>
            <el-button
              v-permission="['sys:category:delete']"
              type="danger"
              link
              icon="Delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-dropdown>
              <el-button type="info" link icon="More" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleToggleStatus(scope.row)">
                    {{ scope.row.isActive ? '禁用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleCopySlug(scope.row)">
                    复制URL名称
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
      width="800px"
      append-to-body
      destroy-on-close
      class="custom-dialog"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="custom-form"
      >
        <el-tabs v-model="activeTab">
          <!-- 基础信息 -->
          <el-tab-pane label="基础信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分类名称" prop="name">
                  <el-input 
                    v-model="form.name" 
                    placeholder="请输入分类名称"
                    clearable
                    @input="generateSlug"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="URL名称" prop="slug">
                  <el-input 
                    v-model="form.slug" 
                    placeholder="URL友好名称，字母、数字、横线"
                    clearable
                  />
                  <el-button 
                    type="text" 
                    @click="generateSlug"
                    class="ml-2"
                    icon="Refresh"
                  >
                    自动生成
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="分类描述" prop="description">
              <el-input 
                v-model="form.description" 
                type="textarea"
                :rows="3"
                placeholder="请输入分类描述"
                clearable
                show-word-limit
                maxlength="500"
              />
            </el-form-item>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分类类型" prop="type">
                  <el-select 
                    v-model="form.type" 
                    placeholder="请选择分类类型"
                    style="width: 100%"
                  >
                    <el-option label="文章" :value="0" />
                    <el-option label="教程" :value="1" />
                    <el-option label="工具" :value="2" />
                    <el-option label="学习路径" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="排序" prop="sort">
                  <el-input-number
                    v-model="form.sort"
                    :min="0"
                    :max="999"
                    placeholder="请输入排序"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 显示设置 -->
          <el-tab-pane label="显示设置" name="display">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="图标类名" prop="icon">
                  <el-input 
                    v-model="form.icon" 
                    placeholder="请输入图标类名，如：el-icon-menu"
                    clearable
                  />
                  <div class="text-xs text-gray-500 mt-1">
                    支持 Element Plus 图标类名
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="背景颜色" prop="bgColor">
                  <el-color-picker
                    v-model="form.bgColor"
                    show-alpha
                    :predefine="predefinedColors"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="状态" prop="isActive">
              <el-radio-group v-model="form.isActive">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-tab-pane>

          <!-- 导航设置 -->
          <el-tab-pane label="导航设置" name="navigation">
            <el-form-item label="导航显示" prop="navEnabled">
              <el-switch
                v-model="form.navEnabled"
                :active-value="1"
                :inactive-value="0"
              />
            </el-form-item>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="导航排序" prop="navOrder">
                  <el-input-number
                    v-model="form.navOrder"
                    :min="0"
                    :max="999"
                    placeholder="导航显示顺序"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="导航颜色" prop="navColor">
                  <el-color-picker
                    v-model="form.navColor"
                    show-alpha
                    :predefine="predefinedColors"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="导航图标" prop="navIcon">
              <el-input 
                v-model="form.navIcon" 
                placeholder="请输入导航图标类名"
                clearable
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getCategoryListApi,
  addCategoryApi,
  updateCategoryApi,
  deleteCategoryApi
} from '@/api/article/category'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  slug: '',
  type: undefined,
  isActive: undefined
})

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()
const submitLoading = ref(false)
const activeTab = ref('basic')

// 选中项数组
const selectedIds = ref<string[]>([])

// 弹窗控制
const dialog = reactive({
  title: '',
  visible: false,
  type: 'add'
})

// 分类类型映射
const categoryTypeMap = {
  0: { label: '文章', type: 'primary' },
  1: { label: '教程', type: 'success' },
  2: { label: '工具', type: 'warning' },
  3: { label: '学习路径', type: 'info' }
}

// 预定义颜色
const predefinedColors = ref([
  '#409EFF',
  '#67C23A',
  '#E6A23C',
  '#F56C6C',
  '#909399',
  '#409EFF80',
  '#67C23A80',
  '#E6A23C80',
  '#F56C6C80'
])

// 表单数据
const form = reactive({
  id: undefined,
  name: '',
  slug: '',
  description: '',
  icon: '',
  bgColor: '#409EFF',
  type: 0,
  isActive: 1,
  sort: 0,
  navColor: '#409EFF',
  navIcon: '',
  navOrder: 0,
  navEnabled: 1
})

// 表单校验规则
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
  // slug: [
  //   { required: true, message: '请输入URL友好名称', trigger: 'blur' },
  //   { 
  //     pattern: /^[a-z0-9]+(?:-[a-z0-9]+)*$/,
  //     message: '只能包含小写字母、数字和横线',
  //     trigger: 'blur'
  //   }
  // ],
  type: [
    { required: true, message: '请选择分类类型', trigger: 'change' }
  ],
  sort: [
    { required: true, message: '请输入排序值', trigger: 'blur' }
  ]
})

// 获取分类类型标签
const getCategoryTypeTag = (type: number) => {
  return categoryTypeMap[type]?.type || 'info'
}

// 获取分类类型标签文本
const getCategoryTypeLabel = (type: number) => {
  return categoryTypeMap[type]?.label || '未知'
}

// 获取分类列表
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getCategoryListApi(queryParams)
    tableData.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 表格选择项变化
const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的分类')
    return
  }
  
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个分类吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    dangerouslyUseHTMLString: true
  }).then(async () => {
    try {
      await deleteCategoryApi(selectedIds.value)
      ElMessage.success('批量删除成功')
      getList()
      selectedIds.value = []
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 删除单个分类
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除分类 "${row.name}" 吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategoryApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 切换分类状态
const handleToggleStatus = async (row: any) => {
  const newStatus = row.isActive ? 0 : 1
  const action = newStatus ? '启用' : '禁用'
  
  ElMessageBox.confirm(`确定要${action}分类 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateCategoryApi({
        id: row.id,
        isActive: newStatus
      })
      ElMessage.success(`${action}成功`)
      getList()
    } catch (error) {
      console.error(`${action}失败:`, error)
      ElMessage.error(`${action}失败`)
    }
  }).catch(() => {})
}

// 切换导航显示状态
const handleNavEnabledChange = async (row: any) => {
  try {
    await updateCategoryApi({
      id: row.id,
      navEnabled: row.navEnabled
    })
    ElMessage.success('导航设置已更新')
  } catch (error) {
    console.error('更新导航设置失败:', error)
    ElMessage.error('更新导航设置失败')
    // 回滚状态
    row.navEnabled = row.navEnabled ? 0 : 1
  }
}

// 复制URL名称
const handleCopySlug = (row: any) => {
  if (row.slug) {
    navigator.clipboard.writeText(row.slug).then(() => {
      ElMessage.success('URL名称已复制到剪贴板')
    }).catch(() => {
      ElMessage.error('复制失败')
    })
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.pageNum = 1
  getList()
}

// 清空表单
const clearForm = () => {
  form.id = undefined
  form.name = ''
  form.slug = ''
  form.description = ''
  form.icon = ''
  form.bgColor = '#409EFF'
  form.type = 0
  form.isActive = 1
  form.sort = 0
  form.navColor = '#409EFF'
  form.navIcon = ''
  form.navOrder = 0
  form.navEnabled = 1
  activeTab.value = 'basic'
}

// 新增分类
const handleAdd = () => {
  dialog.type = 'add'
  dialog.title = '新增分类'
  dialog.visible = true
  clearForm()
}

// 修改分类
const handleUpdate = (row: any) => {
  clearForm()
  dialog.type = 'edit'
  dialog.title = '修改分类'
  dialog.visible = true
  
  // 复制数据
  Object.assign(form, row)
}

// 生成URL友好名称
const generateSlug = () => {
  if (!form.name) return
  
  const slug = form.name
    .toLowerCase()
    .replace(/[^\w\u4e00-\u9fa5]+/g, '-')
    .replace(/^-+|-+$/g, '')
    .replace(/[^a-z0-9-]/g, '')
  
  if (slug) {
    form.slug = slug
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (dialog.type === 'add') {
          await addCategoryApi(form)
          ElMessage.success('新增成功')
        } else {
          await updateCategoryApi(form)
          ElMessage.success('修改成功')
        }
        getList()
        dialog.visible = false
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 取消按钮
const cancel = () => {
  dialog.visible = false
  formRef.value?.resetFields()
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  queryParams.pageNum = 1
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
})
</script>

<style lang="scss" scoped>
.app-container {
  .search-wrapper {
    margin-bottom: 20px;
    
    .el-form-item {
      margin-bottom: 10px;
    }
  }

  .card-header {
    .flex {
      display: flex;
    }
    
    .justify-between {
      justify-content: space-between;
    }
    
    .items-center {
      align-items: center;
    }
    
    .text-sm {
      font-size: 0.875rem;
    }
    
    .text-gray-600 {
      color: #718096;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .mr-2 {
    margin-right: 8px;
  }

  .ml-2 {
    margin-left: 8px;
  }

  .mt-1 {
    margin-top: 4px;
  }

  .text-xs {
    font-size: 0.75rem;
  }

  .text-gray-500 {
    color: #a0aec0;
  }

  :deep(.el-table) {
    .el-table__cell {
      padding: 8px 0;
    }
    
    .el-button--link {
      padding: 0 4px;
    }
  }

  :deep(.custom-dialog) {
    .el-dialog__body {
      padding: 20px 40px;
    }
    
    .el-form-item {
      margin-bottom: 22px;
    }
    
    .el-tabs__content {
      padding: 20px 0;
    }
  }
}
</style>