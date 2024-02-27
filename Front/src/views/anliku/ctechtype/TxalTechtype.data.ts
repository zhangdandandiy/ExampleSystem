import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '产品类型',
    align: 'center',
    dataIndex: 'protypeId_dictText'
  },
  {
    title: '属性编码',
    align: 'left',
    dataIndex: 'code'
  },
  {
    title: '属性类型',
    align: 'center',
    dataIndex: 'sxmc'
  },
  {
    title: '类型名称',
    align: 'center',
    dataIndex: 'name'
  },
  {
    title: '开始流水号',
    align: 'center',
    dataIndex: 'minidx'
  },
  {
    title: '结束流水号',
    align: 'center',
    dataIndex: 'maxidx'
  },
  {
    title: '备注',
    align: 'center',
    dataIndex: 'remark'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "属性编码",
    field: "code",
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: "属性类型",
    field: "sxmc",
    component: 'JInput',
    colProps: { span: 6 },
  },
  {
    label: "类型名称",
    field: "name",
    component: 'JInput',
    colProps: { span: 6 },
  },
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '产品类型',
    field: 'protypeId',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "txal_protype,name,id"
    },
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入产品类型!' },
      ];
    },
  },
  {
    label: '父级节点',
    field: 'pid',
    component: 'JTreeSelect',
    componentProps: {
      dict: "txal_techtype,code,id",
      pidField: "pid",
      pidValue: "0",
      hasChildField: "has_child",
    },
  },
  {
    label: '属性编码',
    field: 'code',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入属性编码!' },
      ];
    },
  },
  {
    label: '属性类型',
    field: 'sxmc',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入属性类型!' },
      ];
    },
  },
  {
    label: '类型名称',
    field: 'name',
    component: 'Input',
  },
  {
    label: '开始流水号',
    field: 'minidx',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入开始流水号!' },
      ];
    },
  },
  {
    label: '结束流水号',
    field: 'maxidx',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入结束流水号!' },
      ];
    },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
  },
  // TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];
