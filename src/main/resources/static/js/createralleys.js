/**
 * Create ralley slotwise logic
 */
const http = new easyHTTP;

//getting fields

let stateSelectedValue = document.getElementById("stateSelect");
let citySelectedValue = document.getElementById("citySelect");
let start_date=document.getElementById("start_date");
let end_date=document.getElementById("end_date");
let optcity=document.getElementById("optcity");
let ralley_id=document.getElementById("ralley_id");
let state_name=document.getElementById("statename");
let city_name=document.getElementById("cityname");
let min_dob=document.getElementById("min_dob");
let max_dob=document.getElementById("max_dob");
let check_input=document.getElementsByName("ralleyForGroup");
let NoOfDays=document.getElementById("noofDays");

window.addEventListener('load', function() {
	 let data = stateSelectedValue.options[stateSelectedValue.selectedIndex].value;
	 state_name.value = stateSelectedValue.options[stateSelectedValue.selectedIndex].text;
    console.log('All assets are loaded');
    
    if(ralley_id.value !== "")
    	{
    	start_date.readOnly =true;
    	end_date.readOnly =true;
    	}
    
 http.post('getCitiesonbasisofStateSeclected', {stateid : data} , function(err, post) {
			  if(err) {
			    console.log(err);
			  } else {
			    console.log(post);
			    let cities=JSON.parse(post);
			    createCitydropdownData(cities);
			    console.log(optcity.value);
			        for ( var i = 0; i < citySelectedValue.options.length; i++ ) {
			            if ( citySelectedValue.options[i].value === optcity.value ) {
			            	citySelectedValue.options[i].selected = true;
			                return;
			            }
			        }
			    
			  }
});
});



citySelectedValue.addEventListener("change", function() {
	
	let data = citySelectedValue.options[citySelectedValue.selectedIndex].text;
	
	city_name.value =data;
	 
	
});

stateSelectedValue.addEventListener("change", function() {
	 console.log(this);
	 let data = stateSelectedValue.options[stateSelectedValue.selectedIndex].value;
	 state_name.value = stateSelectedValue.options[stateSelectedValue.selectedIndex].text;
	    if(data.text !== "0")
	    {
	        addActivityItem(data);
	    }
	    //console.log(activities.value);
	});

function addActivityItem(data){
	 console.log(data);
	 
	// Create Post
	 
	 http.post('getCitiesonbasisofStateSeclected', {stateid : data} , function(err, post) {
	  if(err) {
	    console.log(err);
	  } else {
	    console.log(post);
	    let cities=JSON.parse(post);
	    createCitydropdownData(cities);
	  }
	 });

	 
}

function createCitydropdownData(citiesArrays)  {
	//console.log(JSON.parse(citiesArrays).length);
	removecitiesExcetFirstOption(citiesArrays);
	//Add the Options to the DropDownList.
   for (var i = 0; i < citiesArrays.length; i++) {
       var option = document.createElement("option");
       console.log(citiesArrays[i].city_id);
       //Set Customer Name in Text part.
       option.text = citiesArrays[i].city;

       //Set CustomerId in Value part.
       option.value = citiesArrays[i].city_id;

       //Add the Option element to DropDownList.
       citySelectedValue.appendChild(option);
   }
}
	
function removecitiesExcetFirstOption(citiesArrays){
	
	var i, L = citySelectedValue.length;
	   for(i = L; i > 0; i--) {
		   citySelectedValue.remove(i);
	   }
	   
	  // citySelectedValue.append("<option value='0'>---Select City----</option>");
	
}







end_date.addEventListener("input",function(){
	if(end_date.value === "")
		{
		alert("Please select Start Date");
		return;
		}
	
	var start_jsdate = new Date(start_date.value);
	var end_jsdate = new Date(end_date.value);

	if ( !!start_jsdate.valueOf() ) { // Valid date
	    year = start_jsdate.getFullYear();
	    month = start_jsdate.getMonth();
	    day = start_jsdate.getDate();
	}
	console.log(year+''+month+''+day);



	const oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
	const firstDate = new Date(start_jsdate.getFullYear(),start_jsdate.getMonth() ,start_jsdate.getDate());
	const secondDate =new Date(end_jsdate.getFullYear(),end_jsdate.getMonth() ,end_jsdate.getDate());

	const diffDays = Math.round(Math.abs((firstDate - secondDate) / oneDay));
	console.log(diffDays);
	let dif=diffDays;
	NoOfDays.value=dif+1;
	addRow(diffDays,start_jsdate)
	
	
	
	//callCreateSlotForm(start_jsdate,end_jsdate,diffDays);
});







 function addRow(diffdays,start_date) {
    let listName = 'ralleydaywiseSlot'; //list name in Ralleydetails.class
    let fieldsNames = ['day_date', 'time_of_reporting', 'no_of_intake']; //field names from ralleydaywiseSlot.class
    let clear=document.getElementById("table-slot1");
	clear.innerHTML = "";
	
	callCreateSlotForm();
	
	for(var i=0;i<=diffdays;i++){
    let row = document.createElement('tr');
    row.classList.add('row', 'item');

    fieldsNames.forEach((fieldName) => {
        let col = document.createElement('td');
        col.classList.add('col', 'form-group');
      
let input = document.createElement('input');
       
    if(fieldName === 'day_date')
    { 
    	input.type = 'date';
    	let newstartdate=new Date(start_date);
    	let date=new Date(newstartdate.setDate(start_date.getDate()+i));
    	console.log(start_date.getDate());
    	input.readOnly =true;
    	input.value = formatDateToString(date) ;
    }
     else if(fieldName === 'time_of_reporting')
	{ input.type = 'time';}
	else if(fieldName === 'no_of_intake')
	{ input.type = 'number';}
        
        input.classList.add('form-control');
        input.id = listName + i + '.' + fieldName;
        input.setAttribute('name', listName + '[' + i+ '].' + fieldName);
        input.setAttribute("required","true");

        col.appendChild(input);
        row.appendChild(col);
    });

    
   // console.log(start_date.setDate(start_date.getDate()+1));
    
    document.getElementById('table-slot1').appendChild(row);
}
}

