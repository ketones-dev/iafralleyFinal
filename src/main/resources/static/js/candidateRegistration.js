/**
 * 
 *@Author ketan tank
 * candidateRegistration 
 */

const http = new easyHTTP;

//getting fields
let form=document.getElementById("form-table");
let message=document.getElementById("message");
let error=document.getElementById("error");
let optcity=document.getElementById("optcity");

let passed_exam =document.getElementById('passed_exam');
let eng_passed_exam =document.getElementById('eng_passed_exam');
let dateOfBirth=document.getElementById('dateOfBirth');
let height=document.getElementById('height');


let ralley_stateSelectedValue = document.getElementById("ralleystateSelect");
let ralley_citySelectedValue = document.getElementById("ralleycitySelect");

let stateSelectedValue = document.getElementById("stateSelect");
let citySelectedValue = document.getElementById("citySelect");

let proceed= document.getElementById("proceed");


let grp_tradeSelect = document.getElementById("selectgroup");
let diploma_y=document.getElementById("diploma_y");
let vocational_v=document.getElementById("vocational_v");

let duplicateerror= document.getElementById("error2");

let restrictdistrictvalue;

window.addEventListener('load', function() {
    console.log('All assets are loaded');
    if(error.value === "error")
    	{
    	let data = ralley_stateSelectedValue.options[ralley_stateSelectedValue.selectedIndex].value;
    	toogleshowtable();
    	 http.post('getralleyallotCitiesonbasisofStateSeclected', {stateid : data} , function(err, post) {
			  if(err) {
			    console.log(err);
			  } else {
			    console.log(post);
			    let cities=JSON.parse(post);
			    createCitydropdownData(cities,"ralleystateSelect");
			    //optcity.value
			       // for ( var i = 0; i < ralley_citySelectedValue.options.length; i++ ) {
			         //   if ( ralley_citySelectedValue.options[i].value === "0" ) {
			            	ralley_citySelectedValue.options[0].selected = true;
			           //     return;
			           // }
			       // }
			    
			  }
			 });
    	 ralley_stateSelectedValue.selectedIndex=0;
    	stateSelectedValue.selectedIndex = 0;
    	alert("please check input values...");
    	window.location.reload(true);
    	}
    if(error.value !== "" && error.value !== "error")
    	{
    	alert(error.value);
    	window.location.reload(true);
    	}
    
    if(duplicateerror.value !== "")
    	{
    	ralley_stateSelectedValue.selectedIndex=0;
    	//stateSelectedValue.selectedIndex = 0;
    	alert(duplicateerror.value);
    	window.location.reload(true);
    	}
    
})


