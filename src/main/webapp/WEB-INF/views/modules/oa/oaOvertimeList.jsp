<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加班申请管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/oaOvertime/">加班申请列表</a></li>
		<shiro:hasPermission name="oa:oaOvertime:edit"><li><a href="${ctx}/oa/oaOvertime/form">加班申请添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="oaOvertime" action="${ctx}/oa/oaOvertime/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请人：</label>
				<sys:treeselect id="user" name="user.id" value="${oaOvertime.user.id}" labelName="user.name" labelValue="${oaOvertime.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>归属部门：</label>
				<sys:treeselect id="office" name="office.id" value="${oaOvertime.office.id}" labelName="office.name" labelValue="${oaOvertime.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>加班理由：</label>
				<form:input path="reason" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>申请时间：</label>
				<input name="beginApplyTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${oaOvertime.beginApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endApplyTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${oaOvertime.endApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>申请人</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>申请时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="oa:oaOvertime:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaOvertime">
			<tr>
				<td><a href="${ctx}/oa/oaOvertime/form?id=${oaOvertime.id}">
					${oaOvertime.user.name}
				</a></td>
				<td>
					<fmt:formatDate value="${oaOvertime.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${oaOvertime.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${oaOvertime.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${oaOvertime.remarks}
				</td>
				<shiro:hasPermission name="oa:oaOvertime:edit"><td>
    				<a href="${ctx}/oa/oaOvertime/form?id=${oaOvertime.id}">修改</a>
					<a href="${ctx}/oa/oaOvertime/delete?id=${oaOvertime.id}" onclick="return confirmx('确认要删除该加班申请吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>