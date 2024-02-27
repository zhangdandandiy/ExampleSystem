<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="anli-antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-card title="案例详情" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-row>
          <a-col :span="8">
            <a-form-item label="案例库" v-bind="validateInfos.anlikId">
              <j-dict-select-tag v-model:value="formData.anlikId" dictCode="txal_anlik,name,code" placeholder="请输入案例库"
                :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="16">
            <a-form-item label="案例名称" v-bind="validateInfos.name">
              <a-input v-model:value="formData.name" placeholder="请输入案例名称" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="上传部门" v-bind="validateInfos.deptCode">
              <j-select-dept v-model:value="formData.deptCode" :multiple="false" checkStrictly :disabled="disabled"
                rowKey="orgCode" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="专案" v-bind="validateInfos.percode">
              <a-input v-model:value="formData.percode" placeholder="请输入专案" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="产品类型" v-bind="validateInfos.protypeId">
              <j-dict-select-tag v-model:value="formData.protypeId" dictCode="txal_protype,name,code"
                placeholder="请选择产品类型" :disabled="typeInfoDisable" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="属性类型" v-bind="validateInfos.techtypePid">
              <a-select v-model:value="formData.techtypePid" placeholder="请选择属性类型" :disabled="typeInfoDisable"
                @change="techtypePidChange">
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
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="子类型属性" v-bind="validateInfos.techtypeSxmc">
              <a-select v-model:value="formData.techtypeSxmc" placeholder="请选择子类型属性" :disabled="typeInfoDisable">
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
          <a-col :span="8">
            <a-form-item label="子层后类型名称" v-bind="validateInfos.techtypeId">
              <a-select v-model:value="formData.techtypeId" placeholder="请选择子类型名称" :disabled="typeInfoDisable"
                @change="techtypeIdChanged">
                <a-select-option :value="null">请选择…</a-select-option>
                <template v-for="item in (techtypeCList.filter(o=>o.sxmc==formData.techtypeSxmc))" :key="`${item.id}`">
                  <a-select-option :value="item.code">
                    <span style="display: inline-block; width: 100%" :title="item.name">
                      {{ item.name }}
                    </span>
                  </a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="关键词" v-bind="validateInfos.keywords">
              <a-input v-model:value="formData.keywords" placeholder="请输入关键词" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="案例编号" v-bind="validateInfos.code">
              <a-input v-model:value="formData.code" placeholder="请输入案例编号" disabled></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="问题描述" v-bind="validateInfos.question">
              <a-textarea v-model:value="formData.question" rows="4" placeholder="请输入问题描述" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="根本原因" v-bind="validateInfos.reason">
              <a-textarea v-model:value="formData.reason" rows="4" placeholder="请输入根本原因" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="改善措施" v-bind="validateInfos.solution">
              <a-textarea v-model:value="formData.solution" rows="4" placeholder="请输入改善措施" :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="8">
            <a-form-item label="评价" v-bind="validateInfos.evalt">
              <j-dict-select-tag v-model:value="formData.evalt" dictCode="taxl_alievalt" placeholder="请选择评价"
                :disabled="disabled" />
            </a-form-item>
          </a-col>
        </a-row>

      </a-card>
      <a-card title="案例总结" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-card-grid style="width: 50%; text-align: center; padding: 5px">
          <div class="anli-zj-title">技术</div>
          <div class="anli-zj-subtitle">（设计，制程，检测的总结、拓宽或注意事项或参照的设计和检测文件变更；<br />需为验证对比页该三项内容的升华）</div>
          <a-form-item :wrapperCol="{span:24}">
            <a-textarea v-model:value="formData.sbTech" :auto-size="{ minRows: 6, maxRows:6}" showCount
              maxlength="5000" />
          </a-form-item>
        </a-card-grid>
        <a-card-grid style="width: 50%; text-align: center; padding: 5px">
          <div class="anli-zj-title">管理</div>
          <div class="anli-zj-subtitle">（流程，管理方式的的总结、拓宽或注意事项或参照的体系文件变更；<br />需为验证对比页该两项内容的升华）</div>
          <a-form-item :wrapperCol="{span:24}">
            <a-textarea v-model:value="formData.sbMng" :auto-size="{ minRows: 6, maxRows:6}" showCount
              maxlength="5000" />
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
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" rowspan="4">变更内容</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                设计<br /><span>（产品设计变更和制程设计变更）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgDesB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgDesA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                检测<br /><span>（检测规范变更和检测手法变更）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgJcB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgJcA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                流程<br /><span>（作业方式变更和作业方法新增）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgLcB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgLcA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                管理方式<br /><span>（管理方式变更和管理方式新增）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgGlfsB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bgGlfsA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col1 border-r border-b" rowspan="4">变更后关键指标</td>
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                良率<br /><span>（提供前后良率数据和对应的FACA截图）</span><br /></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLlB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLlA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                CT提升或停机变少<br /><span>（提供前后数据和对应的人机截图或测量或统计截图）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghCtB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghCtA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                人力减少<br /><span>（提供前后DFM中人数变化数据和截图或前后标准工时配比人数变化截图）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghRlB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghRlA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
            <tr class="anli-zj-table-title-row">
              <td class="anli-zj-table-td anli-zj-table-td-col2 border-r border-b">
                布局优化<br /><span>（提供前后layout布局方式及截图，并且阐述优化点）</span></td>
              <td class="anli-zj-table-td border-r border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLyB" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
              <td class="anli-zj-table-td border-b"><a-form-item :wrapperCol="{span:24}">
                  <a-textarea v-model:value="formData.bghLyA" :auto-size="{ minRows: 6, maxRows:6}" :bordered="false"
                    showCount maxlength="5000" placeholder="请输入内容，如果该项内容没有，则留空即可" />
                </a-form-item></td>
            </tr>
          </table>
        </a-card-grid>
      </a-card>
      <a-card title="关联报告" headStyle="background: #ececec" bodyStyle="padding:5px">
        <a-upload-dragger :fileList="fileList" name="files" :multiple="true" :before-upload="beforeUpload"
          @remove="removefile" @download="downloadfile" :disabled="disabled">
          <p class="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p class="ant-upload-text">可拖拽文件到此处或者点击上传<br /><span
              class="ant-upload-text-info">为了方便利用查询，请尽量上传pdf格式的文件</span>
          </p>
          <template #itemRender="{ file, actions }">
            <a-space>
              <span>{{ file.code }}</span>
              <span :style="file.status === 'error' ? 'color: red' : ''">{{ file.name }}</span>
              <a href="javascript:;" title="下载" @click="actions.download">
                <CloudDownloadOutlined />
              </a>
              <a href="javascript:;" title="删除" @click="actions.remove" v-if="!disabled"><delete-outlined /></a>
            </a-space>
          </template>
        </a-upload-dragger>
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
</template>

