<!DOCTYPE html>
<html>
<head>
    <#include "../common.ftl">
</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <input name="lossId" type="hidden" value="${(lossId)!}"/>
    <input name="id" type="hidden" value="${(customerRep.id)!}"/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">暂缓措施</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName" lay-verify="required" name="measure"
                   id="measure" value="${(customerRep.measure)!}" placeholder="请输入计划项内容">
        </div>
    </div>
    <br/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">执行效果</label>
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-lg" lay-submit="" lay-filter="addOrUpdateCustomerRep">确认</button>
                <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript" src="/crm/js/customerLoss/customer.rep.add.update.js"></script>
</body>
</html>