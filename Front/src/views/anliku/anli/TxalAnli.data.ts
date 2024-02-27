import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '案例库',
    align: "center",
    dataIndex: 'anlikId_dictText'
  },
  {
    title: '案例名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '上传部门',
    align: "center",
    dataIndex: 'deptCode_dictText'
  },
  {
    title: '专案',
    align: "center",
    dataIndex: 'percode'
  },
  {
    title: '产品类型',
    align: "center",
    dataIndex: 'protypeId_dictText'
  },
  {
    title: '属性类型',
    align: "center",
    dataIndex: 'techtypePid_dictText'
  },
  {
    title: '子类型属性',
    align: "center",
    dataIndex: 'techtypeSxmc'
  },
  {
    title: '子层后类型名称',
    align: "center",
    dataIndex: 'techtypeId_dictText'
  },
  {
    title: '关键词',
    align: "center",
    dataIndex: 'keywords'
  },
  {
    title: '案例编号',
    align: "center",
    dataIndex: 'code'
  },
  {
    title: '问题描述',
    align: "center",
    dataIndex: 'question'
  },
  {
    title: '根本原因',
    align: "center",
    dataIndex: 'reason'
  },
  {
    title: '改善措施',
    align: "center",
    dataIndex: 'solution'
  },
  {
    title: '评价',
    align: "center",
    dataIndex: 'evalt_dictText'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "案例名称",
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: "专案",
    field: 'percode',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: "关键词",
    field: 'keywords',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: "案例编号",
    field: 'code',
    component: 'Input',
    colProps: { span: 6 },
  },
];

//表单数据
// export const formSchema: FormSchema[] = [
//   {
//     label: '案例库',
//     field: 'anlikId',
//     component: 'Input',
//   },
//   {
//     label: '案例名称',
//     field: 'name',
//     component: 'Input',
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入案例名称!' },
//       ];
//     },
//   },
//   {
//     label: '上传部门',
//     field: 'deptCode',
//     component: 'JSelectDept',
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入上传部门!' },
//       ];
//     },
//   },
//   {
//     label: '专案',
//     field: 'percode',
//     component: 'Input',
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入专案!' },
//       ];
//     },
//   },
//   {
//     label: '产品类型',
//     field: 'protypeId',
//     component: 'JDictSelectTag',
//     componentProps: {
//       dictCode: "txal_protype,name,code"
//     },
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入产品类型!' },
//       ];
//     },
//   },
//   {
//     label: '属性类型',
//     field: 'techtypePid',
//     component: 'JDictSelectTag',
//     componentProps: {
//       dictCode: "txal_techtype where pid=0,sxmc,code"
//     },
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入属性类型!' },
//       ];
//     },
//   },
//   {
//     label: '子类型属性',
//     field: 'techtypeSxmc',
//     component: 'JDictSelectTag',
//     componentProps: {
//       dictCode: ""
//     },
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入子类型属性!' },
//       ];
//     },
//   },
//   {
//     label: '子类型名称',
//     field: 'techtypeId',
//     component: 'JDictSelectTag',
//     componentProps: {
//       dictCode: ""
//     },
//     dynamicRules: ({ model, schema }) => {
//       return [
//         { required: true, message: '请输入子类型名称!' },
//       ];
//     },
//   },
//   {
//     label: '关键词',
//     field: 'keywords',
//     component: 'Input',
//   },
//   {
//     label: '案例编号',
//     field: 'code',
//     component: 'Input',
//   },
//   {
//     label: '问题描述',
//     field: 'question',
//     component: 'InputTextArea',
//   },
//   {
//     label: '根本原因',
//     field: 'reason',
//     component: 'InputTextArea',
//   },
//   {
//     label: '改善措施',
//     field: 'solution',
//     component: 'InputTextArea',
//   },
//   {
//     label: '评价',
//     field: 'evalt',
//     component: 'JDictSelectTag',
//     componentProps: {
//       dictCode: "taxl_alievalt"
//     },
//   },
//   // TODO 主键隐藏字段，目前写死为ID
//   {
//     label: '',
//     field: 'id',
//     component: 'Input',
//     show: false,
//   },
// ];
