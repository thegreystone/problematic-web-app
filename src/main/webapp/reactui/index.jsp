<html>
<head>
<link rel="stylesheet" type="text/css" href="../style.css">
</head>
<body onLoad="loadCustomers();">
	<h2>Dynamic UI</h2>
	
	<ul id="customers">
	</ul>
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
	
	function deleteUser(id){
		const url = '/rest/customers/' + id;
		fetch(url, {
			  method: 'delete',
			  headers: {'Content-Type': 'application/json'}
			})
			.then(deleteListItem("LI" + id));
	}
  </script>
</body>
</html>