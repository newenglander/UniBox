$(document).ready(function() {
	app.initialize();
});

$(window).load(function() {
	console.log("window loaded");
});

function getChatConnection() {
	console.log("document loaded");
};

function scrollBottom(element) {
	var height = element[0].scrollHeight;
	element.scrollTop(height);
}
