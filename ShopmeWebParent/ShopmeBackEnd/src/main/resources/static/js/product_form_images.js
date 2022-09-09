var extraImagesCount = 0;

$(document).ready(function() {
	$("buttonCancel").on("click", function() {
		window.location = moduleURL;
	});

	$("input[name='extraImage']").each(function(index) {
		extraImagesCount++;
		console.log("!");
		$(this).change(function() {

			console.log("??");
			showExtraImageThumbnail(this, index);
		});
	});
	
	$("a[name='linkRemoveExtraImage']").each(function(index){
		$(this).click(function(){
			removeExtraImage(index);
		})
	})
});


function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	var reader = new FileReader();

	reader.onload = function(e) {
		console.log("?");
		$("#extraThumbnail" + index ).attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
	
	if(index >= extraImagesCount - 1){
		addNextExtraImageSection(index + 1);
	}
}

function checkFileSize(fileInput){
	fileSize = fileInput.files[0].size;
		if (fileSize > 1024000) { // fileSize can not bigger than 1 mb
			fileInput.setCustomValidity("Sorry, fileSize can not bigger than 1 mb. Please try again.");
			fileInput.reportValidity();
			return false;
		} else {
			fileInput.setCustomValidity("");
			showExtraImageThumbnail(fileInput);
			return true;
		}
}

function addNextExtraImageSection(index) {
	htmlExtraImage = `
		<div class="col border m-3 p-2" id="divExtraImage${index}">
			<div id="extraImageHeader${index}"><label>Extra Image #${index + 1}:</label></div>
			<div class="m-2">
				<img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid"
					src="${defaultImageThumbnailSrc}"/>
			</div>
			<div>
				<input type="file" name="extraImage"
					onchange="showExtraImageThumbnail(this, ${index})" 
					accept="image/png, image/jpeg" />
			</div>
		</div>	
	`;
	
	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-red float-right"
			href="javascript:removeExtraImage(${index - 1})" 
			title="Remove this image"></a>
	`;
	
	$("#divProductImages").append(htmlExtraImage);
	
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	
	extraImagesCount++;
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
}