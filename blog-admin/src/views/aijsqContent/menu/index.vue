<template>
  <div class="menu-container">
    <!-- 搜索表单 -->
    <div class="search-wrapper">
      <el-form ref="queryMFormRef" :model="queryParams" :inline="true">
        <el-form-item label="学习路径类型" prop="status">
          <el-select v-model="queryParams.type" placeholder="请选择菜单类型" clearable>
            <el-option label="agi之路" :value="0" />
            <el-option label="设计师AI路径" :value="1" />
            <el-option label="程序员AI路径" :value="2" />
            <el-option label="运营人员AI路径" :value="3" />
            <el-option label="学生AI路径" :value="4" />
            <el-option label="管理者AI路径" :value="5" />
            <el-option label="ai岗位路径" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <ButtonGroup>
            <el-button
              v-permission="['sys:menu:add']"
              type="primary"
              icon="Plus"
              @click="handleAdd(null)"
            >新增</el-button>
          </ButtonGroup>
        </div>
      </template>
      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="menuList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column label="菜单名称" prop="title" show-overflow-tooltip />
        <el-table-column label="图标" align="center" width="60">
          <template #default="{ row }">
            <el-icon v-if="row.icon">
              <component :is="row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column label="类型" align="center" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === MenuTypeEnum.CATALOG ? 'primary' : row.type === MenuTypeEnum.MENU ? 'success' : 'info'">
              {{ row.type === MenuTypeEnum.CATALOG ? '目录' : row.type === MenuTypeEnum.MENU ? '菜单' : '按钮' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" prop="sort" width="60" align="center" />
     
        <el-table-column label="状态" align="center" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isVisible === 1 ? 'success' : 'info'">
              {{ row.isVisible === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button type="success" link @click="handleAdd(row)" v-permission="['sys:menu:add']">
              <el-icon><Plus /></el-icon>新增
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)" v-permission="['sys:menu:update']">
              <el-icon><Edit /></el-icon>修改
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-permission="['sys:menu:delete']">
              <el-icon><Delete /></el-icon>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增菜单' : '修改菜单'"
      width="700px"
      append-to-body
    >
      <el-form
        ref="menuFormRef"
        :model="menuForm"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="上级菜单" prop="parentId">
              <el-tree-select
                v-model="menuForm.parentId"
                :data="menuOptions"
                :props="{ label: 'title', value: 'id' }"
                value-key="id"
                placeholder="选择上级菜单"
                check-strictly
                :render-after-expand="false"
                class="flex-grow"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="菜单类型" prop="type">
              <el-select v-model="menuForm.type" placeholder="请选择内容类型" @change="handleContentTypeChange">
                <el-option label="agi之路" :value="0" />
                <el-option label="设计师AI路径" :value="1" />
                <el-option label="程序员AI路径" :value="2" />
                <el-option label="运营人员AI路径" :value="3" />
                <el-option label="学生AI路径" :value="4" />
                <el-option label="管理者AI路径" :value="5" />
                <el-option label="ai岗位路径" :value="6" />
              </el-select>
              <!-- <el-radio-group v-model="menuForm.type" class="menu-type-group">
                <el-radio-button :value="MenuTypeEnum.CATALOG">绘画</el-radio-button>
                <el-radio-button :value="MenuTypeEnum.MENU">音频</el-radio-button>
                <el-radio-button :value="MenuTypeEnum.BUTTON">视频</el-radio-button>
              </el-radio-group> -->
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="菜单名称" prop="title">
              <el-input v-model="menuForm.title" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="排序" prop="menuOrder">
              <el-input-number v-model="menuForm.menuOrder" :min="1" class="flex-grow" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="图标">
              <el-input v-model="menuForm.icon" placeholder="点击选择图标" readonly>
                <template #prefix>
                  <el-icon v-if="menuForm.icon">
                    <component :is="menuForm.icon" />
                  </el-icon>
                </template>
                <template #append>
                  <el-button @click="showIconSelect = true">
                    <el-icon><View /></el-icon>选择图标
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态" prop="isVisible">
              <el-radio-group v-model="menuForm.isVisible">
                <el-radio :value="1">显示</el-radio>
                <el-radio :value="0">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item label="是否外链" prop="isExternal">
              <el-radio-group v-model="menuForm.isExternal">
                <el-radio :value="0">否</el-radio>
                <el-radio :value="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 图标选择器组件 -->
    <icon-select
      v-model="menuForm.icon"
      v-model:visible="showIconSelect"
    />
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import IconSelect from '@/components/IconSelect/index.vue'
import {
  getMenuListApi,
  saveMenuApi,
  updateMenuApi,
  deleteMenuApi,
} from '@/api/aijsqContent/aijsqMenu'
import { MenuTypeEnum } from "@/enum/MenuTypeEnum";

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const menuFormRef = ref<FormInstance>()
const queryMFormRef = ref<FormInstance>()
const showIconSelect = ref(false)
const submitLoading = ref(false)

// 表单校验规则
const rules = {
  parentId: [
    { required: true, message: '请选择父级菜单', trigger: 'blur' }
  ],
  title: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ],
  isVisible: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
}
// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: ''
})
// 菜单表单对象
const menuForm = reactive<any>({
  id: undefined,
  parentId: "",
  name: '',
  title:'',
  menuOrder: 0,
  type: "0",
  icon: '',
  isVisible: 1
})
// 重置查询
const resetQuery = () => {
  queryMFormRef.value?.resetFields()
  handleQuery()
}
// 模拟菜单数据
const menuList = ref<any>([])