<script lang="ts" setup>
import { ref, reactive, defineExpose, nextTick, defineProps, computed, onBeforeMount } from 'vue';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { useGlobSetting } from '/@/hooks/setting';
import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import { getValueType } from '/@/utils';
import { saveOrUpdate, getTechtypeList, newCode, getDocList, download, nameExists } from '../TxalAnli.api';
import { Form, Upload, UploadFile } from 'ant-design-vue';
import { UploadOutlined, InboxOutlined, CloudDownloadOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import type { UploadProps } from 'ant-design-vue';
import { formatDate } from '@vueuse/shared';
import axios from 'axios';
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
  sbTech: '',
  sbMng: '',
  bgDesB: '',
  bgJcB: '',
  bgGlfsB: '',
  bgLcB: '',
  bghLlB: '',
  bghCtB: '',
  bghRlB: '',
  bghLyB: '',
  bgDesA: '',
  bgJcA: '',
  bgGlfsA: '',
  bgLcA: '',
  bghLlA: '',
  bghCtA: '',
  bghRlA: '',
  bghLyA: '',
});
const { createMessage } = useMessage();
const labelCol = ref<any>({ style: { width: '150px' } });
const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
const techtypeList = ref<any>([]);
const techtypeCList = ref<any>([]);
const confirmLoading = ref<boolean>(false);
const typeInfoDisable = ref<boolean>(false);
interface CUploadFile<T = any> extends UploadFile<T> {
  code?: string;
}
const fileList = ref<CUploadFile[]>([]);
//表单验证
const validatorRules = {
  anlikId: [{ required: true, message: '请输入案例库!' }],
  name: [
    { required: true, message: '请输入案例名称!' },
    {
      validator: async (_rule: any, value: string) => {
        if (value) {
          const v = await nameExists(value, formData.id);
          if (v === true) {
            return Promise.reject('该案例名称已经存在');
          }
        }
        return Promise.resolve();
      },
      trigger: ['change'],
    },
  ],
  deptCode: [{ required: true, message: '请输入上传部门!' }],
  percode: [{ required: true, message: '请输入专案!' }],
  protypeId: [{ required: true, message: '请输入产品类型!' }],
  techtypePid: [{ required: true, message: '请输入属性类型!' }],
  techtypeSxmc: [{ required: true, message: '请输入子类型属性!' }],
  techtypeId: [{ required: true, message: '请输入子类型名称!' }],
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

/**
 *
 */
const techtypePidChange = (_v: any, option: any) => {
  techtypeCList.value = techtypeList.value?.filter((o) => o.pid == option.data.id);
};

/**
 * techtypeIdChanged
 */
const techtypeIdChanged = (v: any) => {
  if (v && !formData.id) {
    //没有id说明是新增，新增的时候才
    newCode(formData.techtypePid, v).then((va: any) => {
      formData.code = va;
    });
  }
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

async function getTTlist() {
  techtypeList.value = await getTechtypeList();
}

/**
 * 新增
 */
function add() {
  getTTlist();
  nextTick(() => {
    resetFields();
    typeInfoDisable.value = false;
    fileList.value = [];
    //赋值
    Object.assign(formData, {});
  });
}

/**
 * 编辑
 */
function edit(record) {
  getTTlist();
  getFileList(record?.id);
  nextTick(() => {
    resetFields();
    typeInfoDisable.value = true;
    //赋值
    Object.assign(formData, record);
  });
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
.ant-upload-text-info {
  color: blue;
  font-size: 12px;
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
