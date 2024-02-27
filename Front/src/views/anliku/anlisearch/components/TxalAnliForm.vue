<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="anli-antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-card title="案例详情" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-row>
          <a-col :span="8">
            <a-form-item label="案例库" v-bind="validateInfos.anlikId">
              <a-input v-model:value="formData.anlikId_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="16">
            <a-form-item label="案例名称" v-bind="validateInfos.name">
              <a-input v-model:value="formData.name" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="上传部门">
              <a-input v-model:value="formData.deptCode_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="专案" v-bind="validateInfos.percode">
              <a-input v-model:value="formData.percode" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="产品类型" v-bind="validateInfos.protypeId">
              <a-input v-model:value="formData.protypeId_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="属性类型" v-bind="validateInfos.techtypePid">
              <a-input v-model:value="formData.techtypePid_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="子类型属性" v-bind="validateInfos.techtypeSxmc">
              <a-input v-model:value="formData.techtypeSxmc" :disabled="disabled" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="子层后类型名称" v-bind="validateInfos.techtypeId">
              <a-input v-model:value="formData.techtypeId_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="关键词" v-bind="validateInfos.keywords">
              <a-input v-model:value="formData.keywords" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="案例编号" v-bind="validateInfos.code">
              <a-input v-model:value="formData.code" disabled></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="问题描述" v-bind="validateInfos.question">
              <a-textarea v-model:value="formData.question" rows="4" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="根本原因" v-bind="validateInfos.reason">
              <a-textarea v-model:value="formData.reason" rows="4" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="改善措施" v-bind="validateInfos.solution">
              <a-textarea v-model:value="formData.solution" rows="4" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="评价" v-bind="validateInfos.evalt">
              <a-input v-model:value="formData.evalt_dictText" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
      <a-card title="案例总结" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-card-grid style="width: 50%; text-align: center; padding: 5px">
          <div class="anli-zj-title">技术</div>
          <div class="anli-zj-subtitle">（设计，制程，检测的总结、拓宽或注意事项或参照的设计和检测文件变更；<br />需为验证对比页该三项内容的升华）</div>
          <a-form-item :wrapperCol="{span:24}">
            <a-textarea v-model:value="formData.sbTech" :auto-size="{ minRows: 6, maxRows:6}" showCount maxlength="5000"
              :disabled="disabled" />
          </a-form-item>
        </a-card-grid>
        <a-card-grid style="width: 50%; text-align: center; padding: 5px">
          <div class="anli-zj-title">管理</div>
          <div class="anli-zj-subtitle">（流程，管理方式的的总结、拓宽或注意事项或参照的体系文件变更；<br />需为验证对比页该两项内容的升华）</div>
          <a-form-item :wrapperCol="{span:24}">
            <a-textarea v-model:value="formData.sbMng" :auto-size="{ minRows: 6, maxRows:6}" showCount maxlength="5000"
              :disabled="disabled" />
          </a-form-item>
        </a-card-grid>
        <a-card-grid style="width: 100%; text-align: center; padding: 5px">
          <table class="anli-zj-table">
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b">对比关键点</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">项次</td>
              <td class="anli-zj-table-td anli-zj-table-td-col3 border-r border-b">Before</td>
              <td class="anli-zj-table-td anli-zj-table-td-col4 border-b">After</td>
            </tr>

            <tr class="anli-zj-table-title-row" v-if="formData.bgDesB||formData.bgDesA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[0]">变更内容</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                设计<br /><span>（产品设计变更和制程设计变更）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgDesB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgDesA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bgJcB||formData.bgJcA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[0]"
                v-if="!formData.bgDesB && !formData.bgDesA">变更内容</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                检测<br /><span>（检测规范变更和检测手法变更）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgJcB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgJcA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bgLcB||formData.bgLcA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[0]"
                v-if="!formData.bgDesB && !formData.bgDesA && !formData.bgJcB && !formData.bgJcA">变更内容</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                流程<br /><span>（作业方式变更和作业方法新增）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgLcB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgLcA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bgGlfsB||formData.bgGlfsA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[0]"
                v-if="!formData.bgDesB && !formData.bgDesA && !formData.bgJcB && !formData.bgJcA && !formData.bgLcB && !formData.bgLcA">
                变更内容</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                管理方式<br /><span>（管理方式变更和管理方式新增）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgGlfsB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgGlfsA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bghLlB||formData.bghLlA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[1]">变更后关键指标</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                良率<br /><span>（提供前后良率数据和对应的FACA截图）</span><br /></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLlB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLlA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bghCtB||formData.bghCtA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[1]"
                v-if="!formData.bghLlB && !formData.bghLlA">变更后关键指标</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                CT提升或停机变少<br /><span>（提供前后数据和对应的人机截图或测量或统计截图）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghCtB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghCtA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bghRlB||formData.bghRlA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[1]"
                v-if="!formData.bghLlB && !formData.bghLlA && !formData.bghCtB && !formData.bghCtA">变更后关键指标</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                人力减少<br /><span>（提供前后DFM中人数变化数据和截图或前后标准工时配比人数变化截图）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghRlB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghRlA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row" v-if="formData.bghLyB||formData.bghLyA">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" :rowspan="rowSpans[1]"
                v-if="!formData.bghLlB && !formData.bghLlA && !formData.bghCtB && !formData.bghCtA && !formData.bghRlB && !formData.bghRlA">
                变更后关键指标</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                布局优化<br /><span>（提供前后layout布局方式及截图，并且阐述优化点）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLyB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLyA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" :disabled="disabled" />
                </a-form-item></td>
            </tr>
          </table>
        </a-card-grid>
      </a-card>
      <a-card title="关联报告" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-upload :fileList="fileList" name="files" :multiple="true" :before-upload="beforeUpload" @remove="removefile"
          @download="downloadfile" :disabled="disabled" @preview="previewfile">
          <!-- <p class="ant-upload-drag-icon">
                <InboxOutlined />
              </p>
              <p class="ant-upload-text">可拖拽文件到此处或者点击上传</p> -->
          <template #itemRender="{ file, actions }">
            <a-space>
              <span>{{ file.code }}</span>
              <span :style="file.status === 'error' ? 'color: red' : ''">{{ file.name }}</span>
              <a href="javascript:;" title="下载" @click="actions.download">
                <CloudDownloadOutlined />
              </a>
              <a href="javascript:;" title="查看" @click="actions.preview">
                <EyeOutlined />
              </a>
              <!-- <a h
                    ref="javascript:;" title="删除" @click="actions.remove"><delete-outlined /></a> -->
            </a-space>
          </template>
        </a-upload>
      </a-card>
      <!-- <a-row>
        <a-col :span="24">
          <a-form-item label="关联报告编号">
            <a-space direction="vertical">
              <a-space v-for="(item,index) in  fileList" :key="item.uid">
                <span>{{ item.code }}</span>
                <span :style="item.status === 'error' ? 'color: red' : ''">{{ item.name }}</span>
                <a href="javascript:;" title="下载">
                  <CloudDownloadOutlined />
                </a>
                <a href="javascript:;" title="删除"><delete-outlined /></a>
              </a-space>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row> -->
    </a-form>
  </a-spin>
  <AnliDoc ref="registerDoc"></AnliDoc>
