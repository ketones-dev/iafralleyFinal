/*for time display at top*/
function display_ct() {
	document.getElementById("ct").innerHTML = Date();
}
/*for video pause on click of close button*/
function closeVideo() {
	var vid = document.getElementById("v1");
	vid.pause();
}

function closeVideo2() {
	var vid = document.getElementById("v2");
	vid.pause();
}

function closeVideo3() {
	var vid = document.getElementById("v3");
	vid.pause();
}
