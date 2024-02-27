<template>
  <!-- <a-modal  :title="title" :width="width" :visible="visible" :footer="null"> -->
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :width="width" :footer="null">
    <a-form ref="formRef" class="antd-modal-form-txjobd" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-row>
        <!-- <a-col> :xs="{ span: 24 }" :sm="{ span: 16,offset:8 }" -->
        <a-col :span="8">
          <a-form-item label="三阶文件名称">
            <a-input v-model:value="dataInfo.fname" placeholder="请输入三阶文件名称" :disabled="true"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="三阶文件编号">
            <a-input v-model:value="dataInfo.fcode" placeholder="请输入三阶文件编号" :disabled="true"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="三阶级文件最新版本">
            <a-input v-model:value="dataInfo.fver" placeholder="请输入三阶级文件最新版本" :disabled="true"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="归属程序名称">
            <a-input v-model:value="dataInfo.pname" placeholder="请输入归属程序名称" :disabled="true"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="作业办法关联部门">
            <j-select-dept v-model:value="dataInfo.orgs" :multiple="true" checkStrictly :disabled="true"
              rowKey="orgCode" />
          </a-form-item>
        </a-col>
        <a-col :span="8" v-if="docinfo!=null">
          <a-form-item label="作业办法">
            <a-space>
              <span :style="docinfo.status === 'error' ? 'color: red' : ''">{{ docinfo.name }}</span>
              <a href="javascript:;" title="下载" @click="downloadfile(docinfo)">
                <CloudDownloadOutlined />
              </a>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>

      <Table :columns="columns" :dataSource="dataSource" :pagination="false" :scroll="{ y: 'calc( 80vh - 205px)' }"
        bordered size="small">
        <template #bodyCell="{text, column,record  }">
          <template v-if="column.key === 'context'">
            <Typography>
              <Typography.Title :level="3">{{record?.code}} {{record?.title  }}</Typography.Title>
              <a-space direction="vertical">
                <template v-for="(item) in (record?.context||'').split('\n' ) ">
                  <Typography.Text>{{ item }}</Typography.Text>
                </template>
              </a-space>
            </Typography>
          </template>
          <template v-else-if="column.key === 'maOrgCode'||column.key === 'lkOrgCode'">
            {{ text?.split(",").map(o=>orgList?.find(ol=>ol.orgCode===o)?.departName||o).join(",") }}
          </template>
          <template v-else-if="column.key === 'intext'||column.key === 'outtext'">
            <template v-for="item in getFileList(record.id,column.key)">
              <a-space direction="vertical">
                <div>
                  <span>{{ item.name }}{{ " " }}</span>
                  <a href="javascript:;" title="下载" @click="downloadfile(item)">
                    <CloudDownloadOutlined />
                  </a>{{ " " }}
                  <a href="javascript:;" title="查看" @click="previewfile(record,item)">
                    <EyeOutlined />
                  </a>
                </div>
                <!-- <a href="javascript:;" title="删除" @click="removefile(docinfo)" v-if="!disabled"><delete-outlined /></a> -->
              </a-space>
            </template>
          </template>
        </template>
      </Table>
    </a-form>
  </BasicModal>
  <TxalDDoc ref="registerDoc"></TxalDDoc>
</template>
<script lang="ts" setup>
import { ref, reactive, nextTick, defineExpose, onBeforeUnmount, watch } from 'vue';
import { Table, Typography } from 'ant-design-vue';
import { dlist, getOrgList, getDocListByIds, getDocList, download } from '../TxalTxjob.api';
import { CompressOutlined, ExpandOutlined, CloseOutlined, CloudDownloadOutlined, EyeOutlined } from '@ant-design/icons-vue';
import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import TxalDDoc from './TxalDDoc.vue';
const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
const registerDoc = ref();
const title = ref<string>('');
const width = ref<string | number>('80vw');
const fsref = ref<boolean>();
const [registerModal, { openModal }] = useModal();
const columns = [
  { title: '序', dataIndex: 'code', key: 'code', width: 80 },
  { title: '作业内容', dataIndex: 'context', key: 'context' },
  { title: '主导部门', dataIndex: 'maOrgCode', key: 'maOrgCode', width: 100 },
  { title: '关联部门', dataIndex: 'lkOrgCode', key: 'lkOrgCode', width: 150 },
  { title: '输入内容', dataIndex: 'intext', key: 'intext', width: 300 },
  { title: '输出内容', dataIndex: 'outtext', key: 'outtext', width: 300 },
];
const dataSource = ref<any[]>([]);
const orgList = ref<any[]>([]);
const docList = ref<any[]>([]);
const dataInfo = ref<any>();
const docinfo = ref<any>();
async function showDetail(info: any) {
  dataInfo.value = info;
  title.value = `${info.fcode} ${info.fname}`;
  orgList.value = await getOrgList();
  dataSource.value = await dlist(info.id);
  getMainFileList(info.id);
  const ids = dataSource.value?.map((o) => o.id).join(',');
  if (ids) {
    docList.value = await getDocListByIds({ ids });
  } else {
    docList.value = [];
  }
  openModal(true);
}

async function getMainFileList(id) {
  if (!id) {
    docinfo.value = null;
    return;
  }
  const list = await getDocList(id);
  const obj: any = list?.find((o: any) => o.code === 'jobdoc');
  docinfo.value = { uid: obj.id, code: obj.code, name: obj.name, status: 'done', url: `/txal/txalDoclink/download/${obj.id}` };
}

function getFileList(id, type) {
  const inout = type === 'intext' ? 'in' : 'out';
  return docList.value
    ?.filter((o: any) => o.code === inout && o.linkId === id)
    .map((o) => ({ uid: o.id, code: o.code, name: getShortName(o.name), status: 'done', url: `/txal/txalDoclink/download/${o.id}` }));
}

function getShortName(name: string) {
  let idx = name.indexOf('输入内容');
  if (idx == -1) {
    idx = name.indexOf('输出内容');
  }
  const iLen = '输入内容'.length;
  return name.substring(idx + iLen);
}

const downloadfile = (file) => {
  if (file.url) {
    download(file.url, file.name);
  } else {
    let aurl = window.URL.createObjectURL(new Blob([file as any]));
    let link = document.createElement('a');
    link.style.display = 'none';
    link.href = aurl;
    link.setAttribute('download', file.name);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link); //下载完成移除元素
    window.URL.revokeObjectURL(aurl); //释放掉blob对象
  }
};

const previewfile = (data, file) => {
  registerDoc.value.showDoc({ data, file });
};

defineExpose({
  showDetail,
});
</script>
<style lang="less" scoped>
.antd-modal-form-txjobd {
  min-height: 500px !important;
  height: 80vh !important;
  // overflow-y: auto;
  padding: 24px 24px 24px 24px;
}
</style>