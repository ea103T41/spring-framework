<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Home Page</title>
</head>
<body>
    <h2>Hello, this is the home page.</h2>
	<form method="get" action="user">
        <div>
            <div>
                <label for="username">Username: </label>
                <input id="username" name="username" value="${param.username}"/>
                <button>Search</button>
            </div>
        </div>
    </form>
    <table>
        <tr>
            <td style="font-style: italic; color: red;">${requestScope.errors}</td>
        </tr>
    </table>
</body>
</html>