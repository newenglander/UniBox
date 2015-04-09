$(document).ready(function() {
	$("#loginForm").submit(function() {
		$("#password").val(Base64.encode($("#passwordHolder").val()));
		return true;
	});
});