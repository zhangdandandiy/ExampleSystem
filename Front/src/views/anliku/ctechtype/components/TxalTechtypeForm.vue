<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-row>
        <a-col :span="24">
          <a-form-item label="产品类型" v-bind="validateInfos.protypeId">
            <j-dict-select-tag v-model:value="formData.protypeId" dictCode="txal_protype,name,id" placeholder="请选择产品类型"
              :disabled="disabled" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="父级节点" v-bind="validateInfos.pid">
            <j-tree-select placeholder="请选择父级节点" v-model:value="formData.pid" dict="txal_techtype,code,id"
              pidField="pid" pidValue="0" hasChildField="has_child" :disabled="disabled">
            </j-tree-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="属性编码" v-bind="validateInfos.code">
            <a-input v-model:value="formData.code" placeholder="请输入属性编码" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="属性类型" v-bind="validateInfos.sxmc">
            <a-input v-model:value="formData.sxmc" placeholder="请输入属性类型" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="类型名称" v-bind="validateInfos.name">
            <a-input v-model:value="formData.name" placeholder="请输入类型名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开始流水号" v-bind="validateInfos.minidx">
            <a-input v-model:value="formData.minidx" placeholder="请输入开始流水号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="结束流水号" v-bind="validateInfos.maxidx">
            <a-input v-model:value="formData.maxidx" placeholder="请输入结束流水号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" v-bind="validateInfos.remark">
            <a-input v-model:value="formData.remark" placeholder="请输入备注" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
import { ref, reactive, defineExpose, nextTick, unref, defineProps, computed, onMounted } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
import JTreeSelect from '/@/components/Form/src/jeecg/components/JTreeSelect.vue';
import { getValueType } from '/@/utils';
import { loadTreeData, saveOrUpdateDict } from '../TxalTechtype.api';
import { Form } from 'ant-design-vue';

const useForm = Form.useForm;
const formRef = ref();
const isUpdate = ref(true);
const expandedRowKeys = ref([]);
const treeData = ref([]);
const pidField = ref<string>('pid');
const emit = defineEmits(['register', 'ok']);
let model: Nullable<Recordable> = null;
const formData = reactive<Record<string, any>>({
  id: '',
  protypeId: '',
  pid: '',
  code: '',
  sxmc: '',
  name: '',
  minidx: '001',
  maxidx: '999',
  remark: '',
});
const { createMessage } = useMessage();
const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
const confirmLoading = ref<boolean>(false);
//表单验证
const validatorRules = {
  protypeId: [{ required: true, message: '请输入产品类型!' }],
  code: [{ required: true, message: '请输入属性编码!' }],
  sxmc: [{ required: true, message: '请输入属性类型!' }],
  minidx: [{ required: true, message: '请输入开始流水号!' }],
  maxidx: [{ required: true, message: '请输入结束流水号!' }],
};
const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: true });
const props = defineProps({
  formDisabled: { type: Boolean, default: false },
  formData: { type: Object, default: () => {} },
  formBpm: { type: Boolean, default: true },
});

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
function add(obj = {}) {
  edit(obj);
}

/**
 * 编辑
 */
function edit(record) {
  nextTick(async () => {
    resetFields();
    expandedRowKeys.value = [];
    treeData.value = await loadTreeData({ async: false, pcode: '' });
    //赋值
    Object.assign(formData, record);
    model = record;
  });
}

/**
 * 根据pid获取展开的节点
 * @param pid
 * @param arr
 */
function getExpandKeysByPid(pid, arr) {
  if (pid && arr && arr.length > 0) {
    for (let i = 0; i < arr.length; i++) {
      if (arr[i].key == pid && unref(expandedRowKeys).indexOf(pid) < 0) {
        expandedRowKeys.value.push(arr[i].key);
        getExpandKeysByPid(arr[i]['parentId'], unref(treeData));
      } else {
        getExpandKeysByPid(pid, arr[i].children);
      }
    }
  }
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
  if (formData.id) {
    isUpdate.value = true;
  }
  //循环数据
  for (let data in formData) {
    //如果该数据是数组并且是字符串类型
    if (formData[data] instanceof Array) {
      let valueType = getValueType(formRef.value.getProps, data);
      //如果是字符串类型的需要变成以逗号分割的字符串
      if (valueType === 'string') {
        formData[data] = formData[data].join(',');
      }
    }
  }
  await saveOrUpdateDict(formData, isUpdate.value)
    .then(async (res) => {
      if (res.success) {
        await getExpandKeysByPid(formData['pid'], unref(treeData));
        emit('ok', {
          isUpdate: unref(isUpdate),
          values: { ...formData },
          expandedArr: unref(expandedRowKeys).reverse(),
          // 是否更改了父级节点
          changeParent: model != null && model['pid'] != formData['pid'],
        });
        createMessage.success(res.message);
      } else {
        createMessage.warning(res.message);
      }
    })
    .finally(() => {
      confirmLoading.value = false;
    });
}

/**
 * 值改变事件触发
 * @param key
 * @param value
 */
function handleFormChange(key, value) {
  formData[key] = value;
}

defineExpose({
  add,
  edit,
  submitForm,
});
</script>

<style lang="less" scoped>
.antd-modal-form {
  min-height: 500px !important;
  overflow-y: auto;
  padding: 24px 24px 24px 24px;
}
</style>
