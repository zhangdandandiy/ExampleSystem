<template>
  <div>
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form ref="formRef" @keyup.enter.native="searchQuery" :model="queryParam" :label-col="labelCol"
        :wrapper-col="wrapperCol">
        <a-row :gutter="24">
          <a-col :lg="6">
            <a-form-item label="关联部门" name="orgs">
              <j-select-dept v-model:value="queryParam.orgs" :multiple="false" checkStrictly rowKey="orgCode" />
            </a-form-item>
          </a-col>
          <a-col :lg="6">
            <a-form-item label="归属程序" name="pname">
              <a-select v-model:value="queryParam.pname" placeholder="请选择归属程序">
                <a-select-option :value="null">请选择…</a-select-option>
                <template v-for="(item,index) in pnamelist" :key="index">
                  <a-select-option :value="item">
                    <span style="display: inline-block; width: 100%" :title="item">
                      {{ item }}
                    </span>
                  </a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :lg="6">
              <a-form-item label="文件名称" name="fname">
                <j-input placeholder="请输入文件名称" v-model:value="queryParam.fname"></j-input>
              </a-form-item>
            </a-col>
            <a-col :lg="6">
              <a-form-item label="文件编号" name="fcode">
                <j-input placeholder="请输入文件编号" v-model:value="queryParam.fname"></j-input>
              </a-form-item>
            </a-col>
            <a-col :lg="6">
              <a-form-item label="文件版本" name="fname">
                <j-input placeholder="请输入文件版本" v-model:value="queryParam.fname"></j-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="6" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
                <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset"
                  style="margin-left: 8px">重置</a-button>
                <a @click="toggleSearchStatus = !toggleSearchStatus" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <Icon :icon="toggleSearchStatus ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
                </a>
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <!-- <a-button type="primary" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button> -->
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 下载</a-button>
        <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>批量操作
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown> -->
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
      <!--字段回显插槽-->
      <template #htmlSlot="{text}">
        <div v-html="text"></div>
      </template>
      <!--省市区字段回显插槽-->
      <template #pcaSlot="{text}">
        {{ getAreaTextByCode(text) }}
      </template>
      <template #fileSlot="{text}">
        <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
        <a-button v-else :ghost="true" type="primary" preIcon="ant-design:download-outlined" size="small"
          @click="downloadFile(text)">下载</a-button>
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <TxalTxjobModal ref="registerModal" @success="handleSuccess"></TxalTxjobModal>
    <Txaldetail ref="registerDetail" />
  </div>
</template>

<script lang="ts" name="txal-txalTxjob" setup>
import { ref, reactive, onBeforeMount } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns } from './TxalTxjob.data';
import { list, deleteOne, batchDelete, getImportUrl, getExportUrl, getPnameList } from './TxalTxjob.api';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import { downloadFile } from '/@/utils/common/renderUtils';
import JInput from '/@/components/Form/src/jeecg/components/JInput.vue';
import TxalTxjobModal from './components/TxalTxjobModal.vue';
import Txaldetail from './components/Txaldetail.vue';

const registerDetail = ref();
const formRef = ref();
const queryParam = reactive<any>({});
const toggleSearchStatus = ref<boolean>(false);
const registerModal = ref();
const pnamelist = ref<string[]>();
//注册table数据
const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
  tableProps: {
    title: '体系作业',
    api: list,
    columns,
    canResize: false,
    useSearchForm: false,
    actionColumn: {
      width: 120,
      fixed: 'right',
    },
    beforeFetch: (params) => {
      const { orgs, ...p } = queryParam;
      if (orgs) {
        p.orgs = ',' + orgs + ',';
      }
      return Object.assign(params, p);
    },
  },
  exportConfig: {
    name: '体系作业',
    url: getExportUrl,
    params: { ...queryParam, orgs: queryParam.orgs ? ',' + queryParam.orgs + ',' : undefined },
  },
  importConfig: {
    url: getImportUrl,
    success: handleSuccess,
  },
});
const [registerTable, { reload, collapseAll, updateTableDataRecord, findTableDataRecord, getDataSource }, { rowSelection, selectedRowKeys }] =
  tableContext;
const labelCol = reactive({
  xs: { span: 24 },
  sm: { span: 7 },
});
const wrapperCol = reactive({
  xs: { span: 24 },
  sm: { span: 16 },
});

onBeforeMount(() => {
  queryPnameList();
});

async function queryPnameList() {
  pnamelist.value = await getPnameList();
}

/**
 * 新增事件
 */
function handleAdd() {
  registerModal.value.disableSubmit = false;
  registerModal.value.add();
}

/**
 * 编辑事件
 */
function handleEdit(record: Recordable) {
  registerModal.value.disableSubmit = false;
  registerModal.value.edit(record);
}

/**
 * 详情
 */
function handleDetail(record: Recordable) {
  registerModal.value.disableSubmit = true;
  registerModal.value.edit(record);
}

/**
 * 删除事件
 */
async function handleDelete(record) {
  await deleteOne({ id: record.id }, handleSuccess);
}

/**
 * 批量删除事件
 */
async function batchHandleDelete() {
  await batchDelete({ ids: selectedRowKeys.value }, handleSuccess);
}

function handleTxwjDetail(record) {
  registerDetail.value.showDetail(record);
}
/**
 * 成功回调
 */
function handleSuccess() {
  (selectedRowKeys.value = []) && reload();
}

/**
 * 操作栏
 */
function getTableAction(record) {
  return [
    {
      label: '详情',
      onClick: handleTxwjDetail.bind(null, record),
    },
  ];
}

/**
 * 下拉操作栏
 */
function getDropDownAction(record) {
  return [
    {
      label: '编辑',
      onClick: handleEdit.bind(null, record),
    },
    {
      label: '详情',
      onClick: handleDetail.bind(null, record),
    },
    {
      label: '删除',
      popConfirm: {
        title: '是否确认删除',
        confirm: handleDelete.bind(null, record),
      },
    },
  ];
}

/**
 * 查询
 */
function searchQuery() {
  reload();
}

/**
 * 重置
 */
function searchReset() {
  formRef.value.resetFields();
  selectedRowKeys.value = [];
  //刷新数据
  reload();
}
</script>

<style lang="less" scoped>
.jeecg-basic-table-form-container {
  .table-page-search-submitButtons {
    display: block;
    margin-bottom: 24px;
    white-space: nowrap;
  }
  .query-group-cust {
    width: calc(50% - 15px);
    min-width: 100px !important;
  }
  .query-group-split-cust {
    width: 30px;
    display: inline-block;
    text-align: center;
  }
}
</style>
