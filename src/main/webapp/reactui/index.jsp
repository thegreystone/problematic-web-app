<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
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
		const url = '/rest/customers';
		fetch(url)
		.then((resp) => resp.json())
		.then(function(data) {
    		let customers = data.results;
    		return customers.map(function(customer) {
				let li = createNode('li'),
				span = createNode('span');
				span.innerHTML = `${customer}`;
				append(li, span);
				append(ul, li);
			})
		})
  		.catch(function(error) {
    		console.log(error);
  		}); 
	}
	</script>
</body>
</html>