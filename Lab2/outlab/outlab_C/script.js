var elements = document.getElementsByClassName('mypanel-title');
var length = elements.length;

// for(var i=0;i<length;i++) {
// 	elements[i].addEventListener('click',function() {
// 		// console.log(this);
// 		var panel = this.parentNode;					//panel corresponding to this title
// 		var content = this.nextElementSibling;			//content of the title
// 		var accordion = panel.parentNode;				//accordion
// 		// console.log(accordion);
// 		var el = accordion.getElementsByClassName("panel-body"); var l = el.length;		//all panels of this accordion
// 		console.log("Length of current accordion - "+String(l));		
// 		var swap = 0;
// 		for(var j=0;j<l;j++) {
// 			// console.log(el[j].style.display);
// 			if(el[j].style.display !== "none" && el[j]!=content) {
// 				swap = el[j];
// 				break;
// 			}
// 			else if(el[j].style.display !== "none" && el[j]==content) {
// 				swap = el[j];
// 			}
// 		}
// 		if(swap===0) {							//no tab is open
// 			console.log("No other element to close.");
// 			content.style.display = "block";
// 		}
// 		else if(swap!=content) {	
// 			console.log(swap);									//log the panel to close
// 			swap.style.display = "none";		
// 			content.style.display = "block";
// 		}
// 		else if(swap==content) {
// 			content.style.display = "none";
// 		}
		
		
// 	});
// }
function isNamePresent(value,classlist) {
	var length = classlist.length;
	for(var i=0;i<length;i++) {
		if(value===classlist[i])
			return true;
	}
	return false;
}

for(var i=0;i<length;i++) {
	elements[i].addEventListener('click',function() {
		// console.log(this);
		var panel = this.parentNode;					//panel corresponding to this title
		var content = this.nextElementSibling;			//content of the title
		var accordion = panel.parentNode;				//accordion
		// console.log(accordion);
		var el = accordion.getElementsByClassName("mypanel-body"); var l = el.length;		//all panels of this accordion
		console.log("Length of current accordion - "+String(l));		
		var swap = 0;
		for(var j=0;j<l;j++) {
			// console.log(el[j].style.display);
			if(isNamePresent('show',el[j].classList) && el[j]!=content) {			// some other panel is open
				swap = el[j];
				break;
			}
			else if(isNamePresent('show',el[j].classList) && el[j]==content) {				//the same panel is open, save and skip it
				swap = el[j];
			}
		}
		if(swap===0) {							//no tab is open
			console.log("No other element to close.");
			content.classList.toggle('show');
		}
		else if(swap!=content) {	
			console.log(swap);									//log the panel to close
			swap.classList.toggle("show");
			content.classList.toggle("show");
		}
		else if(swap==content) {
			content.classList.toggle("show");
		}
		
		
	});
}

function alertMe() {
	alert("Yo");
};

