function checkUnique(form) {
	url = "[[@{/products/check_unique}]]"
	catId = $("#id").val();
	catName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();

	params = { id: catId, name: catName, _csrf: csrfValue };

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "DuplicatedName") {
			showModalDialog("Warning", "Product uses this name has existed!");
		} else {
			showModalDialog("Error", "Unknown response from server!");
		}
	}).fail(function() {
		showModalDialog("Error", "Could not connect to the server!");
	});
	return false;
}