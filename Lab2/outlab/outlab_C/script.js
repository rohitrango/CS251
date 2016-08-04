var elements = document.getElementsByClassName('panel-title');
var length = elements.length;

for(var i=0;i<length;i++) {
	elements[i].addEventListener('click',function() {
		// console.log(this);
		var panel = this.parentNode;					//panel corresponding to this title
		var content = this.nextElementSibling;			//content of the title
		var accordion = panel.parentNode;				//accordion
		// console.log(accordion);
		var el = accordion.getElementsByClassName("panel-body"); var l = el.length;		//all panels of this accordion
		console.log("Length of current accordion - "+String(l));		
		var swap = 0;
		for(var j=0;j<l;j++) {
			// console.log(el[j].style.display);
			if(el[j].style.display !== "none") {
				swap = el[j];
				break;
			}
		}
		if(swap===0 || swap==content) {							//clicking on the same element again
			console.log("No other element to close.");
		}
		else {	
			console.log(swap);									//log the panel to close
			swap.style.display = "none";		
		}
		
		content.style.display = "block";
		
	});
}

function alertMe() {
	alert("Yo");
};

