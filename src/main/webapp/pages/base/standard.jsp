<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>取派标准</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript" src="../../js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="../../js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css" href="../../css/default.css">
<script type="text/javascript" src="../../js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript" src="../../js/easyui/ext/jquery.cookie.js"></script>
<script src="../../js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({
			visibility : "visible"
		});

		// 收派标准信息表格
		$('#grid').datagrid({
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			//定义页面上每页最大记录数的列表
			pageList : [ 3, 9, 20 ],
			//分页工具条和分页参数
			pagination : true,
			toolbar : toolbar,
			url : "../../standard_listPage.action",
			idField : 'id',
			columns : columns
		});

		//保存save的事件
		$("#save").click(function() {
			//先判断页面校验，是否都通过
			if ($("#standardForm").form("validate")) {
				//提交表单
				$("#standardForm").submit();
			}
		});
		//给添加或修改窗口添加事件或属性-初始化
		$("#standardWindow").window({
			onClose : function() {
				//$("#standardForm").form("reset");
				//$("input[name='id']").val("");
				$("#standardForm").form("clear");
			}
		});
	});

	//工具栏
	var toolbar = [ {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			//alert('增加');
			//弹出添加表单窗口
			$("#standardWindow").window("open");
		}
	}, {
		id : 'button-edit',
		text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			//获取选中的行
			var rows = $("#grid").datagrid("getSelections");
			if (rows.length != 1) {
				//不是选择一个
				$.messager.alert("提示", "只能选择一条记录！", "info");
				return;
			}
			//2。填充表单数据-回显
			$("#standardWindow").form("load", rows[0]);
			//弹出修改表单窗口
			$("#standardWindow").window("open");
		}
	}, 
	{
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : function() {
			//alert('作废');
			//获取当前选中行
			var rows = $('#grid').datagrid("getSelections");
			if (rows.length == 0) {
				//至少选择一个
				$.messager.alert("提示", "至少选择一条记录！", "info");
				return;
			}
			/* 			var temID="";
			      for(var i=0;i<rows.length;i++){
			        if (temID =="") {
			                    temID = rows[i].id;
			                } else {
			                    temID = rows[i].id + "," + temID;
			                }
			      } */
			//将id取出来，拼接为ids，逗号分割
			//定义临时数组
			var idArray = new Array();
			//循环行
			$(rows).each(function() {
				//id添加到数组
				idArray.push(this.id);
			});
			//将数组转换成字符串,逗号分割
			var ids = idArray.join();//默认英文逗号分割
			$.messager.confirm("提示", "您确认要选中的这些删除记录吗？", function(r) {
				if (r) {
					$.post("../../stabdard_delete.action", {
						"ids" : ids
					}, function(data) {
						if (data.result) {
							//成功
							$.messager.alert("恭喜", "作废记录成功！", "info");
						} else {
							//失败
							$.messager.alert("失败", "作废记录失败！", "error");
						}
						//刷新列表，使用reload来刷新可以保留到当前页
						$("#grid").datagrid("reload");
						//清除所有选中的行
						$("#grid").datagrid("clearSelections");
					});
				}

				//if(r){
				//批量删除
				//	location.href="../../standard_delete?temID=" + temID;

				//提交
				/* $.ajax({
				    type: "POST",
				    async: false,
				    url: "../../standard_delete?;
				    data: temID,
				    success: function (result) {
				        if (result.indexOf("t") <= 0) {
				            $('#dg').datagrid('clearSelections');
				            $.messager.alert("提示", "恭喜您，信息删除成功！", "info");
				            $('#dg').datagrid('reload');
				        } else {
				            $.messager.alert("提示", "删除失败，请重新操作！", "info");
				            return;
				        }
				    }   */
				//}
			});
		}
	}, {
		id : 'button-restore',
		text : '还原',
		iconCls : 'icon-save',
		handler : function() {
			alert('还原');
		}
	} ];

	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true
	}, {
		field : 'name',
		title : '标准名称',
		width : 120,
		align : 'center'
	}, {
		field : 'minWeight',
		title : '最小重量',
		width : 120,
		align : 'center'
	}, {
		field : 'maxWeight',
		title : '最大重量',
		width : 120,
		align : 'center'
	}, {
		field : 'minLength',
		title : '最小长度',
		width : 120,
		align : 'center'
	}, {
		field : 'maxLength',
		title : '最大长度',
		width : 120,
		align : 'center'
	}, {
		field : 'operator',
		title : '操作人',
		width : 120,
		align : 'center'
	}, {
		field : 'operatingTime',
		title : '操作时间',
		width : 120,
		align : 'center'
	}, {
		field : 'company',
		title : '操作单位',
		width : 120,
		align : 'center'
	} ] ];
</script>
</head>

<body class="easyui-layout" style="visibility: hidden;">
	
	<div region="center" border="false">
	<!-- property显示user中任意的属性的值 -->
	欢迎：<shiro:principal property="username"></shiro:principal>
		<shiro:hasPermission name="courier:add">
			<input/>
		</shiro:hasPermission>
		<table id="grid"></table>
	</div>

	<div class="easyui-window" title="对收派标准进行添加或者修改" id="standardWindow" collapsible="false" minimizable="false" maximizable="false" modal="true"
		closed="true" style="width: 600px; top: 50px; left: 200px">
		<div region="north" style="height: 31px; overflow: hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			</div>
		</div>

		<div region="center" style="overflow: auto; padding: 5px;" border="false">

			<form id="standardForm" action="../../standard_add.action" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派标准信息 <!--提供隐藏域 装载id --> <input type="hidden" name="id" />
						</td>
					</tr>
					<tr>
						<td>收派标准名称</td>
						<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true" /></td>
					</tr>
					<tr>
						<td>最小重量</td>
						<td><input type="text" name="minWeight" class="easyui-numberbox" required="true" /></td>
					</tr>
					<tr>
						<td>最大重量</td>
						<td><input type="text" name="maxWeight" class="easyui-numberbox" required="true" /></td>
					</tr>
					<tr>
						<td>最小长度</td>
						<td><input type="text" name="minLength" class="easyui-numberbox" required="true" /></td>
					</tr>
					<tr>
						<td>最大长度</td>
						<td><input type="text" name="maxLength" class="easyui-numberbox" required="true" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>