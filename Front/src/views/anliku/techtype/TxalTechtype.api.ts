import { defHttp } from "/@/utils/http/axios";
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/txal/txalTechtype/rootList',
  save='/txal/txalTechtype/add',
  edit='/txal/txalTechtype/edit',
  deleteTxalTechtype = '/txal/txalTechtype/delete',
  importExcel = '/txal/txalTechtype/importExcel',
  exportXls = '/txal/txalTechtype/exportXls',
  loadTreeData = '/txal/txalTechtype/loadTreeRoot',
  getChildList = '/txal/txalTechtype/childList',
  getChildListBatch = '/txal/txalTechtype/getChildListBatch',
}

/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入api
 * @param params
 */
export const getImportUrl = Api.importExcel;

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除
 * @param params
 * @param handleSuccess
 */
export const deleteTxalTechtype = (params,handleSuccess) => {
  return defHttp.delete({ url: Api.deleteTxalTechtype, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
}

/**
 * 批量删除
 * @param params
 * @param handleSuccess
 */
export const batchDeleteTxalTechtype = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteTxalTechtype, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    }
  });
}

/**
 * 保存或者更新
 * @param params
 * @param isUpdate
 */
export const saveOrUpdateDict = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params },{ isTransformResponse:false });
}

/**
 * 查询全部树形节点数据
 * @param params
 */
export const loadTreeData = (params) => defHttp.get({ url: Api.loadTreeData,params });

/**
 * 查询子节点数据
 * @param params
 */
export const getChildList = (params) => defHttp.get({ url: Api.getChildList, params });
  
/**
 * 批量查询子节点数据
 * @param params
 */
export const getChildListBatch = (params) => defHttp.get({ url: Api.getChildListBatch, params },{ isTransformResponse:false });
