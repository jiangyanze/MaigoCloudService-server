<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MaigoCloudSerivce</title>
</head>
<body>
    <h1>MaigoCloudSerivce Console</h1>
    <form action="push" method="post">
        <input type="radio" name="pushType" value="notification" checked="checked" /> Notification
        <input type="radio" name="pushType" value="message" /> TransparentMessage
        <br/><br/>

        target: <input type="text" name="target" />
        <br/>
        <input type="radio" name="targetType" value="username" checked="checked" /> Username
        <input type="radio" name="targetType" value="alias" /> Alias

        <br/><br/>
        title: <input type="text" name="title" />
        <br/>
        content: <input type="text" name="content" />
        <br/>
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
