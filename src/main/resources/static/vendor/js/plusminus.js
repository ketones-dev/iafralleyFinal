var selectIds =$('#collapseOne,#collapseTwo,#collapseThree,#collapseFour,#collapseFive,#collapseSix,#collapseSeven,#collapseEight,#collapseNine,#collapseTen,#collapseEleven,#collapseTwleve,#collapseThirteen,#collapseFourteen,#collapseFifteen,#collapseSixteen,#collapseSeventeen,#collapseEighteen,#collapseNineteen,#collapseTwenty,#collapseTwentyone,#collapseTwentytwo,#collapseTwentythree,#collapseTwentyfour,#collapseTwentyfive,#collapseTwentysix,#collapseTwentyseven,#collapseTwentyeight');
$(function ($) {
    selectIds.on('show.bs.collapse hidden.bs.collapse', function () {
        $(this).prev().find('.glyphicon').toggleClass('glyphicon-plus glyphicon-minus');
    })

    


});