ralley_citySelectedValue.addEventListener("change", function(e) {
	document.getElementById("upload").style.display ="none";
	 let data = ralley_citySelectedValue.options[ralley_citySelectedValue.selectedIndex].value;
	 let validid=data;
	 let citytext=ralley_citySelectedValue.options[ralley_citySelectedValue.selectedIndex].text;
	 console.log(typeof data);
	 if(data !== "")
		 {
		// Create Post
		 document.getElementById("optedcityname").value = citytext;
		 
		 
		 http.post('getralleyFormOnBasisOfAdminCities', {cityid : data} , function(err, post) {
		  if(err) {
		    console.log(err);
		  } else {
		    console.log(post);
		    let cities=JSON.parse(post);
		    console.log(cities);
		    if(cities === false)
		    	{
		    	toogleshowmsg();
		    	  let messagetag="Seems like registration for selected city is full";
		    	 
		    	  document.getElementById("message").innerHTML = messagetag;
		    	}
		    else
		    	{
		    	
		    	
		    	http.post('getralleyDetailsOnBasisOfAdminCities', {cityid : data} , function(err, post) {
			   		  if(err) {
			   		    console.log(err);
			   		  } else {
			   		    console.log(post);
			   		    let ra=post;
			   		 document.getElementById("ralleyoptdetails").innerHTML= ra;
			   		 
			   		
			   		
			   		if(cities === true)
			   			{
			   			http.post('getralleyCreatedGroups', {cityid : data} , function(err, post) {
					   		  if(err) {
					   		    console.log(err);
					   		  } else {
					   		    console.log(post);
					   		    if(post === "")
					   		    	{
					   		    	document.getElementById("grptrade").style.display = "none";
					   		    	console.log('either no grop is select or city is not there');
					   		    	}
					   		    else{
					   		    let data=JSON.parse(post);
					   		 document.getElementById("grptrade").style.display = "block";
					   		 var dropdown = document.getElementById("selectgroup");
					   		
					   		 dropdown.setAttribute("required","required");
					   		
					   		 if(data.length === 1)
					   			 {
					   			var opt = document.createElement("option"); 
				   		    	opt.value = data[0].id;
				   		    	opt.text = data[0].group_name;
				   		    	dropdown.options.add(opt);
				   		    	dropdown.selectedIndex = 0;
				   		    	ShowQualificationOnSingleValue();
					   			 }
					   		 else{
					   			dropdown.innerHTML="<option value='' selected>---Select Group/trade----</option>";
					   		 for(var s=0;s<data.length;s++)
					   		    	{
					   			 
					   		    	var opt = document.createElement("option"); 
					   		    	opt.value = data[s].id;
					   		    	opt.text = data[s].group_name;
					   		    	dropdown.options.add(opt);
					   		    	}
					   		 }
					   		   // document.getElementById("grptrade").appendChild(dropdown);
					   		 if(cities === true){
					   		 http.post('getralleyValidationDetailsOnBasisOfAdminCities', {cityid : validid} , function(err, post) {
						   		  if(err) {
						   		    console.log(post);
						   		  } else {
						   		    console.log(post);
						   		    let data=JSON.parse(post);
						   		 passed_exam.setAttribute("min",data.minpassing);
						   		eng_passed_exam.setAttribute("min",data.engpassing);
						   	 passed_exam.setAttribute("max","100");
						   		eng_passed_exam.setAttribute("max","100");
						   		dateOfBirth.setAttribute("min",data.mindob);
						   		dateOfBirth.setAttribute("max",data.maxdob);
						   		height.setAttribute("min",data.height);
						   		
						   	 if(cities === true){
						   		 let data=document.getElementById("ralleyid_cust").value;
						   		 http.post('getralleyStateandCities', {rallyid : data} , function(err, post) {
							   		  if(err) {
							   		    console.log(post);
							   		  } else {
							   			console.log(post);
							   			let restvalues=JSON.parse(post);
							   			restrictdistrictvalue=restvalues;
							   			console.log(restvalues.value[1]+"and"+restvalues.value[2]);
							   			
							   			stateSelectedValue.remove(1);
							   			for(var i=0;i<1;i++)
							   				{
							   				var option=document.createElement("option");
							   			    option.value = restvalues.value[1];
							   			    option.text = restvalues.value[2];
							   				stateSelectedValue.append(option);
							   				}
							   			
							   		  }
							   		  });
						   		 
						   	 }
						   		 
						   		  }
						    	 });
					   		 }
					   		    }
					   		 
					   		  }
					    	 });
			   			}
			   		//always user at last as dependancy on first is there
			   		
			   		
			   		 
			   		  }
			    	 });
		    	
		    			    	
		    	
		    	}
		   
	    	
	    	
		  }
		 });
		 }
	 else
		 {
		 toogleshowmsg();
   	  let messagetag="Please select city";
   	 
   	  document.getElementById("message").innerHTML = messagetag;
		 }
	
	
});



