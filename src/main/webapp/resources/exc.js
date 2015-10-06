$(document).ready(function() {
	
	initLayoutToggler();
	initCafeteriaExtras();
});

function initLayoutToggler(){
	//alert("initLayoutToggler");
	if ($("#emptySlider").length > 0) { 
		//alert("close layout " + $("#sliderContent a.ui-layout-unit-header-icon").length);
	    $("#sliderContent a.ui-layout-unit-header-icon").click();
	}	
}


function initCafeteriaExtras(){
	jQuery(".differentBaseData").attr("title", "Az aktuális tárolt érték és az Excelből kinyert érték eltér egymástól. Ez nem okoz gondot, sőt lehetséges az új értékkekkel frissíteni a régieket.");
}

