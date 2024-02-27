import { defHttp, createAxios } from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";
import { ContentTypeEnum } from '/@/enums/httpEnum';
import { Url } from 'url';

const { createConfirm } = useMessage();

enum Api {
  orgtj = '/txal/txalLearnTj/org',
  usertj = '/txal/txalLearnTj/user',
  user10tj = '/txal/txalLearnTj/user10'
}


export const postOrgTj = (params) => defHttp.post({ url: Api.orgtj, params });
export const postUserTj = (params) => defHttp.post({ url: Api.usertj, params });
export const postUser10Tj = (params) => defHttp.post({ url: Api.user10tj, params });