proceed.addEventListener("click",function(e){
	
	 let data1 = ralley_citySelectedValue.options[ralley_citySelectedValue.selectedIndex].value;
	 let data2 = ralley_stateSelectedValue.options[ralley_stateSelectedValue.selectedIndex].value;
	 if(data1 !== "" && data2 !== "")
	 {toogleshowtable();
	 document.getElementById("perRequest").style.display = 'none';
	 document.getElementById("contextshow").style.display = 'none';
	 
	 }
	 else if(data1 === "" && data2 === ""){
		 toogleshowmsg();
	   	  let messagetag="Please select State and city";
	   	 
	   	  document.getElementById("message").innerHTML = messagetag;
	 }
	 else if(data1 === ""){
		 toogleshowmsg();
	   	  let messagetag="Please select city";
	   	 
	   	  document.getElementById("message").innerHTML = messagetag;
	 }
	 else if(data2 === ""){
		 toogleshowmsg();
	   	  let messagetag="Please select State";
	   	 
	   	  document.getElementById("message").innerHTML = messagetag;
	 }
	 
	
});



function toogleshowmsg()
{
	 message.classList.remove("message");
  	  message.classList.add("show");
  	  form.classList.remove("show");
  	  form.classList.add("form-table");
}

function toogleshowtable()
{
	 message.classList.remove("show");
	  message.classList.add("message");
	  form.classList.remove("form-table");
	  form.classList.add("show");
}


ralley_stateSelectedValue.addEventListener("change", function(e) {
	 console.log(this);
	 document.getElementById("upload").style.display ="none";
	 let data = ralley_stateSelectedValue.options[ralley_stateSelectedValue.selectedIndex].value;
	 let statetext=ralley_stateSelectedValue.options[ralley_stateSelectedValue.selectedIndex].text;
	    if(data !== "")
	    {
	    	
	    	toogleshowmsg();
	    	document.getElementById("message").innerHTML = "";
	    	
	    	document.getElementById("optedstatename").value=statetext;
	        addActivityItem(data,e.target.id);
	    }
	    else
	    	{
	    	 toogleshowmsg();
	      	  let messagetag="Please select state";
	      	$('#ralleycitySelect option:not(:first)').remove();
	      	 
	      	  document.getElementById("message").innerHTML = messagetag;
	    	}
	    //console.log(activities.value);
	});




 
 
stateSelectedValue.addEventListener("change", function(e) {
	 console.log(this);
	 let data = stateSelectedValue.options[stateSelectedValue.selectedIndex].value;
	    if(data !== "")
	    {
	       // addActivityItem(data,e.target.id);
	    	createCitydropdownData(restrictdistrictvalue.value[0],e.target.id);
	    	
	    }
	    else{
	    	removecitiesExcetFirstOption(restrictdistrictvalue.value[0],e.target.id);
	    	
	    }
	    //console.log(activities.value);
	});


grp_tradeSelect.addEventListener("change", function(e) {
	console.log(this);
	let data =grp_tradeSelect.options[grp_tradeSelect.selectedIndex].value;
	let text=grp_tradeSelect.options[grp_tradeSelect.selectedIndex].text;
	
	if(data === "")
		{
		document.getElementById("upload").style.display ="none";
		return;
		}
	
	if(data !== "")
		{
		document.getElementById("upload").style.display ="block";
		document.getElementById("groupSelectedValue").value=data;
		document.getElementById("groupSelectedText").value=text;
		if(data === "1")
			{
			document.getElementById("diploma").style.display ="inline-block";
			document.getElementById("vocational").style.display ="none";
			document.getElementById("othercourse").style.display ="none";
			diploma_y.style.display ="block";
			vocational_v.style.display="none";
			
			}
		else if(data === "2")
			{
			document.getElementById("diploma").style.display ="none";
			document.getElementById("vocational").style.display ="inline-block";
			document.getElementById("othercourse").style.display ="none";
			diploma_y.style.display ="none";
			vocational_v.style.display="block";
			}
		else{
			document.getElementById("diploma").style.display ="none";
			document.getElementById("vocational").style.display ="none";
			document.getElementById("othercourse").style.display ="inline-block";
			diploma_y.style.display ="none";
			vocational_v.style.display="block";
			
		}
		console.log(document.getElementById("groupSelectedValue").value);
		console.log(document.getElementById("groupSelectedText").value);
		}
	
});

