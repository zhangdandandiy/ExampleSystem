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
    <div class="antd-modal-form">
      <a-row>
        <a-col :span="12">
          <div class="ant-row ant-form-item" style="row-gap: 0px;">
            <div class="ant-col ant-form-item-label" style="width: 150px;"><label class=""
                title="案例编号">案例编号<!----></label>
            </div>
            <div class="ant-col ant-col-xs-24 ant-col-sm-16 ant-form-item-control">
              <a-input v-model:value="anliInfo.code" :disabled="true" />
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="ant-row ant-form-item" style="row-gap: 0px;">
            <div class="ant-col ant-form-item-label" style="width: 150px;"><label class=""
                title="案例名称">案例名称<!----></label>
            </div>
            <div class="ant-col ant-col-xs-24 ant-col-sm-16 ant-form-item-control">
              <a-input v-model:value="anliInfo.name" :disabled="true"></a-input>
            </div>
          </div>
        </a-col>
        <a-col :span="24">
          <div class="ant-row ant-form-item" style="row-gap: 0px;">
            <div class="ant-col ant-form-item-label" style="width: 150px;"><label class=""
                title="关联报告">关联报告<!----></label>
            </div>
            <div class="ant-col ant-col-xs-20 ant-col-sm-12 ant-form-item-control">
              <a-input :value="`${fileInfo.code} ${fileInfo.filename}`" :disabled="true"></a-input>
            </div>
            <div class="ant-col ant-col-xs-4 ant-col-sm-4 ant-form-item-control">
              <a-button @click="downloadFile" slot:icon="buttonicon">
                <CloudDownloadOutlined />
                下载
              </a-button>
            </div>
          </div>
        </a-col>
      </a-row>
      <a-divider class="anlifile-divider" />
      <a-space>
        <div class="ant-col ant-form-item-label" style="width: 150px;">{{`${pageNum} / ${pageTotalNum}`}}
        </div><a-button type="link" :disabled="pageNum<=1" @click="prePage">上一页</a-button><a-button type="link"
          :disabled="pageNum>=pageTotalNum" @click="nextPage">下一页</a-button>
        <div style="width:200px"><a-slider v-model:value="scale" @afterChange="scaleChange" max="400"></a-slider></div>
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
import { Button } from 'ant-design-vue';
import { CompressOutlined, ExpandOutlined, CloseOutlined, CloudDownloadOutlined } from '@ant-design/icons-vue';
import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
import { viewAsPdf, download } from '../TxalAnli.api';
const title = ref<string>('');
const width = ref<number>(1200);
const fileLoading = ref<boolean>(false);
const anliInfo = reactive<Record<string, any>>({
  id: '',
  anlikId: '',
  name: '',
  deptCode: '',
  percode: '',
  protypeId: '',
  techtypePid: '',
  techtypeSxmc: '',
  techtypeId: '',
  keywords: '',
  code: '',
  question: '',
  reason: '',
  solution: '',
  evalt: '',
});
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
  title.value = `${info.doc.code} ${info.doc.filename}`;
  fileInfo.value = info.doc;
  Object.assign(anliInfo, info.anli);
  // visible.value = true;
  openModal(true);
  viewPdf(info.doc.id);
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
  if (fileInfo.value && fileInfo.value.id) {
    download(`/txal/txalDoclink/download/${fileInfo.value.id}`, fileInfo.value.filename);
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
.antd-modal-form {
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