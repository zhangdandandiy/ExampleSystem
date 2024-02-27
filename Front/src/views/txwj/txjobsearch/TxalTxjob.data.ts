import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '三阶文件名称',
    align: "center",
    dataIndex: 'fname'
  },
  {
    title: '三阶文件编号',
    align: "center",
    dataIndex: 'fcode'
  },
  {
    title: '三阶级文件最新版本',
    align: "center",
    dataIndex: 'fver'
  },
  {
    title: '归属程序名称',
    align: "center",
    dataIndex: 'pname'
  },
  {
    title: '作业办法关联部门',
    align: "center",
    dataIndex: 'orgs_dictText'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '三阶文件名称',
    field: 'fname',
    component: 'Input',
  },
  {
    label: '三阶文件编号',
    field: 'fcode',
    component: 'Input',
  },
  {
    label: '三阶级文件最新版本',
    field: 'fver',
    component: 'Input',
  },
  {
    label: '归属程序名称',
    field: 'pname',
    component: 'Input',
  },
  {
    label: '作业办法关联部门',
    field: 'orgs',
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
