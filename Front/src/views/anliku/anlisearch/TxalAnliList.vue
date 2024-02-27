<template>
  <div>
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form ref="formRef" @keyup.enter.native="searchQuery" :model="queryParam" :label-col="labelCol"
        :wrapper-col="wrapperCol">
        <a-row :gutter="24">
          <a-col :lg="8">
            <a-form-item label="产品类型" name="protypeId">
              <j-dict-select-tag v-model:value="queryParam.protypeId" dictCode="txal_protype,name,code"
                placeholder="请选择产品类型" />
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="专案" name="percode">
              <j-input placeholder="请输入专案" v-model:value="queryParam.percode"></j-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :lg="8">
              <a-form-item label="属性类型" name="techtypePid">
                <a-select v-model:value="queryParam.techtypePid" placeholder="请选择属性类型" @change="techtypePidChange">
                  <a-select-option :value="null">请选择…</a-select-option>
                  <template v-for="item in techtypeList" :key="`${item.id}`">
                    <a-select-option :value="item.code" v-if="item.pid=='0'" :data="item">
                      <span style="display: inline-block; width: 100%" :title="item.sxmc">
                        {{ item.sxmc }}
                      </span>
                    </a-select-option>
                  </template>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="子类型属性" name="techtypeSxmc">
                <a-select v-model:value="queryParam.techtypeSxmc" placeholder="请选择子类型属性"
                  @change="()=>{delete queryParam.techtypeId;}">
                  <a-select-option :value="null">请选择…</a-select-option>
                  <template v-for="item in (new Set(techtypeCList.map(o=>o.sxmc)))" :key="`${item}`">
                    <a-select-option :value="item">
                      <span style="display: inline-block; width: 100%">
                        {{ item }}
                      </span>
                    </a-select-option>
                  </template>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="子层后类型名称" name="techtypeId">
                <a-select v-model:value="queryParam.techtypeId" placeholder="请选择子类型名称">
                  <a-select-option :value="null">请选择…</a-select-option>
                  <template
                    v-for="item in uniqBy(techtypeCList?.filter(o=>((!queryParam.techtypeSxmc ||o.sxmc==queryParam.techtypeSxmc) && o.name)),(obj:any)=>obj.name)"
                    :key="item.id">
                    <a-select-option :value="item.code">
                      <span style="display: inline-block; width: 100%" :title="item.name">
                        {{ item.code }} {{ item.name }}
                      </span>
                    </a-select-option>
                  </template>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="上传部门" name="deptCode">
                <j-select-dept v-model:value="queryParam.deptCode" :multiple="false" checkStrictly rowKey="orgCode" />
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="关键词" name="keywords">
                <j-input placeholder="请输入关键词" v-model:value="queryParam.keywords"></j-input>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="改善结果" name="evalt">
                <j-dict-select-tag v-model:value="queryParam.evalt" dictCode="taxl_alievalt" placeholder="请选择改善结果" />
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
    <TxalAnliModal ref="registerModal" @success="handleSuccess"></TxalAnliModal>
  </div>
</template>

<script lang="ts" name="txal-txalAnli" setup>
import { ref, reactive, onBeforeMount } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage';
import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
import { columns } from './TxalAnli.data';
import { list, deleteOne, batchDelete, getImportUrl, getExportUrl, getTechtypeList } from './TxalAnli.api';
import { downloadFile } from '/@/utils/common/renderUtils';
import TxalAnliModal from './components/TxalAnliModal.vue';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import JInput from '/@/components/Form/src/jeecg/components/JInput.vue';
import { uniqBy } from 'lodash-es';

const formRef = ref();
const queryParam = reactive<any>({});
const toggleSearchStatus = ref<boolean>(false);
const registerModal = ref();

const techtypeList = ref<any>([]);
const techtypeCList = ref<any>([]);

//注册table数据
const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
  tableProps: {
    title: '案例',
    api: list,
    columns,
    canResize: false,
    useSearchForm: false,
    actionColumn: {
      width: 120,
      fixed: 'right',
    },
    beforeFetch: (params) => {
      return Object.assign(params, queryParam);
    },
  },
  exportConfig: {
    name: '案例',
    url: getExportUrl,
    params: queryParam,
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
  getTTlist();
});

async function getTTlist() {
  techtypeList.value = await getTechtypeList();
  techtypeCList.value = techtypeList.value?.filter((o) => o.pid && o.pid != '0');
}

/**
 *
 */
const techtypePidChange = (v: any, option: any) => {
  if (v) {
    techtypeCList.value = techtypeList.value?.filter((o) => o.pid == option.data.id);
  } else {
    techtypeCList.value = techtypeList.value?.filter((o) => o.pid && o.pid != '0');
  }
  delete queryParam.techtypeSxmc;
  delete queryParam.techtypeId;
};

/**
 * 详情
 */
function handleDetail(record: Recordable) {
  registerModal.value.disableSubmit = true;
  registerModal.value.edit(record);
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
      onClick: handleDetail.bind(null, record),
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