function ShowQualificationOnSingleValue(){
	console.log(this);
	let data =grp_tradeSelect.options[grp_tradeSelect.selectedIndex].value;
	let text=grp_tradeSelect.options[grp_tradeSelect.selectedIndex].text;
	
	if(data === "")
		{
		document.getElementById("upload").style.display ="none";
		return;
		}
	
	if(data !== "")
		{
		document.getElementById("upload").style.display ="block";
		document.getElementById("groupSelectedValue").value=data;
		document.getElementById("groupSelectedText").value=text;
		if(data === "1")
			{
			document.getElementById("diploma").style.display ="inline-block";
			document.getElementById("vocational").style.display ="none";
			document.getElementById("othercourse").style.display ="none";
			diploma_y.style.display ="block";
			vocational_v.style.display="none";
			
			}
		else if(data === "2")
			{
			document.getElementById("diploma").style.display ="none";
			document.getElementById("vocational").style.display ="inline-block";
			document.getElementById("othercourse").style.display ="none";
			diploma_y.style.display ="none";
			vocational_v.style.display="block";
			}
		else{
			document.getElementById("diploma").style.display ="none";
			document.getElementById("vocational").style.display ="none";
			document.getElementById("othercourse").style.display ="inline-block";
			diploma_y.style.display ="none";
			vocational_v.style.display="block";
			
		}
		console.log(document.getElementById("groupSelectedValue").value);
		console.log(document.getElementById("groupSelectedText").value);
		}
}
 
 function addActivityItem(data,id){
	 if(id === "stateSelect"){
		 
		 console.log(data);
		 
			// Create Post
			 
			 http.post('getCitiesonbasisofStateSeclected', {stateid : data} , function(err, post) {
			  if(err) {
			    console.log(err);
			  } else {
			    console.log(post);
			    let cities=JSON.parse(post);
			    createCitydropdownData(cities,id);
			  }
			 });

			 
		 
	 }
	 else{
		 
		 console.log(data);
		 
			// Create Post
			 
			 http.post('getralleyallotCitiesonbasisofStateSeclected', {stateid : data} , function(err, post) {
			  if(err) {
			    console.log(err);
			  } else {
			    console.log(post);
			    let cities=JSON.parse(post);
			    createCitydropdownData(cities,id);
			  }
			 });
		 
	 }
	
 }
 
 
 
function createCitydropdownData(citiesArrays,id)  {
	//console.log(JSON.parse(citiesArrays).length);
	removecitiesExcetFirstOption(citiesArrays,id);
	//Add the Options to the DropDownList.
    for (var i = 0; i < citiesArrays.length; i++) {
        var option = document.createElement("option");
        console.log(citiesArrays[i].city_id);
        //Set Customer Name in Text part.
        option.text = citiesArrays[i].city;

        //Set CustomerId in Value part.
        option.value = citiesArrays[i].city_id;
        if(id === "stateSelect")
        	{
        //Add the Option element to DropDownList.
        citySelectedValue.appendChild(option);
        	}
        else{
        	ralley_citySelectedValue.appendChild(option);
        }
    }
}
	
function removecitiesExcetFirstOption(citiesArrays,id){
	
	var i;var L;
	if (id === "stateSelect") {
		L = citySelectedValue.length;
	} else {
		L = ralley_citySelectedValue.length;
	}
	   for(i = L; i > 0; i--) {
		   if(id === "stateSelect")
			   {citySelectedValue.remove(i);}
		   else
			   {ralley_citySelectedValue.remove(i)}
		   
	   }
	   
	  // citySelectedValue.append("<option value='0'>---Select City----</option>");
	
}


