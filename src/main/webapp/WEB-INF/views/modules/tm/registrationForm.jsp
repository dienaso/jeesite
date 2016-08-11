<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商标注册评估管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).parent().parent().addClass("control-group hide");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tm/registration/">商标注册评估列表</a></li>
		<li class="active"><a
			href="${ctx}/tm/registration/form?id=${registration.id}">商标注册评估<shiro:hasPermission
					name="tm:registration:edit">${not empty registration.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="tm:registration:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="registration"
		action="${ctx}/tm/registration/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">注册人：</label>
			<div class="controls">
				<form:input path="applicantCn" htmlEscape="false" maxlength="200"
					class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">期望商标名称：</label>
			<div class="controls">
				<form:input path="tmName" htmlEscape="false" maxlength="100"
					class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">期望商标图形：</label>
			<div class="controls">
				<form:hidden id="tmImg" path="tmImg" htmlEscape="false"
					maxlength="300" class="input-xlarge" />
				<sys:ckfinder input="tmImg" type="images"
					uploadPath="/tm/registration" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">注册类别：</label>
			<div class="controls">
				<form:checkboxes path="intClsList"
					items="${fns:getDictList('tm_category')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">行业类型：</label>
			<div class="controls">
				<form:checkboxes path="businessTypeList"
					items="${fns:getDictList('tm_business')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">相似商标：</label>
			<div class="controls">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th>相似商标名</th>
							<th>相似商标图形</th>
							<th>商标状态</th>
							<th>注册类别</th>
							<th>注册人</th>
							<th>申请号/注册号</th>
							<th>代理公司</th>
							<th>相似度</th>
							<th>顾问建议</th>
							<shiro:hasPermission name="tm:registration:edit">
								<th width="10">&nbsp;</th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody id="similarList">
					</tbody>
				</table>
				<script type="text/template" id="similarTpl">//<!--
						<tr id="similarList{{idx}}">
							<td class="hide">
								<input id="similarList{{idx}}_id" name="similarList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="similarList{{idx}}_delFlag" name="similarList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="similarList{{idx}}_tmName" name="similarList[{{idx}}].tmName" type="text" value="{{row.tmName}}" class="input-small" readonly />
							</td>
							<td>
								<input id="similarList{{idx}}_tmImg" name="similarList[{{idx}}].tmImg" type="hidden" value="{{row.tmImg}}" />
								<img style="max-width:100px;max-height:100px;_height:100px;" src="{{row.tmImg}}">
							</td>
							<td>
								<input id="similarList{{idx}}_currentStatus" name="similarList[{{idx}}].currentStatus" type="text" value="{{row.currentStatus}}" class="input-small " readonly/>
							</td>
							<td>
								<input id="similarList{{idx}}_intCls" name="similarList[{{idx}}].intCls" type="text" value="{{row.intCls}}" class="input-mini " readonly />
							</td>
							<td>
								<input id="similarList{{idx}}_applicantCn" name="similarList[{{idx}}].applicantCn" type="text" value="{{row.applicantCn}}" class="input-large " readonly />
							</td>
							<td>
								<input id="similarList{{idx}}_regNo" name="similarList[{{idx}}].regNo" type="text" value="{{row.regNo}}" class="input-mini " readonly />
							</td>
							<td>
								<input id="similarList{{idx}}_agent" name="similarList[{{idx}}].agent" type="text" value="{{row.agent}}" class="input-large " readonly />
							</td>
							<td>
								<input id="similarList{{idx}}_similarity" name="similarList[{{idx}}].similarity" type="text" value="{{row.similarity}}" max=100 class="input-mini required"/>
								<span class="help-inline"><font color="red">%</font> </span>
							</td>
							<td>
								<textarea id="similarList{{idx}}_advise" name="similarList[{{idx}}].advise" rows="2" maxlength="300" class="input-large required">{{row.advise}}</textarea>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<shiro:hasPermission name="tm:registration:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#similarList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
				<script type="text/javascript">
						var similarRowIdx = 0, similarTpl = $("#similarTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(registration.similarList)};
							for (var i=0; i<data.length; i++){
								addRow('#similarList', similarRowIdx, similarTpl, data[i]);
								similarRowIdx = similarRowIdx + 1;
							}
						});
									
						function searchTm() {
							//删除全部已查询相似商标
							$("input[id$='delFlag']").each(function(obj){  
								$("#similarList"+obj+"_delFlag").val("1");
								$("#similarList"+obj+"_delFlag").parent().parent().addClass("control-group hide");
							});  
							
							//获取选中的注册类别
							var cls =[]; 
							$('input[name="intClsList"]:checked').each(function(){ 
								cls.push($(this).val()); 
							}); 
							var searchKey = $('input[name="tmName"]').val();
							if(searchKey == '' || cls.length == 0){
								alert("请填写注册类别和商标名称");
								return;
							}
							//$.ajaxSettings.async = false; 
							loading('查询中，请稍等...');
							$.getJSON("${ctx}/tm/registration/searchTm",{intCls:cls.join(';'),searchKey:searchKey},function(data){
								if(data.remainCount == 0){
									closeLoading();
									alert(data.msg);
								}
								if(data.allRecords == 0){
									alert('未找到相似商标!');
									closeLoading();
									return;
								}
								$.each(data.results, function(i, item) {
									addRow('#similarList', similarRowIdx, similarTpl, item);
									similarRowIdx = similarRowIdx + 1;
						        });
								closeLoading();
							});
									
						}
					</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">稳健型：</label>
			<div class="controls">
				<form:checkboxes path="safeList"
					items="${fns:getDictList('tm_category')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">平衡型：</label>
			<div class="controls">
				<form:checkboxes path="balanceList"
					items="${fns:getDictList('tm_category')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冲击型：</label>
			<div class="controls">
				<form:checkboxes path="radicalList"
					items="${fns:getDictList('tm_category')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="tm:registration:searchTm">
				<input id="btnSearch" class="btn btn-primary" onclick="searchTm();"
					value="查询相似商标" />&nbsp;</shiro:hasPermission>
			<shiro:hasPermission name="tm:registration:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>