</template>

<script lang="ts" setup>
import { ref, reactive, defineExpose, nextTick, defineProps, computed } from 'vue';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { useGlobSetting } from '/@/hooks/setting';
import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import { getValueType } from '/@/utils';
import { saveOrUpdate, recordLearn, getDocList, download } from '../TxalAnli.api';
import { Form, Upload, UploadFile } from 'ant-design-vue';
import { UploadOutlined, InboxOutlined, CloudDownloadOutlined, DeleteOutlined, EyeOutlined } from '@ant-design/icons-vue';
import type { UploadProps } from 'ant-design-vue';
import { formatDate } from '@vueuse/shared';
import axios from 'axios';
import dayjs from 'dayjs';
import { getYearDay } from 'xe-utils';
import AnliDoc from './AnliDoc.vue';

const registerDoc = ref();
const userStore = useUserStore();
const userInfo = userStore.getUserInfo;

const props = defineProps({
  formDisabled: { type: Boolean, default: false },
  formData: { type: Object, default: () => {} },
  formBpm: { type: Boolean, default: true },
});
const formRef = ref();
const useForm = Form.useForm;
const emit = defineEmits(['register', 'ok']);
const formData = reactive<Record<string, any>>({
  id: '',
  anlikId: '',
  name: '',
  deptCode: userInfo.orgCode,
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
const { createMessage } = useMessage();
const labelCol = ref<any>({ style: { width: '150px' } });
const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
const confirmLoading = ref<boolean>(false);
const typeInfoDisable = ref<boolean>(false);
interface CUploadFile<T = any> extends UploadFile<T> {
  code?: string;
}
const fileList = ref<CUploadFile[]>([]);
const rowSpans = ref<number[]>([4, 4]);
//表单验证
const validatorRules = {
  // anlikId: [{ required: true, message: '请输入案例库!' }],
  // name: [{ required: true, message: '请输入案例名称!' }],
  // deptCode: [{ required: true, message: '请输入上传部门!' }],
  // percode: [{ required: true, message: '请输入专案!' }],
  // protypeId: [{ required: true, message: '请输入产品类型!' }],
  // techtypePid: [{ required: true, message: '请输入属性类型!' }],
  // techtypeSxmc: [{ required: true, message: '请输入子类型属性!' }],
  // techtypeId: [{ required: true, message: '请输入子类型名称!' }],
};
const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: true });
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  if (!formData.techtypePid || !formData.techtypeId || !formData.code) {
    createMessage.info('请先维护上方的基础数据，再上传数据');
    return Upload.LIST_IGNORE;
  }
  if (fileList.value?.find((o) => o.name == file.name)) {
    createMessage.info('已经存在同名文件' + file.name);
    return Upload.LIST_IGNORE;
  }
  const nf: CUploadFile = file;
  nf.code = getFileCode();
  if (fileList.value) {
    fileList.value = [...fileList.value, nf];
  } else {
    fileList.value = [nf];
  }
  return false;
};

