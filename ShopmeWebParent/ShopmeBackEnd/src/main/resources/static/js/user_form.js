function checkEmailUnique(form) {
	url = "[[@{/users/check_email}]]"
	userId = $("#id").val();
	userEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();

	params = { id: userId, email: userEmail, _csrf: csrfValue };

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showModalDialog("Warning", "User for this Email existed!");
		} else {
			showModalDialog("Error", "Unknown response from server!");
		}
	}).fail(function() {
		showModalDialog("Error", "Could not connect to the server!");
	});
	return false;
}