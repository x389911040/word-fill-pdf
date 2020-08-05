/**
 * 打开弹框公共js
 * @param options
 *      tableId: 一个页面多次引入该组件，需要具体的tableId作为区分
 *      url: 请求数据路由，不可为空
 *      searchParams: 请求参数
 *      contentStr: 弹框内容
 *      checkDataMethod: 选择数据加入明细前校验方法
 *      assembleMethod: 组装明细数据方法，不可为空
 *      columnModel: 弹出层表格列定义
 *      btnShow: true,显示按钮；false,则为查看弹框， 默认true
 *      btn: ["确认", "取消"] 默认按钮
 */
var chooseItems  = function (options) {
    var tableId = "searchList";
    if (options.tableId) {
        tableId = options.tableId;
    }

    // 是否详情展示弹框标识
    var detailSee = false;
    if (options.btnShow == false) {
        detailSee = true;
    }

    // 获取子Frame内容
    function getbindDeviceStr() {
        var str = '';
        str += '<div class="main-content">';
        str += '<div class="box box-main">';
        str += '<div class="box-body">';

        // str += '<#form:form id="searchForm" model="${zbDeviceAccounts}" action="${ctx}/device/device_accounts/zbDeviceAccounts/listData" method="post" class="form-inline"\n' +
        //     '\t\t\t\t\tdata-page-no="${parameter.pageNo}" data-page-size="${parameter.pageSize}" data-order-by="${parameter.orderBy}">';
        str += '<div class="form-inline">';
        str += '<div class="form-group">';
        str += '<label class="control-label">设备编号：</label>';
        str += '<div class="control-inline">';
        str += '<input id="searchAccountsCode" maxlength="64" class="form-control width-120"/>';
        str += '</div>';
        str += '</div>';
        str += '<div class="form-group">';
        str += '<label class="control-label">设备名称：</label>';
        str += '<div class="control-inline">';
        str += '<input id="searchDeviceName" maxlength="64" class="form-control width-120"/>';
        str += '</div>';
        str += '</div>';
        str += '<div class="form-group">';
        str += '<button id="searchBtn" class="btn btn-primary btn-sm">查询</button>';
        str += '<button id="resetBtn" class="btn btn-default btn-sm">重置</button>';
        str += '</div>';
        str += '</div>';
        // str += '</#form:form>';

        // str += '<div class="row">';
        // str += '<div class="ml10 mr10">';
        str += '<table id="'+ tableId +'"></table>';
        str += '<div id="'+ tableId +'Page"></div>';

        // str += '</div>';
        // str += '</div>';
        str += '</div>';
        str += '</div>';
        str += '</div>';

        return str;
    }

    // 按钮
    var btn= ["确认", "取消"];
    if (options.btn) {
        btn = btn;
    }

    // 查看详情
    if (detailSee) {
        btn = ["返回"];
    }

    // 主页面内容
    var contentStr = '';
    if (options.contentStr) {
        contentStr = options.contentStr;
    } else {
        contentStr = getbindDeviceStr();
    }

    // 标题
    var title = "选择明细";
    if (options.title) {
        title = options.title;
    }

    // 查询数据参数
    var searchParams = {};
    if (options.searchParams) {
        searchParams = options.searchParams;
    }

    // 查询表格列渲染
    var columnModel = [];
    if (options.columnModel) {
        columnModel = options.columnModel();
    } else {
        columnModel = [
            {header:'主键', name:'id', hidden:true},
            {header:'设备编码', name:'accountsCode', width:150},
            {header:'设备名称', name:'deviceName', width:150},
            {header:'品牌', name:'brand', width:150},
            {header:'型号', name:'unitType', width:150},
            {header:'规格', name:'spec', width:150},
            {header:'供应商', name:'manufacturer', width:150},
            {header:'位置', name:'location', width:150, hidden: true}
        ];
    }

    layer.open({
        type: 1,
        area: ['80%', '80%'],
        title: [title, 'font-size:15px;font-style: oblique;color: #1890ff;border-bottom: 1px solid #ddd;font-weight: normal'],
        content: contentStr,
        btn: btn,
        btnAlign: 'c', //按钮居中
        shade: 0.1, //不显示遮罩
        yes: function (index, layero) {
            if (detailSee) {
                // 详情页 直接返回
                return ;
            }

            var checkDataArr = [];
            var checkRowIds = $("#" + tableId).dataGrid('getSelectRows');
            if (!checkRowIds || checkRowIds.length == 0) {
                layer.msg("请选择行");
                return false;
            }

            if (options.checkDataMethod) {
                options.checkDataMethod(checkRowIds);
            }

            checkRowIds.forEach(function (id, index, arr) {
                var checkData = $("#" + tableId).dataGrid('getRowData', id);
                checkDataArr.push(checkData);
            })

            options.assembleMethod(checkDataArr);
        },
        cancel: function (index, layero) {

        },
        success: function (index, layero) {

        }
    });

    // 初始化查询表格
    $("#" + tableId).dataGrid({
        url: options.url,
        postData: searchParams,
        page: 1,	// 当前页码
        rowNum: 10,
        showRownum: true,		// 显示行号
        showCheckbox: !detailSee,		// 显示复选框
        emptyDataHint: true,	// 无数据时显示
        autoGridHeight: function(){return 'auto'}, // 设置自动高度

        // 设置数据表格列
        columnModel: columnModel,

        // 加载成功后执行事件
        ajaxSuccess: function(data){
            // layer.msg("OK");
        }
    });

    // 查询按钮点击时间
    $("#searchBtn").click(function () {
        searchParams.accountsCode = $("#searchAccountsCode").val();
        searchParams.deviceName = $("#searchDeviceName").val();

        // $("#" + tableId).jqGrid("GridUnload");
        // // 重新渲染
        // tableDataGrid();

        $("#" + tableId).jqGrid('setGridParam', { postData: searchParams }).trigger('reloadGrid');
    });

    // 重置按钮点击时间
    $("#resetBtn").click(function () {
       $("#searchAccountsCode").val("");
       $("#searchDeviceName").val("");
    });
}