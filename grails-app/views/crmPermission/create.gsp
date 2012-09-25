<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'crmNamedPermission.label', default: 'Named Permission')}"/>
    <title><g:message code="crmNamedPermission.create.title" args="[entityName]"/></title>
</head>

<body>

<crm:header title="crmNamedPermission.create.title" args="[entityName]"/>

<div class="row-fluid">
    <div class="span9">

        <g:hasErrors bean="${crmNamedPermission}">
            <crm:alert class="alert-error">
                <ul>
                    <g:eachError bean="${crmNamedPermission}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                error="${error}"/></li>
                    </g:eachError>
                </ul>
            </crm:alert>
        </g:hasErrors>

        <g:form class="form-horizontal" action="create">

            <f:with bean="crmNamedPermission">
                <f:field property="name" input-autofocus=""/>
            </f:with>

            <div class="control-group">
                <label class="control-label">Behörighet</label>

                <div class="controls">
                    <input type="text" name="permissions" maxlength="255" class="span9" value=""/><br/>
                </div>
            </div>
            <div class="form-actions">
                <crm:button visual="primary" icon="icon-ok icon-white" label="crmNamedPermission.button.create.label"/>
            </div>

        </g:form>
    </div>

    <div class="span3">
        <crm:submenu/>
    </div>
</div>

</body>
</html>
