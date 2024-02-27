<template>
  <!-- <a-modal  :title="title" :width="width" :visible="visible" :footer="null"> -->
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :width="width" :footer="null">
    <!-- <template #closeIcon>
      <div>
        <CompressOutlined />
        <ExpandOutlined />
        <CloseOutlined @click="handleCancel" />
      </div>
    </template> -->
    <div class="antd-modal-form-ddoc">
      <a-row>
        <a-col :span="24">
          <Typography>
            <Typography.Title :level="3">{{dataInfo?.code}} {{dataInfo?.title  }}</Typography.Title>
            <a-space direction="vertical">
              <template v-for="(item) in (dataInfo?.context||'').split('\n' ) ">
                <Typography.Text>{{ item }}</Typography.Text>
              </template>
            </a-space>
          </Typography>
        </a-col>
      </a-row>
      <br />
      <a-divider class="anlifile-divider" />
      <br />
      <a-space>
        <a-button @click="downloadFile" slot:icon="buttonicon" type="link">
          <CloudDownloadOutlined />
          下载
        </a-button>
        <div class="ant-col ant-form-item-label" style="width: 150px;">{{`${pageNum} / ${pageTotalNum}`}}
        </div><a-button type="link" :disabled="pageNum<=1" @click="prePage">上一页</a-button><a-button type="link"
          :disabled="pageNum>=pageTotalNum" @click="nextPage">下一页</a-button>
        <div style="width:200px"><a-slider v-model:value="scale" @afterChange="scaleChange" max="400"></a-slider>
        </div>
      </a-space>

      <a-spin tip="如果是第一次查看文件，需要进行转码，这个过程需要几分钟，请耐心等待，或者稍后几分钟再进行查看……" :spinning="fileLoading">
        <div class="filecontent">
          <canvas ref="theCanvas"></canvas>
          <!-- <pdf ref="pdf" :src="fileUrl" @page-loaded="pageNum=$event" @num-pages="pageTotalNum = $event"
            @error="pdfError($event)">
          </pdf> -->

          <!-- <pdf ref="pdf" :src="fileUrl" :page="pageNum" @page-loaded="currentPage=$event"
            @num-pages="pageTotalNum = $event" @error="pdfError($event)" @link-clicked="page = $event">
          </pdf> -->
        </div>
      </a-spin>

    </div>
  </BasicModal>
</template>
<script lang="ts" setup>
import { ref, reactive, nextTick, defineExpose, onBeforeUnmount } from 'vue';
import { Button, Typography } from 'ant-design-vue';
import { CompressOutlined, ExpandOutlined, CloseOutlined, CloudDownloadOutlined } from '@ant-design/icons-vue';
import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
import { viewAsPdf, download } from '../TxalTxjob.api';
const title = ref<string>('');
const width = ref<number>(1200);
const fileLoading = ref<boolean>(false);
const dataInfo = ref<any>();
const fileInfo = ref<any>();

const scale = ref<number>(80);
const pageNum = ref<number>(0);
const pageTotalNum = ref<number>(0);

const theCanvas = ref();
const [registerModal, { openModal }] = useModal();
import * as pdfLib from 'pdfjs-dist';
import * as pdfjsWorker from 'pdfjs-dist/build/pdf.worker.min?url';
let pdfDocument: any;
pdfLib.GlobalWorkerOptions.workerSrc = pdfjsWorker.default;

function showDoc(info: any) {
  title.value = info.file.name;
  fileInfo.value = info.file;
  dataInfo.value = info.data;
  // visible.value = true;
  openModal(true);
  viewPdf(info.file.uid);
}

async function viewPdf(fileId) {
  fileLoading.value = true;
  const url = await viewAsPdf(fileId);
  const src = pdfLib.getDocument(url);
  fileLoading.value = false;
  const doc = await src.promise;
  pdfDocument = doc;
  scale.value = 80;
  pageTotalNum.value = doc.numPages;
  if (pageTotalNum.value > 0) {
    pageNum.value = 1;
    renderPage();
  }

  // src.promise.then((pdfDocument) => {
  //   return pdfDocument.getPage(1).then(function (pdfPage) {
  //     // Display page on the existing canvas with 100% scale.
  //     const viewport = pdfPage.getViewport({ scale: 0.8 });
  //     const canvas = theCanvas.value;

  //     canvas.width = viewport.width;
  //     canvas.height = viewport.height;
  //     console.log(theCanvas.value);
  //     const ctx = canvas.getContext('2d');
  //     const renderTask = pdfPage.render({
  //       canvasContext: ctx,
  //       viewport: viewport,
  //     });
  //     return renderTask.promise;
  //   });
  // });
}

async function renderPage() {
  const pdfPage = await pdfDocument.getPage(pageNum.value);
  const viewport = pdfPage.getViewport({ scale: scale.value / 100.0 });
  const canvas = theCanvas.value;
  canvas.width = viewport.width;
  canvas.height = viewport.height;
  const ctx = canvas.getContext('2d');
  pdfPage.render({
    canvasContext: ctx,
    viewport: viewport,
  });
}

function scaleChange() {
  renderPage();
}

function prePage(e) {
  e.stopPropagation();
  --pageNum.value;
  renderPage();
}

function nextPage(e) {
  e.stopPropagation();
  ++pageNum.value;
  renderPage();
}

function downloadFile(e) {
  e.stopPropagation();
  if (fileInfo.value && fileInfo.value.uid) {
    download(`/txal/txalDoclink/download/${fileInfo.value.uid}`, fileInfo.value.name);
  }
}

onBeforeUnmount(() => {
  if (pdfDocument) {
    pdfDocument.destroy();
    pageNum.value = 0;
    scale.value = 80;
  }
});

defineExpose({
  showDoc,
});
</script>

<style>
.antd-modal-form-ddoc {
  min-height: 500px !important;
  /* overflow-y: auto; */
  padding: 24px 24px 24px 24px;
}
.filecontent {
  min-height: 380px !important;
  overflow: auto;
  text-align: center;
  max-height: calc(80vh - 120px) !important;
}
.anlifile-divider {
  margin: 2px 0 !important;
}
/**隐藏样式-modal确定按钮 */
.jee-hidden {
  display: none !important;
}
</style>