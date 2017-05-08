var id = T.p("id");
var vm = new Vue({
	el:'#rrapp',
	data:{
		title:"新增",
		proxy:{}
	},
	created: function() {
		if(id != null){
			this.title = "修改";
			this.getInfo(id)
		}
    },
	methods: {
		getInfo: function(id){
			$.get("../api/proxy/info/"+id, function(r){
                vm.proxy = r.proxy;
            });
		},
		saveOrUpdate: function (event) {
			var url = vm.proxy.id == null ? "../api/proxy/save" : "../api/proxy/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.proxy),
			    success: function(r){
			    	if(r.status === 0){
						alert('操作成功', function(index){
							vm.back();
						});
					}else{
						alert(r.message);
					}
				}
			});
		},
		back: function (event) {
			history.go(-1);
		}
	}
});