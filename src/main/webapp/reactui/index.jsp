<html>
<head>
<link rel="stylesheet" type="text/css" href="../style.css">
</head>
<body onLoad="loadCustomers();">
	<h2>Dynamic UI</h2>
	<div class="row">
		<div class="column">
			<form onsubmit="return addCustomer();" name="addForm">
				<table>
					<tr>
						<td colspan="2"><b>Add New User</b></td>
					</tr>
					<tr>
						<td>Full Name:</td>
						<td><input type="text" name="fullName" id="fullName"></td>
					</tr>
					<tr>
						<td>Phone Number:</td>
						<td><input type="text" name="phoneNumber" id="phoneNumber"></td>
					</tr>
					<tr>
						<td><input type="submit" value="Submit"></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="column">
			<ul id="customers"></ul>
		</div>
	</div>
	<script>
	function createNode(element) {
		return document.createElement(element);
	}
	
	function append(parent, el) {
		return parent.appendChild(el);
	}

	function loadCustomers() {
		const ul = document.getElementById('customers');
		ul.innerHTML = "";
		const url = '/rest/customers';
		
		fetch(url).then(function(response) {
			  response.text().then(function(data) {
			    var customers = JSON.parse(data)
			    for (const s of customers) {
			    	const customerId = s.id;			    	
			    	let li = document.createElement("li");
			    	li.id = "LI" + customerId;
			    	var span = createNode('span');	    	
			    	const entry = s.fullName + " (" + s.phoneNumber + ") <a class=\"deleteLink\" onclick=\"deleteUser(\'" + customerId + "'\);\">Delete</a>";
					span.innerHTML = entry;
					append(li, span);
					append(ul, li);	
				}
			});
		})
	}
	
	function deleteListItem(elementId) {
		const ul = document.getElementById('customers');
		ul.removeChild(document.getElementById(elementId))
	}
	
	function deleteUser(id) {
		const url = '/rest/customers/' + id;
		fetch(url, {
			  method: 'delete',
			  headers: {'Content-Type': 'application/json'}
			})
			.then(deleteListItem("LI" + id));
	}
	
	function addCustomer() {
		const fullName = document.getElementById('fullName').value;
		const phoneNumber = document.getElementById('phoneNumber').value;
		const url = '/rest/customers/' + id;
		fetch(url, {
			  method: 'put',
			  headers: {'Content-Type': 'application/json'},
			  body: JSON.stringify({"fullName": fullName, "phoneNumber": phoneNumber})
			})
	}
  </script>
</body>
</html>