<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="se.hirt.examples.problematicwebapp.data.Customer"%>
<%@page import="java.util.Collection"%>
<%@page import="se.hirt.examples.problematicwebapp.data.DataAccess"%>
<!-- JSTL would be nice. Anyone know how to get it to work with a simple maven dependency? -->
<html>
<head>
  <link rel="stylesheet" type="text/css" href="../style.css">
</head>
<body>
	<h2>Simple UI!</h2>
</body>
<% List<Customer> customers = new ArrayList<>(DataAccess.getAllCustomers()); %>
<table class="customerTable">
	<tr>
		<th class="customerTable">Customer ID</th>
		<th class="customerTable">Customer Name</th>
		<th class="customerTable">Customer Phone</th>
	</tr>
<% for (Customer c : customers) { %>
	<tr>
		<td class="customerTable"><%=c.getCustomerId() %></td>
		<td class="customerTable"><%=c.getFullName() %></td>
		<td class="customerTable"><%=c.getPhoneNumber() %></td>
	</tr>
<% } %>

</table>
</html>
