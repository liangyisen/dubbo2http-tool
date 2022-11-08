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
    <link rel="stylesheet" type="text/css" href="https://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css">
    <link href="/jquery.json-viewer.css" rel="stylesheet" type="text/css" />
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script src="/jquery.json-viewer.js"></script>
    <script>
        $(document).ready(
            function () {
                $("#sendButton").click(function () {
                    var data = $("#methodDetails").val()
                    var url = $("#submitForm").attr("action")
                    $.ajax({
                        url: url,
                        type: "post",
                        data: data,
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        dataType: "json",
                        complete: function (data) {
                            var options = {
                                //为Key添加双引号
                                withQuotes: true
                            };
                            $('#result').hide();
                              var input = eval('(' + JSON.stringify(data.responseJSON) + ')');
                            $('#resultJson').jsonViewer(input, options);
                        }
                    })
                })
            }
        )

    </script>
</head>
<body>
<h4>你好</h4>
<h4>请求的细节是!</h4>
<div id="div1" style="border: solid gray;font-size: 14px;">
    <p>1，请求的参数应该是</p>
    ${details[0]}
    <p>2，返回值是</p>
    ${details[1]}
</div>
<h4>
    调用接口
</h4>
<div id="div2" style="border: solid red;font-size: 14px;">
    <p>1，请修改参数</p>
    <form id="submitForm" action="${details[2]?html}" method="post">
        <label>
            <textarea id="methodDetails">${details[0]?html}</textarea>
        </label>
        <input id="sendButton" type="button" value="发送请求">
    </form>
    <p>2, 请求地址</p>
    <div>${details[2]?html}</div>
    <p>3，返回结果如下</p>
    <div style="margin-top: 1em;">
        <div>
            <pre id="result">${details[1]}</pre>
            <pre id="resultJson"></pre>
        </div>
    </div>
</div>
</body>
</html> 