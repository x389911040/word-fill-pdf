/**
 * 自定义公共js组件
 */

/* 获取打印单据弹框 */
function getPrintLoading() {
    var str = '';
    str += '<div class="batch-into">';
    str += '	<ul style="margin-top:46px">';
    str += '		<li style="text-align:center">';
    str += '			<img src="/js/static/images/load.gif" width="60px" height="60px">';
    str += '		</li>';
    str += '		<li style="text-align:center; padding-top: 26px;">';
    str += '			<span class="warn-info" style="width: 200px; font-size: 24px; color:red; font-family: inherit;" id="print_info">正在加载数据，请稍后</span>';
    str += '		</li>';
    str += '	</ul>';
    str += '</div>';

    var printLoading = layer.open({
        type:0,
        area: ['500px','300px'],
        title: ['温馨提示', 'font-size:16px;height:40px;line-height:40px;background:#fff;border-top:3px solid #3a90b8;border-bottom:1px solid #e0e0e0;top:250px'],
        content: str,
        btn:[],
        shade:0.1,
        success:function(){
            $(".batch-into").parents(".layui-layer-content").next().hide();
        }
    });

    return printLoading;
}

/**
 * 将日期时间对象转为指定格式的字符串
 * @param fmt
 * @returns {*}
 * @constructor
 */
/**
 * 将日期时间对象转为指定格式的字符串
 */
function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "H+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S": date.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    return fmt;
}