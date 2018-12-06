<b>Exmo</b>
<#if user??>
    <#if user.balances??>
    <i>balance:</i>
    <#list user.balances?keys as key>
        <#assign value = user.balances[key]>
        <#if (value > 0.00001) >
            ${key} = ${value}
        </#if>
    </#list>
    </#if>

    <#if user.reserved??>
    <i>reserved:</i>
    <#list user.reserved?keys as key>
        <#assign value = user.reserved[key]>
        <#if (value > 0.00001) >
            ${key} = ${value}
        </#if>
    </#list>
    </#if>
</#if>
<#if error??>
<i>Missing yours api keys</i>

Please, add keys in the format:
key:API_KEY
secret:API_SECRET
</#if>