// 菜单树选项
const menuOptions = ref<any>([])

// 查询方法
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getMenuListApi(queryParams)
    menuList.value = data
    const topMenu = {id: 0,title: '顶级菜单',  children: data}
    menuOptions.value = []
    menuOptions.value.push(topMenu)
  } catch (error) {
  }
  loading.value = false
}

// 提交表单
const submitForm = async () => {
  if (!menuFormRef.value) return
  
  await menuFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const formData = { ...menuForm }
        formData.icon = formatIconName(formData.icon)
        
        if (dialogType.value === 'add') {
          console.log("dfsadfasd")
          await saveMenuApi(formData)
          ElMessage.success('新增成功')
        } else {
          await updateMenuApi(formData)
          ElMessage.success('修改成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 删除菜单
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除菜单"${row.title}"吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteMenuApi(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
    }
  }).catch(() => {})
}

// 查询
const handleQuery = () => {
  getList()
}
// 重置表单
const resetForm = () => {
  menuForm.id = undefined
  menuForm.parentId = "0"
  menuForm.title = ''
  menuForm.menuOrder = 0
  menuForm.type = "0"
  menuForm.icon = ''
  menuForm.isVisible = 1
}

// 新增菜单
const handleAdd = (row?: any) => {
  resetForm()
  if (row) {
    menuForm.parentId = String(row.id)
  }
  dialogType.value = 'add'
  dialogVisible.value = true
}

// 修改菜单
const handleEdit = (row: any) => {
  resetForm()
  dialogType.value = 'edit'
  dialogVisible.value = true
  // 格式化图标名称
  const formData = { ...row }
  formData.icon = formatIconName(formData.icon)
  Object.assign(menuForm, formData)
}

// 在组件挂载时获取数据
onMounted(() => {
  getList()
})
const handleContentTypeChange = (value: string) => {
  // if (value === '0') {
  //   form.difficulty = ''
  //   form.isFeatured = 0
  //   form.isHot = 0
  //   form.slug = ''
  // }
}
// 处理图标名称转换
const formatIconName = (icon: string) => {
  if (!icon) return ''
  // 如果是 el-icon- 开头，移除前缀
  if (icon.startsWith('el-icon-')) {
    return icon.replace('el-icon-', '')
  }
  // 确保首字母大写
  return icon.charAt(0).toUpperCase() + icon.slice(1)
}
</script>