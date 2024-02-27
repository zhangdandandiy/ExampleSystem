<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="antd-modal-form-txjob" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-row>
        <!-- <a-col> :xs="{ span: 24 }" :sm="{ span: 16,offset:8 }" -->
        <a-col :xs="{ span: 24 }" :sm="{ span: 16,offset:5 }">
          <a-form-item label="" :wrapperCol="{span: 24}">
            <a-upload :file-list="fileList" :max-count="1" :before-upload="beforeUpload" :disabled="disabled"
              accept=".zip">
              <a-button>
                <upload-outlined></upload-outlined>
                选择ZIP包
              </a-button>
            </a-upload>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="三阶文件名称" v-bind="validateInfos.fname">
            <a-input v-model:value="formData.fname" placeholder="请输入三阶文件名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="三阶文件编号" v-bind="validateInfos.fcode">
            <a-input v-model:value="formData.fcode" placeholder="请输入三阶文件编号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="三阶级文件最新版本" v-bind="validateInfos.fver">
            <a-input v-model:value="formData.fver" placeholder="请输入三阶级文件最新版本" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="归属程序名称" v-bind="validateInfos.pname">
            <a-input v-model:value="formData.pname" placeholder="请输入归属程序名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="作业办法关联部门" v-bind="validateInfos.orgs">
            <j-select-dept v-model:value="formData.orgs" :multiple="true" checkStrictly :disabled="disabled"
              rowKey="orgCode" />
          </a-form-item>
        </a-col>
        <a-col :span="24" v-if="docinfo!=null">
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
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
import { ref, reactive, defineExpose, nextTick, defineProps, computed, onMounted } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
import { getValueType } from '/@/utils';
import { saveOrUpdate, getDocList, download } from '../TxalTxjob.api';
import { Form, Modal, Upload, UploadFile, UploadProps } from 'ant-design-vue';
import { UploadOutlined, CloudDownloadOutlined, DeleteOutlined } from '@ant-design/icons-vue';
// import { Archive } from 'libarchive.js/main.js';
// import * as ArchiveWorker from 'libarchive.js/dist/worker-bundle.js?url';
// Archive.init({
//   workerUrl: ArchiveWorker.default,
// });
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
  fname: '',
  fcode: '',
  fver: '',
  pname: '',
  orgs: '',
});
const oldData: any = {};
const { createMessage } = useMessage();
const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
const confirmLoading = ref<boolean>(false);
//表单验证
const validatorRules = {
  fname: [{ required: true, message: '请输入三阶文件名称!' }],
  fcode: [{ required: true, message: '请输入三阶文件编号!' }],
  fver: [{ required: true, message: '请输入三阶级文件最新版本!' }],
  pname: [{ required: true, message: '请输入归属程序名称!' }],
  orgs: [{ required: true, message: '请输入作业办法关联部门!' }],
};
const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: true });
const fileList = ref<UploadFile[]>([]);
const docinfo = ref<any>();

const beforeUpload: UploadProps['beforeUpload'] = async (file) => {
  if (file.type !== 'application/x-zip-compressed' && !file.name?.toLowerCase().endsWith('.zip')) {
    createMessage.info('请选择ZIP格式的压缩包');
    return Upload.LIST_IGNORE;
  }
  // if (!formData.techtypePid || !formData.techtypeId || !formData.code) {
  //   createMessage.info('请先维护上方的基础数据，再上传数据');
  //   return Upload.LIST_IGNORE;
  // }
  // if (fileList.value?.find((o) => o.name == file.name)) {
  //   createMessage.info('已经存在同名文件' + file.name);
  //   return Upload.LIST_IGNORE;
  // }
  const nf: UploadFile = file;
  const fname = nf.name;
  if (fname) {
    const sindex = fname.lastIndexOf('-') + 4;
    const eindx = fname.indexOf('Rev');
    if (sindex > -1 && eindx > sindex) {
      formData.fcode = fname.substring(0, sindex);
      formData.fname = fname.substring(sindex, eindx);
      formData.fver = fname.substring(eindx, fname.lastIndexOf('.'));
    }
  }
  // console.log('obj2', '=======');
  // const archive = await Archive.open(file);
  // let obj = await archive.extractFiles();
  // console.log('obj', obj);
  fileList.value = [nf];
  return false;
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

// const removefile = (file) => {
//   // if (fileList.value) {
//   //   const index = fileList.value.indexOf(file);
//   //   const newFileList = fileList.value.slice();
//   //   newFileList.splice(index, 1);
//   //   fileList.value = newFileList;
//   // }
// };

/**
 * 新增
 */
function add() {
  edit({});
}

/**
 * 编辑
 */
function edit(record) {
  getFileList(record?.id);
  nextTick(() => {
    resetFields();
    //赋值
    Object.assign(formData, record);
    fileList.value = [];
    Object.assign(oldData, record);
  });
}

async function getFileList(id) {
  if (!id) {
    docinfo.value = null;
    return;
  }
  const list = await getDocList(id);
  const obj: any = list?.find((o: any) => o.code === 'jobdoc');
  docinfo.value = { uid: obj.id, code: obj.code, name: obj.name, status: 'done', url: `/txal/txalDoclink/download/${obj.id}` };
}

/**
 * 提交数据
 */
async function submitForm() {
  // 触发表单验证
  await validate();
  if (formData.id && (formData.fname !== oldData.fname || formData.fcode !== oldData.fcode || formData.fver !== oldData.fver)) {
    if (!fileList.value || fileList.value.length < 1) {
      createMessage.warning('三阶文件发生了变化，请先上传文件');
      return;
    }
    const modal = Modal.confirm({
      title: '询问',
      content: '三阶文件发生了变化，继续操作将新增该作业并不会覆盖原作业内容，是否继续？',
      onOk: () => {
        modal.destroy();
        formData.id = '';
        saveData();
      },
    });
    return;
  }
  saveData();
}

async function saveData() {
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
  let fData;
  if (isUpdate.value) {
    fData = model;
  } else {
    fData = new window.FormData();
    fData.append('data', JSON.stringify(model));
    // fData.append('file[]', fileList.value);
    fileList.value?.forEach((file: UploadProps['fileList'][number]) => {
      if (!file.status) {
        fData.append('file', file as any);
      }
    });
  }

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
.antd-modal-form-txjob {
  min-height: 500px !important;
  overflow-y: auto;
  padding: 24px 24px 24px 24px;
}
</style>
