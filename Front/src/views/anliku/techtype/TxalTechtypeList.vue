<template>
  <div>
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form @keyup.enter.native="searchQuery" :model="queryParam" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-row :gutter="24">
          <a-col :lg="8">
            <a-form-item label="属性编码(主层&子层后)" name="code">
              <j-input placeholder="请输入属性编码" v-model:value="queryParam.code"></j-input>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="属性类型(主层&子层)" name="sxmc">
              <j-input placeholder="请输入属性类型" v-model:value="queryParam.sxmc"></j-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :lg="8">
              <a-form-item label="子层后类型名称" name="name">
                <j-input placeholder="请输入类型名称" v-model:value="queryParam.name"></j-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
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
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :expandedRowKeys="expandedRowKeys"
      @expand="handleExpand" @fetch-success="onFetchSuccess">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
      <!--字段回显插槽-->
      <template #htmlSlot="{ text }">
        <div v-html="text"></div>
      </template>
      <!--省市区字段回显插槽-->
      <template #pcaSlot="{ text }">
        {{ getAreaTextByCode(text) }}
      </template>
      <template #fileSlot="{ text }">
        <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
        <a-button v-else :ghost="true" type="primary" preIcon="ant-design:download-outlined" size="small"
          @click="downloadFile(text)">下载</a-button>
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <TxalTechtypeModal ref="registerModal" @success="handleSuccess"></TxalTechtypeModal>
  </div>
</template>

<script lang="ts" name="txal-txalTechtype" setup>
import { ref, reactive, unref } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage';
import JInput from '/@/components/Form/src/jeecg/components/JInput.vue';
import { columns } from './TxalTechtype.data';
import { list, deleteTxalTechtype, batchDeleteTxalTechtype, getExportUrl, getImportUrl, getChildList, getChildListBatch } from './TxalTechtype.api';
import { downloadFile } from '/@/utils/common/renderUtils';
import TxalTechtypeModal from './components/TxalTechtypeModal.vue';

const expandedRowKeys = ref([]);
const queryParam = ref<any>({});
const toggleSearchStatus = ref<boolean>(false);
const registerModal = ref();
//注册table数据
const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
  tableProps: {
    title: '工艺类型',
    api: list,
    columns,
    canResize: false,
    useSearchForm: false,
    actionColumn: {
      width: 120,
      fixed: 'right',
    },
    beforeFetch: (params) => {
      return Object.assign(params, queryParam.value);
    },
  },
  exportConfig: {
    name: '工艺类型',
    url: getExportUrl,
  },
  importConfig: {
    url: getImportUrl,
    success: success,
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
async function handleEdit(record) {
  registerModal.value.disableSubmit = false;
  registerModal.value.edit(record);
}

/**
 * 详情
 */
async function handleDetail(record) {
  registerModal.value.disableSubmit = true;
  registerModal.value.edit(record);
}

/**
 * 删除事件
 */
async function handleDelete(record) {
  await deleteTxalTechtype({ id: record.id }, success);
}

/**
 * 批量删除事件
 */
async function batchHandleDelete() {
  const ids = selectedRowKeys.value.filter((item) => !item.includes('loading'));
  await batchDeleteTxalTechtype({ id: ids }, success);
}

/**
 * 成功回调刷新页面
 */
function success() {
  (selectedRowKeys.value = []) && reload();
}

/**
 * 添加下级
 */
function handleAddSub(record) {
  registerModal.value.disableSubmit = false;
  registerModal.value.add(record);
}

/**
 * 成功回调
 */
async function handleSuccess({ isUpdate, values, expandedArr, changeParent }) {
  if (isUpdate) {
    if (changeParent) {
      reload();
    } else {
      // 编辑回调
      updateTableDataRecord(values.id, values);
    }
  } else {
    if (!values['id'] || !values['pid']) {
      //新增根节点
      reload();
    } else {
      //新增子集
      expandedRowKeys.value = [];
      for (let key of unref(expandedArr)) {
        await expandTreeNode(key);
      }
    }
  }
}

/**
 * 接口请求成功后回调
 */
function onFetchSuccess(result) {
  getDataByResult(result.items) && loadDataByExpandedRows();
}

/**
 * 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
 */
async function loadDataByExpandedRows() {
  if (unref(expandedRowKeys).length > 0) {
    const res = await getChildListBatch({ parentIds: unref(expandedRowKeys).join(',') });
    if (res.success && res.result.records.length > 0) {
      //已展开的数据批量子节点
      let records = res.result.records;
      const listMap = new Map();
      for (let item of records) {
        let pid = item['pid'];
        if (unref(expandedRowKeys).includes(pid)) {
          let mapList = listMap.get(pid);
          if (mapList == null) {
            mapList = [];
          }
          mapList.push(item);
          listMap.set(pid, mapList);
        }
      }
      let childrenMap = listMap;
      let fn = (list) => {
        if (list) {
          list.forEach((data) => {
            if (unref(expandedRowKeys).includes(data.id)) {
              data.children = getDataByResult(childrenMap.get(data.id));
              fn(data.children);
            }
          });
        }
      };
      fn(getDataSource());
    }
  }
}

/**
 * 处理数据集
 */
function getDataByResult(result) {
  if (result && result.length > 0) {
    return result.map((item) => {
      //判断是否标记了带有子节点
      if (item['hasChild'] == '1') {
        let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true };
        item.children = [loadChild];
      }
      return item;
    });
  }
}

/**
 *树节点展开合并
 */
async function handleExpand(expanded, record) {
  // 判断是否是展开状态，展开状态(expanded)并且存在子集(children)并且未加载过(isLoading)的就去查询子节点数据
  if (expanded) {
    expandedRowKeys.value.push(record.id);
    if (record.children.length > 0 && !!record.children[0].isLoading) {
      let result = await getChildList({ pid: record.id });
      result = result.records ? result.records : result;
      if (result && result.length > 0) {
        record.children = getDataByResult(result);
      } else {
        record.children = null;
        record.hasChild = '0';
      }
    }
  } else {
    let keyIndex = expandedRowKeys.value.indexOf(record.id);
    if (keyIndex >= 0) {
      expandedRowKeys.value.splice(keyIndex, 1);
    }
  }
}

/**
 * 操作表格后处理树节点展开合并
 */
async function expandTreeNode(key) {
  let record = findTableDataRecord(key);
  expandedRowKeys.value.push(key);
  let result = await getChildList({ pid: key });
  if (result && result.length > 0) {
    record.children = getDataByResult(result);
  } else {
    record.children = null;
    record.hasChild = '0';
  }
  updateTableDataRecord(key, record);
}

/**
 * 操作栏
 */
function getTableAction(record) {
  return [
    {
      label: '编辑',
      onClick: handleEdit.bind(null, record),
    },
  ];
}

/**
 * 下拉操作栏
 */
function getDropDownAction(record) {
  return [
    {
      label: '详情',
      onClick: handleDetail.bind(null, record),
    },
    {
      label: '添加下级',
      onClick: handleAddSub.bind(null, { pid: record.id }),
    },
    {
      label: '删除',
      popConfirm: {
        title: '确定删除吗?',
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
  queryParam.value = {};
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
