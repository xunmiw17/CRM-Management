<!DOCTYPE html>
<html>
<head>
    <#include "../common.ftl">
</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">菜单名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="moduleName" id="moduleName" value="${(module.moduleName)!}" placeholder="请输入菜单名">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">菜单样式</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   name="moduleStyle" id="moduleStyle" value="${(module.moduleStyle)!}" placeholder="请输入菜单样式">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   name="orders" id="orders" value="${(module.orders)!}" placeholder="请输入排序值">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">权限码</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-varify="required" name="optValue" id="optValue" value="${(module.optValue)!}" placeholder="请输入菜单权限码">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">菜单级别</label>
        <div class="layui-input-block">
            <#if module.grade??>
                <select name="grade">
                    <option value="0" <#if module.grade==0>selected="selected"</#if>>一级菜单</option>
                    <option value="1" <#if module.grade==1>selected="selected"</#if>>二级菜单</option>
                    <option value="2" <#if module.grade==2>selected="selected"</#if>>三级菜单</option>
                </select>
            </#if>
        </div>
    </div>

    <#if module.grade?? && module.grade==1>
        <div class="layui-form-item layui-row layui-col-xs12">
            <label class="layui-form-label">菜单url</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input userName"
                       lay-varify="required" name="url" id="url" value="${(module.url)!}" placeholder="请输入菜单url">
            </div>
        </div>
    </#if>

    <input name="parentId" type="hidden" value="${(module.parentId)!}"/>
    <input name="id" type="hidden" value="${(module.id)!}"/>
    <br/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-lg" lay-submit=""
                    lay-filter="updateModule">确认
            </button>
            <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="/crm/js/module/update.js"></script>
</body>
</html>