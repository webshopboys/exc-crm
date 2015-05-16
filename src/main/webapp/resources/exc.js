$(document).ready(function() {
	
	initLayoutToggler();
    
});

function initLayoutToggler(){
	//alert("initLayoutToggler");
	if ($("#emptySlider").length > 0) { 
		//alert("close layout " + $("#sliderContent a.ui-layout-unit-header-icon").length);
	    $("#sliderContent a.ui-layout-unit-header-icon").click();
	}	
}

