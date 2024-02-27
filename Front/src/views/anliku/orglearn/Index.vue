<template>
  <div>
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container jeecg-basic-table-form-container-learnorg">
      <a-form ref="formRef" @keyup.enter.native="searchQuery" :model="queryParam" :label-col="labelCol"
        :wrapper-col="wrapperCol">
        <a-row :gutter="24">
          <a-col :lg="8">
            <a-form-item label="学习时间" name="learntime">
              <a-range-picker v-model:value="queryParam.learntime" value-format="YYYY-MM-DD" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :lg="8">
              <a-form-item label="案例名称" name="anliname">
                <a-input placeholder="请输入案例名称" v-model:value="queryParam.anliname"></a-input>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="专案" name="percode">
                <a-input placeholder="请输入专案" v-model:value="queryParam.percode"></a-input>
              </a-form-item>
            </a-col>

            <a-col :lg="8">
              <a-form-item label="关键词" name="keywords">
                <a-input placeholder="请输入关键词" v-model:value="queryParam.keywords"></a-input>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="案例编号" name="anlicode">
                <a-input placeholder="请输入案例编号" v-model:value="queryParam.anlicode"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
                <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset"
                  style="margin-left: 8px">重置</a-button>
                <!-- <a @click="toggleSearchStatus = !toggleSearchStatus" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <Icon :icon="toggleSearchStatus ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
                </a> -->
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="chart_container">
      <div ref="chartRef" :style="{ height, width }"></div>
    </div>
  </div>
</template>
<script lang="ts" name="txal-orglearn" setup>
import { ref, Ref, reactive, unref, onMounted } from 'vue';
import JInput from '/@/components/Form/src/jeecg/components/JInput.vue';
import { useTimeoutFn } from '/@/hooks/core/useTimeout';
import { useEventListener } from '/@/hooks/event/useEventListener';
import { useBreakpoint } from '/@/hooks/event/useBreakpoint';
import { useDebounceFn, tryOnUnmounted } from '@vueuse/core';
import { postOrgTj, postUser10Tj } from './Tj.api';
import echarts from '/@/utils/lib/echarts';
import type { EChartsOption } from 'echarts';
const chartRef = ref<HTMLDivElement | null>(null);
let resizeFn: Fn = chartresize;
let chartInstance: echarts.ECharts | null = null;
let removeResizeFn: Fn = () => {};
const height = ref<number | string>('100%');
const width = ref<number | string>('100%');

resizeFn = useDebounceFn(chartresize, 200);

const formRef = ref();
const queryParam = reactive<any>({});
const toggleSearchStatus = ref<boolean>(false);
const labelCol = reactive({
  xs: { span: 24 },
  sm: { span: 7 },
});
const wrapperCol = reactive({
  xs: { span: 24 },
  sm: { span: 16 },
});

async function loadData() {
  const [orgData, userData] = await Promise.all([postOrgTj(queryParam), postUser10Tj(queryParam)]);
  chartInstance?.setOption({
    xAxis: [
      {
        data: userData?.map((o) => o.name),
      },
    ],
    series: [
      {
        data: orgData?.map((o) => ({ value: o.total, name: o.name })),
      },
      {
        data: userData?.map((o) => o.total),
      },
    ],
  });
}

/**
 * 查询
 */
function searchQuery() {
  loadData();
}

/**
 * 重置
 */
function searchReset() {
  formRef.value.resetFields();
  //刷新数据
  loadData();
}

function initCharts() {
  const el = unref(chartRef);
  if (!el || !unref(el)) {
    return;
  }

  chartInstance = echarts.init(el);
  const { removeEvent } = useEventListener({
    el: window,
    name: 'resize',
    listener: resizeFn,
  });
  removeResizeFn = removeEvent;
  const { widthRef, screenEnum } = useBreakpoint();
  if (unref(widthRef) <= screenEnum.MD || el.offsetHeight === 0) {
    useTimeoutFn(() => {
      resizeFn();
    }, 30);
  }
}

function chartresize() {
  chartInstance?.resize();
}
onMounted(() => {
  initCharts();
  chartInstance?.setOption({
    backgroundColor: '#0f375f',
    title: [
      {
        text: '部门学习占比',
        left: '20%',
        top: '1%',
        textStyle: {
          color: '#fff',
          fontSize: 14,
        },
      },
      {
        text: '个人学习排名Top10',
        left: '70%',
        top: '1%',
        textStyle: {
          color: '#fff',
          fontSize: 14,
        },
      },
    ],
    grid: [{ left: '50%', top: '7%', width: '45%', height: '90%' }],
    tooltip: {
      formatter: '{b} ({c})',
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: true,
        // gridIndex: 0,
        // interval: 0,
        // data: yAxisData.reverse(),
        // axisTick: { show: false },
        // axisLabel: { show: true },
        // splitLine: { show: false },
        // axisLine: { show: true, lineStyle: { color: '#6173a3' } },
      },
    ],
    yAxis: [
      {
        gridIndex: 0,
        axisTick: { show: false },
        axisLabel: { show: true },
        splitLine: { show: false },
        axisLine: { show: true },
      },
    ],
    series: [
      {
        name: '部门学习占比',
        type: 'pie',
        radius: '30%',
        center: ['22%', '45%'],
        // data: [
        //   { value: 335, name: '客服电话' },
        //   { value: 310, name: '奥迪官网' },
        //   { value: 234, name: '媒体曝光' },
        //   { value: 135, name: '质检总局' },
        //   { value: 105, name: '其他' },
        // ],
        labelLine: { show: false },
        label: {
          show: true,
          formatter: '{b} \n ({d}%)',
          color: '#B1B9D3',
        },
      },
      {
        name: '个人学习排名Top10',
        type: 'bar',
        xAxisIndex: 0,
        yAxisIndex: 0,
        barWidth: '45%',
        itemStyle: { color: '#86c9f4' },
        label: { show: true, position: 'right', color: '#9EA7C4' },
        // data: dataAll.sort(),
      },
    ],
  });
});

tryOnUnmounted(() => {
  if (!chartInstance) return;
  removeResizeFn();
  chartInstance.dispose();
  chartInstance = null;
});
</script>
<style lang="less" scoped>
.jeecg-basic-table-form-container-learnorg {
  padding: 10px;

  .ant-form {
    padding: 12px 10px 6px 10px;
    margin-bottom: 8px;
    background-color: @component-background;
    border-radius: 2px;
  }

  .table-page-search-submitButtons {
    display: block;
    margin-bottom: 24px;
    white-space: nowrap;
  }
  .query-group-cust {
    width: calc(50% - 15px);
    min-width: 100px !important;
  }
  .query-group-split-cust {
    width: 30px;
    display: inline-block;
    text-align: center;
  }
}
.chart_container {
  max-width: 100% !important;
  height: 80vh;
  min-height: min-content;
  max-height: fit-content;
  background-color: @component-background;
}
</style>