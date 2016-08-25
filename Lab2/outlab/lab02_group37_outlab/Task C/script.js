var elements = document.getElementsByClassName('mypanel-title');
var length = elements.length;

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
		var panel = this.parentNode;					//panel corresponding to this title
		var content = this.nextElementSibling;			//content of the title
		var accordion = panel.parentNode;				//accordion
		var el = accordion.getElementsByClassName("mypanel-body"); var l = el.length;		//all panels of this accordion
		var swap = 0;
		for(var j=0;j<l;j++) {
			if(isNamePresent('show',el[j].classList) && el[j]!=content) {			// some other panel is open
				swap = el[j];
				break;
			}
			else if(isNamePresent('show',el[j].classList) && el[j]==content) {				//the same panel is open, save and skip it
				swap = el[j];
			}
		}
		if(swap===0) {							//no tab is open
			content.classList.toggle('show');
		}
		else if(swap!=content) {
			swap.classList.toggle("show");
			content.classList.toggle("show");
		}
		else if(swap==content) {
			content.classList.toggle("show");
		}


	});
}