function callCreateSlotForm()
{
	
	//create element no basis of days
	
	let d=["Days","Time-of-reporting","intake count"];
	generateTableHead(d);
	//generateTable(table,diffDays,start_date);
	
	
	
	}

function generateTableHead(data) {
let a=document.getElementById("table-slot1");
	
	
	  let row = document.createElement('tr');
	  row.classList.add("row" ,"item");
	  for (let key of data) {
	    let td = document.createElement("td");
	    td.classList.add("col","form-group");
	    let text = document.createTextNode(key);
	    td.appendChild(text);
	    row.appendChild(td);
	  }
	  a.append(row);
	}

/*function generateTable(table, data,start_date) {
	let newstartdate=new Date();
	//let genrateblock=document.createElement("th:block");
	//genrateblock.setAttribute("th:each","ralley : ${ralleyDetails.ralleydaywiseSlot}");
	  for (var i=0;i<=data;i++) {
	    let row = table.insertRow();
	    
	      let cell1 = row.insertCell();
	      let text1 = document.createElement("input");
	      text1.setAttribute("type","date");
	      text1.setAttribute("th:value","${ralley.day_date}");
	      text1.setAttribute("th:name","day_date");
	      text1.setAttribute("class","form-control");
	     
	      let id= "day_date"+i;
	      console.log(id);
	      text1.setAttribute("id",id);
	      let date=new Date(newstartdate.setDate(start_date.getDate()+i));
	      
	      
	     
	      text1.value = formatDateToString(date) ;
	     // console.log(start_date.setDate(start_date.getDate()+1));
	      cell1.appendChild(text1);
	      
	      let cell2 = row.insertCell();
	      let text2 = document.createElement("input");
	      text2.setAttribute("type","time");
	      text2.setAttribute("th:value","${ralley.time_of_reporting}");
	      text2.setAttribute("th:name","time_of_reporting");
	      text2.setAttribute("class","form-control");
	      
	      cell2.appendChild(text2);
	      
	      let cell3 = row.insertCell();
	      let text3 = document.createTextNode("Slot-"+i);
	      cell3.appendChild(text3);
	      
	      let cell4 = row.insertCell();
	      let text4 = document.createElement("input");
	      text4.setAttribute("type","number");
	      text4.setAttribute("min","1");
	      text4.setAttribute("th:value","${ralley.no_of_intake}");
	      text4.setAttribute("th:name","no_of_intake");
	      
	      cell4.appendChild(text4);
	      
	      
	    
	  }
	}*/

function formatDateToString(date){
	   // 01, 02, 03, ... 29, 30, 31
	   var dd = (date.getDate() < 10 ? '0' : '') + date.getDate();
	   // 01, 02, 03, ... 10, 11, 12
	   var MM = ((date.getMonth() + 1) < 10 ? '0' : '') + (date.getMonth() + 1);
	   console.log(MM);
	   // 1970, 1971, ... 2015, 2016, ...
	   var yyyy = date.getFullYear();
	   console.log(yyyy + "-" + MM + "-" + dd);
	   // create the format you want
	   return yyyy + "-" + MM + "-" + dd;
	}


document.getElementById('form').addEventListener('submit', function(evt){
	var d1=new Date(start_date.value);
	var d2=new Date(end_date.value);
	var dob1=new Date(min_dob.value);
	var dob2=new Date(max_dob.value);
	var check=false;
	console.log(d1>d2 );
	if(d1>d2 || dob1>dob2){
		evt.preventDefault();
		
		alert("error!...Please check dates filed...");
		
	}
	
	for(var i=0; i<check_input.length; i++){
		   if((check_input[i].checked))
		     {
			   check=true;
			   break;
				
		     }
		   else{
			   check=false;
		   }
	}
	if(check == false)
		{
		evt.preventDefault();
		
		alert("error!...Please check group/trade ...");
		}
	
	
	
	      
	   
    
    
});

