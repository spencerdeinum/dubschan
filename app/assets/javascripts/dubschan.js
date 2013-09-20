var Dubschan = Dubschan || {};

Dubschan.printMessage = function(message) {
	console.log(message);
};

Dubschan.listenToPostFeed = function(postFeedUrl) {
	postFeed = new EventSource(postFeedUrl);
	postFeed.addEventListener("message", function(msg) {
		console.log(msg);
	}, false);
};

$(function() {
	var postFeedUrl = $('#post-feed-url').val();
	if(postFeedUrl) {
		Dubschan.listenToPostFeed(postFeedUrl);
	}
});
