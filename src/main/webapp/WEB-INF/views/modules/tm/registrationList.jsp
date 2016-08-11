<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商标注册评估管理</title>
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
		<li class="active"><a href="${ctx}/tm/registration/">商标注册评估列表</a></li>
		<shiro:hasPermission name="tm:registration:edit"><li><a href="${ctx}/tm/registration/form">商标注册评估添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="registration" action="${ctx}/tm/registration/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>报告编号：</label>
				<form:input path="reportNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>注册人：</label>
				<form:input path="applicantCn" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>商标名称：</label>
				<form:input path="tmName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>报告编号</th>
				<th>期望商标名称</th>
				<th>期望商标图形</th>
				<th>注册人</th>
				<th>注册类别</th>
				<th>行业类型</th>
				<th>生成次数</th>
				<th>创建人</th>
				<th>所属部门</th>
				<th>更新时间</th>
				<shiro:hasPermission name="tm:registration:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="registration">
			<tr>
				<td><a href="${ctx}/tm/registration/form?id=${registration.id}">
					${registration.reportNo}
				</a></td>
				<td>
					${registration.tmName}
				</td>
				<td>
					<img style="max-width:100px;max-height:100px;_height:100px;" src="${registration.tmImg}">
				</td>
				<td>
					${registration.applicantCn}
				</td>
				<td>
					${fns:getDictLabels(registration.intCls, 'tm_category', '')}
				</td>
				<td>
					${fns:getDictLabels(registration.businessType, 'tm_business', '')}
				</td>
				<td>
					${registration.genTimes}
				</td>
				<td>
					${registration.createBy.name}
				</td>
				<td>
					${registration.createBy.office.name}
				</td>
				<td>
					<fmt:formatDate value="${registration.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="tm:registration:edit"><td>
    				<a href="${ctx}/tm/registration/form?id=${registration.id}">修改</a>
    				<shiro:hasPermission name="tm:registration:generateReports">
    					<c:if test="${(registration.genTimes == 0)}">
	    					<a href="${ctx}/tm/registration/generateReports?id=${registration.id}" onclick="return confirmx('确认要生成商标注册评估报告吗？', this.href)">生成报告</a>
	    				</c:if>
	    				<c:if test="${(registration.genTimes > 0)}">
							<a target="_blank" href="${pageContext.request.contextPath}${fns:getFrontPath()}/tm/report/index-${registration.id}${urlSuffix}">查看报告</a>
	    					<a href="${ctx}/tm/registration/download?id=${registration.id}">下载报告</a>
	    					<a href="${ctx}/tm/registration/generateReports?id=${registration.id}" onclick="return confirmx('确认要重新生成商标注册评估报告吗？', this.href)">重新生成报告</a>
						</c:if>
					</shiro:hasPermission>
					<a href="${ctx}/tm/registration/delete?id=${registration.id}" onclick="return confirmx('确认要删除该商标注册评估吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>