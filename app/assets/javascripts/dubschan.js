var Dubschan = Dubschan || {};

Dubschan.printMessage = function(message) {
	console.log(message);
};

Dubschan.listenToPostFeed = function(postFeedUrl, eventName) {
	postFeed = new EventSource(postFeedUrl);
	postFeed.addEventListener(eventName, Dubschan.printMessage, false);
};

$(function() {
	var postFeedUrl = $('#post-feed-url').val();
	var eventName = $('#post-event-name').val();
	if(postFeedUrl && eventName) {
		Dubschan.listenToPostFeed(postFeedUrl, eventName);
	}
});
