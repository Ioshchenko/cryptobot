<b>Exmo</b>
<i>balance:</i>
<#if user.balances??>
<#list user.balances?keys as key>
    <#assign value=user.balances[key]>
    <#if value > 0.00001 >
        ${key} = ${value}
    </#if>
</#list>
</#if>