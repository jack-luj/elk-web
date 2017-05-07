var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};
var ztree;

//角色ID
var roleId = T.p("roleId");
var vm = new Vue({
	el:'#rrapp',
	data:{
		title:"新增角色",
		role:{}
	},
	created: function() {
		//加载菜单树
		$.get("../api/menu/perms", function(r){
			ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
			//展开所有节点
			ztree.expandAll(true);
			
			if(roleId != null){
				vm.title = "修改角色";
				vm.getRole(roleId);
			}
		});
    },
	methods: {
		getRole: function(roleId){
            $.get("../api/role/info/"+roleId, function(r){
            	vm.role = r.role;
                
                //勾选角色所拥有的菜单
    			var menuIds = vm.role.menuIdList;
    			for(var i=0; i<menuIds.length; i++) {
    				var node = ztree.getNodeByParam("menuId", menuIds[i]);
    				ztree.checkNode(node, true, false);
    			}
    		});
		},
		saveOrUpdate: function (event) {
			//获取选择的菜单
			var nodes = ztree.getCheckedNodes(true);
			var menuIdList = new Array();
			for(var i=0; i<nodes.length; i++) {
				menuIdList.push(nodes[i].menuId);
			}
			vm.role.menuIdList = menuIdList;
			
			var url = vm.role.roleId == null ? "../api/role/save" : "../api/role/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.role),
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