function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	$("#deleteButton").attr("href", link.attr("href"));	
	$("#modalBody").text("Are you sure you want to delete this "
							 + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal();	
}

function clearFilter() {
	window.location = moduleURL;	
}
