$(function () {
    $("#jqGrid").jqGrid({
        url: '../api/proxy/list',
        datatype: "json",
        colModel: [			
			{ label: 'ip', name: 'ip', width: 70 },
			{ label: 'port', name: 'port', width: 50 },
			{ label: '成功次数', name: 'successfulTimes', width: 40 },
			{ label: '失败次数', name: 'failureTimes', width: 40 },
			{ label: '请求成功耗时', name: 'successfulTotalTime', width: 60 },
			{ label: '最近一次检测成功', name: 'lastSuccessfulTime', width: 60, formatter: function(value, options, row){
            				var date = new Date(value);
                            Y = date.getFullYear(),
                            m = date.getMonth() + 1,
                            d = date.getDate(),
                            H = date.getHours(),
                            i = date.getMinutes(),
                            s = date.getSeconds();
                            if (m < 10) {
                            m = '0' + m;
                            }
                            if (d < 10) {
                            d = '0' + d;
                            }
                            if (H < 10) {
                            H = '0' + H;
                            }
                            if (i < 10) {
                            i = '0' + i;
                            }
                            if (s < 10) {
                            s = '0' + s;
                            }
                            <!-- 获取时间格式 2017-01-03 10:13:48 -->
                             var t = Y+'-'+m+'-'+d+' '+H+':'+i+':'+s;
                            return t;
            			}}
        ],
		viewrecords: true,
        height: 400,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 40,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		
	},
	methods: {
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			
			location.href = "proxy_add.html?id="+id;
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../api/proxy/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.status == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.message);
						}
					}
				});
			});
		}
	}
});