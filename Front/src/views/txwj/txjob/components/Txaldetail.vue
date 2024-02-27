<template>
  <!-- <a-modal  :title="title" :width="width" :visible="visible" :footer="null"> -->
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :width="width" :footer="null">
    <Table :columns="columns" :dataSource="dataSource" :pagination="false" :scroll="{ y: '80vh' }" bordered
      size="small">
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
  </BasicModal>
  <TxalDDoc ref="registerDoc"></TxalDDoc>
</template>
<script lang="ts" setup>
import { ref, reactive, nextTick, defineExpose, onBeforeUnmount } from 'vue';
import { Table, Typography } from 'ant-design-vue';
import { dlist, getOrgList, getDocListByIds, download } from '../TxalTxjob.api';
import { CompressOutlined, ExpandOutlined, CloseOutlined, CloudDownloadOutlined, EyeOutlined } from '@ant-design/icons-vue';
import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
import TxalDDoc from './TxalDDoc.vue';

const registerDoc = ref();
const title = ref<string>('');
const width = ref<string | number>('80vw');
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
async function showDetail(info: any) {
  title.value = `${info.fcode} ${info.fname}`;
  orgList.value = await getOrgList();
  dataSource.value = await dlist(info.id);
  const ids = dataSource.value?.map((o) => o.id).join(',');
  if (ids) {
    docList.value = await getDocListByIds({ ids });
  } else {
    docList.value = [];
  }
  openModal(true);
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