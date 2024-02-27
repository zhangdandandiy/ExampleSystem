import { defHttp, createAxios } from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";
import { ContentTypeEnum } from '/@/enums/httpEnum';
import { Url } from 'url';

const { createConfirm } = useMessage();

enum Api {
  list = '/txal/txalAnli/list',
  save = '/txal/txalAnli/add',
  edit = '/txal/txalAnli/edit',
  deleteOne = '/txal/txalAnli/delete',
  deleteBatch = '/txal/txalAnli/deleteBatch',
  importExcel = '/txal/txalAnli/importExcel',
  exportXls = '/txal/txalAnli/exportXls',
  techtypeList = '/txal/txalTechtype',
  newCode = '/txal/txalAnli',
  doclist = '/txal/txalDoclink/query',
  learnAdd = '/txal/txalLearninfo/add',
  downloadAsPdf = '/txal/txalDoclink/download/pdf'
}


export const getTechtypeList = () => defHttp.get({ url: `${Api.techtypeList}` });
export const newCode = (pTTCode, ttCode) => defHttp.get({ url: `${Api.newCode}/${pTTCode}/${ttCode}` });
export const getDocList = (id) => defHttp.get({ url: `${Api.doclist}/txal_anli/${id}` });


/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除单个
 * @param params
 * @param handleSuccess
 */
export const deleteOne = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteOne, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
}

/**
 * 批量删除
 * @param params
 * @param handleSuccess
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
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
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  // defHttp.uploadFile({ url: url }, params);
  // return defHttp.uploadFiles(url,params);
  return defHttp.post({ url: url, params }, { isTransformResponse: false });
}


export const download = async (url, fileName) => {
  const data = await defHttp.get(
    {
      url: url,
      responseType: 'blob',
    },
    { isTransformResponse: false }
  );
  let aurl = window.URL.createObjectURL(new Blob([data]));
  let link = document.createElement('a');
  link.style.display = 'none';
  link.href = aurl;
  link.setAttribute('download', fileName);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link); //下载完成移除元素
  window.URL.revokeObjectURL(aurl); //释放掉blob对象
}

export const viewAsPdf = async (fileid) => {
  const sleep = (time) => {
    return new Promise((resolve) => setTimeout(resolve, time));
  };
  let data;
  let icount = 0;
  while (!data && icount++ < 10) {
    try {
      data = await defHttp.get(
        {
          url: `${Api.downloadAsPdf}/${fileid}`,
          responseType: 'blob',
        },
        { isTransformResponse: false }
      );
      if (!data) {
        await sleep(5000);
      }
    } catch {
      await sleep(5000);
    }
  }
  return window.URL.createObjectURL(new Blob([data]));
}

export const recordLearn = async (info) => {
  return defHttp.post({ url: Api.learnAdd, params: info }, { isTransformResponse: false });
}
