<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Interface!</title>
    <style>

        body {
            margin: 40px auto;
            font-family: 'trebuchet MS', 'Lucida sans', Arial;
            font-size: 14px;
            color: #444;
        }

        table {
            *border-collapse: collapse; /* IE7 and lower */
            border-spacing: 0;
            width: 100%;
        }

        .bordered {
            border: solid #ccc 1px;
            -moz-border-radius: 6px;
            -webkit-border-radius: 6px;
            border-radius: 6px;
            -webkit-box-shadow: 0 1px 1px #ccc;
            -moz-box-shadow: 0 1px 1px #ccc;
            box-shadow: 0 1px 1px #ccc;
        }

        .bordered tr:hover {
            background: #fbf8e9;
            -o-transition: all 0.1s ease-in-out;
            -webkit-transition: all 0.1s ease-in-out;
            -moz-transition: all 0.1s ease-in-out;
            -ms-transition: all 0.1s ease-in-out;
            transition: all 0.1s ease-in-out;
        }

        .bordered td, .bordered th {
            border-left: 1px solid #ccc;
            border-top: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        .bordered th {
            background-color: #dce9f9;
            background-image: -webkit-gradient(linear, left top, left bottom, from(#ebf3fc), to(#dce9f9));
            background-image: -webkit-linear-gradient(top, #ebf3fc, #dce9f9);
            background-image: -moz-linear-gradient(top, #ebf3fc, #dce9f9);
            background-image: -ms-linear-gradient(top, #ebf3fc, #dce9f9);
            background-image: -o-linear-gradient(top, #ebf3fc, #dce9f9);
            background-image: linear-gradient(top, #ebf3fc, #dce9f9);
            -webkit-box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
            -moz-box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
            box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
            border-top: none;
            text-shadow: 0 1px 0 rgba(255, 255, 255, .5);
        }

        .bordered td:first-child, .bordered th:first-child {
            border-left: none;
        }

        .bordered th:first-child {
            -moz-border-radius: 6px 0 0 0;
            -webkit-border-radius: 6px 0 0 0;
            border-radius: 6px 0 0 0;
        }

        .bordered th:last-child {
            -moz-border-radius: 0 6px 0 0;
            -webkit-border-radius: 0 6px 0 0;
            border-radius: 0 6px 0 0;
        }

        .bordered th:only-child {
            -moz-border-radius: 6px 6px 0 0;
            -webkit-border-radius: 6px 6px 0 0;
            border-radius: 6px 6px 0 0;
        }

        .bordered tr:last-child td:first-child {
            -moz-border-radius: 0 0 0 6px;
            -webkit-border-radius: 0 0 0 6px;
            border-radius: 0 0 0 6px;
        }

        .bordered tr:last-child td:last-child {
            -moz-border-radius: 0 0 6px 0;
            -webkit-border-radius: 0 0 6px 0;
            border-radius: 0 0 6px 0;
        }
    </style>
    <link rel="icon" type="image/x-icon" href="../ico/favicon.ico"/>
</head>
<body>
<h4>??????, ???????????????????????????!</h4>
<h4>????????????</h4>
<div style="border: solid cadetblue;font-size: 14px;">
    <p>1??????????????????POST??????,?????????Header?????????Content-Type application/json;charset=UTF-8</p>
    <p>2?????????????????????"??????"??????url??????</p>
    <p>3???????????????JSON???????????????[]????????????????????????{}</p>
    <p>4???????????????(1):
    <div style="text-indent:4em;">
        ????????????com.shiqiao.xxx.facade.UserFacade.getById(Long)<br>
    </div>
    <div style="text-indent:8em;">
        ?????????:http://localhost:21880/xxx/rpc/call/com.shiqiao.xxx.facade.UserFacade/getById/<br>
    </div>
    <div style="text-indent:8em;">
        ????????????:[1]
    </div>
    </p>
    <p>5???????????????(2):
    <div style="text-indent:4em;">
        ????????????com.shiqiao.xxx.facade.UserFacade.getById(com.xxx.RequestObj1, com.xxx.RequestObj2)<br>
    </div>
    <div style="text-indent:8em;">
        ?????????:http://localhost:21880/xxx/rpc/call/com.shiqiao.xxx.facade.UserFacade/getById__RequestObj1__RequestObj2<br>
    </div>
    <div style="text-indent:8em;">
        ????????????:[{"obj1Name":"shiqiao", "obj1Age":1}, {"obj2Name":"ecf", "obj2DomainList": ["xxxcore", "xxxmtp",
        "xxxtc"], "obj2Subobj": {"id": 123}}]
    </div>
    </p>
    <p>6???????????????????????????????????????????????????????????????????????????????????????????????????</p>
</div>
<#list localProvider.classNames as className>
    <#assign details=localProvider.localProviderDetailsMap[className]>
    <table class="bordered">
        <thead>
        <tr style="font-size: 14px;">
            <th>??????</th>
            <th>??????</th>
            <th>??????</th>
        </tr>
        </thead>
        <tbody style="font-size: 14px;">
        <#list details.restfullToMethodDesc?keys as methodName>
            <tr>
                <td>${className}</td>
                <td>
                    <a href="${details.restfullToMethodDesc[methodName][2]?replace('/call/', '/get/')}">${details.restfullToMethodDesc[methodName][3]?html}</a>
                </td>
                <td>${details.restfullToMethodDesc[methodName][2]}</td>
            </tr>
        </#list>
        </tbody>
        </br>
    </table>
</#list>
</body>
</html> 