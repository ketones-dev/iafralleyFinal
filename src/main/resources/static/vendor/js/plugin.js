var myButton=document.getElementById('goup');
 var fixed = false;

$(document).scroll(function() {
	'use strict';
	
        if ($(this).scrollTop() > 250) {
            if (!fixed) {
                fixed = true;
                // $('goup').css({position:'fixed', display:'block'});
                $('#goup').show("slow", function() {
                    $('#goup').css({
                        position: 'fixed',
                        display: 'block'
                    });
                });
            }
        } else {
            if (fixed) {
                fixed = false;
                $('#goup').hide("slow", function() {
                    $('#goup').css({
                        display: 'none'
                    });
                });
            }
        }
		
	});
	myButton.onclick=function(){
		'use strict';
		$("html,body").animate({scrollTop:0},600);
		
	};
