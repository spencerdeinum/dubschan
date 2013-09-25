var Dubschan = Dubschan || {};

Dubschan.printMessage = function(message) {
	console.log(message);
};

Dubschan.appendMessage = function(message) {
	var newPost = JSON.parse(message.data);
	console.log(newPost);
	$('.post:last').after(newPost.html);
};

Dubschan.listenToPostFeed = function(postFeedUrl) {
	postFeed = new EventSource(postFeedUrl);
	postFeed.addEventListener("message", Dubschan.printMessage, false);
	postFeed.addEventListener("message", Dubschan.appendMessage, false);
};

$(function() {
	var postFeedUrl = $('#post-feed-url').val();
	if(postFeedUrl) {
		Dubschan.listenToPostFeed(postFeedUrl);
	}
});
