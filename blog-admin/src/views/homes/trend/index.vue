<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="关键词" prop="keyword">
          <el-input v-model="queryParams.keyword" placeholder="请输入关键词" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="queryParams.category" placeholder="请选择类别" clearable>
            <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="趋势日期" prop="trendDate">
          <el-date-picker
            v-model="queryParams.trendDate"
            type="date"
            placeholder="请选择趋势日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            clearable
          />
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
              <el-button type="primary" icon="Plus" @click="handleAdd">新增趋势</el-button>
              <el-button type="danger" icon="Delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
                批量删除
              </el-button>
              <el-button type="warning" icon="Upload" @click="handleImport">导入数据</el-button>
            </div>
            <div>
              <el-button type="success" icon="Download" @click="handleExport">导出数据</el-button>
            </div>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="关键词" align="center" prop="keyword" width="150" show-overflow-tooltip />
        <el-table-column label="类别" align="center" prop="category" width="120" />
        <el-table-column label="搜索量" align="center" prop="searchVolume" width="120">
          <template #default="scope">
            <span class="font-medium">{{ scope.row.searchVolume }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变化百分比" align="center" prop="changePercent" width="120">
          <template #default="scope">
            <span :class="scope.row.changePercent >= 0 ? 'text-green-500' : 'text-red-500'">
              {{ scope.row.changePercent >= 0 ? '+' : '' }}{{ scope.row.changePercent }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="趋势日期" align="center" prop="trendDate" width="120" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="600px" top="5vh" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="关键词" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入关键词" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category" placeholder="请选择类别" style="width: 100%">
            <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="搜索量" prop="searchVolume">
          <el-input-number v-model="form.searchVolume" :min="0" :step="1000" placeholder="请输入搜索量" style="width: 100%" />
        </el-form-item>
        <el-form-item label="变化百分比" prop="changePercent">
          <el-input-number
            v-model="form.changePercent"
            :min="-100"
            :max="100"
            :step="0.1"
            placeholder="请输入变化百分比"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="趋势日期" prop="trendDate">
          <el-date-picker
            v-model="form.trendDate"
            type="date"
            placeholder="请选择趋势日期"
            value-format="YYYY-MM-DD HH:mm:ss"
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

    <!-- 导入对话框 -->
    <el-dialog title="导入趋势数据" v-model="importDialog.visible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action="/api/ai-trends/import"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :before-upload="beforeUpload"
        :headers="uploadHeaders"
        :data="uploadData"
        accept=".xlsx,.xls"
        :show-file-list="false"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">请上传 .xlsx 或 .xls 格式的文件，大小不超过10MB</div>
        </template>
      </el-upload>
      <div style="margin-top: 20px;">
        <el-link type="primary" :href="downloadTemplateUrl" :download="downloadTemplateName">
          <el-icon><Download /></el-icon>
          下载导入模板
        </el-link>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
// import { Search, Refresh, Plus, Delete, Upload, Download, Edit, UploadFilled } from '@element-plus/icons-vue'
import {
  getAiTrendListApi,
  getAiTrendDetailApi,
  addAiTrendApi,
  updateAiTrendApi,
  deleteAiTrendApi,
  batchDeleteAiTrendApi,
  exportAiTrendApi
} from '@/api/homes/aiTrend'

// 响应式数据
const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const selectedIds = ref<number[]>([])
const categoryOptions = ref([
  { label: '大语言模型', value: '大语言模型' },
  { label: 'AI应用', value: 'AI应用' },
  { label: 'AI技术', value: 'AI技术' },
  { label: 'AI硬件', value: 'AI硬件' },
  { label: 'AI治理', value: 'AI治理' },
  { label: '开发工具', value: '开发工具' }
])

const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()

const submitLoading = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  category: '',
  trendDate: ''
})

// 对话框
const dialog = reactive({
  title: '',
  visible: false,
  type: 'add'
})

const importDialog = reactive({
  visible: false
})

// 表单数据
const form = reactive({
  id: undefined as number | undefined,
  keyword: '',
  category: '',
  searchVolume: 0,
  changePercent: 0,
  trendDate: ''
})

// 表单验证规则
const rules = reactive<FormRules>({
  keyword: [
    { required: true, message: '请输入关键词', trigger: 'blur' },
    { max: 50, message: '关键词长度不能超过50个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择类别', trigger: 'change' }
  ],
  searchVolume: [
    { required: true, message: '请输入搜索量', trigger: 'blur' },
    { type: 'number', min: 0, message: '搜索量不能为负数', trigger: 'blur' }
  ],
  changePercent: [
    { required: true, message: '请输入变化百分比', trigger: 'blur' },
    { type: 'number', min: -100, max: 100, message: '变化百分比必须在-100到100之间', trigger: 'blur' }
  ],
  trendDate: [
    { required: true, message: '请选择趋势日期', trigger: 'change' }
  ]
})

// 导入相关
const uploadHeaders = reactive({
  Authorization: 'Bearer ' + localStorage.getItem('token')
})

const uploadData = reactive({
  type: 'ai-trend'
})

const downloadTemplateUrl = '/api/ai-trends/template'
const downloadTemplateName = 'AI趋势导入模板.xlsx'

// 方法
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getAiTrendListApi(queryParams)
    tableData.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
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
    keyword: '',
    category: '',
    searchVolume: 0,
    changePercent: 0,
    trendDate: ''
  })
}

const handleAdd = () => {
  dialog.type = 'add'
  dialog.title = '新增趋势'
  dialog.visible = true
  clearForm()
}

const handleUpdate = async (row: any) => {
  dialog.type = 'edit'
  dialog.title = '修改趋势'
  dialog.visible = true
  
  try {
    const { data } = await getAiTrendDetailApi(row.id)
    Object.assign(form, data)
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定删除"${row.keyword}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAiTrendApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的趋势')
    return
  }
  
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条趋势吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchDeleteAiTrendApi(selectedIds.value)
      ElMessage.success('删除成功')
      selectedIds.value = []
      getList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      if (dialog.type === 'add') {
        await addAiTrendApi(form)
        ElMessage.success('新增成功')
      } else {
        await updateAiTrendApi(form)
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

const cancel = () => {
  dialog.visible = false
  formRef.value?.resetFields()
}

const handleImport = () => {
  importDialog.visible = true
}

const handleExport = async () => {
  try {
    const response = await exportAiTrendApi(queryParams)
    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `AI趋势数据_${new Date().getTime()}.xlsx`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
  }
}

const beforeUpload = (file: any) => {
  const isExcel = file.type === 'application/vnd.ms-excel' || 
                  file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  const isLt10M = file.size / 1024 / 1024 < 10
  
  if (!isExcel) {
    ElMessage.error('只能上传Excel文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('上传文件大小不能超过10MB!')
    return false
  }
  return true
}

const handleImportSuccess = (response: any) => {
  if (response.code === 200) {
    ElMessage.success('导入成功')
    importDialog.visible = false
    getList()
  } else {
    ElMessage.error(response.msg || '导入失败')
  }
}

const handleImportError = (error: any) => {
  console.error('导入失败:', error)
  ElMessage.error('导入失败，请检查文件格式或联系管理员')
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
  
  .text-green-500 {
    color: #67c23a;
  }
  
  .text-red-500 {
    color: #f56c6c;
  }
  
  .font-medium {
    font-weight: 500;
  }
  
  :deep(.el-dialog) {
    .el-dialog__body {
      padding: 20px;
    }
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }
  
  .upload-demo {
    :deep(.el-upload) {
      width: 100%;
    }
    
    :deep(.el-upload-dragger) {
      width: 100%;
      height: 200px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>