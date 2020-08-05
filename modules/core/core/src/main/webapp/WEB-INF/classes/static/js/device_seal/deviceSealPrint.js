/**
 * 提供页面调用
 */
var myPrint = function (params){
    var data = params.data;
    var loading = params.loading;
    $("#print_info").text("即将打印单据，请稍后...");

    var length = data.length;
    data.forEach(function(item,k){
        $("#print_info").text("即将打印第" + (k+1) + "张单据，共" + length + "张单据，请稍后...");
        var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
        if (!LODOP) {
            console.error("本地未安装打印控件");
            loading && layer.close(loading);
            return;
        }
        var flag = LODOP.PRINT_INIT("");
        console.log(k,flag);
        LODOP.SET_PRINT_PAGESIZE(1, "240mm","140mm", "");//设置打印纸张大小
        LODOP.SET_PRINT_STYLE("FontSize",12);//设置打印项风格，参数为风格名，值
        createPrintPage(item, LODOP);

        LODOP.SET_SHOW_MODE("NP_NO_RESULT",true);//设置NP插件无返回，这可避免chrome对弹窗超时误报崩溃

        LODOP.PREVIEW();
        // LODOP.PRINT();
        console.log("第个单据" + (k+1) + "打印完成", dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        LODOP = null;
    });

    loading && layer.close(loading);
};
/**
 * 构造采购单打印页
 */
var createPrintPage = function (order, LODOP) {
    LODOP.NewPage();
    LODOP.ADD_PRINT_TEXT("0mm","0mm","200mm","20mm","西北核技术研究所\n启封单");
    LODOP.SET_PRINT_STYLEA(0,"Alignment",2);//对齐方式
    LODOP.SET_PRINT_STYLEA(0,"FontSize",20);//对齐方式
    LODOP.SET_PRINT_STYLEA(0,"Bold",1);//对齐方式

    //LODOP.ADD_PRINT_TEXT("12mm","145mm","25mm","20mm","页码：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    //LODOP.ADD_PRINT_TEXT("12mm","165mm","60mm","20mm",util.toDateString(new Date().getTime(), 'yyyy年MM月dd'));

    //配入门店
    LODOP.ADD_PRINT_TEXT("20mm","5mm","30mm","20mm","单据编号：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    LODOP.ADD_PRINT_TEXT("20mm","25mm","60mm","20mm",order.billCode);
    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //单据编号
    LODOP.ADD_PRINT_TEXT("20mm","75mm","25mm","20mm","申请日期：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    LODOP.ADD_PRINT_TEXT("20mm","100mm","40mm","20mm", dateFormat(new Date(order.createDate), "yyyy-MM-dd"));
    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //审核日期
    // LODOP.ADD_PRINT_TEXT("20mm","135mm","30mm","20mm","审核日期");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    // if (allocation.outTime) {
    //     LODOP.ADD_PRINT_TEXT("20mm","155mm","70mm","20mm",util.toDateString(allocation.outTime, 'yyyy年MM月dd'));
    // } else {
    //     LODOP.ADD_PRINT_TEXT("20mm","155mm","70mm","20mm","");
    // }
    // LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    LODOP.ADD_PRINT_TEXT("20mm","140mm","25mm","20mm","单据状态：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    var stateStr = "";
    if (order.state == 0) {
        stateStr = "待提交";
    } else if (order.state == 1) {
        stateStr = "待审核";
    } else {
        stateStr = "已审核";
    }
    LODOP.ADD_PRINT_TEXT("20mm","165mm","30mm","20mm", stateStr);
    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //创建人
    LODOP.ADD_PRINT_TEXT("26mm","5mm","30mm","20mm","归属单位：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    LODOP.ADD_PRINT_TEXT("26mm","25mm","60mm","20mm",order.createDeptName);

    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //配出机构
    LODOP.ADD_PRINT_TEXT("26mm","75mm","25mm","20mm","责任人：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    LODOP.ADD_PRINT_TEXT("26mm","100mm","40mm","20mm",order.createByName);
    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //创建日期
    // LODOP.ADD_PRINT_TEXT("26mm","135mm","30mm","20mm","创建日期");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    // LODOP.ADD_PRINT_TEXT("26mm","155mm","70mm","20mm",util.toDateString(allocation.createTime, 'yyyy年MM月dd'));
    // LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //出库审核人

    // if (allocation.auditOutPersonName) {
    //     LODOP.ADD_PRINT_TEXT("32mm","25mm","60mm","20mm",allocation.auditOutPersonName);
    // } else {
    //     LODOP.ADD_PRINT_TEXT("32mm","25mm","60mm","20mm","");
    // }j

    //
    // //入库审核人
    LODOP.ADD_PRINT_TEXT("32mm","5mm","30mm","20mm","备注信息：");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度
    // if (allocation.auditInPersonName) {
    //     LODOP.ADD_PRINT_TEXT("32mm","85mm","60mm","20mm",allocation.auditInPersonName);
    // } else {
    //     LODOP.ADD_PRINT_TEXT("32mm","85mm","60mm","20mm","");
    // }
    LODOP.ADD_PRINT_TEXT("32mm","35mm","60mm","20mm",order.remarks);
    LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);

    //备注
    // LODOP.ADD_PRINT_TEXT("32mm","135mm","30mm","20mm","单据状态");//纸张内上边距、纸张内左边距、打印区域宽度，打印区域高度//transState
    // if (allocation.transState == 0) {
    //     LODOP.ADD_PRINT_TEXT("32mm","155mm","70mm","20mm","出库待审核");
    // } else if(allocation.transState == 1) {
    //     LODOP.ADD_PRINT_TEXT("32mm","155mm","70mm","20mm","入库待审核");
    // } else {
    //     LODOP.ADD_PRINT_TEXT("32mm","155mm","70mm","20mm","已完成");
    // }
    //
    // LODOP.SET_PRINT_STYLEA(0,"TextFrame",2);
    var str = getPrintTable(order);
    //明细
    // LODOP.ADD_PRINT_TEXT("42mm","5mm","25mm","20mm", "单据明细：");

    LODOP.ADD_PRINT_TABLE("42mm","5mm", "90%","100%",str);
    LODOP.SET_PRINT_STYLEA(0,"Offset2Top","-35mm");//对齐方式
    LODOP.SET_PRINT_STYLEA(0,"Alignment",2);//对齐方式
};
/**
 * 构造采购单打印table
 */
var getPrintTable = function(order){
    var str = '<div style="height:0;overflow:hidden;" class="print">';
    str += '';
    str += '<table border=1 cellSpacing=0 cellPadding=1 style="border-collapse:collapse" bordercolor="#333333">';
    str += '	<tr style="" class="title">';
    str += '		<th width="50px" style="font-size: 14px;">序号</th>';
    str += '		<th width="80px" style="font-size: 14px;">设备编码</th>';
    str += '		<th width="120px" style="font-size: 14px;">设备名称</th>';
    str += '		<th width="120px" style="font-size: 14px;">型号</th>';
    str += '		<th width="150px" style="font-size: 14px;">规格</th>';
    str += '		<th width="220px" style="font-size: 14px;">备注信息</th>';
    str += '	</tr>';
    //border:1px

    var items = order.zbDeviceSealItemList ||[];

    // var numTotal = 0;

    items.forEach(function(item,k){
        // var barCode = "";
        // var unitName = "";
        // var goodsUnits = item.goodsUnits;
        // goodsUnits.forEach(function(goodsUnit){
        //     if (goodsUnit.isShow == 1) {
        //         barCode = goodsUnit.barCode;
        //         unitName = goodsUnit.unitName;
        //     }
        // });

        str += '	<tr style="" class="title">';
        str += '		<td style="text-align:center;font-size: 14px;">' + (k+1) + '</td>';
        str += '		<td style="text-align:center;font-size: 14px;">' + item.accountsCode + '</td>';
        str += '		<td style="text-align:center;font-size: 14px;">' + item.deviceName + '</td>';
        str += '		<td style="text-align:center;font-size: 14px;">' + item.unitType + '</td>';
        str += '		<td style="text-align:center;font-size: 14px;">' + item.spec + '</td>';
        str += '		<td style="text-align:center;font-size: 14px;">' + item.remarks + '</td>';
    });

    // str += '	<tr style="" class="title">';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;"></td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;">合计</td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;"></td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;"></td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;"></td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;">'+numTotal+'</td>';
    // str += '		<td style="border:0px;text-align:center;font-size: 14px;"></td>';
    // str += '	</tr>';
    str += '</table>';
    str += '</div>';

    return str;
    //$("body").append(str);
};