const removefile: UploadProps['onRemove'] = (file) => {
  if (fileList.value) {
    const index = fileList.value.indexOf(file);
    const newFileList = fileList.value.slice();
    newFileList.splice(index, 1);
    fileList.value = newFileList;
  }
};

const downloadfile: UploadProps['onDownload'] = (file) => {
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

const previewfile: UploadProps['onPreview'] = (file) => {
  const fileinfo = { id: file.uid, code: (file as CUploadFile)?.code, filename: file.name };
  nextTick(() => {
    registerDoc.value.showDoc({ doc: fileinfo, anli: formData });
  });
};

const getFileCode = () => {
  let idx;
  if (fileList.value?.length > 0) {
    const old: string = fileList.value[fileList.value.length - 1].code || '0-0';
    idx = parseInt(old.substring(old.lastIndexOf('-') + 1)) + 1;
  } else {
    idx = 1;
  }
  let idxx = idx + '';
  while (idxx.length < 3) {
    idxx = '0' + idxx;
  }
  return `${formData.code}-${idxx}`;
};

// 表单禁用
const disabled = computed(() => {
  if (props.formBpm === true) {
    if (props.formData.disabled === false) {
      return false;
    } else {
      return true;
    }
  }
  return props.formDisabled;
});

/**
 * 新增
 */
function add() {
  nextTick(() => {
    resetFields();
    typeInfoDisable.value = false;
    fileList.value = [];
    //赋值
    Object.assign(formData, {});
  });
}

/**
 * 编辑\详情
 */
function edit(record) {
  const dt = dayjs();
  recordLearn({
    learnTime: dt.format('YYYY-MM-DD HH:mm:ss'),
    orgCode: userInfo.orgCode,
    learnUserId: userInfo.id,
    learnUserCode: userInfo.username,
    learnUserName: userInfo.realname,
    anliId: record.id,
    nd: dt.year(),
    yf: dt.month() + 1,
  });
  getFileList(record?.id);
  rowSpans.value = getCount(record);
  nextTick(() => {
    resetFields();
    typeInfoDisable.value = true;
    //赋值
    Object.assign(formData, record);
  });
}

function getCount(record) {
  let i = 0;
  (record.bgDesB || record.bgDesA) && i++;
  (record.bgJcB || record.bgJcA) && i++;
  (record.bgGlfsB || record.bgGlfsA) && i++;
  (record.bgLcB || record.bgLcA) && i++;
  let j = 0;
  (record.bghLlB || record.bghLlA) && j++;
  (record.bghCtB || record.bghCtA) && j++;
  (record.bghRlB || record.bghRlA) && j++;
  (record.bghLyB || record.bghLyA) && j++;
  return [i, j];
}

async function getFileList(id) {
  if (!id) {
    return;
  }
  const list = await getDocList(id);
  fileList.value = list
    ?.sort((a: any, b: any) => (a.code > b.code ? 1 : -1))
    .map((o) => ({ uid: o.id, code: o.code, name: o.name, status: 'done', url: `/txal/txalDoclink/download/${o.id}` }));
}

/**
 * 提交数据
 */
async function submitForm() {
  // 触发表单验证
  await validate();
  confirmLoading.value = true;
  const isUpdate = ref<boolean>(false);
  //时间格式化
  let model = formData;
  if (model.id) {
    isUpdate.value = true;
  }
  //循环数据
  for (let data in model) {
    //如果该数据是数组并且是字符串类型
    if (model[data] instanceof Array) {
      let valueType = getValueType(formRef.value.getProps, data);
      //如果是字符串类型的需要变成以逗号分割的字符串
      if (valueType === 'string') {
        model[data] = model[data].join(',');
      }
    }
  }

  const fData = new window.FormData();
  fData.append('data', JSON.stringify(model));
  const docinfo: any[] = [];
  // fData.append('file[]', fileList.value);
  fileList.value?.forEach((file: UploadProps['fileList'][number]) => {
    if (!file.status) {
      fData.append('files[]', file as any);
    }
    docinfo.push({ id: file.uid, code: file.code, name: file.name });
  });
  fData.append('docinfo', JSON.stringify(docinfo));

  await saveOrUpdate(fData, isUpdate.value)
    .then((res) => {
      if (res.success) {
        createMessage.success(res.message);
        emit('ok');
      } else {
        createMessage.warning(res.message);
      }
    })
    .finally(() => {
      confirmLoading.value = false;
    });
}

defineExpose({
  add,
  edit,
  submitForm,
});
</script>

<style lang="less" scoped>
.anli-antd-modal-form {
  min-height: 500px !important;
  max-height: 80vh !important;
  overflow-y: auto;
  padding: 24px 24px 24px 24px;
}
.anli-zj {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 5px;
  border: #d9d9d9 solid 1px;
  width: 100%;
  text-align: center;
}
.anli-zj-title {
  font-size: 16px;
}

.anli-zj-subtitle {
  color: blue;
}

.anli-zj-table {
  border: #d9d9d9 solid 1px;
  width: 100%;
}

.anli-zj-table-title-row {
  //  background: #bff0b2;
}
.anli-zj-table-td {
  text-align: center;
  vertical-align: middle;
  font-weight: 600;
}
.anli-zj-table-td span {
  color: blue;
}
.anli-zj-table-td-col1 {
  width: 10%;
  background: #bff0b2;
}
.anli-zj-table-td-col2 {
  width: 20%;
  background: #bff0b2;
}
.anli-zj-table-td-col3 {
  width: 35%;
  background: #ffeb9b;
}
.anli-zj-table-td-col4 {
  width: 35%;
  background: #98dafe;
}
.border-r {
  border-right: #d9d9d9 solid 1px;
}
.border-b {
  border-bottom: #d9d9d9 solid 1px;
}
</style>
