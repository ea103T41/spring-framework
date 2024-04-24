<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Page</title>
</head>
<body>
	<table border='1' cellpadding='2' cellspacing='2'>
		<tr>
		    <td>Name</td>
            <td>${user.name}</td>
		</tr>
		<tr>
		    <td>Email</td>
		    <td>${user.email}</td>
		</tr>
		<tr>
            <td>Role</td>
            <td>${user.role}</td>
        </tr>
	</table>
	<table>
        <tr>
            <td><a href="/spring-mvc-datasource">Home</a></td>
        </tr>
    </table>
</body>
</html>