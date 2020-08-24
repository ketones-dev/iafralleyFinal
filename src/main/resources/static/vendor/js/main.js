// JavaScript Document

$(function() {
	'use strict';
	//adjsut slider height
	var winH= $(window).height(),
	 upperH= $('.upper-bar').innerHeight(),
	 navH= $('.navbar').innerHeight();
	
	$('.slider, .carousel-item').height(winH -(upperH + navH));
	





//featured work shuffle

$('.featured-work ul li').on('click',function(){
	 
	$(this).addClass('active').siblings().removeClass('active');
	console.log($(this).data('class'));
	if($(this).data('class')=== 'all'){
	$('.shuffle-images .col-md').css('opacity',1);
	
	 
	}
	else
	{
		$('.shuffle-images .col-md').css('opacity','0.4');
		$($(this).data('class')).parent().css('opacity',1);
	}
	
		
});




});

$(function() {
	'use strict';
	
	$(".number").countTo();
	
});
	
	
	