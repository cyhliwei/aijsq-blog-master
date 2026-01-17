<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="标签名称" prop="name">
          <el-input
            v-model.trim="queryParams.name"
            placeholder="请输入标签名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="标签类型" prop="tagType">
          <el-select
            v-model="queryParams.tagType"
            placeholder="请选择标签类型"
            clearable
            style="width: 140px"
          >
            <el-option
              v-for="item in tagTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="热门标签" prop="isHot">
          <el-select
            v-model="queryParams.isHot"
            placeholder="是否热门标签"
            clearable
            style="width: 140px"
          >
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          <el-button
            type="warning"
            icon="Download"
            @click="handleExport"
            v-permission="['sys:tag:export']"
          >导出</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="flex justify-between items-center">
            <div class="flex items-center">
              <span class="font-bold text-lg">标签列表</span>
              <el-tooltip content="支持拖拽排序" placement="top">
                <el-icon class="ml-2 cursor-help"><InfoFilled /></el-icon>
              </el-tooltip>
            </div>
            <ButtonGroup>
              <el-button
                v-permission="['sys:tag:add']"
                type="primary"
                icon="Plus"
                @click="handleAdd"
              >新增</el-button>
              <el-button
                v-permission="['sys:tag:edit']"
                type="warning"
                icon="Sort"
                :disabled="sortable"
                @click="toggleSortable"
              >{{ sortable ? '完成排序' : '拖拽排序' }}</el-button>
              <el-button
                v-permission="['sys:tag:delete']"
                type="danger"
                icon="Delete"
                :disabled="selectedIds.length === 0"
                @click="handleBatchDelete"
              >批量删除</el-button>
            </ButtonGroup>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <div v-loading="loading" class="table-container">
        <!-- 拖拽排序模式 -->
        <draggable
          v-if="sortable"
          v-model="tagList"
          item-key="id"
          handle=".drag-handle"
          @end="onDragEnd"
        >
          <template #item="{ element }">
            <div class="drag-item">
              <el-icon class="drag-handle cursor-move mr-2"><Rank /></el-icon>
              <span class="font-medium">{{ element.name }}</span>
              <span class="ml-4 text-gray-500">(排序: {{ element.sort }})</span>
            </div>
          </template>
        </draggable>
        
        <!-- 正常表格模式 -->
        <el-table
          v-else
          :data="tagList"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="ID" align="center" prop="id" width="80" sortable="custom" />
          <el-table-column label="标签名称" align="center" prop="name" show-overflow-tooltip>
            <template #default="scope">
              <div class="flex items-center">
                <span>{{ scope.row.name }}</span>
                <el-tag v-if="scope.row.isHot" size="small" type="danger" class="ml-2">热门</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="URL友好名称" align="center" prop="slug" show-overflow-tooltip />
          <el-table-column label="标签类型" align="center" prop="tagType" width="120">
            <template #default="scope">
              <el-tag :type="getTagTypeColor(scope.row.tagType)">
                {{ getTagTypeLabel(scope.row.tagType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="排序" align="center" prop="sort" width="100" sortable="custom">
            <template #default="scope">
              <el-tag>{{ scope.row.sort }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" width="80">
            <template #default="scope">
              <el-switch
                v-model="scope.row.isHot"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" sortable="custom">
            <template #default="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.updateTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="220" fixed="right">
            <template #default="scope">
              <el-button
                v-permission="['sys:tag:update']"
                type="primary"
                link
                icon="Edit"
                @click="handleUpdate(scope.row)"
              >编辑</el-button>
              <el-button
                v-permission="['sys:tag:delete']"
                type="danger"
                link
                icon="Delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
              <el-button
                v-if="!sortable"
                type="info"
                link
                icon="View"
                @click="handleView(scope.row)"
              >详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="pageSizes"
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
      width="600px"
      append-to-body
      destroy-on-close
      class="custom-dialog"
      @closed="handleDialogClosed"
    >
      <el-form
        ref="tagFormRef"
        :model="tagForm"
        :rules="rules"
        label-width="100px"
        class="custom-form"
      >
        <el-form-item label="标签名称" prop="name">
          <el-input 
            v-model.trim="tagForm.name" 
            placeholder="请输入标签名称，2-20个字符" 
            clearable
            maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="URL友好名称" prop="slug">
          <el-input 
            v-model.trim="tagForm.slug" 
            placeholder="请输入URL友好名称（英文字母、数字、中划线）" 
            clearable
          >
            <template #append>
              <el-button @click="generateSlug">生成</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="标签类型" prop="tagType">
          <el-select
            v-model="tagForm.tagType"
            placeholder="请选择标签类型"
            style="width: 100%"
          >
            <el-option
              v-for="item in tagTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标签描述" prop="description">
          <el-input
            v-model.trim="tagForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入标签描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="热门标签" prop="isHot">
          <el-radio-group v-model="tagForm.isHot">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="tagForm.sort"
            :min="1"
            :max="9999"
            controls-position="right"
            placeholder="请输入排序"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情查看对话框 -->
    <el-dialog
      title="标签详情"
      v-model="detailDialog.visible"
      width="500px"
      append-to-body
      class="custom-dialog"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="标签ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="标签名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="URL友好名称">{{ detailData.slug || '--' }}</el-descriptions-item>
        <el-descriptions-item label="标签类型">
          <el-tag :type="getTagTypeColor(detailData.tagType)">
            {{ getTagTypeLabel(detailData.tagType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标签描述">{{ detailData.description || '--' }}</el-descriptions-item>
        <el-descriptions-item label="热门标签">
          <el-tag :type="detailData.isHot ? 'success' : 'info'">
            {{ detailData.isHot ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="排序">{{ detailData.sort }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(detailData.updateTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
//import { downloadFile } from '@/utils/download'
//import { formatDate } from '@/utils/date'
import dayjs from 'dayjs'
import draggable from 'vuedraggable'
import {
  getTagListApi,
  addTagApi,
  updateTagApi,
  deleteTagApi,
  // batchDeleteTagApi,
  // updateTagSortApi,
  // updateTagStatusApi,
  // exportTagApi
} from '@/api/article/tag'


// 常量定义
const pageSizes = [10, 20, 30, 50]
const tagTypeOptions = [
  { value: 'topic', label: '主题标签' },
  { value: 'skill', label: '技能标签' },
  { value: 'trend', label: '趋势标签' },
  { value: 'difficulty', label: '难度标签' },
  { value: 'price', label: '价格标签' }
]

// 类型定义
interface TagItem {
  id: string
  name: string
  slug?: string
  tagType: string
  description?: string
  isHot: number
  sort: number
  createTime: string
  updateTime?: string
}

interface QueryParams {
  pageNum: number
  pageSize: number
  name?: string | null
  tagType?: string | null
  isHot?: number | null
  orderByColumn?: string
  isAsc?: string
}

interface TagForm {
  id?: string
  name: string
  slug?: string
  tagType: string
  description?: string
  isHot: number
  sort: number
}

// 查询参数
const queryParams = reactive<QueryParams>({
  pageNum: 1,
  pageSize: 10,
  name: null,
  tagType: null,
  isHot: null,
  orderByColumn: 'sort',
  isAsc: 'asc'
})

const loading = ref(false)
const total = ref(0)
const tagList = ref<TagItem[]>([])
const queryFormRef = ref<FormInstance>()
const tagFormRef = ref<FormInstance>()
const submitLoading = ref(false)
const sortable = ref(false)

// 选中项数组
const selectedIds = ref<string[]>([])

// 弹窗控制
const dialog = reactive({
  title: '',
  visible: false,
  type: 'add'
})

// 详情弹窗控制
const detailDialog = reactive({
  visible: false
})

// 详情数据
const detailData = ref<TagItem>({} as TagItem)

// 表单数据
const tagForm = reactive<TagForm>({
  name: '',
  slug: '',
  tagType: 'topic',
  description: '',
  isHot: 0,
  sort: 1
})

// 表单校验规则
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  slug: [
    { pattern: /^[a-z0-9-]+$/, message: '只能包含小写字母、数字和中划线', trigger: 'blur' }
  ],
  tagType: [
    { required: true, message: '请选择标签类型', trigger: 'change' }
  ],
  sort: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
})

// 获取标签列表
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getTagListApi(queryParams)
    tagList.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取标签列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 表格选择项变化
const handleSelectionChange = (selection: TagItem[]) => {
  selectedIds.value = selection.map(item => item.id)
}
const formatDate = (dateStr: string) => {
  return dayjs(dateStr).format('YYYY年M月D日 dddd')
}
// 批量删除
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) return
  
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个标签吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    confirmButtonClass: 'el-button--danger'
  }).then(async () => {
    try {
      await batchDeleteTagApi(selectedIds.value)
      ElMessage.success('批量删除成功')
      getList()
      selectedIds.value = []
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 删除单个标签
const handleDelete = (row: TagItem) => {
  ElMessageBox.confirm(`确定要删除标签 "${row.name}" 吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    confirmButtonClass: 'el-button--danger'
  }).then(async () => {
    try {
      await deleteTagApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 查看详情
const handleView = (row: TagItem) => {
  detailData.value = { ...row }
  detailDialog.visible = true
}

// 切换热门状态
const handleStatusChange = async (row: TagItem) => {
  try {
    await updateTagStatusApi(row.id, row.isHot)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('状态更新失败:', error)
    // 回滚状态
    row.isHot = row.isHot ? 0 : 1
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
  queryParams.orderByColumn = 'sort'
  queryParams.isAsc = 'asc'
  handleQuery()
}

// 导出数据
const handleExport = async () => {
  try {
    ElMessageBox.confirm('确定要导出标签数据吗？', '导出确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }).then(async () => {
      const data = await exportTagApi(queryParams)
      //downloadFile(data, '标签列表.xlsx')
      ElMessage.success('导出成功')
    })
  } catch (error) {
    console.error('导出失败:', error)
  }
}

// 新增标签
const handleAdd = () => {
  dialog.type = 'add'
  dialog.title = '新增标签'
  dialog.visible = true
  Object.assign(tagForm, {
    id: undefined,
    name: '',
    slug: '',
    tagType: 'topic',
    description: '',
    isHot: 0,
    sort: 1
  })
}

// 修改标签
const handleUpdate = (row: TagItem) => {
  dialog.type = 'edit'
  dialog.title = '编辑标签'
  dialog.visible = true
  Object.assign(tagForm, { ...row })
}

// 生成URL友好名称
const generateSlug = () => {
  if (tagForm.name) {
    tagForm.slug = tagForm.name
      .toLowerCase()
      .replace(/[^a-z0-9\u4e00-\u9fa5]+/g, '-')
      .replace(/^-|-$/g, '')
  }
}

// 提交表单
const submitForm = async () => {
  if (!tagFormRef.value) return
  
  await tagFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (dialog.type === 'add') {
          await addTagApi(tagForm)
          ElMessage.success('新增成功')
        } else {
          await updateTagApi(tagForm)
          ElMessage.success('修改成功')
        }
        getList()
        dialog.visible = false
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 取消按钮
const cancel = () => {
  dialog.visible = false
  tagFormRef.value?.resetFields()
}

// 弹窗关闭后重置表单
const handleDialogClosed = () => {
  tagFormRef.value?.resetFields()
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

// 表格排序
const handleSortChange = ({ prop, order }: { prop: string, order: string }) => {
  queryParams.orderByColumn = prop
  queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
  getList()
}

// 切换拖拽排序模式
const toggleSortable = async () => {
  if (sortable.value) {
    // 完成排序，保存排序结果
    try {
      const sortData = tagList.value.map((item, index) => ({
        id: item.id,
        sort: index + 1
      }))
      await updateTagSortApi(sortData)
      ElMessage.success('排序保存成功')
      getList() // 重新获取列表
    } catch (error) {
      console.error('排序保存失败:', error)
    }
  }
  sortable.value = !sortable.value
}

// 拖拽结束事件
const onDragEnd = () => {
  // 更新本地排序值
  tagList.value.forEach((item, index) => {
    item.sort = index + 1
  })
}

// 获取标签类型颜色
const getTagTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    topic: 'primary',
    skill: 'success',
    trend: 'warning',
    difficulty: 'danger',
    price: 'info'
  }
  return colorMap[type] || 'info'
}

// 获取标签类型标签
const getTagTypeLabel = (type: string) => {
  const option = tagTypeOptions.find(item => item.value === type)
  return option?.label || type
}

// 初始化
onMounted(() => {
  getList()
})

// 监听路由参数变化
watch(() => queryParams, () => {
  getList()
}, { deep: true })
</script>

<style scoped>
.search-wrapper {
  margin-bottom: 20px;
}

.box-card {
  margin-top: 20px;
}

.card-header {
  padding: 12px 20px;
}

.pagination-container {
  padding: 20px 0;
  text-align: right;
}

.custom-dialog .el-dialog__body {
  padding: 20px;
}

.custom-form .el-form-item {
  margin-bottom: 20px;
}

.drag-item {
  padding: 12px 16px;
  margin-bottom: 8px;
  background: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  transition: all 0.3s;
}

.drag-item:hover {
  background: #ebedf0;
}

.drag-handle {
  color: #909399;
  cursor: move;
}

.sortable-ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.sortable-chosen {
  background: #f4f4f5;
}
</style>