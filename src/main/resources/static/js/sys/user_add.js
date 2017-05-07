//用户ID
var userId = T.p("userId");
var vm = new Vue({
	el:'#rrapp',
	data:{
		title:"新增管理员",
		roleList:{},
		applicationList:[],
		user:{
			status:1,
			roleIdList:[],
			applicationId:-1
		},
		isUpdate : 0
	},
	created: function() {
		if(userId != null){
			this.title = "修改管理员";
			this.isUpdate = 1;
		}
		//获取角色信息
		this.getRoleList();
		this.getApplicationList();
    },
	methods: {
		getApplicationList: function () {
			$.get("../api/application/select",function (result) {
				vm.applicationList = result.applicationList;
				if(vm.isUpdate == 1){
					vm.getUser(userId);
				}
			});
		},
		getUser: function(userId){
			$.get("../api/user/info/"+userId, function(r){
				vm.user = r.user;
			});
		},
		getRoleList: function(){
			$.get("../api/role/select", function(r){
				vm.roleList = r.list;
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.user.userId == null ? "../api/user/save" : "../api/user/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.user),
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